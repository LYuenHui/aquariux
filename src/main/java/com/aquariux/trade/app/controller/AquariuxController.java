package com.aquariux.trade.app.controller;

import com.aquariux.trade.app.dto.BaseRequest;
import com.aquariux.trade.app.dto.TradeRequest;
import com.aquariux.trade.app.entity.CryptoPair;
import com.aquariux.trade.app.entity.TransactionHistory;
import com.aquariux.trade.app.service.AquariuxService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${urlmapping.base.aquariux}")
public class AquariuxController {

    @Autowired
    private AquariuxService aquariuxService;


    @GetMapping(value = "${urlmapping.path.price}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get cypto price", description = "Returns eth and btc price")
    public ResponseEntity<List<CryptoPair>> getAllPrices() {

        return ResponseEntity.ok().body(aquariuxService.getAllPrices());
    }



    @PostMapping(value = "${urlmapping.path.transact}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Trade crypto", description = "trade  crypto")
    public ResponseEntity<String> trade(@RequestBody TradeRequest tradeRequest) {
        return ResponseEntity.ok().body(aquariuxService.trade(tradeRequest));
    }



    @PostMapping(value = "${urlmapping.path.balance}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get wallet balance", description = "return wallet balance")
    public ResponseEntity<Double> getWalletBalance(@RequestBody BaseRequest request) {


        return ResponseEntity.ok().body(aquariuxService.getWallletBalance(request.getUserName()));
    }

    @PostMapping(value ="${urlmapping.path.transact.history}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get transaction history", description = "return transaction history")
    public ResponseEntity<List<TransactionHistory>> getTradingHistory(@RequestBody BaseRequest request) {

        return ResponseEntity.ok().body(aquariuxService.getHistory(request.getUserName()));
    }
}
