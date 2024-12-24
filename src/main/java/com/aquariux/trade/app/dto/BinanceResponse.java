package com.aquariux.trade.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data

@AllArgsConstructor
public class BinanceResponse {
    private String symbol;
    private String bidPrice;
    private String askPrice;

    public String getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(String bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(String askPrice) {
        this.askPrice = askPrice;
    }
}


