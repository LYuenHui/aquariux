package com.aquariux.trade.app.exceptions;

public enum ErrorType {
    INSUFFICIENT_BALANCE("insuff.balance"),
    USER_NOT_FOUND("user.not.found"),
    CRYPTO_PAIR_NOT_SUPPORTED("crypto.pair.not.supported");

    public final String value;

    private ErrorType(String value ){
        this.value = value;
    }
}
