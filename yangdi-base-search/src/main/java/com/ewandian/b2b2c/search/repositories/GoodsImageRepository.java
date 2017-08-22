package com.ewandian.b2b2c.search.repositories;

import com.ewandian.b2b2c.search.domain.document.GoodsImageEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by suhd on 2016-12-01.
 */
public interface GoodsImageRepository extends PagingAndSortingRepository<GoodsImageEntity,String> {
}
