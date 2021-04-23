package com.project.biddingSoft.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FactoryService {

	@Bean
	IDaoService getUserServiceImpl() {
		return new UserDaoServiceImpl();
	}

	@Bean
	IDaoService getLotServiceImpl() {
		return new LotDaoServiceImpl();
	}

	@Bean
	IDaoService getBidServiceImpl() {
		return new BidDaoServiceImpl();
	}

}
