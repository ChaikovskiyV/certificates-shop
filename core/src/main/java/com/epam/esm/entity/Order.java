package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The type Order.
 *
 * @author VChaikovski
 * @project certificates-shop-backend
 * <p>
 * This class describe entity Order
 */
@Entity
@Table(name = "orders")
public class Order extends AbstractEntity {
    private BigDecimal cost;

    @Column(name = "create_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "orders_gift_certificates",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "certificate_id", referencedColumnName = "id"))
    @JsonIgnore
    private List<GiftCertificate> certificates;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    /**
     * Instantiates a new Order.
     *
     * @param cost         the cost
     * @param createDate   the create date
     * @param certificates the certificates
     * @param user         the user
     */
    public Order(BigDecimal cost, LocalDateTime createDate, List<GiftCertificate> certificates, User user) {
        this.cost = cost;
        this.createDate = createDate;
        this.certificates = certificates;
        this.user = user;
    }

    /**
     * Instantiates a new Order.
     */
    public Order() {
        certificates = new ArrayList<>();
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets user.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets cost.
     *
     * @return the cost
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * Sets cost.
     *
     * @param cost the cost
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * Gets create date.
     *
     * @return the create date
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     * Sets create date.
     *
     * @param createDate the create date
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * Gets certificates.
     *
     * @return the certificates
     */
    public List<GiftCertificate> getCertificates() {
        return certificates;
    }

    /**
     * Sets certificates.
     *
     * @param certificates the certificates
     */
    public void setCertificates(List<GiftCertificate> certificates) {
        this.certificates = certificates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Order order = (Order) o;
        return Objects.equals(user, order.user) && ((cost != null && order.cost != null) ? cost.compareTo(order.cost) == 0 : cost == order.cost) &&
                Objects.equals(createDate, order.createDate) && Objects.equals(certificates, order.certificates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user, cost, createDate, certificates);
    }

    @Override
    public String toString() {
        return new StringBuilder(super.toString())
                .append(", user=")
                .append(user)
                .append(", cost=")
                .append(cost)
                .append(", createDate=")
                .append(createDate)
                .append(", certificates=")
                .append(certificates)
                .append('}')
                .toString();
    }
}