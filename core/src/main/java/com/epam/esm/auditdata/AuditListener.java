package com.epam.esm.auditdata;

import com.epam.esm.entity.AbstractEntity;

import javax.persistence.PostRemove;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.time.ZoneId;


/**
 * @author VChaikovski
 * @project certificates-shop-backend
 *
 * The type Audit listener.
 *
 * This class is responsible for auditing data changes.
 */
public class AuditListener {
    private static final String PERSIST = "PERSIST";
    private static final String UPDATE = "UPDATE";
    private static final String DELETE = "DELETE";

    /**
     * Before delete.
     *
     * @param entity the entity
     */
    @PostRemove
    public void beforeDelete(AbstractEntity entity) {
        entity.setOperationName(DELETE);
        entity.setOperationTime(LocalDateTime.now(ZoneId.systemDefault()));
    }

    /**
     * Before persist.
     *
     * @param entity the entity
     */
    @PrePersist
    public void beforePersist(AbstractEntity entity) {
        entity.setOperationName(PERSIST);
        entity.setOperationTime(LocalDateTime.now(ZoneId.systemDefault()));
    }

    /**
     * Before update.
     *
     * @param entity the entity
     */
    @PreUpdate
    public void beforeUpdate(AbstractEntity entity) {
        entity.setOperationName(UPDATE);
        entity.setOperationTime(LocalDateTime.now(ZoneId.systemDefault()));
    }
}