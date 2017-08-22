package com.ewandian.b2b2c.search.service.impl;

import com.ewandian.b2b2c.search.app.constant.QueryFieldConstant;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineNoDataException;
import com.ewandian.b2b2c.search.domain.document.CategoryEntity;
import com.ewandian.b2b2c.search.domain.receive.PageInfo;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;
import com.ewandian.b2b2c.search.repositories.CategoryRepository;
import com.ewandian.b2b2c.search.service.ICategoryService;
import com.ewandian.platform.util.StringUtil;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Created by suhd on 2016-11-28.
 */
@Service
@Scope("prototype")
public class CategoryService implements ICategoryService{
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    private Logger logger = LoggerFactory.getLogger(CategoryService.class);

    public void addOne(CategoryEntity categoryEntity) throws EwandianSearchEngineException {
        CategoryEntity obj = findOne(categoryEntity);
        if(obj==null) {
            //modified by XH YU on 2017/02/07
            //categoryEntity.setCategoryId(UUID.randomUUID().toString());
            categoryRepository.save(categoryEntity);
    }
    }

    public void updateOne(CategoryEntity categoryEntity) throws EwandianSearchEngineException {
        CategoryEntity obj = findOne(categoryEntity);
        if(obj!=null) {
            categoryEntity.setId(obj.getId());
        }
        categoryRepository.save(categoryEntity);
    }

    public void removeOne(CategoryEntity categoryEntity) throws EwandianSearchEngineException {
        CategoryEntity obj = findOne(categoryEntity);
        if(obj!=null) {
            categoryEntity.setId(obj.getId());
        }
            categoryRepository.delete(categoryEntity);
    }

