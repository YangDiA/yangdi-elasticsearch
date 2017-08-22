package com.ewandian.b2b2c.search.domain.receive;

import com.ewandian.b2b2c.search.domain.document.GoodsEntity;

import java.math.BigDecimal;

/**
 * Created by suhd on 2016-11-30.
 */
public class GoodsPageInfo {
    private GoodsEntity goodsEntity;
    private BigDecimal fluctuatingFigure = BigDecimal.ZERO;
    private PageInfo pageInfo = new PageInfo();

    public GoodsEntity getGoodsEntity() {
        return goodsEntity;
    }

    public void setGoodsEntity(GoodsEntity goodsEntity) {
        this.goodsEntity = goodsEntity;
    }

    public BigDecimal getFluctuatingFigure() {
        return fluctuatingFigure;
    }

    public void setFluctuatingFigure(BigDecimal fluctuatingFigure) {
        this.fluctuatingFigure = fluctuatingFigure;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
}
