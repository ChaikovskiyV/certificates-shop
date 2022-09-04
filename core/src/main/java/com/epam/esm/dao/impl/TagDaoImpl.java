package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.epam.esm.dao.QueryStorage.*;
import static com.epam.esm.dao.RequestParamName.CERTIFICATE_ID;
import static com.epam.esm.dao.RequestParamName.NAME;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The type TagDao.
 * <p>
 * This class implements the GiftCertificateDao interface.
 * This class makes and sends queries to database to execute create, read and delete operations with tags data.
 */
@Repository
public class TagDaoImpl implements TagDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Instantiates a new Tag dao.
     */
    public TagDaoImpl() {
        //default constructor without parameters
    }

    @Override
    public List<Tag> findAll() {
        return entityManager.createQuery(GET_ALL_TAGS, Tag.class).getResultList();
    }

    @Override
    public Tag findById(long id) {
        return entityManager.find(Tag.class, id);
    }

    @Override
    public List<Tag> findByName(String name) {
        return entityManager.createQuery(FIND_TAG_BY_NAME, Tag.class).setParameter(NAME, name).getResultList();
    }

    @Override
    public Tag insert(Tag tag) {
        return entityManager.merge(tag);
    }

    @Override
    public void delete(Tag tag) {
        entityManager.remove(tag);
    }

    @Override
    public List<Tag> findTagByCertificateId(long id) {
        return entityManager.createQuery(FIND_TAGS_BY_CERTIFICATE_ID, Tag.class)
                .setParameter(CERTIFICATE_ID, id)
                .getResultList();
    }

    @Override
    public List<Tag> findMostWidelyUsedTagOfUserWithHighestCostOfOrders() {
        return entityManager.createNativeQuery(FIND_MOST_WIDELY_USED_TAG_IN_USER_WITH_HIGHEST_COST_OF_ORDERS, Tag.class).getResultList();
    }
}