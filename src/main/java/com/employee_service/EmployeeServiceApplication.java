package com.employee_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//import org.springframework.cloud.openfeign.FeignClient;

@SpringBootApplication
//@EnableFeignClients
//@EnableDiscoveryClient
public class EmployeeServiceApplication 
{

	public static void main(String[] args) 
	{
	SpringApplication.run(EmployeeServiceApplication.class, args);
	}

}
 