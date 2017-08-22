package com.ewandian.b2b2c.search.domain.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Created by suhd on 2016-12-14.
 */
@Document(indexName = "shopoverallratingindex", type = "shopoverallrating")
public class ShopOverAllRatingEntity {
    @Id
    private String id;
    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String shopId;

    private double goodsRating;

    private double logisticsRating;

    private double customerRating;

    private double rating;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
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
