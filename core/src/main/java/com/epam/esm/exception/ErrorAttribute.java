package com.epam.esm.exception;

/**
 * The type Error attribute.
 *
 * @author VChaikovski
 * @project certificates-shop-backend
 * <p>
 * This class includes set of constants with errors code and keys of messages.
 */
public final class ErrorAttribute {
    /**
     * The constant NO_ACCESS_CODE.
     */
    public static final int NOT_AUTHORIZED_CODE = 40101;
    /**
     * The constant NOT_FOUND_CODE.
     */
    public static final int NOT_FOUND_CODE = 40401;
    /**
     * The constant ACCESS_FORBIDDEN_CODE.
     */
    public static final int ACCESS_FORBIDDEN_CODE = 40301;
    /**
     * The constant BAD_REQUEST_CODE.
     */
    public static final int BAD_REQUEST_CODE = 40001;
    /**
     * The constant CONFLICT_CODE.
     */
    public static final int CONFLICT_CODE = 40901;
    /**
     * The constant INTERNAL_CODE.
     */
    public static final int INTERNAL_CODE = 50001;
    /**
     * The constant INTERNAL_MESSAGE_KEY.
     */
    public static final String INTERNAL_MESSAGE_KEY = "ex.internal";
    /**
     * The constant TAG_NOT_FOUND_MESSAGE_KEY.
     */
    public static final String TAG_NOT_FOUND_MESSAGE_KEY = "ex.notFoundTag";
    /**
     * The constant NOT_VALID_ID_MESSAGE_KEY.
     */
    public static final String NOT_VALID_ID_MESSAGE_KEY = "ex.notValidId";
    /**
     * The constant NOT_VALID_DATA_MESSAGE_KEY.
     */
    public static final String NOT_VALID_DATA_MESSAGE_KEY = "ex.notValidData";
    /**
     * The constant CERTIFICATE_NOT_FOUND_MESSAGE_KEY.
     */
    public static final String CERTIFICATE_NOT_FOUND_MESSAGE_KEY = "ex.notFoundCertificate";
    /**
     * The constant CERTIFICATE_DUPLICATE_MESSAGE_KEY.
     */
    public static final String CERTIFICATE_DUPLICATE_MESSAGE_KEY = "ex.duplicateCertificate";
    /**
     * The constant USER_NOT_FOUND_MESSAGE_KEY.
     */
    public static final String USER_NOT_FOUND_MESSAGE_KEY = "ex.notFoundSuchUser";
    /**
     * The constant USER_NOT_FOUND_SUCH_MESSAGE_KEY.
     */
    public static final String USER_NOT_FOUND_SUCH_MESSAGE_KEY = "ex.notFoundSuchUser";
    /**
     * The constant USER_DUPLICATE_MESSAGE_KEY.
     */
    public static final String USER_DUPLICATE_MESSAGE_KEY = "ex.duplicateUser";
    /**
     * The constant ORDER_NOT_FOUND_MESSAGE_KEY.
     */
    public static final String ORDER_NOT_FOUND_MESSAGE_KEY = "ex.notFoundOrder";
    /**
     * The constant ORDER_EMPTY_MESSAGE_KEY.
     */
    public static final String ORDER_EMPTY_MESSAGE_KEY = "ex.orderEmpty";
    /**
     * The constant PAGE_NOT_EXIST_MESSAGE_KEY.
     */
    public static final String PAGE_NOT_EXIST_MESSAGE_KEY = "ex.pageNotExist";
    /**
     * The constant NOT_AUTHORIZED_MESSAGE_KEY.
     */
    public static final String NOT_AUTHORIZED_MESSAGE_KEY = "ex.notAuthorized";
    /**
     * The constant NOT_VALID_AUTH_DATA_MESSAGE_KEY.
     */
    public static final String NOT_VALID_AUTH_DATA_MESSAGE_KEY = "ex.notValidAuthData";
    /**
     * The constant ACCESS_FORBIDDEN_MESSAGE_KEY.
     */
    public static final String ACCESS_FORBIDDEN_MESSAGE_KEY = "ex.accessForbidden";


    private ErrorAttribute() {
    }
}