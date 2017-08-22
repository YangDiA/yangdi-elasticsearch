package com.ewandian.b2b2c.search.web.controller;

import com.ewandian.b2b2c.search.app.constant.Status;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineNoDataException;
import com.ewandian.b2b2c.search.domain.document.HelpCenterEntity;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;
import com.ewandian.b2b2c.search.domain.result.Result;
import com.ewandian.b2b2c.search.service.IHelpCenterService;
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
 * Created by Administrator on 2016/12/9.
 */
@RestController
@Scope("prototype")
public class HelpCenterController {

    @Autowired
    private IHelpCenterService helpCenterService;

    private Logger logger = LoggerFactory.getLogger(HelpCenterController.class);

    @RequestMapping(value = "/search/help", method = RequestMethod.POST, produces="application/json")
    public Result searchHelps(@RequestBody SearchKeyWord skw) {
        Result SearchHelpsResult;
        if(StringUtils.isEmpty(skw.getSearchArg())) {
            SearchHelpsResult = new Result(Status.OK, null, null);
        }else {
            try {
                List<HelpCenterEntity> HelpsEntityList = helpCenterService.findHelps(skw);
                long total = helpCenterService.getCount(skw.getSearchArg());
                long totalPage = (total==0||skw.getPageInfo().getPageSize()==1||skw.getPageInfo().getPageSize()==total)?total/skw.getPageInfo().getPageSize():total/skw.getPageInfo().getPageSize() + 1;
                long page = skw.getPageInfo().getPageNumber();
                long pageCount = skw.getPageInfo().getPageSize();
                SearchHelpsResult = new Result(Status.OK, "", HelpsEntityList, total, totalPage, page, pageCount);
            } catch (EwandianSearchEngineNoDataException e) {
                SearchHelpsResult = new Result(Status.OK, e.getMessage());
            } catch (EwandianSearchEngineException e) {
                logger.error(e.getStackTrace().toString());
                SearchHelpsResult = new Result(Status.ERROR, e.getMessage());
            }
        }
        return SearchHelpsResult;
    }

    @RequestMapping(value = "/search/help/list", method = RequestMethod.POST, produces="application/json")
    public Result searchHelpsByHelpsList(@RequestBody SearchKeyWord skw) {
        Result result;
        try {
            List<HelpCenterEntity> resultHelpsEntityList = helpCenterService.findHelpsByHelpsList(skw);
            long total = helpCenterService.findHelpsByColumnIdentifyListCount(skw);
            long totalPage = (total==0||skw.getPageInfo().getPageSize()==1||skw.getPageInfo().getPageSize()==total)?total/skw.getPageInfo().getPageSize():total/skw.getPageInfo().getPageSize() + 1;
            long page = skw.getPageInfo().getPageNumber();
            long pageCount = skw.getPageInfo().getPageSize();
            result = new Result(Status.OK, "", resultHelpsEntityList, total, totalPage, page, pageCount);

        } catch (EwandianSearchEngineNoDataException e) {
            result = new Result(Status.OK, e.getMessage());
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getStackTrace().toString());
            result = new Result(Status.ERROR, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/search/help/all", method = RequestMethod.POST, produces="application/json")
    public Result searchAllAds(@RequestBody SearchKeyWord skw) {
        Result SearchAllHelpsResult;
        try {
            List<HelpCenterEntity> helpsEntityList = helpCenterService.findAll(skw.getPageInfo());
            long total = helpCenterService.getAllCount();
            long totalPage = (total==0||skw.getPageInfo().getPageSize()==1||skw.getPageInfo().getPageSize()==total)?total/skw.getPageInfo().getPageSize():total/skw.getPageInfo().getPageSize() + 1;
            long page = skw.getPageInfo().getPageNumber();
            long pageCount = skw.getPageInfo().getPageSize();
            SearchAllHelpsResult = new Result(Status.OK, "", helpsEntityList, total, totalPage, page, pageCount);
        } catch (EwandianSearchEngineNoDataException e) {
            SearchAllHelpsResult = new Result(Status.OK, e.getMessage());
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getStackTrace().toString());
            SearchAllHelpsResult = new Result(Status.ERROR, e.getMessage());
        }
        return SearchAllHelpsResult;
    }
}