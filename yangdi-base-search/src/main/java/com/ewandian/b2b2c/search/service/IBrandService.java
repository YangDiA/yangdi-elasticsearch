package com.ewandian.b2b2c.search.service;

import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.BrandEntity;
import com.ewandian.b2b2c.search.domain.receive.PageInfo;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;

import java.util.List;

/**
 * Created by suhd on 2016-11-29.
 */
public interface IBrandService extends ICommonService<BrandEntity> {
    List<BrandEntity> getSibling(String brandId, String categoryId, PageInfo pageInfo) throws EwandianSearchEngineException;
    long getSiblingCount(String brandId, String categoryId, PageInfo pageInfo) throws EwandianSearchEngineException;

    List<BrandEntity> findBrandsByShopId(SearchKeyWord skw) throws EwandianSearchEngineException;

    List<BrandEntity> findBrandsPageByShopId(SearchKeyWord skw) throws EwandianSearchEngineException;
    long findBrandsPageByShopIdCount(SearchKeyWord skw) throws EwandianSearchEngineException;

    List<BrandEntity> findBrandsPageBySearchArg(SearchKeyWord skw) throws EwandianSearchEngineException;
    long findBrandsPageBySearchArgCount(SearchKeyWord skw) throws EwandianSearchEngineException;
}