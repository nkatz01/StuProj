package com.project.biddingSoft;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.service.DaoServiceImpl;

//@Configuration
//@ComponentScan({"com.project.biddingSoft.domain","com.project.biddingSoft.dao","com.project.biddingSoft.controller","com.project.biddingSoft.service"})
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
//@PropertySource("classpath:application.properties")
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
		 
	       // logger.info("Application started with command-line arguments: {} . \n To kill this application, press Ctrl + C.", Arrays.toString(args));
			//Arrays.asList(args).forEach(arg -> logger.info(ANSI_RED+ arg+ANSI_RESET ) ) ;
		  

// 		     String[] allBeanNames = applicationContext.getBeanDefinitionNames();
// 		        for(String beanName : allBeanNames) {
// 		            System.out.println(beanName);	  }
// 		       System.out.println(allBeanNames.length);
 	  }
//	  @Bean
//	    static PropertySourcesPlaceholderConfigurer propertyPlaceHolderConfigurer() {
//	        return new PropertySourcesPlaceholderConfigurer();
//	    }

}
