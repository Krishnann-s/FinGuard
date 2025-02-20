package com.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FinanceManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinanceManagementServiceApplication.class, args);
	}
 
}
