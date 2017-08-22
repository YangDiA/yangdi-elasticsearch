package com.ewandian.b2b2c.search.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ewandian.b2b2c.search.app.constant.QueryFieldConstant;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineException;
import com.ewandian.b2b2c.search.app.exception.EwandianSearchEngineNoDataException;
import com.ewandian.b2b2c.search.domain.document.HelpCenterEntity;
import com.ewandian.b2b2c.search.domain.receive.PageInfo;
import com.ewandian.b2b2c.search.domain.receive.SearchKeyWord;
import com.ewandian.b2b2c.search.repositories.HelpCenterRepository;
import com.ewandian.b2b2c.search.service.IHelpCenterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Created by Administrator on 2016/12/8.
 */

@Service
@Scope("prototype")
public class HelpCenterService implements IHelpCenterService {

    private Logger logger = LoggerFactory.getLogger(HelpCenterService.class);

    @Autowired
    private HelpCenterRepository helpCenterRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    public List<HelpCenterEntity> findHelps(SearchKeyWord skw) throws EwandianSearchEngineException{
        try {
            NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder()
                    .withIndices("helpcenterindex")
                    .withTypes("helpcenter")
                    .withQuery(multiMatchQuery(skw.getSearchArg(), QueryFieldConstant.replyId, QueryFieldConstant.typeId, QueryFieldConstant.askContent, QueryFieldConstant.replayContent));

            if(skw.getPageInfo().getSortBy()!=null) {
                searchQuery.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1, skw.getPageInfo().getPageSize(), skw.getPageInfo().getSortDirection(), skw.getPageInfo().getSortBy().getValue()));
            } else {
                searchQuery.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1, skw.getPageInfo().getPageSize()));
            }

            List<HelpCenterEntity> HelpCenterEntityList = elasticsearchTemplate.queryForList(searchQuery.build(), HelpCenterEntity.class);
            if(HelpCenterEntityList==null || HelpCenterEntityList.size()<=0) {
                throw new EwandianSearchEngineNoDataException("No such helps, please try another keyword");
            }
            return HelpCenterEntityList;
        }catch (EwandianSearchEngineNoDataException e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineNoDataException("No such helps, please try another keyword：" + e.getStackTrace().toString());
        }
    }

    public List<HelpCenterEntity> findHelpsByHelpsList(SearchKeyWord skw) throws EwandianSearchEngineException {
        if(skw.getHelpCenterIdList()==null || skw.getHelpCenterIdList().size()<=0) {
            throw new EwandianSearchEngineNoDataException("No search input!");
        }
        List<HelpCenterEntity> resultHelpCenterEntityList;
        try {
            NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                    .withIndices("helpcenterindex")
                    .withTypes("helpcenter")
                    .withQuery(termsQuery(QueryFieldConstant.replyId, skw.getHelpCenterIdList()));

            if(skw.getPageInfo().getSortBy()!=null) {
                nsqb.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1, skw.getPageInfo().getPageSize(), skw.getPageInfo().getSortDirection(), skw.getPageInfo().getSortBy().getValue()));
            } else {
                nsqb.withPageable(new PageRequest(skw.getPageInfo().getPageNumber()-1, skw.getPageInfo().getPageSize()));
            }

            resultHelpCenterEntityList = elasticsearchTemplate.queryForList(nsqb.build(),HelpCenterEntity.class);
            if(resultHelpCenterEntityList==null || resultHelpCenterEntityList.size()<=0) {
                throw new EwandianSearchEngineNoDataException("No such helps!");
            }
        } catch (EwandianSearchEngineNoDataException e) {
            logger.error(e.getMessage());
            throw new EwandianSearchEngineNoDataException(e.getMessage());
        }
        return resultHelpCenterEntityList;
    }


    public List<HelpCenterEntity> findAll(PageInfo pageInfo) throws EwandianSearchEngineException {
        Page<HelpCenterEntity> AdsEntityPage = helpCenterRepository.findAll(new PageRequest(pageInfo.getPageNumber()-1,pageInfo.getPageSize()));
        List<HelpCenterEntity> helpCenterEntityList = new ArrayList<HelpCenterEntity>();
        if(AdsEntityPage!=null && AdsEntityPage.getSize()>0) {
            helpCenterEntityList = AdsEntityPage.getContent();
        }
        return helpCenterEntityList;
    }

    public HelpCenterEntity findOne(HelpCenterEntity helpCenterEntity) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withIndices("helpcenterindex")
                .withTypes("helpcenter")
                .withQuery(termQuery(QueryFieldConstant.replyId,helpCenterEntity.getReplyId()));
        List<HelpCenterEntity> helpsEntityList = elasticsearchTemplate.queryForList(nsqb.build(),HelpCenterEntity.class);
        if(helpsEntityList!=null && helpsEntityList.size()>0) {
            return helpsEntityList.get(0);
        }
        return null;
    }


    public Page<HelpCenterEntity> find(HelpCenterEntity helpCenterEntity) throws EwandianSearchEngineException {
        return null;
    }

    public void addOne(HelpCenterEntity helpCenterEntity) throws EwandianSearchEngineException {
        try {
            helpCenterEntity.setId(UUID.randomUUID().toString());
            helpCenterRepository.save(helpCenterEntity);
        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
            throw new EwandianSearchEngineException("Insert elasticsearch data in helpcenter error：" + e.getStackTrace().toString());
        }
    }

    public void removeOne(HelpCenterEntity helpCenterEntity) throws EwandianSearchEngineException {
    }

    public void updateOne(HelpCenterEntity inputHelp) throws EwandianSearchEngineException {
            inputHelp.setId(UUID.randomUUID().toString());
            helpCenterRepository.save(inputHelp);
    }

    public long getAllCount() throws EwandianSearchEngineException {
        return helpCenterRepository.count();
    }

    public long getCount(String searchArg) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = new NativeSearchQueryBuilder()
                .withIndices("helpcenterindex")
                .withTypes("helpcenter")
                .withQuery(multiMatchQuery(searchArg, QueryFieldConstant.replyId, QueryFieldConstant.typeId, QueryFieldConstant.askContent, QueryFieldConstant.replayContent));
        long count = elasticsearchTemplate.count(nsqb.build(),HelpCenterEntity.class);
        return count;
    }

    public long findHelpsByColumnIdentifyListCount(SearchKeyWord skw) throws EwandianSearchEngineException {
        NativeSearchQueryBuilder nsqb = buildNSQBFByHelpsIdList(skw);
        return elasticsearchTemplate.count(nsqb.build(),HelpCenterEntity.class);
    }

    private NativeSearchQueryBuilder buildNSQBFByHelpsIdList(SearchKeyWord skw) throws EwandianSearchEngineNoDataException {
        if(skw.getHelpCenterIdList()==null || skw.getHelpCenterIdList().size()<=0) {
            throw new EwandianSearchEngineNoDataException("No search input!");
        }
        return new NativeSearchQueryBuilder()
                .withIndices("helpcenterindex")
                .withTypes("helpcenter")
                .withQuery(termsQuery(QueryFieldConstant.replyId, skw.getHelpCenterIdList()));
    }
}
