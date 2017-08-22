package com.ewandian.b2b2c.search.domain.receive;

import com.ewandian.b2b2c.search.domain.document.BrandEntity;

/**
 * Created by suhd on 2016-12-12.
 */
public class BrandPageInfo {
    private BrandEntity brandEntity;
    private String categoryId;
    private PageInfo pageInfo = new PageInfo();

    public BrandEntity getBrandEntity() {
        return brandEntity;
    }

    public void setBrandEntity(BrandEntity brandEntity) {
        this.brandEntity = brandEntity;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
}
