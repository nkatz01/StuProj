package com.project.biddingSoft.testServices;

import java.util.HashSet;
import java.util.Random;
import java.util.function.Predicate;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.project.biddingSoft.domain.Bid;
import com.project.biddingSoft.domain.Lot;
import com.project.biddingSoft.domain.User;

@Component
public class TestUserService {
	private static final HashSet<String> last200usernameCache = new HashSet<String>();
	private static final int CURRENT_USER_LIMIT = 200;
	@Bean
	public User getMeSimpleUser() {
		return new User.UserBuilder(genRandString(7, false), genRandString(8, true)).lotsCreated(new HashSet<Lot>())
				.bidsCreated(new HashSet<Bid>()).build();
	}

	public User getMeSimpleBidder() {
		return new User.UserBuilder(genRandString(7, false), genRandString(8, true)).lotsCreated(new HashSet<Lot>())
				.bidsCreated(new HashSet<Bid>()).build();
	}
	
	/**
	 * A utility function that produces a random string for the use in usernames or passwords. A cache is kept to assure
	 * that usernames remain unique.
	 * @param targetStrLength the desired length of the generated string
	 * @param alphanum indicates whether a username (only letters) or password (letters and digits) is desired
	 * @return the randomly generated string
	 */
	public static final synchronized String genRandString(int targetStrLength, boolean alphanum) {//https://www.baeldung.com/java-random-string
		Predicate<Integer> cond = (i) -> alphanum ? (i <= 57 || i >= 65) && (i <= 90 || i >= 97)
				: (i >= 65 && i <= 90) || (i >= 97 && i <= 122); // alphanumeric (=true) or alphabetic
		int leftLimit = 48; // numeral '0' 
		int rightLimit = 122; // letter 'z'
		int targetStringLength = targetStrLength;
		Random random = new Random();
		String genString = null;
		while (genString == null || last200usernameCache.contains(genString)) {
			genString = random.ints(leftLimit, rightLimit + 1).filter(i -> cond.test(i)).limit(targetStringLength)
					.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
		}
		if (last200usernameCache.size() + 1 == CURRENT_USER_LIMIT)
			last200usernameCache.clear();
		last200usernameCache.add(genString);
		return genString;

	}

}
