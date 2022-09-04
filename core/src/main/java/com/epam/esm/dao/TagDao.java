package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.List;

/**
 * The interface Tag dao.
 *
 * @author VChaikovski
 * @project certificates-shop-backend
 * <p>
 * This interface includes methods that make and send queries to database to execute read operations with tags data from database.
 */
public interface TagDao extends BaseDao<Tag> {
    /**
     * Find by name list.
     * <p>
     * This method executes read operation for all tags that have given name.
     *
     * @param name the name
     * @return the list
     */
    List<Tag> findByName(String name);

    /**
     * Find tag by certificate id list.
     * <p>
     * This method executes read operation for all tags that are used by the gift certificate with given id from database.
     *
     * @param id the id
     * @return the list
     */
    List<Tag> findTagByCertificateId(long id);

    /**
     * Find most widely used tag of user with the highest cost of orders list.
     *
     * @return the list
     */
    List<Tag> findMostWidelyUsedTagOfUserWithHighestCostOfOrders();

    /**
     * Find all list.
     * <p>
     * This method executes read operation for all tags from database.
     *
     * @return the list
     */
    List<Tag> findAll();
}