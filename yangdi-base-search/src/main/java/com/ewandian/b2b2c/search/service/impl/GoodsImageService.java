package com.ewandian.b2b2c.search.service.impl;

import com.ewandian.b2b2c.search.app.constant.QueryFieldConstant;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.domain.document.GoodsImageEntity;
import com.ewandian.b2b2c.search.domain.receive.PageInfo;
import com.ewandian.b2b2c.search.repositories.GoodsImageRepository;
import com.ewandian.b2b2c.search.service.IGoodsImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * Created by suhd on 2016-12-01.
 */
@Service
@Scope("prototype")
public class GoodsImageService implements IGoodsImageService {
    @Autowired
    private GoodsImageRepository goodsImageRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public void addOne(GoodsImageEntity goodsImageEntity) throws EwandianSearchEngineException {
        //Modified by XH YU on 2016/12/19
        GoodsImageEntity obj = findOne(goodsImageEntity);
        if(obj==null) {
            goodsImageEntity.setId(UUID.randomUUID().toString());
            goodsImageRepository.save(goodsImageEntity);
        }
    }

    @Override
    public void updateOne(GoodsImageEntity goodsImageEntity) throws EwandianSearchEngineException {
        //Modified by XH YU on 2016/12/19
        GoodsImageEntity obj = findOne(goodsImageEntity);
        if(obj!=null) {
            goodsImageEntity.setId(obj.getId());
            goodsImageRepository.save(goodsImageEntity);
        }
    }

    @Override
    public void removeOne(GoodsImageEntity goodsImageEntity) throws EwandianSearchEngineException {
        //Modified by XH YU on 2016/12/19
        List<GoodsImageEntity> objList = findGoodsImageListByGoodsId(goodsImageEntity);
        for(int i=0; i<objList.size(); i++) {
            goodsImageRepository.delete(objList.get(i).getId());
        }
    }

    @Override
    public List<GoodsImageEntity> findAll(PageInfo pageInfo) throws EwandianSearchEngineException {
        return null;
    }

    @Override
    public long getAllCount() throws EwandianSearchEngineException {
        return 0;
    }

    //Added by XH YU on 2016/12/19, Not sure if one GoodsImageEntity can be decided solely by ImgId
    public GoodsImageEntity findOne(GoodsImageEntity goodsImageEntity) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withIndices("goodsimageindex")
                .withTypes("goodsimage")
                .withQuery(boolQuery()
                        .must(termQuery(QueryFieldConstant.goodsId,goodsImageEntity.getGoodsId()))
                        .must(termQuery(QueryFieldConstant.imgId,goodsImageEntity.getImgId())));
        List<GoodsImageEntity> brandEntityList = elasticsearchTemplate.queryForList(nsqb.build(),GoodsImageEntity.class);
        if(brandEntityList!=null && brandEntityList.size()>0) {
            return brandEntityList.get(0);
        }
        return null;
    }

    public List<GoodsImageEntity> findGoodsImageListByGoodsId(GoodsImageEntity goodsImageEntity) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withIndices("goodsimageindex")
                .withTypes("goodsimage")
                .withQuery(boolQuery()
                        .must(termQuery(QueryFieldConstant.goodsId,goodsImageEntity.getGoodsId())));
        List<GoodsImageEntity> brandEntityList = elasticsearchTemplate.queryForList(nsqb.build(),GoodsImageEntity.class);
        return brandEntityList;
    }
}
