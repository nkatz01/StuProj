package com.project.biddingSoft.testServices;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TestConfigurationService {
	
	@Bean
	public RestTemplate getTestRestTemplate() {
		return new  TestRestTemplate().getRestTemplate();
	}

}
