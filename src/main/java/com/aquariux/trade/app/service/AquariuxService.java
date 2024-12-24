package com.aquariux.trade.app.service;

import com.aquariux.trade.app.dto.TradeRequest;
import com.aquariux.trade.app.entity.CryptoPair;
import com.aquariux.trade.app.entity.TransactionHistory;

import java.util.List;
import java.util.Optional;

public interface AquariuxService {

    List<CryptoPair> getAllPrices();

    Optional<CryptoPair> getPriceByPair(String pairName);

    String trade(TradeRequest tradeRequest);

    List<TransactionHistory> getHistory (String userName);

    Double getWallletBalance (String userName);
}
