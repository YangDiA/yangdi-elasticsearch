package com.ewandian.b2b2c.search.web.controller;

import com.ewandian.b2b2c.search.app.constant.Status;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineNoDataException;
import com.ewandian.b2b2c.search.domain.document.ShopOverAllRatingEntity;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;
import com.ewandian.b2b2c.search.domain.result.Result;
import com.ewandian.b2b2c.search.service.IShopOverAllRatingService;
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
 * Created by suhd on 2016-12-14.
 */
@RestController
@Scope("prototype")
public class ShopOverAllRatingController {
    @Autowired
    private IShopOverAllRatingService shopOverAllRatingService;

    private Logger logger = LoggerFactory.getLogger(ShopOverAllRatingController.class);

    @RequestMapping(value = "/search/shopOverAllRatingByShopId", method = RequestMethod.POST, produces="application/json")
    public Result searchShopOverAllRatingByShopId(@RequestBody SearchKeyWord skw) {
        Result result;
        if(StringUtil.isNullOrEmpty(skw.getSearchArg())) {
            result = new Result(Status.OK, "请传入searchArg参数", null);
        }else {
            try {
                ShopOverAllRatingEntity shopOverAllRatingEntity = shopOverAllRatingService.findOneByShopId(skw);
                result = new Result(Status.OK, "", shopOverAllRatingEntity);
            } catch (EwandianSearchEngineNoDataException e) {
                result = new Result(Status.OK, e.getMessage());
            } catch (EwandianSearchEngineException e) {
                logger.error(e.getStackTrace().toString());
                result = new Result(Status.ERROR, e.getMessage());
            }
        }
        return result;
    }
}
