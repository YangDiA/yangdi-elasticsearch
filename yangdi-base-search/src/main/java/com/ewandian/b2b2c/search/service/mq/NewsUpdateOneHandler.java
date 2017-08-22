package com.ewandian.b2b2c.search.service.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ewandian.b2b2c.search.app.constant.QueryFieldConstant;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.BrandEntity;
import com.ewandian.b2b2c.search.domain.document.NewsCorrelationEntity;
import com.ewandian.b2b2c.search.domain.document.NewsEntity;
import com.ewandian.b2b2c.search.repositories.NewsRepository;
import com.ewandian.b2b2c.search.service.INewsService;
import com.ewandian.b2b2c.search.service.INewsCorrelationService;
import com.ewandian.distributed.mq.kafka.AbstractKafkaConsumer;
import com.shd.util.validation.ClassValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * Created by Administrator on 2016/12/8.
 */
@Service
@Scope("prototype")
public class NewsUpdateOneHandler extends AbstractKafkaConsumer {
    @Autowired
    private INewsService newsService;

    @Autowired
    private INewsCorrelationService newsCorrelationService;

    public void handleMessage(String msg) throws Exception {
        //System.out.println("msg: " + msg);
        NewsEntity newsEntity = JSONObject.parseObject(msg, NewsEntity.class);
        NewsCorrelationEntity newsCorrelationEntity = JSONObject.parseObject(msg, NewsCorrelationEntity.class);
        this.processNewsColumnIdentify(newsEntity);
        this.processNewsCorrelation(newsCorrelationEntity);
    }

    private void processNewsColumnIdentify(NewsEntity newsEntity) throws Exception {
                ClassValidation.validate(newsEntity); //not used
                newsService.updateOne(newsEntity);
    }

    private void processNewsCorrelation(NewsCorrelationEntity newsCorrelationEntity) throws Exception {
               ClassValidation.validate(newsCorrelationEntity); //not used
               newsCorrelationService.updateOne(newsCorrelationEntity);
    }
}