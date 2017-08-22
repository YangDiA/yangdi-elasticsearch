package com.ewandian.b2b2c.search.service.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.NewsCorrelationEntity;
import com.ewandian.b2b2c.search.repositories.NewsRepository;
import com.ewandian.b2b2c.search.service.INewsCorrelationService;
import com.ewandian.distributed.mq.kafka.AbstractKafkaConsumer;
import com.shd.util.validation.ClassValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2016/12/13.
 */
@Service
@Scope("prototype")
public class NewsCorrelationIdHandler extends AbstractKafkaConsumer {

    @Autowired
    private INewsCorrelationService newsCorrelationService;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public void handleMessage(String message) {
        try {
            //System.out.println("message: " + message);
            this.NewsAddCorrelationId(message);
        }catch (EwandianSearchEngineException e){
            e.getStackTrace().toString();
        }
    }

    //Index Single News Json
    public void NewsAddCorrelationId(String msg) throws EwandianSearchEngineException {
        try {
            //java.lang.System.out.println("msg: " + msg);
            NewsCorrelationEntity newsCorrelationEntity = JSONObject.parseObject(msg, NewsCorrelationEntity.class);
            Map<String,Object> map= JSON.parseObject(msg, Map.class);
            List<String> articleIds = ( List<String>) map.get("articleIds");

            for (int i = 0; i < articleIds.size();i++){

                //if (correlationNewsList.size() ==0 || correlationNewsList == null||!(correlationNewsList.get(i).getCorrelationId().equals(newsCorrelationEntity.getCorrelationId()))) {
                NewsCorrelationEntity resultEntity = newsCorrelationService.findOneById(articleIds.get(i));
                if(resultEntity != null) {
                    resultEntity.setId(UUID.randomUUID().toString());
                    resultEntity.setArticleId(articleIds.get(i));
                    resultEntity.setCorrelationId(newsCorrelationEntity.getCorrelationId());
                    resultEntity.setCorrelationName(newsCorrelationEntity.getCorrelationName()); //Added on 2016/12/28
                    newsCorrelationService.NewsAddCorrelationId(resultEntity);
                }
            }
        } catch (Exception e) {
            throw new EwandianSearchEngineException("Add elasticsearch data in News error：" + e.getStackTrace().toString());
        }
    }
}