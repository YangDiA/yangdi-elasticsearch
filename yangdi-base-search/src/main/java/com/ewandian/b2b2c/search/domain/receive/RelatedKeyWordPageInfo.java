package com.ewandian.b2b2c.search.domain.receive;

import com.ewandian.b2b2c.search.domain.document.RelatedKeyWordEntity;

/**
 * Created by suhd on 2017-01-02.
 */
public class RelatedKeyWordPageInfo {
    private RelatedKeyWordEntity relatedKeyWordEntity;
    private PageInfo pageInfo = new PageInfo();

    public RelatedKeyWordEntity getRelatedKeyWordEntity() {
        return relatedKeyWordEntity;
    }

    public void setRelatedKeyWordEntity(RelatedKeyWordEntity relatedKeyWordEntity) {
        this.relatedKeyWordEntity = relatedKeyWordEntity;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
}
