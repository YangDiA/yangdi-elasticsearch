package com.ewandian.b2b2c.search.repositories;

import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.GoodsImageEntity;
import com.ewandian.b2b2c.search.service.IGoodsImageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by suhd on 2016-12-01.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/springContext-test.xml")
public class GoodsImageServiceTest {
    @Autowired
    private IGoodsImageService goodsImageService;

    GoodsImageEntity goodsImageEntity = new GoodsImageEntity();

    @Before
    public void init() {
        goodsImageEntity.setGoodsId("LMD01MTV46380");
        goodsImageEntity.setSeqNo("1");
        goodsImageEntity.setImgId("8e652e2adc89c8fc523b24b06305a205");
    }

    @Test
    public void testAddOne() throws EwandianSearchEngineException {
        goodsImageService.addOne(goodsImageEntity);
    }
}
