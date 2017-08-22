package com.ewandian.b2b2c.search.service.impl;

import com.ewandian.b2b2c.search.app.constant.QueryFieldConstant;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineNoDataException;
import com.ewandian.b2b2c.search.domain.document.BrandEntity;
import com.ewandian.b2b2c.search.domain.receive.PageInfo;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;
import com.ewandian.b2b2c.search.repositories.BrandRepository;
import com.ewandian.b2b2c.search.service.IBrandService;
import com.ewandian.platform.util.StringUtil;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Created by suhd on 2016-11-29.
 */
@Service
@Scope("prototype")
public class BrandService implements IBrandService {
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    private Logger logger = LoggerFactory.getLogger(BrandService.class);

    public void addOne(BrandEntity brandEntity) throws EwandianSearchEngineException {
        try {
            BrandEntity obj = findOne(brandEntity);
            if(obj==null) {
                brandEntity.setId(UUID.randomUUID().toString());
                brandRepository.save(brandEntity);
            }
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineException("品牌新增出错！");
        }
    }

    public void updateOne(BrandEntity brandEntity) throws EwandianSearchEngineException {
        try {
            BrandEntity obj = findOne(brandEntity);
            if(obj!=null) {
                brandEntity.setId(obj.getId());
                brandRepository.save(brandEntity);
            }
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineException("品牌更新出错！");
        }
    }

    public void removeOne(BrandEntity brandEntity) throws EwandianSearchEngineException {
        try {
            BrandEntity obj = findOne(brandEntity);
            if(obj!=null) {
                brandEntity.setId(obj.getId());
                brandRepository.delete(brandEntity);
            }
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineException("品牌删除出错！");
        }
    }

    public BrandEntity findOne(BrandEntity brandEntity) throws EwandianSearchEngineException {
        try {
            NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                    .withQuery(termQuery(QueryFieldConstant.brandId,brandEntity.getBrandId()));
            List<BrandEntity> brandEntityList = elasticsearchTemplate.queryForList(nsqb.build(),BrandEntity.class);
            if(brandEntityList!=null && brandEntityList.size()>0) {
                return brandEntityList.get(0);
            }
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineException("品牌查询出错！");
        }
    }

    @Override
    public List<BrandEntity> findAll(PageInfo pageInfo) throws EwandianSearchEngineException {
        try {
            Page<BrandEntity> shopEntityPage = brandRepository.findAll(new PageRequest(pageInfo.getPageNumber()-1,pageInfo.getPageSize()));
            List<BrandEntity> shopEntityList = new ArrayList<BrandEntity>();
            if(shopEntityPage!=null && shopEntityPage.getSize()>0) {
                shopEntityList = shopEntityPage.getContent();
            }
            return shopEntityList;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineException("品牌查询出错！");
        }
    }

    @Override
    public long getAllCount() throws EwandianSearchEngineException {
        return brandRepository.count();
    }

    public List<BrandEntity> getSibling(String brandId, String categoryId, PageInfo pageInfo) throws EwandianSearchEngineException {
        if(StringUtil.isNullOrEmpty(brandId) || StringUtil.isNullOrEmpty(categoryId)) {
            throw new EwandianSearchEngineException("参数传入错误：brandId与categoryId不允许为空！");
        }
        try {
            List<String> siblingBrandIdList = findDoHaveSuchGoodsBrandByCategoryId(categoryId, pageInfo);
            List<BrandEntity> siblingBrandEntityList = findBrandEntityList(pageInfo, siblingBrandIdList);
            return siblingBrandEntityList;
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineException("品牌查询同级其它品牌信息出错！");
        }
    }

    public long getSiblingCount(String brandId, String categoryId, PageInfo pageInfo) throws EwandianSearchEngineException {
        if(StringUtil.isNullOrEmpty(brandId) || StringUtil.isNullOrEmpty(categoryId)) {
            throw new EwandianSearchEngineException("参数传入错误：brandId与categoryId不允许为空！");
        }
        try {
            List<String> siblingBrandIdList = findDoHaveSuchGoodsBrandByCategoryId(categoryId, pageInfo);
            return siblingBrandIdList.size();
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineException("品牌查询同级其它品牌信息统计数量出错！");
        }
    }

