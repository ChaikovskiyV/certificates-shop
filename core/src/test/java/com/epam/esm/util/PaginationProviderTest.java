package com.epam.esm.util;

import com.epam.esm.entity.User;
import com.epam.esm.exception.ApplicationNotValidDataException;
import com.epam.esm.util.validator.DataValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The type PaginationProviderTest.
 * <p>
 * This class includes methods for testing the PaginationProvider class.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaginationProviderTest {
    @InjectMocks
    private PaginationProvider paginationProvider;
    @Spy
    private DataValidator validatorMock;

    private List<User> users;
    private Map<String, Object> params;
    private String limitParamName;
    private String pageParamName;
    private int limit;
    private int page;

    /**
     * Initialization of fields.
     */
    @BeforeAll
    void tearDown() {
        users = List.of(new User(), new User(), new User(), new User());
        limitParamName = "limit";
        pageParamName = "page";
        limit = 2;
        page = 1;
    }

    /**
     * Configuration of mocks.
     */
    @BeforeEach
    void setUp() {
        params = new HashMap<>();

        MockitoAnnotations.openMocks(this);
        Mockito.doReturn(true).when(validatorMock).isNumberValid(Mockito.longThat(a -> a > 0));
        Mockito.doReturn(false).when(validatorMock).isNumberValid(Mockito.longThat(a -> (a <= 0)));
    }

    /**
     * Testing the paginateData method when a params map is empty.
     */
    @Test
    void paginateDataWhenParamsMapEmpty() {
        List<User> userList = paginationProvider.paginateData(users, params).getContent();
        assertEquals(userList, users);
    }

    /**
     * Testing the paginateData method when params are correct.
     */
    @Test
    void paginateDataWhenParamsCorrect() {
        params = Map.of(limitParamName, limit, pageParamName, page);
        List<User> userList = paginationProvider.paginateData(users, params).getContent();
        assertEquals(userList.size(), limit);
    }

    /**
     * Testing the paginateData method when the limit parameter is not correct.
     */
    @Test
    void paginateDataWhenLimitParamNotCorrect() {
        int notCorrectLimitParam = -1;
        params = Map.of(limitParamName, notCorrectLimitParam, pageParamName, page);
        assertThrows(ApplicationNotValidDataException.class, () -> paginationProvider.paginateData(users, params));
    }

    /**
     * Testing the paginateData method when the page parameter is not correct.
     */
    @Test
    void paginateDataWhenPageParamNotCorrect() {
        int notCorrectPageParam = 0;
        params = Map.of(limitParamName, limit, pageParamName, notCorrectPageParam);
        assertThrows(ApplicationNotValidDataException.class, () -> paginationProvider.paginateData(users, params));
    }

    /**
     * Testing the paginateData method when the page parameter meaning exceeds an available pages number.
     */
    @Test
    void paginateDataWhenPageParamExceedPagesNumber() {
        int pagesNumber = users.size() + 1;
        params = Map.of(limitParamName, limit, pageParamName, pagesNumber);
        assertThrows(ApplicationNotValidDataException.class, () -> paginationProvider.paginateData(users, params));
    }
}