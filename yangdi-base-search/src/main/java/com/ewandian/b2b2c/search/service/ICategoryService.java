package com.ewandian.b2b2c.search.service;

import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.CategoryEntity;
import com.ewandian.b2b2c.search.domain.receive.PageInfo;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;

import java.util.List;

/**
 * Created by suhd on 2016-11-28.
 */
public interface ICategoryService extends ICommonService<CategoryEntity> {
    List<CategoryEntity> getSibling(String categoryId, PageInfo pageInfo) throws EwandianSearchEngineException;
    long getSiblingCount(String categoryId, PageInfo pageInfo) throws EwandianSearchEngineException;

    CategoryEntity findOneByCategoryId(String categoryId) throws EwandianSearchEngineException;

    List<CategoryEntity> findCategoriesByShopId(SearchKeyWord skw) throws EwandianSearchEngineException;
    List<CategoryEntity> CategoriesOnlyLevelZeroAndOne() throws EwandianSearchEngineException;
    CategoryEntity findParentCategoryByCategoryId(String categoryId) throws EwandianSearchEngineException;

    List<CategoryEntity> findFullParentCategoriesListByCategoryId(SearchKeyWord skw) throws EwandianSearchEngineException;
}
