package com.ewandian.b2b2c.search.domain.document;

import com.ewandian.b2b2c.search.app.constant.MmsegAnalyzerConstant;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

//import java.sql.Date;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/20.
 */
@Document(indexName = "newscorrelationindex",type = "news")
public class NewsCorrelationEntity {
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
    private String correlationId;

    @Field(
            type = FieldType.Date,
            index = FieldIndex.not_analyzed
    )
    private Date createDate;


    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String cloumnIdentify;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String articleId;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String url;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String likeArticle;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String taunt;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String isEffective;

    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            analyzer = MmsegAnalyzerConstant.mmseg_maxword,
            searchAnalyzer = MmsegAnalyzerConstant.mmseg_maxword
    )
    private String correlationName;

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
    private String author;


    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            analyzer = MmsegAnalyzerConstant.mmseg_maxword,
            searchAnalyzer = MmsegAnalyzerConstant.mmseg_maxword
    )
    private String content;

    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            analyzer = MmsegAnalyzerConstant.mmseg_maxword,
            searchAnalyzer = MmsegAnalyzerConstant.mmseg_maxword
    )
    private String keyword;

    private String imgId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getCorrelationName() {
        return correlationName;
    }

    public void setCorrelationName(String correlationName) {
        this.correlationName = correlationName;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getImgId() { return imgId; }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCloumnIdentify() {
        return cloumnIdentify;
    }

    public void setCloumnIdentify(String cloumnIdentify) {
        this.cloumnIdentify = cloumnIdentify;
    }

    public String getIsEffective() {
        return isEffective;
    }

    public void setIsEffective(String isEffective) {
        this.isEffective = isEffective;
    }

    public String getLikeArticle() {
        return likeArticle;
    }

    public void setLikeArticle(String likeArticle) {
        this.likeArticle = likeArticle;
    }

    public String getTaunt() {
        return taunt;
    }

    public void setTaunt(String taunt) {
        this.taunt = taunt;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}