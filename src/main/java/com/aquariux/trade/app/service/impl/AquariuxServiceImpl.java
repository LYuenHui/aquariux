package com.aquariux.trade.app.service.impl;

import com.aquariux.trade.app.constants.Constants;
import com.aquariux.trade.app.dto.TradeRequest;
import com.aquariux.trade.app.entity.CryptoPair;
import com.aquariux.trade.app.entity.Trader;
import com.aquariux.trade.app.entity.TransactionHistory;
import com.aquariux.trade.app.exceptions.ErrorType;
import com.aquariux.trade.app.exceptions.ErrorUtils;
import com.aquariux.trade.app.repository.CryptoPairRepository;
import com.aquariux.trade.app.repository.TransactionHistoryRepository;
import com.aquariux.trade.app.repository.TraderRepository;
import com.aquariux.trade.app.service.AquariuxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AquariuxServiceImpl implements AquariuxService {

    @Autowired
    private CryptoPairRepository cryptoPairRepository;

    @Autowired
    private TraderRepository traderRepository;

    @Autowired
    private TransactionHistoryRepository transactionRepository;

    public List<CryptoPair> getAllPrices() {
        return cryptoPairRepository.findAll();
    }

    public Optional<CryptoPair> getPriceByPair(String pairName) {
        return cryptoPairRepository.findByPairName(pairName);
    }


    public String trade(TradeRequest tradeRequest) {
        String orderType = tradeRequest.getOrderType();
        Double quantity = tradeRequest.getQuantity();
        String cryptoPair = tradeRequest.getCryptoPair();
        Trader trader = traderRepository.findByUsername(tradeRequest.getUserName())
                .orElseThrow(() -> ErrorUtils.buildAPIExceptionMessage(ErrorType.USER_NOT_FOUND, "User not found"));

        CryptoPair pair = cryptoPairRepository.findByPairName(tradeRequest.getCryptoPair())
                .orElseThrow(() -> ErrorUtils.buildAPIExceptionMessage(ErrorType.CRYPTO_PAIR_NOT_SUPPORTED, "Crypto pair not supported"));

        Double price = orderType.equalsIgnoreCase(Constants.BUY) ? pair.getAskPrice() : pair.getBidPrice();
        Double totalCost = price * tradeRequest.getQuantity();

        if (orderType.equalsIgnoreCase(Constants.BUY)) {
            if (trader.getWalletBalance() < totalCost) {
                throw ErrorUtils.buildAPIExceptionMessage(ErrorType.INSUFFICIENT_BALANCE, "Insufficient balance");
            }

            trader.setWalletBalance(trader.getWalletBalance() - totalCost);

            if (cryptoPair.equalsIgnoreCase(Constants.PAIR_ETHUSDT)) {
                trader.setEthBalance(trader.getEthBalance() + quantity);
            } else if (cryptoPair.equalsIgnoreCase(Constants.PAIR_BTCUSDT)) {
                trader.setBtcBalance(trader.getBtcBalance() + quantity);
            } else {
                throw ErrorUtils.buildAPIExceptionMessage(ErrorType.CRYPTO_PAIR_NOT_SUPPORTED, "Crypto pair not supported");
            }
        }
        else if (orderType.equalsIgnoreCase(Constants.SELL)) {
            if (cryptoPair.equalsIgnoreCase(Constants.PAIR_ETHUSDT)) {
                if (trader.getEthBalance() < quantity) {
                    throw ErrorUtils.buildAPIExceptionMessage(ErrorType.INSUFFICIENT_BALANCE, "Insufficient ETH balance");
                }
                trader.setEthBalance(trader.getEthBalance() - quantity);
            } else if (cryptoPair.equalsIgnoreCase(Constants.PAIR_BTCUSDT)) {
                if (trader.getBtcBalance() < quantity) {
                    throw ErrorUtils.buildAPIExceptionMessage(ErrorType.INSUFFICIENT_BALANCE, "Insufficient BTC balance");
                }
                trader.setBtcBalance(trader.getBtcBalance() - quantity);
            } else {
                throw ErrorUtils.buildAPIExceptionMessage(ErrorType.CRYPTO_PAIR_NOT_SUPPORTED, "Crypto pair not supported");
            }

            trader.setWalletBalance(trader.getWalletBalance() + totalCost);
        }


        traderRepository.save(trader);

        TransactionHistory transaction = new TransactionHistory();
        transaction.setUserId(trader.getId());
        transaction.setCryptoPair(tradeRequest.getCryptoPair());
        transaction.setOrderType(orderType);
        transaction.setQuantity(tradeRequest.getQuantity());
        transaction.setPrice(price);
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepository.save(transaction);

        return "Trade successful";
    }

    @Override
    public List<TransactionHistory> getHistory(String userName) {
        Trader user = traderRepository.findByUsername(userName)
                .orElseThrow(() -> ErrorUtils.buildAPIExceptionMessage(ErrorType.USER_NOT_FOUND, "User not found"));


        return transactionRepository.findByUserId(user.getId());
    }

    @Override
    public Double getWallletBalance(String userName) {
        Trader trader = traderRepository.findByUsername(userName)
                .orElseThrow(() -> ErrorUtils.buildAPIExceptionMessage(ErrorType.USER_NOT_FOUND, "User not found"));

        return trader.getWalletBalance();
    }
}
