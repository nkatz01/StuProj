package com.project.biddingSoft.unitTests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.biddingSoft.dao.IBidRepo;
import com.project.biddingSoft.dao.ILotRepo;
import com.project.biddingSoft.dao.IUserRepo;
import com.project.biddingSoft.domain.Bid;
import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.domain.User;
import com.project.biddingSoft.service.ExceptionsCreateor;
import com.project.biddingSoft.testServices.TestBidService;
import com.project.biddingSoft.testServices.TestLotService;
import com.project.biddingSoft.testServices.TestStorableService;
import com.project.biddingSoft.testServices.TestUserService;
import com.rits.cloning.Cloner;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest
@RunWith(JUnitPlatform.class)
@TestInstance(Lifecycle.PER_CLASS)
public class UnitTests {

	private Cloner cloner = new Cloner();

	@Autowired
	private TestLotService testLotService;
	@Autowired
	private TestBidService testBidService;
	@Autowired
	private TestUserService testUserService;

	@Autowired
	private IUserRepo iUserRepo;
	@Autowired
	private ILotRepo iLotRepo;
	@Autowired
	private IBidRepo iBidrepo;
	@Autowired
	private TestStorableService strblService;

	private static final double ONEINCR = 5.0;
	private static final double TWOINCR = 10.0;
	private static final double THREEINCR = 15.0;

	@Test
	void testEqualityOnUsers() throws IllegalAccessException {
		User user1 = testUserService.getMeSimpleUser();
		User user2 = testUserService.getMeSimpleUser();
		assertNotEquals(user1, user2);
		user2 = cloner.deepClone(user1);
		assertFalse(user1 == user2);
		assertEquals(user1, user2);

		user2 = cloner.deepClone(user1);
		iUserRepo.save(user1);
		assertEquals(user1, user2);// even though id is now different for the two
		user2.setUsername("anotherName");
		assertNotEquals(user1, user2);
		user2.setBusinessId(strblService.newUUID());// if we don't do this, the next line would throw an error
		iUserRepo.save(user2); // as the two are still equal in the eyes of the dtbs
		iUserRepo.delete(user2);
		assertEquals(iUserRepo.findById(user2.getId()), Optional.empty());
	}

	@Test
	void testEqualityOnLots() {
		Lot lot1 = testLotService.getMeSimpleLot();// returns a lot packed with a user
		Lot lot2 = testLotService.getMeSimpleLot();
		assertNotEquals(lot1, lot2);
		lot2 = cloner.deepClone(lot1);
		assertEquals(lot1, lot2);
	}

	@Test
	void testEqualityOnBids() {
		Bid bid = testBidService.getOneIncrBidAndPlace();// returns a bid packed with a lot and user
		Bid bid2 = testBidService.getOneIncrBidAndPlace();
		assertNotEquals(bid, bid2);
		bid2 = cloner.deepClone(bid);
		assertEquals(bid, bid2);
	}

	@Test
	void testThatCallingEquals_andHashCode_doesntThrowStackOverFlow() {
		User user = testUserService.getMeSimpleUser();
		assertTrue(user.equals(user));
		user.hashCode();
		Lot lot = testLotService.getMeSimpleLot(user);
		assertTrue(lot.equals(lot));
		lot.hashCode();
		Bid bid = testBidService.getOneIncrBid(lot);
		assertTrue(bid.equals(bid));
		bid.hashCode();

	}

	@Test
	void whenLotIsCreated_autowiredAttributes_areSet() throws IllegalAccessException {
		Bid bid = testBidService.getOneIncrBid(testLotService.getMeSimpleLot());
		assertNotNull(FieldUtils.readField(bid.getLot(), "clock", true));
		assertNotNull(FieldUtils.readField(bid.getLot(), "bidSoftExcepFactory", true));

	}

	// testing persistence

