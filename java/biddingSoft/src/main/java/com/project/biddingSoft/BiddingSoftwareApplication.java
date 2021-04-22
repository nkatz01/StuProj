package com.project.biddingSoft;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;

import com.project.biddingSoft.domain.Lot;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class BiddingSoftwareApplication implements CommandLineRunner { 
	@Autowired
    ApplicationContext applicationContext;
	@Autowired Lot lot;
	 
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_RESET = "\u001B[0m";
	private static final Logger logger = LoggerFactory.getLogger(BiddingSoftwareApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(BiddingSoftwareApplication.class, args);
		
	}
	@Override
    public void run(String...args) throws Exception {
		logger.info(ANSI_RED + "BiddingSoft application running" + ANSI_RESET);
	}

}
