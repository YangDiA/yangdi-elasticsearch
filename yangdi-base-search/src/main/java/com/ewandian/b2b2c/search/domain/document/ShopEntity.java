package com.ewandian.b2b2c.search.domain.document;

import com.ewandian.b2b2c.search.app.constant.MmsegAnalyzerConstant;
import common.domain.annotation.Label;
import common.domain.annotation.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Created by suhd on 2016-11-28.
 */
@Document(indexName = "shopindex", type = "shop")
public class ShopEntity {
    @Id
    private String id;

    @NotEmpty
    @Label("shopId is not allowed to be null")
    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String shopId;

    @NotEmpty
    @Label("shopName is not allowed to be null")
    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            analyzer = MmsegAnalyzerConstant.mmseg_complex,
            searchAnalyzer = MmsegAnalyzerConstant.mmseg_maxword
    )
    private String shopName;
    private int recommendShop;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String status;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String soFrom;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String shopType;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String crmId;

    private String crmName;

    private String introduce;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String imgId;
    private String domainName;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String webVersionId;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String appVersionId;

    private String warehouseAddress;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String coordinateX;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String coordinateY;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String isSelfGetEnabled;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String isCODEnabled;

    private String weight;
    private String attentionQty;
    private String collectQty;
    private String goodRate;
    private String areaId;
    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            analyzer = MmsegAnalyzerConstant.mmseg_maxword,
            searchAnalyzer = MmsegAnalyzerConstant.mmseg_maxword
    )
    private String areaName;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String shopCode;
    private String major;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String shopBackImg;
    private String phone;
    private String businessHours;
    private String busRoutes;
    private String parking;
    private String invoiceTitle;

    @Field(
            type = FieldType.Double,
            index = FieldIndex.no
    )
    private double goodsRating;

    @Field(
            type = FieldType.Double,
            index = FieldIndex.no
    )
    private double logisticsRating;

    @Field(
            type = FieldType.Double,
            index = FieldIndex.no
    )
    private double customerRating;

    @Field(
            type = FieldType.Double,
            index = FieldIndex.no
    )
    private double rating;

    @Field(
            type = FieldType.String,
            index = FieldIndex.no
    )
    private String source;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getRecommendShop() {
        return recommendShop;
    }

    public void setRecommendShop(int recommendShop) {
        this.recommendShop = recommendShop;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSoFrom() {
        return soFrom;
    }

    public void setSoFrom(String soFrom) {
        this.soFrom = soFrom;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getCrmId() {
        return crmId;
    }

    public void setCrmId(String crmId) {
        this.crmId = crmId;
    }

    public String getCrmName() {
        return crmName;
    }

    public void setCrmName(String crmName) {
        this.crmName = crmName;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getWebVersionId() {
        return webVersionId;
    }

    public void setWebVersionId(String webVersionId) {
        this.webVersionId = webVersionId;
    }

    public String getAppVersionId() {
        return appVersionId;
    }

    public void setAppVersionId(String appVersionId) {
        this.appVersionId = appVersionId;
    }

    public String getWarehouseAddress() {
        return warehouseAddress;
    }

    public void setWarehouseAddress(String warehouseAddress) {
        this.warehouseAddress = warehouseAddress;
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

    public String getIsSelfGetEnabled() {
        return isSelfGetEnabled;
    }

    public void setIsSelfGetEnabled(String isSelfGetEnabled) {
        this.isSelfGetEnabled = isSelfGetEnabled;
    }

    public String getIsCODEnabled() {
        return isCODEnabled;
    }

    public void setIsCODEnabled(String isCODEnabled) {
        this.isCODEnabled = isCODEnabled;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAttentionQty() {
        return attentionQty;
    }

    public void setAttentionQty(String attentionQty) {
        this.attentionQty = attentionQty;
    }

    public String getCollectQty() {
        return collectQty;
    }

    public void setCollectQty(String collectQty) {
        this.collectQty = collectQty;
    }

    public String getGoodRate() {
        return goodRate;
    }

    public void setGoodRate(String goodRate) {
        this.goodRate = goodRate;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getShopBackImg() {
        return shopBackImg;
    }

    public void setShopBackImg(String shopBackImg) {
        this.shopBackImg = shopBackImg;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }

    public String getBusRoutes() {
        return busRoutes;
    }

    public void setBusRoutes(String busRoutes) {
        this.busRoutes = busRoutes;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public double getGoodsRating() {
        return goodsRating;
    }

    public void setGoodsRating(double goodsRating) {
        this.goodsRating = goodsRating;
    }

    public double getLogisticsRating() {
        return logisticsRating;
    }

    public void setLogisticsRating(double logisticsRating) {
        this.logisticsRating = logisticsRating;
    }

    public double getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(double customerRating) {
        this.customerRating = customerRating;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
