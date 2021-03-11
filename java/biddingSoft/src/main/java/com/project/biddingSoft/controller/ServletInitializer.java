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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.project.biddingSoft.BiddingSoftwareApplication;
import com.project.biddingSoft.dao.IStorable;
import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.service.DaoServiceImpl;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController

public class ServletInitializer extends SpringBootServletInitializer {

	private static final Logger logger = LoggerFactory.getLogger(SpringBootServletInitializer.class);

	@Autowired
	private DaoServiceImpl daoServiceImpl;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BiddingSoftwareApplication.class);
	}

	@RequestMapping(value = "/")
	public ResponseEntity<Object> hello() {

		return new ResponseEntity<>("Service running", HttpStatus.OK);
	}

//	 @RequestMapping(value = "/")
//	   public ResponseEntity<String> addLot() {
//		 
//	     return ResponseEntity.ok(daoServiceImpl.helloService());
//	   }
//	 
//	 @PostMapping(path="/addlot") 
//	ResponseEntity<String> addNewLot(@RequestParam String userName ) {
//		 
//		 try {
//		    daoServiceImpl.persistEntity(userName);
//		 }
//			catch (Exception e) {
//				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
//			}
//			
//		 return ResponseEntity.status(HttpStatus.CREATED).build();
//		   
//			
//				 
//	 }
	@PostMapping(path = "/addent")
	ResponseEntity<Object> addNewEntity(@RequestBody IStorable entity) {

		try {
			
			daoServiceImpl.persistEntity(entity);
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}

		return new ResponseEntity("Object updated successfully", HttpStatus.CREATED);

	}

//	@GetMapping(path = "/allents")
//	public @ResponseBody Iterable<IStorable> getAllRecords() {
//		return daoServiceImpl.getAllRecordsForEnt();
//	}
//
//	@GetMapping(path = "/getent/{id}")
//	public ResponseEntity<IStorable> getEntity(@PathVariable Long id) {
//		if (daoServiceImpl.getEntityById(id).isPresent())
//			return ResponseEntity.ok(daoServiceImpl.getEntityById(id).get());
//		else
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//	}
//
//	@DeleteMapping(value = "/delent/{id}")
//	public ResponseEntity<String> deleteEntity(@PathVariable Long id) {
//		ResponseEntity<String> response = null;
//		try {
//			response = daoServiceImpl.deleteEntityById(id) == true ? ResponseEntity.status(HttpStatus.OK).build()
//					: ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//		} catch (IllegalArgumentException e) {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//		}
//		return response;
//	}
//
//	@DeleteMapping(value = "/delallents")
//	public ResponseEntity<String> deleteAllEntities() {
//		ResponseEntity<String> response = null;
//		try {
//			response = daoServiceImpl.deleteAllEntities() == true ? ResponseEntity.status(HttpStatus.OK).build()
//					: ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//		} catch (IllegalArgumentException e) {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//		}
//		return response;
//	}

}
