package com.project.biddingSoft.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import com.project.biddingSoft.dao.IUserRepo;

@Configuration
public class FactoryService {
	
	@Bean
	IService getUserServiceImpl() {
		return new UserServiceImpl();
	}
	@Bean
	IService getLotServiceImpl() {
		return new LotServiceImpl();
	}
	@Bean
	IService getBidServiceImpl() {
		return new BidServiceImpl();
	}
	

}
