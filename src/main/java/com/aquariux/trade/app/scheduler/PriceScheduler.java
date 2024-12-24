package com.aquariux.trade.app.scheduler;

import com.aquariux.trade.app.constants.Constants;
import com.aquariux.trade.app.dto.BinanceResponse;
import com.aquariux.trade.app.dto.HuobiResponse;
import com.aquariux.trade.app.dto.HuobiTicker;
import com.aquariux.trade.app.entity.CryptoPair;
import com.aquariux.trade.app.repository.CryptoPairRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Component
public class PriceScheduler {

    @Autowired
    private CryptoPairRepository cryptoPairRepository;

    @Value("${binance.endpoint}")
    private String binanceEndpoint;

    @Value("${huobi.endpoint}")
    private String huobiEndpoint;

    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 10000)
    public void fetchPrices() {
        try {

            BinanceResponse[] binanceResponse = fetchBinancePrices();

            HuobiResponse huobiResponse = fetchHuobiPrices();

            Double ethBid = Math.max(getBinanceBidPrice(binanceResponse, Constants.BINANCE_ETHUSDT),
                    getHuobiBidPrice(huobiResponse, Constants.HUOBI_ETHUSDT));
            Double ethAsk = Math.min(getBinanceAskPrice(binanceResponse, Constants.BINANCE_ETHUSDT),
                    getHuobiAskPrice(huobiResponse, Constants.HUOBI_ETHUSDT));

            Double btcBid = Math.max(getBinanceBidPrice(binanceResponse, Constants.BINANCE_BTCUSDT),
                    getHuobiBidPrice(huobiResponse, Constants.HUOBI_BTCUSDT));
            Double btcAsk = Math.min(getBinanceAskPrice(binanceResponse, Constants.BINANCE_BTCUSDT),
                    getHuobiAskPrice(huobiResponse, Constants.HUOBI_BTCUSDT));

            savePrices(Constants.PAIR_ETHUSDT, ethBid, ethAsk);
            savePrices(Constants.PAIR_BTCUSDT, btcBid, btcAsk);

        } catch (Exception e) {
            System.err.println("Error while fetching or saving prices: " + e.getMessage());
        }
    }

    private BinanceResponse[] fetchBinancePrices() {
        return restTemplate.getForObject(binanceEndpoint, BinanceResponse[].class);
    }

    private HuobiResponse fetchHuobiPrices() {
        return restTemplate.getForObject(huobiEndpoint, HuobiResponse.class);
    }

    private Double getBinanceBidPrice(BinanceResponse[] responses, String symbol) {
        return Arrays.stream(responses)
                .filter(r -> symbol.equals(r.getSymbol()))
                .map(r -> Double.parseDouble(r.getBidPrice()))
                .findFirst()
                .orElse(0.0);
    }

    private Double getBinanceAskPrice(BinanceResponse[] responses, String symbol) {
        return Arrays.stream(responses)
                .filter(r -> symbol.equals(r.getSymbol()))
                .map(r -> Double.parseDouble(r.getAskPrice()))
                .findFirst()
                .orElse(Double.MAX_VALUE);
    }

    private Double getHuobiBidPrice(HuobiResponse response, String symbol) {
        return response.getData().stream()
                .filter(t -> symbol.equalsIgnoreCase(t.getSymbol()))
                .map(HuobiTicker::getBid)
                .findFirst()
                .orElse(0.0);
    }

    private Double getHuobiAskPrice(HuobiResponse response, String symbol) {
        return response.getData().stream()
                .filter(t -> symbol.equalsIgnoreCase(t.getSymbol()))
                .map(HuobiTicker::getAsk)
                .findFirst()
                .orElse(Double.MAX_VALUE);
    }

    private void savePrices(String pairName, Double bidPrice, Double askPrice) {
        CryptoPair pair = cryptoPairRepository.findByPairName(pairName)
                .orElse(new CryptoPair());

        pair.setPairName(pairName);
        pair.setBidPrice(bidPrice);
        pair.setAskPrice(askPrice);
        pair.setUpdatedTime(LocalDateTime.now());

        cryptoPairRepository.save(pair);
    }
}


