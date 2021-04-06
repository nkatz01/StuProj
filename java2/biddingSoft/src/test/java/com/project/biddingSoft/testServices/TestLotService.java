package com.project.biddingSoft.testServices;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
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
//	public   Lot getMeSimpleLot() {
//		return new Lot.LotBuilder(new ArrayList<Bid>())
//				.user( testUserService.getMeSimpleUser())
//				 //.description(TestUserService.genRandString(20, false))
//				.endTime(LotService.between(Instant.now().plus(Duration.ofDays(1)), Instant.now().plus(Duration.ofDays(2)) ))
//				.timeZone(ZoneId.systemDefault())
//				.build();
//	}
	
	public   Lot getMeSimpleLot() {
		return new Lot.LotBuilder(new ArrayList<Bid>())
				.user( testUserService.getMeSimpleUser())
				.description(TestUserService.genRandString(20, false))
				.endTime(LotService.between(Instant.now().plus(Duration.ofDays(1)), Instant.now().plus(Duration.ofDays(2)) ))
				.triggerDuration(Duration.ofMinutes(2))
				.autoExtendDuration(Duration.ofMinutes(5))
				.timeZone(ZoneId.systemDefault())
				.build();
	}
	
	
	
	
	
	
	
	
	
}
