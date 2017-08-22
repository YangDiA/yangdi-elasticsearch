package com.ewandian.b2b2c.search.service.mq;

import com.alibaba.fastjson.JSONArray;
import com.ewandian.distributed.mq.kafka.AbstractKafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by suhd on 2016-12-19.
 */
public abstract class SuperHandler<T> extends AbstractKafkaConsumer{

    private Logger logger = LoggerFactory.getLogger(SuperHandler.class);

    private Class clazz;

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public void handleMessage(String msg) throws Exception {
        try {
            List<T> entityList = JSONArray.parseArray(msg,clazz);
            for(int i=0; i<entityList.size(); i++) {
                this.process(entityList.get(i));
            }
        }catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    public abstract void process(T t) throws Exception;
}
