package com.epam.esm.util.validator.impl;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.util.validator.DataValidator;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The type DataValidator.
 * This class implements the DataValidator interface.
 * This class includes methods that validate given data.
 */
@Component()
public class DataValidatorImpl implements DataValidator {
    private static final String NAME_REGEXP = "\\w[\\w\\s-]{2,100}";
    private static final String DESCRIPTION_REGEXP = "[^><]{3,300}";
    private static final String PARAM_REGEXP = "[^><]{3,50}";
    private static final String EMAIL_REGEX = "(\\w[\\w.-]{2,34}@\\p{Alpha}{2,10}\\.\\p{Alpha}{2,4})";
    private static final String DATE_REGEX = "20\\d{2}-((0[1-9])|(1[0-2]))-((0[1-9])|([1-2]\\d)|(3[01]))";

    @Override
    public boolean isNameValid(String name) {
        return name != null && name.matches(NAME_REGEXP);
    }

    @Override
    public boolean isDescriptionValid(String description) {
        return description != null && description.matches(DESCRIPTION_REGEXP);
    }

    @Override
    public boolean isNumberValid(long number) {
        return number > 0;
    }

    @Override
    public boolean isCertificateValid(GiftCertificateDto certificateDto) {
        return isNameValid(certificateDto.getName()) && isDescriptionValid(certificateDto.getDescription()) && isNumberValid(certificateDto.getDuration()) &&
                isNumberValid(certificateDto.getPrice().longValue());
    }

    @Override
    public boolean isParamValid(String param) {
        return param != null && param.matches(PARAM_REGEXP);
    }

    @Override
    public boolean isTagsValid(Set<Tag> tags) {
        return tags.stream().allMatch(t -> isNameValid(t.getName()));
    }

    @Override
    public boolean isEmailValid(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }

    @Override
    public boolean isDateValid(String date) {
        return date != null && date.matches(DATE_REGEX);
    }
}