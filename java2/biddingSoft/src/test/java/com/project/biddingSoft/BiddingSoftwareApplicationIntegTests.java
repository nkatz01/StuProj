package com.project.biddingSoft;

import org.junit.internal.TextListener;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import com.project.biddingSoft.unitTests.LotsUnitTests;

@RunWith(JUnitPlatform.class)
@SelectClasses( LotsUnitTests.class )
//@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ComponentScan(basePackages = {"com.project.biddingSoft"})
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:application.properties")//change to separate prop file
class BiddingSoftwareApplicationIntegTests {
	 
	 public static void main(String[] args) {
			
		  JUnitCore junit = new JUnitCore();
		  junit.addListener(new TextListener(System.out));
		  junit.run(LotsUnitTests.class);
		  
		  
//		  Result result = JUnitCore.runClasses(LotsUnitTests.class);
//		  for (Failure failure : result.getFailures())
//			System.out.println(failure.toString());
//		  System.out.println(result.wasSuccessful());
		  }

	  
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_RESET = "\u001B[0m";
	private static final Logger logger = LoggerFactory.getLogger(BiddingSoftwareApplicationIntegTests.class);
	

//	
//	@Test
//	void test_get_all_entities() throws Exception {
//		RestTemplate restTemplate =   new TestRestTemplate().getRestTemplate();
// 
//        mvc.perform(get("http://localhost:8080/allents/lot")
//          .contentType(MediaType.APPLICATION_JSON))
//          .andExpect(status().isOk());
//	 
//	}
	

	

}
