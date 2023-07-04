package com.cmc.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Client;

@Configuration
@EnableFeignClients(basePackages = "com.cmc")
public class KakaoFeignConfiguration {

    @Bean
    public Client feignClient() {
        return new Client.Default(null, null);
    }
}
