package com.epam.esm.service;

import com.epam.esm.dto.PageDto;
import com.epam.esm.entity.Tag;

import java.util.Map;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The interface Tag service.
 * <p>
 * This interface includes methods that process requests to tag data from controller, validate it and build data for query in database and call dao class methods.
 */
public interface TagService {
    /**
     * Find tag by id tag.
     * <p>
     * This method calls a method from a dao class.
     * If the data with given id is absent the ApplicationNotFoundException will be thrown.
     *
     * @param tagId the tag id
     * @return the tag
     */
    Tag findTagById(long tagId);

    /**
     * Find tags pageDto.
     *
     * @param params the params
     * @return the pageDto
     */
    PageDto<Tag> findTags(Map<String, Object> params);

    /**
     * add tag long.
     *
     * @param tag the tag
     * @return the long
     */
    Tag addTag(Tag tag);

    /**
     * Delete tag by id tag.
     * <p>
     * This method calls a method from a dao class.
     *
     * @param tagId the tag id
     */
    void deleteTagById(long tagId);

    /**
     * Find most widely used tag of user with highest cost of orders pageDto.
     *
     * @param params the params
     * @return the pageDto
     */
    PageDto<Tag> findMostWidelyUsedTagOfUserWithHighestCostOfOrders(Map<String, Object> params);
}