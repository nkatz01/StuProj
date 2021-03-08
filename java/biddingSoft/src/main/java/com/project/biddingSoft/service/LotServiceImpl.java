/**
 * 
 */
package com.project.biddingSoft.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.biddingSoft.dao.ILotRepo;
import com.project.biddingSoft.model.Lot;

/**
 * @author nuchem
 *
 */
@Service
public class LotServiceImpl {
	@Autowired
	private ILotRepo iLotRepo; 
	public String helloService() {
		
 		return "Hello World from Nuchem";
	}
	
	public  String persistLot(String userName ) {
		Lot lot = new Lot(userName);
		iLotRepo.save(lot);
		return "saved"; 
		
	}
	  
	  public Iterable<Lot> getAllUsers() {
	    // This returns a JSON or XML with the users
	    return iLotRepo.findAll();
	  }
 }
