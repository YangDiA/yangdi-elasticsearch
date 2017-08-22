package com.ewandian.b2b2c.search.app.constant;

/**
 * Created by suhd on 2016-11-23.
 */
public enum  SearchSortingConstant {
    likeArticle("likeArticle"),
    taunt("taunt"),
    createDate("createDate"),
    seqNo("seqNo"),
    replayTime("replayTime"),
    askTime("askTime"),
    salePrice("salePrice"),
    goodsSaleVolume("goodsSaleVolume"),
    goodsCommentNum("goodsCommentNum"),
    goodsUpDate("goodsUpDate"),
    goodsPopularity("goodsPopularity");

    private String value;
    SearchSortingConstant(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }
}
