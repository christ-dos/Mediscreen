package com.mediscreen.microsevicehistorypatient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Class that start the application
 *
 * @author Christine Duarte
 */
@SpringBootApplication
@EnableSwagger2
public class MicroseviceHistoryPatientApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroseviceHistoryPatientApplication.class, args);
    }

}
