package com.epam.esm.exceptionhandler;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The type CertificateError.
 */
public class ApplicationError {
    private int errorCode;
    private String errorMessage;

    /**
     * Instantiates a new CertificateError.
     *
     * @param errorCode    the error code
     * @param errorMessage the error message
     */
    public ApplicationError(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * Gets error code.
     *
     * @return the errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Sets error code.
     *
     * @param errorCode the error code
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Gets error message.
     *
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets error message.
     *
     * @param errorMessage the error message
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}