    private List<BrandEntity> findBrandEntityList(PageInfo pageInfo, List<String> siblingBrandIdList) throws EwandianSearchEngineException {
        try {
            NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                    .withIndices("brandindex")
                    .withTypes("brand")
                    .withQuery(termsQuery(QueryFieldConstant.brandId,siblingBrandIdList));
            if(pageInfo!=null) {
                nsqb.withPageable(new PageRequest(pageInfo.getPageNumber()-1,pageInfo.getPageSize()));
            }
            List<BrandEntity> siblingBrandEntityList = elasticsearchTemplate.queryForList(nsqb.build(),BrandEntity.class);
            if(siblingBrandEntityList==null || siblingBrandEntityList.size()<=0) {
                throw new EwandianSearchEngineNoDataException("搜索引擎在品牌文档中找不到该品牌的同类品牌信息");
            }
            return siblingBrandEntityList;
        } catch (EwandianSearchEngineNoDataException e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineException(e.getMessage());
        }
    }

    private List<String> findDoHaveSuchGoodsBrandByCategoryId(String categoryId, PageInfo pageInfo) throws EwandianSearchEngineException {
        try {
            NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                    .withIndices("goodsindex")
                    .withTypes("goods")
                    .withQuery(termQuery(QueryFieldConstant.categoryId,categoryId))
                    .withFields(QueryFieldConstant.brandId,QueryFieldConstant.categoryId)
                    .withPageable(new PageRequest(pageInfo.getPageNumber()-1,pageInfo.getPageSize()));
            AggregationBuilder aggregationBuilder = AggregationBuilders.terms("goodsFindBrandByCategoryAgg")
                    .size(9999)
                    .field(QueryFieldConstant.brandId);
            SearchResponse searchResponse = elasticsearchTemplate.getClient()
                    .prepareSearch("goodsindex")
                    .setTypes("goods")
                    .setQuery(nsqb.build().getQuery())
                    .addAggregation(aggregationBuilder)
                    .execute()
                    .actionGet();
            Terms terms = searchResponse.getAggregations().get("goodsFindBrandByCategoryAgg");
            if(terms.getBuckets()==null || terms.getBuckets().size()<=0) {
                throw new EwandianSearchEngineNoDataException("搜索引擎在上架商品中找不到该品牌的同类品牌信息");
            }
            List<String> siblingBrandIdList = new ArrayList<String>();
            for(Terms.Bucket bucket : terms.getBuckets()) {
                siblingBrandIdList.add(bucket.getKey().toString());
            }
            return siblingBrandIdList;
        } catch (EwandianSearchEngineNoDataException e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineException(e.getMessage());
        }
    }

    @Override
    public List<BrandEntity> findBrandsByShopId(SearchKeyWord skw) throws EwandianSearchEngineException {
        try {
            if(StringUtil.isNullOrEmpty(skw.getSearchArg())) {
                throw new EwandianSearchEngineException("参数传入错误：searchArg不允许为空！");
            }
            List brandIdList = findDoHaveSuchGoodsBrandByShopId(skw.getSearchArg());
            List<BrandEntity> brandEntityList = findBrandEntityList(null, brandIdList);
            return brandEntityList;
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineException(e.getMessage());
        }
    }

    public List<BrandEntity> findBrandsPageByShopId(SearchKeyWord skw) throws EwandianSearchEngineException {
        try {
            if(StringUtil.isNullOrEmpty(skw.getSearchArg())) {
                throw new EwandianSearchEngineException("参数传入错误：searchArg不允许为空！");
            }
            List brandIdList = findDoHaveSuchGoodsBrandByShopId(skw.getSearchArg());
            NativeSearchQueryBuilder nsqb = buildNSQBForFindBrandsPageByShopId(skw.getPageInfo(), brandIdList);
            List<BrandEntity> brandEntityList = elasticsearchTemplate.queryForList(nsqb.build(),BrandEntity.class);
            return brandEntityList;
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineException(e.getMessage());
        }
    }

    private NativeSearchQueryBuilder buildNSQBForFindBrandsPageByShopId(PageInfo pageInfo, List<String> brandIdList) {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withIndices("brandindex")
                .withTypes("brand")
                .withQuery(termsQuery(QueryFieldConstant.brandId, brandIdList));
        if(pageInfo!=null) {
            nsqb.withPageable(new PageRequest(pageInfo.getPageNumber()-1, pageInfo.getPageSize()));
        }
        return nsqb;
    }

    public long findBrandsPageByShopIdCount(SearchKeyWord skw) throws EwandianSearchEngineException {
        List brandIdList = findDoHaveSuchGoodsBrandByShopId(skw.getSearchArg());
        NativeSearchQueryBuilder nsqb = buildNSQBForFindBrandsPageByShopId(skw.getPageInfo(), brandIdList);
        return elasticsearchTemplate.count(nsqb.build(),BrandEntity.class);
    }

