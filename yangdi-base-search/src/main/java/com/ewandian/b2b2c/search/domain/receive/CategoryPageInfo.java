package com.ewandian.b2b2c.search.domain.receive;

import com.ewandian.b2b2c.search.domain.document.CategoryEntity;

/**
 * Created by suhd on 2016-12-12.
 */
public class CategoryPageInfo {
    private CategoryEntity categoryEntity;
    private PageInfo pageInfo = new PageInfo();

    public CategoryEntity getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
}
