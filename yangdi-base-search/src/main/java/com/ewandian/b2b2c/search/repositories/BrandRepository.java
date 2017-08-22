package com.ewandian.b2b2c.search.repositories;

import com.ewandian.b2b2c.search.domain.document.BrandEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by suhd on 2016-11-29.
 */
public interface BrandRepository extends PagingAndSortingRepository<BrandEntity,String> {
}
