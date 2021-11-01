package com.mediscreen.microservicereportdiabetes.configuration;

import com.mediscreen.microservicereportdiabetes.exception.CustomErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Class that configure a custom decoder for feign
 *
 * @author Christine Duarte
 */
@Configuration
public class FeignExceptionConfig {

    @Bean
    public CustomErrorDecoder myCustomErrorDecoder() {
        return new CustomErrorDecoder();
    }
}
