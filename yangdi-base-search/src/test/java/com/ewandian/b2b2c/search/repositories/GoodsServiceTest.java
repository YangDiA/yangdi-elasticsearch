
package com.ewandian.b2b2c.search.repositories;

import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.GoodsEntity;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;
import com.ewandian.b2b2c.search.service.IGoodsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;





@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/springContext-test.xml")
public class GoodsServiceTest {

    @Autowired
    private IGoodsService goodsService;

    /*@Autowired
    private GoodsRepository repository;*/



    private GoodsEntity goods = new GoodsEntity();

    @Before
    public void init() {
        goods.setShopId("S160728000001");
        goods.setShopName("创维");
        goods.setGoodsId("LMD01MTV46380");
        goods.setGoodsName("万家乐中华平板电视");
        goods.setParentCategoryId("G1001004");
        goods.setCategoryId("L000007");
        goods.setCategoryName("曲面电视");
        goods.setBrandId("BR000052");
        goods.setBrandName("飞利浦");
        goods.setSalePrice(new BigDecimal(500));
        goods.setPromotionPrice(new BigDecimal(450));
        goods.setGoodsPopularity(10);
       // goods.setGoodsUpDate(new Date(System.currentTimeMillis()));

    }

    @Test
    public void testAddOne() throws EwandianSearchEngineException {
      /*  goodsService.addOne(goods);
        GoodsEntity retVal = repository.findOne(goods.getId());
        assertThat(retVal,is(notNullValue()));*/
    }

    /*@Test
    public void testRemoveOne() throws EwandianSearchEngineException {
        testAddOne();
        goodsService.removeOne(goods);
        GoodsEntity retVal = repository.findOne(goods.getId());
        assertThat(retVal,is(nullValue()));
    }

    @Test
    public void testRemoveAll() throws EwandianSearchEngineException {
    repository.deleteAll();
        assertEquals(repository.count(),0);

    }

    @Test
    public void testFind() throws EwandianSearchEngineException {
        //Page<GoodsEntity> page = goodsService.find(goods);
        //goodsService.findBySelfDefine(goods);
        //goodsService.findAllBySelfDefine(goods);
    }*/


    @Test
    public void testFindOneByShopIdAndGoodsId() throws EwandianSearchEngineException {
       /* goods.setShopId("S160728000001");
        goods.setGoodsId("LMD01MTV46380s");
        goodsService.findOneByShopIdAndGoodsId(goods);*/
    }

    @Test
    public void findGoods() throws EwandianSearchEngineException {
        SearchKeyWord skw = new SearchKeyWord();
        skw.setSearchArg("电视");
        List<String> categoryIds = new ArrayList<String>();
        categoryIds.add("S00B4AX");
        skw.setCategoryIdList(categoryIds);
        List<GoodsEntity> goodsEntityList = goodsService.findGoods(skw);
        for (GoodsEntity g:goodsEntityList){
            System.out.println(g.getGoodsName());
        }
    }

    @Test
    public void re()throws EwandianSearchEngineException {
        /*SearchKeyWord skw = new SearchKeyWord();
        List<String> goodsIds = new ArrayList<String>();
        goodsIds.add("LMD066T7");
        skw.setGoodsIdList(goodsIds);

        List<GoodsEntity> goodsEntityList = goodsService.findGoodsPageByGoodsIdList(skw);
        for (GoodsEntity g:goodsEntityList){
            goodsService.removeOne(g);
        }*/

    }
}

