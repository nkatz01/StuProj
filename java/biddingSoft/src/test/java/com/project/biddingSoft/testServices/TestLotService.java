package com.project.biddingSoft.testServices;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
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

	/**
	 * @return a lot with the minimum dependencies needed such as user, start and end times and trigger duration etc.
	 */
	public Lot getMeSimpleLot() {
		return new Lot.LotBuilder(new HashSet<Bid>()).user(testUserService.getMeSimpleUser())
				.description(TestUserService.genRandString(20, false))
				//a lot that starts now (default in lot class) and ends between a day from now to two days from now.
				.endTime(LotService.between(Instant.now().plus(Duration.ofDays(1)),
						Instant.now().plus(Duration.ofDays(2))))
				.triggerDuration(Duration.ofMinutes(2)).autoExtendDuration(Duration.ofMinutes(5))
				.timeZone(ZoneId.systemDefault()).biddingIncrement(5.0).build();
	}

	/**
	 * @param user
	 * @return a simple lot created by the <b>user</b> provided.
	 */
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

	
	public User lotsUser(Optional<Lot> lot) {
		return lot.get().getUser();
	}

}
