package com.epam.esm.entity;

import com.epam.esm.auditdata.AuditListener;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The type Abstract entity.
 * <p>
 * This class is the parent for other entity classes.
 */
@EntityListeners(AuditListener.class)
@MappedSuperclass
public class AbstractEntity extends RepresentationModel<AbstractEntity> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String operationName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime operationTime;

    /**
     * Gets id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public LocalDateTime getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(LocalDateTime operationTime) {
        this.operationTime = operationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity that = (AbstractEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return new StringBuilder(getClass().getSimpleName())
                .append("{ id=")
                .append(id)
                .toString();
    }
}