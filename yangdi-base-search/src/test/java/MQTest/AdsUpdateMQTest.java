package MQTest;

import com.ewandian.b2b2c.search.service.mq.AdsUpdateOneHandler;
import com.ewandian.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2016/12/8.
 */
public class AdsUpdateMQTest extends BaseTest {
    @Autowired
    AdsUpdateOneHandler adsUpdateOneHandler;


    @Test
    public void  consumeMQ(){
        String message = "[{\"id\":\"DEC161202000007\"},{\"webPageId\":\"DEC161202000007\"},{\"columnId\":\"更新 columnId\"},{\"columnIdentify\":\"更新 columnIdentify\"},{\"entityName\":\"更新 entityName\"},{\"entityId\":\"更新 entityId\"},{\"imgId\":\"更新 imgId\"}]";

        try {
            adsUpdateOneHandler.handleMessage(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}