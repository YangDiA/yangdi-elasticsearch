package com.ewandian.b2b2c.search.service;

import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.HelpCenterEntity;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;

import java.util.List;

/**
 * Created by Administrator on 2016/12/8.
 */
public interface IHelpCenterService extends ICommonService<HelpCenterEntity> {
    List<HelpCenterEntity> findHelps(SearchKeyWord skw) throws EwandianSearchEngineException;
    HelpCenterEntity findOne(HelpCenterEntity helpCenterEntity) throws EwandianSearchEngineException;
    List<HelpCenterEntity> findHelpsByHelpsList(SearchKeyWord skw) throws EwandianSearchEngineException;
    long getCount(String searchArg) throws EwandianSearchEngineException;
    long findHelpsByColumnIdentifyListCount(SearchKeyWord skw) throws EwandianSearchEngineException;
}