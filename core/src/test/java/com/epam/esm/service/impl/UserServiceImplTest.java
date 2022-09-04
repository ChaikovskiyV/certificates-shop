package com.epam.esm.service.impl;

import com.epam.esm.dao.impl.UserDaoImpl;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ApplicationNotFoundException;
import com.epam.esm.exception.ApplicationNotValidDataException;
import com.epam.esm.util.PaginationProvider;
import com.epam.esm.util.validator.DataValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The type User service impl test.
 * <p>
 * This class includes methods for testing the UserServiceImpl class.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Spy
    private UserDaoImpl userDaoMock;
    @Spy
    private DataValidator dataValidatorMock;
    @Spy
    private PaginationProvider paginationProviderMock;
    @Spy
    private BCryptPasswordEncoder passwordEncoderMock;
    @Spy
    private BindingResult bindingResultMock;
    private long id;
    private long notCorrectId;
    private String firstNameParam;
    private String lastNameParam;
    private String emailParam;
    private String certificateIdParam;
    private User userOne;
    private List<User> users;
    private String strParam;
    private String notCorrectStrParam;
    private Map<String, Object> params;
    private GiftCertificate certificate;

    /**
     * Initialization of fields.
     */
    @BeforeAll
    void setUp() {
        id = 10;
        notCorrectId = 0;
        firstNameParam = "firstName";
        lastNameParam = "lastName";
        emailParam = "email";
        certificateIdParam = "certificateId";
        userOne = new User();
        userOne.setId(1);
        userOne.setFirstName("first");
        userOne.setLastName("lastName-first");
        userOne.setEmail("first@first.by");
        certificate = new GiftCertificate();
        Order order = new Order();
        order.setCertificates(List.of(certificate));
        userOne.setOrders(Set.of(order));

        User userTwo = new User();
        userTwo.setId(2);
        userTwo.setFirstName("second");
        userTwo.setLastName("lastName-second");
        userTwo.setEmail("second@second.by");

        User userThree = new User();
        userThree.setId(3);
        userThree.setFirstName("third");
        userThree.setLastName("lastName-third");
        userThree.setEmail("third@third.by");

        users = List.of(userOne, userTwo, userThree);
        strParam = "Some string";
        notCorrectStrParam = "<Some text>";
    }

    /**
     * Configuration of mocks.
     */
    @BeforeEach
    void configureMock() {
        params = new HashMap<>();

        MockitoAnnotations.openMocks(this);
        Mockito.doReturn(false).when(dataValidatorMock).isNameValid(Mockito.argThat(s -> s.contains("<") || s.contains(">")));
        Mockito.doReturn(true).when(dataValidatorMock).isNameValid(Mockito.argThat(s -> !s.contains("<") && !s.contains(">")));
        Mockito.doReturn(false).when(dataValidatorMock).isParamValid(Mockito.argThat(s -> s.contains("<") || s.contains(">")));
        Mockito.doReturn(true).when(dataValidatorMock).isParamValid(Mockito.argThat(s -> !s.contains("<") && !s.contains(">")));
        Mockito.doReturn(false).when(dataValidatorMock).isEmailValid(Mockito.argThat(s -> s.contains("<") || s.contains(">")));
        Mockito.doReturn(true).when(dataValidatorMock).isEmailValid(Mockito.argThat(s -> !s.contains("<") && !s.contains(">")));
        Mockito.doReturn(true).when(dataValidatorMock).isNumberValid(Mockito.longThat(a -> a > 0));
        Mockito.doReturn(false).when(dataValidatorMock).isNumberValid(Mockito.longThat(a -> (a <= 0)));
    }

    /**
     * Testing the findUsers method by a first and a last name when params are correct.
     */
    @Test
    void findUsersByFirstAndLastNameWhenParamsCorrect() {
        params = Map.of(firstNameParam, strParam, lastNameParam, strParam);
        Mockito.doReturn(users).when(userDaoMock).findUserByFirstNameAndLastName(Mockito.anyString(), Mockito.anyString());
        List<User> foundUsers = userService.findUsers(params).getContent();

        assertEquals(users, foundUsers);
    }

    /**
     * Testing the findUsers method by a first name when the param is correct.
     */
    @Test
    void findUsersByFirstNameWhenParamCorrect() {
        params = Map.of(firstNameParam, strParam);
        Mockito.doReturn(users).when(userDaoMock).findUserByFirstName(Mockito.anyString());
        List<User> foundUsers = userService.findUsers(params).getContent();

        assertEquals(users, foundUsers);
    }

    /**
     * Testing the findUsers method by a last name when the param is correct.
     */
    @Test
    void findUsersByLastNameWhenParamCorrect() {
        params = Map.of(lastNameParam, strParam);
        Mockito.doReturn(users).when(userDaoMock).findUserByLastName(Mockito.anyString());
        List<User> foundUsers = userService.findUsers(params).getContent();

        assertEquals(users, foundUsers);
    }

    /**
     * Testing the findUsers method by an email when the param is correct.
     */
    @Test
    void findUsersByEmailWhenParamCorrect() {
        String email = "some_email@gmail.com";
        params = Map.of(emailParam, email);
        Mockito.doReturn(users).when(userDaoMock).findUserByEmail(Mockito.anyString());
        List<User> foundUsers = userService.findUsers(params).getContent();

        assertEquals(users, foundUsers);
    }

    /**
     * Testing the findUsers method by an email when the param is not correct.
     */
    @Test
    void findUsersByEmailWhenParamNotCorrect() {
        params = Map.of(emailParam, notCorrectStrParam);
        Mockito.verify(userDaoMock, Mockito.never()).findUserByEmail(Mockito.anyString());
        List<User> foundUsers = userService.findUsers(params).getContent();

        assertTrue(foundUsers.isEmpty());
    }

    /**
     * Testing the findUsers method by a first name when the param is not correct.
     */
    @Test
    void findUsersByFirstNameWhenParamNotCorrect() {
        params = Map.of(firstNameParam, notCorrectStrParam);
        Mockito.verify(userDaoMock, Mockito.never()).findUserByFirstName(Mockito.anyString());
        List<User> foundUsers = userService.findUsers(params).getContent();

        assertTrue(foundUsers.isEmpty());
    }

    /**
     * Testing the findUsers method by a certificate id.
     */
    @Test
    void findUsersByCertificateId() {
        params = Map.of(certificateIdParam, id);
        Mockito.doReturn(users).when(userDaoMock).findUserByCertificateId(Mockito.anyLong());
        List<User> foundUsers = userService.findUsers(params).getContent();

        assertEquals(users, foundUsers);
    }

    /**
     * Testing the findUsers method by a certificate id when the param is not correct.
     */
    @Test
    void findUsersByCertificateIdWhenIdNotCorrect() {
        params = Map.of(certificateIdParam, notCorrectId);
        Mockito.verify(userDaoMock, Mockito.never()).findUserByCertificateId(Mockito.anyLong());

        assertThrows(ApplicationNotValidDataException.class, () -> userService.findUsers(params));
    }

    /**
     * Testing the findUsers method when a params map is empty.
     */
    @Test
    void findUsers() {
        Mockito.doReturn(users).when(userDaoMock).findAll();
        List<User> foundUsers = userService.findUsers(params).getContent();

        assertEquals(users, foundUsers);
    }

    /**
     * Testing the findUserById method when the id is present.
     */
    @Test
    void findUserByIdWhenIdPresent() {
        Mockito.doReturn(userOne).when(userDaoMock).findById(Mockito.anyLong());
        User user = userService.findUserById(id);

        assertEquals(userOne, user);
    }

    /**
     * Testing the findUserById method when the id is absent.
     */
    @Test
    void findUserByIdWhenIdAbsent() {
        Mockito.doReturn(null).when(userDaoMock).findById(Mockito.anyLong());

        assertThrows(ApplicationNotFoundException.class, () -> userService.findUserById(id));
    }

    /**
     * Testing the findUserById method when the id is not correct.
     */
    @Test
    void findUserByIdWhenIdNotCorrect() {
        Mockito.verify(userDaoMock, Mockito.never()).findById(Mockito.anyLong());

        assertThrows(ApplicationNotValidDataException.class, () -> userService.findUserById(notCorrectId));
    }

    /**
     * Testing the findCertificateByUserId method.
     */
    @Test
    void findCertificateByUserId() {
        Mockito.doReturn(userOne).when(userDaoMock).findById(Mockito.anyLong());
        Set<GiftCertificate> certificates = userService.findCertificateByUserId(id);

        assertEquals(Set.of(certificate), certificates);
    }

    /**
     * Testing the addUser method.
     */
    @Test
    void addUser() {
        String email = "some_email@tut.by";
        String password = "password";
        UserDto userDto = new UserDto();
        userDto.setFirstName(firstNameParam);
        userDto.setLastName(lastNameParam);
        userDto.setEmail(email);
        userDto.setPassword(password);
        Mockito.doReturn(userOne).when(userDaoMock).insert(Mockito.any(User.class));
        Mockito.doReturn(List.of()).when(userDaoMock).findUserByEmail(Mockito.anyString());
        Mockito.doReturn(false).when(bindingResultMock).hasErrors();
        User user = userService.addUser(userDto, bindingResultMock);

        assertEquals(user, userOne);
    }

    /**
     * Testing the addUser method.
     */
    @Test
    void addAdmin() {
        Mockito.doReturn(userOne).when(userDaoMock).findById(Mockito.anyLong());
        Mockito.doReturn(userOne).when(userDaoMock).updateUser(Mockito.any(User.class));
        User user = userService.addAdmin(id);

        assertEquals(user, userOne);
    }
}