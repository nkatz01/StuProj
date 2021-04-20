package com.project.biddingSoft.controller;

import org.springframework.boot.builder.SpringApplicationBuilder;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.util.Optional;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.project.biddingSoft.BiddingSoftwareApplication;
import com.project.biddingSoft.dao.IBidRepo;

import com.project.biddingSoft.dao.IStorableRepo;
import com.project.biddingSoft.dao.IUserRepo;
import com.project.biddingSoft.domain.Bid;
import com.project.biddingSoft.domain.BidDTO;
import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.domain.LotDTO;
import com.project.biddingSoft.domain.Storable;
import com.project.biddingSoft.domain.StorableDTO;
import com.project.biddingSoft.domain.User;
import com.project.biddingSoft.domain.UserDTO;
import com.project.biddingSoft.service.BidServiceImpl;
import com.project.biddingSoft.service.IService;
import com.project.biddingSoft.service.LotServiceImpl;
import com.project.biddingSoft.service.UserServiceImpl;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class ServletInitializer extends SpringBootServletInitializer {

	private static final Logger logger = LoggerFactory.getLogger(SpringBootServletInitializer.class);
	@Autowired
	@Qualifier("getUserServiceImpl")
	private IService<Storable> userServiceImpl;
	@Autowired
	@Qualifier("getLotServiceImpl")
	private IService<Storable> lotServiceImpl;
	@Autowired
	@Qualifier("getBidServiceImpl")
	private IService<Storable> bidServiceImpl;

	@Autowired
	private static IStorableRepo<Storable> iStorableRepo;

	@Autowired
	@Qualifier("IStorableRepo")
	public void setIStorable(IStorableRepo istorableRepo) {
		iStorableRepo = istorableRepo;
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BiddingSoftwareApplication.class);
	}

	@RequestMapping(value = "/")
	public ResponseEntity<Object> hello() {

		return new ResponseEntity<>("Service running", HttpStatus.OK);
	}

	@PutMapping(path = "/update")
	ResponseEntity<Object> updateEntity(@RequestBody StorableDTO entity) {
		String message = "";
		if (entity instanceof UserDTO)
			message = userServiceImpl.updateEntity(entity);
		else if (entity instanceof LotDTO)
			message = lotServiceImpl.updateEntity(entity);
		else if (entity instanceof BidDTO)
			message = bidServiceImpl.updateEntity(entity);
		else
			return new ResponseEntity("Entity type doesn't exist", HttpStatus.BAD_REQUEST);

		return new ResponseEntity(message, HttpStatus.OK);
	}

	@PostMapping(path = "/create")
	ResponseEntity<Object> addNewEntity(@RequestBody Storable entity) {
		String message = "";
		try {
			if (entity instanceof User)
				message = userServiceImpl.persistEntity(entity);
			else if (entity instanceof Lot)
				message = lotServiceImpl.persistEntity(entity);
			else {
				message = bidServiceImpl.persistEntity(entity);
			}

		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return new ResponseEntity(message + "\n created successfully", HttpStatus.CREATED);

	}

	@GetMapping(path = "/allents")
	public @ResponseBody ResponseEntity<Iterable<? extends Storable>> getAllRecords() {
		Iterable<? extends Storable> results = iStorableRepo.findAll();
		if (StreamSupport.stream(results.spliterator(), false).count() > 0)
			return ResponseEntity.ok(results);
		else
			return new ResponseEntity("No entities found", HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(path = "/getent/{id}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<? extends Storable> getEntity(@PathVariable Long id) {
		Optional<? extends Storable> results = iStorableRepo.findByEntityId(id);
		if (results.isPresent())
			return ResponseEntity.ok(results.get());
		else
			return new ResponseEntity("Entity not found", HttpStatus.NOT_FOUND);
	}

	@DeleteMapping(path = "/delent/{id}") // model catch/throw
	public ResponseEntity<String> deleteEntity(@PathVariable Long id) {
		ResponseEntity<String> response = null;
		try {
			iStorableRepo.deleteById(id);
			response = new ResponseEntity("Entity successfully deleted", HttpStatus.OK);
		} catch (Exception e) {
			response = new ResponseEntity("Entity not found - " + e.getMessage(), HttpStatus.NOT_FOUND);
		}
		return response;
	}

	@DeleteMapping(path = "/delents")
	public ResponseEntity<String> deleteAllEntities() {
		iStorableRepo.deleteAll();
		return new ResponseEntity("Entities successfully deleted", HttpStatus.OK);
	}

}