	@Test
	void newLot_canBeSavedToDtbs() throws Exception {

		Lot lot = testLotService.getMeSimpleLot();
		iUserRepo.save(lot.getUser());
		assertTrue(iLotRepo.existsById(lot.getId()));

	}

	@Test
	void ableTo_createBid_AddLotToIt_andSaveToDtbs() throws Exception {
		Bid bid = testBidService.getOneIncrBid(testLotService.getMeSimpleLot());
		iUserRepo.save(testLotService.lotsUser(bid.getLot().placeBid(bid)));
		assertTrue(iBidrepo.existsById(bid.getId()));

	}

	@Test
	void deleteBid_leavesLotInDtbs() {
		Bid bid = testBidService.getOneIncrBid(testLotService.getMeSimpleLot());
		Lot lot = bid.getLot();
		iUserRepo.save(testLotService.lotsUser(lot.placeBid(bid)));
		assertTrue(iBidrepo.existsById(bid.getId()));
		iBidrepo.delete(bid);
		assertTrue(iLotRepo.existsById(lot.getId()));
		assertTrue(!iLotRepo.existsById(bid.getId()));
	}

	@Test
	void deleteLot_leavesUserInDtbs() throws IllegalAccessException {
		Bid bid = testBidService.getOneIncrBid(testLotService.getMeSimpleLot());
		Lot lot = bid.getLot();
		iUserRepo.save(testLotService.lotsUser(lot.placeBid(bid)));

		User user = lot.getUser();
		iLotRepo.delete(lot);
		assertTrue(iUserRepo.existsById(user.getId()));

	}

	@Test
	void deleteLot_removesAllRelatedBids() {
		Bid bid = testBidService.getOneIncrBid(testLotService.getMeSimpleLot());
		Lot lot = bid.getLot();
		iUserRepo.save(testLotService.lotsUser(lot.placeBid(bid)));

		assertTrue(iBidrepo.existsById(bid.getId()));
		iLotRepo.delete(lot);
		assertTrue(!iBidrepo.existsById(bid.getId()));
	}

	@Test
	void deleteUser_removesAllRelatedLots() throws IllegalAccessException {
		Bid bid = testBidService.getOneIncrBid(testLotService.getMeSimpleLot());
		Lot lot = bid.getLot();
		iUserRepo.save(testLotService.lotsUser(lot.placeBid(bid)));
		assertTrue(iLotRepo.existsById(lot.getId()));
		iUserRepo.delete(lot.getUser());
		assertTrue(!iLotRepo.existsById(lot.getId()));
		assertTrue(!iBidrepo.existsById(bid.getId()));

	}

	@Test
	void addingSecondBidOnLot_doesntAffectFirstBid_indDtbs() {
		Lot lot = testLotService.getMeSimpleLot();
		Bid bid = testBidService.getOneIncrBid(lot);
		iUserRepo.save(testLotService.lotsUser(lot.placeBid(bid)));

		Bid bid2 = testBidService.getBidBumpedUpByOneIncrMore(lot, bid);
		lot.placeBid(bid2);
		iLotRepo.save(bid.getLot());
		assertEquals(bid.getLot().getId(), lot.getId());

	}

	// testing bidding logic
	@Test
	void whenLotIsCreated_extendedEndTimeEqueals_endTime() throws IllegalAccessException {
		Lot lot = testLotService.getMeSimpleLot();
		assertEquals(FieldUtils.readField(lot, "endTime", true), FieldUtils.readField(lot, "extendedEndtime", true));
	}

	@Test
	void placeBid_equalOrAfterEndTime_ThrowsException() throws IllegalAccessException {
		Lot lot = testLotService.getMeSimpleLot();
		Instant endTime = lot.getEndTime();
		FieldUtils.writeDeclaredField(lot, "clock", Clock.fixed(endTime, ZoneId.systemDefault()), true);
		ExceptionsCreateor.LotHasEndedException exception = assertThrows(ExceptionsCreateor.LotHasEndedException.class,
				() -> {
					lot.placeBid(testBidService.getOneIncrBid(lot));
				});
		String expectedExcepId = "1";
		assertEquals(exception.getId(), expectedExcepId);
		assertEquals("lot endTime is qual or before current time", exception.getCause().getMessage());
	}

