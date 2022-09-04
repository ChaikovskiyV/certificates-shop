package com.epam.esm.dao;

import com.epam.esm.entity.User;

import java.util.List;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The interface User dao.
 * <p>
 * This interface includes methods that make and send queries to database to execute read operations with users data from database.
 */
public interface UserDao extends BaseDao<User> {
    /**
     * Find user by first name list.
     * <p>
     * This method executes read operation for all users that have given first name.
     *
     * @param firstName the first name
     * @return the list
     */
    List<User> findUserByFirstName(String firstName);

    /**
     * Find user by last name list.
     * <p>
     * This method executes read operation for all users that have given last name.
     *
     * @param lastName the last name
     * @return the list
     */
    List<User> findUserByLastName(String lastName);

    /**
     * Find user by first name and last name list.
     * <p>
     * This method executes read operation for all users that have given first and last name.
     *
     * @param firstName the first name
     * @param lastName  the last name
     * @return the list
     */
    List<User> findUserByFirstNameAndLastName(String firstName, String lastName);

    /**
     * Find user by email list.
     * <p>
     * This method executes read operation for all users that have given email.
     *
     * @param email the email
     * @return the list
     */
    List<User> findUserByEmail(String email);

    /**
     * Find user by certificate id list.
     *
     * @param certificateId the certificate id
     * @return the list
     */
    List<User> findUserByCertificateId(long certificateId);

    /**
     * Find all list.
     * <p>
     * This method executes read operation for all users from database.
     *
     * @return the list
     */
    List<User> findAll();

    /**
     * Update user user.
     * <p>
     * This method executes update operation for provided user from database.
     *
     * @return the user
     */
    User updateUser(User user);
}