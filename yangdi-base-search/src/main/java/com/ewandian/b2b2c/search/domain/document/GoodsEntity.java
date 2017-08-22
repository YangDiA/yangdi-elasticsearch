package com.ewandian.b2b2c.search.domain.document;

import com.ewandian.b2b2c.search.app.constant.MmsegAnalyzerConstant;
import common.domain.annotation.Label;
import common.domain.annotation.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by suhd on 2016-11-21.
 */
@Document(indexName = "goodsindex",type = "goods")
public class GoodsEntity {

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
    @NotEmpty@Label("shopId 不允许为空")
    private String shopId;

    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            analyzer = MmsegAnalyzerConstant.mmseg_complex,
            searchAnalyzer = MmsegAnalyzerConstant.mmseg_maxword
    )
    @NotEmpty@Label("shopName 不允许为空")
    private String shopName;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    @NotEmpty@Label("goodsId 不允许为空")
    private String goodsId;

    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            analyzer = MmsegAnalyzerConstant.mmseg_maxword,
            searchAnalyzer = MmsegAnalyzerConstant.mmseg_maxword
    )
    @NotEmpty@Label("goodsName 不允许为空")
    private String goodsName;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String imgId;

    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            analyzer = MmsegAnalyzerConstant.mmseg_maxword,
            searchAnalyzer = MmsegAnalyzerConstant.mmseg_maxword
    )
    private String description;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String detail;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String packList;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String afterService;

    private int qty;

    private String isPresellEnabled;

    private String deliverMsg;

    private String isPostFree;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String templateId;

    private BigDecimal lmsVolume;

    private BigDecimal lmsWeight;

    private BigDecimal marketPrice;

    private BigDecimal salePrice;

    private String goodsLabel;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String parentCategoryId;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    @NotEmpty@Label("categoryId 不允许为空")
    private String categoryId;

    //The following four fields are added by XH YU on 2017/02/09
    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String isup;


    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String seokeywords;


    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            analyzer = MmsegAnalyzerConstant.mmseg_maxword,
            searchAnalyzer = MmsegAnalyzerConstant.mmseg_maxword
    )
    private String seotitle;


    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            analyzer = MmsegAnalyzerConstant.mmseg_maxword,
            searchAnalyzer = MmsegAnalyzerConstant.mmseg_maxword
    )
    private String seodescription;


    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            analyzer = MmsegAnalyzerConstant.mmseg_maxword,
            searchAnalyzer = MmsegAnalyzerConstant.mmseg_maxword
    )
    @NotEmpty@Label("categoryName 不允许为空")
    private String categoryName;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    @NotEmpty@Label("brandId 不允许为空")
    private String brandId;

    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            analyzer = MmsegAnalyzerConstant.mmseg_maxword,
            searchAnalyzer = MmsegAnalyzerConstant.mmseg_maxword
    )
    @NotEmpty@Label("brandName 不允许为空")
    private String brandName;

    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            analyzer = MmsegAnalyzerConstant.mmseg_maxword,
            searchAnalyzer = MmsegAnalyzerConstant.mmseg_maxword
    )
    @NotEmpty@Label("model 不允许为空")
    private String model;
    private String spec;

    private BigDecimal promotionPrice;

    private List<GoodsImageEntity> goodsImageEntityList;

    @Field(
            type = FieldType.Integer
    )
    private int goodsPopularity;
    @Field(
            type = FieldType.Integer
    )
    private int goodsSaleVolume;
    @Field(
            type = FieldType.Integer
    )
    private int goodsCommentNum;
    @NotEmpty@Label("goodsUpDate 不允许为空")
    private Timestamp goodsUpDate;

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

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPackList() {
        return packList;
    }

    public void setPackList(String packList) {
        this.packList = packList;
    }

    public String getAfterService() {
        return afterService;
    }

    public void setAfterService(String afterService) {
        this.afterService = afterService;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getIsPresellEnabled() {
        return isPresellEnabled;
    }

    public void setIsPresellEnabled(String isPresellEnabled) {
        this.isPresellEnabled = isPresellEnabled;
    }

    public String getDeliverMsg() {
        return deliverMsg;
    }

    public void setDeliverMsg(String deliverMsg) {
        this.deliverMsg = deliverMsg;
    }

    public String getIsPostFree() {
        return isPostFree;
    }

    public void setIsPostFree(String isPostFree) {
        this.isPostFree = isPostFree;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public BigDecimal getLmsVolume() {
        return lmsVolume;
    }

    public void setLmsVolume(BigDecimal lmsVolume) {
        this.lmsVolume = lmsVolume;
    }

    public BigDecimal getLmsWeight() {
        return lmsWeight;
    }

    public void setLmsWeight(BigDecimal lmsWeight) {
        this.lmsWeight = lmsWeight;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public String getGoodsLabel() {
        return goodsLabel;
    }

    public void setGoodsLabel(String goodsLabel) {
        this.goodsLabel = goodsLabel;
    }

    public String getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(String parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public BigDecimal getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(BigDecimal promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    public List<GoodsImageEntity> getGoodsImageEntityList() {
        return goodsImageEntityList;
    }

    public void setGoodsImageEntityList(List<GoodsImageEntity> goodsImageEntityList) {
        this.goodsImageEntityList = goodsImageEntityList;
    }

    public int getGoodsPopularity() {
        return goodsPopularity;
    }

    public void setGoodsPopularity(int goodsPopularity) {
        this.goodsPopularity = goodsPopularity;
    }

    public int getGoodsSaleVolume() {
        return goodsSaleVolume;
    }

    public void setGoodsSaleVolume(int goodsSaleVolume) {
        this.goodsSaleVolume = goodsSaleVolume;
    }

    public int getGoodsCommentNum() {
        return goodsCommentNum;
    }

    public void setGoodsCommentNum(int goodsCommentNum) {
        this.goodsCommentNum = goodsCommentNum;
    }

    public Timestamp getGoodsUpDate() {
        return goodsUpDate;
    }

    public void setGoodsUpDate(Timestamp goodsUpDate) {
        this.goodsUpDate = goodsUpDate;
    }

    public String getIsup() {
        return isup;
    }

    public void setIsup(String isup) {
        this.isup = isup;
    }

    public String getSeokeywords() {
        return seokeywords;
    }

    public void setSeokeywords(String seokeywords) {
        this.seokeywords = seokeywords;
    }

    public String getSeotitle() {
        return seotitle;
    }

    public void setSeotitle(String seotitle) {
        this.seotitle = seotitle;
    }

    public String getSeodescription() {
        return seodescription;
    }

    public void setSeodescription(String seodescription) {
        this.seodescription = seodescription;
    }
}
