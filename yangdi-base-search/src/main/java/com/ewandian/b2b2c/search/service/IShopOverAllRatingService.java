package com.ewandian.b2b2c.search.service;

import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.ShopOverAllRatingEntity;
import com.ewandian.b2b2c.search.domain.receive.PageInfo;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;

import java.util.List;

/**
 * Created by suhd on 2016-12-14.
 */
public interface IShopOverAllRatingService extends ICommonService<ShopOverAllRatingEntity> {
    ShopOverAllRatingEntity findOneByShopId(SearchKeyWord skw) throws EwandianSearchEngineException;
    List<ShopOverAllRatingEntity> findShopRatingsByShopIdList(List<String> shopIdList, PageInfo pageInfo) throws EwandianSearchEngineException;
}
