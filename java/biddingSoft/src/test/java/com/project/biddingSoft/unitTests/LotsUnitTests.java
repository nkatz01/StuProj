package com.project.biddingSoft.unitTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.Matchers.*; 
import static org.hamcrest.MatcherAssert.*;

import com.project.biddingSoft.BiddingSoftwareApplication;
import com.project.biddingSoft.controller.ServletInitializer;
import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.service.ILotService;
import com.project.biddingSoft.testServices.TestLotService;

@SpringBootTest
class LotsUnitTests {
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_RESET = "\u001B[0m";
	private static final Logger logger = LoggerFactory.getLogger(LotsUnitTests.class);
	@Autowired
	 ServletInitializer servletInitializer;
	@Autowired
	ILotService LotService; 
	
	@Test
	void contextLoads() throws Exception {
	assertThat(servletInitializer).isNotNull();
 		
	}
	
	@Test
	void createLot_isNotNull() throws Exception { 
		Lot lot = TestLotService.getMeSimpleLot();
		assertThat(lot, is(notNullValue()));
	}
	
//	@Test
//	void createLot_startTimeEquals_endTime() throws Exception { 
//		Lot lot = TestLotService.getMeSimpleLot().build();
//		assertThat(lot.getStartTime(), equalTo(lot.getEndTime()));
//	}
//	
//	@Test
//	void createLot_highestBidEquals_startingBid() throws Exception { 
//		Lot lot = Lot.giveDependencies(TestLotService.getMeSimpleLot().build());
//		assertThat(lot.getStartingBid(), equalTo(lot.getHighestBid()));
//	}
//	
//	@Test
//	void placeOneBidIncr_bumpsHighestBidUp_byOne() throws Exception { 
//		Lot lot = Lot.giveDependencies(TestLotService.getMeSimpleLot().build());
//		Class<?> prevHighestBid = lot.getHighestBid();
//		lot.placeBid(Bid.giveDependencies(TestBidComponents.oneIncr().build()));
//		assertThat(lot.getHighestBid(), equalTo(prevHighestBid.bumpUpOne()));
//	}
//	
//	@Test
//	void placeOneBid_afterEndTime_ThrowsException() throws Exception { 
//		Lot lot = Lot.giveDependencies(TestLotService.getMeSimpleLot().build());
//		lot.endLot();
//		 Exception exception = assertThrows(Exception.class, () -> {
//			lot.placeBid(Bid.giveDependencies(TestBidComponents.oneIncr().build()));
//	    });
//		String expectedMessage = "Bid rejected - Lot expired";
//		assertThat(exception.getMessage(), equalTo( expectedMessage));
//	}
//	
//	@Test
//	void placeBid_thatIsBelowAutoBid_kicksHighestBidUp_byOne() throws Exception { 
//		Lot lot = Lot.giveDependencies(TestLotService.getMeSimpleLot().build());
//		lot.placeBid(Bid.giveDependencies(TestBidComponents.threeIncr().build()));
//		Class<?> prevHighestBid = lot.getHighestBid();
//		User userWithPrevHigestBid = prevHighestBid.getUser();
//		Class<?> pendingAutoBid = lot.getPendingAutobid();
//		lot.placeBid(Bid.giveDependencies(TestBidComponents.oneIncr().build()));
//		assertThat(lot.getHighestBid(), equalTo(prevHighestBid.bumpUpOne()));
//		assertThat(userWithPrevHigestBid, equalTo(lot.getLeadingBidder()));
//		assertThat(pendingAutoBid, equalTo(lot.getPendingAutobid()));
//	}
//	@Test
//	void placeBid_thatIsEqualToAutoBid_autoBidIsRemoved() throws Exception { 
//		Lot lot = Lot.giveDependencies(TestLotService.getMeSimpleLot().build());
//		lot.placeBid(Bid.giveDependencies(TestBidComponents.threeIncr().build()));
//		Class<?> prevHighestBid = lot.getHighestBid();
//		User userWithPrevHigestBid = prevHighestBid.getUser();
//		assertThat(lot.getPendingAutoBit(), is(true));
//		lot.placeBid(Bid.giveDependencies(TestBidComponents.twoIncr().build()));
//		assertThat(lot.getHighestBid(), equalTo(prevHighestBid.bumpUpTwo()));
//		assertThat(userWithPrevHigestBid, equalTo(lot.getLeadingBidder()));
//		 Exception exception = assertThrows(Exception.class, () -> {
//			 lot.getPendingAutobid();
//		    });
//		 String expectedMessage = "Lot doesn't have an autobid";
//			assertThat(exception.getMessage(), equalTo( expectedMessage));
//	}
//	@Test
//	void placeBid_thatIsAboveAutoBid_leadingBidderChanges() throws Exception { 
//		Lot lot = Lot.giveDependencies(TestLotService.getMeSimpleLot().build());
//		lot.placeBid(Bid.giveDependencies(TestBidComponents.threeIncr().build()));
//		Class<?> prevHighestBid = lot.getHighestBid();
//		Class<?> pendingAutoBid = lot.getPendingAutobid();
//		lot.placeBid(Bid.giveDependencies(TestBidComponents.threeIncr().build()));
//		User userWithNewHigestBid = lot.getHighestBid().getUser();
//		
//		assertThat(lot.getHighestBid(), equalTo(prevHighestBid.bumpUpTwo()));
//		assertThat(userWithNewHigestBid, equalTo(lot.getLeadingBidder()));
//		assertThat(pendingAutoBid, equalTo(lot.getPendingAutobid()));
//		
//		 String expectedMessage = "Lot doesn't have an autobid";
//			assertThat(exception.getMessage(), equalTo( expectedMessage));
//	}
//	
//	@Test
//	void triggerDurationPeriodOnLot_andPushTimeToEnd_endsLot{
//		Lot lot = Lot.giveDependencies(TestLotService.getMeSimpleLot().build());
//		lot.beginTriggerDuration();
//		lot.setTimeToEnd();
//		 Exception exception = assertThrows(Exception.class, () -> {
//				lot.placeBid(Bid.giveDependencies(TestBidComponents.oneIncr().build()));
//		    });
//			String expectedMessage = "Bid rejected - Lot expired";
//			assertThat(exception.getMessage(), equalTo( expectedMessage));
//	}
//	
//	@Test
//	void placeBidWithin_triggerDuration_extendsEndTime{
//		Lot lot = Lot.giveDependencies(TestLotService.getMeSimpleLot().build());
//		
//		lot.beginTriggerDuration();
//		assertThat(lot.getEndTime() < (lot.getTickingTime()-   Duration.ofMinutes(3)));
//		lot.placeBid(Bid.giveDependencies(TestBidComponents.oneIncr().build()));
//		assertThat(lot.getEndTime() > (lot.getTickingTime()-   Duration.ofMinutes(3)));
//
//	}
 
}
