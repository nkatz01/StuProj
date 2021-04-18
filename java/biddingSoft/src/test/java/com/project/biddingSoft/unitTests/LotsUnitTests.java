package com.project.biddingSoft.unitTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.shaded.org.apache.commons.lang.reflect.FieldUtils;
import org.testcontainers.shaded.org.bouncycastle.util.test.TestFailedException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.project.biddingSoft.controller.ServletInitializer;
import com.project.biddingSoft.dao.IBidRepo;
import com.project.biddingSoft.dao.ILotRepo;
import com.project.biddingSoft.dao.IStorableRepo;
import com.project.biddingSoft.dao.IUserRepo;
import com.project.biddingSoft.domain.Bid;
import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.domain.Storable;
import com.project.biddingSoft.domain.User;
import com.project.biddingSoft.service.ExceptionsCreateor;
import com.project.biddingSoft.testServices.TestBidService;
import com.project.biddingSoft.testServices.TestLotService;
import com.project.biddingSoft.testServices.TestUserService;


import junit.framework.TestFailure;
@TestMethodOrder(OrderAnnotation.class)
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
	@Qualifier("getTestRestTemplate")
	RestTemplate restTemplate;
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	@Autowired
	private IUserRepo iUserRepo;
	@Autowired 
	private ILotRepo iLotRepo;
	@Autowired 
	private IBidRepo iBidrepo;
	@Autowired
	private static IStorableRepo<Storable> iStorableRepo;
	@Autowired
	@Qualifier("IStorableRepo")
	public void setIStorable(IStorableRepo istorableRepo) {
		iStorableRepo = istorableRepo;
	}
	@Test
	@Order(1)
	public void testThatSuperRepo_canReturnSubclass_ofStorable() {
		assertTrue(iStorableRepo.findById(wiredLot.getId()).isPresent());
	}
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
		entityManager.createNativeQuery("TRUNCATE TABLE storable;").executeUpdate();
		entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1;").executeUpdate();
		entityManager.createNativeQuery("SET SQL_SAFE_UPDATES = 0;").executeUpdate();
		entityManager.createNativeQuery("UPDATE  biddingsoft.hibernate_sequence SET next_val = 1;").executeUpdate();
		entityManager.createNativeQuery("SET SQL_SAFE_UPDATES = 1;").executeUpdate();

	entityManager.flush();
		entityManager.getTransaction().commit();
		
		User user = testUserService.getMeSimpleUser();
		user.addLotToList(wiredLot);
		wiredLot.setUser(user);
		
		Bid bid = testBidService.getOneIncrBid(wiredLot);//for the purpose of endpoints' tests
	wiredLot.placeBid(bid);
		 iUserRepo.save(wiredLot.getUser());//error bidder inside Bid and lot inside Bid are transient still
		 iUserRepo.save(bid.getBidder());
	 	iBidrepo.save(bid); 
	

			
		

	}


	@Test
	public void test_app_is_up() throws IOException {
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/", String.class);
		assertEquals(HttpStatus.OK,response.getStatusCode());
		assertEquals( "Service running",response.getBody());
	}
	
	@Test
	public void newLot_canBeSavedToDtbs() throws Exception {

		Lot lot = testLotService.getMeSimpleLot();
		iUserRepo.save(lot.getUser());
		assertTrue(iLotRepo.existsById(lot.getId()));

	}


	@Test
	public void createTwoLots_areNotTheSame() throws Exception {
		Lot lot1 = testLotService.getMeSimpleLot();
		Lot lot2 = testLotService.getMeSimpleLot();
		assertNotEquals(lot1, lot2);
	}

	@Test
	public void ableTo_createBid_AddLotToIt_andSaveToDtbs() throws Exception {
		Bid bid = testBidService.getOneIncrBid(testLotService.getMeSimpleLot());
		iUserRepo.save(testLotService.lotsUser(bid.getLot().placeBid(bid)));
		assertTrue(iBidrepo.existsById(bid.getId()));

	}
	@Test
	public void addingSecondBidOnLot_doesntAffectFirstBid() {
		Lot lot = testLotService.getMeSimpleLot();
		Bid bid = testBidService.getOneIncrBid(lot);
		iUserRepo.save(testLotService.lotsUser(lot.placeBid(bid)));
		
		Bid bid2 = testBidService.getBidBumpedUpByOneIncrMore(lot,bid);
		lot.placeBid(bid2);
		iLotRepo.save(bid.getLot());
		assertEquals(bid.getLot().getId(), lot.getId());	
		
	}
	//@Transactional//because lots are loaded eagurly together with bids, bids won't get deleted. 
	//I guess the reason is because of the reference to lot maintained within bid's FK column named  lot_id
	@Test
	public void deleteBid_leavesLotInDtbs() {
		Bid bid = testBidService.getOneIncrBid(testLotService.getMeSimpleLot());
		Lot lot = bid.getLot();
		iUserRepo.save(testLotService.lotsUser(lot.placeBid(bid)));
		//iStorableRepo.save(testLotService.lotsUser(lot.placeBid(bid)));
		assertTrue(iBidrepo.existsById(bid.getId()));
		iBidrepo.delete(bid);
		//iStorableRepo.delete(bid);
		 assertTrue(iLotRepo.existsById(lot.getId()));
		 assertTrue(!iLotRepo.existsById(bid.getId()));
	}

	@Test
	public void deleteLot_leavesUserInDtbs() throws IllegalAccessException {
		Bid bid = testBidService.getOneIncrBid(testLotService.getMeSimpleLot());
		Lot lot = bid.getLot();
		iUserRepo.save(testLotService.lotsUser(lot.placeBid(bid)));
		
		User user = lot.getUser();
		iLotRepo.delete(lot);
		assertTrue(iUserRepo.existsById(user.getId()));

	}

	@Test
	public void deleteLot_removesAllRelatedBids() {
		Bid bid = testBidService.getOneIncrBid(testLotService.getMeSimpleLot());
		Lot lot = bid.getLot();
		iUserRepo.save(testLotService.lotsUser(lot.placeBid(bid)));
		
		assertTrue(iBidrepo.existsById(bid.getId()));
		iLotRepo.delete(lot);
		assertTrue(!iBidrepo.existsById(bid.getId()));
	} 

	@Test
	public void deleteUser_removesAllRelatedLots() throws IllegalAccessException {
		Bid bid = testBidService.getOneIncrBid(testLotService.getMeSimpleLot());
		Lot lot = bid.getLot();
		iUserRepo.save(testLotService.lotsUser( lot.placeBid(bid)));
		assertTrue(iLotRepo.existsById(lot.getId()));
		iUserRepo.delete(lot.getUser());
		assertTrue(!iLotRepo.existsById(lot.getId()));
		assertTrue(!iBidrepo.existsById(bid.getId()));

	}

	@Test
	public void whenLotIsAutoWired_autowiredAttributes_areSet() throws NoSuchFieldException, IllegalAccessException {
		assertNotNull(FieldUtils.readField(wiredLot, "title", true));
		assertNotNull(FieldUtils.readField(wiredLot, "description", true));
		assertNotNull(FieldUtils.readField(wiredLot, "ZONE", true));
		assertNotNull(FieldUtils.readField(wiredLot, "biddingIncrement", true));
		assertNotNull(FieldUtils.readField(wiredLot, "startingPrice", true));
		assertNotNull(FieldUtils.readField(wiredLot, "triggerDuration", true));
		assertNotNull(FieldUtils.readField(wiredLot, "autoExtendDuration", true));
		assertNotNull(FieldUtils.readField(wiredLot, "clock", true));
		assertNotNull(FieldUtils.readField(wiredLot, "bidSoftExcepFactory", true));

	}

	@Test
	public void whenLotIsCreated_extendedEndTimeEqueals_endTime() throws IllegalAccessException {
		Lot lot = testLotService.getMeSimpleLot();
		assertEquals(FieldUtils.readField(lot, "endTime", true),FieldUtils.readField(lot, "extendedEndtime", true));
	}

	@Test
	public void whenLotIsCreated_autowiredAttributes_areSet() throws IllegalAccessException {
		Bid bid = testBidService.getOneIncrBid(testLotService.getMeSimpleLot());
		assertNotNull(FieldUtils.readField(bid.getLot(), "clock", true));
		assertNotNull(FieldUtils.readField(bid.getLot(), "bidSoftExcepFactory", true));

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
		assertEquals(exception.getId(), expectedExcepId);
		assertEquals("lot endTime is qual or before current time",exception.getCause().getMessage());
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
		assertEquals(exception.getId(), expectedExcepId);
	}

	@Test
	public void bidWhenThereIsNoStartingPrice_thatIsEqualToOneBidIncr_isAccepted() {
		Bid bid = testBidService.getOneIncrBid(testLotService.getMeSimpleLot());
		bid.getLot().placeBid(bid);
		assertTrue(bid.getLot().contains(bid));

	}

	@Test
	public void bidBelowStartingPrice_isRefused() {
		Lot lot = testLotService.getMeLotWithStrtingPrice(7.0);
		ExceptionsCreateor.BidTooLow exception = assertThrows(ExceptionsCreateor.BidTooLow.class, () -> {
			lot.placeBid(testBidService.getOneIncrBid(lot));
		});
		String expectedExcepId = "2";
		assertEquals(exception.getId(), expectedExcepId);
	}

	@Test
	public void bidEqualToStartingPrice_isAccepted() {
		Lot lot = testLotService.getMeLotWithStrtingPrice(5.0);
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
		assertEquals(lot.getLeadingBidder(),bidder2);
	}

	@Test
	public void placeOneBidIncr_bumpsHighestBidUp_byOneIncr() throws Exception {
		Bid prevHighestBid = testBidService.getOneIncrBid(testLotService.getMeSimpleLot());
		Lot lot = prevHighestBid.getLot();
		lot.placeBid(prevHighestBid);
		lot.placeBid(testBidService.getOneIncrBid(lot));
		assertEquals(testBidService.bumpUpOne(prevHighestBid),lot.getHighestBid());
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
		assertEquals(lot.getBiddingIncrement(), lot.getHighestBid());
		assertEquals(autoBid,lot.getPendingAutoBid());

		Bid newBid = testBidService.getOneIncrBid(lot);
		lot.placeBid(newBid);
		assertEquals(testBidService.bumpUpOne(newBid),lot.getHighestBid());
		assertEquals(lot.getLeadingBidder(),autoBid.getBidder());

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
		assertEquals(expectedExcepId,exception.getId());
		assertEquals(autoBid.getAmount(),lot.getHighestBid() );
		assertEquals(autoBidder,lot.getLeadingBidder() );
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
		assertEquals(expectedExcepId,exception.getId());
	}

	@Test
	void placeBid_thatIsAboveAutoBid_withMoreThanOneIncr_becomesAutoBid() throws Exception {
		Lot lot = testLotService.getMeSimpleLot();
		lot.placeBid(testBidService.getTwoIncrBid(lot));
		Bid newBid = testBidService.getThreeIncrBid(lot);// bids at 20
		assertEquals(testBidService.bumpOneDown(newBid),lot.placeBid(newBid).get().getHighestBid());	//new highest bid is only raised to one incrementA																								
		assertEquals(lot.getLeadingBidder(),newBid.getBidder());
		assertEquals(newBid,lot.getPendingAutoBid());

	}
	
