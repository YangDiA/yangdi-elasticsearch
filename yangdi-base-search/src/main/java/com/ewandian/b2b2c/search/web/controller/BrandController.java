package com.ewandian.b2b2c.search.web.controller;

import com.ewandian.b2b2c.search.app.constant.Status;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineNoDataException;
import com.ewandian.b2b2c.search.domain.document.BrandEntity;
import com.ewandian.b2b2c.search.domain.document.GoodsEntity;
import com.ewandian.b2b2c.search.domain.receive.BrandPageInfo;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;
import com.ewandian.b2b2c.search.domain.result.Result;
import com.ewandian.b2b2c.search.service.IBrandService;
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
 * Created by suhd on 2016-12-08.
 */
@RestController
@Scope("prototype")
public class BrandController {
    @Autowired
    private IBrandService brandService;

    private Logger logger = LoggerFactory.getLogger(BrandController.class);

    @RequestMapping(value = "/search/brandSiblingsPageByBrandIdAndCategoryId", method = RequestMethod.POST, produces="application/json")
    public Result searchBrandSiblingsPageByBrandIdAndCategoryId(@RequestBody BrandPageInfo brandPageInfo) {
        Result result;
        if(brandPageInfo.getBrandEntity()==null || StringUtil.isNullOrEmpty(brandPageInfo.getBrandEntity().getBrandId()) || StringUtil.isNullOrEmpty(brandPageInfo.getCategoryId())) {
            result = new Result(Status.ERROR, "参数传入错误：brandId与categoryId不允许为空！");
        }
        try {
            List<BrandEntity> brandEntityList = brandService.getSibling(brandPageInfo.getBrandEntity().getBrandId(),brandPageInfo.getCategoryId(),brandPageInfo.getPageInfo());
            long total = brandService.getSiblingCount(brandPageInfo.getBrandEntity().getBrandId(),brandPageInfo.getCategoryId(),brandPageInfo.getPageInfo());
            long totalPage = (total==0||brandPageInfo.getPageInfo().getPageSize()==1||brandPageInfo.getPageInfo().getPageSize()==total)?total/brandPageInfo.getPageInfo().getPageSize():total/brandPageInfo.getPageInfo().getPageSize() + 1;;
            long page = brandPageInfo.getPageInfo().getPageNumber();
            long pageCount = brandPageInfo.getPageInfo().getPageSize();
            result = new Result(Status.OK, "", brandEntityList, total, totalPage, page, pageCount);
        } catch (EwandianSearchEngineNoDataException e) {
            result = new Result(Status.OK, e.getMessage());
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getStackTrace().toString());
            result = new Result(Status.ERROR, e.getMessage());
        }
        return result;
    }

    @Deprecated
    @RequestMapping(value = "/search/brandsByShopId", method = RequestMethod.POST, produces="application/json")
    public Result searchBrandsByShopId(@RequestBody SearchKeyWord skw) {
        Result result;
        try {
            List<BrandEntity> brandEntityList = brandService.findBrandsByShopId(skw);
            result = new Result(Status.OK, "", brandEntityList);
        } catch (EwandianSearchEngineNoDataException e) {
            result = new Result(Status.OK, e.getMessage());
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getStackTrace().toString());
            result = new Result(Status.ERROR, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/search/brandsPageByShopId", method = RequestMethod.POST, produces="application/json")
    public Result searchBrandsPageByShopId(@RequestBody SearchKeyWord skw) {
        Result result;
        try {
            List<BrandEntity> brandEntityList = brandService.findBrandsPageByShopId(skw);
            long total = brandService.findBrandsPageByShopIdCount(skw);
            long totalPage = (total==0||skw.getPageInfo().getPageSize()==1||skw.getPageInfo().getPageSize()==total)?total/skw.getPageInfo().getPageSize():total/skw.getPageInfo().getPageSize() + 1;;
            long page = skw.getPageInfo().getPageNumber();
            long pageCount = skw.getPageInfo().getPageSize();
            result = new Result(Status.OK, "", brandEntityList, total, totalPage, page, pageCount);
        } catch (EwandianSearchEngineNoDataException e) {
            result = new Result(Status.OK, e.getMessage());
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getStackTrace().toString());
            result = new Result(Status.ERROR, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/search/brandsPageBySearchArg", method = RequestMethod.POST, produces="application/json")
    public Result searchBrandsPageBySearchArg(@RequestBody SearchKeyWord skw) {
        Result result;
        try {
            List<BrandEntity> brandEntityList = brandService.findBrandsPageBySearchArg(skw);
            long total = brandService.findBrandsPageBySearchArgCount(skw);
            long totalPage = (total==0||skw.getPageInfo().getPageSize()==1||skw.getPageInfo().getPageSize()==total)?total/skw.getPageInfo().getPageSize():total/skw.getPageInfo().getPageSize() + 1;;
            long page = skw.getPageInfo().getPageNumber();
            long pageCount = skw.getPageInfo().getPageSize();
            result = new Result(Status.OK, "", brandEntityList, total, totalPage, page, pageCount);
        } catch (EwandianSearchEngineNoDataException e) {
            result = new Result(Status.OK, e.getMessage());
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getStackTrace().toString());
            result = new Result(Status.ERROR, e.getMessage());
        }
        return result;
    }
}
