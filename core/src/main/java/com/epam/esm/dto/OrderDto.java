package com.epam.esm.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * <p>
 * The type Order dto.
 */
public class OrderDto {
    private String username;
    private List<Long> certificatesId;

    /**
     * Instantiates a new Order dto.
     */
    public OrderDto() {
        certificatesId = new ArrayList<>();
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets certificates id.
     *
     * @return the certificates id
     */
    public List<Long> getCertificatesId() {
        return certificatesId;
    }

    /**
     * Sets certificates id.
     *
     * @param certificatesId the certificates id
     */
    public void setCertificatesId(List<Long> certificatesId) {
        this.certificatesId = certificatesId;
    }

    @Override
    public String toString() {
        return new StringBuilder("OrderDto{")
                .append("username=")
                .append(username)
                .append(", certificatesId=")
                .append(certificatesId)
                .append('}')
                .toString();
    }
}