package com.sunil.employee.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(value = "com.sunil.*")
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = "com.sunil.employee.daoImpl")
public class EmployeeAppApplication extends SpringBootServletInitializer
{
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeAppApplication.class);

	public static void main(String[] args) 
	{
		logger.info("Employee Application is starting");
		SpringApplication.run(EmployeeAppApplication.class, args);
		logger.info("Employee Application is started");
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) 
	{
		return application.sources(EmployeeAppApplication.class);
	}

}
