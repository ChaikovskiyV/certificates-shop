package com.epam.esm.service;

import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import org.springframework.validation.BindingResult;

import java.util.Map;
import java.util.Set;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The interface User service.
 * <p>
 * This interface includes methods that process requests to user data from controller,
 * validate it and build data for query in database and call dao class methods.
 */
public interface UserService {
    /**
     * Find users pageDto.
     *
     * @param searchParams the search params
     * @return the pageDto
     */
    PageDto<User> findUsers(Map<String, Object> searchParams);

    /**
     * Find user by id user.
     * <p>
     * This method calls a method from a dao class.
     * If the data with given id is absent the ApplicationNotFoundException will be thrown.
     *
     * @param userId the user id
     * @return the user
     */
    User findUserById(long userId);

    /**
     * Find certificate by user id list.
     *
     * @param id the id
     * @return the list
     */
    Set<GiftCertificate> findCertificateByUserId(long id);

    /**
     * Add user user.
     *
     * @param userDto       the user dto
     * @param bindingResult the binding result
     * @return user
     */
    User addUser(UserDto userDto, BindingResult bindingResult);

    /**
     * Add admin user.
     *
     * @param userId the user id
     * @return user
     */
    User addAdmin(long userId);
}