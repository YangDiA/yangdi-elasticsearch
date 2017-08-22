package com.ewandian.b2b2c.search.service.mq;

import com.alibaba.fastjson.JSONArray;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.GoodsImageEntity;
import com.ewandian.b2b2c.search.service.IGoodsImageService;
import com.ewandian.distributed.mq.kafka.AbstractKafkaConsumer;
import com.ewandian.platform.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/12/19.
 */
@Service
@Scope("prototype")
public class GoodsImageRemoveOneHandler extends AbstractKafkaConsumer {
    @Autowired
    private IGoodsImageService goodsImageService;
    private Logger logger = LoggerFactory.getLogger(GoodsImageRemoveOneHandler.class);

    public void handleMessage(String msg) throws Exception {
        try {
            List<GoodsImageEntity> goodsImageEntityList = JSONArray.parseArray(msg,GoodsImageEntity.class);
            for(int i=0; i<goodsImageEntityList.size(); i++) {
                this.process(goodsImageEntityList.get(i));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    private void process(GoodsImageEntity goodsImageEntity) throws Exception {
        //validating for checking property that should not be empty
        if(goodsImageEntity==null || StringUtil.isNullOrEmpty(goodsImageEntity.getGoodsId()) || StringUtil.isNullOrEmpty(goodsImageEntity.getImgId())) {
            throw new EwandianSearchEngineException("goodsId与imgId 不允许为空！");
        }
        goodsImageService.removeOne(goodsImageEntity);
    }
}
