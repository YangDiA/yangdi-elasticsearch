package com.ewandian.b2b2c.search.service;

import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.NewsEntity;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;
import org.elasticsearch.search.aggregations.Aggregation;

import java.util.List;

/**
 * Created by Administrator on 2016/12/6.
 */
public interface INewsService extends ICommonService<NewsEntity> {
    List<NewsEntity> findNewsColumnIdentify(SearchKeyWord skw) throws EwandianSearchEngineException;
    List<NewsEntity> findNewsByColumnIdentifyList(SearchKeyWord skw) throws EwandianSearchEngineException;
    NewsEntity findOne(NewsEntity newsEntity) throws EwandianSearchEngineException;
    NewsEntity findOneById(String msg) throws EwandianSearchEngineException;
    List<NewsEntity> findListofNews(NewsEntity newsEntity) throws EwandianSearchEngineException;
    List<NewsEntity> findListofNewsbyId(String msg) throws EwandianSearchEngineException;
    void NewsAddColumnIdentify(NewsEntity originalNews) throws EwandianSearchEngineException;
    List<NewsEntity> findNewsArticleIdAndColumnIdentify(NewsEntity newsEntity) throws EwandianSearchEngineException;
    List<NewsEntity> findByKeywordAndColumnIdentify(SearchKeyWord skw) throws EwandianSearchEngineException;
    long findNewsByColumnIdentifyListCount(SearchKeyWord skw) throws EwandianSearchEngineException;
    long findByKeywordAndColumnIdentifyCount(SearchKeyWord skw) throws EwandianSearchEngineException;
    long findNewsByAggregationListCount(SearchKeyWord skw) throws EwandianSearchEngineException;
    long getCount(String searchArg) throws EwandianSearchEngineException;


    List<NewsEntity> findNewsByAggregationList(SearchKeyWord skw) throws EwandianSearchEngineException;
}