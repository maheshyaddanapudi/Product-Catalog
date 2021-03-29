package com.my.retail.catalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@CrossOrigin("*")
public class CatalogApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(CatalogApplication.class);
		application.run(args);
	}
}
