package com.project.biddingSoft.service;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

@Component
public class LotService {

	public static Instant between(Instant startInclusive, Instant endExclusive) {// https://www.baeldung.com/java-random-dates
		long startSeconds = startInclusive.getEpochSecond();
		long endSeconds = endExclusive.getEpochSecond();
		long random = ThreadLocalRandom.current().nextLong(startSeconds, endSeconds);

		return Instant.ofEpochSecond(random);
	}
	

}
