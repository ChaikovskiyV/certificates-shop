package com.epam.esm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Application duplicate exception.
 *
 * @author VChaikovski
 * @project certificates-shop-backend  The type ApplicationDuplicateException.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ApplicationDuplicateException extends RuntimeException {
    private final transient String message;
    private final transient Object param;

    /**
     * Instantiates a new ApplicationDuplicateException.
     *
     * @param message the message
     * @param param   the param
     */
    public ApplicationDuplicateException(String message, Object param) {
        this.message = message;
        this.param = param;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * Gets param.
     *
     * @return the param
     */
    public Object getParam() {
        return param;
    }
}