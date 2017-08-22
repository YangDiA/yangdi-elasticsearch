package com.ewandian.b2b2c.search.repositories;

import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.RelatedKeyWordEntity;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;
import com.ewandian.b2b2c.search.service.IRelatedKeyWordService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by suhd on 2016-11-24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/springContext-test.xml")
public class RelatedWordServiceTest {
    @Autowired
    private IRelatedKeyWordService relatedKeyWordService;

    RelatedKeyWordEntity relatedKeyWordEntity = new RelatedKeyWordEntity();
    SearchKeyWord skw = new SearchKeyWord();

    @Before
    public void init() {
        relatedKeyWordEntity.setRelatedKeyWord("平板电视");
        relatedKeyWordEntity.setRelatedKeyWordNotAnalyzed("平板电视");
        relatedKeyWordEntity.setShopId("平板电视ShopId");
        skw.setSearchArg("老板电视");
    }

    @Test
    public void testAddOne() throws EwandianSearchEngineException {
        relatedKeyWordService.addOne(relatedKeyWordEntity);
    }

    @Test
    public void testFindWithAggCount() throws EwandianSearchEngineException {
        relatedKeyWordService.findWithAggCount(skw);
    }
}
