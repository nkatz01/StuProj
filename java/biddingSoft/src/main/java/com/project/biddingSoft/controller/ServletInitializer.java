package com.project.biddingSoft.controller;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.biddingSoft.BiddingSoftwareApplication;
import com.project.biddingSoft.model.Lot;
import com.project.biddingSoft.service.LotServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
	 
//	 @RequestMapping(value = "/")
//	   public ResponseEntity<String> addLot() {
//		 
//	     return ResponseEntity.ok(lotServiceImpl.helloService());
//	   }
//	 
	 @PostMapping(path="/add") 
	 @ResponseBody String addNewLot(@RequestParam String userName ) {
		return lotServiceImpl.persistLot(userName);
	 }
	  @GetMapping(path="/all")
	  public @ResponseBody Iterable<Lot> getAllLots() {
		  return lotServiceImpl.getAllUsers();
	  }
}
