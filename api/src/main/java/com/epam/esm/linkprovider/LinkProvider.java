package com.epam.esm.linkprovider;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The interface Link provider.
 *
 * This interface includes methods that provide HATEOAS support and add links to objects.
 */
public interface LinkProvider {
    /**
     * Add link to tag.
     *
     * @param tag the tag
     */
    void addLinkToTag(Tag tag);

    /**
     * Add link to order.
     *
     * @param order the order
     */
    void addLinkToOrder(Order order);

    /**
     * Add link to user.
     *
     * @param user the user
     */
    void addLinkToUser(User user);

    /**
     * Add link to certificate.
     *
     * @param certificate the certificate
     */
    void addLinkToCertificate(GiftCertificate certificate);
}