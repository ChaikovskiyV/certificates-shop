package com.epam.esm.util;

import com.epam.esm.dto.PageDto;
import com.epam.esm.entity.AbstractEntity;
import com.epam.esm.exception.ApplicationNotValidDataException;
import com.epam.esm.util.validator.DataValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epam.esm.dao.RequestParamName.*;
import static com.epam.esm.exception.ErrorAttribute.NOT_VALID_DATA_MESSAGE_KEY;
import static com.epam.esm.exception.ErrorAttribute.PAGE_NOT_EXIST_MESSAGE_KEY;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The type Pagination param provider.
 * <p>
 * This class check pagination parameters provided from a request, build pagination parameters map and paginates data.
 * If pagination parameters are null, default meanings will be set.
 * If pagination parameters are not correct, the ApplicationNotCorrectDataException will be thrown.
 * If a page number is not present, the ApplicationNotCorrectDataException will be thrown.
 */
@Component
public class PaginationProvider {
    private static final char EQ = '=';
    private static final int DEFAULT_LIMIT = 20;
    private static final int DEFAULT_PAGE = 1;

    private DataValidator validator;

    /**
     * Instantiates a new Pagination param provider.
     *
     * @param validator the validator
     */
    @Autowired
    public PaginationProvider(DataValidator validator) {
        this.validator = validator;
    }

    /**
     * Instantiates a new Pagination param provider.
     */
    public PaginationProvider() {
    }

    /**
     * Paginate data.
     *
     * @param entityList the entity list
     * @param params     the params
     * @return the pageDto
     */
    public <T extends AbstractEntity> PageDto<T> paginateData(List<T> entityList, Map<String, Object> params) {
        Integer limit = (Integer) params.get(LIMIT);
        Integer page = (Integer) params.get(PAGE);
        Map<String, Integer> paginationParams = getPaginationParam(page, limit);
        int offset = paginationParams.get(OFFSET);
        int last = offset + paginationParams.get(LIMIT);
        if (offset > 0 && offset >= entityList.size()) {
            throw new ApplicationNotValidDataException(PAGE_NOT_EXIST_MESSAGE_KEY, page);
        }
        List<T> content = last >= entityList.size() ? entityList.subList(offset, entityList.size()) : entityList.subList(offset, last);

        return new PageDto<>(content, paginationParams.get(PAGE), paginationParams.get(LIMIT), entityList.size(),
                findTotalPages(entityList.size(), paginationParams.get(LIMIT)));
    }

    private Map<String, Integer> getPaginationParam(Integer page, Integer limit) {
        Map<String, Integer> paginationParam = new HashMap<>();
        if (limit == null) {
            limit = DEFAULT_LIMIT;
        } else if (!validator.isNumberValid(limit)) {
            throw new ApplicationNotValidDataException(NOT_VALID_DATA_MESSAGE_KEY, buildNotCorrectParamStr(LIMIT, limit));
        }
        if (page == null) {
            page = DEFAULT_PAGE;
        } else if (!validator.isNumberValid(page)) {
            throw new ApplicationNotValidDataException(NOT_VALID_DATA_MESSAGE_KEY, buildNotCorrectParamStr(PAGE, page));
        }
        paginationParam.put(LIMIT, limit);
        paginationParam.put(OFFSET, getStartPosition(limit, page));
        paginationParam.put(PAGE, page);

        return paginationParam;
    }


    private String buildNotCorrectParamStr(String paramName, int value) {
        return new StringBuilder(paramName).append(EQ)
                .append(value)
                .toString();
    }

    private int getStartPosition(int limit, int page) {
        return limit * (page - 1);
    }

    private int findTotalPages(int totalElements, int limit) {
        return (int) Math.ceil((float) totalElements / limit);
    }
}