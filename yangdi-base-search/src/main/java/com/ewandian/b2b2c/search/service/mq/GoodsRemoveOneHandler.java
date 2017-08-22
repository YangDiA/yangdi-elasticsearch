package com.ewandian.b2b2c.search.service.mq;

import com.ewandian.b2b2c.search.domain.document.GoodsEntity;
import com.ewandian.b2b2c.search.domain.document.RelatedKeyWordEntity;
import com.ewandian.b2b2c.search.service.IGoodsService;
import com.ewandian.b2b2c.search.service.IRelatedKeyWordService;
import com.shd.util.validation.ClassValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by suhd on 2016-12-15.
 */
@Service
@Scope("prototype")
public class GoodsRemoveOneHandler extends SuperHandler<GoodsEntity> {
    @Autowired
    private IGoodsService goodsService;
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
        ClassValidation.validate(goodsEntity);
        //Remove relatedKeyWord(including brand, category, brand_category, brand_category_model)
        removeRelatedKeyWordToSearchingDocument(goodsEntity.getBrandName(),goodsEntity.getGoodsId(),goodsEntity.getShopId());
        removeRelatedKeyWordToSearchingDocument(goodsEntity.getCategoryName(),goodsEntity.getGoodsId(),goodsEntity.getShopId());
        removeRelatedKeyWordToSearchingDocument(goodsEntity.getBrandName()+goodsEntity.getCategoryName(),goodsEntity.getGoodsId(),goodsEntity.getShopId());
        removeRelatedKeyWordToSearchingDocument(goodsEntity.getBrandName()+goodsEntity.getCategoryName()+goodsEntity.getModel(),goodsEntity.getGoodsId(),goodsEntity.getShopId());
        goodsService.removeOne(goodsEntity);
    }

    private void removeRelatedKeyWordToSearchingDocument(String relatedKeyWord,String goodsId,String shopId) throws Exception {
        RelatedKeyWordEntity relatedKeyWordEntity = new RelatedKeyWordEntity();
        relatedKeyWordEntity.setGoodsId(goodsId);
        relatedKeyWordEntity.setShopId(shopId);
        relatedKeyWordEntity.setRelatedKeyWord(relatedKeyWord);
        relatedKeyWordEntity.setRelatedKeyWordNotAnalyzed(relatedKeyWord);
        ClassValidation.validate(relatedKeyWordEntity);
        relatedKeyWordService.removeOne(relatedKeyWordEntity);
    }
}
