package com.ewandian.b2b2c.search.domain.document;

import com.ewandian.b2b2c.search.app.constant.MmsegAnalyzerConstant;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
//import java.sql.Date;

/**
 * Created by Administrator on 2016/12/6.
 */
@Document(indexName = "adindex",type = "ad")
public class AdsEntity {
    @Id
    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String id;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String adId;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String columnId;


    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String columnIdentify;


    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String isDeleted;


    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String shopId;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String status;

    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            analyzer = MmsegAnalyzerConstant.mmseg_maxword,
            searchAnalyzer = MmsegAnalyzerConstant.mmseg_maxword
    )
    private String title;

    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            analyzer = MmsegAnalyzerConstant.mmseg_maxword,
            searchAnalyzer = MmsegAnalyzerConstant.mmseg_maxword
    )
    private String paperWork;


    @Field(
            type = FieldType.Date,
            index = FieldIndex.not_analyzed
    )
    private Date deliveryStartDate;


    @Field(
            type = FieldType.Date,
            index = FieldIndex.not_analyzed
    )
    private Date deliveryEndDate;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String seqNo;


    private String adImage;
    private String linkHref;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getColumnIdentify() {
        return columnIdentify;
    }

    public void setColumnIdentify(String columnIdentify) {
        this.columnIdentify = columnIdentify;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPaperWork() {
        return paperWork;
    }

    public void setPaperWork(String paperWork) {
        this.paperWork = paperWork;
    }

    public String getAdImage() {
        return adImage;
    }

    public void setAdImage(String adImage) {
        this.adImage = adImage;
    }

    public String getLinkHref() {
        return linkHref;
    }

    public void setLinkHref(String linkHref) {
        this.linkHref = linkHref;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDeliveryStartDate() {
        return deliveryStartDate;
    }

    public void setDeliveryStartDate(Date deliveryStartDate) {
        this.deliveryStartDate = deliveryStartDate;
    }

    public Date getDeliveryEndDate() {
        return deliveryEndDate;
    }

    public void setDeliveryEndDate(Date deliveryEndDate) {
        this.deliveryEndDate = deliveryEndDate;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }
}