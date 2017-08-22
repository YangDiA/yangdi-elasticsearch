package com.ewandian.b2b2c.search.domain.receive;

import java.util.List;

/**
 * Created by suhd on 2016-11-25.
 */
public class SearchKeyWord {
    private String searchArg;
    private PageInfo pageInfo = new PageInfo();
    private String areaName;
    private String coordinateX;
    private String coordinateY;
    private List<String> goodsIdList;
    private List<String> brandIdList;
    private List<String> categoryIdList;
    private List<String> shopIdList;
    private List<String> newsIdList;
    private List<String> adsIdList;
    private List<String> helpCenterIdList;

    public String getSearchArg() {
        return searchArg;
    }

    public void setSearchArg(String searchArg) {
        this.searchArg = searchArg;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

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

    public List<String> getGoodsIdList() {
        return goodsIdList;
    }

    public void setGoodsIdList(List<String> goodsIdList) {
        this.goodsIdList = goodsIdList;
    }

    public List<String> getBrandIdList() {
        return brandIdList;
    }

    public void setBrandIdList(List<String> brandIdList) {
        this.brandIdList = brandIdList;
    }

    public List<String> getCategoryIdList() {
        return categoryIdList;
    }

    public void setCategoryIdList(List<String> categoryIdList) {
        this.categoryIdList = categoryIdList;
    }

    public List<String> getShopIdList() {
        return shopIdList;
    }

    public void setShopIdList(List<String> shopIdList) {
        this.shopIdList = shopIdList;
    }

    public List<String> getNewsIdList() {
        return newsIdList;
    }

    public void setNewsIdList(List<String> newsIdList) {
        this.newsIdList = newsIdList;
    }

    public List<String> getAdsIdList() {
        return adsIdList;
    }

    public void setAdsIdList(List<String> adsIdList) {
        this.adsIdList = adsIdList;
    }

    public List<String> getHelpCenterIdList() {
        return helpCenterIdList;
    }

    public void setHelpCenterIdList(List<String> helpCenterIdList) {
        this.helpCenterIdList = helpCenterIdList;
    }
}
