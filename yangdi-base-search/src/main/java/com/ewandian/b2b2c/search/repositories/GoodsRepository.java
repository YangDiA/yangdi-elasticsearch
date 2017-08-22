package com.ewandian.b2b2c.search.repositories;


import com.ewandian.b2b2c.search.domain.document.GoodsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by suhd on 2016-11-21.
 */
public interface GoodsRepository extends PagingAndSortingRepository<GoodsEntity,String> {
    List<GoodsEntity> findAllByGoodsId(String goodsId);
}
