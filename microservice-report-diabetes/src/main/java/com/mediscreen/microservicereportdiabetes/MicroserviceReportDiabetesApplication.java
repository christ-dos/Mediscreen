package com.mediscreen.microservicereportdiabetes;

import com.mediscreen.microservicereportdiabetes.service.ReportDiabetesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableFeignClients("com.mediscreen.microservicereportdiabetes")
@EnableSwagger2
public class MicroserviceReportDiabetesApplication implements CommandLineRunner {
	@Autowired
	private ReportDiabetesService reportDiabetesService;

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceReportDiabetesApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
