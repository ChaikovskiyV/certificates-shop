package com.epam.esm.dto;

import com.epam.esm.entity.Tag;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * <p>
 * The type GitCertificateDto.
 */
public class GiftCertificateDto {
    @NotEmpty(message = "A gift certificate's name shouldn't be empty.")
    @Pattern(regexp = "\\w[\\w\\s-]+", message = "A gift certificate's name should contain only alphabetic symbols, space or dash.")
    @Length(min = 2, max = 100, message = "The length of the gift certificate's name should be between 2 and 100 symbols.")
    private String name;

    @NotEmpty(message = "Description shouldn't be empty.")
    @Pattern(regexp = "[^><].+", message = "Symbols '<' and '>' are forbidden.")
    @Length(min = 3, max = 300, message = "The length of the gift certificate description should be between 3 and 300 symbols.")
    private String description;

    @DecimalMin(value = "0.00", message = "A price shouldn't be negative.")
    @DecimalMax(value = "10000.00", message = "A price shouldn't be more than 10000.00.")
    private BigDecimal price;

    @Min(value = 1, message = "A duration shouldn't be less than 1 day.")
    @Max(value = 180, message = "A duration shouldn't be more than 180 days.")
    private int duration;

    @NotEmpty(message = "Certificate should contain at least one tag.")
    private Set<Tag> tags;

    /**
     * Instantiates a new Git certificate dto.
     */
    public GiftCertificateDto() {
        tags = new HashSet<>();
    }

    /**
     * Instantiates a new Gift certificate dto.
     *
     * @param name        the name
     * @param description the description
     * @param price       the price
     * @param duration    the duration
     * @param tags        the tags
     */
    public GiftCertificateDto(String name, String description, BigDecimal price, int duration, Set<Tag> tags) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.tags = tags;
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
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Sets price.
     *
     * @param price the price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Gets duration.
     *
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets duration.
     *
     * @param duration the duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Gets tags.
     *
     * @return the tags
     */
    public Set<Tag> getTags() {
        return tags;
    }

    /**
     * Sets tags.
     *
     * @param tags the tags
     */
    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return new StringBuilder("GiftCertificate{")
                .append("name='")
                .append(name)
                .append("', description='")
                .append(description)
                .append("', price=")
                .append(price)
                .append(", duration=")
                .append(duration)
                .append(", tags=")
                .append(tags)
                .append('}')
                .toString();
    }
}