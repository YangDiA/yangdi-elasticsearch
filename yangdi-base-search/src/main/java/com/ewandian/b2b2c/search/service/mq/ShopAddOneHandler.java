package com.ewandian.b2b2c.search.service.mq;

import com.alibaba.fastjson.JSONArray;
import com.ewandian.b2b2c.search.domain.document.ShopEntity;
import com.ewandian.b2b2c.search.service.IShopService;
import com.ewandian.distributed.mq.kafka.AbstractKafkaConsumer;
import com.shd.util.validation.ClassValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/12/19.
 */
@Service
@Scope("prototype")
public class ShopAddOneHandler extends AbstractKafkaConsumer {
    @Autowired
    private IShopService shopService;
    private Logger logger = LoggerFactory.getLogger(ShopAddOneHandler.class);

    public void handleMessage(String msg) throws Exception {
        try {
            List<ShopEntity> shopEntityList = JSONArray.parseArray(msg, ShopEntity.class);
            for(int i=0; i<shopEntityList.size(); i++) {
                this.process(shopEntityList.get(i));
            }
        }catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    private void process(ShopEntity shopEntity) throws Exception {
        //validating for checking property that should not be empty
        ClassValidation.validate(shopEntity);
        shopService.addOne(shopEntity);
    }
}
