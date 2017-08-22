package com.ewandian.b2b2c.search.service.multithread;

import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.CategoryEntity;
import com.ewandian.b2b2c.search.domain.document.GoodsEntity;
import com.ewandian.b2b2c.search.service.IGoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by suhd on 2016-12-30.
 */
@Service
@Scope("prototype")
public class UpdateCategoryNameOfGoodsEntity implements Runnable {
    @Autowired
    private IGoodsService goodsService;
    private CategoryEntity categoryEntity;
    private Logger logger = LoggerFactory.getLogger(UpdateCategoryNameOfGoodsEntity.class);

    public UpdateCategoryNameOfGoodsEntity setCategoryEntity(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
        return this;
    }

    public void run() {
        try {
            List<GoodsEntity> goodsEntityList = goodsService.fetchAllGoodsEntityByCategoryId(categoryEntity.getCategoryId());
            doUpdateBrandNameOfGoodsEntity(goodsEntityList,categoryEntity.getCategoryName());
        } catch (EwandianSearchEngineException e) {
            logger.error("UpdateCategoryNameOfGoodsEntity error : " + e.getMessage());
        }
    }

    private void doUpdateBrandNameOfGoodsEntity(List<GoodsEntity> goodsEntityList, String categoryName) throws EwandianSearchEngineException {
        for(int i=0; i<goodsEntityList.size(); i++) {
            GoodsEntity goodsEntity = goodsEntityList.get(i);
            goodsEntity.setCategoryName(categoryName);
            goodsService.updateCategoryNameByGoodsEntityWithGoodsIdAndCategoryName(goodsEntity);
        }
    }
}
