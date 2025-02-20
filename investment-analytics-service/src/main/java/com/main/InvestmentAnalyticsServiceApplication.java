package com.main;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
//import com.main.service.InvestmentCalculator;

@SpringBootApplication
@EnableFeignClients
public class InvestmentAnalyticsServiceApplication {
 
    public static void main(String[] args) {
        SpringApplication.run(InvestmentAnalyticsServiceApplication.class, args);
        
        // Example usage of InvestmentCalculator
        
    }
}