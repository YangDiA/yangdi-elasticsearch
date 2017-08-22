package com.ewandian.b2b2c.search.service.multithread;

import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.HotspotEntity;
import com.ewandian.b2b2c.search.domain.keyword.RelatedGoodsInfo;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;
import com.ewandian.b2b2c.search.service.IHotspotService;
import com.ewandian.b2b2c.search.service.IRelatedKeyWordService;
import com.ewandian.platform.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by suhd on 2016-12-12.
 */
@Service
@Scope("prototype")
public class AddInfoToHotspot implements Runnable {
    @Autowired
    private IRelatedKeyWordService relatedKeyWordService;
    @Autowired
    private IHotspotService hotspotService;

    private Logger logger = LoggerFactory.getLogger(AddInfoToHotspot.class);
    private SearchKeyWord skw;

    public AddInfoToHotspot setSKW(SearchKeyWord skw) {
        this.skw = skw;
        return this;
    }

    public void run() {
        List<RelatedGoodsInfo> relatedGoodsInfoList = relatedKeyWordService.getRelatedGoodsInfoForAddInfoToHotspot(skw);
        String hotspotName = skw.getSearchArg();
        if(relatedGoodsInfoList!=null && relatedGoodsInfoList.size()>0) {
            hotspotName = relatedGoodsInfoList.get(0).getGoodsName();
        }
        HotspotEntity hotspotEntity = new HotspotEntity();
        hotspotEntity.setName(hotspotName);
        try {
            if (StringUtil.isNotNullAndNotEmpty(hotspotName)){
                hotspotService.addOne(hotspotEntity);
            }
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getMessage());
        }
    }
}
