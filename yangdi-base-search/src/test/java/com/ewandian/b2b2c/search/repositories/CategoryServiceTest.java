package com.ewandian.b2b2c.search.repositories;

import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.CategoryEntity;
import com.ewandian.b2b2c.search.domain.receive.PageInfo;
import com.ewandian.b2b2c.search.service.ICategoryService;
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
public class CategoryServiceTest {
    @Autowired
    private ICategoryService categoryService;

    private CategoryEntity categoryEntity = new CategoryEntity();

    @Before
    public void init() {
        categoryEntity.setCategoryId("L0000071");
        categoryEntity.setCategoryName("平板电视1");
        categoryEntity.setParentId("G1001004");
        categoryEntity.setIsFinalStage("Y");
        categoryEntity.setImgId("2347b3e589551fd28fd9182f4665de101");
    }

    @Test
    public void testAddOne() throws EwandianSearchEngineException {
        categoryService.addOne(categoryEntity);
    }

    @Test
    public void testGetSibling() throws EwandianSearchEngineException {
        categoryService.getSibling("L000007",new PageInfo());
    }
}
