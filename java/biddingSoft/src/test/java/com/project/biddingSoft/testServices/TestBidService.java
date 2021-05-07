package com.project.biddingSoft.testServices;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.project.biddingSoft.domain.Bid;
import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.domain.User;


@Component
public class TestBidService {

	private static final double ONEINCR = 5.0;


	@Autowired
	TestLotService testLotService;
	@Autowired
	TestUserService testUserService;

	

	public Bid getOneIncrBid(Lot lot) {//returns a Bid with an amount incremented by one biddingIncrement with the base
		//as the given lot's highestBid
		return new Bid.BidBuilder(lot).amount(lot.getBiddingIncrement() + lot.getHighestBid())
				.bidder(testUserService.getMeSimpleBidder()).build();
	}

	public Bid getOneIncrBidAndPlace() {
		Lot lot = testLotService.getMeSimpleLot();
		return lot.placeBid( new Bid.BidBuilder(lot).amount(lot.getBiddingIncrement() + lot.getHighestBid())
				.bidder(testUserService.getMeSimpleBidder()).build()).get().getBid(0);
	}
	public Bid getTwoIncrBidAndPlace() {
		Lot lot = testLotService.getMeSimpleLot();
		return lot.placeBid( new Bid.BidBuilder(lot).amount((lot.getBiddingIncrement() * 2) + lot.getHighestBid())
				.bidder(testUserService.getMeSimpleBidder()).build()).get().getBid(0);
	}
	public Bid getThreeIncrBidAndPlace() {
		Lot lot = testLotService.getMeSimpleLot();
		return lot.placeBid( new Bid.BidBuilder(lot).amount((lot.getBiddingIncrement() * 3) + lot.getHighestBid())
				.bidder(testUserService.getMeSimpleBidder()).build()).get().getBid(0);
	}
	public Bid getTwoIncrBid(Lot lot) {
		return new Bid.BidBuilder(lot).amount((lot.getBiddingIncrement() * 2) + lot.getHighestBid())
				.bidder(testUserService.getMeSimpleBidder()).build();
	}

	public Bid getThreeIncrBid(Lot lot) {
		return new Bid.BidBuilder(lot).amount((lot.getBiddingIncrement() * 3) + lot.getHighestBid())
				.bidder(testUserService.getMeSimpleBidder()).build();
	}

	public Bid getBidBumpedUpByTwoIncrMore(Lot lot, Bid previousBid) {
		return new Bid.BidBuilder(lot).amount((lot.getBiddingIncrement() * 2) + previousBid.getAmount())
				.bidder(testUserService.getMeSimpleBidder()).build();
	}
	public Bid getBidBumpedUpByTwoIncrMore(Lot lot, Bid previousBid, User bidder) {
		return new Bid.BidBuilder(lot).amount((lot.getBiddingIncrement() * 2) + previousBid.getAmount())
				.bidder(bidder).build();
	}

	public Bid getBidBumpedUpByOneIncrMore(Lot lot, Bid previousBid) {
		return new Bid.BidBuilder(lot).amount(lot.getBiddingIncrement() + previousBid.getAmount())
				.bidder(testUserService.getMeSimpleBidder()).build();
	}
	public Bid getBidBumpedUpByOneIncrMore(Lot lot, Bid previousBid, User bidder) {
		return new Bid.BidBuilder(lot).amount(lot.getBiddingIncrement() + previousBid.getAmount())
				.bidder(bidder).build();
	}


	public Bid getArbitraryAmountBid(Lot lot, double amount) {
		return new Bid.BidBuilder(lot).amount(amount + lot.getHighestBid()).bidder(testUserService.getMeSimpleBidder())
				.build();
	}

	public Bid getArbitraryAmountBid(Lot lot, double amount, User bidder) {
		return new Bid.BidBuilder(lot).amount(amount + lot.getHighestBid()).bidder(bidder)
				.build();
	}
	public double bumpUpOne(Bid bid) {
		return bid.getAmount() + ONEINCR;
	}

	public double bumpOneDown(Bid bid) {
		return bid.getAmount() - ONEINCR;
	}

}
