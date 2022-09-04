package com.epam.esm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The type ApplicationNotValidDataException.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ApplicationNotValidDataException extends RuntimeException {
    private final transient String message;
    private final transient Object param;

    /**
     * Instantiates a new ApplicationNotValidDataException.
     *
     * @param message the message
     * @param param   the param
     */
    public ApplicationNotValidDataException(String message, Object param) {
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