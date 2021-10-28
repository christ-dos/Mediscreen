package com.mediscreen.microservicereportdiabetes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Class that start the application
 *
 * @author Christine Duarte
 */
@SpringBootApplication
@EnableFeignClients("com.mediscreen.microservicereportdiabetes")
@EnableSwagger2
public class MicroserviceReportDiabetesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceReportDiabetesApplication.class, args);
    }

}
