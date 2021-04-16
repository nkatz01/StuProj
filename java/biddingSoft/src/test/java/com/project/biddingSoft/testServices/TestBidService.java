package com.project.biddingSoft.testServices;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.biddingSoft.domain.Bid;
import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.domain.User;

@Component
public class TestBidService {

	private static final double ONEINCR = 5.0;
	private static final double ONE_AND_HALF = 7.5;
	private static final double TWOINCR = 10.0;

	public static double getOneincr() {
		return ONEINCR;
	}

	@Autowired
	TestLotService testLotService;
	@Autowired
	TestUserService testUserService;

	public Bid getOneIncrBid() {
		return new Bid.BidBuilder(testLotService.getMeSimpleLot()).amount(ONEINCR)
				.bidder(testUserService.getMeSimpleBidder()).build();
	}

	public Bid getTwoIncrBid() {
		return new Bid.BidBuilder(testLotService.getMeSimpleLot()).amount(TWOINCR)
				.bidder(testUserService.getMeSimpleBidder()).build();
	}

//	public Optional<Lot> placeSuccessfulOneIncrBid(){
//		Bid bid = getOneIncrBid();
//		
//			return bid.getLot().placeBid(bid);
//		
//	}
//	public Optional<Lot> placeSuccessfulOneIncrBid(Lot lot){
//		Bid bid = getOneIncrBid(lot);
//		return lot.addBid(bid);
//		
//	}
	public Lot bidsLot(Bid bid) {
		return bid.getLot();
	}
	public User bidsUser(Bid bid) {
		return bid.getLot().getUser();
	}
	public User bidsBidder(Bid bid) {
		return bid.getBidder();
	}
	public Bid getOneIncrBid(Lot lot) {
		return new Bid.BidBuilder(lot).amount(lot.getBiddingIncrement() + lot.getHighestBid())
				.bidder(testUserService.getMeSimpleBidder())	.build();
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

	public Bid getBidBumpedUpByOneIncrMore(Lot lot, Bid previousBid) {
		return new Bid.BidBuilder(lot).amount(lot.getBiddingIncrement() + previousBid.getAmount())
				.bidder(testUserService.getMeSimpleBidder()).build();
	}

	public Bid getArbitraryAmountBid(Lot lot, double amount) {
		return new Bid.BidBuilder(lot).amount(amount + lot.getHighestBid()).bidder(testUserService.getMeSimpleBidder())
				.build();
	}

	public double bumpUpOne(Bid bid) {
		return bid.getAmount() + ONEINCR;
	}

	public double bumpOneDown(Bid bid) {
		return bid.getAmount() - ONEINCR;
	}

}
