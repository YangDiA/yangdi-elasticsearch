package com.ewandian.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by wonderful on 2016/11/20.
 */
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class BaseTestNG  extends AbstractTestNGSpringContextTests{
    protected MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext wac;
}
