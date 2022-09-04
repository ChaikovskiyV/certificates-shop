package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.PageDto;
import com.epam.esm.entity.Order;

import java.util.Map;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * <p>
 * The interface Order service.
 * <p>
 * This interface includes methods that process requests to order data from controller,
 * validate it and build data for query in database and call dao class methods.
 */
public interface OrderService {
    /**
     * Add order order.
     *
     * @param orderDto the order dto
     * @return the order
     */
    Order addOrder(OrderDto orderDto);

    /**
     * Delete order.
     *
     * @param orderId the order id
     */
    void deleteOrder(long orderId);

    /**
     * Find orders pageDto.
     *
     * @param params the params
     * @return the pageDto
     */
    PageDto<Order> findOrders(Map<String, Object> params);

    /**
     * Find order by id order.
     *
     * @param orderId the order id
     * @return the order
     */
    Order findOrderById(long orderId);
}