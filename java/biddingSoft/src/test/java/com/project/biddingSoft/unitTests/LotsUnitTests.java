package com.project.biddingSoft.unitTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.shaded.org.apache.commons.lang.reflect.FieldUtils;
import org.testcontainers.shaded.org.bouncycastle.util.test.TestFailedException;

import com.project.biddingSoft.controller.ServletInitializer;
import com.project.biddingSoft.domain.Bid;
import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.domain.User;
import com.project.biddingSoft.service.ExceptionsCreateor;
import com.project.biddingSoft.testServices.TestBidService;
import com.project.biddingSoft.testServices.TestLotService;
import com.project.biddingSoft.testServices.TestUserService;

import junit.framework.TestFailure;

@AutoConfigureMockMvc
@SpringBootTest
@RunWith(JUnitPlatform.class)
@TestInstance(Lifecycle.PER_CLASS)
public class LotsUnitTests {

	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_RESET = "\u001B[0m";
	private static final Logger logger = LoggerFactory.getLogger(LotsUnitTests.class);
	@Autowired
	ServletInitializer servletInitializer;
	@Autowired
	Lot wiredLot;
	@Autowired
	TestLotService testLotService;
	@Autowired
	TestBidService testBidService;
	@Autowired
	TestUserService testUserService;
	@Autowired
	private MockMvc mvc;
	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Test
	public void contextLoads() throws Exception {
		assertThat(servletInitializer).isNotNull();

	}

	@BeforeAll
	public void removeAllTables() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0;").executeUpdate();
		entityManager.createNativeQuery("TRUNCATE TABLE bid;").executeUpdate();
		entityManager.createNativeQuery("TRUNCATE TABLE lot;").executeUpdate();
		entityManager.createNativeQuery("TRUNCATE TABLE user;").executeUpdate();
		entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1;").executeUpdate();
		entityManager.createNativeQuery("SET SQL_SAFE_UPDATES = 0;").executeUpdate();
		entityManager.createNativeQuery("UPDATE  biddingsoft.hibernate_sequence SET next_val = 1;").executeUpdate();
		entityManager.createNativeQuery("SET SQL_SAFE_UPDATES = 1;").executeUpdate();

