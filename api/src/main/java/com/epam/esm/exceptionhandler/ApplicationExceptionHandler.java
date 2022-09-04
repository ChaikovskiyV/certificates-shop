package com.epam.esm.exceptionhandler;

import com.epam.esm.exception.ApplicationDuplicateException;
import com.epam.esm.exception.ApplicationNotFoundException;
import com.epam.esm.exception.ApplicationNotValidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;

import static com.epam.esm.exception.ErrorAttribute.*;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * <p>
 * The type ApplicationExceptionHandler.
 */
@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
    private final ResourceBundleMessageSource messageSource;

    @Autowired
    public ApplicationExceptionHandler(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * NotFoundError ApplicationError.
     *
     * @param e the e
     * @return the ApplicationError
     */
    @ExceptionHandler(ApplicationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApplicationError notFoundError(ApplicationNotFoundException e, Locale locale) {
        return new ApplicationError(NOT_FOUND_CODE, prepareMessage(e.getMessage(), e.getParam(), locale));
    }

    /**
     * NotValidError ApplicationError.
     *
     * @param e the e
     * @return the ApplicationError
     */
    @ExceptionHandler(ApplicationNotValidDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApplicationError notValidError(ApplicationNotValidDataException e, Locale locale) {
        return new ApplicationError(BAD_REQUEST_CODE, prepareMessage(e.getMessage(), e.getParam(), locale));
    }

    /**
     * DuplicateError ApplicationError.
     *
     * @param e the e
     * @return the ApplicationError
     */
    @ExceptionHandler(ApplicationDuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApplicationError duplicateError(ApplicationDuplicateException e, Locale locale) {
        return new ApplicationError(CONFLICT_CODE, prepareMessage(e.getMessage(), e.getParam(), locale));
    }

    /**
     * AccessForbiddenError ApplicationError.
     *
     * @param e the e
     * @return the ApplicationError
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApplicationError accessForbiddenError(Exception e, Locale locale) {
        return new ApplicationError(ACCESS_FORBIDDEN_CODE, messageSource.getMessage(ACCESS_FORBIDDEN_MESSAGE_KEY, null, locale));
    }

    /**
     * NotValidAuthenticationData ApplicationError.
     *
     * @param e the e
     * @return the ApplicationError
     */
    @ExceptionHandler({BadCredentialsException.class, InternalAuthenticationServiceException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApplicationError notValidAuthenticationDataError(Exception e, Locale locale) {
        return new ApplicationError(ACCESS_FORBIDDEN_CODE, messageSource.getMessage(NOT_VALID_AUTH_DATA_MESSAGE_KEY, null, locale));
    }

    /**
     * ServerError ApplicationError.
     *
     * @param e the e
     * @return the ApplicationError
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApplicationError serverError(Exception e, Locale locale) {
        return new ApplicationError(INTERNAL_CODE, messageSource.getMessage(INTERNAL_MESSAGE_KEY, null, locale));
    }

    private String prepareMessage(String keyMessage, Object param, Locale locale) {
        return String.format(messageSource.getMessage(keyMessage, null, locale), param);
    }
}