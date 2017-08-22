package com.ewandian.b2b2c.search.repositories;

import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.HotspotEntity;
import com.ewandian.b2b2c.search.domain.receive.PageInfo;
import com.ewandian.b2b2c.search.service.IHotspotService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by suhd on 2016-12-10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/springContext-test.xml")
public class HotspotServiceTest {
    @Autowired
    private IHotspotService hotspotService;

    private HotspotEntity hotspotEntity = new HotspotEntity();

    @Before
    public void init() {
        hotspotEntity.setName("中国2");
        hotspotEntity.setCount(1L);
    }

    @Test
    public void testAddOne() throws EwandianSearchEngineException {
        hotspotService.addOne(hotspotEntity);
    }

    @Test
    public void testUpdateOne() throws EwandianSearchEngineException {
        hotspotService.updateOne(hotspotEntity);
    }

    @Test
    public void testFindAll() throws EwandianSearchEngineException {
        hotspotService.findAll(new PageInfo());
    }

    @Test
    public void testFindOne() throws EwandianSearchEngineException {
        hotspotService.findOne(hotspotEntity.getName());
    }
}
