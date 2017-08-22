package com.ewandian.b2b2c.search.repositories;

import com.ewandian.b2b2c.search.domain.document.ShopEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by suhd on 2016-11-28.
 */
public interface ShopRepository extends PagingAndSortingRepository<ShopEntity,String> {
}
