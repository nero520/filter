package com.demo.filter;

//import com.codingapi.filter.zuul.handler.FilterHandlerConfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * Created by lorne on 2017/7/8.
 */


@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
//@Import({ FilterHandlerConfiguration.class})
public class DemoClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoClientApplication.class, args);
    }

}