package com.ewandian.b2b2c.search.service;

import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.HotspotEntity;

/**
 * Created by suhd on 2016-12-10.
 */
public interface IHotspotService extends ICommonService<HotspotEntity> {
    HotspotEntity findOne(String name) throws EwandianSearchEngineException;
}
