package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.impl.OrderDaoImpl;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.PageDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ApplicationNotFoundException;
import com.epam.esm.exception.ApplicationNotValidDataException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.PaginationProvider;
import com.epam.esm.util.validator.DataValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.epam.esm.dao.RequestParamName.*;
import static com.epam.esm.exception.ErrorAttribute.*;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * <p>
 * The type Order service.
 * <p>
 * This class implements the OrderService interface.
 * This class includes methods that process requests to order data from controller,
 * validate it and build data for query in database and call dao class methods.
 */
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Service
public class OrderServiceImpl implements OrderService {
    private UserDao userDao;
    private GiftCertificateService certificateService;
    private OrderDao orderDao;
    private DataValidator validator;
    private PaginationProvider paginationProvider;

    /**
     * Instantiates a new Order service.
     *
     * @param userDao            the userDao
     * @param certificateService the certificate service
     * @param orderDao           the order dao
     * @param validator          the validator
     * @param paginationProvider the pagination param provider
     */
    @Autowired
    public OrderServiceImpl(UserDao userDao, GiftCertificateService certificateService, OrderDaoImpl orderDao,
                            DataValidator validator, PaginationProvider paginationProvider) {
        this.userDao = userDao;
        this.certificateService = certificateService;
        this.orderDao = orderDao;
        this.validator = validator;
        this.paginationProvider = paginationProvider;
    }

    /**
     * Instantiates a new Order service.
     */
    public OrderServiceImpl() {
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Order addOrder(OrderDto orderDto) {
        Order order;
        if (orderDto.getUsername() == null) {
            throw new AccessDeniedException(ACCESS_FORBIDDEN_MESSAGE_KEY);
        }
        User user = userDao.findUserByEmail(orderDto.getUsername()).get(0);
        List<GiftCertificate> certificates = new ArrayList<>();
        orderDto.getCertificatesId().forEach(id -> certificates.add(certificateService.findCertificateById(id)));
        if (certificates.isEmpty()) {
            throw new ApplicationNotValidDataException(ORDER_EMPTY_MESSAGE_KEY, certificates);
        } else {
            order = new Order(findOrderCost(certificates), getCurrentTime(), certificates, user);
        }
        return orderDao.insert(order);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteOrder(long orderId) {
        orderDao.delete(findOrderById(orderId));
    }

    @Override
    public PageDto<Order> findOrders(Map<String, Object> params) {
        List<Order> orders;
        List<BigDecimal> costParam = (List<BigDecimal>) params.get(COST);
        List<String> dateParam = (List<String>) params.get(CREATE_DATE);
        Long userId = (Long) params.get(USER_ID);
        if (userId != null) {
            if (!validator.isNumberValid(userId)) {
                throw new ApplicationNotValidDataException(NOT_VALID_ID_MESSAGE_KEY, userId);
            }
            orders = orderDao.findOrderByUserId(userId);
        } else if (costParam != null || dateParam != null) {
            Map<String, List<?>> filterParams = new HashMap<>();
            if (costParam != null && !costParam.isEmpty() && isCostParamCorrect(costParam)) {
                filterParams.put(COST, costParam);
            }
            if (dateParam != null && !dateParam.isEmpty() && isDateParamCorrect(dateParam)) {
                filterParams.put(CREATE_DATE, parseStringToLocalDateTime(dateParam));
            }
            orders = filterParams.isEmpty() ? new ArrayList<>() :
                    orderDao.findOrderByCostAndCreateDate(filterParams);
        } else {
            orders = orderDao.findAll();
        }
        return paginationProvider.paginateData(orders, params);
    }

    @Override
    public Order findOrderById(long orderId) {
        if (!validator.isNumberValid(orderId)) {
            throw new ApplicationNotValidDataException(NOT_VALID_ID_MESSAGE_KEY, orderId);
        }
        return Optional.ofNullable(orderDao.findById(orderId)).orElseThrow(() ->
                new ApplicationNotFoundException(ORDER_NOT_FOUND_MESSAGE_KEY, orderId));
    }

    private BigDecimal findOrderCost(List<GiftCertificate> certificates) {
        BigDecimal cost = BigDecimal.ZERO;
        for (GiftCertificate certificate : certificates) {
            cost = cost.add(certificate.getPrice());
        }
        return cost;
    }

    private LocalDateTime getCurrentTime() {
        return LocalDateTime.now(ZoneId.systemDefault());
    }

    private boolean isCostParamCorrect(List<BigDecimal> costs) {
        return costs.stream().allMatch(c -> validator.isNumberValid(c.longValue()));
    }

    private boolean isDateParamCorrect(List<String> dates) {
        return dates.stream().allMatch(validator::isDateValid);
    }

    private List<LocalDate> parseStringToLocalDateTime(List<String> dates) {
        return dates.stream()
                .map(d -> LocalDate.parse(d, DateTimeFormatter.ISO_LOCAL_DATE))
                .toList();
    }
}