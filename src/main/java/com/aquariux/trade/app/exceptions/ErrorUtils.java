package com.aquariux.trade.app.exceptions;

import org.springframework.http.HttpStatus;

import java.util.*;

public class ErrorUtils {
    private static Map<String, List<String>> errorMap;

    public static final String UTILITY_CLASS = "Utility Class";
    public static final String ERROR_LIST_PROPERTY_DELIMITER = "\\|";
    public static final String SEPARATOR = ":::";

    private ErrorUtils() {
        throw new IllegalStateException(ErrorUtils.UTILITY_CLASS);
    }

    public static void initErrorMap(Map<String,String> errorPropertyMap){
        errorMap = new HashMap<>();
        errorPropertyMap.forEach((key,value)->
                errorMap.put(key, Arrays.stream(value.split(ERROR_LIST_PROPERTY_DELIMITER))
                        .map(String::trim)
                        .toList()
                )
        );
    }

    public static List<String> get(String key){
        return errorMap.get(key);

    }

    public static APIException buildAPIExceptionMessage(ErrorType errorType, String message){
        List<String> errorDef = get(errorType.value);
        String errorMessage = "%s[%s]".formatted(errorDef.get(1),UUID.randomUUID().toString());
        Set<APIErrors> errors = new HashSet<>();
        errors.add(new APIErrors(errorDef.get(0),errorMessage));

        return new APIException(HttpStatus.valueOf(Integer.valueOf(errorDef.get(2))),errors);
    }
}
