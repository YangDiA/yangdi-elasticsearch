package com.ewandian.b2b2c.search.service.mq;

import com.ewandian.b2b2c.search.domain.document.GoodsEntity;
import com.ewandian.b2b2c.search.domain.document.GoodsImageEntity;
import com.ewandian.b2b2c.search.service.IGoodsImageService;
import com.ewandian.b2b2c.search.service.IGoodsService;
import com.shd.util.validation.ClassValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by suhd on 2016-12-24.
 */
@Service
@Scope("prototype")
public class GoodsUpdateOneHandler extends SuperHandler<GoodsEntity> {
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private IGoodsImageService goodsImageService;

    @Override
    public void handleMessage(String msg) throws Exception {
        super.setClazz(GoodsEntity.class);
        super.handleMessage(msg);
    }

    @Override
    public void process(GoodsEntity goodsEntity) throws Exception {
        //validating for checking property that should not be empty
        ClassValidation.validate(goodsEntity);
        GoodsImageEntity goodsImageEntity = new GoodsImageEntity();
        goodsImageEntity.setGoodsId(goodsEntity.getGoodsId());
        //Delete goodsImageEntity here reluctantly for kafka reason
        goodsImageService.removeOne(goodsImageEntity);
        goodsService.updateOne(goodsEntity);
    }

}
