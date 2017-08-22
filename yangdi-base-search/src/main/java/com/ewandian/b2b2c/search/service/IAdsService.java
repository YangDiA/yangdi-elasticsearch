package com.ewandian.b2b2c.search.service;

import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.AdsEntity;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;

import java.util.List;

/**
 * Created by Administrator on 2016/12/6.
 */
public interface IAdsService extends ICommonService<AdsEntity> {
    List<AdsEntity> findAds(SearchKeyWord skw) throws EwandianSearchEngineException;
    AdsEntity findOne(AdsEntity adsEntity) throws EwandianSearchEngineException;
    List<AdsEntity> findAdsByAdsList(SearchKeyWord skw) throws EwandianSearchEngineException;
    List <AdsEntity> findByColumnIdentifyAndShopIdList(SearchKeyWord skw) throws EwandianSearchEngineException;
    long findAdsByAdsListCount(SearchKeyWord skw) throws EwandianSearchEngineException;
    long findByColumnIdentifyAndShopIdListCount(SearchKeyWord skw) throws EwandianSearchEngineException;
    long getCount(String searchArg) throws EwandianSearchEngineException;

    List<AdsEntity> test(SearchKeyWord skw) throws EwandianSearchEngineException;
}