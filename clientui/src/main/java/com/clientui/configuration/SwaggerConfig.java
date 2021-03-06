package com.clientui.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Class which configures Swagger documentation
 *
 * @author Christine Duarte
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * Method which customizes the API documentation of swagger
     *
     * @return An instance of Docket which manage all configurations
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.clientui")).paths(PathSelectors.any()).build();
    }
}
