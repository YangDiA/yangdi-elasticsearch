package com.ewandian.b2b2c.search.repositories;

import com.ewandian.b2b2c.search.domain.document.HotspotEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by suhd on 2016-12-10.
 */
public interface HotspotRepository extends PagingAndSortingRepository<HotspotEntity,String> {
}
