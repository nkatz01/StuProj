/**
 * 
 */
package com.project.biddingSoft.service;

import javax.persistence.Transient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.project.biddingSoft.dao.IBidRepo;
import com.project.biddingSoft.dao.ILotRepo;

import com.project.biddingSoft.dao.IUserRepo;
import com.project.biddingSoft.domain.Bid;
import com.project.biddingSoft.domain.BidDTO;
import com.project.biddingSoft.domain.BiddingSoftMapper;
import com.project.biddingSoft.domain.StorableDTO;

/**
 * @author nuchem
 *
 */
@Service
@Component
@Qualifier("BidServiceImpl")
public class BidDaoServiceImpl implements IDaoService<Bid> {

	@Autowired
	private ILotRepo iLotRepo;
	@Autowired
	private IUserRepo iUserRepo;
	@Autowired
	private IBidRepo iBidRepo;

	

	@Autowired
	private BiddingSoftMapper bidMapper;

	@Override
	public String updateEntity(StorableDTO bidDto) {
		String mesg = "" + Bid.class.getName() + " " + bidDto.getId();
		try {
			Bid bid = iBidRepo.findById(bidDto.getId()).get();
			bidMapper.updateBidFromDto((BidDTO) bidDto, bid);
			iBidRepo.save(bid);
			mesg += " successfully updated";
		} catch (Exception e) {
			mesg += " could not be updated: " + e.getMessage();
		}
		return mesg;
	}

	@Override
	public String persistEntity(Bid bid) {
		StringBuilder stringBuilder = new StringBuilder();

		associatCreatorWithLot(bid);

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

		bid.getBidder().addBidToSet(bid);// fine (for the purpose of demonstrating persistence) even though bid might
											// not be valid according to business rules.
		iBidRepo.save(bid);// doesn't get saved via cascade, as bid wasn't added to lot.bidList via
							// placeBid() -> addBid().
		stringBuilder.append("\n" + bid.getClass().getName() + " " + bid.getId());
		return stringBuilder.toString();

	}

	public void associatCreatorWithLot(Bid bid) {
		if (bid.getLot().getId() == null) {
			
			if (!bid.getLot().getUser().createdLotscontainsLot(bid.getLot()))
			bid.getLot().getUser().addLotToSet(bid.getLot());
		}
	}

	public boolean lotCreatorAndLotAreNew(Bid bid) {
		return bid.getLot().getId() == null && bid.getLot().getUser().getId() == null;
	}

	public boolean lotIsNew(Bid bid) {
		return bid.getLot().getId() == null;

	}

	public boolean bidderIsNew(Bid bid) {
		return bid.getBidder().getId() == null;

	}
	// bidder exists AND //bidder doesn't exist AND
	// 1. both, lot and user exist //1. both lot and user exist
	// 2. both don't exist //2. both dont' exist
	// 3. only user //. only user

}
