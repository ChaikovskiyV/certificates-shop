package com.epam.esm.dao;

import com.epam.esm.entity.AbstractEntity;

/**
 * The interface BaseDao.
 * This interface includes methods that make and send queries to database to execute create, read and delete operations.
 *
 * @param <T> the type parameter
 */
public interface BaseDao<T extends AbstractEntity> {
    /**
     * Insert T.
     * <p>
     * This method executes create operation.
     *
     * @param entity the entity
     * @return the T
     */
    T insert(T entity);

    /**
     * Delete by id.
     * <p>
     * This method executes delete operation by id.
     *
     * @param entity the entity
     */
    void delete(T entity);

    /**
     * Find by id tag.
     * <p>
     * This method executes read operation from database by id.
     *
     * @param id the id
     * @return the T
     */
    T findById(long id);
}