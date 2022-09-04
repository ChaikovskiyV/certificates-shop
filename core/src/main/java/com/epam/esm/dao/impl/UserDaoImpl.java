package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.epam.esm.dao.QueryStorage.*;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The type User dao.
 * <p>
 * This class implements the UserDao interface.
 * This class makes and sends queries to database to execute read operations with users data.
 */
@Repository()
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class UserDaoImpl implements UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Instantiates a new User dao.
     */
    public UserDaoImpl() {
        //default constructor without parameters
    }

    @Override
    public User insert(User user) {
        return entityManager.merge(user);
    }

    @Override
    public void delete(User user) {
        entityManager.remove(user);
    }

    @Override
    public User findById(long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public List<User> findAll() {
        return entityManager.createQuery(GET_ALL_USERS, User.class).getResultList();
    }

    @Override
    public User updateUser(User user) {
        return entityManager.merge(user);
    }

    @Override
    public List<User> findUserByFirstName(String firstName) {
        return entityManager.createQuery(FIND_USER_BY_FIRST_NAME, User.class)
                .setParameter(1, firstName)
                .getResultList();
    }

    @Override
    public List<User> findUserByLastName(String lastName) {
        return entityManager.createQuery(FIND_USER_BY_LAST_NAME, User.class)
                .setParameter(1, lastName)
                .getResultList();
    }

    @Override
    public List<User> findUserByFirstNameAndLastName(String firstName, String lastName) {
        return entityManager.createQuery(FIND_USER_BY_FIRST_NAME_AND_LAST_NAME, User.class)
                .setParameter(1, firstName)
                .setParameter(2, lastName)
                .getResultList();
    }

    @Override
    public List<User> findUserByEmail(String email) {
        return entityManager.createQuery(FIND_USER_BY_EMAIL, User.class)
                .setParameter(1, email)
                .getResultList();
    }

    @Override
    public List<User> findUserByCertificateId(long certificateId) {
        return entityManager.createQuery(FIND_USER_BY_CERTIFICATE_ID, User.class)
                .setParameter(1, certificateId)
                .getResultList();
    }
}