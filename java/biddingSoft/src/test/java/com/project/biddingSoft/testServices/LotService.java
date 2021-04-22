package com.project.biddingSoft.testServices;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

@Component
public class LotService {

	public static Instant between(Instant startInclusive, Instant endExclusive) {
		long startSeconds = startInclusive.getEpochSecond();
		long endSeconds = endExclusive.getEpochSecond();
		long random = ThreadLocalRandom.current().nextLong(startSeconds, endSeconds);
		return Instant.ofEpochSecond(random);
	}

}
