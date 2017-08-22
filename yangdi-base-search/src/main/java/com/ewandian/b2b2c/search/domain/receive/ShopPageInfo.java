package com.ewandian.b2b2c.search.domain.receive;

import com.ewandian.b2b2c.search.domain.document.ShopEntity;

/**
 * Created by suhd on 2016-12-08.
 */
public class ShopPageInfo {
    private ShopEntity shopEntity;

    private String coordinateX;
    private String coordinateY;

    public ShopEntity getShopEntity() {
        return shopEntity;
    }

    public void setShopEntity(ShopEntity shopEntity) {
        this.shopEntity = shopEntity;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    private PageInfo pageInfo = new PageInfo();

    public String getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(String coordinateX) {
        this.coordinateX = coordinateX;
    }

    public String getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(String coordinateY) {
        this.coordinateY = coordinateY;
    }
}
