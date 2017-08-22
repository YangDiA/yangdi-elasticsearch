package com.ewandian.b2b2c.search.service.impl;

import com.ewandian.b2b2c.search.app.constant.QueryFieldConstant;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineNoDataException;
import com.ewandian.b2b2c.search.domain.document.AdsEntity;
import com.ewandian.b2b2c.search.domain.receive.PageInfo;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;
import com.ewandian.b2b2c.search.repositories.AdsRepository;
import com.ewandian.b2b2c.search.service.IAdsService;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Created by Administrator on 2016/12/6.
 */

@Service
@Scope("prototype")
public class AdsService implements IAdsService {

    private Logger logger = LoggerFactory.getLogger(AdsService.class);

    @Autowired
    private AdsRepository adsRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    Date currentDate = new Date(System.currentTimeMillis());


    public void addOne(AdsEntity adsEntity) throws EwandianSearchEngineException {
        try {
            adsEntity.setId(UUID.randomUUID().toString());
            adsRepository.save(adsEntity);

        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
            throw new EwandianSearchEngineException("Insert elasticsearch data in ads error：" + e.getStackTrace().toString());
        }
    }

    public void updateOne(AdsEntity inputAd) throws EwandianSearchEngineException {
        try {
            inputAd.setId(inputAd.getId());
            adsRepository.save(inputAd);

        }catch (Exception e) {
            logger.error(e.getStackTrace().toString());
            throw new EwandianSearchEngineException("update elasticsearch data in ads error：" + e.getStackTrace().toString());
        }
    }

    public void removeOne(AdsEntity adsEntity) throws EwandianSearchEngineException {
        try {
            adsRepository.delete(adsEntity);
        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
            throw new EwandianSearchEngineException("Delete elasticsearch data in ads error：" + e.getStackTrace().toString());
        }
    }

    public void removeAll() throws EwandianSearchEngineException {
        try {
            adsRepository.deleteAll();
        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
            throw new EwandianSearchEngineException("Empty elasticsearch data in ads error：" + e.getStackTrace().toString());
        }
    }

