package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.entity.enums.UserRole;
import com.epam.esm.exception.ApplicationDuplicateException;
import com.epam.esm.exception.ApplicationNotFoundException;
import com.epam.esm.exception.ApplicationNotValidDataException;
import com.epam.esm.service.UserService;
import com.epam.esm.util.PaginationProvider;
import com.epam.esm.util.validator.DataValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.*;

import static com.epam.esm.dao.RequestParamName.*;
import static com.epam.esm.exception.ErrorAttribute.*;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The type User service.
 * <p>
 * This class implements the UserService interface.
 * This class  includes methods that process requests from controller, validate and build data for query in database and call dao class methods.
 */
@Service
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class UserServiceImpl implements UserService {
    private DataValidator validator;
    private UserDao userDao;
    private PaginationProvider paginationProvider;
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Instantiates a new User service.
     *
     * @param validator          the validator
     * @param userDao            the user dao
     * @param paginationProvider the pagination param provider
     * @param passwordEncoder    the password encoder
     */
    @Autowired
    public UserServiceImpl(DataValidator validator, UserDao userDao, PaginationProvider paginationProvider,
                           BCryptPasswordEncoder passwordEncoder) {
        this.validator = validator;
        this.userDao = userDao;
        this.paginationProvider = paginationProvider;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Instantiates a new User service.
     */
    public UserServiceImpl() {
    }

    @Override
    public PageDto<User> findUsers(Map<String, Object> params) {
        List<User> users;
        String firstName = (String) params.get(FIRST_NAME);
        String lastName = (String) params.get(LAST_NAME);
        String email = (String) params.get(EMAIL);
        Long certificateId = (Long) params.get(CERTIFICATE_ID);
        if (firstName != null && lastName != null) {
            users = isNameParamValid(firstName, lastName) ? userDao
                    .findUserByFirstNameAndLastName(firstName, lastName) : new ArrayList<>();
        } else if (firstName != null) {
            users = isNameParamValid(firstName) ? userDao
                    .findUserByFirstName(firstName) : new ArrayList<>();
        } else if (lastName != null) {
            users = isNameParamValid(lastName) ? userDao
                    .findUserByLastName(lastName) : new ArrayList<>();
        } else if (email != null) {
            users = validator.isEmailValid(email) ? userDao
                    .findUserByEmail(email) : new ArrayList<>();
        } else if (certificateId != null) {
            checkId(certificateId);
            users = userDao.findUserByCertificateId(certificateId);
        } else {
            users = userDao.findAll();
        }
        return paginationProvider.paginateData(users, params);
    }

    @Override
    public User findUserById(long userId) {
        checkId(userId);
        return Optional.ofNullable(userDao.findById(userId)).orElseThrow(() ->
                new ApplicationNotFoundException(USER_NOT_FOUND_MESSAGE_KEY, userId));
    }

    @Override
    public Set<GiftCertificate> findCertificateByUserId(long id) {
        Set<GiftCertificate> certificates = new HashSet<>();
        findUserById(id).getOrders()
                .stream()
                .map(Order::getCertificates)
                .forEach(certificates::addAll);
        return certificates;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User addUser(UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ApplicationNotValidDataException(NOT_VALID_DATA_MESSAGE_KEY, userDto);
        }
        if (isUserExisted(userDto.getEmail())) {
            throw new ApplicationDuplicateException(USER_DUPLICATE_MESSAGE_KEY, userDto.getEmail());
        }
        return userDao.insert(buildUser(userDto));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User addAdmin(long userId) {
        User user = findUserById(userId);
        user.setUserRole(UserRole.ADMIN);
        return userDao.updateUser(user);
    }

    private User buildUser(UserDto userDto) {
        return new User(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()), UserRole.USER);
    }

    private boolean isUserExisted(String email) {
        return !userDao.findUserByEmail(email).isEmpty();
    }

    private void checkId(long id) {
        if (!validator.isNumberValid(id)) {
            throw new ApplicationNotValidDataException(NOT_VALID_ID_MESSAGE_KEY, id);
        }
    }

    private boolean isNameParamValid(String... nameParams) {
        return Arrays.stream(nameParams).allMatch(validator::isNameValid);
    }
}