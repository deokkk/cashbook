package com.gdu.cashbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// @SpringBootApplication == @Configuration (application.properties||pom.xml) + @EnableAutoConfiguration + @ComponentScan (@Autowired)
public class CashbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(CashbookApplication.class, args);
	}

}
