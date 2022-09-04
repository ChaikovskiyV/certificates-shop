package com.epam.esm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The type ApplicationNotFoundException.
 * .
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ApplicationNotFoundException extends RuntimeException {
    private final transient Object param;
    private final transient String message;

    /**
     * Instantiates a new ApplicationNotFoundException.
     *
     * @param message the message
     * @param param   the param
     */
    public ApplicationNotFoundException(String message, Object param) {
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