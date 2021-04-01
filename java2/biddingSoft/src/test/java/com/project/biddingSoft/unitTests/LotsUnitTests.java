package com.project.biddingSoft.unitTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import java.io.IOException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.project.biddingSoft.controller.ServletInitializer;
import com.project.biddingSoft.domain.Bid;
import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.domain.User;
import com.project.biddingSoft.testServices.TestBidService;
import com.project.biddingSoft.testServices.TestLotService;
import com.project.biddingSoft.testServices.TestUserService;

@AutoConfigureMockMvc
@SpringBootTest
class LotsUnitTests    {
	
	  
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_RESET = "\u001B[0m";
	private static final Logger logger = LoggerFactory.getLogger(LotsUnitTests.class);
	@Autowired
	ServletInitializer servletInitializer;
	@Autowired
	Lot lot;
	@Autowired
	TestLotService testLotService;
	@Autowired
	TestBidService testBidService;
	@Autowired 
	TestUserService testUserService; 
	@Autowired
    private MockMvc mvc;
	@Test
	void contextLoads() throws Exception {
		assertThat(servletInitializer).isNotNull();

	}
	
	

	@Test
	void test_app_is_up() throws IOException {
		RestTemplate restTemplate =   new TestRestTemplate().getRestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
         assertThat(response.getBody(), equalTo("Service running"));	
	}

	@Test
	void createLot_canBeSavedToDtbs() throws Exception {
		
		Lot lot = testLotService.getMeSimpleLot();
		lot.saveToRepo();
		assertThat(true, equalTo(lot.find().isPresent()));

	}
	
	 @Test
	 
	void createLot_withAutowired_canBeSavedToDtbs() throws Exception {
		 lot.setUser(testUserService.getMeSimpleUser());
		  lot.saveToRepo();
   assertThat(true, equalTo(lot.find().isPresent()));

	}
	 

	
	@Test
	void newLot_canBeSavedToDtbs() throws Exception {
		Lot lot = testLotService.getMeSimpleLot();
		lot.saveToRepo();
		assertThat(true, equalTo(lot.find().isPresent()));
	}

	@Test
	void createTwoLots_areNotTheSame() throws Exception {
		Lot lot1 = testLotService.getMeSimpleLot();
		Lot lot2 = testLotService.getMeSimpleLot();
		assertThat(lot1, not(lot2));
	}
 


	
	@Test
	void createLot_canAddBidWithLot_Toit() throws Exception {
		Lot lot =  testLotService.getMeSimpleLot();
		Bid bid = testBidService.getOneIncrBid();
		Lot.addBid( lot, bid);
		assertThat(lot.getHighestBid(), equalTo(bid));
		
	}

	@Test
	void ableTo_createBid_AddLotToIt_andSaveToDtbs() throws Exception{
		Lot lot =  testLotService.getMeSimpleLot();
		Bid bid = testBidService.getOneIncrBid();		 
		Lot.addBid(lot, bid);
		lot.getUser().saveToRepo();
		System.out.println(lot.getId());
		System.out.println(lot.getUser().getLot(1));
	 	 assertThat(true, equalTo(bid.find().isPresent()));

	 	
	}
	
	@Test 
	void deleteBid_leavesLotInDtbs(){
		Lot lot =  testLotService.getMeSimpleLot();
		Bid bid = testBidService.getOneIncrBid();	
		Lot.addBid(lot, bid);
		lot.saveToRepo();
		lot.getHighestBid().delete();
	 	 assertThat(Optional.empty(), equalTo(bid.find()));
		 assertThat(true, equalTo(lot.find().isPresent()));
	}
	@Test 
	void deleteLot_leavesUserInDtbs(){
		Lot lot =  testLotService.getMeSimpleLot();	
		Bid bid = testBidService.getOneIncrBid();	
		Lot.addBid(lot, bid);
		lot.saveToRepo();
	 	User user = lot.getUser();
		lot.delete();
	  assertThat(true, equalTo(user.find().isPresent()));

	}
	@Transactional
	@Test 
	void deleteLot_removesAllRelatedBids(){
		Lot lot =  testLotService.getMeSimpleLot();	
		Bid bid = testBidService.getOneIncrBid();	
		Lot.addBid(lot, bid);
		lot.saveToRepo();
	 	assertThat(true, equalTo(bid.find().isPresent()));
		lot.delete();
		assertThat(true, equalTo(bid.find().isEmpty()));
	}
	@Transactional
	@Test 
	void deleteUser_removesAllRelatedLots(){
		Lot lot =  testLotService.getMeSimpleLot();	
		Bid bid = testBidService.getOneIncrBid();	
		Lot.addBid(lot, bid);
		User user = lot.getUser();
		user.saveToRepo();	
		assertThat(true, equalTo(lot.find().isPresent()));
		user.delete();
	  	assertThat(true, equalTo(lot.find().isEmpty()));
	  	assertThat(true, equalTo(bid.find().isEmpty())); //should work?!

	}
	@Test
	void whenLotIsCreated_extendedEndTimeEqueals_endTime() {
		Lot lot = testLotService.getMeSimpleLot();
		assertThat(lot.getExtendedEndTime(), equalTo(lot.getEndTime()));
	}

