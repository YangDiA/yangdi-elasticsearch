package com.ewandian.b2b2c.search.service.mq;

import com.alibaba.fastjson.JSONObject;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.NewsCorrelationEntity;
import com.ewandian.b2b2c.search.domain.document.NewsEntity;
import com.ewandian.b2b2c.search.repositories.NewsRepository;
import com.ewandian.b2b2c.search.service.INewsCorrelationService;
import com.ewandian.b2b2c.search.service.INewsService;
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
 * Created by Administrator on 2016/12/8.
 */
@Service
@Scope("prototype")
public class NewsRemoveOneHandler extends AbstractKafkaConsumer {
    @Autowired
    private INewsService newsService;

    @Autowired
    private INewsCorrelationService newsCorrelationService;

    public void handleMessage(String message) throws Exception {
        //System.out.println("message: " + message);
        this.processNewsColumnIdentify(message);
        this.processNewsCorrelation(message);
    }

    private void processNewsColumnIdentify(String msg) throws Exception {
        List<NewsEntity> columnIdentifyNewsList = newsService.findListofNewsbyId(msg);
        if (columnIdentifyNewsList.size() > 0) {
            for (int i = 0; i < columnIdentifyNewsList.size(); i++) {
                newsService.removeOne(columnIdentifyNewsList.get(i));
            }
        }
    }

    private void processNewsCorrelation(String msg) throws Exception {
        List<NewsCorrelationEntity> correlationNewsList = newsCorrelationService.findListofNewsbyId(msg);
        if (correlationNewsList.size() > 0) {
            for (int i = 0; i < correlationNewsList.size(); i++) {
                newsCorrelationService.removeOne(correlationNewsList.get(i));
            }
        }
    }
}