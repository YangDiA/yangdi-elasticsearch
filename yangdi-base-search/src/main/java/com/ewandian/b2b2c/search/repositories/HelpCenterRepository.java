package com.ewandian.b2b2c.search.repositories;

import com.ewandian.b2b2c.search.domain.document.HelpCenterEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2016/12/8.
 */
@Repository
public interface HelpCenterRepository extends ElasticsearchRepository<HelpCenterEntity, String> {
}