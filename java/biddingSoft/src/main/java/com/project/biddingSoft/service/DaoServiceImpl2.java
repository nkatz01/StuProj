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
import com.project.biddingSoft.dao.ILotRepo;
import com.project.biddingSoft.domain.Storable;
import com.project.biddingSoft.domain.Lot;

/**
 * @author nuchem
 *
 */
@Service
public class DaoServiceImpl2 {
//	private static final Logger logger = LoggerFactory.getLogger(DaoServiceImpl2.class);
//
//	@Autowired
//	private IRepo iRepo; 
//	public String helloService() {
//		
// 		return "Hello World from Nuchem";
//	}
//	
//	public  boolean persistEntity(Storable entity ) throws IllegalArgumentException {
//		// Lot subclass = (Lot) entity.getSubtype() ;
//		//logger.info("dsf "+ subclass.getClass());
//		try {
//			Storable success = iRepo.save(entity);
//	 	}
//		catch(IllegalArgumentException e) {
//			logger.info("Error is " ,e);
//			throw e; 
//		}
//		return true; 
//		
//	}
//	  
//	  public Iterable<Storable> getAllRecordsForEnt() {
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
//	  public Optional<Storable> getEntityById(Long id) {
//		  return iRepo.findById(id);
// 	  }
//	  
 }
