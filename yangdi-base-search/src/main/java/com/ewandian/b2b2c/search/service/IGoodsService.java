package com.ewandian.b2b2c.search.service;

import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.GoodsEntity;
import com.ewandian.b2b2c.search.domain.receive.GoodsPageInfo;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;

import java.util.List;

/**
 * Created by suhd on 2016-11-22.
 */
public interface IGoodsService extends ICommonService<GoodsEntity> {
    List<GoodsEntity> findGoods(SearchKeyWord skw) throws EwandianSearchEngineException;
    long getCount(SearchKeyWord skw) throws EwandianSearchEngineException;

    GoodsEntity findOneByGoodsId(GoodsEntity goodsEntity) throws EwandianSearchEngineException;

    List<GoodsEntity> findPageListByGoodsEntity(GoodsPageInfo goodsPageInfo) throws EwandianSearchEngineException;
    long findListByGoodsEntityCount(GoodsPageInfo goodsPageInfo) throws EwandianSearchEngineException;

    List<GoodsEntity> findGoodsPageByGoodsIdList(SearchKeyWord skw) throws EwandianSearchEngineException;
    long findGoodsPageByGoodsIdListCount(SearchKeyWord skw) throws EwandianSearchEngineException;

    List<GoodsEntity> findGoodsPageByCategoryIdInPriceRange(GoodsPageInfo goodsPageInfo) throws EwandianSearchEngineException;
    long findGoodsPageByCategoryIdInPriceRangeCount(GoodsPageInfo goodsPageInfo) throws EwandianSearchEngineException;

    List<GoodsEntity> findGoodsPageByBrandIdInPriceRange(GoodsPageInfo goodsPageInfo) throws EwandianSearchEngineException;
    long findGoodsPageByBrandIdInPriceRangeCount(GoodsPageInfo goodsPageInfo) throws EwandianSearchEngineException;

    void addPopularityToGoods(String goodsId) throws EwandianSearchEngineException;
    void addCommentNumToGoods(String goodsId) throws EwandianSearchEngineException;
    void addSaleVolumeToGoods(String goodsId, int qty) throws EwandianSearchEngineException;

    List<GoodsEntity> fetchAllGoodsEntityByBrandId(String brandId) throws EwandianSearchEngineException;
    List<GoodsEntity> fetchAllGoodsEntityByCategoryId(String categoryId) throws EwandianSearchEngineException;

    void updateBrandNameByGoodsEntityWithGoodsIdAndBrandName(GoodsEntity goodsEntity) throws EwandianSearchEngineException;
    void updateCategoryNameByGoodsEntityWithGoodsIdAndCategoryName(GoodsEntity goodsEntity) throws EwandianSearchEngineException;
}
