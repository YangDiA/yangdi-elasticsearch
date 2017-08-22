package MQTest;

import com.ewandian.b2b2c.search.service.mq.NewsAddOneHandler;
import com.ewandian.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2016/12/8.
 */
public class NewsAddMQTest extends BaseTest {

    @Autowired
    NewsAddOneHandler newsAddOneHandler;

    @Test
    public void  consumeMQ(){
        String message = "[{\"id\":\"测试 id 003\"},{\"newsId\":\"测试 id 003\"},{\"correlationId\":\"测试 correlationId 003\"},{\"correlationName\":\"测试 correlationName 003\"},{\"articleId\":\"测试 articleId 003\"},{\"title\":\"测试 title 003\"},{\"content\":\"测试 content 003\"},{\"typeName\":\"测试 typeName 003\"},{\"status\":\"测试 status 003\"},{\"imgId\":\"测试 imgId 003\"}]";

        try {
            newsAddOneHandler.handleMessage(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}