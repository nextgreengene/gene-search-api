package org.gene.search.vertx.common.lang;

import java.util.Optional;

/**
 * Utility methods than enhance using Optional instances
 */
public class Optionals {

	/**
	 * Converts an object to a given class if possible.
	 *
	 * @param o     Object to attempt to cast
	 * @param clazz Target class of the cast
	 * @param <T>   Type of the casted object
	 * @return An optional contining the casted object or empty if the object doesnt implement the target type
	 */
	public static <T> Optional<T> safeCast(Object o, Class<T> clazz) {
		if (clazz.isInstance(o)) {
			return Optional.of(clazz.cast(o));
		}
		return Optional.empty();
	}

}