    private CategoryEntity findOne(CategoryEntity categoryEntity) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withQuery(termQuery(QueryFieldConstant.categoryId, categoryEntity.getCategoryId()));
        List<CategoryEntity> categoryEntityList = elasticsearchTemplate.queryForList(nsqb.build(),CategoryEntity.class);
        if(categoryEntityList!=null && categoryEntityList.size()>0) {
            return categoryEntityList.get(0);
        }
        return null;
    }

    @Override
    public List<CategoryEntity> findAll(PageInfo pageInfo) throws EwandianSearchEngineException {
        Page<CategoryEntity> shopEntityPage = categoryRepository.findAll(new PageRequest(pageInfo.getPageNumber()-1,pageInfo.getPageSize()));
        List<CategoryEntity> shopEntityList = new ArrayList<CategoryEntity>();
        if(shopEntityPage!=null && shopEntityPage.getSize()>0) {
            shopEntityList = shopEntityPage.getContent();
        }
        return shopEntityList;
    }

    @Override
    public long getAllCount() throws EwandianSearchEngineException {
        return categoryRepository.count();
    }

    public List<CategoryEntity> getSibling(String categoryId, PageInfo pageInfo) throws EwandianSearchEngineException {
        if(StringUtils.isEmpty(categoryId)) {
            throw new EwandianSearchEngineException("参数传入错误：categoryId不允许为空！");
        }
        CategoryEntity paraCE = findOneByCategoryId(categoryId);
        List<String> siblingCategoryIdList = findDoHaveSuchGoodsCategoryList(paraCE.getParentId());
        List<CategoryEntity> siblingCategoryEntityList = findSiblingEntityListByIdList(siblingCategoryIdList,pageInfo);
        return siblingCategoryEntityList;
    }

    public long getSiblingCount(String categoryId, PageInfo pageInfo) throws EwandianSearchEngineException {
        CategoryEntity paraCE = findOneByCategoryId(categoryId);
        List<String> siblingCategoryIdList = findDoHaveSuchGoodsCategoryList(paraCE.getParentId());
        NativeSearchQueryBuilder nsqb = buildNSQBForGetSibling(siblingCategoryIdList,pageInfo);
        return elasticsearchTemplate.count(nsqb.build());
    }

    private List<String> findDoHaveSuchGoodsCategoryList(String parentId) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withIndices("goodsindex")
                .withTypes("goods")
                .withQuery(termQuery(QueryFieldConstant.parentCategoryIdInGoods, parentId));
        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("goodsFindCategoryAgg")
                .size(9999)
                .field(QueryFieldConstant.categoryId);
        SearchResponse searchResponse = elasticsearchTemplate.getClient()
                .prepareSearch("goodsindex")
                .setTypes("goods")
                .setQuery(nsqb.build().getQuery())
                .addAggregation(aggregationBuilder)
                .execute()
                .actionGet();
        Terms terms = searchResponse.getAggregations().get("goodsFindCategoryAgg");
        if(terms.getBuckets()==null || terms.getBuckets().size()<=0) {
            throw new EwandianSearchEngineNoDataException("搜索引擎在上架商品中找不到该品类的兄弟信息");
        }
        List<String> siblingCategoryIdList = new ArrayList<String>();
        for(Terms.Bucket bucket : terms.getBuckets()) {
            siblingCategoryIdList.add(bucket.getKey().toString());
        }
        return siblingCategoryIdList;
    }

    private List<CategoryEntity> findSiblingEntityListByIdList(List<String> categoryIdList,PageInfo pageInfo) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = buildNSQBForGetSibling(categoryIdList,pageInfo);
                List<CategoryEntity> siblingCategoryEntityList = elasticsearchTemplate.queryForList(nsqb.build(),CategoryEntity.class);
        if(siblingCategoryEntityList==null || siblingCategoryEntityList.size()<=0) {
            throw new EwandianSearchEngineNoDataException("搜索引擎在品类文档中找不到该品类的兄弟信息");
        }
        return siblingCategoryEntityList;
    }

    private NativeSearchQueryBuilder buildNSQBForGetSibling(List<String> categoryIdList,PageInfo pageInfo) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withIndices("categoryindex")
                .withTypes("category")
                .withQuery(termsQuery(QueryFieldConstant.categoryId, categoryIdList))
                .withPageable(new PageRequest(pageInfo.getPageNumber()-1,pageInfo.getPageSize()));
        return nsqb;
    }

    public CategoryEntity findOneByCategoryId(String categoryId) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withQuery(termQuery(QueryFieldConstant.categoryId, categoryId));
        List<CategoryEntity> siblingCategoryEntityList = elasticsearchTemplate.queryForList(nsqb.build(),CategoryEntity.class);
        if(siblingCategoryEntityList==null || siblingCategoryEntityList.size()<=0) {
            throw new EwandianSearchEngineNoDataException("搜索引擎在品类文档中找不到该品类的信息");
        }
        return siblingCategoryEntityList.get(0);
    }

    @Override
    public List<CategoryEntity> findCategoriesByShopId(SearchKeyWord skw) throws EwandianSearchEngineException{
        if(StringUtil.isNullOrEmpty(skw.getSearchArg())) {
            throw new EwandianSearchEngineException("参数传入错误：searchArg不允许为空！");
        }
        List<CategoryEntity> categoryEntityList = new ArrayList<CategoryEntity>();
        List<String> categoryIdList = findDoHaveSuchGoodsCategoryListByShopId(skw.getSearchArg());
        categoryEntityList.addAll(findCategoryEntityList(categoryIdList));
        categoryEntityList.addAll(findParentCategoryEntityList(categoryEntityList));
        return categoryEntityList;
    }

    private List<String> findDoHaveSuchGoodsCategoryListByShopId(String shopId) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withIndices("goodsindex")
                .withTypes("goods")
                .withQuery(termQuery(QueryFieldConstant.shopId, shopId))
                .withFields(QueryFieldConstant.categoryId,QueryFieldConstant.categoryName);
        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("goodsFindCategoryByShopIdAgg")
                .size(9999)
                .field(QueryFieldConstant.categoryId);
        SearchResponse searchResponse = elasticsearchTemplate.getClient()
                .prepareSearch("goodsindex")
                .setTypes("goods")
                .setQuery(nsqb.build().getQuery())
                .addAggregation(aggregationBuilder)
                .execute()
                .actionGet();
        Terms terms = searchResponse.getAggregations().get("goodsFindCategoryByShopIdAgg");
        if(terms.getBuckets()==null || terms.getBuckets().size()<=0) {
            throw new EwandianSearchEngineNoDataException("搜索引擎在上架商品中找不到该品类信息");
        }
        Set<String> categoryIdSet = new HashSet<String>();
        for(Terms.Bucket bucket : terms.getBuckets()) {
            categoryIdSet.add(bucket.getKey().toString());
        }
        return new ArrayList<String>(categoryIdSet);
    }

    private List<CategoryEntity> findCategoryEntityList(List<String> categoryIdList) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withQuery(termsQuery(QueryFieldConstant.categoryId, categoryIdList))
                .withPageable(new PageRequest(0,9999));
        List<CategoryEntity> categoryEntityList = elasticsearchTemplate.queryForList(nsqb.build(),CategoryEntity.class);
        if(categoryEntityList==null || categoryEntityList.size()<=0) {
            throw new EwandianSearchEngineNoDataException("搜索引擎在品类文档中找不到该品类信息");
        }
        return categoryEntityList;
    }

    private List<CategoryEntity> findParentCategoryEntityList(List<CategoryEntity> categoryEntityList) throws EwandianSearchEngineException {
        Set<String> parentIdSet = new HashSet<String>();
        for(int i=0; i<categoryEntityList.size(); i++) {
            parentIdSet.add(categoryEntityList.get(i).getParentId());
        }
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withQuery(termsQuery(QueryFieldConstant.categoryId, new ArrayList<String>(parentIdSet)))
                .withPageable(new PageRequest(0,9999));
        List<CategoryEntity> retCategoryEntityList = elasticsearchTemplate.queryForList(nsqb.build(),CategoryEntity.class);
        return retCategoryEntityList;
    }

    public List<CategoryEntity> CategoriesOnlyLevelZeroAndOne() throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withIndices("categoryindex")
                .withTypes("category")
                .withQuery(boolQuery().should(termQuery(QueryFieldConstant.level,0)).should(termQuery(QueryFieldConstant.level,1)))
                .withPageable(new PageRequest(0,9999));
        List<CategoryEntity> categoryEntityList = elasticsearchTemplate.queryForList(nsqb.build(),CategoryEntity.class);
        if(categoryEntityList==null || categoryEntityList.size()<=0) {
            throw new EwandianSearchEngineNoDataException("搜索引擎在品类文档中找不到该品类信息");
        }
        return categoryEntityList;
    }

    public CategoryEntity findParentCategoryByCategoryId(String categoryId) throws EwandianSearchEngineException {
        CategoryEntity categoryEntity = findOneByCategoryId(categoryId);
        if(categoryEntity==null || StringUtil.isNullOrEmpty(categoryEntity.getParentId())) {
            throw new EwandianSearchEngineNoDataException("该品类没有父品类信息");
        }
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withQuery(termQuery(QueryFieldConstant.categoryId, categoryEntity.getParentId()))
                .withPageable(new PageRequest(0,9999));
        List<CategoryEntity> categoryEntityList = elasticsearchTemplate.queryForList(nsqb.build(),CategoryEntity.class);
        if(categoryEntityList==null || categoryEntityList.size()<=0) {
            throw new EwandianSearchEngineNoDataException("搜索引擎在品类文档中找不到该品类的父品类信息");
        }
        return categoryEntityList.get(0);
    }

    public List<CategoryEntity> findFullParentCategoriesListByCategoryId(SearchKeyWord skw) throws EwandianSearchEngineException {
        CategoryEntity categoryEntity = findOneByCategoryId(skw.getSearchArg());
        if(categoryEntity==null || StringUtil.isNullOrEmpty(categoryEntity.getParentId())) {
            throw new EwandianSearchEngineNoDataException("该品类没有父品类信息");
        }
        CategoryEntity tempEntity = findOneByCategoryId(skw.getSearchArg());
        List<CategoryEntity> categoryEntityList = new ArrayList<CategoryEntity>();
        for(int i=0; i<Integer.valueOf(categoryEntity.getLevel()); i++) {
            NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                    .withQuery(termQuery(QueryFieldConstant.categoryId, tempEntity.getParentId()))
                    .withPageable(new PageRequest(0,9999));
            List<CategoryEntity> tempEntityList = elasticsearchTemplate.queryForList(nsqb.build(),CategoryEntity.class);
            if(tempEntityList!=null || tempEntityList.size()>0) {
                categoryEntityList.add(tempEntityList.get(0));
            }
            tempEntity = findOneByCategoryId(tempEntity.getParentId());
        }
        Collections.reverse(categoryEntityList);
        //including itself
        categoryEntityList.add(categoryEntity);
        return categoryEntityList;
    }

}
