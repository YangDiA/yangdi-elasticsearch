package com.ewandian.b2b2c.search.service.mq;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.AdsEntity;
import com.ewandian.b2b2c.search.repositories.AdsRepository;
import com.ewandian.b2b2c.search.service.IAdsService;
import com.ewandian.distributed.mq.kafka.AbstractKafkaConsumer;
import com.shd.util.validation.ClassValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/12/7.
 */
@Service
@Scope("prototype")
public class AdsUpdateOneHandler extends AbstractKafkaConsumer {
    @Autowired
    private IAdsService adsService;

    public void handleMessage(String msg) throws Exception {
        try {
            //System.out.println("msg: " + msg);
            AdsEntity adsEntity = JSONObject.parseObject(msg, AdsEntity.class);

            AdsEntity resultAdsEntity = adsService.findOne(adsEntity);
            if(resultAdsEntity != null) {

                adsEntity.setShopId(resultAdsEntity.getShopId());
                adsEntity.setColumnIdentify(resultAdsEntity.getColumnIdentify());

                if (adsEntity.getIsDeleted().equals("F")) {
                    this.updateProcess(adsEntity);
                    this.removeProcess(resultAdsEntity);
                } else if (adsEntity.getIsDeleted().equals("T")) {
                    this.removeProcess(resultAdsEntity);
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private void updateProcess(AdsEntity adsEntity) throws Exception {
        //validating for checking property that should not be empty
        ClassValidation.validate(adsEntity);
        adsService.updateOne(adsEntity);
    }

    private void removeProcess(AdsEntity adsEntity) throws Exception {
        //validating for checking property that should not be empty
        ClassValidation.validate(adsEntity);
        adsService.removeOne(adsEntity);
    }
}