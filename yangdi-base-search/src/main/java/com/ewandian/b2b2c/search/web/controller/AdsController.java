package com.ewandian.b2b2c.search.web.controller;

import com.ewandian.b2b2c.search.app.constant.Status;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineNoDataException;
import com.ewandian.b2b2c.search.domain.document.AdsEntity;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;
import com.ewandian.b2b2c.search.domain.result.Result;
import com.ewandian.b2b2c.search.service.IAdsService;
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
public class AdsController {

    @Autowired
    private IAdsService adsService;

    private Logger logger = LoggerFactory.getLogger(AdsController.class);

    @RequestMapping(value = "/search/ad", method = RequestMethod.POST, produces="application/json")
    public Result searchAds(@RequestBody SearchKeyWord skw) {
        Result SearchAdsResult;
        if(StringUtils.isEmpty(skw.getSearchArg())) {
            SearchAdsResult = new Result(Status.OK, null, null);
        }else {
            try {
                List<AdsEntity> AdsEntityList = adsService.findAds(skw);
                long total = adsService.getCount(skw.getSearchArg());
                long totalPage = (total==0||skw.getPageInfo().getPageSize()==1||skw.getPageInfo().getPageSize()==total)?total/skw.getPageInfo().getPageSize():total/skw.getPageInfo().getPageSize() + 1;
                long page = skw.getPageInfo().getPageNumber();
                long pageCount = skw.getPageInfo().getPageSize();
                SearchAdsResult = new Result(Status.OK, "", AdsEntityList, total, totalPage, page, pageCount);
            } catch (EwandianSearchEngineNoDataException e) {
                SearchAdsResult = new Result(Status.OK, e.getMessage());
            } catch (EwandianSearchEngineException e) {
                logger.error(e.getStackTrace().toString());
                SearchAdsResult = new Result(Status.ERROR, e.getMessage());
            }
        }
        return SearchAdsResult;
    }

    @RequestMapping(value = "/search/ad/list", method = RequestMethod.POST, produces="application/json")
    public Result searchAdsByNewsList(@RequestBody SearchKeyWord skw) {
        Result result;
        try {
            List<AdsEntity> resultAdsEntityList = adsService.findAdsByAdsList(skw);
            long total = adsService.findAdsByAdsListCount(skw);
            long totalPage = (total==0||skw.getPageInfo().getPageSize()==1||skw.getPageInfo().getPageSize()==total)?total/skw.getPageInfo().getPageSize():total/skw.getPageInfo().getPageSize() + 1;
            long page = skw.getPageInfo().getPageNumber();
            long pageCount = skw.getPageInfo().getPageSize();
            result = new Result(Status.OK, "", resultAdsEntityList, total, totalPage, page, pageCount);
        } catch (EwandianSearchEngineNoDataException e) {
            result = new Result(Status.OK, e.getMessage());
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getStackTrace().toString());
            result = new Result(Status.ERROR, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/search/ad/ColumnIdentifyAndShopIdList", method = RequestMethod.POST, produces="application/json")
    public Result searchAdsByColumnIdentifyAndShopIdList(@RequestBody SearchKeyWord skw) {
        Result result;
        if(StringUtils.isEmpty(skw.getAdsIdList()) || StringUtils.isEmpty(skw.getSearchArg())) {
            result = new Result(Status.OK, "no search adsIdList or SearchArg!", null);
        }else {
            try {
                List <AdsEntity> resultAdsEntity = adsService.findByColumnIdentifyAndShopIdList(skw);
                long total = adsService.findByColumnIdentifyAndShopIdListCount(skw);
                long totalPage = (total==0||skw.getPageInfo().getPageSize()==1||skw.getPageInfo().getPageSize()==total)?total/skw.getPageInfo().getPageSize():total/skw.getPageInfo().getPageSize() + 1;
                long page = skw.getPageInfo().getPageNumber();
                long pageCount = skw.getPageInfo().getPageSize();
                result = new Result(Status.OK, "", resultAdsEntity, total, totalPage, page, pageCount);
            } catch (EwandianSearchEngineNoDataException e) {
                result = new Result(Status.OK, e.getMessage());
            } catch (EwandianSearchEngineException e) {
                logger.error(e.getStackTrace().toString());
                result = new Result(Status.ERROR, e.getMessage());
            }
        }
        return result;
    }

    @RequestMapping(value = "/search/ad/all", method = RequestMethod.POST, produces="application/json")
    public Result searchAllAds(@RequestBody SearchKeyWord skw) {
        Result SearchAllAdsResult;
            try {
                List<AdsEntity> AllAdsEntityList = adsService.findAll(skw.getPageInfo());
                long total = adsService.getAllCount();
                long totalPage = (total==0||skw.getPageInfo().getPageSize()==1||skw.getPageInfo().getPageSize()==total)?total/skw.getPageInfo().getPageSize():total/skw.getPageInfo().getPageSize() + 1;
                long page = skw.getPageInfo().getPageNumber();
                long pageCount = skw.getPageInfo().getPageSize();
                SearchAllAdsResult = new Result(Status.OK, "", AllAdsEntityList, total, totalPage, page, pageCount);
            } catch (EwandianSearchEngineNoDataException e) {
                SearchAllAdsResult = new Result(Status.OK, e.getMessage());
            } catch (EwandianSearchEngineException e) {
                logger.error(e.getStackTrace().toString());
                SearchAllAdsResult = new Result(Status.ERROR, e.getMessage());
            }
        return SearchAllAdsResult;
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST, produces="application/json")
    public Result test(@RequestBody SearchKeyWord skw) {
        Result SearchAdsResult;
        if(StringUtils.isEmpty(skw.getSearchArg())) {
            SearchAdsResult = new Result(Status.OK, null, null);
        }else {
            try {
                List<AdsEntity> AdsEntityList = adsService.test(skw);
                SearchAdsResult = new Result(Status.OK, "", AdsEntityList, AdsEntityList.size());
            } catch (EwandianSearchEngineNoDataException e) {
                SearchAdsResult = new Result(Status.OK, e.getMessage());
            } catch (EwandianSearchEngineException e) {
                logger.error(e.getStackTrace().toString());
                SearchAdsResult = new Result(Status.ERROR, e.getMessage());
            }
        }
        return SearchAdsResult;
    }

}