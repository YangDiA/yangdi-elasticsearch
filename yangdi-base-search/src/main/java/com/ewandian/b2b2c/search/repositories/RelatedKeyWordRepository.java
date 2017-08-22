package com.ewandian.b2b2c.search.repositories;

import com.ewandian.b2b2c.search.domain.document.RelatedKeyWordEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by suhd on 2016-11-24.
 */
public interface RelatedKeyWordRepository extends PagingAndSortingRepository<RelatedKeyWordEntity,String> {
}
