package com.ewandian.b2b2c.search.web.controller;

import com.ewandian.b2b2c.search.app.constant.Status;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.keyword.ReturnKeyWord;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;
import com.ewandian.b2b2c.search.domain.result.Result;
import com.ewandian.b2b2c.search.service.IRelatedKeyWordService;
import com.ewandian.platform.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by suhd on 2016-11-30.
 */
@RestController
@Scope("prototype")
public class RelatedKeyWordController {

    @Autowired
    private IRelatedKeyWordService relatedKeyWordService;

    private Logger logger = LoggerFactory.getLogger(RelatedKeyWordController.class);

    @RequestMapping(value="/search/onChangingEvent", method=RequestMethod.POST, produces="application/json")
    public Result onChangingEvent(@RequestBody SearchKeyWord skw) {
        Result result;
        if(StringUtil.isNullOrEmpty(skw.getSearchArg())
                || StringUtil.isNullOrEmpty(skw.getAreaName())
                || StringUtil.isNullOrEmpty(skw.getCoordinateX())
                || StringUtil.isNullOrEmpty(skw.getCoordinateY()) ) {
            result = new Result(Status.OK, "请传入searchArg、areaName、coordinateX、coordinateY参数", null);
        }else {
            try {
                ReturnKeyWord returnKeyWord = relatedKeyWordService.findWithAggCount(skw);
                result = new Result(Status.OK,"",returnKeyWord);
            } catch (EwandianSearchEngineException e) {
                logger.error(e.getStackTrace().toString());
                result = new Result(Status.ERROR, e.getMessage());
            }
        }
        return result;
    }


}
