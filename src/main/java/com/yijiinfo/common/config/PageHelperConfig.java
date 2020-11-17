package com.yijiinfo.common.config;

import com.github.pagehelper.PageInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class PageHelperConfig {

    @Bean
    PageInterceptor pageInterceptor(){
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("offsetAsPageNum","false");
        properties.setProperty("rowBoundsWithCount","false");
        properties.setProperty("pageSizeZero","true");
        properties.setProperty("reasonable","false");
        properties.setProperty("supportMethodsArguments","false");
        properties.setProperty("returnPageInfo","none");
        properties.setProperty("autoRuntimeDialect","true");
        pageInterceptor.setProperties(properties);
        return pageInterceptor;
    }

}