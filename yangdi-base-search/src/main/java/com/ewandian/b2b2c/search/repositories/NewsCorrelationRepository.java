package com.ewandian.b2b2c.search.repositories;

import com.ewandian.b2b2c.search.domain.document.NewsCorrelationEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2016/12/20.
 */
@Repository
public interface NewsCorrelationRepository extends ElasticsearchRepository<NewsCorrelationEntity, String> {
}