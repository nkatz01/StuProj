package com.project.biddingSoft.controller;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.biddingSoft.BiddingSoftwareApplication;
import com.project.biddingSoft.service.LotServiceImpl;

@RestController
 
public class ServletInitializer extends SpringBootServletInitializer {
	@Autowired
	private LotServiceImpl lotServiceImpl;
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BiddingSoftwareApplication.class);
	}

	 @RequestMapping(value = "/")
	   public ResponseEntity<String> hello() {
		 
	     return ResponseEntity.ok(lotServiceImpl.helloService());
	   }
}
