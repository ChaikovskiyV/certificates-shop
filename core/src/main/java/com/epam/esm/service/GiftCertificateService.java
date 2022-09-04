package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.PageDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Map;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The interface GiftCertificateService.
 * <p>
 * This interface includes methods that process requests to gift certificate data from controller,
 * validate it and build data for query in database and call dao class methods.
 */
public interface GiftCertificateService {

    /**
     * Find certificate by id gift certificate.
     * <p>
     * This method calls a method from a dao class.
     * If the data with given id is absent the ApplicationNotFoundException will be thrown.
     *
     * @param id the id
     * @return the gift certificate
     */
    GiftCertificate findCertificateById(long id);

    /**
     * Find all certificates pageDto.
     * <p>
     * This method validates and prepares given data and calls a method from a dao class.
     * If sortParam is null the method will return not sorted data.
     *
     * @param params the params
     * @return the pageDto
     */
    PageDto<GiftCertificate> findCertificates(Map<String, Object> params);

    /**
     * Add gift certificate giftCertificate.
     *
     * @param giftCertificateDto the gift certificate dto
     * @param bindingResult      the binding result
     * @return the giftCertificate
     */
    GiftCertificate addGiftCertificate(GiftCertificateDto giftCertificateDto, BindingResult bindingResult);

    /***
     * Update gift certificate giftCertificate.
     * <p>
     * This method validates and prepares given data and calls a method from a dao class.
     * If sortParam is null the method will return not sorted data.
     * Of the given data is not correct the ApplicationNotValidDataException will be thrown
     *
     * @param giftCertificateDto the gift certificate dto
     * @param id the id
     * @return the giftCertificate
     */
    GiftCertificate updateGiftCertificate(GiftCertificateDto giftCertificateDto, long id);

    /**
     * Delete gift certificate boolean.
     * <p>
     * This method calls a method from a dao class.
     *
     * @param id the id
     */
    void deleteGiftCertificate(long id);

    /**
     * Find users by certificate id list.
     *
     * @param id the id
     * @return the list
     */
    List<User> findUsersByCertificateId(long id);
}