	@Test
	void placeOneBidIncr_bumpsHighestBidUp_byOne() throws Exception {
		Bid prevHighestBid = testBidService.getOneIncrBid();	
		Lot.addBid(lot, prevHighestBid);
		lot.placeBid(testBidService.getOneIncrBid());
		//System.out.println(lot.getTriggerDuration() +" "+ lot.getAutoExtendDuration());  
		assertThat(lot.getHighestBid().getAmount(), equalTo(testBidService.getOneincr()));
	}
//
//	@Test
//	void placeOneBid_afterEndTime_ThrowsException() throws Exception {
//		Lot lot = testLotService.getMeSimpleLot());
//		lot.endLot();
//		Exception exception = assertThrows(Exception.class, () -> {
//			lot.placeBid(Bid.giveDependencies(TestBidComponents.oneIncr().build()));
//		});
//		String expectedMessage = "Bid rejected - Lot expired";
//		assertThat(exception.getMessage(), equalTo(expectedMessage));
//	}
//
//	@Test
//	void placeBid_thatIsBelowAutoBid_kicksHighestBidUp_byOne() throws Exception {
//		Lot lot = testLotService.getMeSimpleLot());
//		lot.placeBid(Bid.giveDependencies(TestBidComponents.threeIncr().build()));
//		Class<?> prevHighestBid = lot.getHighestBid();
//		User userWithPrevHigestBid = prevHighestBid.getUser();
//		Class<?> pendingAutoBid = lot.getPendingAutobid();
//		lot.placeBid(Bid.giveDependencies(TestBidComponents.oneIncr().build()));
//		assertThat(lot.getHighestBid(), equalTo(prevHighestBid.bumpUpOne()));
//		assertThat(userWithPrevHigestBid, equalTo(lot.getLeadingBidder()));
//		assertThat(pendingAutoBid, equalTo(lot.getPendingAutobid()));
//	}
//
//	@Test
//	void placeBid_thatIsEqualToAutoBid_autoBidIsRemoved() throws Exception {
//		Lot lot = testLotService.getMeSimpleLot());
//		lot.placeBid(Bid.giveDependencies(TestBidComponents.threeIncr().build()));
//		Class<?> prevHighestBid = lot.getHighestBid();
//		User userWithPrevHigestBid = prevHighestBid.getUser();
//		assertThat(lot.getPendingAutoBit(), is(true));
//		lot.placeBid(Bid.giveDependencies(TestBidComponents.twoIncr().build()));
//		assertThat(lot.getHighestBid(), equalTo(prevHighestBid.bumpUpTwo()));
//		assertThat(userWithPrevHigestBid, equalTo(lot.getLeadingBidder()));
//		Exception exception = assertThrows(Exception.class, () -> {
//			lot.getPendingAutobid();
//		});
//		String expectedMessage = "Lot doesn't have an autobid";
//		assertThat(exception.getMessage(), equalTo(expectedMessage));
//	}
//
//	@Test
//	void placeBid_thatIsAboveAutoBid_leadingBidderChanges() throws Exception {
//		Lot lot = testLotService.getMeSimpleLot());
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
//		String expectedMessage = "Lot doesn't have an autobid";
//		assertThat(exception.getMessage(), equalTo(expectedMessage));
//	}
//
//	@Test
//	void triggerDurationPeriodOnLot_andPushTimeToEnd_endsLot
//	{
//		Lot lot = testLotService.getMeSimpleLot());
//		lot.beginTriggerDuration();
//		lot.setTimeToEnd();
//		Exception exception = assertThrows(Exception.class, () -> {
//			lot.placeBid(Bid.giveDependencies(TestBidComponents.oneIncr().build()));
//		});
//		String expectedMessage = "Bid rejected - Lot expired";
//		assertThat(exception.getMessage(), equalTo(expectedMessage));
//	}
//
//	@Test
//	void placeBidWithin_triggerDuration_extendsEndTime
//	{
//		Lot lot = testLotService.getMeSimpleLot());
//
//		lot.beginTriggerDuration();
//		assertThat(lot.getEndTime() < (lot.getTickingTime() - Duration.ofMinutes(3)));
//		lot.placeBid(Bid.giveDependencies(TestBidComponents.oneIncr().build()));
//		assertThat(lot.getEndTime() > (lot.getTickingTime() - Duration.ofMinutes(3)));
//
//	}

}
