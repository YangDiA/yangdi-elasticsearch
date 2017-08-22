package com.ewandian.b2b2c.search.domain.keyword;

import java.util.List;

/**
 * Created by suhd on 2016-11-23.
 */
public class ReturnKeyWord {
    private List<RecommendShop> recommendShopList;
    private List<RelatedGoodsInfo> relatedGoodsInfoList;

    public List<RecommendShop> getRecommendShopList() {
        return recommendShopList;
    }

    public void setRecommendShopList(List<RecommendShop> recommendShopList) {
        this.recommendShopList = recommendShopList;
    }

    public List<RelatedGoodsInfo> getRelatedGoodsInfoList() {
        return relatedGoodsInfoList;
    }

    public void setRelatedGoodsInfoList(List<RelatedGoodsInfo> relatedGoodsInfoList) {
        this.relatedGoodsInfoList = relatedGoodsInfoList;
    }

}
