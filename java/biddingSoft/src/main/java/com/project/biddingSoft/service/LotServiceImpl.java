/**
 * 
 */
package com.project.biddingSoft.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.biddingSoft.BiddingSoftwareApplication;
import com.project.biddingSoft.dao.ILotRepo;
import com.project.biddingSoft.model.Lot;

/**
 * @author nuchem
 *
 */
@Service
public class LotServiceImpl {
	private static final Logger logger = LoggerFactory.getLogger(LotServiceImpl.class);

	@Autowired
	private ILotRepo iLotRepo; 
	public String helloService() {
		
 		return "Hello World from Nuchem";
	}
	
	public  boolean persistLot(String userName ) throws IllegalArgumentException {
		Lot lot = new Lot(userName);
		 
		try {
	 Lot success = iLotRepo.save(null);
	 	}
		catch(IllegalArgumentException e) {
			logger.info("Error is " ,e);
			throw e; 
		}
		return true; 
		
	}
	  
	  public Iterable<Lot> getAllUsers() {
	    // This returns a JSON or XML with the users
	    return iLotRepo.findAll();
	  }
	  
	  public String deleteLotById(Long id) {
		  iLotRepo.deleteById(id);
		  return "deleted";
	  }
 }
