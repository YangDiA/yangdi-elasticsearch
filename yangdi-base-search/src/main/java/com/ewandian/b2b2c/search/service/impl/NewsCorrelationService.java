package com.ewandian.b2b2c.search.service.impl;

import com.ewandian.b2b2c.search.app.constant.QueryFieldConstant;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineNoDataException;
import com.ewandian.b2b2c.search.domain.document.NewsCorrelationEntity;
import com.ewandian.b2b2c.search.domain.receive.PageInfo;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;
import com.ewandian.b2b2c.search.repositories.NewsCorrelationRepository;
import com.ewandian.b2b2c.search.service.INewsCorrelationService;
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
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

/**
 * Created by Administrator on 2016/12/20.
 */

@Service
@Scope("prototype")
public class NewsCorrelationService implements INewsCorrelationService {

    private Logger logger = LoggerFactory.getLogger(NewsCorrelationService.class);

    @Autowired
    private NewsCorrelationRepository newsCorrelationRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public List<NewsCorrelationEntity> findNewsCorrelation(SearchKeyWord skw) throws EwandianSearchEngineException{
        try {
            NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder()
                    .withIndices("newscorrelationindex")
                    .withTypes("news")
                    .withQuery(multiMatchQuery(skw.getSearchArg(),QueryFieldConstant.correlationId,QueryFieldConstant.articleId, QueryFieldConstant.correlationName,QueryFieldConstant.title, QueryFieldConstant.content))
                    .withFilter(boolQuery().must(termsQuery(QueryFieldConstant.isEffective,"Y")));

            if(skw.getPageInfo().getSortBy()!=null) {
                searchQuery.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1, skw.getPageInfo().getPageSize(), skw.getPageInfo().getSortDirection(), skw.getPageInfo().getSortBy().getValue()));
            } else {
                searchQuery.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1, skw.getPageInfo().getPageSize()));
                //.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1,skw.getPageInfo().getPageSize()));
            }

            List<NewsCorrelationEntity> NewsCorrelationEntityList = elasticsearchTemplate.queryForList(searchQuery.build(), NewsCorrelationEntity.class);

            if(NewsCorrelationEntityList==null || NewsCorrelationEntityList.size()<=0) {
                throw new EwandianSearchEngineNoDataException("No such news, please try another keyword: ");
            }
            return NewsCorrelationEntityList;

        }catch (EwandianSearchEngineNoDataException e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineNoDataException("No such news, please try another keyword：" + e.getStackTrace().toString());
        }
    }


    public List<NewsCorrelationEntity> findNewsArticleIdAndCorrelation(NewsCorrelationEntity newsCorrelationEntity) throws EwandianSearchEngineException {
            NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                    .withFilter(boolQuery().must(termsQuery(QueryFieldConstant.articleId, newsCorrelationEntity.getArticleId())).must(termsQuery(QueryFieldConstant.correlationId, newsCorrelationEntity.getCorrelationId())));
            List<NewsCorrelationEntity> newsCorrelationEntityList = elasticsearchTemplate.queryForList(nsqb.build(), NewsCorrelationEntity.class);
        return newsCorrelationEntityList;
    }


    public List<NewsCorrelationEntity> findNewsByCorrelationIdNewsList(SearchKeyWord skw) throws EwandianSearchEngineException {
        if(skw.getNewsIdList()==null || skw.getNewsIdList().size()<=0) {
            throw new EwandianSearchEngineNoDataException("No search input!");
        }
        List<NewsCorrelationEntity> resultCorrelationNewsEntityList;
        try {
            NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                    .withIndices("newscorrelationindex")
                    .withTypes("news")
                    .withQuery(termsQuery(QueryFieldConstant.correlationId, skw.getNewsIdList()))
                    .withFilter(boolQuery().must(termsQuery(QueryFieldConstant.isEffective,"Y")));

            if(skw.getPageInfo().getSortBy()!=null) {
                nsqb.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1, skw.getPageInfo().getPageSize(), skw.getPageInfo().getSortDirection(), skw.getPageInfo().getSortBy().getValue()));
            } else {
                nsqb.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1, skw.getPageInfo().getPageSize()));
            }

            resultCorrelationNewsEntityList = elasticsearchTemplate.queryForList(nsqb.build(),NewsCorrelationEntity.class);

            if(resultCorrelationNewsEntityList==null || resultCorrelationNewsEntityList.size()<=0) {
                throw new EwandianSearchEngineNoDataException("No such news with these correlationIds!");
            }
        } catch (EwandianSearchEngineNoDataException e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineException(e.getMessage());
        }
        return resultCorrelationNewsEntityList;
    }

    public long findNewsByCorrelationIdNewsListCount(SearchKeyWord skw) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = buildNSQBFByNewsIdList(skw);
        return elasticsearchTemplate.count(nsqb.build(),NewsCorrelationEntity.class);
    }

    private NativeSearchQueryBuilder buildNSQBFByNewsIdList(SearchKeyWord skw) throws EwandianSearchEngineNoDataException {
        if(skw.getNewsIdList()==null || skw.getNewsIdList().size()<=0) {
            throw new EwandianSearchEngineNoDataException("No search input!");
        }
        return new NativeSearchQueryBuilder()
                .withIndices("newscorrelationindex")
                .withTypes("news")
                .withQuery(termsQuery(QueryFieldConstant.correlationId, skw.getNewsIdList()))
                .withFilter(boolQuery().must(termsQuery(QueryFieldConstant.isEffective,"Y")))
                .withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1,skw.getPageInfo().getPageSize()));
    }

    public List<NewsCorrelationEntity> findListofNews(NewsCorrelationEntity newsEntity) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withQuery(termQuery(QueryFieldConstant.articleId,newsEntity.getArticleId()));
        List<NewsCorrelationEntity> newsEntityList = elasticsearchTemplate.queryForList(nsqb.build(),NewsCorrelationEntity.class);
        //if(newsEntityList!=null && newsEntityList.size()>0) {
            return newsEntityList;
        //}
        //return null;
    }

    public NewsCorrelationEntity findOne(NewsCorrelationEntity newsCorrelationEntity) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withQuery(termQuery(QueryFieldConstant.articleId,newsCorrelationEntity.getArticleId()));
        List<NewsCorrelationEntity> newsEntityList = elasticsearchTemplate.queryForList(nsqb.build(),NewsCorrelationEntity.class);
        if(newsEntityList!=null && newsEntityList.size()>0) {
            return newsEntityList.get(0);
        }
        return null;
    }

    public NewsCorrelationEntity findOneById(String msg) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withQuery(termQuery(QueryFieldConstant.articleId, msg));
        List<NewsCorrelationEntity> newsEntityList = elasticsearchTemplate.queryForList(nsqb.build(),NewsCorrelationEntity.class);
        return newsEntityList.get(0);
    }

    public List<NewsCorrelationEntity> findListofNewsbyId(String msg) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withQuery(termQuery(QueryFieldConstant.articleId, msg));
        List<NewsCorrelationEntity> newsEntityList = elasticsearchTemplate.queryForList(nsqb.build(),NewsCorrelationEntity.class);
        //if(newsEntityList!=null && newsEntityList.size()>0) {
            return newsEntityList;
        //}else{
           // return null;
        //}
    }

    public Page<NewsCorrelationEntity> find(NewsCorrelationEntity newsCorrelationEntity) throws EwandianSearchEngineException {
        return null;
    }

    public List<NewsCorrelationEntity> findAll(PageInfo pageInfo) throws EwandianSearchEngineException {
        Page<NewsCorrelationEntity> newsEntityPage = newsCorrelationRepository.findAll(new PageRequest(pageInfo.getPageNumber()-1,pageInfo.getPageSize()));
        List<NewsCorrelationEntity> newsEntityList = new ArrayList<NewsCorrelationEntity>();
        if(newsEntityPage!=null && newsEntityPage.getSize()>0) {
            newsEntityList = newsEntityPage.getContent();
        }
        return newsEntityList;
    }

    public void removeOne(NewsCorrelationEntity newsCorrelationEntity) throws EwandianSearchEngineException{
        try {
            newsCorrelationRepository.delete(newsCorrelationEntity);
        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
            throw new EwandianSearchEngineException("Delete elasticsearch data in news error：" + e.getStackTrace().toString());
        }
    }

    public void updateOne(NewsCorrelationEntity inputNewsCorrelation) throws EwandianSearchEngineException {
        List<NewsCorrelationEntity> correlationNewsList = this.findListofNews(inputNewsCorrelation);
        List<NewsCorrelationEntity> deletedNewsList = this.findListofNews(inputNewsCorrelation);
        System.out.println("correlationNewsList.size: " + correlationNewsList.size());
        if (correlationNewsList.size() > 0) {
            for (int i = 0; i < correlationNewsList.size(); i++) {
                inputNewsCorrelation.setId(UUID.randomUUID().toString());
                correlationNewsList.get(i).setId(inputNewsCorrelation.getId());
                correlationNewsList.get(i).setArticleId(inputNewsCorrelation.getArticleId());

                correlationNewsList.get(i).setUrl(inputNewsCorrelation.getUrl()); //Added on 2017/02/09
                correlationNewsList.get(i).setAuthor(inputNewsCorrelation.getAuthor()); //Added on 2017/02/09
                correlationNewsList.get(i).setKeyword(inputNewsCorrelation.getKeyword()); //Added on 2017/02/13

                correlationNewsList.get(i).setTitle(inputNewsCorrelation.getTitle());
                correlationNewsList.get(i).setContent(inputNewsCorrelation.getContent());
                correlationNewsList.get(i).setCreateDate(inputNewsCorrelation.getCreateDate());
                correlationNewsList.get(i).setLikeArticle(inputNewsCorrelation.getLikeArticle());
                correlationNewsList.get(i).setTaunt(inputNewsCorrelation.getTaunt()); //Added on 2016/12/28
                correlationNewsList.get(i).setIsEffective(inputNewsCorrelation.getIsEffective());
                correlationNewsList.get(i).setImgId(inputNewsCorrelation.getImgId());
                newsCorrelationRepository.save(correlationNewsList.get(i));
                newsCorrelationRepository.delete(deletedNewsList.get(i));
            }
        }
    }

    public void NewsAddCorrelationId(NewsCorrelationEntity inputNewsCorrelation) throws EwandianSearchEngineException {
        newsCorrelationRepository.save(inputNewsCorrelation);
    }

    public void addOne(NewsCorrelationEntity newsCorrelationEntity) throws EwandianSearchEngineException{
        try {
            newsCorrelationEntity.setId(UUID.randomUUID().toString());
            newsCorrelationRepository.save(newsCorrelationEntity);

        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
            throw new EwandianSearchEngineException("Insert elasticsearch data in news correlation error：" + e.getStackTrace().toString());
        }
    }

    public long getAllCount() throws EwandianSearchEngineException {
        return newsCorrelationRepository.count();
    }

    public long getCount(String searchArg) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withIndices("newscorrelationindex")
                .withTypes("news")
                .withQuery(multiMatchQuery(searchArg,QueryFieldConstant.correlationId,QueryFieldConstant.articleId, QueryFieldConstant.correlationName,QueryFieldConstant.title, QueryFieldConstant.content))
                .withFilter(boolQuery().must(termsQuery(QueryFieldConstant.isEffective,"Y")));

        long count = elasticsearchTemplate.count(nsqb.build(),NewsCorrelationEntity.class);
        return count;
    }
}