	@Test
	void placeBidBefore_triggerDuration_EndTimeIsNotExtended() throws IllegalAccessException {
		Lot lot = testLotService.getMeSimpleLot();
		Instant endTime = lot.getEndTime();
		FieldUtils.writeDeclaredField(lot, "clock",
				Clock.fixed(endTime.minus(Duration.ofSeconds(121)), ZoneId.systemDefault()), true);
		lot.placeBid(testBidService.getOneIncrBid(lot));
		assertTrue(lot.getEndTime().compareTo(endTime) == 0);

	}

	@Test
	void placeBidWithin_triggerDuration_extendsEndTime() throws IllegalAccessException {
		Lot lot = testLotService.getMeSimpleLot();
		Instant endTime = lot.getEndTime();
		FieldUtils.writeDeclaredField(lot, "clock",
				Clock.fixed(endTime.minus(Duration.ofSeconds(119)), ZoneId.systemDefault()), true);
		lot.placeBid(testBidService.getOneIncrBid(lot));
		assertTrue(lot.getExtendedEndtime().compareTo(endTime.plus(Duration.ofMinutes(5))) == 0);

	}

	@Test
	void placeOneBidIncr_bumpsHighestBidUp_byOneIncr() throws Exception {
		Bid prevHighestBid = testBidService.getOneIncrBid(testLotService.getMeSimpleLot());
		Lot lot = prevHighestBid.getLot();
		lot.placeBid(prevHighestBid);
		lot.placeBid(testBidService.getOneIncrBid(lot));
		assertEquals(testBidService.bumpUpOne(prevHighestBid), lot.getHighestBid());
	}

	@Test
	void bidWhenThereIsNoStartingPrice_thatIsLowerThanOneBidIncr_isRefused() {
		Lot lot = testLotService.getMeSimpleLot();
		ExceptionsCreateor.BidTooLow exception = assertThrows(ExceptionsCreateor.BidTooLow.class, () -> {
			lot.placeBid(testBidService.getArbitraryAmountBid(lot, 4.99));
		});
		String expectedExcepId = "2";
		assertEquals(exception.getId(), expectedExcepId);
	}

	@Test
	void bidWhenThereIsNoStartingPrice_thatIsEqualToOneBidIncr_isAccepted() {
		Bid bid = testBidService.getOneIncrBid(testLotService.getMeSimpleLot());
		bid.getLot().placeBid(bid);
		assertTrue(bid.getLot().contains(bid));

	}

	@Test
	void bidBelowStartingPrice_isRefused() {
		Lot lot = testLotService.getMeLotWithStrtingPrice(7.0);
		ExceptionsCreateor.BidTooLow exception = assertThrows(ExceptionsCreateor.BidTooLow.class, () -> {
			lot.placeBid(testBidService.getOneIncrBid(lot));
		});
		String expectedExcepId = "2";
		assertEquals(exception.getId(), expectedExcepId);
	}

	@Test
	void bidEqualToStartingPrice_isAccepted() {
		Lot lot = testLotService.getMeLotWithStrtingPrice(ONEINCR);
		assertTrue(lot.placeBid(testBidService.getOneIncrBid(lot)).isPresent());
	}

	@Test
	void bidAboveStartingPrice_highestBidBecomesStartingPrice() {
		Lot lot = testLotService.getMeLotWithStrtingPrice(ONEINCR);
		assertTrue(lot.getHighestBid() == 0.0);
		assertTrue(lot.placeBid(testBidService.getTwoIncrBid(lot)).get().getHighestBid() == lot.getStartingPrice());
	}

