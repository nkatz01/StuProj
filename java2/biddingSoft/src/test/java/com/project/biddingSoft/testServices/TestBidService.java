package com.project.biddingSoft.testServices;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.biddingSoft.domain.Bid;
import com.project.biddingSoft.domain.Lot;

@Component
public class TestBidService {
	
	private static final  double ONEINCR = 5.0;
	private static final  double ONE_AND_HALF = 7.5;
	private static final  double TWOINCR = 10.0;
	public static double getOneincr() {
		return ONEINCR;
	}
	@Autowired
	TestLotService testLotService;
	@Autowired
	TestUserService testUserService; 
	public Bid getOneIncrBid() {
		return new Bid.BidBuilder(testLotService.getMeSimpleLot()).amount(ONEINCR).bidder(testUserService.getMeSimpleBidder()).build();
	}
	
	public Optional<Lot> placeSuccessfulOneIncrBid(){
		Bid bid = getOneIncrBid();
		
			return bid.getLot().addBid(bid);
		
	}
//	public Optional<Lot> placeSuccessfulOneIncrBid(Lot lot){
//		Bid bid = getOneIncrBid(lot);
//		return lot.addBid(bid);
//		
//	}
	public Bid getOneIncrBid(Lot lot) {
		return new Bid.BidBuilder(lot).amount(ONEINCR).bidder(testUserService.getMeSimpleBidder()).build();
	}
	public Bid getTwoIncrBid(Lot lot) {
		return new Bid.BidBuilder(lot).amount(TWOINCR).bidder(testUserService.getMeSimpleBidder()).build();
	}
		public double bumpUpOne(Bid bid) { 
		 return bid.getAmount() + ONEINCR;
	}
	
	}

