package com.epam.esm.controller;

import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.linkprovider.LinkProvider;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.epam.esm.dao.RequestParamName.*;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The type UserController.
 */
@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private LinkProvider linkProvider;

    /**
     * Gets user by id.
     *
     * @param id the id
     * @return the user
     */
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public HttpEntity<User> getUserById(@PathVariable("id") long id) {
        User user = userService.findUserById(id);
        linkProvider.addLinkToUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Gets all users pageDto.
     *
     * @param email         the email
     * @param firstName     the first name
     * @param lastName      the last name
     * @param certificateId the certificate id
     * @param page          the page
     * @param limit         the limit
     * @return the pageDto
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public PageDto<User> getAllUsers(@RequestParam(value = "email", required = false) String email,
                                      @RequestParam(value = "firstName", required = false) String firstName,
                                      @RequestParam(value = "lastName", required = false) String lastName,
                                      @RequestParam(value = "certificateId", required = false) Long certificateId,
                                      @RequestParam(value = "page", required = false) Integer page,
                                      @RequestParam(value = "limit", required = false) Integer limit) {
        Map<String, Object> params = new HashMap<>();
        params.put(EMAIL, email);
        params.put(FIRST_NAME, firstName);
        params.put(LAST_NAME, lastName);
        params.put(CERTIFICATE_ID, certificateId);
        params.put(PAGE, page);
        params.put(LIMIT, limit);
        PageDto<User> usersPage = userService.findUsers(params);
        usersPage.getContent().forEach(linkProvider::addLinkToUser);
        return usersPage;
    }

    /**
     * Find orders by user id set.
     *
     * @param userId the id
     * @return the set
     */
    @GetMapping(value = "/{id}/orders")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public Set<Order> findOrdersByUserId(@PathVariable("id") long userId) {
        Set<Order> orders = userService.findUserById(userId).getOrders();
        orders.forEach(linkProvider::addLinkToOrder);
        return orders;
    }

    /**
     * Find certificates by user id set.
     *
     * @param userId the id
     * @return the set
     */
    @GetMapping(value = "/{id}/certificates")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public Set<GiftCertificate> findCertificatesByUserId(@PathVariable("id") long userId) {
        Set<GiftCertificate> certificates = userService.findCertificateByUserId(userId);
        certificates.forEach(linkProvider::addLinkToCertificate);
        return certificates;
    }

    /**
     * Sign on user user.
     *
     * @param userDto       the user dto
     * @param bindingResult the bindingResult
     * @return the user
     */
    @PostMapping
    public ResponseEntity<User> signUpUser(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) {
        User user = userService.addUser(userDto, bindingResult);
        linkProvider.addLinkToUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Sign on user user.
     *
     * @param userId the user id
     * @return the user
     */
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> addAdmin(@PathVariable("id") long userId) {
        User user = userService.addAdmin(userId);
        linkProvider.addLinkToUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}