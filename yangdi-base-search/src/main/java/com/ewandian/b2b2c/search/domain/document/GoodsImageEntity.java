package com.ewandian.b2b2c.search.domain.document;

import common.domain.annotation.Label;
import common.domain.annotation.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * Created by suhd on 2016-12-01.
 */
@Document(indexName = "goodsimageindex", type = "goodsimage")
public class GoodsImageEntity {
    @Id
    private String id;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String goodsId;

    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String seqNo;

    @NotEmpty
    @Label("imgId is not allowed to be null")
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

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }
}