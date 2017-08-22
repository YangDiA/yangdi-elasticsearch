package com.ewandian.b2b2c.search.service.impl;

import com.ewandian.b2b2c.search.app.constant.QueryFieldConstant;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineNoDataException;
import com.ewandian.b2b2c.search.app.utils.CalculateDistanceUtil;
import com.ewandian.b2b2c.search.domain.document.ShopEntity;
import com.ewandian.b2b2c.search.domain.document.ShopOverAllRatingEntity;
import com.ewandian.b2b2c.search.domain.keyword.RecommendShop;
import com.ewandian.b2b2c.search.domain.receive.PageInfo;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;
import com.ewandian.b2b2c.search.domain.receive.ShopPageInfo;
import com.ewandian.b2b2c.search.repositories.ShopRepository;
import com.ewandian.b2b2c.search.service.IShopOverAllRatingService;
import com.ewandian.b2b2c.search.service.IShopService;
import com.ewandian.platform.util.StringUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Created by suhd on 2016-11-28.
 */
@Service
@Scope("prototype")
public class ShopService implements IShopService {

    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private IShopOverAllRatingService shopOverAllRatingService;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    private Logger logger = LoggerFactory.getLogger(ShopService.class);

    public void addOne(ShopEntity shopEntity) throws EwandianSearchEngineException {
        //Modified by XH YU on 2016/12/19
        ShopEntity obj = findOne(shopEntity);
        if(obj==null) {
            shopEntity.setId(UUID.randomUUID().toString());
            shopRepository.save(shopEntity);
        }
    }

    public void updateOne(ShopEntity shopEntity) throws EwandianSearchEngineException {
        //Modified by XH YU on 2016/12/19
        ShopEntity obj = findOne(shopEntity);
        if(obj!=null) {
            shopEntity.setId(obj.getId());
            shopRepository.save(shopEntity);
        }
    }

    public void removeOne(ShopEntity shopEntity) throws EwandianSearchEngineException {
        //Modified by XH YU on 2016/12/19
        ShopEntity obj = findOne(shopEntity);
        if(obj!=null) {
            shopEntity.setId(obj.getId());
            shopRepository.delete(shopEntity);
        }
    }

    public List<ShopEntity> findAll(PageInfo pageInfo) throws EwandianSearchEngineException {
        Page<ShopEntity> shopEntityPage = shopRepository.findAll(new PageRequest(pageInfo.getPageNumber()-1,pageInfo.getPageSize()));
        List<ShopEntity> shopEntityList = new ArrayList<ShopEntity>();
        if(shopEntityPage!=null && shopEntityPage.getSize()>0) {
            shopEntityList = shopEntityPage.getContent();
        }
        return shopEntityList;
    }

    @Override
    public long getAllCount() throws EwandianSearchEngineException {
        return shopRepository.count();
    }

    @Override
    public ShopEntity findOneByShopId(String shopId) throws EwandianSearchEngineException {
        StringQuery stringQuery = new StringQuery(termQuery(QueryFieldConstant.shopId,shopId).toString());
        ShopEntity shopEntity = elasticsearchTemplate.queryForObject(stringQuery,ShopEntity.class);
        if(shopEntity==null) {
            throw new EwandianSearchEngineNoDataException();
        }
        return shopEntity;
    }

