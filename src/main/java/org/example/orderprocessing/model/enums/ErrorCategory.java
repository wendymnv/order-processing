package org.example.orderprocessing.model.enums;

import org.springframework.http.HttpStatus;

import static org.example.orderprocessing.model.constants.Constants.*;
/**
 * Enum representing different error categories.
 */
public enum ErrorCategory {


    INVALID_REQUEST("invalid-request", HttpStatus.BAD_REQUEST, SVC0001,
            "Generic Client Error: invalid-request - additional info", "API Generic wildcard fault response"),
    UNEXPECTED("unexpected", HttpStatus.INTERNAL_SERVER_ERROR, SVR1000, "Generic error exception",
            "An error occurred"),

    EXTERNAL_ERROR("Generic Server Fault", HttpStatus.INTERNAL_SERVER_ERROR, SVR1000, "Generic Server Error",
            "There was a problem in the Service Providers network that prevented to carry out the request"),

    EXTERNAL_DATA_REPOSITORY("Repository transaction error", HttpStatus.SERVICE_UNAVAILABLE, SVR1000, "", "");

    private static final String PROPERTY_PREFIX = "application.order-processing.error-code.";

    private final String userMessage;
    private final String exceptionId;
    private final String exceptionText;
    private final String moreInfo;
    private final HttpStatus httpStatus;

    ErrorCategory(String userMessage, HttpStatus httpStatus, String exceptionId, String exceptionText,
                  String moreInfo) {
        this.userMessage = PROPERTY_PREFIX.concat(userMessage);
        this.httpStatus = httpStatus;
        this.exceptionId = exceptionId;
        this.exceptionText = exceptionText;
        this.moreInfo = moreInfo;
    }

}
