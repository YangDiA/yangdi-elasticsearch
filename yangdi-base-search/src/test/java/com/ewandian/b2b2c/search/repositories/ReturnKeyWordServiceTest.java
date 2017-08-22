/*
package com.ewandian.b2b2c.search.repositories;

import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.GoodsEntity;
import com.ewandian.b2b2c.search.domain.keyword.ReturnKeyWord;
import com.ewandian.b2b2c.search.service.impl.GoodsService;
import com.ewandian.b2b2c.search.service.impl.ReturnKeyWordService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
*/
/**
 * Created by suhd on 2016-11-24.
 *//*

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/springContext-test.xml")
public class ReturnKeyWordServiceTest {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private ReturnKeyWordService keyWordService;

    private GoodsEntity goodsEntity = new GoodsEntity();

    @Before
    public void init() {
        goodsEntity.setGoodsId("sd12");
        goodsEntity.setGoodsName("万家乐灶具JZT-IQL83B(12T)");
        goodsEntity.setCategoryName("中兴");
        goodsEntity.setModel("XQB60-BZ1216");
    }

    @Test
    public void testOnChangingEvent() throws EwandianSearchEngineException {
        goodsService.addOne(goodsEntity);
        ReturnKeyWord kwe = keyWordService.onChangingEvent("中国");
        assertEquals(kwe.isNull(),false);
        */
/*kwe = keyWordService.onChangingEvent("中");
        assertEquals(kwe.isNull(),false);
        kwe = keyWordService.onChangingEvent("XQB60-BZ120");
        assertEquals(kwe.isNull(),false);*//*

        goodsService.removeOne(goodsEntity);
    }
}
*/
