package com.epam.esm.controller;

import com.epam.esm.dto.PageDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.linkprovider.LinkProvider;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.epam.esm.dao.RequestParamName.*;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * <p>
 * The type TagController.
 */
@RestController
@RequestMapping(value = "/api/v1/tags")
public class TagController {
    @Autowired
    private TagService tagService;
    @Autowired
    private LinkProvider linkProvider;

    /**
     * Find tag by id tag.
     *
     * @param id the id
     * @return the tag
     */
    @GetMapping("/{id}")
    public HttpEntity<Tag> findTagById(@PathVariable("id") long id) {
        Tag tag = tagService.findTagById(id);
        linkProvider.addLinkToTag(tag);
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    /**
     * Find tags list.
     *
     * @param name          the name
     * @param certificateId the certificate id
     * @param limit         the limit
     * @param page          the page
     * @return the list
     */
    @GetMapping
    public PageDto<Tag> findTags(@RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "certificateId", required = false) Long certificateId,
                                 @RequestParam(value = "limit", required = false) Integer limit,
                                 @RequestParam(value = "page", required = false) Integer page) {
        Map<String, Object> params = new HashMap<>();
        params.put(TAG_NAME, name);
        params.put(CERTIFICATE_ID, certificateId);
        params.put(LIMIT, limit);
        params.put(PAGE, page);
        PageDto<Tag> tagsPage = tagService.findTags(params);
        tagsPage.getContent().forEach(linkProvider::addLinkToTag);
        return tagsPage;
    }

    /**
     * Get the most widely used tag of user with the highest cost of orders pageDto.
     *
     * @param limit the limit
     * @param page  the page
     * @return the pageDto
     */
    @GetMapping(value = "/mostWidelyUsed")
    public PageDto<Tag> getMostWidelyUsedTagOfUserWithHighestCostOfOrders(@RequestParam(value = "limit", required = false) Integer limit,
                                                                       @RequestParam(value = "page", required = false) Integer page) {
        Map<String, Object> params = new HashMap<>();
        params.put(LIMIT, limit);
        params.put(PAGE, page);
        PageDto<Tag> tagsPage = tagService.findMostWidelyUsedTagOfUserWithHighestCostOfOrders(params);
        tagsPage.getContent().forEach(linkProvider::addLinkToTag);
        return tagsPage;
    }

    /**
     * Find certificate by tag id set.
     *
     * @param id the id
     * @return the set
     */
    @GetMapping(value = "/{id}/certificates")
    public Set<GiftCertificate> findCertificateByTagId(@PathVariable("id") long id) {
        Set<GiftCertificate> certificates = tagService.findTagById(id).getCertificates();
        certificates.forEach(linkProvider::addLinkToCertificate);
        return certificates;
    }
}