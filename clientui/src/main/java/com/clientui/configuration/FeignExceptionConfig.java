package com.clientui.configuration;

import com.clientui.exception.CustomErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignExceptionConfig {

    @Bean
    public CustomErrorDecoder myCustomErrorDecoder(){
        return new CustomErrorDecoder();
    }
}
