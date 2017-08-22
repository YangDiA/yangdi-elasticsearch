package com.ewandian.b2b2c.search.web.controller;

import com.ewandian.b2b2c.search.app.constant.Status;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineNoDataException;
import com.ewandian.b2b2c.search.domain.document.NewsEntity;
import com.ewandian.b2b2c.search.domain.document.NewsCorrelationEntity;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;
import com.ewandian.b2b2c.search.domain.result.Result;
import com.ewandian.b2b2c.search.service.INewsService;
import com.ewandian.b2b2c.search.service.INewsCorrelationService;
import org.elasticsearch.search.aggregations.Aggregation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Administrator on 2016/12/6.
 */
@RestController
@Scope("prototype")
public class NewsController {

    @Autowired
    private INewsService newsService;


    @Autowired
    private INewsCorrelationService newsCorrelationService;

    private Logger logger = LoggerFactory.getLogger(NewsController.class);

    @RequestMapping(value = "/search/news", method = RequestMethod.POST, produces="application/json")
    public Result searchNews(@RequestBody SearchKeyWord skw) {
        Result SearchNewsResult;
        if(StringUtils.isEmpty(skw.getSearchArg())) {
            SearchNewsResult = new Result(Status.OK, null, null);
        }else {
            try {
                List<NewsEntity> NewsEntityList = newsService.findNewsColumnIdentify(skw);
                long total = newsService.getCount(skw.getSearchArg());
                long totalPage = (total==0||skw.getPageInfo().getPageSize()==1||skw.getPageInfo().getPageSize()==total)?total/skw.getPageInfo().getPageSize():total/skw.getPageInfo().getPageSize() + 1;
                long page = skw.getPageInfo().getPageNumber();
                long pageCount = skw.getPageInfo().getPageSize();
                SearchNewsResult = new Result(Status.OK, "", NewsEntityList, total, totalPage, page, pageCount);
            } catch (EwandianSearchEngineNoDataException e) {
                SearchNewsResult = new Result(Status.OK, e.getMessage());
            } catch (EwandianSearchEngineException e) {
                logger.error(e.getStackTrace().toString());
                SearchNewsResult = new Result(Status.ERROR, e.getMessage());
            }
        }
        return SearchNewsResult;
    }

    @RequestMapping(value = "/search/news/correlation", method = RequestMethod.POST, produces="application/json")
    public Result searchNewsCorrelation(@RequestBody SearchKeyWord skw) {
        Result SearchNewsCorrelationResult;
        if(StringUtils.isEmpty(skw.getSearchArg())) {
            SearchNewsCorrelationResult = new Result(Status.OK, null, null);
        }else {
            try {
                List<NewsCorrelationEntity> NewsEntityList = newsCorrelationService.findNewsCorrelation(skw);
                long total = newsCorrelationService.getCount(skw.getSearchArg());
                long totalPage = (total==0||skw.getPageInfo().getPageSize()==1||skw.getPageInfo().getPageSize()==total)?total/skw.getPageInfo().getPageSize():total/skw.getPageInfo().getPageSize() + 1;
                long page = skw.getPageInfo().getPageNumber();
                long pageCount = skw.getPageInfo().getPageSize();
                SearchNewsCorrelationResult = new Result(Status.OK, "", NewsEntityList, total, totalPage, page, pageCount);
            } catch (EwandianSearchEngineNoDataException e) {
                SearchNewsCorrelationResult = new Result(Status.OK, e.getMessage());
            } catch (EwandianSearchEngineException e) {
                logger.error(e.getStackTrace().toString());
                SearchNewsCorrelationResult = new Result(Status.ERROR, e.getMessage());
            }
        }
        return SearchNewsCorrelationResult;
    }

