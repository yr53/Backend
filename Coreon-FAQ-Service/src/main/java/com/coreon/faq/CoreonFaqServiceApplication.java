package com.coreon.faq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.coreon.faq.config.OpenAiProperties;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableConfigurationProperties(OpenAiProperties.class)
@MapperScan("com.coreon.faq.mapper")
public class CoreonFaqServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreonFaqServiceApplication.class, args);
    }
}
