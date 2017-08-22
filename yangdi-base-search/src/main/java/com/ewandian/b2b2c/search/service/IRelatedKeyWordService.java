package com.ewandian.b2b2c.search.service;

import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.RelatedKeyWordEntity;
import com.ewandian.b2b2c.search.domain.keyword.RelatedGoodsInfo;
import com.ewandian.b2b2c.search.domain.keyword.ReturnKeyWord;
import com.ewandian.b2b2c.search.domain.receive.RelatedKeyWordPageInfo;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;

import java.util.List;

/**
 * Created by suhd on 2016-11-24.
 */
public interface IRelatedKeyWordService extends ICommonService<RelatedKeyWordEntity> {

    ReturnKeyWord findWithAggCount(SearchKeyWord rkw) throws EwandianSearchEngineException;

    List<RelatedGoodsInfo> getRelatedGoodsInfoForAddInfoToHotspot(SearchKeyWord rkw);

    List<RelatedKeyWordEntity> findRelatedKeyWordEntityPageByShopId(RelatedKeyWordPageInfo relatedKeyWordPageInfo) throws Exception;
}
