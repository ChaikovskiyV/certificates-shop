package com.epam.esm.dao;

import java.util.List;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * <p>
 * The type Request param name.
 * <p>
 * This class includes a set of constants with request parameter names.
 */
public final class RequestParamName {
    /**
     * The constant TAGS.
     */
    public static final String TAGS = "tags";
    /**
     * The constant CERTIFICATE_ID.
     */
    public static final String CERTIFICATE_ID = "certificateId";
    /**
     * The constant CREATE_DATE.
     */
    public static final String CREATE_DATE = "createDate";
    /**
     * The constant DESCRIPTION.
     */
    public static final String DESCRIPTION = "description";
    /**
     * The constant PART_OF_WORD.
     */
    public static final String PART_OF_WORD = "partOfWord";
    /**
     * The constant DURATION.
     */
    public static final String DURATION = "duration";
    /**
     * The constant LAST_UPDATE_DATE.
     */
    public static final String LAST_UPDATE_DATE = "lastUpdateDate";
    /**
     * The constant PRICE.
     */
    public static final String PRICE = "price";
    /**
     * The constant TAG_NAME.
     */
    public static final String TAG_NAME = "tagName";
    /**
     * The constant NAME.
     */
    public static final String NAME = "name";
    /**
     * The constant EMAIL.
     */
    public static final String EMAIL = "email";
    /**
     * The constant FIRST_NAME.
     */
    public static final String FIRST_NAME = "firstName";
    /**
     * The constant LAST_NAME.
     */
    public static final String LAST_NAME = "lastName";
    /**
     * The constant OFFSET.
     */
    public static final String OFFSET = "offset";
    /**
     * The constant PAGE.
     */
    public static final String PAGE = "page";
    /**
     * The constant LIMIT.
     */
    public static final String LIMIT = "limit";
    /**
     * The constant SORT_PARAMS.
     */
    public static final String SORT_PARAMS = "sortParams";
    /**
     * The constant COST.
     */
    public static final String COST = "cost";
    /**
     * The constant USER_ID.
     */
    public static final String USER_ID = "userId";
    /**
     * The constant USER_ROLE.
     */
    public static final String USER_ROLE = "userRole";
    /**
     * The constant TOKEN.
     */
    public static final String TOKEN = "token";
    /**
     * The constant CERTIFICATE_PARAM_LIST.
     */
    public static final List<String> CERTIFICATE_PARAM_LIST = List.of(NAME, DESCRIPTION, DURATION, PRICE, CREATE_DATE, LAST_UPDATE_DATE);

    private RequestParamName() {
    }
}