package com.project.biddingSoft.testServices;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.biddingSoft.domain.Bid;
import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.service.LotService;
@Component
public class TestLotService {
	  
	@Autowired
	TestUserService testUserService;
	public   Lot getMeSimpleLot() {
		return new Lot.LotBuilder(new ArrayList<Bid>())
				.user( testUserService.getMeSimpleUser())
				 //.description(TestUserService.genRandString(20, false))
				.endTime(LotService.between(Instant.now().plus(Duration.ofDays(1)), Instant.now().plus(Duration.ofDays(2)) ))
				.build();
	}
	
	public   Lot getMeLotWithTriggerDuration() {
		return new Lot.LotBuilder(new ArrayList<Bid>())
				.user( testUserService.getMeSimpleUser())
				 .description(TestUserService.genRandString(20, false))
				.endTime(LotService.between(Instant.now().plus(Duration.ofDays(1)), Instant.now().plus(Duration.ofDays(2)) ))
				.triggerDuration(Duration.ofMinutes(2000))
				.autoExtendDuration(Duration.ofMinutes(5000))
				.build();
	}
	
	
	
	
	
	
	
	
	
}
