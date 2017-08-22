package MQTest;

import com.ewandian.b2b2c.search.service.mq.AdsAddOneHandler;
import com.ewandian.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2016/12/8.
 */
public class AdsAddMQTest extends BaseTest {
    @Autowired
    AdsAddOneHandler adsAddOneHandler;

    @Test
    public void  consumeMQ(){
        String message = "{\"adId\":\"DEC161214000027\",\"columnId\":\"CEE3DE0D-929E-4DD4-BC4E-8128C9795FF6\",\"createDate\":1481685330791,\"dataMap\":{},\"deliveryEndDate\":1482163200000,\"deliveryStartDate\":1481644800000,\"isDeleted\":\"F\",\"linkHref\":\"http://google\",\"new\":true,\"seqNo\":3,\"singleFind\":\"\",\"status\":\"0\",\"verified\":false,\"version\":0}";
        try {
            adsAddOneHandler.handleMessage(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}