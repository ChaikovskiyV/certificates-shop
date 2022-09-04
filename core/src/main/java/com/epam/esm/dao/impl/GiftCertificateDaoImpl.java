package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.epam.esm.dao.QueryStorage.*;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The type GiftCertificateDao.
 * <p>
 * This class implements the GitCertificateDao interface.
 * This class makes and sends queries to database to execute create, read and delete operations with gift certificates data.
 */
@Repository()
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class GiftCertificateDaoImpl implements GiftCertificateDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Instantiates a new GiftCertificateDao.
     */
    public GiftCertificateDaoImpl() {
        //default constructor without parameters
    }

    @Override
    public GiftCertificate findById(long id) {
        return entityManager.find(GiftCertificate.class, id);
    }

    @Override
    public List<GiftCertificate> findByName(String name, String sortParam) {
        return entityManager.createQuery(buildSortQuery(FIND_CERTIFICATE_BY_NAME, sortParam), GiftCertificate.class)
                .setParameter(1, name)
                .getResultList();
    }

    @Override
    public List<GiftCertificate> findByTagName(List<String> tagNames, String sortParam) {
        return entityManager.createQuery(buildFindByTagNameQuery(tagNames, sortParam), GiftCertificate.class)
                .getResultList();
    }

    @Override
    public List<GiftCertificate> findByDescription(String description, String sortParam) {
        return entityManager.createQuery(buildSortQuery(FIND_CERTIFICATE_BY_DESCRIPTION, sortParam), GiftCertificate.class)
                .setParameter(1, description)
                .getResultList();
    }

    @Override
    public List<GiftCertificate> findByUserId(long userId, String sortParam) {
        return entityManager.createQuery(FIND_CERTIFICATE_BY_USER_ID, GiftCertificate.class)
                .setParameter(1, userId)
                .getResultList();
    }

    @Override
    public List<GiftCertificate> findAll(String sortParam) {
        return entityManager.createQuery(buildSortQuery(GET_ALL_CERTIFICATES, sortParam), GiftCertificate.class)
                .getResultList();
    }

    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) {
        return entityManager.merge(giftCertificate);
    }

    @Override
    public GiftCertificate insert(GiftCertificate giftCertificate) {
        return entityManager.merge(giftCertificate);
    }

    @Override
    public void delete(GiftCertificate certificate) {
        entityManager.remove(certificate);
    }

    private String buildFindByTagNameQuery(List<String> tags, String sortParam) {
        char opBr = '(';
        char clBr = ')';
        char ap = '\'';
        String delim = ", ";
        StringBuilder sqlQuery = new StringBuilder(FIND_CERTIFICATE_BY_TAG_NAME).append(opBr);
        for (int i = 0; i < tags.size(); i++) {
            if (i > 0 && i <= tags.size() - 1) {
                sqlQuery.append(delim).append(ap).append(tags.get(i)).append(ap);
            } else {
                sqlQuery.append(ap).append(tags.get(i)).append(ap);
            }
        }
        sqlQuery.append(clBr);
        return buildSortQuery(sqlQuery.toString(), sortParam);
    }

    private String buildSortQuery(String query, String sortParam) {
        String sortQuery;
        if (sortParam.isEmpty()) {
            sortQuery = query;
        } else {
            sortQuery = new StringBuilder(query).append(ORDER_BY)
                    .append(sortParam)
                    .toString();
        }
        return sortQuery;
    }
}