	@Test
	void placeBid_higherThanCurrentHighestBid_changesLeadingBidder() {
		Lot lot = testLotService.getMeSimpleLot();
		Bid bid1 = testBidService.getOneIncrBid(lot);
		User bidder1 = lot.placeBid(bid1).get().getLeadingBidder();
		assertTrue(bidder1.equals(lot.getLeadingBidder()));
		Bid bid2 = testBidService.getOneIncrBid(lot);

		User bidder2 = lot.placeBid(bid2).get().getLeadingBidder();
		assertTrue(!bidder1.equals(bidder2));
		assertEquals(lot.getLeadingBidder(), bidder2);
	}

	@Test
	void bidThatEqualsTo_oneIncrMoreThanPrevBid_doesNotbecomePendingAutoBid() {
		Bid bid1 = testBidService.getOneIncrBidAndPlace();
		Lot lot = bid1.getLot();
		Bid bid2 = testBidService.getBidBumpedUpByOneIncrMore(lot, bid1);
		lot.placeBid(bid2);
		ExceptionsCreateor.AutobidNotSet exception = assertThrows(ExceptionsCreateor.AutobidNotSet.class, () -> {
			lot.getPendingAutoBid();
		});
		String expectedExcepId = "3";
		assertEquals(expectedExcepId, exception.getId());
	}

	@Test
	void bidThatIsOver_oneIncrMoreThanPrevBid_becomesPendingAutoBid() {
		Bid bid1 = testBidService.getOneIncrBidAndPlace();
		Lot lot = bid1.getLot();
		Bid bid2 = testBidService.getBidBumpedUpByTwoIncrMore(lot, bid1);// bid2 is bumped up by two increments relative
																			// to bid1
		lot.placeBid(bid2);
		assertTrue(lot.getPendingAutoBid().getAmount() == bid2.getAmount());
	}

	@Test
	void bidThatIsBelow_highestBidIsRefused() {
		Bid bid = testBidService.getOneIncrBidAndPlace();
		ExceptionsCreateor.BidTooLow exception = assertThrows(ExceptionsCreateor.BidTooLow.class, () -> {
			bid.getLot().placeBid(testBidService.getArbitraryAmountBid(bid.getLot(), -0.01));
		});
		String expectedExcepId = "2";
		assertEquals(exception.getId(), expectedExcepId);
	}

	@Test
	void bidThatIsEqual_highestBidIsRefused() {
		Bid bid = testBidService.getOneIncrBidAndPlace();
		ExceptionsCreateor.BidTooLow exception = assertThrows(ExceptionsCreateor.BidTooLow.class, () -> {
			bid.getLot().placeBid(testBidService.getArbitraryAmountBid(bid.getLot(), 0.00));
		});
		String expectedExcepId = "2";
		assertEquals(exception.getId(), expectedExcepId);
	}

	@Test
	void bidAgainstSelf_thatIsBelow_highestBidIsRefused() {
		Bid bid = testBidService.getOneIncrBidAndPlace();
		ExceptionsCreateor.BidTooLow exception = assertThrows(ExceptionsCreateor.BidTooLow.class, () -> {
			bid.getLot().placeBid(testBidService.getArbitraryAmountBid(bid.getLot(), -0.01, bid.getBidder()));
		});
		String expectedExcepId = "2";
		assertEquals(exception.getId(), expectedExcepId);
	}

	@Test
	void bidAgainstSelf_thatIsEqual_highestBidIsRefused() {
		Bid bid = testBidService.getOneIncrBidAndPlace();
		ExceptionsCreateor.BidTooLow exception = assertThrows(ExceptionsCreateor.BidTooLow.class, () -> {
			bid.getLot().placeBid(testBidService.getArbitraryAmountBid(bid.getLot(), 0.00, bid.getBidder()));
		});
		String expectedExcepId = "2";
		assertEquals(exception.getId(), expectedExcepId);
	}