		entityManager.flush();
		entityManager.getTransaction().commit();

	}

	@Test
	public void test_app_is_up() throws IOException {
		RestTemplate restTemplate = new TestRestTemplate().getRestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/", String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		assertThat(response.getBody(), equalTo("Service running"));
	}

	@Test
	public void createLot_canBeSavedToDtbs() throws Exception {

		Lot lot = testLotService.getMeSimpleLot();
		lot.saveToRepo();
		assertThat(true, equalTo(lot.find().isPresent()));

	}

	@Test

	public void createLot_withAutowired_canBeSavedToDtbs() throws Exception {
		wiredLot.setUser(testUserService.getMeSimpleUser());
		wiredLot.saveToRepo();
		assertThat(true, equalTo(wiredLot.find().isPresent()));

	}

	@Test
	public void newLot_canBeSavedToDtbs() throws Exception {
		Lot lot = testLotService.getMeSimpleLot();
		lot.saveToRepo();
		assertThat(true, equalTo(lot.find().isPresent()));
	}

	@Test
	public void createTwoLots_areNotTheSame() throws Exception {
		Lot lot1 = testLotService.getMeSimpleLot();
		Lot lot2 = testLotService.getMeSimpleLot();
		assertThat(lot1, not(lot2));
	}

	@Test
	public void ableTo_createBid_AddLotToIt_andSaveToDtbs() throws Exception {
		Bid bid = testBidService.getOneIncrBid(testLotService.getMeSimpleLot());
		bid.getLot().placeBid(bid);
		bid.getLot().saveToRepo();
		assertThat(true, equalTo(bid.find().isPresent()));

	}

	@Test
	public void deleteBid_leavesLotInDtbs() {
		Bid bid = testBidService.getOneIncrBid(testLotService.getMeSimpleLot());
		Lot lot = bid.getLot();
		lot.placeBid(bid);
		lot.saveToRepo();
		bid.delete();
		assertThat(Optional.empty(), equalTo(bid.find()));
		assertThat(true, equalTo(lot.find().isPresent()));
	}

	@Test
	public void deleteLot_leavesUserInDtbs() throws IllegalAccessException {
		Bid bid = testBidService.getOneIncrBid(testLotService.getMeSimpleLot());
		Lot lot = bid.getLot();
		lot.placeBid(bid);
		lot.saveToRepo();
		User user = (User) FieldUtils.readField(lot, "user", true);
		lot.delete();
		assertThat(true, equalTo(user.find().isPresent()));

	}

	@Test
	public void deleteLot_removesAllRelatedBids() {
		Bid bid = testBidService.getOneIncrBid(testLotService.getMeSimpleLot());
		Lot lot = bid.getLot();
		lot.placeBid(bid);
		lot.saveToRepo();
		assertThat(true, equalTo(bid.find().isPresent()));
		lot.delete();
		assertThat(true, equalTo(bid.find().isEmpty()));
	}

	@Test
	public void deleteUser_removesAllRelatedLots() throws IllegalAccessException {
		Bid bid = testBidService.getOneIncrBid(testLotService.getMeSimpleLot());
		Lot lot = bid.getLot();
		lot.placeBid(bid);
		User user = (User) FieldUtils.readField(lot, "user", true);
		user.saveToRepo();
		assertThat(true, equalTo(lot.find().isPresent()));
		user.delete();
		assertThat(true, equalTo(lot.find().isEmpty()));
		assertThat(true, equalTo(bid.find().isEmpty())); // should work?!

	}

	@Test
	public void whenLotIsAutoWired_autowiredAttributes_areSet() throws NoSuchFieldException, IllegalAccessException {
		assertThat(FieldUtils.readField(wiredLot, "title", true)).isNotNull();
		assertThat(FieldUtils.readField(wiredLot, "description", true)).isNotNull();
		assertThat(FieldUtils.readField(wiredLot, "ZONE", true)).isNotNull();
		assertThat(FieldUtils.readField(wiredLot, "biddingIncrement", true)).isNotNull();
		assertThat(FieldUtils.readField(wiredLot, "startingPrice", true)).isNotNull();
		assertThat(FieldUtils.readField(wiredLot, "triggerDuration", true)).isNotNull();
		assertThat(FieldUtils.readField(wiredLot, "autoExtendDuration", true)).isNotNull();
		assertThat(FieldUtils.readField(wiredLot, "iLotRepo", true)).isNotNull();
		assertThat(FieldUtils.readField(wiredLot, "clock", true)).isNotNull();
		assertThat(FieldUtils.readField(wiredLot, "bidSoftExcepFactory", true)).isNotNull();

	}

	@Test
	public void whenLotIsCreated_extendedEndTimeEqueals_endTime() throws IllegalAccessException {
		Lot lot = testLotService.getMeSimpleLot();
		assertThat(FieldUtils.readField(lot, "extendedEndtime", true),
				equalTo(FieldUtils.readField(lot, "endTime", true)));
	}

	@Test
	public void whenLotIsCreated_autowiredAttributes_areSet() throws IllegalAccessException {
		Bid bid = testBidService.getOneIncrBid(testLotService.getMeSimpleLot());
		assertThat(FieldUtils.readField(bid.getLot(), "clock", true)).isNotNull();
		assertThat(FieldUtils.readField(bid.getLot(), "bidSoftExcepFactory", true)).isNotNull();

	}

	@Test
	public void placeBid_equalOrAfterEndTime_ThrowsException() throws IllegalAccessException {
		Lot lot = testLotService.getMeSimpleLot();
		Instant endTime = lot.getEndTime();
		FieldUtils.writeDeclaredField(lot, "clock", Clock.fixed(endTime, ZoneId.systemDefault()), true);
		ExceptionsCreateor.LotHasEndedException exception = assertThrows(ExceptionsCreateor.LotHasEndedException.class,
				() -> {
					lot.placeBid(testBidService.getOneIncrBid(lot));
				});
		String expectedExcepId = "1";
		assertThat(exception.getId(), equalTo(expectedExcepId));
		assertThat(exception.getCause().getMessage(), equalTo("lot endTime is qual or before current time"));
	}

	@Test
	public void placeBidWithin_triggerDuration_extendsEndTime() throws IllegalAccessException {
		Lot lot = testLotService.getMeSimpleLot();
		Instant endTime = lot.getEndTime();
		FieldUtils.writeDeclaredField(lot, "clock",
				Clock.fixed(endTime.minus(Duration.ofSeconds(119)), ZoneId.systemDefault()), true);
		lot.placeBid(testBidService.getOneIncrBid(lot));
		assertTrue(lot.getEndTime().compareTo(endTime.plus(Duration.ofMinutes(5))) == 0);

	}

	@Test
	public void placeBidBefore_triggerDuration_EndTimeIsNotExtended() throws IllegalAccessException {
		Lot lot = testLotService.getMeSimpleLot();
		Instant endTime = lot.getEndTime();
		FieldUtils.writeDeclaredField(lot, "clock",
				Clock.fixed(endTime.minus(Duration.ofSeconds(121)), ZoneId.systemDefault()), true);
		lot.placeBid(testBidService.getOneIncrBid(lot));
		assertTrue(lot.getEndTime().compareTo(endTime) == 0);

	}

	@Test
	public void bidWhenThereIsNoStartingPrice_thatIsLowerThanOneBidIncr_isRefused() {
		Lot lot = testLotService.getMeSimpleLot();
		ExceptionsCreateor.BidTooLow exception = assertThrows(ExceptionsCreateor.BidTooLow.class, () -> {
			lot.placeBid(testBidService.getArbitraryAmountBid(lot, 4.99));
		});
		String expectedExcepId = "2";
		assertThat(exception.getId(), equalTo(expectedExcepId));
	}

	@Test
	public void bidWhenThereIsNoStartingPrice_thatIsEqualToOneBidIncr_isAccepted() {
		Bid bid = testBidService.getOneIncrBid(testLotService.getMeSimpleLot());
		bid.getLot().placeBid(bid);
		assertTrue(bid.getLot().contains(bid));

	}

	@Test
	public void bidBelowStartingPrice_isRefused() {
		Lot lot = testLotService.getMeLotWithSrtingPrice(7.0);
		ExceptionsCreateor.BidTooLow exception = assertThrows(ExceptionsCreateor.BidTooLow.class, () -> {
			lot.placeBid(testBidService.getOneIncrBid(lot));
		});
		String expectedExcepId = "2";
		assertThat(exception.getId(), equalTo(expectedExcepId));
	}

	@Test
	public void bidEqualToStartingPrice_isAccepted() {
		Lot lot = testLotService.getMeLotWithSrtingPrice(5.0);
		assertTrue(lot.placeBid(testBidService.getOneIncrBid(lot)).isPresent());
	}

	@Test
	public void placeBid_higherThanCurrentHighestBid_changesLeadingBidder() {
		Lot lot = testLotService.getMeSimpleLot();
		Bid bid1 = testBidService.getOneIncrBid(lot);
		User bidder1 = lot.placeBid(bid1).get().getLeadingBidder();
		assertTrue(bidder1.equals(lot.getLeadingBidder()));
		Bid bid2 = testBidService.getOneIncrBid(lot);

		User bidder2 = lot.placeBid(bid2).get().getLeadingBidder();
		assertTrue(!bidder1.equals(bidder2));
		assertTrue(lot.getLeadingBidder().equals(bidder2));
	}

	@Test
	public void placeOneBidIncr_bumpsHighestBidUp_byOneIncr() throws Exception {
		Bid prevHighestBid = testBidService.getOneIncrBid(testLotService.getMeSimpleLot());
		Lot lot = prevHighestBid.getLot();
		lot.placeBid(prevHighestBid);
		lot.placeBid(testBidService.getOneIncrBid(lot));
		assertThat(lot.getHighestBid(), equalTo(testBidService.bumpUpOne(prevHighestBid)));
	}

	@Test
	public void bidThatIsOver_orEqualTo_oneIncrMoreThanPrevBid_becomesPendingAutoBid() {
		Lot lot = testLotService.getMeSimpleLot();
		Bid bid1 = testBidService.getOneIncrBid(lot);
		Bid bid2 = testBidService.getBidBumpedUpByTwoIncrMore(lot, bid1);// bid2 is bumped up by two increments relative
																			// to bid1
		lot.placeBid(bid1).get().placeBid(bid2);
		assertTrue(lot.getPendingAutoBid().getAmount() == bid2.getAmount());
	}

	@Test
	void placeBid_thatIsBelowAutoBid_kicksHighestBidUp_byOneIncr_higherThanBid() throws Exception {
		Lot lot = testLotService.getMeSimpleLot();
		Bid autoBid = testBidService.getThreeIncrBid(lot);
		lot.placeBid(autoBid);
		assertThat(lot.getHighestBid(), equalTo(lot.getBiddingIncrement()));
		assertThat(lot.getPendingAutoBid(), equalTo(autoBid));

		Bid newBid = testBidService.getOneIncrBid(lot);
		lot.placeBid(newBid);
		assertThat(lot.getHighestBid(), equalTo(testBidService.bumpUpOne(newBid)));
		assertThat(autoBid.getBidder(), equalTo(lot.getLeadingBidder()));

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
		assertThat(exception.getId(), equalTo(expectedExcepId));
		assertThat(lot.getHighestBid(), equalTo(autoBid.getAmount()));
		assertThat(autoBidder, equalTo(lot.getLeadingBidder()));
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
		assertThat(exception.getId(), equalTo(expectedExcepId));
	}

	@Test
	void placeBid_thatIsAboveAutoBid_withMoreThanOneIncr_becomesAutoBid() throws Exception {
		Lot lot = testLotService.getMeSimpleLot();
		lot.placeBid(testBidService.getTwoIncrBid(lot));
		Bid newBid = testBidService.getThreeIncrBid(lot);// bids at 20
		assertThat(lot.placeBid(newBid).get().getHighestBid(), equalTo(testBidService.bumpOneDown(newBid)));	//new highest bid is only raised to one incrementA																								
		assertThat(newBid.getBidder(), equalTo(lot.getLeadingBidder()));
		assertThat(lot.getPendingAutoBid(), equalTo(newBid));

	}

}