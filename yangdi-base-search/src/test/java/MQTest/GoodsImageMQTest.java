package MQTest;

import com.alibaba.fastjson.JSONArray;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.BrandEntity;
import com.ewandian.b2b2c.search.domain.document.GoodsImageEntity;
import com.ewandian.b2b2c.search.service.IGoodsImageService;
import com.ewandian.distributed.mq.kafka.KafkaJmsTemplate;
import com.ewandian.test.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suhd on 2016-12-24.
 */
public class GoodsImageMQTest extends BaseTest {
    @Autowired
    private KafkaJmsTemplate kafkaJmsTemplate;
    private GoodsImageEntity goodsImageEntity = new GoodsImageEntity();

    @Before
    public void init() {
        goodsImageEntity.setGoodsId("goodsId");
        goodsImageEntity.setImgId("imgId");
        goodsImageEntity.setSeqNo("1");
    }

    @Test
    public void testAddOne() throws Exception {
        process(goodsImageEntity, "GOODS-IMAGE-ADD-ONE1");
    }

    @Test
    public void testUpdateOne() throws Exception {
        process(goodsImageEntity, "GOODS-IMAGE-UPDATE-ONE1");
    }

    @Test
    public void testRemoveOne() throws Exception {
        process(goodsImageEntity, "GOODS-IMAGE-REMOVE-ONE1");
    }

    private void process(GoodsImageEntity goodsImageEntity, String topic) throws InterruptedException {
        List<GoodsImageEntity> goodsImageEntityList = new ArrayList<GoodsImageEntity>();
        goodsImageEntityList.add(goodsImageEntity);
        String msg = JSONArray.toJSONString(goodsImageEntityList);
        kafkaJmsTemplate.sendStringMessage(msg, topic);
        Thread.sleep(3 * 1000);
    }
}
