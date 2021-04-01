package com.project.biddingSoft.testServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.biddingSoft.domain.Bid;

@Component
public class TestBidService {
	
	private static final  double ONEINCR = 5.0;
	public static double getOneincr() {
		return ONEINCR;
	}
	@Autowired
	TestLotService testLotService;
	public Bid getOneIncrBid() {
		return new Bid.BidBuilder(testLotService.getMeSimpleLot()).amount(ONEINCR).build();
	}
		public double bumpUpOne(Bid bid) { 
		 return bid.getAmount() + ONEINCR;
	}
	
	}

