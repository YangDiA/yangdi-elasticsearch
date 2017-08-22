package com.ewandian.b2b2c.search.service.impl;

import com.ewandian.b2b2c.search.app.constant.QueryFieldConstant;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineNoDataException;
import com.ewandian.b2b2c.search.domain.document.GoodsEntity;
import com.ewandian.b2b2c.search.domain.document.GoodsImageEntity;
import com.ewandian.b2b2c.search.domain.receive.GoodsPageInfo;
import com.ewandian.b2b2c.search.domain.receive.PageInfo;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;
import com.ewandian.b2b2c.search.repositories.GoodsRepository;
import com.ewandian.b2b2c.search.service.IGoodsService;
import com.ewandian.b2b2c.search.service.multithread.AddPopularityToGoods;
import com.ewandian.platform.util.StringUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executors;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Created by suhd on 2016-11-22.
 */

@Service
@Scope("prototype")
public class GoodsService implements IGoodsService {

    private Logger logger = LoggerFactory.getLogger(GoodsService.class);

    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private AddPopularityToGoods addPopularityToGoods;

    public void addOne(GoodsEntity goodsEntity) throws EwandianSearchEngineException {
        try {
            GoodsEntity obj = findOne(goodsEntity);
            if(obj==null) {
                goodsEntity.setId(UUID.randomUUID().toString());
                goodsRepository.save(goodsEntity);
            }
        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
            throw new EwandianSearchEngineException("插入elasticsearch数据出错，原因：" + e.getStackTrace().toString());
        }
    }

    public void updateOne(GoodsEntity goodsEntity) throws EwandianSearchEngineException {
        try {
            GoodsEntity obj = findOne(goodsEntity);
            if(obj!=null) {
                goodsEntity.setId(obj.getId());
                goodsRepository.save(goodsEntity);
            }
        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
            throw new EwandianSearchEngineException("更新elasticsearch数据出错，原因：" + e.getStackTrace().toString());
        }
    }

    public void updateBrandNameByGoodsEntityWithGoodsIdAndBrandName(GoodsEntity goodsEntity) throws EwandianSearchEngineException {
        try {
            GoodsEntity obj = findOne(goodsEntity);
            if(obj!=null) {
                obj.setBrandName(goodsEntity.getBrandName());
                goodsRepository.save(goodsEntity);
            }
        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
            throw new EwandianSearchEngineException("更新elasticsearch数据出错，原因：" + e.getStackTrace().toString());
        }
    }

    public void updateCategoryNameByGoodsEntityWithGoodsIdAndCategoryName(GoodsEntity goodsEntity) throws EwandianSearchEngineException {
        try {
            GoodsEntity obj = findOne(goodsEntity);
            if(obj!=null) {
                obj.setCategoryName(goodsEntity.getCategoryName());
                goodsRepository.save(goodsEntity);
            }
        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
            throw new EwandianSearchEngineException("更新elasticsearch数据出错，原因：" + e.getStackTrace().toString());
        }
    }

    public void addPopularityToGoods(String goodsId) throws EwandianSearchEngineException {
        try {
            GoodsEntity goodsEntity = new GoodsEntity();
            goodsEntity.setGoodsId(goodsId);
            GoodsEntity obj = findOne(goodsEntity);
            if(obj != null) {
                obj.setGoodsPopularity(obj.getGoodsPopularity()+1);
                goodsRepository.save(obj);
            }
        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
            throw new EwandianSearchEngineException("addPopularityToGoods出错，原因：" + e.getStackTrace().toString());
        }
    }

    public void addCommentNumToGoods(String goodsId) throws EwandianSearchEngineException {
        try {
            GoodsEntity goodsEntity = new GoodsEntity();
            goodsEntity.setGoodsId(goodsId);
            GoodsEntity obj = findOne(goodsEntity);
            if(obj != null) {
                obj.setGoodsCommentNum(obj.getGoodsCommentNum()+1);
                goodsRepository.save(obj);
            }
        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
            throw new EwandianSearchEngineException("addCommentNumToGoods出错，原因：" + e.getStackTrace().toString());
        }
    }

