package com.ewandian.b2b2c.search.repositories;

import com.ewandian.b2b2c.search.domain.document.NewsEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
/**
 * Created by Administrator on 2016/12/6.
 */
@Repository
public interface NewsRepository extends ElasticsearchRepository<NewsEntity, String> {
}