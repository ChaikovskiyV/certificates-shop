package com.epam.esm.dao;

import com.epam.esm.entity.Order;

import java.util.List;
import java.util.Map;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The interface Order dao.
 * <p>
 * This interface includes methods that make and send queries to database to execute read operations with orders data from database.
 */
public interface OrderDao extends BaseDao<Order> {
    /**
     * Find order by cost and create date list.
     * <p>
     * This method executes read operation for all orders depend on given params.
     *
     * @param filterParam the filter param
     * @return the list
     */
    List<Order> findOrderByCostAndCreateDate(Map<String, List<?>> filterParam);

    /**
     * Find order by user id list.
     * <p>
     * This method executes read operation for all orders that have the user with given id.
     *
     * @param userId the user id
     * @return the list
     */
    List<Order> findOrderByUserId(long userId);

    /**
     * Find all list.
     * <p>
     * This method executes read operation for all orders from database.
     *
     * @return the list
     */
    List<Order> findAll();
}