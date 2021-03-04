package com.project.biddingSoft;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.biddingSoft.controller.ServletInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.hamcrest.Matchers.*; 
import static org.hamcrest.MatcherAssert.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;

@RunWith(SpringRunner.class)
@ComponentScan(basePackages = {"com.project.biddingSoft"})
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.DEFINED_PORT)
class BiddingSoftwareApplicationIntegTests {
	
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_RESET = "\u001B[0m";
	private static final Logger logger = LoggerFactory.getLogger(BiddingSoftwareApplicationIntegTests.class);
	
	
	@Test
	void test() throws IOException {
		RestTemplate restTemplate =   new TestRestTemplate().getRestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
         assertThat(response.getBody(), equalTo("Hello World from Nuchem"));
         logger.info(ANSI_RED + response.getBody() + ANSI_RESET);
	
	}
	
	

}
