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
 * Created by suhd on 2016-11-29.
 */
@Document(indexName = "brandindex", type = "brand")
public class BrandEntity {
    @Id
    private String id;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    @NotEmpty
    @Label("brandId 不允许为空")
    private String brandId;

    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            analyzer = MmsegAnalyzerConstant.mmseg_complex,
            searchAnalyzer = MmsegAnalyzerConstant.mmseg_maxword
    )
    @NotEmpty
    @Label("brandName 不允许为空")
    private String brandName;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String addrName;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String imgId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAddrName() {
        return addrName;
    }

    public void setAddrName(String addrName) {
        this.addrName = addrName;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }
}