	@Test
	void bidAgainstSelf_thatIsEqual_oneIncrMoreThanPrevBid_isAccepted() {
		Bid bid1 = testBidService.getOneIncrBidAndPlace();
		Lot lot = bid1.getLot();
		Bid bid2 = testBidService.getBidBumpedUpByOneIncrMore(lot, bid1, bid1.getBidder());

		lot.placeBid(bid2);
		assertTrue(lot.getHighestBid() == bid2.getAmount());
		assertThrows(ExceptionsCreateor.AutobidNotSet.class, () -> {
			lot.getPendingAutoBid();
		});
	}

	@Test
	void bidAgainstSelf_thatIsAbove_oneIncrMoreThanPrevBid_becomesAutobid() {
		Bid bid1 = testBidService.getOneIncrBidAndPlace();
		Lot lot = bid1.getLot();
		Bid bid2 = testBidService.getBidBumpedUpByTwoIncrMore(lot, bid1, bid1.getBidder());
		lot.placeBid(bid2);
		assertTrue(lot.getPendingAutoBid().getAmount() == bid2.getAmount());
	}

	@Test
	void bidThatIsBelow_highestBid_whenThereExistsAutobid_IsRefused() {
		Bid bid = testBidService.getTwoIncrBidAndPlace();
		assertThrows(ExceptionsCreateor.BidTooLow.class, () -> {
			bid.getLot().placeBid(testBidService.getArbitraryAmountBid(bid.getLot(), -0.01));
		});

	}

	@Test
	void bidThatIsEqual_highestBid_whenThereExistsAutobid_IsRefused() {
		Bid bid = testBidService.getTwoIncrBidAndPlace();
		assertThrows(ExceptionsCreateor.BidTooLow.class, () -> {
			bid.getLot().placeBid(testBidService.getArbitraryAmountBid(bid.getLot(), 0.00));
		});
	}

	@Test
	void placeBid_thatIsBelowAutoBid_kicksHighestBidUp_byOneIncr_higherThanBid() throws Exception {
		Lot lot = testLotService.getMeSimpleLot();
		Bid autoBid = testBidService.getThreeIncrBid(lot);
		lot.placeBid(autoBid);
		assertEquals(lot.getBiddingIncrement(), lot.getHighestBid());
		assertEquals(autoBid, lot.getPendingAutoBid());

		Bid newBid = testBidService.getOneIncrBid(lot);
		lot.placeBid(newBid);
		assertEquals(testBidService.bumpUpOne(newBid), lot.getHighestBid());
		assertEquals(lot.getLeadingBidder(), autoBid.getBidder());

	}

	@Test
	void bidAgainstSelf_thatIsOverHighestBid_butBelowAutobidThrowsException() throws Exception {
		Bid autoBid = testBidService.getThreeIncrBidAndPlace();
		assertThrows(ExceptionsCreateor.AlreadyLeadingBidder.class, () -> {
			autoBid.getLot()
					.placeBid(testBidService.getArbitraryAmountBid(autoBid.getLot(), ONEINCR, autoBid.getBidder()));
		});

	}

	@Test
	void bidAgainstSelf_thatIsOverHighestBid_andEqualAutobidThrowsException() throws Exception {
		Bid autoBid = testBidService.getThreeIncrBidAndPlace();
		assertThrows(ExceptionsCreateor.AlreadyLeadingBidder.class, () -> {
			autoBid.getLot()
					.placeBid(testBidService.getArbitraryAmountBid(autoBid.getLot(), TWOINCR, autoBid.getBidder()));
		});

	}

	@Test
	void bidAgainstSelf_thatIsOverHighestBid_andOverAutobidThrowsException_isAccepted() {
		Bid autoBid = testBidService.getThreeIncrBidAndPlace();
		autoBid.getLot()
				.placeBid(testBidService.getArbitraryAmountBid(autoBid.getLot(), THREEINCR, autoBid.getBidder()));
		assertEquals(testBidService.bumpUpOne(autoBid), autoBid.getLot().getPendingAutoBid().getAmount());
	}

