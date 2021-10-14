package com.mediscreen.microsevicehistorypatient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
//@EnableMongoRepositories
public class MicroseviceHistoryPatientApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroseviceHistoryPatientApplication.class, args);
	}

}
