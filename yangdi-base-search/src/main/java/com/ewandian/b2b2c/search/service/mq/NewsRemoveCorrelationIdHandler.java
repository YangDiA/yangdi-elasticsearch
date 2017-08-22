package com.ewandian.b2b2c.search.service.mq;

import com.alibaba.fastjson.JSONObject;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.NewsCorrelationEntity;
import com.ewandian.b2b2c.search.service.INewsCorrelationService;
import com.ewandian.distributed.mq.kafka.AbstractKafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/12/13.
 */
@Service
@Scope("prototype")
public class NewsRemoveCorrelationIdHandler extends AbstractKafkaConsumer {

    @Autowired
    private INewsCorrelationService newsCorrelationService;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public void handleMessage(String message) {
        try {
            //java.lang.System.out.println("message: " + message);
            this.NewsRemoveCorrelationid(message);
        }catch (EwandianSearchEngineException e){
            e.getStackTrace().toString();
        }
    }

    public void NewsRemoveCorrelationid(String msg) throws EwandianSearchEngineException {
        try {
            java.lang.System.out.println("msg: " + msg);
            NewsCorrelationEntity newsEntity = JSONObject.parseObject(msg, NewsCorrelationEntity.class);
            List<NewsCorrelationEntity> CorrelationidNewsList = newsCorrelationService.findNewsArticleIdAndCorrelation(newsEntity);

            if (CorrelationidNewsList.size() > 0) {
                for (int i = 0; i < CorrelationidNewsList.size(); i++) {
                    newsCorrelationService.removeOne(CorrelationidNewsList.get(i));
                }
            }

        } catch (Exception e) {
            throw new EwandianSearchEngineException("remove correlation in News correlation error：" + e.getStackTrace().toString());
        }
    }
}