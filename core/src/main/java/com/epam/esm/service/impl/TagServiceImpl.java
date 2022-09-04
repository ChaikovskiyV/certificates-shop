package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.impl.TagDaoImpl;
import com.epam.esm.dto.PageDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ApplicationNotFoundException;
import com.epam.esm.exception.ApplicationNotValidDataException;
import com.epam.esm.service.TagService;
import com.epam.esm.util.PaginationProvider;
import com.epam.esm.util.validator.DataValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.epam.esm.dao.RequestParamName.CERTIFICATE_ID;
import static com.epam.esm.dao.RequestParamName.TAG_NAME;
import static com.epam.esm.exception.ErrorAttribute.NOT_VALID_ID_MESSAGE_KEY;
import static com.epam.esm.exception.ErrorAttribute.TAG_NOT_FOUND_MESSAGE_KEY;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The type TagService.
 * <p>
 * This class implements the TagService interface.
 * This class  includes methods that process requests from controller, validate and build data for query in database and call dao class methods.
 */
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Service
public class TagServiceImpl implements TagService {
    private DataValidator validator;
    private TagDao tagDao;
    private PaginationProvider paginationProvider;

    /**
     * Instantiates a new TagService.
     *
     * @param tagDao             the tag dao
     * @param validator          the validator
     * @param paginationProvider the paginationParamProvider
     */
    @Autowired
    public TagServiceImpl(TagDaoImpl tagDao, DataValidator validator,
                          PaginationProvider paginationProvider) {
        this.validator = validator;
        this.tagDao = tagDao;
        this.paginationProvider = paginationProvider;
    }

    public TagServiceImpl() {
    }

    @Override
    public Tag findTagById(long tagId) {
        if (!validator.isNumberValid(tagId)) {
            throw new ApplicationNotValidDataException(NOT_VALID_ID_MESSAGE_KEY, tagId);
        }
        return Optional.ofNullable(tagDao.findById(tagId)).orElseThrow(() ->
                new ApplicationNotFoundException(TAG_NOT_FOUND_MESSAGE_KEY, tagId));
    }

    @Override
    public PageDto<Tag> findTags(Map<String, Object> params) {
        List<Tag> tags;
        String tagName = (String) params.get(TAG_NAME);
        Long certificateId = (Long) params.get(CERTIFICATE_ID);
        if (tagName != null) {
            tags = !validator.isNameValid(tagName) ? new ArrayList<>() : tagDao.findByName(tagName);
        } else if (certificateId != null) {
            if (validator.isNumberValid(certificateId)) {
                tags = tagDao.findTagByCertificateId(certificateId);
            } else {
                throw new ApplicationNotValidDataException(NOT_VALID_ID_MESSAGE_KEY, certificateId);
            }
        } else {
            tags = tagDao.findAll();
        }
        return paginationProvider.paginateData(tags, params);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Tag addTag(Tag tag) {
        List<Tag> tags = tagDao.findByName(tag.getName());

        return tags.isEmpty() ? tagDao.insert(tag) : tags.get(0);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteTagById(long tagId) {
        tagDao.delete(findTagById(tagId));
    }

    @Override
    public PageDto<Tag> findMostWidelyUsedTagOfUserWithHighestCostOfOrders(Map<String, Object> params) {
        return paginationProvider.paginateData(tagDao.findMostWidelyUsedTagOfUserWithHighestCostOfOrders(), params);
    }
}