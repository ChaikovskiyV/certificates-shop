package com.epam.esm.linkprovider.impl;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.linkprovider.LinkProvider;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The type Link provider.
 * This class implements the LinkProvider interface.
 * This class includes methods that provide HATEOAS support and add links to objects.
 */
@Component
public class LinkProviderImpl implements LinkProvider {
    public static final String TAGS = "tags";
    public static final String CERTIFICATES = "certificates";
    public static final String USERS = "users";
    public static final String ORDERS = "orders";
    private static final String USER = "user";

    @Override
    public void addLinkToTag(Tag tag) {
        tag.add(linkTo(methodOn(TagController.class).findCertificateByTagId(tag.getId())).withRel(CERTIFICATES));
    }

    @Override
    public void addLinkToOrder(Order order) {
        order.add(linkTo(methodOn(OrderController.class).findCertificatesByOrderId(order.getId())).withRel(CERTIFICATES));
        order.add(linkTo(methodOn(OrderController.class).findUserByOrderId(order.getId())).withRel(USER));
    }

    @Override
    public void addLinkToUser(User user) {
        user.add(linkTo(methodOn(UserController.class).findOrdersByUserId(user.getId())).withRel(ORDERS));
        user.add(linkTo(methodOn(UserController.class).findCertificatesByUserId(user.getId())).withRel(CERTIFICATES));
    }

    @Override
    public void addLinkToCertificate(GiftCertificate certificate) {
        certificate.add(linkTo(methodOn(GiftCertificateController.class).findOrdersByCertificateId(certificate.getId())).withRel(ORDERS));
        certificate.add(linkTo(methodOn(GiftCertificateController.class).findUsersByCertificateId(certificate.getId())).withRel(USERS));
        certificate.add(linkTo(methodOn(GiftCertificateController.class).findTagsByCertificateId(certificate.getId())).withRel(TAGS));
    }
}