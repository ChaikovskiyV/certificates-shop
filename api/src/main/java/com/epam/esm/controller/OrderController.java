package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.PageDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.linkprovider.LinkProvider;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epam.esm.dao.RequestParamName.*;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * <p>
 * The type OrderController.
 */
@RestController
@RequestMapping(value = "/api/v1/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private LinkProvider linkProvider;

    /**
     * Find order by id order.
     *
     * @param id the id
     * @return the order
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Order> findOrderById(@PathVariable("id") long id) {
        Order order = orderService.findOrderById(id);
        linkProvider.addLinkToOrder(order);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    /**
     * Find orders pageDto.
     *
     * @param cost       the cost
     * @param createDate the create date
     * @param userId     the user id
     * @param page       the page
     * @param limit      the limit
     * @return the pageDto
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public PageDto<Order> findOrders(@RequestParam(value = "cost", required = false) List<BigDecimal> cost,
                                     @RequestParam(value = "createDate", required = false) List<String> createDate,
                                     @RequestParam(value = "userId", required = false) Long userId,
                                     @RequestParam(value = "page", required = false) Integer page,
                                     @RequestParam(value = "limit", required = false) Integer limit) {
        Map<String, Object> params = new HashMap<>();
        params.put(COST, cost);
        params.put(CREATE_DATE, createDate);
        params.put(USER_ID, userId);
        params.put(PAGE, page);
        params.put(LIMIT, limit);
        PageDto<Order> ordersPage = orderService.findOrders(params);
        ordersPage.getContent().forEach(linkProvider::addLinkToOrder);
        return ordersPage;
    }

    /**
     * Find certificates by order id list.
     *
     * @param id the id
     * @return the list
     */
    @GetMapping("/{id}/certificates")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public List<GiftCertificate> findCertificatesByOrderId(@PathVariable("id") long id) {
        List<GiftCertificate> certificates = orderService.findOrderById(id).getCertificates();
        certificates.forEach(linkProvider::addLinkToCertificate);
        return certificates;
    }

    /**
     * Find user by order id user.
     *
     * @param id the id
     * @return the user
     */
    @GetMapping(value = "/{id}/user")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<User> findUserByOrderId(@PathVariable("id") long id) {
        User user = orderService.findOrderById(id).getUser();
        linkProvider.addLinkToUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Create order response entity.
     *
     * @param orderDto the order dto
     * @return the responseEntity
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Order> createOrder(@RequestBody OrderDto orderDto, Principal principal) {
        orderDto.setUsername(principal.getName());
        Order order = orderService.addOrder(orderDto);
        linkProvider.addLinkToOrder(order);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    /**
     * Delete order.
     *
     * @param id the id
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteOrder(@PathVariable("id") long id) {
        orderService.deleteOrder(id);
    }
}