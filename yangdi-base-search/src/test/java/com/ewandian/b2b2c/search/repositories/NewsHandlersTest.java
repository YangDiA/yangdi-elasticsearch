package com.ewandian.b2b2c.search.repositories;

import com.alibaba.fastjson.JSONObject;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.NewsEntity;
import com.ewandian.b2b2c.search.service.impl.NewsService;
import com.ewandian.b2b2c.search.service.mq.NewsAddOneHandler;
import com.ewandian.b2b2c.search.service.mq.NewsRemoveOneHandler;
import com.ewandian.b2b2c.search.service.mq.NewsUpdateOneHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Administrator on 2016/12/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/springContext-test.xml")
public class NewsHandlersTest {

    @Autowired
    private NewsAddOneHandler newsAddOneHandler;

    @Autowired
    private NewsRemoveOneHandler newsRemoveOneHandler;

    @Autowired
    private NewsUpdateOneHandler newsUpdateOneHandler;

    @Autowired
    private NewsService newsService;


    @Autowired
    private NewsRepository newsRepository;

    private NewsEntity newsEntity = new NewsEntity();


    @Before
    public void init() {
        newsEntity.setId("JDW00000078");
        newsEntity.setArticleId("JDW00000078");
        newsEntity.setTitle("Just for test, you know");
        newsEntity.setContent("Just for test, you know");
        newsEntity.setIsEffective("Just for test, you know");
        newsEntity.setImgId("Just for test, you know");
        newsRepository.save(newsEntity);
    }

    @Test
    public void testAddOneNewsEntity() throws EwandianSearchEngineException {
       /* newsAddOneHandler.addOneNewsEntity(newsEntity);
        NewsEntity retVal = newsRepository.findOne(newsEntity.getId());
        assertThat(retVal,is(notNullValue()));
        assertThat(retVal.getId(),is(newsEntity.getId()));*/
    }

    @Test
    public void testAddOneNewsJson() throws EwandianSearchEngineException {
        //String jsonStr = "[{\"id\":\"测试 id 003\"},{\"newsId\":\"测试 id 003\"},{\"correlationId\":\"测试 correlationId 003\"},{\"correlationName\":\"测试 correlationName 003\"},{\"articleId\":\"测试 articleId 003\"},{\"title\":\"测试 title 003\"},{\"content\":\"测试 content 003\"},{\"typeName\":\"测试 typeName 003\"},{\"status\":\"测试 status 003\"},{\"imgId\":\"测试 imgId 003\"}]";
        //newsAddOneHandler.addOneNewsJson(jsonStr);
/*        AdsEntity retVal = adsRepository.findOne(adsEntity.getId());
        assertThat(retVal,is(notNullValue()));
        assertThat(retVal.getId(),is(adsEntity.getId()));*/
    }

    @Test
    public void testRemoveOneNewsEntity() throws EwandianSearchEngineException {
          //newsRemoveOneHandler.removeOneNewsEntity(newsEntity);
/*        AdsEntity retVal = adsRepository.findOne(adsEntity.getId());
        assertThat(retVal,is(nullValue()));*/
    }

    @Test
    public void testRemoveOneNewsJson() throws EwandianSearchEngineException {
        String jsonStr = "{\"id\":\"JDW00000175\"}";
        //newsRemoveOneHandler.removeOneNewsJson(jsonStr);
    }

    @Test
    //Id should be kept while other fields will be updated.
    public void testUpdateOneNewsEntity() throws EwandianSearchEngineException {
          //newsUpdateOneHandler.updateOneNewsEntity(newsEntity);
    }

    @Test
    //Id should be kept while other fields will be updated.
    public void testUpdateOneNewsJson() throws EwandianSearchEngineException {
        String jsonStr = "[{\"id\":\"测试 id 000\"},{\"newsId\":\"测试 id 000\"},{\"correlationId\":\"更新 correlationId 002\"},{\"correlationName\":\"更新 correlationName 002\"},{\"articleId\":\"更新 articleId 002\"},{\"title\":\"更新 title 002\"},{\"content\":\"更新 content 002\"},{\"typeName\":\"更新 typeName 002\"},{\"status\":\"更新 status 002\"},{\"imgId\":\"更新 imgId 002\"}]";
            //newsUpdateOneHandler.updateOneNewsJson(jsonStr);
    }

    @Test
    public void removeAll() throws EwandianSearchEngineException {
        try {
            newsRepository.deleteAll();
        } catch (Exception e) {
            throw new EwandianSearchEngineException("Empty elasticsearch data in News error：" + e.getStackTrace().toString());
        }
    }

    @Test
    public void testFindOne() throws EwandianSearchEngineException {
        NewsEntity news = newsService.findOne(newsEntity);
    }

    @Test
    public void testUpdateOne() throws EwandianSearchEngineException {
       newsService.updateOne(newsEntity);
    }

    @Test
    public void testNewsAddColumnIdentify() throws EwandianSearchEngineException{
        String msg = "{\"articleId\":[\"JDW00000421\",\"JDW00000420\",\"JDW00000419\"],\"articleType\":\"TELEVISION\",\"dataMap\":{},\"new\":true,\"singleFind\":\"\",\"typeName\":\"电视\",\"verified\":false,\"version\":0}";
        NewsEntity newsEntity = JSONObject.parseObject(msg, NewsEntity.class);
        String articleId  = newsEntity.getArticleId();

        String articleIdNew = articleId.substring(1,articleId.length()-1);// remove head and tail
        String[] articleIdArray = articleIdNew.split(",");//split the string
        //List articleIdList = java.util.Arrays.asList(articleIdArray);

        for (int i =0; i < articleIdArray.length;i++){
            java.lang.System.out.println(articleIdArray[i]);
        }


        //newsService.NewsAddColumnIdentify(newsEntity);

    }
}