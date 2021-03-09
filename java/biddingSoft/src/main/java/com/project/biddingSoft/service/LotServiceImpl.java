/**
 * 
 */
package com.project.biddingSoft.service;
import java.util.Optional;

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
	 Lot success = iLotRepo.save(lot);
	 	}
		catch(IllegalArgumentException e) {
			logger.info("Error is " ,e);
			throw e; 
		}
		return true; 
		
	}
	  
	  public Iterable<Lot> getAllUsers() {
		  
	    return iLotRepo.findAll();
	  }
	  
	  public boolean deleteLotById(Long id) throws IllegalArgumentException{
		  try {
 		  iLotRepo.deleteById(id);
		  }
		  catch(IllegalArgumentException e) {
				logger.info("Error is " ,e);
				throw e; 
			}
		 return   iLotRepo.findById(id).isEmpty(); 
		  
	  }
	
	  public Optional<Lot> getLotById(Long id) {
		  return iLotRepo.findById(id);
		//return   lot.isPresent() ? lot : new L("dsf");
	  }
	  
 }
