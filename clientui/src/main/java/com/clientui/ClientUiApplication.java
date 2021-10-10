package com.clientui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableFeignClients("com.clientui")
@EnableSwagger2
public class ClientUiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientUiApplication.class, args);
	}

}