    @Override
    public List<ShopEntity> findShopsByShopName(SearchKeyWord skw) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withIndices("shopindex")
                .withTypes("shop")
                .withQuery(matchPhrasePrefixQuery(QueryFieldConstant.shopName,skw.getSearchArg()))
                .withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1,skw.getPageInfo().getPageSize()));
        List<ShopEntity> shopEntityList = elasticsearchTemplate.queryForList(nsqb.build(),ShopEntity.class);
        if(shopEntityList==null || shopEntityList.size()<=0) {
            throw new EwandianSearchEngineNoDataException("抱歉，没有找到任何店铺搜索结果");
        }
        addRatingToShops(skw.getPageInfo(), shopEntityList);
        return shopEntityList;
    }

    private void addRatingToShops(PageInfo pageInfo, List<ShopEntity> shopEntityList) throws EwandianSearchEngineException {
        List<String> shopIdList = new ArrayList<String>();
        for(int i=0; i<shopEntityList.size(); i++) {
            shopIdList.add(shopEntityList.get(i).getShopId());
        }
        List<ShopOverAllRatingEntity> shopOverAllRatingEntityList = shopOverAllRatingService.findShopRatingsByShopIdList(shopIdList,pageInfo);
        if(shopOverAllRatingEntityList!=null && shopOverAllRatingEntityList.size()>0) {
            for(int i=0; i<shopEntityList.size(); i++) {
                for(int j=0; j<shopOverAllRatingEntityList.size(); j++) {
                    if(shopEntityList.get(i).getShopId().equals(shopOverAllRatingEntityList.get(j).getShopId())) {
                        shopEntityList.get(i).setRating(shopOverAllRatingEntityList.get(j).getRating());
                        shopEntityList.get(i).setCustomerRating(shopOverAllRatingEntityList.get(j).getCustomerRating());
                        shopEntityList.get(i).setGoodsRating(shopOverAllRatingEntityList.get(j).getGoodsRating());
                        shopEntityList.get(i).setLogisticsRating(shopOverAllRatingEntityList.get(j).getLogisticsRating());
                    }
                }
            }
        }
    }

    @Override
    public long getCount(String searchArg) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withIndices("shopindex")
                .withTypes("shop")
                .withQuery(matchPhrasePrefixQuery(QueryFieldConstant.shopName,searchArg));
        long count = elasticsearchTemplate.count(nsqb.build(),ShopEntity.class);
        return count;
    }

    @Override
    public List<ShopEntity> findShopsPageByShopEntity(ShopPageInfo shopPageInfo) throws EwandianSearchEngineException {
        try {
            /*NativeSearchQueryBuilder nsqb = getFindShopsPageByShopEntitySearchQueryBuilder(shopPageInfo);
            List<ShopEntity> shopEntityList = elasticsearchTemplate.queryForList(nsqb.build(),ShopEntity.class);
            */
            ShopPageInfo shopPageInfoAll = new ShopPageInfo();
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPageSize(9999);
            pageInfo.setPageNumber(1);
            BeanUtils.copyProperties(shopPageInfo,shopPageInfoAll);
            shopPageInfoAll.setPageInfo(pageInfo);
            NativeSearchQueryBuilder nsqbAll = getFindShopsPageByShopEntitySearchQueryBuilder(shopPageInfoAll);
            List<ShopEntity> shopEntityList = elasticsearchTemplate.queryForList(nsqbAll.build(),ShopEntity.class);

            if(shopEntityList==null || shopEntityList.size()<=0) {
                throw new EwandianSearchEngineNoDataException("抱歉，没有找到任何店铺搜索结果");
            }
            if(StringUtil.isNotNullAndNotEmpty(shopPageInfo.getCoordinateX())&&StringUtil.isNotNullAndNotEmpty(shopPageInfo.getCoordinateY())){
                //距离近的排在前面
                shopEntityList = findShopOrderByDistance(shopEntityList,shopPageInfo.getCoordinateX(),shopPageInfo.getCoordinateY());
            }
            int pageNumber = shopPageInfo.getPageInfo().getPageNumber();
            int pageSize = shopPageInfo.getPageInfo().getPageSize();
            boolean isFullPage = shopEntityList.size()%pageSize==0;
            int shopEntityListPageNumber = isFullPage?shopEntityList.size()/pageSize:shopEntityList.size()/pageSize+1;

            if (pageNumber ==shopEntityListPageNumber){//最后一页
                shopEntityList =shopEntityList.subList((pageNumber-1)*pageSize,shopEntityList.size());
            }else if(pageNumber < shopEntityListPageNumber){
                shopEntityList =shopEntityList.subList((pageNumber-1)*pageSize,pageNumber*pageSize);
            }else {
                throw new EwandianSearchEngineNoDataException("请输入正确的页码");
            }

            addRatingToShops(shopPageInfo.getPageInfo(), shopEntityList);
            return shopEntityList;
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineException(e.getMessage());
        }
    }

    /**
     * 计算距离
     * @param shopEntityList
     * @param coordinateX
     * @param coordinateY
     * @return
     */
    private List<ShopEntity> findShopOrderByDistance(List<ShopEntity> shopEntityList,String coordinateX, String coordinateY){
        Map<String,String> shopIdDistanceMap = new HashMap<String, String>();
         shopEntityList = new ArrayList<ShopEntity>(shopEntityList);
        for(int i=0; i<shopEntityList.size(); i++) {
            double distance = CalculateDistanceUtil.calculateDistance(coordinateX,coordinateY,shopEntityList.get(i).getCoordinateX(),shopEntityList.get(i).getCoordinateY());
            shopIdDistanceMap.put(shopEntityList.get(i).getShopId(),String.valueOf(distance));
        }
        for(int i=1; i<shopEntityList.size()-1; i++) {//第一个不排序，第一个相关性最高
            for(int j=i+1; j<shopEntityList.size(); j++) {
                if(Double.valueOf(shopIdDistanceMap.get(shopEntityList.get(i).getShopId())) > Double.valueOf(shopIdDistanceMap.get(shopEntityList.get(j).getShopId()))) {
                    ShopEntity shop = shopEntityList.get(j);
                    shopEntityList.set(j,shopEntityList.get(i));
                    shopEntityList.set(i,shop);
                }
            }
        }
        return shopEntityList;
    }

    @Override
    public long getFindShopsPageByShopEntityCount(ShopPageInfo shopPageInfo) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = getFindShopsPageByShopEntitySearchQueryBuilder(shopPageInfo);
        return elasticsearchTemplate.count(nsqb.build(),ShopEntity.class);
    }

    private NativeSearchQueryBuilder getFindShopsPageByShopEntitySearchQueryBuilder(ShopPageInfo shopPageInfo) throws EwandianSearchEngineException {
        try {
            NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                    .withIndices("shopindex")
                    .withTypes("shop");
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            if(shopPageInfo.getShopEntity()!=null && StringUtil.isNotNullAndNotEmpty(shopPageInfo.getShopEntity().getShopName())) {
                boolQueryBuilder.must(matchQuery(QueryFieldConstant.shopName, shopPageInfo.getShopEntity().getShopName()));
            }
            if(shopPageInfo.getShopEntity()!=null && StringUtil.isNotNullAndNotEmpty(shopPageInfo.getShopEntity().getAreaName())) {
                boolQueryBuilder.must(matchPhrasePrefixQuery(QueryFieldConstant.areaName, shopPageInfo.getShopEntity().getAreaName()));
            }
            if(shopPageInfo.getShopEntity()!=null && StringUtil.isNotNullAndNotEmpty(shopPageInfo.getShopEntity().getShopType())) {
                boolQueryBuilder.must(termQuery(QueryFieldConstant.shopType,shopPageInfo.getShopEntity().getShopType()));
            }
            if(boolQueryBuilder.hasClauses()) {
                nsqb.withQuery(boolQueryBuilder);
            }
            nsqb.withPageable(new PageRequest(shopPageInfo.getPageInfo().getPageNumber()-1,shopPageInfo.getPageInfo().getPageSize()));
            return nsqb;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineException(e.getMessage());
        }
    }

    public List<ShopEntity> findShopsPageByShopIdList(SearchKeyWord skw) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = buildNSQBForFindShopsPageByShopIdList(skw);
        List<ShopEntity> shopEntityList = elasticsearchTemplate.queryForList(nsqb.build(),ShopEntity.class);
        if(shopEntityList!=null && shopEntityList.size()>0) {
            addRatingToShops(skw.getPageInfo(), shopEntityList);
            return shopEntityList;
        }
        return null;
    }

    public long getShopsPageByShopIdListCount(SearchKeyWord skw) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = buildNSQBForFindShopsPageByShopIdList(skw);
        return elasticsearchTemplate.count(nsqb.build(),ShopEntity.class);
    }

    private NativeSearchQueryBuilder buildNSQBForFindShopsPageByShopIdList(SearchKeyWord skw) {
        return new NativeSearchQueryBuilder()
                .withIndices("shopindex")
                .withTypes("shop")
                .withQuery(termsQuery(QueryFieldConstant.shopId,skw.getShopIdList()))
                .withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1,skw.getPageInfo().getPageSize()));
    }

    public ShopEntity findOne(ShopEntity shopEntity) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withQuery(termQuery(QueryFieldConstant.shopId,shopEntity.getShopId()));
        List<ShopEntity> shopEntityList = elasticsearchTemplate.queryForList(nsqb.build(),ShopEntity.class);
        if(shopEntityList!=null && shopEntityList.size()>0) {
            return shopEntityList.get(0);
        }
        return null;
    }
}
