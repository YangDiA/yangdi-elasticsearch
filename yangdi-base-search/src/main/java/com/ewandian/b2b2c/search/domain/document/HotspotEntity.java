package com.ewandian.b2b2c.search.domain.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Created by suhd on 2016-12-10.
 */
@Document(indexName = "hotspotindex", type = "hotspot")
public class HotspotEntity {
    @Id
    @Field(
            type = FieldType.String,
            index = FieldIndex.not_analyzed
    )
    private String name;

    @Field(
            type = FieldType.Long,
            index = FieldIndex.not_analyzed
    )
    private Long count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
