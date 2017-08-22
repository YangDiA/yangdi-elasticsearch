package com.ewandian.b2b2c.search.service.mq;

import com.alibaba.fastjson.JSONArray;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineNoDataException;
import com.ewandian.b2b2c.search.domain.document.GoodsEntity;
import com.ewandian.b2b2c.search.domain.document.RelatedKeyWordEntity;
import com.ewandian.b2b2c.search.domain.document.ShopEntity;
import com.ewandian.b2b2c.search.domain.receive.GoodsPageInfo;
import com.ewandian.b2b2c.search.domain.receive.PageInfo;
import com.ewandian.b2b2c.search.domain.receive.RelatedKeyWordPageInfo;
import com.ewandian.b2b2c.search.service.IGoodsService;
import com.ewandian.b2b2c.search.service.IRelatedKeyWordService;
import com.ewandian.b2b2c.search.service.IShopService;
import com.ewandian.distributed.mq.kafka.AbstractKafkaConsumer;
import com.shd.util.validation.ClassValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/19.
 */
@Service
@Scope("prototype")
public class ShopRemoveOneHandler extends AbstractKafkaConsumer {
    @Autowired
    private IShopService shopService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private IRelatedKeyWordService relatedKeyWordService;

    private Logger logger = LoggerFactory.getLogger(ShopRemoveOneHandler.class);

    public void handleMessage(String msg) throws Exception {
        try {
            List<ShopEntity> shopEntityList = JSONArray.parseArray(msg,ShopEntity.class);
            for(int i=0; i<shopEntityList.size(); i++) {
                this.process(shopEntityList.get(i));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    private void process(ShopEntity shopEntity) throws Exception {
        //validating for checking property that should not be empty
        ClassValidation.validate(shopEntity);
        //Call goodsService to drop all its goods from elasticsearch server
        removeGoodsEntityByShopId(shopEntity.getShopId());
        //Call relatedKeyWordService to drop shop related relatedkeywordindex from elasticsearch server
        removeRelatedKeyWordEntityByShopId(shopEntity.getShopId());
        shopService.removeOne(shopEntity);
    }

    private void removeGoodsEntityByShopId(String shopId) throws Exception {
        GoodsEntity goodsEntity = new GoodsEntity();
        goodsEntity.setShopId(shopId);
        PageInfo pageInfo = new PageInfo(1,9999);
        GoodsPageInfo goodsPageInfo = new GoodsPageInfo();
        goodsPageInfo.setGoodsEntity(goodsEntity);
        goodsPageInfo.setPageInfo(pageInfo);
        List<GoodsEntity> goodsEntityList = new ArrayList<GoodsEntity>();
        try {
            goodsEntityList = goodsService.findPageListByGoodsEntity(goodsPageInfo);
        } catch (EwandianSearchEngineNoDataException e) {
            //if it is NoDataException then do nothing
        }
        for(int i=0; i<goodsEntityList.size(); i++) {
            goodsService.removeOne(goodsEntityList.get(i));
        }
    }

    private void removeRelatedKeyWordEntityByShopId(String shopId) throws Exception {
        RelatedKeyWordEntity relatedKeyWordEntity = new RelatedKeyWordEntity();
        relatedKeyWordEntity.setShopId(shopId);
        PageInfo pageInfo = new PageInfo(1,9999);
        RelatedKeyWordPageInfo relatedKeyWordPageInfo = new RelatedKeyWordPageInfo();
        relatedKeyWordPageInfo.setRelatedKeyWordEntity(relatedKeyWordEntity);
        relatedKeyWordPageInfo.setPageInfo(pageInfo);
        List<RelatedKeyWordEntity> relatedKeyWordEntityList = new ArrayList<RelatedKeyWordEntity>();
        try {
            relatedKeyWordEntityList = relatedKeyWordService.findRelatedKeyWordEntityPageByShopId(relatedKeyWordPageInfo);
        } catch (EwandianSearchEngineNoDataException e) {
            //if it is NoDataException then do nothing
        }
        for(int i=0; i<relatedKeyWordEntityList.size(); i++) {
            relatedKeyWordService.removeOne(relatedKeyWordEntityList.get(i));
        }
    }
}