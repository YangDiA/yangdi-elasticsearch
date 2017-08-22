package com.ewandian.b2b2c.search.service;

import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.receive.PageInfo;

import java.util.List;

/**
 * Created by suhd on 2016-11-22.
 */
public interface ICommonService<T> {
    void addOne(T t) throws EwandianSearchEngineException;
    void updateOne(T t) throws EwandianSearchEngineException;
    void removeOne(T t) throws EwandianSearchEngineException;
    List<T> findAll(PageInfo pageInfo) throws EwandianSearchEngineException;
    long getAllCount() throws EwandianSearchEngineException;
}
