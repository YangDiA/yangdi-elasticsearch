package com.ewandian.b2b2c.search.service.mq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.HelpCenterEntity;
import com.ewandian.b2b2c.search.repositories.HelpCenterRepository;
import com.ewandian.b2b2c.search.service.IHelpCenterService;
import com.ewandian.distributed.mq.kafka.AbstractKafkaConsumer;
import com.shd.util.validation.ClassValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/12/13.
 */
@Service
@Scope("prototype")
public class HelpCenterAddOneHandler extends AbstractKafkaConsumer {

    @Autowired
    private IHelpCenterService helpCenterService;

    public void handleMessage(String msg) throws Exception {
        try {
            System.out.println("add msg: " + msg);
            HelpCenterEntity helpCenterEntity = JSONObject.parseObject(msg, HelpCenterEntity.class);
            this.process(helpCenterEntity);
        }catch (Exception e) {
            throw e;
        }
    }

    private void process(HelpCenterEntity helpCenterEntity) throws Exception {
        //validating for checking property that should not be empty
        ClassValidation.validate(helpCenterEntity);
        helpCenterService.addOne(helpCenterEntity);
    }
}