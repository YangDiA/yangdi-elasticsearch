package com.ewandian.b2b2c.search.service.impl;

import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineNoDataException;
import com.ewandian.b2b2c.search.domain.document.HotspotEntity;
import com.ewandian.b2b2c.search.domain.receive.PageInfo;
import com.ewandian.b2b2c.search.repositories.HotspotRepository;
import com.ewandian.b2b2c.search.service.IHotspotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Created by suhd on 2016-12-10.
 */
@Service
@Scope("prototype")
public class HotspotService implements IHotspotService {
    @Autowired
    private HotspotRepository hotspotRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public void addOne(HotspotEntity hotspotEntity) throws EwandianSearchEngineException {
        HotspotEntity checkHotspotEntity = hotspotRepository.findOne(hotspotEntity.getName());
        if(checkHotspotEntity==null) {
            hotspotEntity.setCount(1L);
            hotspotRepository.save(hotspotEntity);
        } else {
            checkHotspotEntity.setCount(checkHotspotEntity.getCount()+1L);
            this.updateOne(checkHotspotEntity);
        }
    }

    public void updateOne(HotspotEntity hotspotEntity) throws EwandianSearchEngineException {
        hotspotRepository.save(hotspotEntity);
    }

    public void removeOne(HotspotEntity hotspotEntity) throws EwandianSearchEngineException {
        hotspotRepository.delete(hotspotEntity);
    }

    public List<HotspotEntity> findAll(PageInfo pageInfo) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withIndices("hotspotindex")
                .withTypes("hotspot")
                .withPageable(new PageRequest(pageInfo.getPageNumber()-1,pageInfo.getPageSize(),pageInfo.getSortDirection(),"count"));
        List<HotspotEntity> hotspotEntityList = elasticsearchTemplate.queryForList(nsqb.build(),HotspotEntity.class);
        if(hotspotEntityList==null || hotspotEntityList.size()<=0) {
            throw new EwandianSearchEngineNoDataException("抱歉，没有找到任何搜索结果");
        }
        return hotspotEntityList;
    }

    public long getAllCount() throws EwandianSearchEngineException {
        return hotspotRepository.count();
    }

    public HotspotEntity findOne(String name) throws EwandianSearchEngineException {
        return hotspotRepository.findOne(name);
    }
}
