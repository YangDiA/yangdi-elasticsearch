package com.ewandian.b2b2c.search.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ewandian.b2b2c.search.app.constant.QueryFieldConstant;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineNoDataException;
import com.ewandian.b2b2c.search.domain.document.NewsEntity;
import com.ewandian.b2b2c.search.domain.receive.PageInfo;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;
import com.ewandian.b2b2c.search.repositories.NewsRepository;
import com.ewandian.b2b2c.search.service.INewsService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Created by Administrator on 2016/12/6.
 */
@Service
@Scope("prototype")
public class NewsService implements INewsService {

    private Logger logger = LoggerFactory.getLogger(NewsService.class);

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    public void NewsAddColumnIdentify(NewsEntity inputNews) throws EwandianSearchEngineException {
        newsRepository.save(inputNews);
    }

    public void addOne(NewsEntity newsEntity) throws EwandianSearchEngineException {
        try {
            newsEntity.setId(UUID.randomUUID().toString());
            newsRepository.save(newsEntity);

        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
            throw new EwandianSearchEngineException("Insert elasticsearch data in news error：" + e.getStackTrace().toString());
        }
    }

    public List<NewsEntity> findListofNews(NewsEntity newsEntity) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withQuery(termQuery(QueryFieldConstant.articleId,newsEntity.getArticleId()));
        List<NewsEntity> newsEntityList = elasticsearchTemplate.queryForList(nsqb.build(),NewsEntity.class);
        //if(newsEntityList!=null && newsEntityList.size()>0) {
            return newsEntityList;
        //}
        //return null;
    }

    public List<NewsEntity> findListofNewsbyId(String msg) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withQuery(termQuery(QueryFieldConstant.articleId, msg));
        List<NewsEntity> newsEntityList = elasticsearchTemplate.queryForList(nsqb.build(),NewsEntity.class);
        //if(newsEntityList!=null && newsEntityList.size()>0) {
            return newsEntityList;
       // }
       // return null;
    }

    public List<NewsEntity> findNewsArticleIdAndColumnIdentify(NewsEntity newsEntity) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                //.withQuery(termQuery(QueryFieldConstant.articleId, msg));
        .withFilter(boolQuery().must(termsQuery(QueryFieldConstant.articleId, newsEntity.getArticleId())).must(termsQuery(QueryFieldConstant.cloumnIdentify, newsEntity.getCloumnIdentify())));
        List<NewsEntity> newsEntityList = elasticsearchTemplate.queryForList(nsqb.build(),NewsEntity.class);
        return newsEntityList;
    }

    public void updateOne(NewsEntity inputNews) throws EwandianSearchEngineException {
        List<NewsEntity> columnIdentifyNewsList = this.findListofNews(inputNews);
        List<NewsEntity> deletedNewsList = this.findListofNews(inputNews);
        if (columnIdentifyNewsList.size() > 0) {
            for (int i = 0; i < columnIdentifyNewsList.size(); i++) {
                inputNews.setId(UUID.randomUUID().toString());
                columnIdentifyNewsList.get(i).setId(inputNews.getId());
                columnIdentifyNewsList.get(i).setArticleId(inputNews.getArticleId());

                columnIdentifyNewsList.get(i).setUrl(inputNews.getUrl()); //Added on 2017/02/09
                columnIdentifyNewsList.get(i).setAuthor(inputNews.getAuthor()); //Added on 2017/02/09
                columnIdentifyNewsList.get(i).setKeyword(inputNews.getKeyword()); //Added on 2017/02/13
                columnIdentifyNewsList.get(i).setCorrelationId(inputNews.getCorrelationId()); //Added on 2017/02/09
                columnIdentifyNewsList.get(i).setCorrelationName(inputNews.getCorrelationName()); //Added on 2017/02/09

                columnIdentifyNewsList.get(i).setTitle(inputNews.getTitle());
                columnIdentifyNewsList.get(i).setContent(inputNews.getContent());
                columnIdentifyNewsList.get(i).setCreateDate(inputNews.getCreateDate());
                columnIdentifyNewsList.get(i).setLikeArticle(inputNews.getLikeArticle());
                columnIdentifyNewsList.get(i).setTaunt(inputNews.getTaunt());
                columnIdentifyNewsList.get(i).setImgId(inputNews.getImgId()); //Added on 2016/12/28
                columnIdentifyNewsList.get(i).setIsEffective(inputNews.getIsEffective());
                newsRepository.save(columnIdentifyNewsList.get(i));
                newsRepository.delete(deletedNewsList.get(i));
            }
        }
    }

    public void removeOne(NewsEntity newsEntity) throws EwandianSearchEngineException {
        try {
            newsRepository.delete(newsEntity);
        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
            throw new EwandianSearchEngineException("Delete elasticsearch data in news error：" + e.getStackTrace().toString());
        }
    }

    //According to elastic search id remove one entity
    public void removeOne(String jsonStr) throws EwandianSearchEngineException {
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            newsRepository.delete(jsonObject.getString("Id"));
        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
            throw new EwandianSearchEngineException("Delete elasticsearch data according to ID error：" + e.getStackTrace().toString());
        }
    }


    public void removeAll() throws EwandianSearchEngineException {
        try {
            newsRepository.deleteAll();
        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
            throw new EwandianSearchEngineException("Empty elasticsearch data error：" + e.getStackTrace().toString());
        }
    }

    public List<NewsEntity> findNewsColumnIdentify(SearchKeyWord skw) throws EwandianSearchEngineException{
        try {
            NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder()
                    .withIndices("newsindex")
                    .withTypes("news")
                    .withQuery(multiMatchQuery(skw.getSearchArg(), QueryFieldConstant.cloumnIdentify, QueryFieldConstant.articleId,QueryFieldConstant.title, QueryFieldConstant.content))
                    .withFilter(boolQuery().must(termsQuery(QueryFieldConstant.isEffective,"Y")));

            if(skw.getPageInfo().getSortBy()!=null) {
                searchQuery.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1, skw.getPageInfo().getPageSize(), skw.getPageInfo().getSortDirection(), skw.getPageInfo().getSortBy().getValue()));
            } else {
                searchQuery.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1, skw.getPageInfo().getPageSize()));
            }

            List<NewsEntity> ArticleEntityList = elasticsearchTemplate.queryForList(searchQuery.build(), NewsEntity.class);

            if(ArticleEntityList==null || ArticleEntityList.size()<=0) {
                throw new EwandianSearchEngineNoDataException("No such news, please try another keyword");
            }
            return ArticleEntityList;

        }catch (EwandianSearchEngineNoDataException e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineNoDataException("No such news, please try another keyword：" + e.getStackTrace().toString());
        }
    }

    public List<NewsEntity> findNewsByColumnIdentifyList(SearchKeyWord skw) throws EwandianSearchEngineException {
        if(skw.getNewsIdList()==null || skw.getNewsIdList().size()<=0) {
            throw new EwandianSearchEngineNoDataException("No search input!");
        }

        List<NewsEntity> resultNewsEntityList;

        try {
            NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                    .withIndices("newsindex")
                    .withTypes("news")
                    .withQuery(termsQuery(QueryFieldConstant.cloumnIdentify, skw.getNewsIdList()))
                    .withFilter(boolQuery().must(termsQuery(QueryFieldConstant.isEffective,"Y")));
                    //.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1,skw.getPageInfo().getPageSize()));

            if(skw.getPageInfo().getSortBy()!=null) {
                nsqb.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1, skw.getPageInfo().getPageSize(), skw.getPageInfo().getSortDirection(), skw.getPageInfo().getSortBy().getValue()));
            } else {
                nsqb.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1, skw.getPageInfo().getPageSize()));
            }

            resultNewsEntityList = elasticsearchTemplate.queryForList(nsqb.build(),NewsEntity.class);

            if(resultNewsEntityList==null || resultNewsEntityList.size()<=0) {
                throw new EwandianSearchEngineNoDataException("No such news with this columnIdentify!");
            }
        } catch (EwandianSearchEngineNoDataException e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineException(e.getMessage());
        }
        return resultNewsEntityList;
    }


    public long findNewsByColumnIdentifyListCount(SearchKeyWord skw) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = buildNSQBFByNewsIdList(skw);
        return elasticsearchTemplate.count(nsqb.build(),NewsEntity.class);
    }

    private NativeSearchQueryBuilder buildNSQBFByNewsIdList(SearchKeyWord skw) throws EwandianSearchEngineNoDataException {
        if(skw.getNewsIdList()==null || skw.getNewsIdList().size()<=0) {
            throw new EwandianSearchEngineNoDataException("No search input!");
        }
        return new NativeSearchQueryBuilder()
                .withIndices("newsindex")
                .withTypes("news")
                .withQuery(termsQuery(QueryFieldConstant.cloumnIdentify, skw.getNewsIdList()))
                .withFilter(boolQuery().must(termsQuery(QueryFieldConstant.isEffective,"Y")))
                .withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1,skw.getPageInfo().getPageSize()));
    }

    public List<NewsEntity> findNewsByAggregationList(final SearchKeyWord skw) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withIndices("newsindex")
                .withTypes("news")
                .withQuery(termsQuery(QueryFieldConstant.cloumnIdentify, skw.getNewsIdList()))
                .withFilter(boolQuery().must(termsQuery(QueryFieldConstant.isEffective,"Y")));

               AggregationBuilder aggregationBuilder = AggregationBuilders.terms("subjects").size(skw.getPageInfo().getPageSize())
                .field(QueryFieldConstant.articleId);

