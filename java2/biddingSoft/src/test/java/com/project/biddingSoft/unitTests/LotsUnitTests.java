package com.project.biddingSoft.unitTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
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
import org.testcontainers.shaded.org.apache.commons.lang.reflect.FieldUtils;

import com.project.biddingSoft.controller.ServletInitializer;
import com.project.biddingSoft.domain.Bid;
import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.domain.User;
import com.project.biddingSoft.testServices.TestBidService;
import com.project.biddingSoft.testServices.TestLotService;
import com.project.biddingSoft.testServices.TestUserService;

@AutoConfigureMockMvc
@SpringBootTest
//@RunWith(SpringRunner.class)
@RunWith(JUnitPlatform.class)
public class LotsUnitTests    {

	 
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
	@Test
	public void contextLoads() throws Exception {
		assertThat(servletInitializer).isNotNull();

	}
	
	
	@Test
	public void test_app_is_up() throws IOException {
		RestTemplate restTemplate =   new TestRestTemplate().getRestTemplate();
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
	public void createLot_canAddBidWithLot_Toit() throws Exception {
		Bid bid = testBidService.getOneIncrBid();
		Lot.addBid( bid.getLot(), bid);
		assertThat(bid.getLot().getHighestBid(), equalTo(bid));
		
	}

	@Test
	public void ableTo_createBid_AddLotToIt_andSaveToDtbs() throws Exception{
		Bid bid = testBidService.getOneIncrBid();		 
		Lot.addBid(bid.getLot(), bid);
		bid.getLot().saveToRepo();
	 	 assertThat(true, equalTo(bid.find().isPresent()));

	 	
	}
	
	@Test 
	public	void deleteBid_leavesLotInDtbs(){
		Bid bid = testBidService.getOneIncrBid();	
		Lot lot = bid.getLot();
		Lot.addBid(lot, bid);
		lot.saveToRepo();
		lot.getHighestBid().delete();
	 	 assertThat(Optional.empty(), equalTo(bid.find()));
		 assertThat(true, equalTo(lot.find().isPresent()));
	}
	@Test 
	public	void deleteLot_leavesUserInDtbs() throws IllegalAccessException{
		Bid bid = testBidService.getOneIncrBid();	
		Lot lot = bid.getLot();
		Lot.addBid(lot, bid);
		lot.saveToRepo();
	 	User user = (User)FieldUtils.readField(lot,"user",true);
	 	lot.delete();
	  assertThat(true, equalTo(user.find().isPresent()));

	}
	//@Transactional
	@Test 
	public	void deleteLot_removesAllRelatedBids(){
		Bid bid = testBidService.getOneIncrBid();
		Lot lot = bid.getLot();
		Lot.addBid(lot, bid);
		lot.saveToRepo();
	 	assertThat(true, equalTo(bid.find().isPresent()));
		lot.delete();
		assertThat(true, equalTo(bid.find().isEmpty()));
	}
//	@Transactional
	@Test 
	public	void deleteUser_removesAllRelatedLots() throws IllegalAccessException{
		Bid bid = testBidService.getOneIncrBid();
		Lot lot = bid.getLot();
		Lot.addBid(lot, bid);
		User user = (User)FieldUtils.readField(lot,"user",true);
		user.saveToRepo();	
		assertThat(true, equalTo(lot.find().isPresent()));
		user.delete();
	  	assertThat(true, equalTo(lot.find().isEmpty()));
	  	assertThat(true, equalTo(bid.find().isEmpty())); //should work?!

	}
	@Test
	public	void whenLotIsAuoWired_autowiredAttributes_areSet() throws NoSuchFieldException, IllegalAccessException {
	assertThat(FieldUtils.readField(wiredLot,"title",true)).isNotNull(); 
	assertThat(FieldUtils.readField(wiredLot,"description",true)).isNotNull();
	assertThat(FieldUtils.readField(wiredLot,"ZONE",true)).isNotNull();
	assertThat(FieldUtils.readField(wiredLot,"biddingIncrement",true)).isNotNull(); 
	assertThat(FieldUtils.readField(wiredLot,"startingPrice",true)).isNotNull(); 
	assertThat(FieldUtils.readField(wiredLot,"triggerDuration",true)).isNotNull();
	assertThat(FieldUtils.readField(wiredLot,"autoExtendDuration",true)).isNotNull(); 
 	assertThat(FieldUtils.readField(wiredLot,"iLotRepo",true)).isNotNull();
 	assertThat(FieldUtils.readField(wiredLot,"clock",true)).isNotNull();
 	assertThat(FieldUtils.readField(wiredLot,"bidSoftExcepFactory",true)).isNotNull();
		
	}
	@Test
	public	void whenLotIsCreated_extendedEndTimeEqueals_endTime() throws  IllegalAccessException{
		Lot lot = testLotService.getMeSimpleLot();
		assertThat(FieldUtils.readField(lot,"extendedEndtime",true), equalTo(FieldUtils.readField(lot,"endTime",true)));
	}

 
	@Test
	public void placeOneBidIncr_bumpsHighestBidUp_byOne() throws Exception {
		Bid prevHighestBid = testBidService.getOneIncrBid(testLotService.getMeLotWithTriggerDuration());
		Lot lot = prevHighestBid.getLot();
		Lot.addBid(lot, prevHighestBid);
		lot.placeBid(testBidService.getOneIncrBid(lot));
		//System.out.println(lot.getTriggerDuration() +" "+ lot.getAutoExtendDuration());  
		assertThat(lot.getHighestBid().getAmount(), equalTo(TestBidService.getOneincr()));
	}
	@Test
	public void whenLotIsCreated_autowiredAttributes_areSet() throws IllegalAccessException{
		Bid bid = testBidService.getOneIncrBid(testLotService.getMeLotWithTriggerDuration());
		assertThat(FieldUtils.readField(bid.getLot(),"clock",true)).isNotNull();
		assertThat(FieldUtils.readField(wiredLot,"bidSoftExcepFactory",true)).isNotNull();
		
	}

//	@Test
//	void placeOneBid_equalOrAfterEndTime_ThrowsException() throws Exception {
//		Lot lot = testLotService.getMeLotWithTriggerDuration(); 
//		Bid bid = testBidService.getOneIncrBid(lot);
//		Instant endTime = lot.getEndTime();
//		FieldUtils.writeDeclaredField(bid.getLot(),"clock", Clock.fixed(endTime,ZoneId.of("UTC")), true);
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
