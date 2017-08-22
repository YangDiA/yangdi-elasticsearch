package MQTest;

import com.alibaba.fastjson.JSONArray;
import com.ewandian.b2b2c.search.domain.document.BrandEntity;
import com.ewandian.distributed.mq.kafka.KafkaJmsTemplate;
import com.ewandian.test.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suhd on 2016-12-13.
 */
public class BrandMQTest extends BaseTest {

    @Autowired
    private KafkaJmsTemplate kafkaJmsTemplate;

    private BrandEntity brandEntity = new BrandEntity();

    @Before
    public void init() {
        brandEntity.setBrandId("newbrandid");
        brandEntity.setBrandName("newbrandname");
        brandEntity.setImgId("newbrandimgid");
        brandEntity.setAddrName("newbrandaddrname");
    }

    @Test
    public void testAddOne() throws Exception {
        process(brandEntity, "BRANDs-ADD-ONE");
    }

    @Test
    public void testRemoveOne() throws Exception {
        process(brandEntity, "BRANDs-REMOVE-ONE");
    }

    @Test
    public void testUpdateOne() throws Exception {
        process(brandEntity, "BRANDs-UPDATE-ONE");
    }

    private void process(BrandEntity brandEntity, String topic) throws InterruptedException {
        List<BrandEntity> brandEntityList = new ArrayList<BrandEntity>();
        brandEntityList.add(brandEntity);
        String msg = JSONArray.toJSONString(brandEntityList);
        kafkaJmsTemplate.sendStringMessage(msg, topic);
        Thread.sleep(3 * 1000);
    }
}
