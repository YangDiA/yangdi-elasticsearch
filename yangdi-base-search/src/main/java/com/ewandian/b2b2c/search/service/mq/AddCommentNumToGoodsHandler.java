package com.ewandian.b2b2c.search.service.mq;

import com.ewandian.b2b2c.search.domain.document.GoodsEntity;
import com.ewandian.b2b2c.search.service.IGoodsService;
import com.ewandian.platform.util.StringUtil;
import com.shd.exception.ClassValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by suhd on 2016-12-19.
 */
@Service
@Scope("prototype")
public class AddCommentNumToGoodsHandler extends SuperHandler<GoodsEntity> {
    @Autowired
    private IGoodsService goodsService;

    @Override
    public void handleMessage(String msg) throws Exception {
        super.setClazz(GoodsEntity.class);
        super.handleMessage(msg);
    }

    @Override
    public void process(GoodsEntity goodsEntity) throws Exception {
        if(StringUtil.isNullOrEmpty(goodsEntity.getGoodsId())) {
            throw new ClassValidationException("goodsId 不允许为空");
        }
        goodsService.addCommentNumToGoods(goodsEntity.getGoodsId());
    }
}
