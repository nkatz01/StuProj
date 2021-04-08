package com.project.biddingSoft.controller;

import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
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

	 

	@PostMapping(path = "/addent")
	ResponseEntity<Object> addNewEntity(@RequestBody IStorable entity) {
		try {

			daoServiceImpl.persistEntity(entity);
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
		return new ResponseEntity("Object updated successfully", HttpStatus.CREATED);

	}

	@GetMapping(path = "/allents")
	public @ResponseBody Iterable<? extends IStorable> getAllRecords(@RequestBody IStorable entity) {

		return daoServiceImpl.getAllRecordsForEnt(entity);
	}

	@GetMapping(path = "/getent")
	public ResponseEntity<? extends IStorable> getEntity(@RequestBody IStorable entity) {
		Optional<? extends IStorable> results = daoServiceImpl.getEntity(entity);
		if (results.isPresent())
			return ResponseEntity.ok(results.get());
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping(value = "/delent")
	public ResponseEntity<String> deleteEntity(@RequestBody IStorable entity) {
		ResponseEntity<String> response = null;
		try {
			response = daoServiceImpl.deleteEntity(entity) == true
					? ResponseEntity.status(HttpStatus.OK).build()
					: ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
		return response;
	}

	@DeleteMapping(value = "/delallents")
	public ResponseEntity<String> deleteAllEntities(@RequestBody IStorable entity) {
		ResponseEntity<String> response = null;
		try {
			response = daoServiceImpl.deleteAllEntities(entity) == true ? ResponseEntity.status(HttpStatus.OK).build()
					: ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
		return response;
	}

}