    public void addSaleVolumeToGoods(String goodsId, int qty) throws EwandianSearchEngineException {
        try {
            GoodsEntity goodsEntity = new GoodsEntity();
            goodsEntity.setGoodsId(goodsId);
            GoodsEntity obj = findOne(goodsEntity);
            if(obj != null) {
                obj.setGoodsSaleVolume(obj.getGoodsSaleVolume() + qty);
                goodsRepository.save(obj);
            }
        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
            throw new EwandianSearchEngineException("addSaleVolumeToGoods出错，原因：" + e.getStackTrace().toString());
        }
    }

    public void removeOne(GoodsEntity goodsEntity) throws EwandianSearchEngineException {
        try {
            GoodsEntity obj = findOne(goodsEntity);
            if(obj!=null) {
                goodsEntity.setId(obj.getId());
                goodsRepository.delete(goodsEntity);
            }
        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
            throw new EwandianSearchEngineException("删除elasticsearch数据出错，原因：" + e.getStackTrace().toString());
        }
    }

    private GoodsEntity findOne(GoodsEntity goodsEntity) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withIndices("goodsindex")
                .withTypes("goods")
                .withQuery(termQuery(QueryFieldConstant.goodsId,goodsEntity.getGoodsId()));
        List<GoodsEntity> goodsEntityList = elasticsearchTemplate.queryForList(nsqb.build(),GoodsEntity.class);
        if(goodsEntityList!=null && goodsEntityList.size()>0) {
            return goodsEntityList.get(0);
        }
        return null;
    }

    @Override
    public List<GoodsEntity> findAll(PageInfo pageInfo) throws EwandianSearchEngineException {
        Page<GoodsEntity> goodsEntityPage = goodsRepository.findAll(new PageRequest(pageInfo.getPageNumber()-1,pageInfo.getPageSize()));
        List<GoodsEntity> goodsEntityList = new ArrayList<GoodsEntity>();
        if(goodsEntityPage!=null && goodsEntityPage.getSize()>0) {
            goodsEntityList = goodsEntityPage.getContent();
        }
        //set images to goodsEntity
        buildGoodsEntityWithGoodsImage(goodsEntityList);
        return goodsEntityList;
    }

    @Override
    public long getAllCount() throws EwandianSearchEngineException {
        return goodsRepository.count();
    }

    @Override
    public List<GoodsEntity> findGoods(SearchKeyWord skw) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = buildNSQBForFindGoods(skw);
        if(skw.getPageInfo().getSortBy()!=null) {
            nsqb.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1, skw.getPageInfo().getPageSize(), skw.getPageInfo().getSortDirection(), skw.getPageInfo().getSortBy().getValue()));
        } else {
            nsqb.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1, skw.getPageInfo().getPageSize()));

        }
        GoodsEntity goodsSearchEntity = new GoodsEntity();
        goodsSearchEntity.setGoodsId(skw.getSearchArg());
        GoodsEntity goodsEntity = this.findOne(goodsSearchEntity);
        List<GoodsEntity> goodsEntityList = new ArrayList<GoodsEntity>();
        if(goodsEntity == null ){
             goodsEntityList = elasticsearchTemplate.queryForList(nsqb.build(),GoodsEntity.class);
        }else {
            goodsEntityList.add(goodsEntity);
        }


        if(goodsEntityList==null || goodsEntityList.size()<=0) {
            throw new EwandianSearchEngineNoDataException("抱歉，没有找到“"+skw.getSearchArg()+"”的搜索结果");
        }
        //set images to goodsEntity
        buildGoodsEntityWithGoodsImage(goodsEntityList);
        return goodsEntityList;
    }

    private NativeSearchQueryBuilder buildNSQBForFindGoods(SearchKeyWord skw) {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withIndices("goodsindex")
                .withTypes("goods");
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if(skw.getBrandIdList()!=null && skw.getBrandIdList().size()>0) {
            boolQueryBuilder.must(termsQuery(QueryFieldConstant.brandId, skw.getBrandIdList()));
        }
        if(skw.getCategoryIdList()!=null && skw.getCategoryIdList().size()>0) {
            boolQueryBuilder.must(termsQuery(QueryFieldConstant.categoryId, skw.getCategoryIdList()));
        }
        if(boolQueryBuilder.hasClauses()) {
            nsqb.withFilter(boolQueryBuilder);
        }
        if(StringUtil.isNotNullAndNotEmpty(skw.getSearchArg())) {
            nsqb.withQuery(
                    boolQuery()
                            .should(matchPhrasePrefixQuery(QueryFieldConstant.goodsName,skw.getSearchArg()))
                            .should(matchPhrasePrefixQuery(QueryFieldConstant.categoryName,skw.getSearchArg()))
                            .should(matchPhrasePrefixQuery(QueryFieldConstant.brandName,skw.getSearchArg()))
                            .should(matchPhrasePrefixQuery(QueryFieldConstant.model,skw.getSearchArg()))
                            .should(matchPhrasePrefixQuery(QueryFieldConstant.description,skw.getSearchArg()))
                            .minimumNumberShouldMatch(1)
            );
        }
        return nsqb;
    }

    @Override
    public long getCount(SearchKeyWord skw) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = buildNSQBForFindGoods(skw);
        long count = elasticsearchTemplate.count(nsqb.build(),GoodsEntity.class);
        return count;
    }

    private void buildGoodsEntityWithGoodsImage(List<GoodsEntity> goodsEntityList) throws EwandianSearchEngineException {
        Set<String> goodsSet = new HashSet<String>();
        for(int i=0; i<goodsEntityList.size(); i++) {
            goodsSet.add(goodsEntityList.get(i).getGoodsId());
        }
        List<GoodsImageEntity> goodsImageEntityList = findGoodsImages(new ArrayList<String>(goodsSet));
        for(int i=0; i<goodsEntityList.size(); i++) {
            List<GoodsImageEntity> gieList = new ArrayList<GoodsImageEntity>();
            for(int j=0; j<goodsImageEntityList.size(); j++) {
                if(goodsEntityList.get(i).getGoodsId().equals(goodsImageEntityList.get(j).getGoodsId())) {
                    GoodsImageEntity gie = goodsImageEntityList.get(j);
                    gieList.add(gie);
                }else {
                    continue;
                }
                goodsEntityList.get(i).setGoodsImageEntityList(gieList);
            }
        }
    }

    private List<GoodsImageEntity> findGoodsImages(List<String> goodsIdList) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb1 = new NativeSearchQueryBuilder()
                .withQuery(termsQuery(QueryFieldConstant.goodsId,goodsIdList));

        List<GoodsImageEntity> returnList = new ArrayList<GoodsImageEntity>();
        try {
            Iterator<GoodsImageEntity> goodsImageEntityIterator=elasticsearchTemplate.stream(nsqb1.build(),GoodsImageEntity.class);
            while(goodsImageEntityIterator.hasNext()){
                returnList.add(goodsImageEntityIterator.next());
            }
        } catch (Exception e) {
        }
      return returnList;


        /*NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withQuery(termsQuery(QueryFieldConstant.goodsId,goodsIdList));
       List<GoodsImageEntity> goodsImageEntityList = elasticsearchTemplate.queryForList(nsqb.build(),GoodsImageEntity.class);

        return goodsImageEntityList;*/
    }

    @Override
    public GoodsEntity findOneByGoodsId(GoodsEntity goodsEntity) throws EwandianSearchEngineException {
        if(goodsEntity != null && StringUtil.isNotNullAndNotEmpty(goodsEntity.getGoodsId())) {
            //add goods'popularity
            Executors.newCachedThreadPool().execute(addPopularityToGoods.newInstance(this,goodsEntity.getGoodsId()));
        }
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withQuery(termQuery(QueryFieldConstant.goodsId,goodsEntity.getGoodsId()));
        List<GoodsEntity> goodsEntityList = elasticsearchTemplate.queryForList(nsqb.build(),GoodsEntity.class);
        if(goodsEntityList==null || goodsEntityList.size()<=0) {
            throw new EwandianSearchEngineNoDataException();
        }
        //set images to goodsEntity
        buildGoodsEntityWithGoodsImage(goodsEntityList);
        GoodsEntity retGoodsEntity = goodsEntityList.get(0);
        return retGoodsEntity;
    }

    public List<GoodsEntity> findPageListByGoodsEntity(GoodsPageInfo goodsPageInfo) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = buildNSQBForFindGoodsByGoodsEntity(goodsPageInfo);
        List<GoodsEntity> goodsEntityList = elasticsearchTemplate.queryForList(nsqb.build(),GoodsEntity.class);
        if(goodsEntityList==null || goodsEntityList.size()<=0) {
            throw new EwandianSearchEngineNoDataException("抱歉，没有找到任何搜索结果");
        }
        //set images to goodsEntity
        buildGoodsEntityWithGoodsImage(goodsEntityList);
        return goodsEntityList;
    }

    public long findListByGoodsEntityCount(GoodsPageInfo goodsPageInfo) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = buildNSQBForFindGoodsByGoodsEntity(goodsPageInfo);
        return elasticsearchTemplate.count(nsqb.build(),GoodsEntity.class);
    }

    private NativeSearchQueryBuilder buildNSQBForFindGoodsByGoodsEntity(GoodsPageInfo goodsPageInfo) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if(goodsPageInfo != null) {
            if(goodsPageInfo.getGoodsEntity()!=null && StringUtil.isNotNullAndNotEmpty(goodsPageInfo.getGoodsEntity().getShopId())) {
                boolQueryBuilder = boolQueryBuilder.must(termQuery(QueryFieldConstant.shopId,goodsPageInfo.getGoodsEntity().getShopId()));
            }
            if(goodsPageInfo.getGoodsEntity()!=null && StringUtil.isNotNullAndNotEmpty(goodsPageInfo.getGoodsEntity().getBrandId())) {
                boolQueryBuilder = boolQueryBuilder.must(termQuery(QueryFieldConstant.brandId,goodsPageInfo.getGoodsEntity().getBrandId()));
            }
            if(goodsPageInfo.getGoodsEntity()!=null && StringUtil.isNotNullAndNotEmpty(goodsPageInfo.getGoodsEntity().getCategoryId())) {
                boolQueryBuilder = boolQueryBuilder.must(termQuery(QueryFieldConstant.categoryId,goodsPageInfo.getGoodsEntity().getCategoryId()));
            }
        }
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder();
        if(boolQueryBuilder.hasClauses()) {
            nsqb.withQuery(boolQueryBuilder);
        }
        if(goodsPageInfo != null && goodsPageInfo.getPageInfo().getSortBy()!=null) {
            nsqb.withPageable(new PageRequest(goodsPageInfo.getPageInfo().getPageNumber()-1, goodsPageInfo.getPageInfo().getPageSize(), goodsPageInfo.getPageInfo().getSortDirection(), goodsPageInfo.getPageInfo().getSortBy().getValue()));
        } else {
            nsqb.withPageable(new PageRequest(goodsPageInfo.getPageInfo().getPageNumber()-1, goodsPageInfo.getPageInfo().getPageSize()));

        }
        return nsqb;
    }

    @Override
    public List<GoodsEntity> findGoodsPageByGoodsIdList(SearchKeyWord skw) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = buildNSQBForGoodsPageByGoodsIdList(skw);
        List<GoodsEntity> goodsEntityList = elasticsearchTemplate.queryForList(nsqb.build(),GoodsEntity.class);
        if(goodsEntityList==null || goodsEntityList.size()<=0) {
            throw new EwandianSearchEngineNoDataException("搜索引擎在上架商品中找不到商品信息");
        }
        //set images to goodsEntity
        buildGoodsEntityWithGoodsImage(goodsEntityList);
        return goodsEntityList;
    }

    public long findGoodsPageByGoodsIdListCount(SearchKeyWord skw) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = buildNSQBForGoodsPageByGoodsIdList(skw);
        return elasticsearchTemplate.count(nsqb.build(),GoodsEntity.class);
    }

    private NativeSearchQueryBuilder buildNSQBForGoodsPageByGoodsIdList(SearchKeyWord skw) throws EwandianSearchEngineNoDataException {
        if(skw.getGoodsIdList()==null || skw.getGoodsIdList().size()<=0) {
            throw new EwandianSearchEngineNoDataException("参数传入错误：goodsIdList不允许为空！");
        }
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withIndices("goodsindex")
                .withTypes("goods")
                .withQuery(termsQuery(QueryFieldConstant.goodsId, skw.getGoodsIdList()));
        if(skw.getPageInfo().getSortBy()!=null && StringUtil.isNotNullAndNotEmpty(skw.getPageInfo().getSortBy().getValue())) {
            nsqb.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1,skw.getPageInfo().getPageSize(),skw.getPageInfo().getSortDirection(),skw.getPageInfo().getSortBy().getValue()));
        }else {
            nsqb.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1,skw.getPageInfo().getPageSize()));
        }
        return nsqb;
    }

    @Override
    public List<GoodsEntity> findGoodsPageByCategoryIdInPriceRange(GoodsPageInfo goodsPageInfo) throws EwandianSearchEngineException {
        List<GoodsEntity> goodsEntityList;
        try {
            if(StringUtil.isNullOrEmpty(goodsPageInfo.getGoodsEntity().getCategoryId())
                    || StringUtil.isNullOrEmpty(goodsPageInfo.getGoodsEntity().getSalePrice().toString())) {
                throw new EwandianSearchEngineException("参数传入错误：categoryId与salePrice不允许为空！");
            }
            NativeSearchQueryBuilder nsqb = buildNSQBForFindGoodsPageByCategoryIdInPriceRange(goodsPageInfo);
            goodsEntityList = elasticsearchTemplate.queryForList(nsqb.build(),GoodsEntity.class);
            if(goodsEntityList==null || goodsEntityList.size()<=0) {
                throw new EwandianSearchEngineNoDataException("搜索引擎在上架商品中找不到商品信息");
            }
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineException(e.getMessage());
        }
        //set images to goodsEntity
        buildGoodsEntityWithGoodsImage(goodsEntityList);
        return goodsEntityList;
    }

    private NativeSearchQueryBuilder buildNSQBForFindGoodsPageByCategoryIdInPriceRange(GoodsPageInfo goodsPageInfo) {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withIndices("goodsindex")
                .withTypes("goods")
                .withFilter(termQuery(QueryFieldConstant.categoryId,goodsPageInfo.getGoodsEntity().getCategoryId()))
                .withQuery(rangeQuery(QueryFieldConstant.salePrice).gte(goodsPageInfo.getGoodsEntity().getSalePrice().subtract(goodsPageInfo.getFluctuatingFigure())).lte(goodsPageInfo.getGoodsEntity().getSalePrice().add(goodsPageInfo.getFluctuatingFigure())));
        if(goodsPageInfo.getPageInfo().getSortBy()!=null && StringUtil.isNotNullAndNotEmpty(goodsPageInfo.getPageInfo().getSortBy().toString())) {
            nsqb.withPageable(new PageRequest(goodsPageInfo.getPageInfo().getPageNumber()-1,goodsPageInfo.getPageInfo().getPageSize(),goodsPageInfo.getPageInfo().getSortDirection(),goodsPageInfo.getPageInfo().getSortBy().getValue()));
        } else {
            nsqb.withPageable(new PageRequest(goodsPageInfo.getPageInfo().getPageNumber()-1,goodsPageInfo.getPageInfo().getPageSize()));
        }
        return nsqb;
    }

    public long findGoodsPageByCategoryIdInPriceRangeCount(GoodsPageInfo goodsPageInfo) throws EwandianSearchEngineException {
        try {
            NativeSearchQueryBuilder nsqb = buildNSQBForFindGoodsPageByCategoryIdInPriceRange(goodsPageInfo);
            return elasticsearchTemplate.count(nsqb.build(),GoodsEntity.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineException(e.getMessage());
        }
    }

    @Override
    public List<GoodsEntity> findGoodsPageByBrandIdInPriceRange(GoodsPageInfo goodsPageInfo) throws EwandianSearchEngineException {
        List<GoodsEntity> goodsEntityList;
        try {
            if(StringUtil.isNullOrEmpty(goodsPageInfo.getGoodsEntity().getBrandId())
                    || StringUtil.isNullOrEmpty(goodsPageInfo.getGoodsEntity().getSalePrice().toString())) {
                throw new EwandianSearchEngineException("参数传入错误：brandId与salePrice不允许为空！");
            }
            NativeSearchQueryBuilder nsqb = buildNSQBForFindGoodsPageByBrandIdInPriceRange(goodsPageInfo);
            goodsEntityList = elasticsearchTemplate.queryForList(nsqb.build(),GoodsEntity.class);
            if(goodsEntityList==null || goodsEntityList.size()<=0) {
                throw new EwandianSearchEngineNoDataException("搜索引擎在上架商品中找不到商品信息");
            }
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineException(e.getMessage());
        }
        //set images to goodsEntity
        buildGoodsEntityWithGoodsImage(goodsEntityList);
        return goodsEntityList;
    }

    private NativeSearchQueryBuilder buildNSQBForFindGoodsPageByBrandIdInPriceRange(GoodsPageInfo goodsPageInfo) {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withIndices("goodsindex")
                .withTypes("goods")
                .withFilter(termQuery(QueryFieldConstant.brandId,goodsPageInfo.getGoodsEntity().getBrandId()))
                .withQuery(rangeQuery(QueryFieldConstant.salePrice).gte(goodsPageInfo.getGoodsEntity().getSalePrice().subtract(goodsPageInfo.getFluctuatingFigure())).lte(goodsPageInfo.getGoodsEntity().getSalePrice().add(goodsPageInfo.getFluctuatingFigure())));
        if(goodsPageInfo.getPageInfo().getSortBy()!=null && StringUtil.isNotNullAndNotEmpty(goodsPageInfo.getPageInfo().getSortBy().toString())) {
            nsqb.withPageable(new PageRequest(goodsPageInfo.getPageInfo().getPageNumber()-1,goodsPageInfo.getPageInfo().getPageSize(),goodsPageInfo.getPageInfo().getSortDirection(),goodsPageInfo.getPageInfo().getSortBy().getValue()));
        } else {
            nsqb.withPageable(new PageRequest(goodsPageInfo.getPageInfo().getPageNumber()-1,goodsPageInfo.getPageInfo().getPageSize()));
        }
        return nsqb;
    }

    public long findGoodsPageByBrandIdInPriceRangeCount(GoodsPageInfo goodsPageInfo) throws EwandianSearchEngineException {
        try {
            NativeSearchQueryBuilder nsqb = buildNSQBForFindGoodsPageByBrandIdInPriceRange(goodsPageInfo);
            return elasticsearchTemplate.count(nsqb.build(),GoodsEntity.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineException(e.getMessage());
        }
    }

    public List<GoodsEntity> fetchAllGoodsEntityByBrandId(String brandId) throws EwandianSearchEngineException {
        if(StringUtil.isNullOrEmpty(brandId)) {
            throw new EwandianSearchEngineException("参数传入错误：brandId不允许为空！");
        }
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withIndices("goodsindex")
                .withTypes("goods")
                .withQuery(termQuery(QueryFieldConstant.brandId, brandId))
                .withPageable(new PageRequest(0,100));
        List<GoodsEntity> goodsEntityList = getAllDataOfGoodsEntityByGivingNSQB(nsqb);
        return goodsEntityList;
    }

    private List<GoodsEntity> getAllDataOfGoodsEntityByGivingNSQB(NativeSearchQueryBuilder nsqb) {
        String scrollId = elasticsearchTemplate.scan(nsqb.build(),1000L,false);
        List<GoodsEntity> goodsEntityList = new ArrayList<GoodsEntity>();
        long remainQuantity = 0;
        Page<GoodsEntity> page = elasticsearchTemplate.scroll(scrollId,5000L,GoodsEntity.class);
        if(page != null) {
            remainQuantity = page.getTotalElements()-page.getContent().size();
            goodsEntityList.addAll(page.getContent());
        }
        while(remainQuantity > 0) {
            page = elasticsearchTemplate.scroll(scrollId,5000L,GoodsEntity.class);
            if(page != null) {
                remainQuantity = remainQuantity-page.getContent().size();
                goodsEntityList.addAll(page.getContent());
            }
        }
        return goodsEntityList;
    }

    public List<GoodsEntity> fetchAllGoodsEntityByCategoryId(String categoryId) throws EwandianSearchEngineException {
        if(StringUtil.isNullOrEmpty(categoryId)) {
            throw new EwandianSearchEngineException("参数传入错误：categoryId不允许为空！");
        }
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withIndices("goodsindex")
                .withTypes("goods")
                .withQuery(termQuery(QueryFieldConstant.categoryId, categoryId))
                .withPageable(new PageRequest(0,100));
        List<GoodsEntity> goodsEntityList = getAllDataOfGoodsEntityByGivingNSQB(nsqb);
        return goodsEntityList;
    }
}
