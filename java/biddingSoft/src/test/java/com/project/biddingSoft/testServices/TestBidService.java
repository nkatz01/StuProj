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

	

	/**
	 * @param lot
	 * @return a bid with an amount incremented by one biddingIncrement relative to the given lot's highestBid
	 */
	public Bid getOneIncrBid(Lot lot) {
		return new Bid.BidBuilder(lot).amount(lot.getBiddingIncrement() + lot.getHighestBid())
				.bidder(testUserService.getMeSimpleBidder()).build();
	}

	/**
	 * @return a ready "packaged" user, lot and one increment bid with the bid already placed on the lot by some bidder
	 */
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
	
	/**
	 * @param lot
	 * @return a ready "packaged" user, lot and one increment bid with the bid already placed on the <b>lot provided</b>, by some bidder
	 */
	public Bid getTwoIncrBid(Lot lot) {
		return new Bid.BidBuilder(lot).amount((lot.getBiddingIncrement() * 2) + lot.getHighestBid())
				.bidder(testUserService.getMeSimpleBidder()).build();
	}

	public Bid getThreeIncrBid(Lot lot) {
		return new Bid.BidBuilder(lot).amount((lot.getBiddingIncrement() * 3) + lot.getHighestBid())
				.bidder(testUserService.getMeSimpleBidder()).build();
	}
	
	/**
	 * @param lot
	 * @param previousBid
	 * @return a bid, associated with <b>lot</b>, whose amount is increased by one bidding increment relative to <b>previousBid</b>
	 */
	public Bid getBidBumpedUpByOneIncrMore(Lot lot, Bid previousBid) {
		return new Bid.BidBuilder(lot).amount(lot.getBiddingIncrement() + previousBid.getAmount())
				.bidder(testUserService.getMeSimpleBidder()).build();
	}
	
	public Bid getBidBumpedUpByTwoIncrMore(Lot lot, Bid previousBid) {
		return new Bid.BidBuilder(lot).amount((lot.getBiddingIncrement() * 2) + previousBid.getAmount())
				.bidder(testUserService.getMeSimpleBidder()).build();
	}
	/**
	 * @param lot
	 * @param previousBid
	 * @return a bid, associated with <b>lot</b>, whose amount is increased by one bidding increment relative to <b>previousBid</b>
	 *, and whose <b>bidder</b> is provided.
	 */
	public Bid getBidBumpedUpByTwoIncrMore(Lot lot, Bid previousBid, User bidder) {
		return new Bid.BidBuilder(lot).amount((lot.getBiddingIncrement() * 2) + previousBid.getAmount())
				.bidder(bidder).build();
	}
	public Bid getBidBumpedUpByOneIncrMore(Lot lot, Bid previousBid, User bidder) {
		return new Bid.BidBuilder(lot).amount(lot.getBiddingIncrement() + previousBid.getAmount())
				.bidder(bidder).build();
	}


	/**
	 * @param lot
	 * @param amount
	 * @return a bid whose amount is incremented arbitrarily but relative to the <b>lot provided</b>.
	 */
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
