package com.ewandian.b2b2c.search.web.controller;

import com.ewandian.b2b2c.search.app.constant.Status;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineNoDataException;
import com.ewandian.b2b2c.search.domain.document.HotspotEntity;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;
import com.ewandian.b2b2c.search.domain.result.Result;
import com.ewandian.b2b2c.search.service.IHotspotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by suhd on 2016-12-12.
 */
@RestController
@Scope("prototype")
public class HotspotController {
    @Autowired
    private IHotspotService hotspotService;

    private Logger logger = LoggerFactory.getLogger(HotspotController.class);

    @RequestMapping(value = "/search/hotspotPage/all", method = RequestMethod.POST, produces="application/json")
    public Result findAllHotspotPage(@RequestBody SearchKeyWord skw) {
        Result result;
        try {
            List<HotspotEntity> goodsEntityList = hotspotService.findAll(skw.getPageInfo());
            long total = hotspotService.getAllCount();
            long totalPage = (total==0||skw.getPageInfo().getPageSize()==1||skw.getPageInfo().getPageSize()==total)?total/skw.getPageInfo().getPageSize():total/skw.getPageInfo().getPageSize() + 1;
            long page = skw.getPageInfo().getPageNumber();
            long pageCount = skw.getPageInfo().getPageSize();
            result = new Result(Status.OK, "", goodsEntityList, total, totalPage, page, pageCount);
        } catch (EwandianSearchEngineNoDataException e) {
            result = new Result(Status.OK, e.getMessage());
        } catch (EwandianSearchEngineException e) {
            logger.error(e.getStackTrace().toString());
            result = new Result(Status.ERROR, e.getMessage());
        }
        return result;
    }
}
