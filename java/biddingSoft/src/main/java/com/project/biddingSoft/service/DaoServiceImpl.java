/**
 * 
 */
package com.project.biddingSoft.service;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.biddingSoft.BiddingSoftwareApplication;
import com.project.biddingSoft.dao.IRepo;
import com.project.biddingSoft.dao.IStorable;
import com.project.biddingSoft.dao.IUserRepo;
import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.domain.User;

/**
 * @author nuchem
 *
 */
@Service
public class DaoServiceImpl {
	private static final Logger logger = LoggerFactory.getLogger(DaoServiceImpl2.class);

	@Autowired
	private IRepo iRepo;
	@Autowired
	private IUserRepo iUserRepo;
	public String helloService() {
		
 		return "Hello World from Nuchem";
	}
	 
	
	public  boolean persistEntity(IStorable entity ) throws IllegalArgumentException {
 		 
		try {
			 if (entity instanceof Lot) {
				 Lot lot = (Lot) entity;
				    lot = iRepo.save(lot);
			 }
			 else {
				 User user = (User) entity;
			    user = iUserRepo.save(user);
			 }
 			
	 	}
		catch(IllegalArgumentException e) {
			logger.info("Error is " ,e);
			throw e; 
		}
		return true; 
		
	}
	  
//	  public Iterable<IStorable> getAllRecordsForEnt() {
//		  
//	    return iRepo.findAll();
//	  }
//	  
//	  public boolean deleteAllEntities() {
//		  return StreamSupport.stream(getAllRecordsForEnt().spliterator(), false).allMatch(l ->   deleteEntityById( l.getId()) == true);
//	  }
//	  
//	  public boolean deleteEntityById(Long id) throws IllegalArgumentException{
//		  try {
// 		  iRepo.deleteById(id);
//		  }
//		  catch(IllegalArgumentException e) {
//				logger.info("Error is " ,e);
//				throw e; 
//			}
//		 return   iRepo.findById(id).isEmpty(); 
//		  
//	  }
//	
//	  public Optional<IStorable> getEntityById(Long id) {
//		  return iRepo.findById(id);
// 	  }
	  
 }
