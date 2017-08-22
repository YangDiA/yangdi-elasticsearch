package com.ewandian.b2b2c.search.service.impl;

import com.ewandian.b2b2c.search.app.constant.QueryFieldConstant;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineNoDataException;
import com.ewandian.b2b2c.search.app.utils.CalculateDistanceUtil;
import com.ewandian.b2b2c.search.domain.document.RelatedKeyWordEntity;
import com.ewandian.b2b2c.search.domain.document.ShopEntity;
import com.ewandian.b2b2c.search.domain.keyword.RecommendShop;
import com.ewandian.b2b2c.search.domain.keyword.RelatedGoodsInfo;
import com.ewandian.b2b2c.search.domain.keyword.ReturnKeyWord;
import com.ewandian.b2b2c.search.domain.receive.PageInfo;
import com.ewandian.b2b2c.search.domain.receive.RelatedKeyWordPageInfo;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;
import com.ewandian.b2b2c.search.repositories.RelatedKeyWordRepository;
import com.ewandian.b2b2c.search.service.IRelatedKeyWordService;
import com.ewandian.platform.util.StringUtil;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Created by suhd on 2016-11-24.
 */
@Service
@Scope("prototype")
public class RelatedKeyWordService implements IRelatedKeyWordService {
    @Autowired
    private RelatedKeyWordRepository relatedKeyWordRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public void addOne(RelatedKeyWordEntity relatedKeyWordEntity) throws EwandianSearchEngineException {
        RelatedKeyWordEntity obj = findOne(relatedKeyWordEntity);
        if(obj==null) {
            relatedKeyWordEntity.setId(UUID.randomUUID().toString());
            relatedKeyWordRepository.save(relatedKeyWordEntity);
        }
    }

    public void updateOne(RelatedKeyWordEntity relatedKeyWordEntity) throws EwandianSearchEngineException {

    }

    public void removeOne(RelatedKeyWordEntity relatedKeyWordEntity) throws EwandianSearchEngineException {
        RelatedKeyWordEntity obj = findOne(relatedKeyWordEntity);
        if(obj!=null) {
            relatedKeyWordEntity.setId(obj.getId());
            relatedKeyWordRepository.delete(relatedKeyWordEntity);
        }
    }

