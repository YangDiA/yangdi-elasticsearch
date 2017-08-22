package com.ewandian.b2b2c.search.service;

import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.NewsCorrelationEntity;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;

import java.util.List;

/**
 * Created by Administrator on 2016/12/20.
 */
public interface INewsCorrelationService extends ICommonService<NewsCorrelationEntity> {
    List<NewsCorrelationEntity> findListofNews(NewsCorrelationEntity newsCorrelationEntity) throws EwandianSearchEngineException;
    List<NewsCorrelationEntity> findNewsCorrelation(SearchKeyWord skw) throws EwandianSearchEngineException;
    NewsCorrelationEntity findOneById(String msg) throws EwandianSearchEngineException;
    List<NewsCorrelationEntity> findNewsByCorrelationIdNewsList(SearchKeyWord skw) throws EwandianSearchEngineException;
    List<NewsCorrelationEntity> findListofNewsbyId(String msg) throws EwandianSearchEngineException;
    void NewsAddCorrelationId(NewsCorrelationEntity originalNews) throws EwandianSearchEngineException;
    List<NewsCorrelationEntity> findNewsArticleIdAndCorrelation(NewsCorrelationEntity newsCorrelationEntity) throws EwandianSearchEngineException;
    long findNewsByCorrelationIdNewsListCount(SearchKeyWord skw) throws EwandianSearchEngineException;
    long getCount(String searchArg) throws EwandianSearchEngineException;
}