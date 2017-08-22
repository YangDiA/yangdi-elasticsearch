package com.ewandian.b2b2c.search.repositories;

import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.BrandEntity;
import com.ewandian.b2b2c.search.domain.receive.PageInfo;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;
import com.ewandian.b2b2c.search.service.IBrandService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suhd on 2016-11-29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/springContext-test.xml")
public class BrandServiceTest {
    @Autowired
    private IBrandService brandService;

    private BrandEntity brandEntity = new BrandEntity();

    @Before
    public void init() {
        brandEntity.setBrandId("unusedBrandId");
        brandEntity.setBrandName("unusedBrandName");
        brandEntity.setAddrName("F");
        brandEntity.setImgId("7d32a39e914fca931e555d38eeafa74c2");
    }

    @Test
    public void testAddOne() throws EwandianSearchEngineException {
        brandService.addOne(brandEntity);
    }

    @Test
    public void testGetSibling() throws EwandianSearchEngineException {
        brandService.getSibling("BR000052","L000007",new PageInfo());
    }

    @Test
    public void testRemoveOne() throws EwandianSearchEngineException {
        brandService.removeOne(brandEntity);
    }

    @Test
    public void findBrandsPageBySearchArg() throws EwandianSearchEngineException {
        SearchKeyWord skw = new SearchKeyWord();
        skw.setSearchArg("电视");
        List<String> categoryIds = new ArrayList<String>();
        //categoryIds.add("S00B4AX");
        skw.setCategoryIdList(categoryIds);
        List<BrandEntity> brandEntityList = brandService.findBrandsPageBySearchArg(skw);
        for (BrandEntity b: brandEntityList){
            System.out.println(b.getBrandId()+b.getBrandName());
        }

    }
}