    private List<String> findDoHaveSuchGoodsBrandByShopId(String shopId) throws EwandianSearchEngineException {
        try {
            NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                    .withIndices("goodsindex")
                    .withTypes("goods")
                    .withQuery(termQuery(QueryFieldConstant.shopId,shopId))
                    .withFields(QueryFieldConstant.brandId,QueryFieldConstant.brandName);
            AggregationBuilder aggregationBuilder = AggregationBuilders.terms("goodsFindBrandByShopIdAgg")
                    .field(QueryFieldConstant.brandId)
                    .size(9999);
            SearchResponse searchResponse = elasticsearchTemplate.getClient()
                    .prepareSearch("goodsindex")
                    .setTypes("goods")
                    .setQuery(nsqb.build().getQuery())
                    .addAggregation(aggregationBuilder)
                    .execute()
                    .actionGet();
            Terms terms = searchResponse.getAggregations().get("goodsFindBrandByShopIdAgg");
            if(terms.getBuckets()==null || terms.getBuckets().size()<=0) {
                throw new EwandianSearchEngineNoDataException("搜索引擎在上架商品中找不到该店铺的任何品牌信息");
            }
            List<String> brandIdList = new ArrayList<String>();
            for(Terms.Bucket bucket : terms.getBuckets()) {
                brandIdList.add(bucket.getKey().toString());
            }
            return brandIdList;
        } catch (EwandianSearchEngineNoDataException e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineException(e.getMessage());
        }
    }

    public List<BrandEntity> findBrandsPageBySearchArg(SearchKeyWord skw) throws EwandianSearchEngineException {
        try {
            Terms terms = getTermsForFindBrandsPageBySearchArg(skw);
            List<String> brandIdList = new ArrayList<String>();
            for(Terms.Bucket bucket : terms.getBuckets()) {
                brandIdList.add(bucket.getKey().toString());
            }
            List<BrandEntity> brandEntityList = findBrandEntityList(skw.getPageInfo(),brandIdList);
            return brandEntityList;
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineException(e.getMessage());
        }
    }

    public long findBrandsPageBySearchArgCount(SearchKeyWord skw) throws EwandianSearchEngineException {
        try {
            Terms terms = getTermsForFindBrandsPageBySearchArg(skw);
            return terms.getBuckets().size();
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineException(e.getMessage());
        }
    }

    private Terms getTermsForFindBrandsPageBySearchArg(SearchKeyWord skw) throws EwandianSearchEngineException {
        try {
            NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                        .withIndices("goodsindex")
                        .withTypes("goods");
            if(StringUtil.isNotNullAndNotEmpty(skw.getSearchArg())) {
                BoolQueryBuilder boolQueryBuilder = boolQuery().should(matchPhraseQuery(QueryFieldConstant.shopName,skw.getSearchArg()))
                        .should(matchPhraseQuery(QueryFieldConstant.goodsName,skw.getSearchArg()))
                        .should(matchPhraseQuery(QueryFieldConstant.brandName,skw.getSearchArg()))
                        .should(matchPhraseQuery(QueryFieldConstant.categoryName,skw.getSearchArg()))
                        .should(matchPhraseQuery(QueryFieldConstant.model,skw.getSearchArg()));
                if (skw.getCategoryIdList()!=null && skw.getCategoryIdList().size()>0){
                    boolQueryBuilder.must(termsQuery(QueryFieldConstant.categoryId, skw.getCategoryIdList()));
                }
                nsqb.withQuery(boolQueryBuilder);
            }
            nsqb.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1,skw.getPageInfo().getPageSize()));
            AggregationBuilder aggregationBuilder = AggregationBuilders.terms("goodsFindAllBrandAgg")
                    .field(QueryFieldConstant.brandId)
                    .size(9999);
            SearchResponse searchResponse = elasticsearchTemplate.getClient()
                    .prepareSearch("goodsindex")
                    .setTypes("goods")
                    .setQuery(nsqb.build().getQuery())
                    .addAggregation(aggregationBuilder)
                    .execute()
                    .actionGet();
            Terms terms = searchResponse.getAggregations().get("goodsFindAllBrandAgg");
            if(terms.getBuckets()==null || terms.getBuckets().size()<=0) {
                throw new EwandianSearchEngineNoDataException("搜索引擎在上架商品中找不到任何品牌信息");
            }
            return terms;
        } catch (EwandianSearchEngineNoDataException e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineException(e.getMessage());
        }
    }

}
