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
@Document(indexName = "categoryindex", type = "category")
public class CategoryEntity {
    @Id
    private String id;

    @NotEmpty
    @Label("categoryId is not allowed to be null")
    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String categoryId;

    @NotEmpty
    @Label("categoryName is not allowed to be null")
    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            analyzer = MmsegAnalyzerConstant.mmseg_complex,
            searchAnalyzer = MmsegAnalyzerConstant.mmseg_maxword
    )
    private String categoryName;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String fullId;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String fullName;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String parentId;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String level;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String seqNo;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String isFinalStage;

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

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getIsFinalStage() {
        return isFinalStage;
    }

    public void setIsFinalStage(String isFinalStage) {
        this.isFinalStage = isFinalStage;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }
}
