package com.ewandian.b2b2c.search.repositories;

import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.AdsEntity;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2016/12/6.
 */
@Repository
public interface AdsRepository extends ElasticsearchRepository<AdsEntity, String> {
}