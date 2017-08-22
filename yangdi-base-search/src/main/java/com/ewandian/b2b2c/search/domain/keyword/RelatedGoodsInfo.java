package com.ewandian.b2b2c.search.domain.keyword;

import java.math.BigInteger;

/**
 * Created by suhd on 2016-11-23.
 */
public class RelatedGoodsInfo {
    private String goodsName;
    private BigInteger goodsNum;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public BigInteger getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(BigInteger goodsNum) {
        this.goodsNum = goodsNum;
    }
}
