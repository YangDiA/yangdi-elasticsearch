package com.ewandian.b2b2c.search.service.mq;

import com.alibaba.fastjson.JSONArray;
import com.ewandian.b2b2c.search.domain.document.CategoryEntity;
import com.ewandian.b2b2c.search.service.ICategoryService;
import com.ewandian.distributed.mq.kafka.AbstractKafkaConsumer;
import com.shd.util.validation.ClassValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/12/19.
 */
@Service
@Scope("prototype")
public class CategoryAddOneHandler extends AbstractKafkaConsumer {
    @Autowired
    private ICategoryService categoryService;
    private Logger logger = LoggerFactory.getLogger(CategoryAddOneHandler.class);

    public void handleMessage(String msg) throws Exception {
        try {
            List<CategoryEntity> categoryEntityList = JSONArray.parseArray(msg, CategoryEntity.class);
            for(int i=0; i<categoryEntityList.size(); i++) {
                this.process(categoryEntityList.get(i));
            }
        }catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    private void process(CategoryEntity categoryEntity) throws Exception {
        //validating for checking property that should not be empty
        ClassValidation.validate(categoryEntity);
        categoryService.addOne(categoryEntity);
    }
}