/*        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("subjects")
                .field(QueryFieldConstant.articleId)
                .subAggregation(
                        AggregationBuilders.topHits("top").setFrom((skw.getPageInfo().getPageNumber()-2)*skw.getPageInfo().getPageSize())
                                .setSize(skw.getPageInfo().getPageSize()));*/

        SearchResponse searchResponse = elasticsearchTemplate.getClient()
                .prepareSearch("newsindex")
                .setTypes("news")
                .setQuery(nsqb.build().getQuery())
                .addAggregation(aggregationBuilder)
                .execute()
                .actionGet();
        Terms terms = searchResponse.getAggregations().get("subjects");

        List<NewsEntity> resultNewsEntityList = new ArrayList<NewsEntity>();

        if(terms != null) {
            for(Terms.Bucket bucket : terms.getBuckets()) {
                NewsEntity news= new NewsEntity();
                news.setArticleId(bucket.getKey().toString());
                NewsEntity resultNews = this.findOne(news);
                resultNewsEntityList.add(resultNews);
            }
        }
        return resultNewsEntityList;
    }

    public long findNewsByAggregationListCount(SearchKeyWord skw) throws EwandianSearchEngineException {
        if(skw.getNewsIdList()==null || skw.getNewsIdList().size()<=0) {
            throw new EwandianSearchEngineNoDataException("No search input!");
        }
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withIndices("newsindex")
                .withTypes("news")
                .withQuery(termsQuery(QueryFieldConstant.cloumnIdentify, skw.getNewsIdList()))
                .withFilter(boolQuery().must(termsQuery(QueryFieldConstant.isEffective,"Y")));

        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("subjects").size(skw.getPageInfo().getPageSize())
                .field(QueryFieldConstant.articleId);

        SearchResponse searchResponse = elasticsearchTemplate.getClient()
                .prepareSearch("newsindex")
                .setTypes("news")
                .setQuery(nsqb.build().getQuery())
                .addAggregation(aggregationBuilder)
                .execute()
                .actionGet();
        Terms terms = searchResponse.getAggregations().get("subjects");
        terms.getBuckets().size();
        java.lang.System.out.println("terms.getBuckets().size(): " + terms.getBuckets().size());
        return  terms.getBuckets().size();

    }

    public List <NewsEntity> findByKeywordAndColumnIdentify(SearchKeyWord skw) throws EwandianSearchEngineException {
        List<NewsEntity> resultNewsEntityList;
        try {
            NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                    .withIndices("newsindex")
                    .withTypes("news")
                    .withFilter(boolQuery().must(multiMatchQuery(skw.getSearchArg(), QueryFieldConstant.cloumnIdentify, QueryFieldConstant.articleId,QueryFieldConstant.title, QueryFieldConstant.content)).must(termsQuery(QueryFieldConstant.cloumnIdentify,"SYLB_YC_ZC")).must(termsQuery(QueryFieldConstant.isEffective,"Y")));

            if(skw.getPageInfo().getSortBy()!=null) {
                nsqb.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1, skw.getPageInfo().getPageSize(), skw.getPageInfo().getSortDirection(), skw.getPageInfo().getSortBy().getValue()));
            } else {
                nsqb.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1, skw.getPageInfo().getPageSize()));
            }

            resultNewsEntityList = elasticsearchTemplate.queryForList(nsqb.build(),NewsEntity.class);

            if(resultNewsEntityList==null || resultNewsEntityList.size()<=0) {
                throw new EwandianSearchEngineNoDataException("No such news!");
            }
        } catch (EwandianSearchEngineNoDataException e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineNoDataException(e.getMessage());
        }
        return resultNewsEntityList;
    }


    public long findByKeywordAndColumnIdentifyCount(SearchKeyWord skw) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = buildNSQBFByKeywordAndColumnIdentify(skw);
        return elasticsearchTemplate.count(nsqb.build(),NewsEntity.class);
    }

    private NativeSearchQueryBuilder buildNSQBFByKeywordAndColumnIdentify(SearchKeyWord skw) throws EwandianSearchEngineNoDataException {

        return new NativeSearchQueryBuilder()
                .withIndices("newsindex")
                .withTypes("news")
                .withFilter(boolQuery().must(multiMatchQuery(skw.getSearchArg(), QueryFieldConstant.cloumnIdentify, QueryFieldConstant.articleId,QueryFieldConstant.title, QueryFieldConstant.content)).must(termsQuery(QueryFieldConstant.cloumnIdentify,"SYLB_YC_ZC")).must(termsQuery(QueryFieldConstant.isEffective,"Y")))
                .withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1,skw.getPageInfo().getPageSize()));
    }

    public List<NewsEntity> findAll(PageInfo pageInfo) throws EwandianSearchEngineException {
        Page<NewsEntity> newsEntityPage = newsRepository.findAll(new PageRequest(pageInfo.getPageNumber()-1,pageInfo.getPageSize()));
        List<NewsEntity> newsEntityList = new ArrayList<NewsEntity>();
        if(newsEntityPage!=null && newsEntityPage.getSize()>0) {
            newsEntityList = newsEntityPage.getContent();
        }
        return newsEntityList;
    }


    public NewsEntity findOne(NewsEntity newsEntity) throws EwandianSearchEngineException {

        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withQuery(termQuery(QueryFieldConstant.articleId,newsEntity.getArticleId()));
        List<NewsEntity> newsEntityList = elasticsearchTemplate.queryForList(nsqb.build(),NewsEntity.class);
        //if(newsEntityList!=null && newsEntityList.size()>0) {
            return newsEntityList.get(0);
        //}
        //return null;
    }

    public NewsEntity findOneById(String msg) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withQuery(termQuery(QueryFieldConstant.articleId, msg));
        List<NewsEntity> newsEntityList = elasticsearchTemplate.queryForList(nsqb.build(),NewsEntity.class);
        return newsEntityList.get(0);
    }

    public Page<NewsEntity> find(NewsEntity newsEntity) throws EwandianSearchEngineException {
        return null;
    }

    public long getAllCount() throws EwandianSearchEngineException {
        return newsRepository.count();
    }

    public long getCount(String searchArg) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withIndices("newsindex")
                .withTypes("news")
                .withQuery(multiMatchQuery(searchArg, QueryFieldConstant.cloumnIdentify, QueryFieldConstant.articleId,QueryFieldConstant.title, QueryFieldConstant.content))
                .withFilter(boolQuery().must(termsQuery(QueryFieldConstant.isEffective,"Y")));
        long count = elasticsearchTemplate.count(nsqb.build(),NewsEntity.class);
        return count;
    }
}