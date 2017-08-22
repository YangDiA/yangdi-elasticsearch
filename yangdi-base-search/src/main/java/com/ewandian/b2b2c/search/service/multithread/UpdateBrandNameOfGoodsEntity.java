package com.ewandian.b2b2c.search.service.multithread;

import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.BrandEntity;
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
public class UpdateBrandNameOfGoodsEntity implements Runnable  {
    @Autowired
    private IGoodsService goodsService;
    private BrandEntity brandEntity;
    private Logger logger = LoggerFactory.getLogger(UpdateBrandNameOfGoodsEntity.class);

    public UpdateBrandNameOfGoodsEntity setBrandEntity(BrandEntity brandEntity) {
        this.brandEntity = brandEntity;
        return this;
    }

    public void run() {
        try {
            List<GoodsEntity> goodsEntityList = goodsService.fetchAllGoodsEntityByBrandId(brandEntity.getBrandId());
            doUpdateBrandNameOfGoodsEntity(goodsEntityList,brandEntity.getBrandName());
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getMessage());
        }
    }

    private void doUpdateBrandNameOfGoodsEntity(List<GoodsEntity> goodsEntityList, String brandName) throws EwandianSearchEngineException {
        for(int i=0; i<goodsEntityList.size(); i++) {
            GoodsEntity goodsEntity = goodsEntityList.get(i);
            goodsEntity.setBrandName(brandName);
            goodsService.updateBrandNameByGoodsEntityWithGoodsIdAndBrandName(goodsEntity);
        }
    }
}
