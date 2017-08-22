package com.ewandian.b2b2c.search.service.mq;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.AdsEntity;
import com.ewandian.b2b2c.search.service.IAdsService;
import com.ewandian.distributed.mq.kafka.AbstractKafkaConsumer;
import com.shd.util.validation.ClassValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Created by Administrator on 2016/12/7.
 */
 @Service
 @Scope("prototype")
 public class AdsAddOneHandler extends AbstractKafkaConsumer {

        @Autowired
        private IAdsService adsService;

        public void handleMessage(String msg) throws Exception {
            try {
                //System.out.println("msg: " + msg);
                AdsEntity adsEntity = JSONObject.parseObject(msg, AdsEntity.class);
                this.process(adsEntity);
            }catch (Exception e) {
                throw e;
            }
        }

        private void process(AdsEntity adsEntity) throws Exception {
            //validating for checking property that should not be empty
            //ClassValidation.validate(adsEntity);
            adsService.addOne(adsEntity);
        }
    }