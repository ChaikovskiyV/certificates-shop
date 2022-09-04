package com.epam.esm.util.validator;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.Tag;

import java.util.Set;

/**
 * The interface Data validator.
 *
 * @author VChaikovski
 * @project certificates-shop-backend
 * <p>
 * This interface includes methods that validate given data.
 */
public interface DataValidator {
    /**
     * Is name valid boolean.
     *
     * @param name the name
     * @return the boolean
     */
    boolean isNameValid(String name);

    /**
     * Is description valid boolean.
     *
     * @param description the description
     * @return the boolean
     */
    boolean isDescriptionValid(String description);

    /**
     * Is number valid boolean.
     *
     * @param number the number
     * @return the boolean
     */
    boolean isNumberValid(long number);

    /**
     * Is certificate valid boolean.
     *
     * @param certificateDto the certificateDto
     * @return the boolean
     */
    boolean isCertificateValid(GiftCertificateDto certificateDto);

    /**
     * Is param valid boolean.
     *
     * @param param the param
     * @return the boolean
     */
    boolean isParamValid(String param);

    /**
     * Is tags valid boolean.
     *
     * @param tags the tags
     * @return the boolean
     */
    boolean isTagsValid(Set<Tag> tags);

    /**
     * Is email valid boolean.
     *
     * @param email the email
     * @return the boolean
     */
    boolean isEmailValid(String email);

    /**
     * Is date valid boolean.
     *
     * @param date the date
     * @return the boolean
     */
    boolean isDateValid(String date);
}