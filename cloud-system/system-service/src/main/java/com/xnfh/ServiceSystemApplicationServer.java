package com.xnfh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/18
 */


@SpringBootApplication
@EnableJpaRepositories("com.xnfh.cec")
@EntityScan(basePackages = "com.xnfh")
@MapperScan("com.xnfh.cec.repository")
@EnableScheduling
public class ServiceSystemApplicationServer {
    public static void main(String[] args) {
        SpringApplication.run(ServiceSystemApplicationServer.class, args);
    }
}
