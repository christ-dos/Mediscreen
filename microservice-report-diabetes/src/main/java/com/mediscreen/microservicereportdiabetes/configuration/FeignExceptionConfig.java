package com.mediscreen.microservicereportdiabetes.configuration;

import com.mediscreen.microservicereportdiabetes.exception.CustomErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignExceptionConfig {

    @Bean
    public CustomErrorDecoder myCustomErrorDecoder(){
        return new CustomErrorDecoder();
    }
}
