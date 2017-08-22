package com.ewandian.b2b2c.search.service.mq;


import com.ewandian.b2b2c.search.domain.document.BrandEntity;
import com.ewandian.b2b2c.search.service.IBrandService;
import com.shd.util.validation.ClassValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by suhd on 2016-12-13.
 */
@Service
@Scope("prototype")
public class BrandAddOneHandler extends SuperHandler<BrandEntity> {
    @Autowired
    private IBrandService brandService;

    @Override
    public void handleMessage(String msg) throws Exception {
        super.setClazz(BrandEntity.class);
        super.handleMessage(msg);
    }

    @Override
    public void process(BrandEntity brandEntity) throws Exception {
        //validating for checking property that should not be empty
        ClassValidation.validate(brandEntity);
        brandService.addOne(brandEntity);
    }
}
