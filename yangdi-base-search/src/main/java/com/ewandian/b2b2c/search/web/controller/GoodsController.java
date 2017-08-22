package com.ewandian.b2b2c.search.web.controller;

import com.ewandian.b2b2c.search.app.constant.Status;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineNoDataException;
import com.ewandian.b2b2c.search.domain.document.GoodsEntity;
import com.ewandian.b2b2c.search.domain.document.HotspotEntity;
import com.ewandian.b2b2c.search.domain.keyword.ReturnKeyWord;
import com.ewandian.b2b2c.search.domain.receive.GoodsPageInfo;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;
import com.ewandian.b2b2c.search.domain.result.Result;
import com.ewandian.b2b2c.search.service.IGoodsService;
import com.ewandian.b2b2c.search.service.IHotspotService;
import com.ewandian.b2b2c.search.service.IRelatedKeyWordService;
import com.ewandian.b2b2c.search.service.multithread.AddInfoToHotspot;
import com.ewandian.platform.util.StringUtil;
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
import java.util.concurrent.Executors;

/**
 * Created by suhd on 2016-11-30.
 */
@RestController
@Scope("prototype")
public class GoodsController {

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private AddInfoToHotspot addInfoToHotspot;

    private Logger logger = LoggerFactory.getLogger(RelatedKeyWordController.class);

