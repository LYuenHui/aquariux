package com.aquariux.trade.app.properties;

import com.aquariux.trade.app.exceptions.ErrorUtils;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties
@PropertySource("classpath:error.properties")
@Data
public class ErrorProperties {
    private Map<String,String> errors;
    private Map<String,String> generalErrors;
    private Map<String,String> errorStatus;
    private String errorLogDelimiter;


    @PostConstruct
    private void init(){
        ErrorUtils.initErrorMap(errors);}


    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getGeneralErrors() {
        return generalErrors;
    }

    public void setGeneralErrors(Map<String, String> generalErrors) {
        this.generalErrors = generalErrors;
    }

    public Map<String, String> getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(Map<String, String> errorStatus) {
        this.errorStatus = errorStatus;
    }

    public String getErrorLogDelimiter() {
        return errorLogDelimiter;
    }

    public void setErrorLogDelimiter(String errorLogDelimiter) {
        this.errorLogDelimiter = errorLogDelimiter;
    }
}
