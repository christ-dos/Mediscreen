package com.mediscreen.microsevicehistorypatient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class MicroseviceHistoryPatientApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroseviceHistoryPatientApplication.class, args);
	}

}
