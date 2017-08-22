package com.ewandian.b2b2c.search.service.multithread;

import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.service.IGoodsService;
import com.ewandian.b2b2c.search.service.impl.GoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by suhd on 2016-12-17.
 */
@Service
@Scope("prototype")
public class AddPopularityToGoods implements Runnable {

    private IGoodsService goodsService;

    private Logger logger = LoggerFactory.getLogger(AddPopularityToGoods.class);
    private String goodsId;

    public AddPopularityToGoods newInstance(GoodsService goodsService,String goodsId) {
        this.goodsService = goodsService;
        this.goodsId = goodsId;
        return this;
    }

    public void run() {
        try {
            goodsService.addPopularityToGoods(this.goodsId);
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getMessage());
        }
    }
}
