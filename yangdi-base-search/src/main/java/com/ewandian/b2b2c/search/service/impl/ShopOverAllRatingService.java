package com.ewandian.b2b2c.search.service.impl;

import com.ewandian.b2b2c.search.app.constant.QueryFieldConstant;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineNoDataException;
import com.ewandian.b2b2c.search.domain.document.ShopOverAllRatingEntity;
import com.ewandian.b2b2c.search.domain.receive.PageInfo;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;
import com.ewandian.b2b2c.search.repositories.ShopOverAllRatingRepository;
import com.ewandian.b2b2c.search.service.IShopOverAllRatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

/**
 * Created by suhd on 2016-12-14.
 */
@Service
@Scope("prototype")
public class ShopOverAllRatingService implements IShopOverAllRatingService {
    @Autowired
    private ShopOverAllRatingRepository shopOverAllRatingRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    private Logger logger = LoggerFactory.getLogger(ShopOverAllRatingService.class);

    public void addOne(ShopOverAllRatingEntity shopOverAllRatingEntity) throws EwandianSearchEngineException {
        ShopOverAllRatingEntity obj = findOne(shopOverAllRatingEntity);
        if(obj==null) {
            shopOverAllRatingEntity.setId(UUID.randomUUID().toString());
            shopOverAllRatingRepository.save(shopOverAllRatingEntity);
        }
    }

    public void updateOne(ShopOverAllRatingEntity shopOverAllRatingEntity) throws EwandianSearchEngineException {
        ShopOverAllRatingEntity obj = findOne(shopOverAllRatingEntity);
        if(obj!=null) {
            shopOverAllRatingEntity.setId(obj.getId());
        }
        shopOverAllRatingRepository.save(obj);
    }

    public void removeOne(ShopOverAllRatingEntity shopOverAllRatingEntity) throws EwandianSearchEngineException {
        ShopOverAllRatingEntity obj = findOne(shopOverAllRatingEntity);
        if(obj!=null) {
            shopOverAllRatingEntity.setId(obj.getId());
        }
        shopOverAllRatingRepository.delete(obj);
    }

    private ShopOverAllRatingEntity findOne(ShopOverAllRatingEntity shopOverAllRatingEntity) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withQuery(termQuery(QueryFieldConstant.shopId, shopOverAllRatingEntity.getShopId()));
        List<ShopOverAllRatingEntity> shopOverAllRatingEntityList = elasticsearchTemplate.queryForList(nsqb.build(),ShopOverAllRatingEntity.class);
        if(shopOverAllRatingEntityList!=null && shopOverAllRatingEntityList.size()>0) {
            return shopOverAllRatingEntityList.get(0);
        }
        return null;
    }

    public List<ShopOverAllRatingEntity> findAll(PageInfo pageInfo) throws EwandianSearchEngineException {
        return null;
    }

    public long getAllCount() throws EwandianSearchEngineException {
        return 0;
    }

    public ShopOverAllRatingEntity findOneByShopId(SearchKeyWord skw) throws EwandianSearchEngineException {
        ShopOverAllRatingEntity shopOverAllRatingEntity = new ShopOverAllRatingEntity();
        shopOverAllRatingEntity.setShopId(skw.getSearchArg());
        shopOverAllRatingEntity = findOne(shopOverAllRatingEntity);
        if(shopOverAllRatingEntity==null) {
            throw new EwandianSearchEngineNoDataException("搜索引擎找不到该店铺评分信息");
        }
        return shopOverAllRatingEntity;
    }

    public List<ShopOverAllRatingEntity> findShopRatingsByShopIdList(List<String> shopIdList,PageInfo pageInfo) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withQuery(termsQuery(QueryFieldConstant.shopId, shopIdList))
                .withPageable(new PageRequest(pageInfo.getPageNumber()-1,pageInfo.getPageSize()));
        List<ShopOverAllRatingEntity> shopOverAllRatingEntityList = elasticsearchTemplate.queryForList(nsqb.build(),ShopOverAllRatingEntity.class);
        if(shopOverAllRatingEntityList!=null && shopOverAllRatingEntityList.size()>0) {
            return shopOverAllRatingEntityList;
        }
        return null;
    }
}
