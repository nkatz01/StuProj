/**
 * 
 */
package com.project.biddingSoft.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

import javax.persistence.Transient;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.lang.reflect.*;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.project.biddingSoft.BiddingSoftwareApplication;
import com.project.biddingSoft.dao.IBidRepo;
import com.project.biddingSoft.dao.ILotRepo;
import com.project.biddingSoft.dao.IStorable;
import com.project.biddingSoft.dao.IUserRepo;
import com.project.biddingSoft.domain.Bid;
import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.domain.Storable;
import com.project.biddingSoft.domain.User;
import com.project.biddingSoft.service.IService;

/**
 * @author nuchem
 *
 */
@Service
@Component
@Qualifier("BidServiceImpl")
public class BidServiceImpl implements IService<Bid> {
	private static final Logger logger = LoggerFactory.getLogger(BidServiceImpl.class);

	@Transient
	@Autowired
	private static ILotRepo iLotRepo;
	@Transient
	@Autowired
	private static IUserRepo iUserRepo;
	@Transient
	@Autowired
	private static IBidRepo iBidRepo;
	@Autowired
	public void setIRepo(IBidRepo ibidRepo) {
		iBidRepo = ibidRepo;
	}
	@Autowired
	public void setILotRepo(ILotRepo ilotrepo) {
		iLotRepo = ilotrepo;
	}
	@Autowired
	public void setIUserRepo(IUserRepo iuserRepo) {
		iUserRepo = iuserRepo;
	}	

	public String persistEntity(Bid bid) {
		StringBuilder stringBuilder = new StringBuilder();
		 
		associatedCreatorWithLot(bid);
		

		if (lotCreatorAndLotAreNew(bid)) {

			iUserRepo.save(bid.getLot().getUser());
			stringBuilder.append(bid.getLot().getUser().getClass().getName() + " " + bid.getLot().getUser().getId()
					+ "\n" + bid.getLot().getClass() + " " + bid.getLot().getId());

		} else if (lotIsNew(bid)) {
			iLotRepo.save(bid.getLot());
			stringBuilder.append("\n" + bid.getLot().getClass().getName() + " " + bid.getLot().getId());
		}
		if (bidderIsNew(bid)) {

			iUserRepo.save(bid.getBidder());
			stringBuilder.append("\n" + bid.getBidder().getClass().getName() + " as Bidder " + bid.getBidder().getId());
		}

		bid.getBidder().addBidToList(bid);// fine (for the purpose of demonstrating persistence) even though bid might
											// not be valid according to business rules.
		iBidRepo.save(bid);// doesn't get saved via cascade, as bid wasn't added to lot.bidList via
							// placeBid() -> addBid().
		stringBuilder.append("\n" + bid.getClass().getName() + " " + bid.getId());
		return stringBuilder.toString();

	}

	public boolean associatedCreatorWithLot(Bid bid) {
		if (bid.getLot().getId() == null) {
			if (!bid.getLot().getUser().createdLotscontainsLot(bid.getLot()))
				return bid.getLot().getUser().addLotToList(bid.getLot());

		}
		return true;
	}	
 		

 	public boolean lotCreatorAndLotAreNew(Bid bid) {
  		return bid.getLot().getId()==null && bid.getLot().getUser().getId()==null ; 
 		 
 		
 	}
 	public boolean lotIsNew(Bid bid) {
 		return bid.getLot().getId()==null; 
 		
 	}
 	public boolean bidderIsNew(Bid bid) {
 		return bid.getBidder().getId()==null;
 		
 	}
	//bidder exists AND					//bidder doesn't exist AND
	//1. both, lot and user exist		//1. both lot and user exist
	//2. both don't exist				//2. both dont' exist
	//3. only user						//. only user

	
	
// 	@Override
//	public Iterable<Bid> getAllRecordsForEnt() {
//
//		return iBidRepo.findAll();
//	}

//	public  boolean deleteAllEntities(Bid bid) {
//		
//			StreamSupport.stream(getAllRecordsForEnt(bid).spliterator(), false)
//					.forEach(u -> iBidRepo.delete(u));
//	
//		return getAllRecordsForEnt(bid).iterator().hasNext() == false;
//	}
//
//	public  boolean deleteEntity(Bid bid)  {
//		
//		iBidRepo.delete(bid);	
//	
//		return iBidRepo.findById(bid.getId()).isEmpty(); 
//
//	}

 
	


}
