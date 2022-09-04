package com.epam.esm.dto;

import com.epam.esm.entity.AbstractEntity;

import java.util.List;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * <p>
 * The type PageDto.
 * This class includes data for pagination.
 */
public class PageDto<T extends AbstractEntity> {
    private final List<T> content;
    private final int currentPage;
    private final int size;
    private final int totalElements;
    private final int totalPages;

    /**
     * Instantiates a new PageDto.
     *
     * @param content       the content
     * @param currentPage   the currentPage
     * @param size          the size
     * @param totalElements the totalElements
     * @param totalPages    the totalPages
     */
    public PageDto(List<T> content, int currentPage, int size, int totalElements, int totalPages) {
        this.content = content;
        this.currentPage = currentPage;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public List<T> getContent() {
        return content;
    }

    /**
     * Gets currentPage.
     *
     * @return the currentPage
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * Gets size.
     *
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * Gets totalElements.
     *
     * @return the totalElements
     */
    public int getTotalElements() {
        return totalElements;
    }

    /**
     * Gets totalPages.
     *
     * @return the totalPages
     */
    public int getTotalPages() {
        return totalPages;
    }

    @Override
    public String toString() {
        return new StringBuilder("Page{")
                .append("currentPage=")
                .append(currentPage)
                .append(", size=")
                .append(size)
                .append(", totalPages=")
                .append(totalPages)
                .append(", totalElements=")
                .append(totalElements)
                .append(", content=")
                .append(content)
                .append('}')
                .toString();
    }
}