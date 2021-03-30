package com.project.biddingSoft.testServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.biddingSoft.domain.Bid;

@Component
public class TestBidService {
	
	private static final  double ONEINCR = 5.0;
	@Autowired
	TestLotService testLotService;
	public Bid getOneIncrBid() {
		return new Bid.BidBuilder(testLotService.getMeSimpleLot()).amount(ONEINCR).build();
	}
		public double bumpUpOne(Bid bid) {
		 bid.setAmount(bid.getAmount()+ ONEINCR);
		 return bid.getAmount();
	}
	
	}

