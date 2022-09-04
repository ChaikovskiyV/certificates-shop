package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * The type Tag.
 *
 * @author VChaikovski
 * @project certificates-shop-backend.
 * <p>
 * This class describe entity Tag
 */
@Entity
@Table(name = "tags")
public class Tag extends AbstractEntity {
    @Column(name = "tag_name", unique = true)
    private String name;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private Set<GiftCertificate> certificates;

    /**
     * Instantiates a new Tag.
     */
    public Tag() {
        certificates = new HashSet<>();
    }

    /**
     * Instantiates a new Tag.
     *
     * @param name the name
     */
    public Tag(String name) {
        this.name = name;
        certificates = new HashSet<>();
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets certificates.
     *
     * @return the certificates
     */
    public Set<GiftCertificate> getCertificates() {
        return certificates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Tag tag = (Tag) o;
        return Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }

    @Override
    public String toString() {
        return new StringBuilder(super.toString())
                .append(", name='")
                .append(name)
                .append("}")
                .toString();
    }
}