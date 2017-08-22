package com.ewandian.b2b2c.search.domain.receive;

import com.ewandian.b2b2c.search.app.constant.SearchSortingConstant;
import org.springframework.data.domain.Sort;

/**
 * Created by suhd on 2016-11-25.
 */
public class PageInfo {
    private int pageNumber = 1;
    private int pageSize = 10;
    private SearchSortingConstant sortBy = null;
    private Sort.Direction sortDirection = Sort.Direction.ASC;

    public PageInfo() {
    }

    public PageInfo(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public SearchSortingConstant getSortBy() {
        return sortBy;
    }

    public void setSortBy(SearchSortingConstant sortBy) {
        this.sortBy = sortBy;
    }

    public Sort.Direction getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(Sort.Direction sortDirection) {
        this.sortDirection = sortDirection;
    }
}
