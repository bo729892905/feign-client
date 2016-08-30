package com.test;

import com.test.config.FeignClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by ren.xiaobo on 2016/8/29.
 */
@SpringBootApplication//配置控制
@ComponentScan(basePackages = {"com.test"})//组件扫描
@EnableDiscoveryClient
@EnableFeignClients(defaultConfiguration = { FeignClientConfig.class },basePackages = {"com.test"})
public class Application implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * (non-Javadoc)
     *
     * @see CommandLineRunner#run(String[])
     * <p>
     * 在SpringBoot应用启动之前执行一些代码。比如：<br/>
     * 1. 打印信息<br/>
     * 2. 初始化参数<br/>
     * ...
     */
    @Override
    public void run(String... args) throws Exception {
        logger.info("CommandLineRunner.run()!");
    }
}
