package com.project.biddingSoft.service;

import java.util.HashSet;
import java.util.Random;
import java.util.function.Predicate;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.project.biddingSoft.domain.User;

@Component
public class UserService {
	private static final HashSet<String> last200usernameCache = new HashSet<String>();

	@Bean
	public User getMeSimpleUser() {
		return new User.UserBuilder(genRandString(7, true), genRandString(8, false)).build();
	}

	public static synchronized String genRandString(int targetStrLength, boolean alphanum) {
		Predicate<Integer> cond = (i) -> alphanum ? (i <= 57 || i >= 65) && (i <= 90 || i >= 97)
				: (i >= 65 && i <= 90) || (i >= 97 && i <= 122); // alphanumeric (=true) or alphabetic
		int leftLimit = 48; // numeral '0' //https://www.baeldung.com/java-random-string
		int rightLimit = 122; // letter 'z'
		int targetStringLength = targetStrLength;
		Random random = new Random();
		String genString = null;
		while (genString == null || last200usernameCache.contains(genString)) {
			genString = random.ints(leftLimit, rightLimit + 1).filter(i -> cond.test(i)).limit(targetStringLength)
					.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
		}
		if (last200usernameCache.size() + 1 == 200)
			last200usernameCache.clear();
		last200usernameCache.add(genString);
		return genString;

	}

}
