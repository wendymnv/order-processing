package org.example.orderprocessing.exception;


import org.example.orderprocessing.model.enums.ErrorCategory;

public class ExternalServerException extends AbstractDomainException {

    private static final long serialVersionUID = -4421133771413546676L;

    public ExternalServerException(ErrorCategory error,
                                   String userMessage) {
        super(userMessage, error);
    }
}
