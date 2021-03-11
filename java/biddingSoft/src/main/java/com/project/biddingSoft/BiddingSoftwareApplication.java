package com.project.biddingSoft;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.biddingSoft.service.DaoServiceImpl2;

//@Configuration
//@EnableJpaRepositories(basePackages = "com.project.biddingSoft.*")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
 
public class BiddingSoftwareApplication implements CommandLineRunner {
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_RESET = "\u001B[0m";
	private static final Logger logger = LoggerFactory.getLogger(BiddingSoftwareApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(BiddingSoftwareApplication.class, args);
		
	}
	  @Override
	    public void run(String...args) throws Exception {
	       // logger.info("Application started with command-line arguments: {} . \n To kill this application, press Ctrl + C.", Arrays.toString(args));
			Arrays.asList(args).forEach(arg -> logger.info(ANSI_RED+ arg+ANSI_RESET ) ) ;

	  }
	

}
