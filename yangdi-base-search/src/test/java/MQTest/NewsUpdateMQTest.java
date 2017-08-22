package MQTest;

import com.ewandian.b2b2c.search.service.mq.NewsAddOneHandler;
import com.ewandian.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2016/12/8.
 */
public class NewsUpdateMQTest extends BaseTest {

/*
    @Autowired
    NewsUpdateOneHandler newsUpdateOneHandler;
*/

    @Autowired
    NewsAddOneHandler newsAddOneHandler;

    @Test
    public void  consumeMQ(){
        String message = "[{\"id\":\"rrrrrrr\"},{\"newsId\":\"rrrrrrrrr\"},{\"correlationId\":\"更新 correlationId 001\"},{\"correlationName\":\"更新 correlationName 001\"},{\"articleId\":\"更新 articleId 001\"},{\"title\":\"更新 title 001\"},{\"content\":\"更新 content 001\"},{\"typeName\":\"更新 typeName 001\"},{\"status\":\"更新 status 001\"},{\"imgId\":\"更新 imgId 001\"}]";

        try {
            newsAddOneHandler.handleMessage(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}