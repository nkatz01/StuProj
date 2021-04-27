package com.project.biddingSoft;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.internal.TextListener;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.shaded.org.apache.commons.lang.reflect.FieldUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.project.biddingSoft.controller.ServletInitializer;
import com.project.biddingSoft.dao.IBidRepo;
import com.project.biddingSoft.dao.ILotRepo;
import com.project.biddingSoft.dao.IStorableRepo;
import com.project.biddingSoft.dao.IUserRepo;
import com.project.biddingSoft.domain.Bid;
import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.domain.Storable;
import com.project.biddingSoft.domain.User;
import com.project.biddingSoft.testServices.TestStorableService;
import com.project.biddingSoft.testServices.TestBidService;
import com.project.biddingSoft.testServices.TestLotService;
import com.project.biddingSoft.testServices.TestUserService;
import com.project.biddingSoft.unitTests.LotsUnitTests;

@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
@RunWith(JUnitPlatform.class)

@SelectClasses({ BiddingSoftwareApplicationIntegTests.class, LotsUnitTests.class })
@ComponentScan(basePackages = { "com.project.biddingSoft" })
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class BiddingSoftwareApplicationIntegTests {

	@Value("${service.url}")
	String serverAddress;
	@Autowired
	ServletInitializer servletInitializer;
	@Autowired
	@Qualifier("getTestRestTemplate")
	RestTemplate restTemplate;
	@Autowired
	Lot wiredLot;
	@Autowired
	TestLotService testLotService;
	@Autowired
	TestBidService testBidService;
	@Autowired
	TestUserService testUserService;
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	@Autowired
	private IUserRepo iUserRepo;
	@Autowired
	private ILotRepo iLotRepo;
	@Autowired
	private IBidRepo iBidrepo;
	@Autowired
	private  IStorableRepo<Storable> iStorableRepo;

	

	public static void main(String[] args) {

		JUnitCore junit = new JUnitCore();
		junit.addListener(new TextListener(System.out));
		junit.run(LotsUnitTests.class);

	}

	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_RESET = "\u001B[0m";
	private static final Logger logger = LoggerFactory.getLogger(BiddingSoftwareApplicationIntegTests.class);

	@BeforeAll
	void removeAllTables() {
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
		user.addLotToSet(wiredLot);
		wiredLot.setUser(user);
		Bid bid = testBidService.getOneIncrBid(wiredLot);// for the purpose of endpoints' tests

		wiredLot.placeBid(bid);
		iUserRepo.save(wiredLot.getUser());
		
	}

	@Test
	void test_app_is_up() throws IOException {
		ResponseEntity<String> response = restTemplate.getForEntity(serverAddress + "/", String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Service running", response.getBody());
	}

	@Test
	void whenLotIsAutoWired_autowiredAttributes_areSet() throws NoSuchFieldException, IllegalAccessException {
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
	void contextLoads() throws Exception {
		assertThat(servletInitializer).isNotNull();
		logger.info(ANSI_RED + "Tests running" + ANSI_RESET);

	}

	@Test
	@Order(1)
	void testThatSuperRepo_canReturnSubclass_ofStorable() {
		assertTrue(iStorableRepo.findById(wiredLot.getId()).isPresent());
	}

	
	@Test
	@Order(2)
	void testPostEntity_forUser() throws IOException, URISyntaxException {

		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(new URI(serverAddress + "/create"));
		StringEntity params = new StringEntity("{\"type\": \"user\"}");
		request.addHeader("content-type", "application/json");
		request.setEntity(params);
		HttpResponse response = httpClient.execute(request);
		assertEquals(201, response.getStatusLine().getStatusCode());

	}

	@Test
	@Order(3)
	void testPostEntity_newLotWithExistingUser() throws IOException, URISyntaxException {

		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(new URI(serverAddress + "/create"));
		StringEntity params = new StringEntity(
				"{\"type\":\"lot\", \"user\":{\"id\": " + wiredLot.getUser().getId() + " }}");
		request.addHeader("content-type", "application/json");
		request.setEntity(params);
		HttpResponse response = httpClient.execute(request);
		assertEquals(201, response.getStatusLine().getStatusCode());

	}

	@Test
	@Order(4)
	void testPostEntity_newBidWithExistingUser_AndExistingBidder() throws IOException, URISyntaxException {

		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(new URI(serverAddress + "/create"));
		StringEntity params = new StringEntity(
				"{\"type\":\"bid\", \"lot\": {\"type\":\"lot\",\"user\":{\"id\": " + wiredLot.getUser().getId()
						+ "}}, \"bidder\":{\"id\": " + wiredLot.getLeadingBidder().getId() + "} }\r\n" + "");
		request.addHeader("content-type", "application/json");
		request.setEntity(params);
		HttpResponse response = httpClient.execute(request);
		assertEquals(201, response.getStatusLine().getStatusCode());

	}

	@Test
	@Order(5)
	void assertWiredLot_andItsBid_areInDtbs_andIdsAreNotNull() {
		assertTrue(iLotRepo.existsById(wiredLot.getId()));
		assertTrue(iBidrepo.existsById(wiredLot.getBid(0).getId()));

	}

	@Test
	@Order(6)
	void testGetOneEntity() throws IOException, URISyntaxException, JSONException {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(new URI(serverAddress + "/getent/" + wiredLot.getId()));
		request.addHeader("content-type", "application/json");
		HttpResponse response = httpClient.execute(request);
		assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
		Lot lot = new ObjectMapper().findAndRegisterModules().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.readValue(EntityUtils.toString(response.getEntity()), Lot.class);
		assertEquals(wiredLot.getId(), lot.getId());
	}

	@Test
	@Order(7)
	void testUpdateUser() throws IOException, URISyntaxException {
		String username = iUserRepo.findById(wiredLot.getUser().getId()).get().getUsername();
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPut request = new HttpPut(new URI(serverAddress + "/update"));
		StringEntity params = new StringEntity(
				"{\"type\": \"userdto\",\"id\": " + wiredLot.getUser().getId() + ", \"username\":\"Jim\"}");
		request.addHeader("content-type", "application/json");
		request.setEntity(params);
		HttpResponse response = httpClient.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertNotEquals(username, iUserRepo.findById(wiredLot.getUser().getId()).get().getUsername());

	}

	@Test
	@Order(8)
	void testUpdateLot() throws IOException, URISyntaxException {
		User newUser = testUserService.getMeSimpleUser();
		iUserRepo.save(newUser);
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPut request = new HttpPut(new URI(serverAddress + "/update"));
		StringEntity params = new StringEntity("{\"type\": \"lotdto\",\"id\": " + wiredLot.getId()
				+ ", \"reservePrice\":\"50.0\", \"user\": {\"id\":" + newUser.getId() + "}}");
		request.addHeader("content-type", "application/json");
		request.setEntity(params);
		HttpResponse response = httpClient.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals(50.0, iLotRepo.findById(wiredLot.getId()).get().getReservePrice());
		assertEquals(newUser.getId(), iLotRepo.findById(wiredLot.getId()).get().getUser().getId());

	}

	@Test
	@Order(9)
	void testUpdateBid() throws IOException, URISyntaxException {
		Lot newLot = testLotService.getMeSimpleLot();
		iUserRepo.save(newLot.getUser());
		iLotRepo.save(newLot);
		User newBidder = testUserService.getMeSimpleUser();
		iUserRepo.save(newBidder);

		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPut request = new HttpPut(new URI(serverAddress + "/update"));
		StringEntity params = new StringEntity("{\"type\": \"biddto\",\"id\": " + wiredLot.getBid(0).getId()
				+ ", \"amount\":\"20.0\", \"bidder\": {\"id\":" + newBidder.getId()
				+ "}, \"lot\": {\"type\":\"lot\",\"id\":" + newLot.getId() + "}}");
		request.addHeader("content-type", "application/json");
		request.setEntity(params);
		HttpResponse response = httpClient.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals(newLot.getId(), iBidrepo.findById(wiredLot.getBid(0).getId()).get().getLot().getId());
		assertEquals(newBidder.getId(), iBidrepo.findById(wiredLot.getBid(0).getId()).get().getBidder().getId());
		assertEquals(20.0, iBidrepo.findById(wiredLot.getBid(0).getId()).get().getAmount());

	}

	@Test
	@Order(10)
	void testGetAllEnts() throws IOException, URISyntaxException, JSONException {

		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(new URI(serverAddress + "/allents"));
		request.addHeader("content-type", "application/json");
		HttpResponse response = httpClient.execute(request);
		assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
		JSONArray json = new JSONArray(EntityUtils.toString(response.getEntity()));
		assertEquals(json.length(), iStorableRepo.count());
		}

	@Test
	@Order(11)
	void testDelOneEnt() throws IOException, URISyntaxException, JSONException {

		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpDelete request = new HttpDelete(new URI(serverAddress + "/delent/" + wiredLot.getId()));
		HttpResponse response = httpClient.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertTrue(!iLotRepo.existsById(wiredLot.getId()));
	}

	@Test
	void testDelAllEnts() throws IOException, URISyntaxException, JSONException {

		assertTrue(iStorableRepo.count() > 0);
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpDelete request = new HttpDelete(new URI(serverAddress + "/delents"));
		HttpResponse response = httpClient.execute(request);
		assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
		assertTrue(iStorableRepo.count() == 0);
	}

}
