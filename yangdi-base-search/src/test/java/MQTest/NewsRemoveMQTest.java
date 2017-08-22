package MQTest;

import com.ewandian.b2b2c.search.service.mq.NewsRemoveOneHandler;
import com.ewandian.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2016/12/8.
 */
public class NewsRemoveMQTest extends BaseTest {

    @Autowired
    NewsRemoveOneHandler newsRemoveOneHandler;

    @Test
    public void  consumeMQ(){
        String message = "{\"id\":\"测试 id 003\"}";

        try {
            newsRemoveOneHandler.handleMessage(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}