    @RequestMapping(value = "/search/goods", method = RequestMethod.POST, produces="application/json")
    public Result search(@RequestBody SearchKeyWord skw) {
        Result result;
        try {
            List<GoodsEntity> goodsEntityList = goodsService.findGoods(skw);
            if(StringUtil.isNotNullAndNotEmpty(skw.getSearchArg()) && goodsEntityList!=null && goodsEntityList.size()>0) {
                //Asynchronously add hotspot. In order to promote performance slightly inaccuracy is allowed.
                Executors.newCachedThreadPool().execute(addInfoToHotspot.setSKW(skw));
            }
            long total = goodsService.getCount(skw);
            long totalPage = (total==0||skw.getPageInfo().getPageSize()==1||skw.getPageInfo().getPageSize()==total)?total/skw.getPageInfo().getPageSize():total/skw.getPageInfo().getPageSize() + 1;
            long page = skw.getPageInfo().getPageNumber();
            long pageCount = skw.getPageInfo().getPageSize();
            result = new Result(Status.OK, "", goodsEntityList, total, totalPage, page, pageCount);
        } catch (EwandianSearchEngineNoDataException e) {
            result = new Result(Status.OK, e.getMessage());
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getStackTrace().toString());
            result = new Result(Status.ERROR, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/search/goodByGoodsId", method = RequestMethod.POST, produces="application/json")
    public Result search(@RequestBody GoodsPageInfo goodsPageInfo) {
        Result result;
        if(goodsPageInfo.getGoodsEntity()==null ||StringUtil.isNullOrEmpty(goodsPageInfo.getGoodsEntity().getGoodsId())) {
            result = new Result(Status.OK, "请传入goodsId参数", null);
        }else {
            try {
                GoodsEntity goodsEntity = goodsService.findOneByGoodsId(goodsPageInfo.getGoodsEntity());
                result = new Result(Status.OK, "", goodsEntity);
            } catch (EwandianSearchEngineNoDataException e) {
                result = new Result(Status.OK, e.getMessage());
            } catch (EwandianSearchEngineException e) {
                logger.error(e.getStackTrace().toString());
                result = new Result(Status.ERROR, e.getMessage());
            }
        }
        return result;
    }

    @RequestMapping(value = "/search/goods/all", method = RequestMethod.POST, produces="application/json")
    public Result searchAllGoods(@RequestBody SearchKeyWord skw) {
        Result result;
        result = findAllGoods(skw);
        return result;
    }

    private Result findAllGoods(@RequestBody SearchKeyWord skw) {
        Result result;
        try {
            List<GoodsEntity> goodsEntityList = goodsService.findAll(skw.getPageInfo());
            long total = goodsService.getAllCount();
            long totalPage = (total==0||skw.getPageInfo().getPageSize()==1||skw.getPageInfo().getPageSize()==total)?total/skw.getPageInfo().getPageSize():total/skw.getPageInfo().getPageSize() + 1;;
            long page = skw.getPageInfo().getPageNumber();
            long pageCount = skw.getPageInfo().getPageSize();
            result = new Result(Status.OK, "", goodsEntityList, total, totalPage, page, pageCount);
        } catch (EwandianSearchEngineNoDataException e) {
            result = new Result(Status.OK, e.getMessage());
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getStackTrace().toString());
            result = new Result(Status.ERROR, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/search/goodsPageByGoodsEntity", method = RequestMethod.POST, produces="application/json")
    public Result searchGoodsPageByGoodsEntity(@RequestBody GoodsPageInfo goodsPageInfo) {
        Result result;
        try {
            List<GoodsEntity> goodsEntityList = goodsService.findPageListByGoodsEntity(goodsPageInfo);
            long total = goodsService.findListByGoodsEntityCount(goodsPageInfo);
            long totalPage = (total==0||goodsPageInfo.getPageInfo().getPageSize()==1||goodsPageInfo.getPageInfo().getPageSize()==total)?total/goodsPageInfo.getPageInfo().getPageSize():total/goodsPageInfo.getPageInfo().getPageSize() + 1;
            long page = goodsPageInfo.getPageInfo().getPageNumber();
            long pageCount = goodsPageInfo.getPageInfo().getPageSize();
            result = new Result(Status.OK, "", goodsEntityList, total, totalPage, page, pageCount);
        } catch (EwandianSearchEngineNoDataException e) {
            result = new Result(Status.OK, e.getMessage());
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getStackTrace().toString());
            result = new Result(Status.ERROR, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/search/goodsPageByGoodsIdList", method = RequestMethod.POST, produces="application/json")
    public Result searchGoodsPageByGoodsIdList(@RequestBody SearchKeyWord skw) {
        Result result;
        try {
            List<GoodsEntity> goodsEntityList = goodsService.findGoodsPageByGoodsIdList(skw);
            long total = goodsService.findGoodsPageByGoodsIdListCount(skw);
            long totalPage = (total==0||skw.getPageInfo().getPageSize()==1||skw.getPageInfo().getPageSize()==total)?total/skw.getPageInfo().getPageSize():total/skw.getPageInfo().getPageSize() + 1;
            long page = skw.getPageInfo().getPageNumber();
            long pageCount = skw.getPageInfo().getPageSize();
            result = new Result(Status.OK, "", goodsEntityList, total, totalPage, page, pageCount);
        } catch (EwandianSearchEngineNoDataException e) {
            result = new Result(Status.OK, e.getMessage());
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getStackTrace().toString());
            result = new Result(Status.ERROR, e.getMessage());
        }
        return result;
    }

    /**
     *
     * Please use searchGoodsPageByGoodsIdList(above) instead
     *
     * @param skw
     * @return
     */
    @Deprecated
    @RequestMapping(value = "/search/goodsByGoodsIdList", method = RequestMethod.POST, produces="application/json")
    public Result searchGoodsByGoodsIdList(@RequestBody SearchKeyWord skw) {
        Result result;
        try {
            List<GoodsEntity> goodsEntityList = goodsService.findGoodsPageByGoodsIdList(skw);
            result = new Result(Status.OK, "", goodsEntityList);
        } catch (EwandianSearchEngineNoDataException e) {
            result = new Result(Status.OK, e.getMessage());
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getStackTrace().toString());
            result = new Result(Status.ERROR, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/search/goodsPageByCategoryIdInPriceRange", method = RequestMethod.POST, produces="application/json")
    public Result searchGoodsPageByCategoryIdInPriceRange(@RequestBody GoodsPageInfo goodsPageInfo) {
        Result result;
        try {
            List<GoodsEntity> goodsEntityList = goodsService.findGoodsPageByCategoryIdInPriceRange(goodsPageInfo);
            long total = goodsService.findGoodsPageByCategoryIdInPriceRangeCount(goodsPageInfo);
            long totalPage = (total==0||goodsPageInfo.getPageInfo().getPageSize()==1||goodsPageInfo.getPageInfo().getPageSize()==total)?total/goodsPageInfo.getPageInfo().getPageSize():total/goodsPageInfo.getPageInfo().getPageSize() + 1;
            long page = goodsPageInfo.getPageInfo().getPageNumber();
            long pageCount = goodsPageInfo.getPageInfo().getPageSize();
            result = new Result(Status.OK, "", goodsEntityList, total, totalPage, page, pageCount);
        } catch (EwandianSearchEngineNoDataException e) {
            result = new Result(Status.OK, e.getMessage());
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getStackTrace().toString());
            result = new Result(Status.ERROR, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/search/goodsPageByBrandIdInPriceRange", method = RequestMethod.POST, produces="application/json")
    public Result searchGoodsPageByBrandIdInPriceRange(@RequestBody GoodsPageInfo goodsPageInfo) {
        Result result;
        try {
            List<GoodsEntity> goodsEntityList = goodsService.findGoodsPageByBrandIdInPriceRange(goodsPageInfo);
            long total = goodsService.findGoodsPageByBrandIdInPriceRangeCount(goodsPageInfo);
            long totalPage = (total==0||goodsPageInfo.getPageInfo().getPageSize()==1||goodsPageInfo.getPageInfo().getPageSize()==total)?total/goodsPageInfo.getPageInfo().getPageSize():total/goodsPageInfo.getPageInfo().getPageSize() + 1;
            long page = goodsPageInfo.getPageInfo().getPageNumber();
            long pageCount = goodsPageInfo.getPageInfo().getPageSize();
            result = new Result(Status.OK, "", goodsEntityList, total, totalPage, page, pageCount);
        } catch (EwandianSearchEngineNoDataException e) {
            result = new Result(Status.OK, e.getMessage());
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getStackTrace().toString());
            result = new Result(Status.ERROR, e.getMessage());
        }
        return result;
    }
}
