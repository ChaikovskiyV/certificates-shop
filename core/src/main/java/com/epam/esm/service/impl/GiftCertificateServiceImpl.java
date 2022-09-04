package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.PageDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ApplicationDuplicateException;
import com.epam.esm.exception.ApplicationNotFoundException;
import com.epam.esm.exception.ApplicationNotValidDataException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.PaginationProvider;
import com.epam.esm.util.validator.DataValidator;
import com.epam.esm.util.validator.impl.DataValidatorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.epam.esm.dao.RequestParamName.*;
import static com.epam.esm.exception.ErrorAttribute.*;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The type GiftCertificateService.
 * <p>
 * This class implements the GiftCertificateService interface.
 * This class includes methods that process requests to gift certificate data from controller,
 * validate it and build data for query in database and call dao class methods.
 */
@Service()
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private static final String ASC = " asc";
    private static final String DESC = " desc";
    private static final String DELIMITER = ", ";
    private static final String EMPTY_STR = "";

    private GiftCertificateDao certificateDao;
    private TagDao tagDao;
    private DataValidator validator;
    private PaginationProvider paginationProvider;

    /**
     * Instantiates a new GiftCertificateService.
     *
     * @param certificateDao     the gift certificate dao
     * @param tagDao             the tag dao
     * @param validator          the validator
     * @param paginationProvider the pagination param provider
     */
    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao certificateDao, TagDao tagDao, DataValidatorImpl validator,
                                      PaginationProvider paginationProvider) {
        this.certificateDao = certificateDao;
        this.tagDao = tagDao;
        this.validator = validator;
        this.paginationProvider = paginationProvider;
    }

    /**
     * Instantiates a new GiftCertificateService.
     */
    public GiftCertificateServiceImpl() {
    }

    @Override
    public GiftCertificate findCertificateById(long certificateId) {
        if (!validator.isNumberValid(certificateId)) {
            throw new ApplicationNotValidDataException(NOT_VALID_ID_MESSAGE_KEY, certificateId);
        }
        return Optional.ofNullable(certificateDao.findById(certificateId)).orElseThrow(() ->
                new ApplicationNotFoundException(CERTIFICATE_NOT_FOUND_MESSAGE_KEY, certificateId));
    }

    @Override
    public PageDto<GiftCertificate> findCertificates(Map<String, Object> params) {
        List<GiftCertificate> certificates;
        String certificateName = (String) params.get(NAME);
        String tagNames = (String) params.get(TAGS);
        String description = (String) params.get(DESCRIPTION);
        String partOfWord = (String) params.get(PART_OF_WORD);
        Long userId = (Long) params.get(USER_ID);
        if (partOfWord != null && tagNames != null) {
            certificates = filterCertificatesByPartOfWordInNameOrDescription(partOfWord, findCertificatesByTags(tagNames, params));
        } else if (tagNames != null) {
            certificates = findCertificatesByTags(tagNames, params);
        } else if (partOfWord != null) {
            certificates = filterCertificatesByPartOfWordInNameOrDescription(partOfWord, certificateDao.findAll(buildSortParamStr(params)));
        } else if (certificateName != null) {
            certificates = !validator.isNameValid(certificateName) ? new ArrayList<>() : certificateDao
                    .findByName(certificateName, buildSortParamStr(params));
        } else if (description != null) {
            certificates = !validator.isDescriptionValid(description) ? new ArrayList<>() : certificateDao
                    .findByDescription(description, buildSortParamStr(params));
        } else if (userId != null) {
            if (!validator.isNumberValid(userId)) {
                throw new ApplicationNotValidDataException(NOT_VALID_ID_MESSAGE_KEY, userId);
            }
            certificates = certificateDao.findByUserId(userId, buildSortParamStr(params));
        } else {
            certificates = certificateDao.findAll(buildSortParamStr(params));
        }
        return paginationProvider.paginateData(certificates, params);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteGiftCertificate(long id) {
        GiftCertificate certificate = findCertificateById(id);
        certificateDao.delete(certificate);
        deleteUnusedTag(certificate.getTags());
    }

    @Override
    public List<User> findUsersByCertificateId(long id) {
        return findCertificateById(id).getOrders()
                .stream()
                .map(Order::getUser)
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public GiftCertificate addGiftCertificate(GiftCertificateDto giftCertificateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ApplicationNotValidDataException(NOT_VALID_DATA_MESSAGE_KEY, giftCertificateDto);
        }
        GiftCertificate certificate = findTheSame(giftCertificateDto);
        if (certificate != null) {
            throw new ApplicationDuplicateException(CERTIFICATE_DUPLICATE_MESSAGE_KEY, certificate);
        }
        return certificateDao.insert(buildCertificate(giftCertificateDto));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public GiftCertificate updateGiftCertificate(GiftCertificateDto certificateDto, long id) {
        GiftCertificate certificate = findCertificateById(id);
        Set<Tag> unusedTags = certificateDto.getTags().isEmpty() ? new HashSet<>() : certificate.getTags();
        GiftCertificate updated = certificateDao.update(prepareCertificate(certificate, certificateDto));
        deleteUnusedTag(unusedTags);
        return updated;
    }

    private GiftCertificate buildCertificate(GiftCertificateDto certificateDto) {
        return new GiftCertificate(certificateDto.getName(), certificateDto.getDescription(), certificateDto.getPrice(),
                certificateDto.getDuration(), getCurrentTime(), getCurrentTime(), checkTagsAndReplaceTheSame(certificateDto.getTags()));
    }

    private LocalDateTime getCurrentTime() {
        return LocalDateTime.now(ZoneId.systemDefault());
    }

    private List<String> buildTagNameList(String tagNames) {
        List<String> tagNameList = new ArrayList<>();
        for (String tagName : tagNames.split(DELIMITER)) {
            if (validator.isNameValid(tagName)) {
                tagNameList.add(tagName);
            }
        }
        return tagNameList;
    }

    private String buildSortParamStr(Map<String, Object> params) {
        StringBuilder sortParamBuilder = new StringBuilder();
        String sortParam = (String) params.get(SORT_PARAMS);
        if (sortParam != null) {
            String[] paramsArray = sortParam.split(DELIMITER);
            for (int i = 0; i < paramsArray.length; i++) {
                String param = paramsArray[i];
                if (validator.isNameValid(param) && isParamPresent(param) && (param.contains(DESC) || param.contains(ASC))) {
                    if (i > 0 && i <= paramsArray.length - 1) {
                        sortParamBuilder.append(DELIMITER).append(param);
                    } else {
                        sortParamBuilder.append(param);
                    }
                }
            }
        }
        return sortParamBuilder.toString();
    }

    private boolean isParamPresent(String param) {
        return CERTIFICATE_PARAM_LIST.stream().anyMatch(param::contains);
    }

    private GiftCertificate prepareCertificate(GiftCertificate certificate, GiftCertificateDto certificateDto) {
        Map<String, Object> updatedParams = new HashMap<>();
        String name = certificateDto.getName();
        String description = certificateDto.getDescription();
        int duration = certificateDto.getDuration();
        BigDecimal price = certificateDto.getPrice();
        Set<Tag> tags = certificateDto.getTags();
        if (name != null && !name.equals(certificate.getName())) {
            updatedParams.put(NAME, name);
        }
        if (description != null && !description.equals(certificate.getDescription())) {
            updatedParams.put(DESCRIPTION, description);
        }
        if (duration != 0 && duration != certificate.getDuration()) {
            updatedParams.put(DURATION, duration);
        }
        if (price != null && !price.equals(certificate.getPrice())) {
            updatedParams.put(PRICE, price);
        }
        if (!tags.isEmpty()) {
            updatedParams.put(TAGS, checkTagsAndReplaceTheSame(tags));
        }
        Map<String, Object> notCorrectParams = checkParams(updatedParams);
        if (!notCorrectParams.isEmpty()) {
            throw new ApplicationNotValidDataException(NOT_VALID_DATA_MESSAGE_KEY, notCorrectParams);
        }
        return updateFields(certificate, updatedParams);
    }

    private Set<Tag> checkTagsAndReplaceTheSame(Set<Tag> tagSet) {
        Set<Tag> tags = new HashSet<>();
        if (!validator.isTagsValid(tagSet)) {
            throw new ApplicationNotValidDataException(NOT_VALID_DATA_MESSAGE_KEY, tagSet);
        } else {
            tagSet.forEach(t -> {
                List<Tag> theSame = tagDao.findByName(t.getName());
                if (theSame.isEmpty()) {
                    tags.add(t);
                } else {
                    tags.add(theSame.get(0));
                }
            });
        }
        return tags;
    }

    private GiftCertificate updateFields(GiftCertificate certificate, Map<String, Object> params) {
        if (!params.isEmpty()) {
            certificate.setLastUpdateDate(getCurrentTime());
            for (var entry : params.entrySet()) {
                String key = entry.getKey();
                switch (key) {
                    case NAME -> certificate.setName(entry.getValue().toString());
                    case DESCRIPTION -> certificate.setDescription(entry.getValue().toString());
                    case DURATION -> certificate.setDuration((int) entry.getValue());
                    case PRICE -> certificate.setPrice((BigDecimal) entry.getValue());
                    case TAGS -> certificate.setTags((Set<Tag>) entry.getValue());
                }
            }
        }
        return certificate;
    }

    private GiftCertificate findTheSame(GiftCertificateDto certificateDto) {
        List<GiftCertificate> certificates = certificateDao.findByName(certificateDto.getName(), EMPTY_STR);
        return certificates.stream()
                .filter(c -> c.getName().equals(certificateDto.getName()) && c.getDescription().equals(certificateDto.getDescription()))
                .findAny()
                .orElse(null);
    }

    private Map<String, Object> checkParams(Map<String, Object> params) {
        Map<String, Object> notCorrectParam = new HashMap<>();
        for (var entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (key.equals(NAME) && !validator.isNameValid(value.toString())) {
                notCorrectParam.put(key, value);
            } else if (key.equals(DESCRIPTION) && !validator.isDescriptionValid(value.toString())) {
                notCorrectParam.put(key, value);
            } else if (key.equals(DURATION) && !validator.isNumberValid((int) value)) {
                notCorrectParam.put(key, value);
            } else if (key.equals(PRICE)) {
                BigDecimal price = (BigDecimal) value;
                if (!validator.isNumberValid(price.longValue())) {
                    notCorrectParam.put(key, value);
                }
            }
        }
        return notCorrectParam;
    }

    private void deleteUnusedTag(Set<Tag> tags) {
        tags.forEach(t -> {
            List<GiftCertificate> certificates = findCertificates(Map.of(TAGS, t.getName())).getContent();
            if (certificates.isEmpty()) {
                tagDao.delete(t);
            }
        });
    }

    private List<GiftCertificate> filterCertificatesByPartOfWordInNameOrDescription(String partOfWord, List<GiftCertificate> certificates) {
        return !validator.isParamValid(partOfWord) ? new ArrayList<>() : certificates
                .stream()
                .filter(c -> c.getDescription().contains(partOfWord) || c.getName().contains(partOfWord))
                .toList();
    }

    private List<GiftCertificate> findCertificatesByTags(String tagNames, Map<String, Object> params) {
        List<String> tags = buildTagNameList(tagNames);
        return tags.isEmpty() ? new ArrayList<>() : certificateDao.findByTagName(tags, buildSortParamStr(params));
    }
}