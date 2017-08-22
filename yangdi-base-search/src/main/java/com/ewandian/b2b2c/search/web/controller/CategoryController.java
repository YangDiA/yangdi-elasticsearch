package com.ewandian.b2b2c.search.web.controller;

import com.ewandian.b2b2c.search.app.constant.Status;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineNoDataException;
import com.ewandian.b2b2c.search.domain.document.CategoryEntity;
import com.ewandian.b2b2c.search.domain.receive.CategoryPageInfo;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;
import com.ewandian.b2b2c.search.domain.result.Result;
import com.ewandian.b2b2c.search.service.ICategoryService;
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
 * Created by suhd on 2016-12-09.
 */
@RestController
@Scope("prototype")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    private Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @RequestMapping(value = "/search/categorySiblingsPageByCategoryId", method = RequestMethod.POST, produces="application/json")
    public Result searchCategorySiblingsPageByCategoryId(@RequestBody CategoryPageInfo categoryPageInfo) {
        Result result;
        if(categoryPageInfo.getCategoryEntity()==null || StringUtil.isNullOrEmpty(categoryPageInfo.getCategoryEntity().getCategoryId())) {
            result = new Result(Status.ERROR, "参数传入错误：categoryId不允许为空！");
        } else {
            try {
                List<CategoryEntity> categoryEntityList = categoryService.getSibling(categoryPageInfo.getCategoryEntity().getCategoryId(),categoryPageInfo.getPageInfo());
                long total = categoryService.getSiblingCount(categoryPageInfo.getCategoryEntity().getCategoryId(),categoryPageInfo.getPageInfo());
                long totalPage = (total==0||categoryPageInfo.getPageInfo().getPageSize()==1||categoryPageInfo.getPageInfo().getPageSize()==total)?total/categoryPageInfo.getPageInfo().getPageSize():total/categoryPageInfo.getPageInfo().getPageSize() + 1;
                long page = categoryPageInfo.getPageInfo().getPageNumber();
                long pageCount = categoryPageInfo.getPageInfo().getPageSize();
                result = new Result(Status.OK, "", categoryEntityList, total, totalPage, page, pageCount);
            } catch (EwandianSearchEngineNoDataException e) {
                result = new Result(Status.OK, e.getMessage());
            } catch (EwandianSearchEngineException e) {
                logger.error(e.getStackTrace().toString());
                result = new Result(Status.ERROR, e.getMessage());
            }
        }
        return result;
    }

    @RequestMapping(value = "/search/categories/all", method = RequestMethod.POST, produces="application/json")
    public Result searchAllCategories(@RequestBody SearchKeyWord skw) {
        Result result;
        try {
            List<CategoryEntity> categoryEntityList = categoryService.findAll(skw.getPageInfo());
            result = new Result(Status.OK, "", categoryEntityList);
        } catch (EwandianSearchEngineNoDataException e) {
            result = new Result(Status.OK, e.getMessage());
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getStackTrace().toString());
            result = new Result(Status.ERROR, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/search/categoriesByShopIdIncludingParent", method = RequestMethod.POST, produces="application/json")
    public Result searchCategoriesByShopId(@RequestBody SearchKeyWord skw) {
        Result result;
        try {
            List<CategoryEntity> categoryEntityList = categoryService.findCategoriesByShopId(skw);
            result = new Result(Status.OK, "", categoryEntityList);
        } catch (EwandianSearchEngineNoDataException e) {
            result = new Result(Status.OK, e.getMessage());
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getStackTrace().toString());
            result = new Result(Status.ERROR, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/search/categoriesOnlyLevelZeroAndOne", method = RequestMethod.POST, produces="application/json")
    public Result searchCategoriesOnlyLevelZeroAndOne(@RequestBody SearchKeyWord skw) {
        Result result;
        try {
            List<CategoryEntity> categoryEntityList = categoryService.CategoriesOnlyLevelZeroAndOne();
            result = new Result(Status.OK, "", categoryEntityList);
        } catch (EwandianSearchEngineNoDataException e) {
            result = new Result(Status.OK, e.getMessage());
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getStackTrace().toString());
            result = new Result(Status.ERROR, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/search/parentCategoryByCategoryId", method = RequestMethod.POST, produces="application/json")
    public Result searchParentCategoryByCategoryId(@RequestBody SearchKeyWord skw) {
        Result result;
        if(StringUtil.isNullOrEmpty(skw.getSearchArg())) {
            result = new Result(Status.ERROR, "参数传入错误：categoryId不允许为空！");
        } else {
            try {
                CategoryEntity parentCategoryEntity = categoryService.findParentCategoryByCategoryId(skw.getSearchArg());
                result = new Result(Status.OK, "", parentCategoryEntity);
            } catch (EwandianSearchEngineNoDataException e) {
                result = new Result(Status.OK, e.getMessage());
            } catch (EwandianSearchEngineException e) {
                logger.error(e.getStackTrace().toString());
                result = new Result(Status.ERROR, e.getMessage());
            }
        }
        return result;
    }

    @RequestMapping(value = "/search/fullParentCategoriesListByCategoryId", method = RequestMethod.POST, produces="application/json")
    public Result searchFullParentCategoriesListByCategoryId(@RequestBody SearchKeyWord skw) {
        Result result;
        if(StringUtil.isNullOrEmpty(skw.getSearchArg())) {
            result = new Result(Status.ERROR, "参数传入错误：categoryId不允许为空！");
        } else {
            try {
                List<CategoryEntity> parentCategoriesEntityList = categoryService.findFullParentCategoriesListByCategoryId(skw);
                result = new Result(Status.OK, "", parentCategoriesEntityList);
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
