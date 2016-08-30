package com.test.config;

import com.test.feign.jaxrs.JAXRSContract;
import feign.Contract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ren.xiaobo on 2016/8/29.
 */
@Configuration
public class FeignClientConfig {
    public FeignClientConfig() {
    }

    @Bean
    public Contract feignContract() {
        return new JAXRSContract();
    }
}
