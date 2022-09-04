package com.epam.esm.service.impl;

import com.epam.esm.dao.impl.OrderDaoImpl;
import com.epam.esm.dao.impl.UserDaoImpl;
import com.epam.esm.dto.OrderDto;
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
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The type Order service impl test.
 * <p>
 * This class includes methods for testing the OrderServiceImpl class.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderServiceImplTest {
    @InjectMocks
    private OrderServiceImpl orderService;
    @Spy
    private GiftCertificateServiceImpl certificateServiceMock;
    @Spy
    private UserDaoImpl userDaoMock;
    @Spy
    private OrderDaoImpl orderDaoMock;
    @Spy
    private DataValidator dataValidatorMock;
    @Spy
    private PaginationProvider paginationProviderMock;

    private long id;
    private long notCorrectId;
    private Order orderOne;
    private OrderDto orderDto;
    private List<Order> orders;
    private String dateStr;
    private String notCorrectDateStr;
    private Map<String, Object> params;
    private GiftCertificate certificate;

    /**
     * Initialization of fields.
     */
    @BeforeAll
    void setUp() {
        id = 10;
        notCorrectId = 0;
        certificate = new GiftCertificate();
        certificate.setPrice(new BigDecimal(50));

        orderOne = new Order();
        orderOne.setId(1);
        orderOne.setUser(new User());
        orderOne.setCreateDate(LocalDateTime.of(2022, 5, 17, 23, 30, 0));
        orderOne.setCertificates(List.of(new GiftCertificate()));
        orderOne.setCost(new BigDecimal(50));

        Order orderTwo = new Order();
        orderTwo.setId(2);
        orderTwo.setUser(new User());
        orderTwo.setCreateDate(LocalDateTime.of(2022, 5, 17, 23, 35, 0));
        orderTwo.setCertificates(List.of(new GiftCertificate()));
        orderTwo.setCost(new BigDecimal(50));

        Order orderThree = new Order();
        orderThree.setId(3);
        orderThree.setUser(new User());
        orderThree.setCreateDate(LocalDateTime.of(2022, 5, 17, 23, 40, 0));
        orderThree.setCertificates(List.of(new GiftCertificate()));
        orderThree.setCost(new BigDecimal(50));

        orderDto = new OrderDto();
        orderDto.setUsername("some_username");
        orderDto.setCertificatesId(List.of(5L, 6L, 8L));

        orders = List.of(orderOne, orderTwo, orderThree);
        dateStr = "2022-05-17";
        notCorrectDateStr = "2022-05-32";
    }

    /**
     * Configuration of mocks.
     */
    @BeforeEach
    void configureMock() {
        params = new HashMap<>();

        MockitoAnnotations.openMocks(this);
        Mockito.doReturn(false).when(dataValidatorMock).isDateValid(notCorrectDateStr);
        Mockito.doReturn(true).when(dataValidatorMock).isDateValid(dateStr);
        Mockito.doReturn(true).when(dataValidatorMock).isNumberValid(Mockito.longThat(a -> a > 0));
        Mockito.doReturn(false).when(dataValidatorMock).isNumberValid(Mockito.longThat(a -> (a <= 0)));
    }

    /**
     * Testing the addOrder method.
     */
    @Test
    void addOrder() {
        User user = new User();
        user.setEmail("some_email@tut.by");
        Mockito.doReturn(certificate).when(certificateServiceMock).findCertificateById(Mockito.anyLong());
        Mockito.doReturn(List.of(user)).when(userDaoMock).findUserByEmail(Mockito.anyString());
        Mockito.doReturn(orderOne).when(orderDaoMock).insert(Mockito.any(Order.class));
        Order order = orderService.addOrder(orderDto);

        assertEquals(orderOne, order);
    }

    /**
     * Testing the deleteOrder method.
     */
    @Test
    void deleteOrder() {
        Mockito.doNothing().when(orderDaoMock).delete(Mockito.any(Order.class));
        Mockito.doReturn(new Order()).when(orderDaoMock).findById(id);

        assertDoesNotThrow(() -> orderService.deleteOrder(id));
    }

    /**
     * Testing the findOrders method by user id.
     */
    @Test
    void findOrdersByUserId() {
        params = Map.of("userId", id);
        Mockito.doReturn(orders).when(orderDaoMock).findOrderByUserId(Mockito.anyLong());
        List<Order> foundOrders = orderService.findOrders(params).getContent();

        assertEquals(orders, foundOrders);
    }

    /**
     * Testing the findOrders method by user id when the id is not correct.
     */
    @Test
    void findOrdersByUserIdWhenIdNotCorrect() {
        params = Map.of("userId", notCorrectId);
        Mockito.verify(orderDaoMock, Mockito.never()).findOrderByUserId(Mockito.anyLong());

        assertThrows(ApplicationNotValidDataException.class, () -> orderService.findOrders(params));
    }

    /**
     * Testing the findOrders method by a cost.
     */
    @Test
    void findOrdersByCost() {
        params = Map.of("cost", List.of(new BigDecimal(50), new BigDecimal(30)));
        Mockito.doReturn(orders).when(orderDaoMock).findOrderByCostAndCreateDate(Mockito.anyMap());
        List<Order> foundOrders = orderService.findOrders(params).getContent();

        assertEquals(orders, foundOrders);
    }

    /**
     * Testing the findOrders method by a date.
     */
    @Test
    void findOrdersByDate() {
        params = Map.of("createDate", List.of(dateStr));
        Mockito.doReturn(orders).when(orderDaoMock).findOrderByCostAndCreateDate(Mockito.anyMap());
        List<Order> foundOrders = orderService.findOrders(params).getContent();

        assertEquals(orders, foundOrders);
    }

    /**
     * Testing the findOrders method by a date when the date is not correct.
     */
    @Test
    void findOrdersByDateWhenDateNotCorrect() {
        params = Map.of("createDate", List.of(notCorrectDateStr));
        Mockito.verify(orderDaoMock, Mockito.never()).findOrderByCostAndCreateDate(Mockito.anyMap());
        List<Order> foundOrders = orderService.findOrders(params).getContent();

        assertTrue(foundOrders.isEmpty());
    }

    /**
     * Testing the findOrders method when a params map is empty.
     */
    @Test
    void findOrders() {
        Mockito.doReturn(orders).when(orderDaoMock).findAll();
        List<Order> foundOrders = orderService.findOrders(params).getContent();

        assertEquals(orders, foundOrders);
    }

    /**
     * Testing the findOrderById method when an id is present.
     */
    @Test
    void findOrderByIdWhenIdPresent() {
        Mockito.doReturn(orderOne).when(orderDaoMock).findById(Mockito.anyLong());
        Order order = orderService.findOrderById(id);

        assertEquals(orderOne, order);
    }

    /**
     * Testing the findOrderById method when an id is absent.
     */
    @Test
    void findOrderByIdWhenIdAbsent() {
        Mockito.doReturn(null).when(orderDaoMock).findById(Mockito.anyLong());

        assertThrows(ApplicationNotFoundException.class, () -> orderService.findOrderById(id));
    }

    /**
     * Testing the findOrderById method when an id is not correct.
     */
    @Test
    void findOrderByIdWhenIdNotCorrect() {
        Mockito.verify(orderDaoMock, Mockito.never()).findById(Mockito.anyLong());

        assertThrows(ApplicationNotValidDataException.class, () -> orderService.findOrderById(notCorrectId));
    }
}