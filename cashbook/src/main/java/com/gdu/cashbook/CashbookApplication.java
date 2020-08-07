package com.gdu.cashbook;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@SpringBootApplication
// @SpringBootApplication == @Configuration (application.properties||pom.xml) + @EnableAutoConfiguration + @ComponentScan (@Autowired)
@PropertySource("classpath:google.properties")
public class CashbookApplication {
	@Value("${google.username}")
	public String username;
	@Value("${google.password}")
	public String password;
	public static void main(String[] args) {
		SpringApplication.run(CashbookApplication.class, args);
	}

	@Bean 
	public JavaMailSender getJavamailSender() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost("smtp.gmail.com"); // 메일서버 이름
		javaMailSender.setPort(587);
		//System.out.println(username); // google.properties 파일로 대체
		//System.out.println(password);
		javaMailSender.setUsername(username);
		javaMailSender.setPassword(password);
		Properties prop = new Properties();
		prop.setProperty("mail.smtp.auth", "true");
		prop.setProperty("mail.smtp.starttls.enable", "true");
		javaMailSender.setJavaMailProperties(prop);
		return javaMailSender;
	}
	
	
}
