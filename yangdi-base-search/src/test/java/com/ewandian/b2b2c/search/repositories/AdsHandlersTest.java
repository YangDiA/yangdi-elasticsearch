package com.ewandian.b2b2c.search.repositories;


import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.AdsEntity;
import com.ewandian.b2b2c.search.service.mq.AdsAddOneHandler;
import com.ewandian.b2b2c.search.service.impl.AdsService;
import com.ewandian.b2b2c.search.service.mq.AdsUpdateOneHandler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by Administrator on 2016/12/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/springContext-test.xml")
public class AdsHandlersTest {

    @Autowired
    private AdsAddOneHandler adsAddOneHandler;

    @Autowired
    private AdsUpdateOneHandler adsUpdateOneHandler;

    @Autowired
    private AdsService adsService;


    @Autowired
    private AdsRepository adsRepository;

    private AdsEntity adsEntity = new AdsEntity();

    @Before
    public void init() {
  /*      adsEntity.setColumnId("Just for test, you know 更新前");
        adsEntity.setColumnIdentify("Just for test, you know 更新前");
        adsEntity.setEntityName("Just for test, you know 更新前");
        adsEntity.setEntityId("Just for test, you know 更新前");
        adsEntity.setWebPageId("Just for test, you know , before update, it should be kept the same");
        adsEntity.setImgId("Just for test, you know 更新前");*/
    }

    @Test
    public void testAddOneAdsEntity() throws EwandianSearchEngineException {
        //String jsonStr = "[{\"articleId\":\"测试这个与众不同 003\"},{\"title\":\"测试与众不同 003\"},{\"content\":\"测试 content 003\"},{\"articleType\":\"测试 articleType 003\"},{\"typeName\":\"测试 typeName 003\"},{\"status\":\"测试 status 003\"}]";
        //adsAddOneHandler.addOneAdsEntity(adsEntity);
        AdsEntity retVal = adsRepository.findOne(adsEntity.getId());
        assertThat(retVal,is(notNullValue()));
        assertThat(retVal.getId(),is(adsEntity.getId()));
    }

    @Test
    public void testAddOneAdsJson() throws EwandianSearchEngineException {
        //String jsonStr = "[{\"Id\":\"测试 Id 003\"},{\"webPageId\":\"测试 webPageId 003\"},{\"columnId\":\"测试 columnId 003\"},{\"columnIdentify\":\"测试 columnIdentify 003\"},{\"entityName\":\"测试 entityName 003\"},{\"entityId\":\"测试 entityId 003\"},{\"imgId\":\"测试 imgId 003\"}]";
        String jsonStr = "[{\"id\":\"测试 id 003\"},{\"webPageId\":\"测试 id 003\"},{\"columnId\":\"测试 columnId 003\"},{\"columnIdentify\":\"测试 columnIdentify 003\"},{\"entityName\":\"测试 entityName 003\"},{\"entityId\":\"测试 entityId 003\"},{\"imgId\":\"测试 imgId 003\"}]";
        //adsAddOneHandler.addOneAdsJson(jsonStr);
/*        AdsEntity retVal = adsRepository.findOne(adsEntity.getId());
        assertThat(retVal,is(notNullValue()));
        assertThat(retVal.getId(),is(adsEntity.getId()));*/
    }

    @Test
    public void testRemoveOneAdsEntity() throws EwandianSearchEngineException {
        //String jsonStr = "{\"Id\":\"AVjY8kX9geVVOWR3ohIu\"}";
        //adsRemoveOneHandler.removeOneAdsEntity(adsEntity);
/*        AdsEntity retVal = adsRepository.findOne(adsEntity.getWebPageId());
        assertThat(retVal,is(nullValue()));*/
    }

    @Test
    public void testRemoveOneAdsJson() throws EwandianSearchEngineException {
        String jsonStr = "{\"id\":\"DEC161202000007\"}";
        //String jsonStr = "[{\"Id\":\"AVjaJHX7geVVOWR3olN4\"},{\"webPageId\":\"测试 webPageId 003\"},{\"columnIdentify\":\"测试 columnIdentify 003\"}]";
        //adsRemoveOneHandler.removeOneAdsJson(jsonStr);
    }

    @Test
    //Id should be kept while other fields will be updated.
    public void testUpdateOneAdsEntity() throws EwandianSearchEngineException {
        //String jsonStr = "[{\"Id\":\"c3260781-7db3-4207-9193-bd543f8783cf\"},{\"articleId\":\"测试更新后articleId002\"},{\"title\":\"测试更新后title002\"},{\"content\":\"测试更新后content002\"},{\"articleType\":\"测试更新后articleType002\"},{\"status\":\"测试更新后status002\"}]";
        //adsUpdateOneHandler.updateOneAdsEntity(adsEntity);
    }

    @Test
    //Id should be kept while other fields will be updated.
    public void testUpdateOneAdsJson() throws EwandianSearchEngineException {
        String jsonStr = "[{\"id\":\"DEC161207000004\"},{\"webPageId\":\"DEC161207000004\"},{\"columnId\":\"更新 columnId\"},{\"columnIdentify\":\"更新 columnIdentify\"},{\"entityName\":\"更新 entityName\"},{\"entityId\":\"更新 entityId\"},{\"imgId\":\"更新 imgId\"}]";
        //adsUpdateOneHandler.updateOneAdsJson(jsonStr);
    }

/*    @Test
    public void testFindByColumnIdentifyAndShopId() throws EwandianSearchEngineException{
        String columnIdentify = "SHOPINDEX_GC01";
        String shopId = "DP1608283903";
        List<AdsEntity> contentPage = adsService.findByColumnIdentifyAndShopId(columnIdentify,shopId);
        System.out.println(contentPage.size());
    }*/

    @Test
    public void removeAll() throws EwandianSearchEngineException {
        try {
            adsRepository.deleteAll();
        } catch (Exception e) {
            throw new EwandianSearchEngineException("Empty elasticsearch data in Ads error：" + e.getStackTrace().toString());
        }
    }
}
