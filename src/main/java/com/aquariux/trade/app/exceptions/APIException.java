package com.aquariux.trade.app.exceptions;

import org.springframework.http.HttpStatus;

import java.util.Set;

public class APIException extends RuntimeException{
    private final HttpStatus httpStatusCode;
    private final transient Set<APIErrors> errors;
    private Boolean success;
    private String message;

    public APIException(HttpStatus httpStatusCode, Set<APIErrors> errors) {
        this.httpStatusCode = httpStatusCode;
        this.errors = errors;
    }

    public APIException(HttpStatus httpStatusCode, Set<APIErrors> errors, Boolean success, String message) {
        this.httpStatusCode = httpStatusCode;
        this.errors = errors;
        this.success = success;
        this.message = message;
    }

    public HttpStatus getHttpStatusCode() {
        return httpStatusCode;
    }

    public Set<APIErrors> getErrors() {
        return errors;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
