/**
 * 
 */
package com.project.biddingSoft.service;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

import javax.persistence.Transient;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.lang.reflect.*;
import java.time.Clock;
import java.time.Duration;
import java.time.ZoneId;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.biddingSoft.BiddingSoftwareApplication;
import com.project.biddingSoft.dao.ILotRepo;
import com.project.biddingSoft.dao.IStorable;
import com.project.biddingSoft.dao.IUserRepo;
import com.project.biddingSoft.domain.Bid;
import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.domain.Storable;
import com.project.biddingSoft.domain.User;

/**
 * @author nuchem
 *
 */
@Service
@Component
@Qualifier("LotServiceImpl")
public class LotServiceImpl implements IService<Lot> {//
	private static final Logger logger = LoggerFactory.getLogger(LotServiceImpl.class);

	@Transient
	@Autowired
	private static ILotRepo iLotRepo;
	@Transient
	@Autowired
	private static IUserRepo iUserRepo;

	@Autowired
	public void setILotRepo(ILotRepo ilotrepo) {
		iLotRepo = ilotrepo;
	}
	@Autowired
	public void setIUserRepo(IUserRepo iuserRepo) {
		iUserRepo = iuserRepo;
	}

	public String persistEntity(Lot lot) {
		StringBuilder stringBuilder = new StringBuilder();
		if (!lot.getUser().createdLotscontainsLot(lot))
			lot.getUser().addLotToList(lot);

		if (lot.getUser().getId() == null) {
			iUserRepo.save(lot.getUser());
			stringBuilder.append(lot.getUser().getClass().getName() + " " + lot.getUser().getId());

		}
//	 	System.out.println(lot.getId()!=null);
//	 	System.out.println(iUserRepo.findById(lot.getUser().getId()).isPresent());
		// lot.getUser().setId(iUserRepo.findById(lot.getUser().getId()).get().getId());
		else {
			iLotRepo.save(lot);
		}
		stringBuilder.append("\n" + lot.getClass().getName() + " " + lot.getId());//gets saved via cascade, in the first case.
		return stringBuilder.toString();

	}



//	@Override
//	public Iterable<Lot> getAllRecordsForEnt() {
//
//		return iLotRepo.findAll();
//	}

//	public  boolean deleteAllEntities(Lot lot) {
//		
//			StreamSupport.stream(getAllRecordsForEnt(lot).spliterator(), false)
//					.forEach(u -> iLotRepo.delete(u));
//	
//		return getAllRecordsForEnt(lot).iterator().hasNext() == false;
//	}
//
//	public  boolean deleteEntity(Lot lot)  {
//		
//		iLotRepo.delete(lot);	
//	
//		return iLotRepo.findById(lot.getId()).isEmpty(); 
//
//	}
//	@Override
//	public Optional<Lot> getEntity(Long id) {
//		return iLotRepo.findById(id);
//	}
//	@Override
//	public String updateEntity(Storable storable) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	


}
