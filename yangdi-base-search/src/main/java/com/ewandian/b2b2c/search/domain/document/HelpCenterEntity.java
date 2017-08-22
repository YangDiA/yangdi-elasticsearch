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
 * Created by Administrator on 2016/12/8.
 */
@Document(indexName = "helpcenterindex",type = "helpcenter")
public class HelpCenterEntity {
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
    private String replyId;


    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String typeId;


    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            analyzer = MmsegAnalyzerConstant.mmseg_maxword,
            searchAnalyzer = MmsegAnalyzerConstant.mmseg_maxword
    )
    private String askContent;


    @Field(
            type = FieldType.String,
            index = FieldIndex.analyzed,
            analyzer = MmsegAnalyzerConstant.mmseg_maxword,
            searchAnalyzer = MmsegAnalyzerConstant.mmseg_maxword
    )
    private String replayContent;

    @Field(
            type = FieldType.Date,
            index = FieldIndex.not_analyzed
    )
    private Date replayTime;

    @Field(
            type = FieldType.Date,
            index = FieldIndex.not_analyzed
    )
    private Date askTime;

    private String helpLike;
    private String auditState;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getTypeId() { return typeId; }

    public void setTypeId(String entityName) { this.typeId = typeId; }

    public String getAskContent() { return askContent; }

    public void setAskContent(String askContent) {
        this.askContent = askContent;
    }

    public String getReplayContent() {
        return replayContent;
    }

    public void setReplayContent(String replayContent) {
        this.replayContent = replayContent;
    }

    public String getHelpLike() { return helpLike; }

    public void setHelpLike(String helpLike) {
        this.helpLike = helpLike;
    }

    public String getAuditState() { return auditState; }

    public void setAuditState(String auditState) {
        this.auditState = auditState;
    }

    public Date getAskTime() {
        return askTime;
    }

    public void setAskTime(Date askTime) {
        this.askTime = askTime;
    }

    public Date getReplayTime() {
        return replayTime;
    }

    public void setReplayTime(Date replayTime) {
        this.replayTime = replayTime;
    }
}