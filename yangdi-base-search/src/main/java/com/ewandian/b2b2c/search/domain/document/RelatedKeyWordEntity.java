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
 * Created by suhd on 2016-11-24.
 */
@Document(indexName = "relatedkeywordindex",type = "relatedkeyword")
public class RelatedKeyWordEntity {
    @Id
    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String id;

    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            analyzer = MmsegAnalyzerConstant.mmseg_maxword,
            searchAnalyzer = MmsegAnalyzerConstant.mmseg_maxword
    )
    @NotEmpty@Label("relatedKeyWord 不允许为空")
    private String relatedKeyWord;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    @NotEmpty@Label("relatedKeyWordNotAnalyzed 不允许为空")
    private String relatedKeyWordNotAnalyzed;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    @NotEmpty@Label("goodsId 不允许为空")
    private String goodsId;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    @NotEmpty@Label("shopId 不允许为空")
    private String shopId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRelatedKeyWord() {
        return relatedKeyWord;
    }

    public void setRelatedKeyWord(String relatedKeyWord) {
        this.relatedKeyWord = relatedKeyWord;
    }

    public String getRelatedKeyWordNotAnalyzed() {
        return relatedKeyWordNotAnalyzed;
    }

    public void setRelatedKeyWordNotAnalyzed(String relatedKeyWordNotAnalyzed) {
        this.relatedKeyWordNotAnalyzed = relatedKeyWordNotAnalyzed;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
