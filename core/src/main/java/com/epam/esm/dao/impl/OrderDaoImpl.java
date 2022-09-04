package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.entity.Order;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

import static com.epam.esm.dao.QueryStorage.*;
import static com.epam.esm.dao.RequestParamName.COST;
import static com.epam.esm.dao.RequestParamName.CREATE_DATE;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * <p>
 * The type Order dao.
 * <p>
 * This class implements the OrderDao interface.
 * This class makes and sends queries to database to execute create, read and delete operations with orders data.
 */
@Repository()
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class OrderDaoImpl implements OrderDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Instantiates a new Order dao.
     */
    public OrderDaoImpl() {
        //default constructor without parameters
    }

    @Override
    public Order insert(Order order) {
        return entityManager.merge(order);
    }

    @Override
    public void delete(Order order) {
        entityManager.remove(order);
    }

    @Override
    public Order findById(long id) {
        return entityManager.find(Order.class, id);
    }

    @Override
    public List<Order> findAll() {
        return entityManager.createQuery(GET_ALL_ORDERS, Order.class).getResultList();
    }

    @Override
    public List<Order> findOrderByCostAndCreateDate(Map<String, List<?>> filterParam) {
        return entityManager.createQuery(buildFindOrderByCostAndCreateDateQuery(filterParam), Order.class).getResultList();
    }

    @Override
    public List<Order> findOrderByUserId(long userId) {
        return entityManager.createQuery(FIND_ORDER_BY_USER_ID, Order.class)
                .setParameter(1, userId)
                .getResultList();
    }

    private String buildFindOrderByCostAndCreateDateQuery(Map<String, List<?>> filterParam) {
        StringBuilder query = new StringBuilder(FIND_ORDER);
        if (filterParam.containsKey(COST) && filterParam.containsKey(CREATE_DATE)) {
            query.append(BY_COST)
                    .append(buildParamForQuery(filterParam.get(COST)))
                    .append(AND)
                    .append(BY_CREATING_DATE)
                    .append(buildParamForQuery(filterParam.get(CREATE_DATE)));
        } else if (filterParam.containsKey(COST)) {
            query.append(BY_COST).append(buildParamForQuery(filterParam.get(COST)));
        } else if (filterParam.containsKey(CREATE_DATE)) {
            query.append(CREATE_DATE).append(buildParamForQuery(filterParam.get(CREATE_DATE)));
        }
        return query.toString();
    }

    private String buildParamForQuery(List<?> params) {
        StringBuilder query = new StringBuilder();
        if (params != null && !params.isEmpty()) {
            if (params.size() == 1) {
                query.append(LE).append('\'').append(params.get(0)).append('\'');
            } else {
                query.append(BETWEEN).append('\'').append(params.get(0)).append('\'').append(AND).append('\'').append(params.get(1)).append('\'');
            }
        }
        return query.toString();
    }
}