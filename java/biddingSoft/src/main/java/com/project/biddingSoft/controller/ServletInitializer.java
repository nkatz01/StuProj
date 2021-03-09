package com.project.biddingSoft.controller;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.project.biddingSoft.BiddingSoftwareApplication;
import com.project.biddingSoft.model.Lot;
import com.project.biddingSoft.service.LotServiceImpl;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
 
public class ServletInitializer extends SpringBootServletInitializer {
	
	private static final Logger logger = LoggerFactory.getLogger(SpringBootServletInitializer.class);

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
	ResponseEntity<String> addNewLot(@RequestParam String userName ) {
		 
		 try {
		    lotServiceImpl.persistLot(userName);
		 }
			catch (Exception e) {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
			}
			
		 return ResponseEntity.status(HttpStatus.CREATED).build();
		   
			
				 
	 }
	  @GetMapping(path="/all")
	  public @ResponseBody Iterable<Lot> getAllLots() {
		  return lotServiceImpl.getAllUsers();
	  }
	  
	  @DeleteMapping(path="/id")
	  public @ResponseBody String deleteLot(@RequestParam Long id)  {
		return lotServiceImpl.deleteLotById(id);
		  
	  }
}
