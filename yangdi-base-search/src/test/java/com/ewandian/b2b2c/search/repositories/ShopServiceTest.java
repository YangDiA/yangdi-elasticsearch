package com.ewandian.b2b2c.search.repositories;

import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.ShopEntity;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;
import com.ewandian.b2b2c.search.service.IShopService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by suhd on 2016-11-28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/springContext-test.xml")
public class ShopServiceTest {
    @Autowired
    private IShopService shopService;
    private ShopEntity shopEntity = new ShopEntity();
    private SearchKeyWord skw = new SearchKeyWord();

    @Before
    public void init() {
        shopEntity.setShopId("S160301000005");
        shopEntity.setShopName("老板专卖");
        shopEntity.setImgId("icon1");
        shopEntity.setShopBackImg("address1");
        shopEntity.setRecommendShop(1);

        skw.setSearchArg("家电");
    }

    @Test
    public void testAddOne() throws EwandianSearchEngineException {
        shopService.addOne(shopEntity);
    }

    @Test
    public void testUpdateOne() throws EwandianSearchEngineException {
        shopEntity = shopService.findOneByShopId("S160301000005");
        shopEntity.setRecommendShop(1);
        shopService.updateOne(shopEntity);
    }

    @Test
    public void testFindOneByShopId() throws EwandianSearchEngineException {
        shopService.findOneByShopId(shopEntity.getShopId());
    }

    @Test
    public void testRemoveOne() throws EwandianSearchEngineException {
        shopEntity.setId("422ff70c-7961-441d-881f-26666b8677e1");
        shopService.removeOne(shopEntity);
    }

    @Test
    public void findShops() throws EwandianSearchEngineException {
        shopService.findShopsByShopName(skw);
    }
}
