package com.project.biddingSoft.testServices;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

@Component
public class LotService {

	/**
	 * @param startInclusive
	 * @param endExclusive
	 * @return a randomly generated instant in time between startInclusive and endExclusive
	 */
	public final static Instant between(Instant startInclusive, Instant endExclusive) {//https://www.baeldung.com/java-random-dates
		long startSeconds = startInclusive.getEpochSecond();
		long endSeconds = endExclusive.getEpochSecond();
		long random = ThreadLocalRandom.current().nextLong(startSeconds, endSeconds);
		return Instant.ofEpochSecond(random);
	}

}
