package com.project.biddingSoft.testServices;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.biddingSoft.domain.Bid;
import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.domain.User;

@Component
public class TestLotService {

	@Autowired
	TestUserService testUserService;

	public Lot getMeSimpleLot() {
		return new Lot.LotBuilder(new HashSet<Bid>()).user(testUserService.getMeSimpleUser())
				.description(TestUserService.genRandString(20, false))
				.endTime(LotService.between(Instant.now().plus(Duration.ofDays(1)),
						Instant.now().plus(Duration.ofDays(2))))
				.triggerDuration(Duration.ofMinutes(2)).autoExtendDuration(Duration.ofMinutes(5))
				.timeZone(ZoneId.systemDefault()).biddingIncrement(5.0).build();
	}

	public Lot getMeSimpleLot(User user) {
		return new Lot.LotBuilder(new HashSet<Bid>()).user(user).description(TestUserService.genRandString(20, false))
				.endTime(LotService.between(Instant.now().plus(Duration.ofDays(1)),
						Instant.now().plus(Duration.ofDays(2))))
				.triggerDuration(Duration.ofMinutes(2)).autoExtendDuration(Duration.ofMinutes(5))
				.timeZone(ZoneId.systemDefault()).biddingIncrement(5.0).build();
	}

	public Lot getMeLotWithStrtingPrice(double startingPrice) {
		return new Lot.LotBuilder(new HashSet<Bid>()).user(testUserService.getMeSimpleUser())
				.description(TestUserService.genRandString(20, false))
				.endTime(LotService.between(Instant.now().plus(Duration.ofDays(1)),
						Instant.now().plus(Duration.ofDays(2))))
				.triggerDuration(Duration.ofMinutes(2)).autoExtendDuration(Duration.ofMinutes(5))
				.timeZone(ZoneId.systemDefault()).biddingIncrement(5.0).startingPrice(startingPrice).build();
	}

	public User lotsUser(Lot lot) {
		return lot.getUser();
	}

	public User lotsUser(Optional<Lot> lot) {
		return lot.get().getUser();
	}

}
