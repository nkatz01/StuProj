package com.project.biddingSoft.controller;

import org.springframework.boot.builder.SpringApplicationBuilder;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.util.Optional;

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
import com.project.biddingSoft.dao.IStorable;
import com.project.biddingSoft.dao.IStorableRepo;
import com.project.biddingSoft.dao.IUserRepo;
import com.project.biddingSoft.domain.Bid;
import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.domain.Storable;
import com.project.biddingSoft.domain.User;
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
	private IService<IStorable>  userServiceImpl;
	@Autowired
	@Qualifier("getLotServiceImpl")
	private IService<IStorable> lotServiceImpl; 
	@Autowired
	@Qualifier("getBidServiceImpl")
	private IService<IStorable> bidServiceImpl; 
	@Autowired
	private static IStorableRepo<Storable> iStorableRepoUser;
	@Autowired
	@Qualifier("IUserRepo")
	public void setIStorableRepoUser(IStorableRepo istorableRepo) {
		iStorableRepoUser = istorableRepo;
	}
	@Autowired	
	private static IStorableRepo<Storable> iStorableRepoLot;
	@Autowired
	@Qualifier("ILotRepo")
	public void setIStorableLot(IStorableRepo istorableRepo) {
		iStorableRepoLot = istorableRepo;
	}
	@Autowired
	private static IStorableRepo<Storable> iStorableRepoBid;
	@Autowired
	@Qualifier("IBidRepo")
	public void setIStorableBid(IStorableRepo istorableRepo) {
		iStorableRepoBid = istorableRepo;
	}
	
//	@Autowired
//	@Qualifier("IUserRepo")
//	private  IStorableRepo<Storable> iUserRepo;
//	
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BiddingSoftwareApplication.class);
	}

	@RequestMapping(value = "/")
	public ResponseEntity<Object> hello() {

		return new ResponseEntity<>("Service running", HttpStatus.OK);
	}

	@PutMapping(path="/update")
	ResponseEntity<Object> updateNewEntity(@RequestBody Storable entity){
		String message ="";
		if (entity instanceof User)
		message =	userServiceImpl.update(iStorableRepoUser, entity);
		else if (entity instanceof Lot)
			message =	lotServiceImpl.update(iStorableRepoLot, entity);
		else if (entity instanceof Bid)
			message =	bidServiceImpl.update(iStorableRepoBid, entity);
		else 
			 return new ResponseEntity("Entity type doesn't exist", HttpStatus.BAD_REQUEST);
		
		return new ResponseEntity(message + "\n created successfully", HttpStatus.CREATED);
	}

	@PostMapping(path = "/create")
	ResponseEntity<Object> addNewEntity(@RequestBody IStorable entity) {
		String message ="";
		try {
			if(entity instanceof User)
				message =	userServiceImpl.persistEntity(entity);
			else if (entity instanceof Lot)
			message =	lotServiceImpl.persistEntity(entity);
			else {
				message =	bidServiceImpl.persistEntity(entity);}
				
			//iService.persistEntity(entity);
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return new ResponseEntity(message + "\n created successfully", HttpStatus.CREATED);

	}

	@GetMapping(path = "/allents/{tableName}" )
	public @ResponseBody  ResponseEntity<Iterable<? extends IStorable>> getAllRecords(@PathVariable String tableName) {
		Iterable<? extends IStorable> results;
		if(tableName.equals("user")) 
			results =	 userServiceImpl.getAllRecordsForEnt(iStorableRepoUser);
		 else if (tableName.equals("lot"))
			 results = lotServiceImpl.getAllRecordsForEnt(iStorableRepoLot);
		 else if (tableName.equals("bid"))
			 results = bidServiceImpl.getAllRecordsForEnt(iStorableRepoBid);
		 else
			 return new ResponseEntity("Entity type doesn't exist", HttpStatus.BAD_REQUEST);
		
		return  ResponseEntity.ok(results);
	}

	@RequestMapping(path = "/getent/{tableName}/{id}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<? extends IStorable> getEntity(@PathVariable    Long id, @PathVariable  String tableName) {
		Optional<? extends IStorable> results;
		if(tableName.equals("user")) 
		{
			results =	 userServiceImpl.getEntity(iStorableRepoUser, id);
		}
		 else if (tableName.equals("lot"))
			 results = lotServiceImpl.getEntity(iStorableRepoLot, id);
		 else if (tableName.equals("bid"))
			 results = bidServiceImpl.getEntity(iStorableRepoBid, id);
		 else
			 return new ResponseEntity("Entity type doesn't exist",HttpStatus.BAD_REQUEST);
		
		if (results.isPresent())
			return ResponseEntity.ok(results.get());
		else
			return new ResponseEntity("Entity not found", HttpStatus.NOT_FOUND);
	}
	

//	@DeleteMapping(value = "/delent")
//	public ResponseEntity<String> deleteEntity(@RequestBody IStorable entity) {
//		ResponseEntity<String> response = null;
//		try {
//			response = daoServiceImpl.deleteEntity(entity) == true
//					? ResponseEntity.status(HttpStatus.OK).build()
//					: ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//		} catch (IllegalArgumentException e) {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//		}
//		return response;
//	}
//
//	@DeleteMapping(value = "/delallents")
//	public ResponseEntity<String> deleteAllEntities(@RequestBody IStorable entity) {
//		ResponseEntity<String> response = null;
//		try {
//			response = daoServiceImpl.deleteAllEntities(entity) == true ? ResponseEntity.status(HttpStatus.OK).build()
//					: ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//		} catch (IllegalArgumentException e) {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//		}
//		return response;
//	}

}
