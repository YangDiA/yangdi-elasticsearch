package MQTest;

import com.alibaba.fastjson.JSONArray;
import com.ewandian.b2b2c.search.domain.document.AdsEntity;
import com.ewandian.distributed.mq.kafka.KafkaJmsTemplate;
import com.ewandian.test.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/14.
 */
public class AdsMQTest extends BaseTest {

    @Autowired
    private KafkaJmsTemplate kafkaJmsTemplate;

    private AdsEntity adsEntity = new AdsEntity();

    @Before
    public void init() {
        adsEntity.setColumnId("columnId");
        //adsEntity.setEntityName("entityName");
        adsEntity.setColumnIdentify("columnIdentify");
        //adsEntity.setEntityId("entityId");
    }

    @Test
    public void testAddOne() throws Exception {
        process(adsEntity, "ADs-ADD-ONE");
    }


    private void process(AdsEntity adsEntity, String topic) throws InterruptedException {
        List<AdsEntity> adsEntityList = new ArrayList<AdsEntity>();
        adsEntityList.add(adsEntity);
        String msg = JSONArray.toJSONString(adsEntityList);
        kafkaJmsTemplate.sendStringMessage(msg, topic);
        Thread.sleep(100 * 1000);
    }
}