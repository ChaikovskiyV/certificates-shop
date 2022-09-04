package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The interface GiftCertificateDao.
 * <p>
 * This interface includes methods that make and send queries to database to execute
 * read and update operations with gift certificates data from database.
 */
public interface GiftCertificateDao extends BaseDao<GiftCertificate> {

    /**
     * Find by name list.
     * <p>
     * This method executes read operation for all gift certificates that have given name or their names include it from database
     * and returns data that sorted by sortParam. If sortParam is null or empty the method will return not sorted data.
     *
     * @param name             the name
     * @param sortParam        the sort param
     * @return the list
     */
    List<GiftCertificate> findByName(String name, String sortParam);

    /**
     * Find by tag name list.
     * <p>
     * This method executes read operation for all gift certificates that have tags given name or their names include it from database
     * and returns data that sorted by sortParam. If sortParam is null or empty the method will return not sorted data.
     *
     * @param tagNames         the tag names
     * @param sortParam        the sort param
     * @return the list
     */
    List<GiftCertificate> findByTagName(List<String> tagNames, String sortParam);

    /**
     * Find by description list.
     * <p>
     * This method executes read operation for all gift certificates that have given description or their description include it from database
     * and returns data that sorted by sortParam. If sortParam is null the method will return not sorted data.
     *
     * @param description      the description
     * @param sortParam        the sort param
     * @return the list
     */
    List<GiftCertificate> findByDescription(String description, String sortParam);

    /**
     * Find by user id list.
     *
     * @param userId           the user id
     * @param sortParam        the sort param
     * @return the list
     */
    List<GiftCertificate> findByUserId(long userId, String sortParam);

    /**
     * Find all list.
     * <p>
     * This method executes read operation for all gift certificates from database and returns data that sorted by sortParam.
     * If sortParam is null or empty the method will return not sorted data.
     *
     * @param sortParam        the sort param
     * @return the list
     */
    List<GiftCertificate> findAll(String sortParam);

    /**
     * Update gift certificate.
     * <p>
     * This method executes update operation for the gift certificate that has given id.
     *
     * @param giftCertificate the gift certificate
     * @return the gift certificate
     */
    GiftCertificate update(GiftCertificate giftCertificate);
}