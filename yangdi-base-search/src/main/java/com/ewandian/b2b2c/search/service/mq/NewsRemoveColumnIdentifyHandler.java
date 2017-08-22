package com.ewandian.b2b2c.search.service.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.NewsEntity;
import com.ewandian.b2b2c.search.repositories.NewsRepository;
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
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2016/12/13.
 */
@Service
@Scope("prototype")
public class NewsRemoveColumnIdentifyHandler extends AbstractKafkaConsumer {

    @Autowired
    private INewsService newsService;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public void handleMessage(String message) {
        try {
            //java.lang.System.out.println("message: " + message);
            this.NewsRemoveColumnIdentify(message);
        }catch (EwandianSearchEngineException e){
            e.getStackTrace().toString();
        }
    }

    public void NewsRemoveColumnIdentify(String msg) throws EwandianSearchEngineException {
        try {
            //java.lang.System.out.println("msg: " + msg);
            NewsEntity newsEntity = JSONObject.parseObject(msg, NewsEntity.class);
            List<NewsEntity> columnIdentifyNewsList = newsService.findNewsArticleIdAndColumnIdentify(newsEntity);
            //System.out.println("columnIdentifyNewsList.size() before remove; " + columnIdentifyNewsList.size());

            if (columnIdentifyNewsList.size() > 0) {
                for (int i = 0; i < columnIdentifyNewsList.size(); i++) {
                    newsService.removeOne(columnIdentifyNewsList.get(i));
                }
            }
            //System.out.println("columnIdentifyNewsList.size() after remove; " + columnIdentifyNewsList.size());

        } catch (Exception e) {
            throw new EwandianSearchEngineException("remove columnIdentify in News error：" + e.getStackTrace().toString());
        }
    }
}