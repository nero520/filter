package com.codingapi.filter.core.interceptor;

import com.codingapi.filter.core.interceptor.handler.FilterPreResponseHandler;
import com.codingapi.filter.core.interceptor.handler.def.DefFilterPreResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by lorne on 2017/8/16.
 */

@Configuration
public class FilterPreInterceptorConfigurer extends WebMvcConfigurerAdapter {


    private FilterPreResponseHandler filterPreResponseHandler = null;

    @Autowired
    private ApplicationContext applicationContext;


    public void addInterceptors(InterceptorRegistry registry) {

        if(filterPreResponseHandler==null) {
            try {
                filterPreResponseHandler = applicationContext.getBean(FilterPreResponseHandler.class);
            } catch (Exception e) {
                filterPreResponseHandler = new DefFilterPreResponseHandler();
            }
        }


        InterceptorRegistration filterResponseInterceptor = registry.addInterceptor(new FilterResponseInterceptor(filterPreResponseHandler));
        // 拦截配置
        filterResponseInterceptor.addPathPatterns("/**");

    }

}
