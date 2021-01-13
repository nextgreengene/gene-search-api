package org.gene.search.vertx.common.lang;

import java.security.SecureRandom;
import java.util.Random;

public class RandomStrings {

	private final static Random random = new SecureRandom();

	private RandomStrings() {
	}

	public static String alphanumeric(int length) {
		return random.ints(48, 123)
				.filter(i -> (i < 58) || (i > 64 && i < 91) || (i > 96))
				.mapToObj(i -> (char) i)
				.limit(length)
				.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
				.toString();
	}

}
