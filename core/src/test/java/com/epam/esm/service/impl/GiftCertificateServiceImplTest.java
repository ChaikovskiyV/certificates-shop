package com.epam.esm.service.impl;

import com.epam.esm.dao.impl.GiftCertificateDaoImpl;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.PageDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ApplicationDuplicateException;
import com.epam.esm.exception.ApplicationNotFoundException;
import com.epam.esm.exception.ApplicationNotValidDataException;
import com.epam.esm.util.PaginationProvider;
import com.epam.esm.util.validator.impl.DataValidatorImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The type GiftCertificateServiceImplTest.
 * <p>
 * This class includes methods for testing the GiftCertificateServiceImpl class.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GiftCertificateServiceImplTest {
    @InjectMocks
    private GiftCertificateServiceImpl certificateService;
    @Spy
    private GiftCertificateDaoImpl certificateDaoMock;
    @Spy
    private TagServiceImpl tagServiceMock;
    @Spy
    private DataValidatorImpl dataValidatorMock;
    @Spy
    private PaginationProvider paginationProviderMock;
    @Spy
    private BindingResult bindingResultMock;

    private long id;
    private long notCorrectId;
    private String sortParamName;
    private String tagsParamName;
    private String nameParamName;
    private String userIdParamName;
    private String message;
    private String sortParams;
    private String strParam;
    private String description;
    private String notCorrectStrParam;
    private GiftCertificate certificateOne;
    private GiftCertificate certificateTwo;
    private GiftCertificateDto certificateDto;
    private GiftCertificateDto updatedCertificate;
    private List<GiftCertificate> certificates;
    private Map<String, Object> params;
    private PageDto<Tag> tagsPage;
    private User user;

    /**
     * Initialization of fields.
     */
    @BeforeAll
    void setUp() {
        id = 5;
        notCorrectId = -1;
        sortParams = "name desc, createDate asc, price desc";
        strParam = "some text";
        message = "some message";
        notCorrectStrParam = "some text with symbols like '>' and '<'";
        description = "description";
        sortParamName = "sortParams";
        nameParamName = "name";
        tagsParamName = "tags";
        userIdParamName = "userId";

        certificateOne = new GiftCertificate();
        certificateOne.setId(1);
        certificateOne.setName("first");
        certificateOne.setDuration(1);
        certificateOne.setDescription(description);
        certificateOne.setPrice(new BigDecimal(10));
        certificateOne.setCreateDate(LocalDateTime.of(2020, 4, 15, 20, 15, 40, 50));
        certificateOne.setLastUpdateDate(LocalDateTime.of(2022, 4, 15, 20, 15, 40, 50));
        user = new User();
        Order order = new Order();
        order.setUser(user);
        certificateOne.setOrders(Set.of(order));

        certificateTwo = new GiftCertificate();
        certificateTwo.setId(1);
        certificateTwo.setName("second");
        certificateTwo.setDuration(2);
        certificateTwo.setDescription(description);
        certificateTwo.setPrice(new BigDecimal(20));
        certificateTwo.setCreateDate(LocalDateTime.of(2020, 4, 15, 20, 15, 40, 50));
        certificateTwo.setLastUpdateDate(LocalDateTime.of(2022, 4, 15, 20, 15, 40, 50));

        GiftCertificate certificateThree = new GiftCertificate();
        certificateThree.setId(1);
        certificateThree.setName("third");
        certificateThree.setDuration(1);
        certificateThree.setDescription(description);
        certificateThree.setPrice(new BigDecimal(30));
        certificateThree.setCreateDate(LocalDateTime.of(2022, 3, 15, 20, 15, 40, 50));
        certificateThree.setLastUpdateDate(LocalDateTime.of(2022, 4, 15, 20, 15, 40, 50));

        certificateDto = new GiftCertificateDto();
        certificateDto.setName("third");
        certificateDto.setDuration(1);
        certificateDto.setDescription(description);
        certificateDto.setPrice(new BigDecimal(30));

        updatedCertificate = new GiftCertificateDto();
        updatedCertificate.setDuration(2);
        updatedCertificate.setDescription(description);
        updatedCertificate.setPrice(new BigDecimal(20));

        certificates = List.of(certificateOne, certificateTwo, certificateThree);
        tagsPage = new PageDto<>(List.of(), 1, 0, 0, 1);
    }

    /**
     * Configuration of mocks.
     */
    @BeforeEach
    void figureMock() {
        params = new HashMap<>();

        MockitoAnnotations.openMocks(this);
        Mockito.doReturn(true).when(dataValidatorMock).isNameValid(strParam);
        Mockito.doReturn(false).when(dataValidatorMock).isNameValid(notCorrectStrParam);
        Mockito.doReturn(true).when(dataValidatorMock).isDescriptionValid(strParam);
        Mockito.doReturn(false).when(dataValidatorMock).isDescriptionValid(notCorrectStrParam);
        Mockito.doReturn(true).when(dataValidatorMock).isNumberValid(Mockito.longThat(a -> a > 0));
        Mockito.doReturn(false).when(dataValidatorMock).isNumberValid(Mockito.longThat(a -> (a <= 0)));

        Mockito.doReturn(false).when(bindingResultMock).hasErrors();

        Mockito.doReturn(tagsPage).when(tagServiceMock).findTags(Mockito.anyMap());
    }

    /**
     * Testing the findCertificates method by a tag name when the name is correct.
     */
    @Test
    void findCertificateByTagNameWhenNameCorrect() {
        params = Map.of(tagsParamName, strParam, sortParamName, sortParams);
        Mockito.doReturn(certificates).when(certificateDaoMock).findByTagName(Mockito.anyList(), Mockito.anyString());
        List<GiftCertificate> certificateList = certificateService.findCertificates(params).getContent();

        assertEquals(certificates, certificateList);
    }

    /**
     * Testing the findCertificates method by a tag name when the name is not correct.
     */
    @Test
    void findCertificateByTagNameWhenNameNotCorrect() {
        params = Map.of(tagsParamName, notCorrectStrParam, sortParamName, sortParams);
        Mockito.verify(certificateDaoMock, Mockito.never()).findByTagName(Mockito.anyList(), Mockito.anyString());
        List<GiftCertificate> certificateList = certificateService.findCertificates(params).getContent();

        assertTrue(certificateList.isEmpty());
    }

    /**
     * Testing the findCertificates method by a couple of tags name when names are not correct.
     */
    @Test
    void findCertificateByCoupleTagsNameWhenNamesCorrect() {
        params = Map.of(tagsParamName, sortParams);
        Mockito.doReturn(certificates).when(certificateDaoMock).findByTagName(Mockito.argThat(l -> l.size() > 1), Mockito.anyString());
        List<GiftCertificate> certificateList = certificateService.findCertificates(params).getContent();

        assertEquals(certificates, certificateList);
    }

    /**
     * Testing the findCertificateById method when the id is present.
     */
    @Test
    void findCertificateByIdWhenIdPresent() {
        Mockito.doReturn(certificateOne).when(certificateDaoMock).findById(Mockito.anyLong());
        GiftCertificate certificate = certificateService.findCertificateById(id);

        assertEquals(certificateOne, certificate);
    }

    /**
     * Testing the findCertificateById method when the id is absent.
     */
    @Test
    void findCertificateByIdWhenIdAbsent() {
        Mockito.doReturn(null).when(certificateDaoMock).findById(Mockito.anyLong());

        assertThrows(ApplicationNotFoundException.class, () -> certificateService.findCertificateById(id), message);
    }

    /**
     * Testing the findCertificateById method when the id is not correct.
     */
    @Test
    void findCertificateByIdWhenIdNotCorrect() {
        assertThrows(ApplicationNotValidDataException.class, () -> certificateService.findCertificateById(notCorrectId), message);
    }

    /**
     * Testing the findCertificates method when a params map is empty.
     */
    @Test
    void findAllCertificates() {
        Mockito.doReturn(certificates).when(certificateDaoMock).findAll(Mockito.anyString());
        List<GiftCertificate> certificateList = certificateService.findCertificates(params).getContent();

        assertEquals(certificates, certificateList);
    }

    /**
     * Testing the findCertificates method by a name when the name is correct.
     */
    @Test
    void findCertificateByNameWhenNameCorrect() {
        params = Map.of(nameParamName, strParam, sortParamName, sortParams);
        Mockito.doReturn(certificates).when(certificateDaoMock).findByName(Mockito.anyString(), Mockito.anyString());
        List<GiftCertificate> certificateList = certificateService.findCertificates(params).getContent();

        assertEquals(certificates, certificateList);
    }

    /**
     * Testing the findCertificates method by a name when the name is not correct.
     */
    @Test
    void findCertificateByNameWhenNameNotCorrect() {
        params = Map.of(nameParamName, notCorrectStrParam, sortParamName, sortParams);
        Mockito.verify(certificateDaoMock, Mockito.never()).findByName(Mockito.anyString(), Mockito.anyString());
        List<GiftCertificate> certificateList = certificateService.findCertificates(params).getContent();

        assertTrue(certificateList.isEmpty());
    }

    /**
     * Testing the findCertificates method by a description when the description is correct.
     */
    @Test
    void findCertificateByDescriptionWhenDescriptionCorrect() {
        params = Map.of(description, strParam, sortParamName, sortParams);
        Mockito.doReturn(certificates).when(certificateDaoMock).findByDescription(Mockito.anyString(), Mockito.anyString());
        List<GiftCertificate> certificateList = certificateService.findCertificates(params).getContent();

        assertEquals(certificates, certificateList);
    }

    /**
     * Testing the findCertificates method by a description when the description is not correct.
     */
    @Test
    void findCertificateByDescriptionWhenDescriptionNotCorrect() {
        params = Map.of(description, notCorrectStrParam, sortParamName, sortParams);
        Mockito.verify(certificateDaoMock, Mockito.never()).findByDescription(Mockito.anyString(), Mockito.anyString());
        List<GiftCertificate> certificateList = certificateService.findCertificates(params).getContent();

        assertTrue(certificateList.isEmpty());
    }

    /**
     * Testing the findCertificates method by a user id when the id is not correct.
     */
    @Test
    void findCertificatesByUserId() {
        params = Map.of(userIdParamName, id, sortParamName, sortParams);
        Mockito.doReturn(certificates).when(certificateDaoMock).findByUserId(Mockito.anyLong(), Mockito.anyString());
        List<GiftCertificate> certificateList = certificateService.findCertificates(params).getContent();

        assertEquals(certificates, certificateList);
    }

    /**
     * Testing the findCertificates method by a user id when the id is not correct.
     */
    @Test
    void findCertificatesByUserIdWhenIdNotCorrect() {
        params = Map.of(userIdParamName, notCorrectId, sortParamName, sortParams);
        Mockito.verify(certificateDaoMock, Mockito.never()).findByUserId(Mockito.anyLong(), Mockito.anyString());

        assertThrows(ApplicationNotValidDataException.class, () -> certificateService.findCertificates(params));
    }


    /**
     * Testing the findUsersByCertificateId method.
     */
    @Test
    void findUsersByCertificateId() {
        params = Map.of(userIdParamName, notCorrectId, sortParamName, sortParams);
        Mockito.doReturn(certificateOne).when(certificateDaoMock).findById(Mockito.anyLong());
        List<User> users = certificateService.findUsersByCertificateId(id);

        assertEquals(List.of(user), users);
    }

    /**
     * Testing the addGiftCertificate method.
     */
    @Test
    void addGiftCertificate() {
        Mockito.doReturn(List.of()).when(certificateDaoMock).findByName(Mockito.anyString(), Mockito.anyString());
        Mockito.doReturn(certificateOne).when(certificateDaoMock).insert(Mockito.any(GiftCertificate.class));
        GiftCertificate certificate = certificateService.addGiftCertificate(certificateDto, bindingResultMock);

        assertEquals(certificateOne, certificate);
    }

    /**
     * Testing the addGiftCertificate method when such a certificate is already present.
     */
    @Test
    void addGiftCertificateWhenSuchAlreadyPresent() {
        Mockito.verify(certificateDaoMock, Mockito.never()).insert(Mockito.any(GiftCertificate.class));
        Mockito.doReturn(certificates).when(certificateDaoMock).findByName(Mockito.anyString(), Mockito.anyString());
        assertThrows(ApplicationDuplicateException.class, () ->
                certificateService.addGiftCertificate(certificateDto, bindingResultMock), message);
    }

    /**
     * Testing the addGiftCertificate method when input data is not correct.
     */
    @Test
    void addGiftCertificateWhenInputDataNotCorrect() {
        Mockito.doReturn(true).when(bindingResultMock).hasErrors();
        Mockito.verify(certificateDaoMock, Mockito.never()).insert(Mockito.any(GiftCertificate.class));
        assertThrows(ApplicationNotValidDataException.class, () ->
                certificateService.addGiftCertificate(certificateDto, bindingResultMock), message);
    }

    /**
     * Testing the updateGiftCertificate method.
     */
    @Test
    void updateGiftCertificate() {
        Mockito.doReturn(certificateOne).when(certificateDaoMock).findById(Mockito.anyLong());
        Mockito.doReturn(certificateTwo).when(certificateDaoMock).update(Mockito.any(GiftCertificate.class));
        GiftCertificate certificate = certificateService.updateGiftCertificate(updatedCertificate, id);

        assertEquals(certificateTwo, certificate);
    }

    /**
     * Testing the updateGiftCertificate method when parameters are the same.
     */
    @Test
    void updateGiftCertificateWhenParametersTheSame() {
        Mockito.doReturn(certificateTwo).when(certificateDaoMock).findById(Mockito.anyLong());
        Mockito.doReturn(certificateTwo).when(certificateDaoMock).update(Mockito.any(GiftCertificate.class));
        GiftCertificate certificate = certificateService.updateGiftCertificate(updatedCertificate, id);

        assertEquals(certificateTwo, certificate);
    }

    /**
     * Testing the deleteGiftCertificate method when a certificate is present.
     */
    @Test
    void deleteGiftCertificateWhenCertificatePresent() {
        Mockito.doReturn(certificateTwo).when(certificateDaoMock).findById(Mockito.anyLong());
        Mockito.doNothing().when(certificateDaoMock).delete(Mockito.any(GiftCertificate.class));
        assertDoesNotThrow(() -> certificateService.deleteGiftCertificate(id));
    }

    /**
     * Testing the deleteGiftCertificate method when a certificate is absent.
     */
    @Test
    void deleteGiftCertificateWhenCertificateAbsent() {
        Mockito.doReturn(null).when(certificateDaoMock).findById(Mockito.anyLong());
        Mockito.verify(certificateDaoMock, Mockito.never()).delete(Mockito.any(GiftCertificate.class));
        assertThrows(ApplicationNotFoundException.class, () -> certificateService.deleteGiftCertificate(id), message);
    }
}