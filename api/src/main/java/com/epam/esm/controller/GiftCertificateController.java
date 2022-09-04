package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.PageDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.linkprovider.LinkProvider;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.epam.esm.dao.RequestParamName.*;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * <p>
 * The type GiftCertificateController.
 */
@RestController
@RequestMapping(value = "/api/v1/certificates")
public class GiftCertificateController {

    @Autowired
    private GiftCertificateService certificateService;
    @Autowired
    private LinkProvider linkProvider;

    /**
     * Create certificate response entity.
     *
     * @param certificateDto the certificate dto
     * @param bindingResult  the binding result
     * @return the responseEntity
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GiftCertificate> createCertificate(@Valid @RequestBody GiftCertificateDto certificateDto, BindingResult bindingResult) {
        GiftCertificate newCertificate = certificateService.addGiftCertificate(certificateDto, bindingResult);
        linkProvider.addLinkToCertificate(newCertificate);
        return new ResponseEntity<>(newCertificate, HttpStatus.CREATED);
    }

    /**
     * Find gift certificate by id gift certificate.
     *
     * @param id the id
     * @return the giftCertificate
     */
    @GetMapping(value = "/{id}")
    public HttpEntity<GiftCertificate> findGiftCertificateById(@PathVariable("id") long id) {
        GiftCertificate certificate = certificateService.findCertificateById(id);
        linkProvider.addLinkToCertificate(certificate);
        return new ResponseEntity<>(certificate, HttpStatus.OK);
    }

    /**
     * Find tags by certificate id set.
     *
     * @param id the id
     * @return the set
     */
    @GetMapping(value = "/{id}/tags")
    public Set<Tag> findTagsByCertificateId(@PathVariable("id") long id) {
        Set<Tag> tags = certificateService.findCertificateById(id).getTags();
        tags.forEach(linkProvider::addLinkToTag);
        return tags;
    }

    /**
     * Find orders by certificate id set.
     *
     * @param id the id
     * @return the set
     */
    @GetMapping(value = "/{id}/orders")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public Set<Order> findOrdersByCertificateId(@PathVariable("id") long id) {
        Set<Order> orders = certificateService.findCertificateById(id).getOrders();
        orders.forEach(linkProvider::addLinkToOrder);
        return orders;
    }

    /**
     * Find users by certificate id list.
     *
     * @param id the id
     * @return the list
     */
    @GetMapping(value = "/{id}/users")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public List<User> findUsersByCertificateId(@PathVariable("id") long id) {
        List<User> users = certificateService.findUsersByCertificateId(id);
        users.forEach(linkProvider::addLinkToUser);
        return users;
    }

    /**
     * Find certificates pageDto.
     *
     * @param name        the name
     * @param description the description
     * @param partOfWord  the part of word
     * @param tags        the tags
     * @param userId      the user id
     * @param sortParams  the sort params
     * @param limit       the limit
     * @param page        the page
     * @return the pageDto
     */
    @GetMapping
    public PageDto<GiftCertificate> findCertificates(@RequestParam(value = "name", required = false) String name,
                                                     @RequestParam(value = "description", required = false) String description,
                                                     @RequestParam(value = "partOfWord", required = false) String partOfWord,
                                                     @RequestParam(value = "tags", required = false) String tags,
                                                     @RequestParam(value = "userId", required = false) Long userId,
                                                     @RequestParam(value = "sortParams", required = false) String sortParams,
                                                     @RequestParam(value = "limit", required = false) Integer limit,
                                                     @RequestParam(value = "page", required = false) Integer page) {
        Map<String, Object> params = new HashMap<>();
        params.put(NAME, name);
        params.put(DESCRIPTION, description);
        params.put(PART_OF_WORD, partOfWord);
        params.put(TAGS, tags);
        params.put(USER_ID, userId);
        params.put(LIMIT, limit);
        params.put(PAGE, page);
        params.put(SORT_PARAMS, sortParams);
        PageDto<GiftCertificate> certificatesPage = certificateService.findCertificates(params);
        certificatesPage.getContent().forEach(linkProvider::addLinkToCertificate);
        return certificatesPage;
    }

    /**
     * Update certificate.
     *
     * @param id                 the id
     * @param giftCertificateDto the gift certificate dto
     * @return the httpEntity
     */
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public HttpEntity<GiftCertificate> updateCertificate(@PathVariable("id") long id, @RequestBody GiftCertificateDto giftCertificateDto) {
        GiftCertificate certificate = certificateService.updateGiftCertificate(giftCertificateDto, id);
        linkProvider.addLinkToCertificate(certificate);
        return new ResponseEntity<>(certificate, HttpStatus.OK);
    }

    /**
     * Delete by id.
     *
     * @param id the id
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteById(@PathVariable("id") long id) {
        certificateService.deleteGiftCertificate(id);
    }
}