package com.ewandian.b2b2c.search.service;

import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.ShopEntity;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;
import com.ewandian.b2b2c.search.domain.receive.ShopPageInfo;

import java.util.List;

/**
 * Created by suhd on 2016-11-28.
 */
public interface IShopService extends ICommonService<ShopEntity> {
    ShopEntity findOneByShopId(String shopId) throws EwandianSearchEngineException;

    List<ShopEntity> findShopsByShopName(SearchKeyWord skw) throws EwandianSearchEngineException;
    long getCount(String searchArg) throws EwandianSearchEngineException;

    List<ShopEntity> findShopsPageByShopEntity(ShopPageInfo shopPageInfo) throws EwandianSearchEngineException;
    long getFindShopsPageByShopEntityCount(ShopPageInfo shopPageInfo) throws EwandianSearchEngineException;

    List<ShopEntity> findShopsPageByShopIdList(SearchKeyWord skw) throws EwandianSearchEngineException;
    long getShopsPageByShopIdListCount(SearchKeyWord skw) throws EwandianSearchEngineException;
}
