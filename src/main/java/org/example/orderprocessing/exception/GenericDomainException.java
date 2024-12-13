package org.example.orderprocessing.exception;

import org.example.orderprocessing.model.enums.ErrorCategory;
/**
 * Represents a response from Kafka containing order information.
 */
public class GenericDomainException extends AbstractDomainException {

    private static final long serialVersionUID = 5876962427698288005L;

    public GenericDomainException(ErrorCategory error,
                                  String userMessage) {
        super(userMessage, error);
    }
}
