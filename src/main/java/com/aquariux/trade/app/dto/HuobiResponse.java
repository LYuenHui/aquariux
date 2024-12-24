package com.aquariux.trade.app.dto;

import lombok.Data;

import java.util.List;

@Data
public class HuobiResponse {
    private List<HuobiTicker> data;


    public List<HuobiTicker> getData() {
        return data;
    }

    public void setData(List<HuobiTicker> data) {
        this.data = data;
    }
}

