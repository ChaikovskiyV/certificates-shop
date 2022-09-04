package com.epam.esm.service.impl;

import com.epam.esm.dao.impl.TagDaoImpl;
import com.epam.esm.entity.Tag;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The type Tag service impl test.
 * <p>
 * This class includes methods for testing the TagServiceImpl class.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TagServiceImplTest {
    @InjectMocks
    private TagServiceImpl tagService;
    @Spy
    private TagDaoImpl tagDaoMock;
    @Spy
    private DataValidator dataValidatorMock;
    @Spy
    private PaginationProvider paginationProviderMock;
    private long id;
    private long notCorrectId;
    private Tag tagOne;
    private Tag tagTwo;
    private Tag tagThree;
    private List<Tag> tags;
    private String name;
    private String notCorrectName;
    private String message;
    private Map<String, Object> params;

    /**
     * Initialization of fields.
     */
    @BeforeAll
    void setUp() {
        id = 10;
        notCorrectId = 0;
        tagOne = new Tag();
        tagOne.setName("First tag name");
        tagOne.setId(1);
        tagTwo = new Tag();
        tagTwo.setName("Second tag name");
        tagTwo.setId(2);
        tagThree = new Tag();
        tagThree.setName("Third tag name");
        tagThree.setId(3);
        tags = List.of(tagOne, tagTwo, tagThree);
        name = "Some name";
        notCorrectName = "<Some text>";
        message = "Some message";
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
        Mockito.doReturn(true).when(dataValidatorMock).isNumberValid(Mockito.longThat(a -> a > 0));
        Mockito.doReturn(false).when(dataValidatorMock).isNumberValid(Mockito.longThat(a -> (a <= 0)));
    }

    /**
     * Testing the findTagById method when the id is present.
     */
    @Test
    void findTagByIdWhenIdPresent() {
        Mockito.doReturn(tagOne).when(tagDaoMock).findById(Mockito.anyLong());
        Tag tag = tagService.findTagById(id);

        assertEquals(tagOne, tag);
    }

    /**
     * Testing the findTagById method when the id is absent.
     */
    @Test
    void findTagByIdWhenIdAbsent() {
        Mockito.doReturn(null).when(tagDaoMock).findById(Mockito.anyLong());

        assertThrows(ApplicationNotFoundException.class, () -> tagService.findTagById(id), message);
    }

    /**
     * Testing the findTagById method when the id is not correct.
     */
    @Test
    void findTagByIdWhenIdNotCorrect() {
        Mockito.verify(tagDaoMock, Mockito.never()).findById(Mockito.anyLong());

        assertThrows(ApplicationNotValidDataException.class, () -> tagService.findTagById(notCorrectId), message);
    }

    /**
     * Testing the findTags method when a params map is empty.
     */
    @Test
    void findAllTags() {
        Mockito.doReturn(tags).when(tagDaoMock).findAll();
        List<Tag> foundTags = tagService.findTags(params).getContent();

        assertEquals(tags, foundTags);
    }

    /**
     * Testing the findTags method by a name when the name is correct.
     */
    @Test
    void findTagByNameWhenNameCorrect() {
        params = Map.of("tagName", name);
        Mockito.doReturn(List.of(tagOne)).when(tagDaoMock).findByName(Mockito.anyString());
        List<Tag> foundTags = tagService.findTags(params).getContent();

        assertEquals(tagOne, foundTags.get(0));
    }

    /**
     * Testing the findTags method when the name is not correct.
     */
    @Test
    void findTagByNameWhenNameNotCorrect() {
        params = Map.of("tagName", notCorrectName);
        Mockito.verify(tagDaoMock, Mockito.never()).findByName(Mockito.anyString());
        List<Tag> tagList = tagService.findTags(params).getContent();

        assertTrue(tagList.isEmpty());
    }

    /**
     * Testing the findTags method by certificate id when the id is correct.
     */
    @Test
    void findTagsByCertificateIdWhenIdCorrect() {
        params = Map.of("certificateId", id);
        Mockito.doReturn(tags).when(tagDaoMock).findTagByCertificateId(Mockito.anyLong());
        List<Tag> tagList = tagService.findTags(params).getContent();

        assertEquals(tags, tagList);
    }

    /**
     * Testing the findTags method by certificate id when the id is not correct.
     */
    @Test
    void findTagsByCertificateIdWhenIdNotCorrect() {
        params = Map.of("certificateId", notCorrectId);
        Mockito.verify(tagDaoMock, Mockito.never()).findTagByCertificateId(Mockito.anyLong());

        assertThrows(ApplicationNotValidDataException.class, () -> tagService.findTags(params));
    }

    /**
     * Testing the addTag method.
     */
    @Test
    void addTag() {
        Mockito.doReturn(List.of()).when(tagDaoMock).findByName(Mockito.anyString());
        Mockito.doReturn(tagTwo).when(tagDaoMock).insert(Mockito.any(Tag.class));
        Tag tag = tagService.addTag(tagTwo);

        assertEquals(tagTwo, tag);
    }

    /**
     * Testing the addTag method when such a tag is already present.
     */
    @Test
    void addTagWhenSuchTagPresent() {
        Mockito.doReturn(List.of(tagThree)).when(tagDaoMock).findByName(Mockito.anyString());
        Mockito.verify(tagDaoMock, Mockito.never()).insert(Mockito.any(Tag.class));
        Tag tag = tagService.addTag(tagTwo);

        assertEquals(tagThree, tag);
    }

    /**
     * Testing the deleteTagById method.
     */
    @Test
    void deleteTagById () {
        Mockito.doReturn(tagOne).when(tagDaoMock).findById(Mockito.anyLong());
        Mockito.doNothing().when(tagDaoMock).delete(Mockito.any(Tag.class));

        assertDoesNotThrow(() -> tagService.deleteTagById(id));
    }

    /**
     * Testing the findMostWidelyUsedTagOfUserWithHighestCostOfOrders method.
     */
    @Test
    void findMostWidelyUsedTagOfUserWithHighestCostOfOrders () {
        Mockito.doReturn(tags).when(tagDaoMock).findMostWidelyUsedTagOfUserWithHighestCostOfOrders();
        List<Tag> foundedTags = tagService.findMostWidelyUsedTagOfUserWithHighestCostOfOrders(params).getContent();

        assertEquals(tags, foundedTags);
    }
}