//	//Test methods for endpoints
	
//	@Test
//	public void testGetEnts_forBid() throws IOException , URISyntaxException {
//			
//		
//			HttpClient httpClient = HttpClientBuilder.create().build();
//			HttpGetWithEntity request = new HttpGetWithEntity(new URI("http://localhost:8080/getent"));
//			StringEntity params = new StringEntity("{\"type\": \"bid\", \"id\": "+ wiredLot.getBid(0).getId() +"}");
//			request.addHeader("content-type",  "application/json");
//			request.setEntity(params);
//			HttpResponse response = httpClient.execute(request);
//			assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
//		
//	}
//	@Test
//	public void testGetEnts_forLot() throws IOException , URISyntaxException {
//			
//		
//			HttpClient httpClient = HttpClientBuilder.create().build();
//			HttpGetWithEntity request = new HttpGetWithEntity(new URI("http://localhost:8080/getent"));
//			StringEntity params = new StringEntity("{\"type\": \"lot\", \"id\": "+ wiredLot.getId() +"}");
//			request.addHeader("content-type",  "application/json");
//			request.setEntity(params);
//			HttpResponse response = httpClient.execute(request);
//			assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
//		
//	}
	@Test
	public void testGetAllEnts() throws IOException , URISyntaxException, JSONException {
	
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(new URI("http://localhost:8080/allents"));
		request.addHeader("content-type",  "application/json");
		HttpResponse response = httpClient.execute(request);
		assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
		JSONArray json = new JSONArray(EntityUtils.toString(response.getEntity()));
		assertEquals(json.length(),iStorableRepo.count() );
 	}
	@Test
	@Order(2)
	public void testPostEntity_forUser() throws IOException , URISyntaxException {
			
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost(new URI("http://localhost:8080/create"));
			StringEntity params = new StringEntity("{\"type\": \"user\"}");
			request.addHeader("content-type",  "application/json");
			request.setEntity(params);
			HttpResponse response = httpClient.execute(request);
			assertEquals(201, response.getStatusLine().getStatusCode());
		
	}
	@Test
	@Order(3)
	public void testPostEntity_newLotWithExistingUser() throws IOException , URISyntaxException {
			
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost(new URI("http://localhost:8080/create"));
			StringEntity params = new StringEntity("{\"type\":\"lot\", \"user\":{\"id\": "+ wiredLot.getUser().getId() +" }}");
			request.addHeader("content-type",  "application/json");
			request.setEntity(params);
			HttpResponse response = httpClient.execute(request);
			assertEquals(201, response.getStatusLine().getStatusCode());
		
	}
	@Test
	@Order(4)
	public void testPostEntity_newBidWithExistingUser_AndExistingBidder() throws IOException , URISyntaxException {
			
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost(new URI("http://localhost:8080/create"));
			StringEntity params = new StringEntity("{\"type\":\"bid\", \"lot\": {\"type\":\"lot\",\"user\":{\"id\": "+ wiredLot.getUser().getId() +"}}, \"bidder\":{\"id\": "+ wiredLot.getLeadingBidder().getId() +"} }\r\n"
					+ "");
			request.addHeader("content-type",  "application/json");
			request.setEntity(params);
			HttpResponse response = httpClient.execute(request);
			assertEquals(201, response.getStatusLine().getStatusCode());
		
	}
	@Test
	@Order(5)
	public void assertWiredLot_isInDtbs_andIdIsNotNull() {
		assertTrue(iLotRepo.existsById(wiredLot.getId()));
		assertTrue(iBidrepo.existsById(wiredLot.getBid(0).getId()));
	}
	@Test
	@Order(6)
	public void testGetOneEntity() throws IOException , URISyntaxException , JSONException{
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(new URI("http://localhost:8080/getent/"+ wiredLot.getId()));
	request.addHeader("content-type",  "application/json");
		HttpResponse response = httpClient.execute(request);
		assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
		Lot lot = new ObjectMapper()
				.findAndRegisterModules()
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.readValue(EntityUtils.toString(response.getEntity()), Lot.class);
		 assertEquals(wiredLot.getId(),lot.getId());		
	}

	@Test
	 @Order(7)
	public void testDelOneEnt() throws IOException , URISyntaxException, JSONException {
	
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpDelete request = new HttpDelete(new URI("http://localhost:8080/delent/" +wiredLot.getId()));
		HttpResponse response = httpClient.execute(request);
		assertEquals(200,response.getStatusLine().getStatusCode());
		assertTrue(!iLotRepo.existsById(wiredLot.getId()));
	}
	@Test
	 @AfterAll
	public void testDelAllEnts() throws IOException , URISyntaxException, JSONException {
	
		assertTrue(iStorableRepo.count() >0 );
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpDelete request = new HttpDelete(new URI("http://localhost:8080/delents"));
		HttpResponse response = httpClient.execute(request);
		assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
		assertTrue(iStorableRepo.count() == 0 );
 	}
//
//	@Test
//	public void testGetAllEnts_forBids() throws IOException , URISyntaxException {
//	
//		
//		HttpClient httpClient = HttpClientBuilder.create().build();
//		HttpGetWithEntity request = new HttpGetWithEntity(new URI("http://localhost:8080/allents"));
//		StringEntity params = new StringEntity("{\"type\": \"bid\"}");
//		request.addHeader("content-type",  "application/json");
//		request.setEntity(params);
//		HttpResponse response = httpClient.execute(request);
//		assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
//		
//	}
//	@Test
//	public void testGetAllEnts_forLots() throws IOException , URISyntaxException {
//
//		
//			HttpClient httpClient = HttpClientBuilder.create().build();
//			HttpGetWithEntity request = new HttpGetWithEntity(new URI("http://localhost:8080/allents"));
//			StringEntity params = new StringEntity("{\"type\": \"lot\"}");
//			request.addHeader("content-type",  "application/json");
//			request.setEntity(params);
//			HttpResponse response = httpClient.execute(request);
//			assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
//		
//	}
	
	
	@Test
	void checkThatAfterBidIsCreated_BidderGetsAssociation_withBid_onTheirObject() {
		
	}

}
