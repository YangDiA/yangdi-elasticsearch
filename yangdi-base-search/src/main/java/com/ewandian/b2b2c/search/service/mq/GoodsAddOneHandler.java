package com.ewandian.b2b2c.search.service.mq;

import com.ewandian.b2b2c.search.domain.document.CategoryEntity;
import com.ewandian.b2b2c.search.domain.document.GoodsEntity;
import com.ewandian.b2b2c.search.domain.document.RelatedKeyWordEntity;
import com.ewandian.b2b2c.search.service.ICategoryService;
import com.ewandian.b2b2c.search.service.IGoodsService;
import com.ewandian.b2b2c.search.service.IRelatedKeyWordService;
import com.ewandian.platform.util.StringUtil;
import com.shd.util.validation.ClassValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by suhd on 2016-12-15.
 */
@Service
@Scope("prototype")
public class GoodsAddOneHandler extends SuperHandler<GoodsEntity> {
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IRelatedKeyWordService relatedKeyWordService;

    @Override
    public void handleMessage(String msg) throws Exception {
        super.setClazz(GoodsEntity.class);
        super.handleMessage(msg);
    }

    @Override
    public void process(GoodsEntity goodsEntity) throws Exception {
        //validating for checking property that should not be empty
        goodsEntity.setGoodsUpDate(new Timestamp(System.currentTimeMillis()));
        ClassValidation.validate(goodsEntity);
        setParentCategoryIdToGoodsEntity(goodsEntity);
        //Add relatedKeyWord(including brand, category, brand_category, brand_category_model)
        addRelatedKeyWordToSearchingDocument(goodsEntity.getBrandName(),goodsEntity.getGoodsId(),goodsEntity.getShopId());
        addRelatedKeyWordToSearchingDocument(goodsEntity.getCategoryName(),goodsEntity.getGoodsId(),goodsEntity.getShopId());
        addRelatedKeyWordToSearchingDocument(goodsEntity.getBrandName()+goodsEntity.getCategoryName(),goodsEntity.getGoodsId(),goodsEntity.getShopId());
        addRelatedKeyWordToSearchingDocument(goodsEntity.getBrandName()+goodsEntity.getCategoryName()+goodsEntity.getModel(),goodsEntity.getGoodsId(),goodsEntity.getShopId());
        goodsService.addOne(goodsEntity);
    }

    private void setParentCategoryIdToGoodsEntity(GoodsEntity goodsEntity) throws Exception {
        CategoryEntity parentCategoryEntity = categoryService.findParentCategoryByCategoryId(goodsEntity.getCategoryId());
        if(parentCategoryEntity!=null && StringUtil.isNotNullAndNotEmpty(parentCategoryEntity.getCategoryId())) {
            goodsEntity.setParentCategoryId(parentCategoryEntity.getCategoryId());
        }
    }

    private void addRelatedKeyWordToSearchingDocument(String relatedKeyWord,String goodsId,String shopId) throws Exception {
        RelatedKeyWordEntity relatedKeyWordEntity = new RelatedKeyWordEntity();
        relatedKeyWordEntity.setGoodsId(goodsId);
        relatedKeyWordEntity.setShopId(shopId);
        relatedKeyWordEntity.setRelatedKeyWord(relatedKeyWord);
        relatedKeyWordEntity.setRelatedKeyWordNotAnalyzed(relatedKeyWord);
        ClassValidation.validate(relatedKeyWordEntity);
        relatedKeyWordService.addOne(relatedKeyWordEntity);
    }
}
