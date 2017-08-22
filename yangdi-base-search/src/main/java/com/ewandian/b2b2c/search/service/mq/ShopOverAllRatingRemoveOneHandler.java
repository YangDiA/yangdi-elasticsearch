package com.ewandian.b2b2c.search.service.mq;

import com.alibaba.fastjson.JSONArray;
import com.ewandian.b2b2c.search.domain.document.ShopOverAllRatingEntity;
import com.ewandian.b2b2c.search.service.IShopOverAllRatingService;
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
public class ShopOverAllRatingRemoveOneHandler extends AbstractKafkaConsumer {
    @Autowired
    private IShopOverAllRatingService shopOverAllRatingService;
    private Logger logger = LoggerFactory.getLogger(ShopOverAllRatingRemoveOneHandler.class);

    public void handleMessage(String msg) throws Exception {
        try {
            List<ShopOverAllRatingEntity> shopOverAllRatingEntityList = JSONArray.parseArray(msg,ShopOverAllRatingEntity.class);
            for(int i=0; i<shopOverAllRatingEntityList.size(); i++) {
                this.process(shopOverAllRatingEntityList.get(i));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    private void process(ShopOverAllRatingEntity shopOverAllRatingEntity) throws Exception {
        //validating for checking property that should not be empty
        ClassValidation.validate(shopOverAllRatingEntity);
        shopOverAllRatingService.removeOne(shopOverAllRatingEntity);
    }
}