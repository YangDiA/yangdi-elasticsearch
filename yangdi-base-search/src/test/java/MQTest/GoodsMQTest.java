package MQTest;

import com.alibaba.fastjson.JSONArray;
import com.ewandian.b2b2c.search.domain.document.BrandEntity;
import com.ewandian.b2b2c.search.domain.document.GoodsEntity;
import com.ewandian.distributed.mq.kafka.KafkaJmsTemplate;
import com.ewandian.test.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.SystemProfileValueSource;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suhd on 2016-12-15.
 */
public class GoodsMQTest extends BaseTest {
    @Autowired
    private KafkaJmsTemplate kafkaJmsTemplate;
    private GoodsEntity goodsEntity = new GoodsEntity();

    @Before
    public void init() {
        goodsEntity.setGoodsId("goodsId");
        goodsEntity.setGoodsName("goodsName");
        goodsEntity.setShopId("S160426000005");
        goodsEntity.setShopName("shopName");
        goodsEntity.setBrandId("brandId");
        goodsEntity.setBrandName("brandName");
        goodsEntity.setCategoryId("G1001004");
        goodsEntity.setCategoryName("categoryName");
        goodsEntity.setModel("model");
        goodsEntity.setAfterService("afterService");
        goodsEntity.setDeliverMsg("deliverMsg");
        goodsEntity.setDescription("description");
        goodsEntity.setDetail("detail");
        goodsEntity.setGoodsLabel("goodsLabel");
        goodsEntity.setImgId("image");
        goodsEntity.setIsPostFree("Y");
        goodsEntity.setIsPresellEnabled("Y");
        goodsEntity.setLmsVolume(new BigDecimal(1));
        goodsEntity.setLmsWeight(new BigDecimal(1));
        goodsEntity.setMarketPrice(new BigDecimal(10));
        goodsEntity.setPromotionPrice(new BigDecimal(5));
        goodsEntity.setGoodsPopularity(2);
        goodsEntity.setGoodsCommentNum(3);
        goodsEntity.setGoodsSaleVolume(4);
        //goodsEntity.setGoodsUpDate(new Date(System.currentTimeMillis()));
    }

    @Test
    public void testAddOne() throws Exception {
        process(goodsEntity, "GOODs-ADD-ONE");
    }

    @Test
    public void testRemoveOne() throws Exception {
        process(goodsEntity, "GOODs-REMOVE-ONE");
    }

    @Test
    public void testUpdateOne() throws Exception {
        process(goodsEntity, "GOODs-UPDATE-ONE");
    }

    @Test
    public void testAddSaleVolumeToGoods() throws Exception {
        goodsEntity.setGoodsId("LMD01BQC47131");
        process(goodsEntity, "GOODs-ADD-SALE_VOLUME");
    }

    @Test
    public void testSaleVolumeToGoodsHandler() throws Exception {
        goodsEntity.setGoodsId("LMD01BQC47131");
        process(goodsEntity, "GOODs-ADD-COMMENT_NUM");
    }

    private void process(GoodsEntity goodsEntity, String topic) throws InterruptedException {
        List<GoodsEntity> goodsEntityList = new ArrayList<GoodsEntity>();
        goodsEntityList.add(goodsEntity);
        String msg = JSONArray.toJSONString(goodsEntityList);
        kafkaJmsTemplate.sendStringMessage(msg, topic);
        Thread.sleep(3 * 1000);
    }
}