	@Test
	void placeBid_thatIsEqualToAutoBid_autoBidIsRemoved() throws Exception {
		Lot lot = testLotService.getMeSimpleLot();
		Bid autoBid = testBidService.getThreeIncrBid(lot);
		User autoBidder = lot.placeBid(autoBid).get().getLeadingBidder();

		Bid newBid = testBidService.getTwoIncrBid(lot);
		lot.placeBid(newBid);
		ExceptionsCreateor.AutobidNotSet exception = assertThrows(ExceptionsCreateor.AutobidNotSet.class, () -> {
			lot.getPendingAutoBid();
		});
		String expectedExcepId = "3";
		assertEquals(expectedExcepId, exception.getId());
		assertEquals(autoBid.getAmount(), lot.getHighestBid());
		assertEquals(autoBidder, lot.getLeadingBidder());
	}

	@Test
	void placeBid_thatIsAboveAutoBid_withOnlyOneIncr_leadingBidderChanges_andAutoBidIsRemoved() throws Exception {
		Lot lot = testLotService.getMeSimpleLot();
		Bid autoBid = testBidService.getThreeIncrBid(lot);
		lot.placeBid(autoBid);
		Bid newBid = testBidService.getThreeIncrBid(lot);
		lot.placeBid(newBid);
		assertThat(lot.getHighestBid(), equalTo(testBidService.bumpUpOne(autoBid)));
		assertThat(newBid.getBidder(), equalTo(lot.getLeadingBidder()));

		ExceptionsCreateor.AutobidNotSet exception = assertThrows(ExceptionsCreateor.AutobidNotSet.class, () -> {
			lot.getPendingAutoBid();
		});
		String expectedExcepId = "3";
		assertEquals(expectedExcepId, exception.getId());
	}

	@Test
	void placeBid_thatIsAboveAutoBid_withMoreThanOneIncr_becomesAutoBid() throws Exception {
		Lot lot = testLotService.getMeSimpleLot();
		lot.placeBid(testBidService.getTwoIncrBid(lot));
		Bid newBid = testBidService.getThreeIncrBid(lot);// bids at 20
		assertEquals(testBidService.bumpOneDown(newBid), lot.placeBid(newBid).get().getHighestBid()); // new highest bid
																										// is only
																										// raised to one
																										// incrementA
		assertEquals(lot.getLeadingBidder(), newBid.getBidder());
		assertEquals(newBid, lot.getPendingAutoBid());

	}

	@Test
	void addMultipileUniqueLots_toSetInUser_inMultipleThreadsPassess() throws InterruptedException, ExecutionException {
		User user = testUserService.getMeSimpleUser();
		int threads = 10;
		ExecutorService service = Executors.newFixedThreadPool(threads);
		CountDownLatch latch = new CountDownLatch(1);
		AtomicBoolean running = new AtomicBoolean();// shared boolean among threads
		AtomicInteger overlaps = new AtomicInteger();// shared integer counter among threads
		Collection<Future<Boolean>> futures = new ArrayList<>(threads);// will store the bools returned after calling
																		// add
		for (int t = 0; t < threads; ++t) {
			final Lot lot = testLotService.getMeSimpleLot();
			futures.add(service.submit(() -> {
				latch.await();// instructs all the threads to wait here for the latch.coundDown signal, to
								// force overlapment
				if (running.get()) {// will be true if other thread is running at the same time (because of line
									// 400)
					overlaps.incrementAndGet();// this thread indicating that other thread is overlapping its work
				}
				running.set(true);
				boolean bool = user.addLotToSet(lot);// we need to assure overlapment as this naturally takes too little
														// time
				running.set(false);
				return bool;// value returned from calling add
			}));

		}
		latch.countDown();// opens the latch so that all threads start together
		for (Future<Boolean> f : futures) {
			assertTrue(f.get().equals(true));// assure each add was successful
		}
		assertTrue(overlaps.get() > 0);
		assertTrue(user.getNumberOfLots() == 10);// assure lotsCreatedSet contains 10 lots
	}

}
