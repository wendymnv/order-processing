package org.example.orderprocessing.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.orderprocessing.model.enums.ErrorCategory;

@Data
@EqualsAndHashCode(callSuper = false)
public class AbstractDomainException extends RuntimeException {

    private static final long serialVersionUID = -1461096138865827919L;

    private final ErrorCategory error;

    protected AbstractDomainException(String userMessage, ErrorCategory error) {
        super(userMessage);
        this.error = error;
    }

    public AbstractDomainException(ErrorCategory error) {
        this.error = error;
    }
}
