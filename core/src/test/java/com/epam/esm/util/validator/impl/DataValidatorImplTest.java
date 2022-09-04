package com.epam.esm.util.validator.impl;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The type DataValidatorImplTest.
 * <p>
 * This class includes methods for testing of the DataValidatorImpl class
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DataValidatorImplTest {
    private DataValidatorImpl dataValidator;
    private List<String> names;
    private List<String> notCorrectNames;
    private List<String> descriptions;
    private List<String> notCorrectDescriptions;
    private List<String> emails;
    private List<String> notCorrectEmails;
    private List<String> dates;
    private List<String> notCorrectDates;

    /**
     * Initialization of fields.
     */
    @BeforeAll
    void setUp() {
        dataValidator = new DataValidatorImpl();
        names = List.of("price desc", "weekend", "la-la-la", "101 opportunities", "last_update_date", "Sergey", "Syu");
        notCorrectNames = List.of("<ice fishing>", "-^**^-", "la<la>la", "la, la, la",
                "30la- la- la- la- la- la- la- la- la- la- la- la- la- la- la- la- la- la- la- la- la- la- la- la- la- la- la- la- la- la- la");
        descriptions = List.of("There is some description about something", "3La: la, la, la.", "Our discount- 15%.");
        notCorrectDescriptions = List.of("100la: la, la, la, la, la, la, la, la, la, la, la, la la, la, la, la, la, la, la, la, la, la, la, la, " +
                        "la la, la, la, la, la, la, la, la, la, la, la, la la, la, la, la, la, la la, la, la, la, la, la, la, la, la, la, la, " +
                        "la, la, la, la, la, la, la, la la, la, la, la, la, la, la, la, la, la, la, la, la la, la, la, la, la",
                "There is <some> description about something", "la");
        emails = List.of("syu_s@mail.ru", "chaikovskiy.va@mail.ru", "sergey_pernikau@epam.com", "alyaksey_parus-ok@gmail.com",
                "anna-maria_kotova@tut.by");
        notCorrectEmails = List.of("alyaksey_parus-ok_gmail.com", "sergey%pernikau@epam.com", "syu s@mail.ru", "chaikovskiy.va@mailru", "a@a.by");
        dates = List.of("2022-05-12", "2021-12-31", "2021-01-09", "2021-10-25");
        notCorrectDates = List.of("1990-12-12", "2022-00-15", "2022-21-10", "2022-01-38", "2022-11-00");
    }

    /**
     * Testing the isNameValid method when a name is correct.
     */
    @Test
    void isNameValidWhenNameCorrect() {
        int validCount = 0;
        for (String name : names) {
            if (dataValidator.isNameValid(name)) {
                validCount++;
            }
        }
        assertEquals(names.size(), validCount);
    }

    /**
     * Testing the isNameValid method when a name is not correct.
     */
    @Test
    void isNameValidWhenNameNotCorrect() {
        int notValidCount = 0;
        for (String name : notCorrectNames) {
            if (!dataValidator.isNameValid(name)) {
                notValidCount++;
            }
        }
        assertEquals(notCorrectNames.size(), notValidCount);
    }

    /**
     * Testing the isDescriptionValid method when a description is correct.
     */
    @Test
    void isDescriptionValidWhenDescriptionCorrect() {
        int validCount = 0;
        for (String description : descriptions) {
            if (dataValidator.isDescriptionValid(description)) {
                validCount++;
            }
        }
        assertEquals(descriptions.size(), validCount);
    }

    /**
     * Testing the isDescriptionValid method when a description is not correct.
     */
    @Test
    void isDescriptionValidWhenDescriptionNotCorrect() {
        int notValidCount = 0;
        for (String description : notCorrectDescriptions) {
            if (!dataValidator.isDescriptionValid(description)) {
                notValidCount++;
            }
        }
        assertEquals(notCorrectDescriptions.size(), notValidCount);
    }

    /**
     * Testing the isEmailValid method when an email is correct.
     */
    @Test
    void isEmailValidWhenEmailCorrect() {
        int validCount = 0;
        for (String email : emails) {
            if (dataValidator.isEmailValid(email)) {
                validCount++;
            }
        }
        assertEquals(emails.size(), validCount);
    }

    /**
     * Testing the isEmailValid method when an email is not correct.
     */
    @Test
    void isEmailValidWhenEmailNotCorrect() {
        int notValidCount = 0;
        for (String email : notCorrectEmails) {
            if (!dataValidator.isEmailValid(email)) {
                notValidCount++;
            }
        }
        assertEquals(notCorrectEmails.size(), notValidCount);
    }

    /**
     * Testing the isDateValid method when a date is correct.
     */
    @Test
    void isDateValidWhenDateCorrect() {
        int validCount = 0;
        for (String date : dates) {
            if (dataValidator.isDateValid(date)) {
                validCount++;
            }
        }
        assertEquals(dates.size(), validCount);
    }

    /**
     * Testing the isDateValid method when a date is not correct.
     */
    @Test
    void isDateValidWhenDateNotValid() {
        int notValidCount = 0;
        for (String date : notCorrectDates) {
            if (!dataValidator.isEmailValid(date)) {
                notValidCount++;
            }
        }
        assertEquals(notCorrectDates.size(), notValidCount);
    }

    /**
     * Testing the isTagsValid method when tags are valid.
     */
    @Test
    void isTagsValidWhenTagsValid() {
        Set<Tag> tags = Set.of(new Tag("firstTag"), new Tag("secondTag"));
        assertTrue(dataValidator.isTagsValid(tags));
    }

    /**
     * Testing the isTagsValid method when tags are not valid.
     */
    @Test
    void isTagsValidWhenTagsNotValid() {
        Set<Tag> tags = Set.of(new Tag("<not correct name>"), new Tag("correct name"));
        assertFalse(dataValidator.isTagsValid(tags));
    }

    /**
     * Testing the isNumberValid method when a number is negative.
     */
    @Test
    void isNumberValidWhenNumberNegative() {
        int negativeNumber = -1;
        assertFalse(dataValidator.isNumberValid(negativeNumber));
    }

    /**
     * Testing the isNumberValid method when a number is zero.
     */
    @Test
    void isNumberValidWhenNumberZero() {
        int zero = 0;
        assertFalse(dataValidator.isNumberValid(zero));
    }

    /**
     * Testing the isNumberValid method when a number is positive.
     */
    @Test
    void isNumberValidWhenNumberPositive() {
        int positiveNumber = 5;
        assertTrue(dataValidator.isNumberValid(positiveNumber));
    }

    /**
     * Testing the isCertificateValid method when a certificate is not valid.
     */
    @Test
    void isCertificateValidWhenCertificateNotValid() {
        int notValidDuration = -1;
        GiftCertificateDto notValidCertificateDto =
                new GiftCertificateDto("name", "description", new BigDecimal(1), notValidDuration, Set.of());
        assertFalse(dataValidator.isCertificateValid(notValidCertificateDto));
    }

    /**
     * Testing the isCertificateValid method when a certificate is valid.
     */
    @Test
    void isCertificateValidWhenCertificateValid() {
        GiftCertificateDto notValidCertificateDto =
                new GiftCertificateDto("name", "description", new BigDecimal(1), 2, Set.of(new Tag("tagName")));
        assertTrue(dataValidator.isCertificateValid(notValidCertificateDto));
    }
}