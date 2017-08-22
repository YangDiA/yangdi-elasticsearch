package com.ewandian.b2b2c.search.service.mq;

import com.ewandian.b2b2c.search.domain.document.BrandEntity;
import com.ewandian.b2b2c.search.service.IBrandService;
import com.ewandian.b2b2c.search.service.multithread.UpdateBrandNameOfGoodsEntity;
import com.shd.util.validation.ClassValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;

/**
 * Created by suhd on 2016-12-13.
 */
@Service
@Scope("prototype")
public class BrandUpdateOneHandler extends SuperHandler<BrandEntity> {
    @Autowired
    private IBrandService brandService;
    @Autowired
    private UpdateBrandNameOfGoodsEntity updateBrandNameOfGoodsEntity;

    @Override
    public void handleMessage(String msg) throws Exception {
        super.setClazz(BrandEntity.class);
        super.handleMessage(msg);
    }

    @Override
    public void process(BrandEntity brandEntity) throws Exception {
        //validating for checking property that should not be empty
        ClassValidation.validate(brandEntity);
        Executors.newCachedThreadPool().execute(updateBrandNameOfGoodsEntity.setBrandEntity(brandEntity));
        brandService.updateOne(brandEntity);
    }

}
