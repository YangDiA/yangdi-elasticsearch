package com.ewandian.b2b2c.search.web.controller;

import com.alibaba.fastjson.JSON;
import com.ewandian.b2b2c.search.app.constant.Status;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineNoDataException;
import com.ewandian.b2b2c.search.domain.document.ShopEntity;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;
import com.ewandian.b2b2c.search.domain.receive.ShopPageInfo;
import com.ewandian.b2b2c.search.domain.result.Result;
import com.ewandian.b2b2c.search.service.IShopService;
import com.ewandian.platform.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by suhd on 2016-12-02.
 */
@RestController
@Scope("prototype")
public class ShopController {
    @Autowired
    private IShopService shopService;

    private Logger logger = LoggerFactory.getLogger(ShopController.class);

    @RequestMapping(value = "/search/shopsByShopName", method = RequestMethod.POST, produces="application/json")
    public Result searchShops(@RequestBody SearchKeyWord skw) {
        Result result;
        if(StringUtil.isNullOrEmpty(skw.getSearchArg())) {
            result = new Result(Status.OK, "请传入searchArg参数", null);
        }else {
            try {
                List<ShopEntity> shopEntityList = shopService.findShopsByShopName(skw);
                long total = shopService.getCount(skw.getSearchArg());
                long totalPage = (total==0||skw.getPageInfo().getPageSize()==1||skw.getPageInfo().getPageSize()==total)?total/skw.getPageInfo().getPageSize():total/skw.getPageInfo().getPageSize() + 1;
                long page = skw.getPageInfo().getPageNumber();
                long pageCount = skw.getPageInfo().getPageSize();
                result = new Result(Status.OK, "", shopEntityList, total, totalPage, page, pageCount);
            } catch (EwandianSearchEngineNoDataException e) {
                result = new Result(Status.OK, e.getMessage());
            } catch (EwandianSearchEngineException e) {
                logger.error(e.getStackTrace().toString());
                result = new Result(Status.ERROR, e.getMessage());
            }
        }
        return result;
    }

    @RequestMapping(value = "/search/shopByShopId", method = RequestMethod.POST, produces="application/json")
    public Result searchOne(@RequestBody SearchKeyWord skw) {
        Result result;
        if(StringUtil.isNullOrEmpty(skw.getSearchArg())) {
            result = new Result(Status.OK, "请传入searchArg参数", null);
        }else {
            try {
                ShopEntity shopEntity = shopService.findOneByShopId(skw.getSearchArg());
                result = new Result(Status.OK, "", shopEntity);
            } catch (EwandianSearchEngineNoDataException e) {
                result = new Result(Status.OK, e.getMessage());
            } catch (EwandianSearchEngineException e) {
                logger.error(e.getStackTrace().toString());
                result = new Result(Status.ERROR, e.getMessage());
            }
        }
        return result;
    }

    @RequestMapping(value = "/search/shops/all", method = RequestMethod.POST, produces="application/json")
    public Result searchAllShops(@RequestBody SearchKeyWord skw) {
        Result result;
        result = findAllShops(skw);
        return result;
    }

    private Result findAllShops(@RequestBody SearchKeyWord skw) {
        Result result;
        try {
            List<ShopEntity> shopEntityList = shopService.findAll(skw.getPageInfo());
            long total = shopService.getAllCount();
            long totalPage = (total==0||skw.getPageInfo().getPageSize()==1||skw.getPageInfo().getPageSize()==total)?total/skw.getPageInfo().getPageSize():total/skw.getPageInfo().getPageSize() + 1;
            long page = skw.getPageInfo().getPageNumber();
            long pageCount = skw.getPageInfo().getPageSize();
            result = new Result(Status.OK, "", shopEntityList, total, totalPage, page, pageCount);
        } catch (EwandianSearchEngineNoDataException e) {
            result = new Result(Status.OK, e.getMessage());
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getStackTrace().toString());
            result = new Result(Status.ERROR, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/search/shops/shopsPageByShopEntity", method = RequestMethod.POST, produces="application/json")
    public Result searchShopsPageByShopEntity(@RequestBody ShopPageInfo shopPageInfo) {
        Result result;
        try {
            List<ShopEntity> shopEntityList = shopService.findShopsPageByShopEntity(shopPageInfo);
            long total = shopService.getFindShopsPageByShopEntityCount(shopPageInfo);
            long totalPage = (total==0||shopPageInfo.getPageInfo().getPageSize()==1||shopPageInfo.getPageInfo().getPageSize()==total)?total/shopPageInfo.getPageInfo().getPageSize():total/shopPageInfo.getPageInfo().getPageSize() + 1;
            long page = shopPageInfo.getPageInfo().getPageNumber();
            long pageCount = shopPageInfo.getPageInfo().getPageSize();
            result = new Result(Status.OK, "", shopEntityList, total, totalPage, page, pageCount);
        } catch (EwandianSearchEngineNoDataException e) {
            result = new Result(Status.OK, e.getMessage());
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getStackTrace().toString());
            result = new Result(Status.ERROR, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/search/shopsPageByShopIdList", method = RequestMethod.POST, produces="application/json")
    public Result searchShopsByShopIdList(@RequestBody SearchKeyWord skw) {
        Result result;
        if(skw.getShopIdList()==null || skw.getShopIdList().size()<=0) {
            result = new Result(Status.ERROR, "请传入shopIdList参数", null);
        }else {
            try {
                List<ShopEntity> shopEntityList = shopService.findShopsPageByShopIdList(skw);
                long total = shopService.getShopsPageByShopIdListCount(skw);
                long totalPage = (total==0||skw.getPageInfo().getPageSize()==1||skw.getPageInfo().getPageSize()==total)?total/skw.getPageInfo().getPageSize():total/skw.getPageInfo().getPageSize() + 1;
                long page = skw.getPageInfo().getPageNumber();
                long pageCount = skw.getPageInfo().getPageSize();
                result = new Result(Status.OK, "", shopEntityList, total, totalPage, page, pageCount);
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
