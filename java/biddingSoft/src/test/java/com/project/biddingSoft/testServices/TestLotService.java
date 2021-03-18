package com.project.biddingSoft.testServices;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.project.biddingSoft.domain.Bid;
import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.domain.Lot.LotBuilder;
import com.project.biddingSoft.domain.User;
import com.project.biddingSoft.service.LotService;
import com.project.biddingSoft.service.UserService;

@Component
public class TestLotService {

	@Autowired
	UserService userService;

	public Lot getMeSimpleLot() {
		return new Lot.LotBuilder(new ArrayList<Bid>()).user(userService.getMeSimpleUser())
				.description(UserService.genRandString(20, false)).endTime(LotService
						.between(Instant.now().plus(Duration.ofDays(1)), Instant.now().plus(Duration.ofDays(2))))
				.build();
	}

}