    private RelatedKeyWordEntity findOne(RelatedKeyWordEntity relatedKeyWordEntity) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withIndices("relatedkeywordindex")
                .withTypes("relatedkeyword")
                .withQuery(
                        boolQuery()
                        .must(termQuery(QueryFieldConstant.relatedKeyWordNotAnalyzed,relatedKeyWordEntity.getRelatedKeyWordNotAnalyzed()))
                        .must(termQuery(QueryFieldConstant.goodsId,relatedKeyWordEntity.getGoodsId()))
                        .must(termQuery(QueryFieldConstant.shopId,relatedKeyWordEntity.getShopId()))
                );
        List<RelatedKeyWordEntity> relatedKeyWordEntityList = elasticsearchTemplate.queryForList(nsqb.build(),RelatedKeyWordEntity.class);
        if(relatedKeyWordEntityList!=null && relatedKeyWordEntityList.size()>0) {
            return relatedKeyWordEntityList.get(0);
        }
        return null;
    }

    public ReturnKeyWord findWithAggCount(SearchKeyWord skw) throws EwandianSearchEngineException {
        //find RelatedGoodsInfo
        List<RelatedGoodsInfo> relatedGoodsInfoList = findRelatedGoodsInfo(skw);
        //find RecommendShop
        List<RecommendShop> recommendShopList = getRecommendShop(skw);
        //assemble
        ReturnKeyWord returnKeyWord = new ReturnKeyWord();
        returnKeyWord.setRecommendShopList(recommendShopList);
        returnKeyWord.setRelatedGoodsInfoList(relatedGoodsInfoList);
        return returnKeyWord;
    }

    private List<RecommendShop> getRecommendShop(SearchKeyWord skw) {
        List shopIdList = getDoHaveSuchGoodsShopList(skw);
        List<RecommendShop> recommendShopList = checkWhetherRecommendShop(shopIdList,skw.getAreaName());
        List<RecommendShop> shopOrderByDistanceList = findShopOrderByDistance(recommendShopList,skw.getCoordinateX(),skw.getCoordinateY());
        if(shopOrderByDistanceList!=null && shopOrderByDistanceList.size()>0) {
            List<RecommendShop> recommendShops = new ArrayList<RecommendShop>();
            recommendShops.add(shopOrderByDistanceList.get(0));
            return recommendShops;
        }
        return null;
    }

    private List<String> getDoHaveSuchGoodsShopList(SearchKeyWord skw) {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withIndices("goodsindex")
                .withTypes("goods")
                .withQuery(
                        boolQuery().should(matchPhrasePrefixQuery(QueryFieldConstant.goodsName,skw.getSearchArg()))
                                .should(matchPhrasePrefixQuery(QueryFieldConstant.categoryName,skw.getSearchArg()))
                                .should(matchPhrasePrefixQuery(QueryFieldConstant.brandName,skw.getSearchArg()))
                                .should(matchPhrasePrefixQuery(QueryFieldConstant.model,skw.getSearchArg()))
                                .should(matchPhrasePrefixQuery(QueryFieldConstant.goodsId,skw.getSearchArg()))
                                .minimumNumberShouldMatch(1)
                )
                .withFields("shopId")
                .withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1, skw.getPageInfo().getPageSize()));
        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("shopEntityAgg")
                .size(skw.getPageInfo().getPageSize())
                .field(QueryFieldConstant.shopId);
        SearchResponse searchResponse = elasticsearchTemplate.getClient()
                .prepareSearch("goodsindex")
                .setTypes("goods")
                .setQuery(nsqb.build().getQuery())
                .addAggregation(aggregationBuilder)
                .execute()
                .actionGet();
        Terms terms = searchResponse.getAggregations().get("shopEntityAgg");
        List<String> shopIdList = new ArrayList<String>();
        if(terms != null) {
            for(Terms.Bucket bucket : terms.getBuckets()) {
                shopIdList.add(bucket.getKey().toString());
            }
        }
        return shopIdList;
    }

    private List<RecommendShop> checkWhetherRecommendShop(List shopIdList, String areaName) {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder();
                if(StringUtil.isNotNullAndNotEmpty(areaName)) {
                    // Filtered shops by areaName, for reducing calculation.
                    nsqb.withFilter(matchPhrasePrefixQuery(QueryFieldConstant.areaName, areaName));
                }
                nsqb.withFields("shopId","shopName","recommendShop","imgId","shopBackImg","coordinateX","coordinateY")
                    .withPageable(new PageRequest(0,9999));
        List<ShopEntity> shopEntityList = elasticsearchTemplate.queryForList(nsqb.build(),ShopEntity.class);
        List<RecommendShop> recommendShopList = new ArrayList<RecommendShop>();
        for(int i=0; i<shopEntityList.size(); i++) {
            for(int j=0; j<shopIdList.size(); j++) {
                if(shopEntityList.get(i).getShopId().equalsIgnoreCase((shopIdList.get(j).toString()))) {
                    RecommendShop recommendShop = new RecommendShop();
                    recommendShop.setShopId(shopEntityList.get(i).getShopId());
                    recommendShop.setShopName(shopEntityList.get(i).getShopName());
                    recommendShop.setImgId(shopEntityList.get(i).getImgId());
                    recommendShop.setShopBackImg(shopEntityList.get(i).getShopBackImg());
                    recommendShop.setCoordinateX(shopEntityList.get(i).getCoordinateX());
                    recommendShop.setCoordinateY(shopEntityList.get(i).getCoordinateY());
                    recommendShopList.add(recommendShop);
                }
            }
        }
        return recommendShopList;
    }

    private List<RecommendShop> findShopOrderByDistance(List<RecommendShop> recommendShopList, String coordinateX, String coordinateY) {
        Map<String,String> shopIdDistanceMap = new HashMap<String, String>();
        for(int i=0; i<recommendShopList.size(); i++) {
            double distance = CalculateDistanceUtil.calculateDistance(coordinateX,coordinateY,recommendShopList.get(i).getCoordinateX(),recommendShopList.get(i).getCoordinateY());
            shopIdDistanceMap.put(recommendShopList.get(i).getShopId(),String.valueOf(distance));
        }
        for(int i=0; i<recommendShopList.size()-1; i++) {
            for(int j=i+1; j<recommendShopList.size(); j++) {
                if(Double.valueOf(shopIdDistanceMap.get(recommendShopList.get(i).getShopId())) > Double.valueOf(shopIdDistanceMap.get(recommendShopList.get(j).getShopId()))) {
                    RecommendShop shop = recommendShopList.get(j);
                    recommendShopList.set(j,recommendShopList.get(i));
                    recommendShopList.set(i,shop);
                }
            }
        }
        return recommendShopList;
    }

    /*private double calculateDistance(String IPCoordinateX,String IPCoordinateY,String shopCoordinateX,String shopCoordinateY) {
        if(StringUtil.isNullOrEmpty(shopCoordinateX) || StringUtil.isNullOrEmpty(shopCoordinateY)) {
            return Double.MAX_VALUE;
        }
        Double x = Double.valueOf(IPCoordinateX) - Double.valueOf(shopCoordinateX);
        Double y = Double.valueOf(IPCoordinateY) - Double.valueOf(shopCoordinateY);
        Double z = Math.abs(Math.sqrt(Math.pow(x,2)+Math.pow(y,2)));
        return z;
    }*/

    private List<RelatedGoodsInfo> findRelatedGoodsInfo(SearchKeyWord rkw) {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withIndices("relatedkeywordindex")
                .withTypes("relatedkeyword")
                .withQuery(matchPhrasePrefixQuery(QueryFieldConstant.relatedKeyWord,rkw.getSearchArg()))
                .withPageable(new PageRequest(rkw.getPageInfo().getPageNumber()-1, rkw.getPageInfo().getPageSize()));
        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("relatedKeyWordAgg")
                .size(rkw.getPageInfo().getPageSize())
                .field(QueryFieldConstant.relatedKeyWordNotAnalyzed);
        SearchResponse searchResponse = elasticsearchTemplate.getClient()
                .prepareSearch("relatedkeywordindex")
                .setTypes("relatedkeyword")
                .setQuery(nsqb.build().getQuery())
                .addAggregation(aggregationBuilder)
                .execute()
                .actionGet();
        Terms terms = searchResponse.getAggregations().get("relatedKeyWordAgg");
        List<RelatedGoodsInfo> relatedGoodsInfoList = new ArrayList<RelatedGoodsInfo>();
        if(terms != null) {
            for(Terms.Bucket bucket : terms.getBuckets()) {
                RelatedGoodsInfo relatedGoodsInfo = new RelatedGoodsInfo();
                relatedGoodsInfo.setGoodsName(bucket.getKey().toString());
                relatedGoodsInfo.setGoodsNum(BigInteger.valueOf(bucket.getDocCount()));
                relatedGoodsInfoList.add(relatedGoodsInfo);
            }
        }

        //根据goodsId 2017-07-17
        NativeSearchQueryBuilder nsqbGoodsId = new NativeSearchQueryBuilder()
                .withIndices("relatedkeywordindex")
                .withTypes("relatedkeyword")
                .withQuery(termQuery("goodsId",rkw.getSearchArg()))
                .withPageable(new PageRequest(rkw.getPageInfo().getPageNumber()-1, 1));
        List<RelatedKeyWordEntity> relatedKeyWordEntityByGoodsIdList = elasticsearchTemplate.queryForList(nsqbGoodsId.build(),RelatedKeyWordEntity.class);
        if (relatedKeyWordEntityByGoodsIdList!=null && relatedKeyWordEntityByGoodsIdList.size()>0){
            RelatedGoodsInfo relatedGoodsInfo = new RelatedGoodsInfo();
            relatedGoodsInfo.setGoodsName(relatedKeyWordEntityByGoodsIdList.get(0).getGoodsId());
            relatedGoodsInfo.setGoodsNum(BigInteger.valueOf(1));
            relatedGoodsInfoList.add(0,relatedGoodsInfo);
        }

        return relatedGoodsInfoList;
    }

    public List<RelatedGoodsInfo> getRelatedGoodsInfoForAddInfoToHotspot(SearchKeyWord rkw) {
        return findRelatedGoodsInfo(rkw);
    }

    @Override
    public List<RelatedKeyWordEntity> findAll(PageInfo pageInfo) throws EwandianSearchEngineException {
        return null;
    }

    @Override
    public long getAllCount() throws EwandianSearchEngineException {
        return 0;
    }

    public List<RelatedKeyWordEntity> findRelatedKeyWordEntityPageByShopId(RelatedKeyWordPageInfo relatedKeyWordPageInfo) throws Exception {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withIndices("relatedkeywordindex")
                .withTypes("relatedkeyword")
                .withQuery(termQuery(QueryFieldConstant.shopId, relatedKeyWordPageInfo.getRelatedKeyWordEntity().getShopId()))
                .withPageable(new PageRequest(relatedKeyWordPageInfo.getPageInfo().getPageNumber()-1,relatedKeyWordPageInfo.getPageInfo().getPageSize()));
        List<RelatedKeyWordEntity> relatedKeyWordEntityList = elasticsearchTemplate.queryForList(nsqb.build(),RelatedKeyWordEntity.class);
        if(relatedKeyWordEntityList==null || relatedKeyWordEntityList.size()<=0) {
            throw new EwandianSearchEngineNoDataException("找不到关键字索引的任何数据！");
        }
        return relatedKeyWordEntityList;
    }
}