    @RequestMapping(value = "/search/news/list", method = RequestMethod.POST, produces="application/json")
    public Result searchNewsByNewsCorrelationIdList(@RequestBody SearchKeyWord skw) {
        Result result;
        try {
            List<NewsCorrelationEntity> resultNewsEntityList = newsCorrelationService.findNewsByCorrelationIdNewsList(skw);
            long total = newsCorrelationService.findNewsByCorrelationIdNewsListCount(skw);
            long totalPage = (total==0||skw.getPageInfo().getPageSize()==1||skw.getPageInfo().getPageSize()==total)?total/skw.getPageInfo().getPageSize():total/skw.getPageInfo().getPageSize() + 1;
            long page = skw.getPageInfo().getPageNumber();
            long pageCount = skw.getPageInfo().getPageSize();
            result = new Result(Status.OK, "", resultNewsEntityList, total, totalPage, page, pageCount);

        } catch (EwandianSearchEngineNoDataException e) {
            result = new Result(Status.OK, e.getMessage());
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getStackTrace().toString());
            result = new Result(Status.ERROR, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/search/news/list/columnidentify", method = RequestMethod.POST, produces="application/json")
    public Result searchNewsByNewsColumnIdentifyList(@RequestBody SearchKeyWord skw) {
        Result result;
        try {
            List<NewsEntity> resultNewsEntityList = newsService.findNewsByColumnIdentifyList(skw);
            long total = newsService.findNewsByColumnIdentifyListCount(skw);
            long totalPage = (total==0||skw.getPageInfo().getPageSize()==1||skw.getPageInfo().getPageSize()==total)?total/skw.getPageInfo().getPageSize():total/skw.getPageInfo().getPageSize() + 1;
            long page = skw.getPageInfo().getPageNumber();
            long pageCount = skw.getPageInfo().getPageSize();
            result = new Result(Status.OK, "", resultNewsEntityList, total, totalPage, page, pageCount);

        } catch (EwandianSearchEngineNoDataException e) {
            result = new Result(Status.OK, e.getMessage());
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getStackTrace().toString());
            result = new Result(Status.ERROR, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/search/news/doubleword", method = RequestMethod.POST, produces="application/json")
    public Result searchNewsByDoubleKeyword(@RequestBody SearchKeyWord skw) {
        Result result;
        try {
            List<NewsEntity> resultNewsEntityList = newsService.findByKeywordAndColumnIdentify(skw);
            long total = newsService.findByKeywordAndColumnIdentifyCount(skw);
            long totalPage = (total==0||skw.getPageInfo().getPageSize()==1||skw.getPageInfo().getPageSize()==total)?total/skw.getPageInfo().getPageSize():total/skw.getPageInfo().getPageSize() + 1;
            long page = skw.getPageInfo().getPageNumber();
            long pageCount = skw.getPageInfo().getPageSize();
            result = new Result(Status.OK, "", resultNewsEntityList, total, totalPage, page, pageCount);

        } catch (EwandianSearchEngineNoDataException e) {
            result = new Result(Status.OK, e.getMessage());
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getStackTrace().toString());
            result = new Result(Status.ERROR, e.getMessage());
        }
        return result;
    }


    @RequestMapping(value = "/search/news/all/columnidentify", method = RequestMethod.POST, produces="application/json")
    public Result searchAllNewsColumnIdentify(@RequestBody SearchKeyWord skw) {
        Result SearchAllNewsResult;
            try {
                List<NewsEntity> AllNewsEntityList = newsService.findAll(skw.getPageInfo());
                long total = newsService.getAllCount();
                long totalPage = (total==0||skw.getPageInfo().getPageSize()==1||skw.getPageInfo().getPageSize()==total)?total/skw.getPageInfo().getPageSize():total/skw.getPageInfo().getPageSize() + 1;
                long page = skw.getPageInfo().getPageNumber();
                long pageCount = skw.getPageInfo().getPageSize();
                SearchAllNewsResult = new Result(Status.OK, "", AllNewsEntityList, total, totalPage, page, pageCount);
            } catch (EwandianSearchEngineNoDataException e) {
                SearchAllNewsResult = new Result(Status.OK, e.getMessage());
            } catch (EwandianSearchEngineException e) {
                logger.error(e.getStackTrace().toString());
                SearchAllNewsResult = new Result(Status.ERROR, e.getMessage());
            }
        return SearchAllNewsResult;
    }

    @RequestMapping(value = "/search/news/all/correlation", method = RequestMethod.POST, produces="application/json")
    public Result searchAllNews(@RequestBody SearchKeyWord skw) {
        Result SearchAllNewsResult;
        try {
            List<NewsCorrelationEntity> AllNewsEntityList = newsCorrelationService.findAll(skw.getPageInfo());
            long total = newsCorrelationService.getAllCount();
            long totalPage = (total==0||skw.getPageInfo().getPageSize()==1||skw.getPageInfo().getPageSize()==total)?total/skw.getPageInfo().getPageSize():total/skw.getPageInfo().getPageSize() + 1;
            long page = skw.getPageInfo().getPageNumber();
            long pageCount = skw.getPageInfo().getPageSize();
            SearchAllNewsResult = new Result(Status.OK, "", AllNewsEntityList, total, totalPage, page, pageCount);
        } catch (EwandianSearchEngineNoDataException e) {
            SearchAllNewsResult = new Result(Status.OK, e.getMessage());
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getStackTrace().toString());
            SearchAllNewsResult = new Result(Status.ERROR, e.getMessage());
        }
        return SearchAllNewsResult;
    }

    @RequestMapping(value = "/search/news/list/aggregation", method = RequestMethod.POST, produces="application/json")
    public Result searchNewsAggregationList(@RequestBody SearchKeyWord skw) {
        Result result;
        try {
            List<NewsEntity> resultNewsEntityList = newsService.findNewsByAggregationList(skw);
            //System.out.println("resultNewsEntityList.size(): " + resultNewsEntityList.size());
            long total = newsService.findNewsByAggregationListCount(skw);
            //long totalPage = (total==0||skw.getPageInfo().getPageSize()==1||skw.getPageInfo().getPageSize()==total)?total/skw.getPageInfo().getPageSize():total/skw.getPageInfo().getPageSize() + 1;
            long page = skw.getPageInfo().getPageNumber();
            long pageCount = skw.getPageInfo().getPageSize();
            long totalPage = total/pageCount + 1;
            result = new Result(Status.OK, "", resultNewsEntityList, total, totalPage, page, pageCount);

        } catch (EwandianSearchEngineNoDataException e) {
            result = new Result(Status.OK, e.getMessage());
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getStackTrace().toString());
            result = new Result(Status.ERROR, e.getMessage());
        }
        return result;
    }
}