    public List<AdsEntity> findAds(SearchKeyWord skw) throws EwandianSearchEngineException{
        try {
            NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder()
                    .withIndices("adindex")
                    .withTypes("ad")
                    .withQuery(multiMatchQuery(skw.getSearchArg(),QueryFieldConstant.adId,QueryFieldConstant.columnId, QueryFieldConstant.shopId, QueryFieldConstant.columnIdentify, QueryFieldConstant.title, QueryFieldConstant.paperWork))
                    .withFilter(boolQuery().must(termsQuery(QueryFieldConstant.status,"3")).must(termsQuery(QueryFieldConstant.isDeleted,"F")).must(rangeQuery(QueryFieldConstant.deliveryStartDate).lt(currentDate.getTime())).must(rangeQuery(QueryFieldConstant.deliveryEndDate).gt(currentDate.getTime())));

            if(skw.getPageInfo().getSortBy()!=null) {
                searchQuery.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1, skw.getPageInfo().getPageSize(), skw.getPageInfo().getSortDirection(), skw.getPageInfo().getSortBy().getValue()));
            } else {
                searchQuery.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1, skw.getPageInfo().getPageSize()));
            }

            List<AdsEntity> AdsEntityList = elasticsearchTemplate.queryForList(searchQuery.build(), AdsEntity.class);
            if(AdsEntityList==null || AdsEntityList.size()<=0) {
                throw new EwandianSearchEngineNoDataException("No such ads, please try another keyword");
            }
            return AdsEntityList;

        }catch (EwandianSearchEngineNoDataException e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineNoDataException("No such ads, please try another keyword");
        }
    }

    public List<AdsEntity> findAdsByAdsList(SearchKeyWord skw) throws EwandianSearchEngineException {
        if(skw.getAdsIdList()==null || skw.getAdsIdList().size()<=0) {
            throw new EwandianSearchEngineNoDataException("No search input!");
        }
        List<AdsEntity> resultAdsEntityList;
        try {
            NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                    .withIndices("adindex")
                    .withTypes("ad")
                    .withQuery(termsQuery(QueryFieldConstant.columnIdentify, skw.getAdsIdList()))
                    .withFilter(boolQuery().must(termsQuery(QueryFieldConstant.status,"3")).must(termsQuery(QueryFieldConstant.isDeleted,"F")).must(rangeQuery(QueryFieldConstant.deliveryStartDate).lt(currentDate.getTime())).must(rangeQuery(QueryFieldConstant.deliveryEndDate).gt(currentDate.getTime())));
                    //.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1,skw.getPageInfo().getPageSize()));

            if(skw.getPageInfo().getSortBy()!=null) {
                nsqb.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1, skw.getPageInfo().getPageSize(), skw.getPageInfo().getSortDirection(), skw.getPageInfo().getSortBy().getValue()));
            } else {
                nsqb.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1, skw.getPageInfo().getPageSize()));
            }

            resultAdsEntityList = elasticsearchTemplate.queryForList(nsqb.build(),AdsEntity.class);
            if(resultAdsEntityList==null || resultAdsEntityList.size()<=0) {
                throw new EwandianSearchEngineNoDataException("No such ads!");
            }
        } catch (EwandianSearchEngineNoDataException e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineNoDataException(e.getMessage());
        }
        return resultAdsEntityList;
    }


    public long findAdsByAdsListCount(SearchKeyWord skw) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = buildNSQBFByNewsIdList(skw);
        return elasticsearchTemplate.count(nsqb.build(),AdsEntity.class);
    }

    private NativeSearchQueryBuilder buildNSQBFByNewsIdList(SearchKeyWord skw) throws EwandianSearchEngineNoDataException {
        if(skw.getAdsIdList()==null || skw.getAdsIdList().size()<=0) {
            throw new EwandianSearchEngineNoDataException("No search input!");
        }
        return new NativeSearchQueryBuilder()
                .withIndices("adindex")
                .withTypes("ad")
                .withQuery(termsQuery(QueryFieldConstant.columnIdentify, skw.getAdsIdList()))
                .withFilter(boolQuery().must(termsQuery(QueryFieldConstant.status,"3")).must(termsQuery(QueryFieldConstant.isDeleted,"F")).must(rangeQuery(QueryFieldConstant.deliveryStartDate).lt(currentDate.getTime())).must(rangeQuery(QueryFieldConstant.deliveryEndDate).gt(currentDate.getTime())));
    }

    public List <AdsEntity> findByColumnIdentifyAndShopIdList(SearchKeyWord skw) throws EwandianSearchEngineException {
        if(skw.getAdsIdList()==null || skw.getAdsIdList().size()<=0) {
            throw new EwandianSearchEngineNoDataException("No search input!");
        }
        List<AdsEntity> resultAdsEntityList;
        try {
            NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                    .withIndices("adindex")
                    .withTypes("ad")
                    .withFilter(boolQuery().must(termsQuery(QueryFieldConstant.columnIdentify,skw.getAdsIdList())).must(termsQuery(QueryFieldConstant.shopId, skw.getSearchArg())).must(termsQuery(QueryFieldConstant.status,"3")).must(termsQuery(QueryFieldConstant.isDeleted,"F")).must(rangeQuery(QueryFieldConstant.deliveryStartDate).lt(currentDate.getTime())).must(rangeQuery(QueryFieldConstant.deliveryEndDate).gt(currentDate.getTime())));
                    //.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1,skw.getPageInfo().getPageSize()));

            if(skw.getPageInfo().getSortBy()!=null) {
                nsqb.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1, skw.getPageInfo().getPageSize(), skw.getPageInfo().getSortDirection(), skw.getPageInfo().getSortBy().getValue()));
            } else {
                nsqb.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1, skw.getPageInfo().getPageSize()));
            }

            resultAdsEntityList = elasticsearchTemplate.queryForList(nsqb.build(),AdsEntity.class);
            if(resultAdsEntityList==null || resultAdsEntityList.size()<=0) {
                throw new EwandianSearchEngineNoDataException("No such ads!");
            }
        } catch (EwandianSearchEngineNoDataException e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineNoDataException(e.getMessage());
        }
        return resultAdsEntityList;
    }

    public long findByColumnIdentifyAndShopIdListCount(SearchKeyWord skw) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = buildNSQBFByColumnIdentifyAndShopIdList(skw);
        return elasticsearchTemplate.count(nsqb.build(),AdsEntity.class);
    }

    private NativeSearchQueryBuilder buildNSQBFByColumnIdentifyAndShopIdList(SearchKeyWord skw) throws EwandianSearchEngineNoDataException {
        if(skw.getAdsIdList()==null || skw.getAdsIdList().size()<=0) {
            throw new EwandianSearchEngineNoDataException("No search input!");
        }
        return new NativeSearchQueryBuilder()
                .withIndices("adindex")
                .withTypes("ad")
                .withFilter(boolQuery().must(termsQuery(QueryFieldConstant.columnIdentify,skw.getAdsIdList())).must(termsQuery(QueryFieldConstant.shopId, skw.getSearchArg())).must(termsQuery(QueryFieldConstant.status,"3")).must(termsQuery(QueryFieldConstant.isDeleted,"F")).must(rangeQuery(QueryFieldConstant.deliveryStartDate).lt(currentDate.getTime())).must(rangeQuery(QueryFieldConstant.deliveryEndDate).gt(currentDate.getTime())));
    }

    public List<AdsEntity> findAll(PageInfo pageInfo) throws EwandianSearchEngineException {
        Page<AdsEntity> AdsEntityPage = adsRepository.findAll(new PageRequest(pageInfo.getPageNumber()-1,pageInfo.getPageSize()));
        List<AdsEntity> AdsEntityList = new ArrayList<AdsEntity>();
        if(AdsEntityPage!=null && AdsEntityPage.getSize()>0) {
            AdsEntityList = AdsEntityPage.getContent();
        }
        return AdsEntityList;
    }

    public AdsEntity findOne(AdsEntity adsEntity) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withQuery(termQuery(QueryFieldConstant.adId,adsEntity.getAdId()));
        List<AdsEntity> adsEntityList = elasticsearchTemplate.queryForList(nsqb.build(),AdsEntity.class);
        if(adsEntityList!=null && adsEntityList.size()>0) {
            return adsEntityList.get(0);
        }
        return null;
    }

    public Page<AdsEntity> find(AdsEntity adsEntity) throws EwandianSearchEngineException {
        return null;
    }

    public long getAllCount() throws EwandianSearchEngineException {
        return adsRepository.count();
    }

    public long getCount(String searchArg) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withIndices("adindex")
                .withTypes("ad")
                .withQuery(multiMatchQuery(searchArg,QueryFieldConstant.adId,QueryFieldConstant.columnId, QueryFieldConstant.shopId, QueryFieldConstant.columnIdentify, QueryFieldConstant.title, QueryFieldConstant.paperWork))
                .withFilter(boolQuery().must(termsQuery(QueryFieldConstant.status,"3")).must(termsQuery(QueryFieldConstant.isDeleted,"F")).must(rangeQuery(QueryFieldConstant.deliveryStartDate).lt(currentDate.getTime())).must(rangeQuery(QueryFieldConstant.deliveryEndDate).gt(currentDate.getTime())));

        long count = elasticsearchTemplate.count(nsqb.build(),AdsEntity.class);
        return count;
    }


    public List<AdsEntity> test(SearchKeyWord skw) throws EwandianSearchEngineException{
        try {
            NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder()
                    .withIndices("adindex")
                    .withTypes("ad")
                    .withQuery(multiMatchQuery(skw.getSearchArg(),QueryFieldConstant.adId,QueryFieldConstant.columnId, QueryFieldConstant.shopId, QueryFieldConstant.columnIdentify, QueryFieldConstant.title, QueryFieldConstant.paperWork))
                    .withFilter(boolQuery().must(termsQuery(QueryFieldConstant.status,"3")).must(termsQuery(QueryFieldConstant.isDeleted,"F")).must(rangeQuery(QueryFieldConstant.deliveryStartDate).lt(currentDate.getTime())).must(rangeQuery(QueryFieldConstant.deliveryEndDate).gt(currentDate.getTime())))
                    .withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1,skw.getPageInfo().getPageSize()));

            List<AdsEntity> AdsEntityList = elasticsearchTemplate.queryForList(searchQuery.build(), AdsEntity.class);
            if(AdsEntityList==null || AdsEntityList.size()<=0) {
                throw new EwandianSearchEngineNoDataException("No such ads, please try another keyword");
            }
            return AdsEntityList;

        }catch (EwandianSearchEngineNoDataException e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineNoDataException("No such ads, please try another keyword");
        }
    }
}
