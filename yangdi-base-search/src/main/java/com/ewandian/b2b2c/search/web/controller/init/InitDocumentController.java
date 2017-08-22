package com.ewandian.b2b2c.search.web.controller.init;

import com.ewandian.b2b2c.search.app.constant.Status;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.*;
import com.ewandian.b2b2c.search.domain.result.Result;
import com.ewandian.b2b2c.search.service.*;
import com.ewandian.b2b2c.search.web.controller.RelatedKeyWordController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by suhd on 2016-12-05.
 */
@RestController
public class InitDocumentController {
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private IRelatedKeyWordService relatedKeyWordService;
    @Autowired
    private IShopService shopService;
    @Autowired
    private IBrandService brandService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IGoodsImageService goodsImageService;

    private Logger logger = LoggerFactory.getLogger(RelatedKeyWordController.class);

    @RequestMapping(value = "/search/init", method = RequestMethod.POST, produces="application/json")
    public Result init() {
        Result result;
        try {
            GoodsEntity goodsEntity = new GoodsEntity();
            goodsEntity.setId("goodsId1");
            goodsService.addOne(goodsEntity);
            goodsService.removeOne(goodsEntity);

            RelatedKeyWordEntity rkw = new RelatedKeyWordEntity();
            rkw.setId("rkwId1");
            relatedKeyWordService.addOne(rkw);
            relatedKeyWordService.removeOne(rkw);

            ShopEntity shopEntity = new ShopEntity();
            shopEntity.setId("shopId1");
            shopService.addOne(shopEntity);
            shopService.removeOne(shopEntity);

            BrandEntity brandEntity = new BrandEntity();
            brandEntity.setId("brandId1");
            brandService.addOne(brandEntity);
            brandService.removeOne(brandEntity);

            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setId("categoryId1");
            categoryService.addOne(categoryEntity);
            categoryService.removeOne(categoryEntity);

            GoodsImageEntity goodsImageEntity = new GoodsImageEntity();
            goodsImageEntity.setId("goodsImageId1");
            goodsImageService.addOne(goodsImageEntity);
            goodsImageService.removeOne(goodsImageEntity);

            result = new Result(Status.OK,"");
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getMessage());
            result = new Result(Status.ERROR,e.getMessage());
        }
        return result;
    }
}
