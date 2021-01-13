package org.gene.search.util;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.CharMatcher.whitespace;

public class ObjectUtils {
    public static <T> Optional<T> firstNonEmpty(Optional<T> first, Optional<T> second) {
        return first.isPresent() ? first : second;
    }

    public static <T> List<T> firstNonEmpty(List<T> first, List<T> second) {
        return first == null || first.isEmpty() ? second : first;
    }

    public static <T> Set<T> firstNonEmpty(Set<T> first, Set<T> second) {
        return first == null || first.isEmpty() ? second : first;
    }

    public static <T> boolean isEqual(T arg1, T arg2) {
        String x = arg1 == null ? null : trim(arg1.toString()).toUpperCase();
        String y = arg2 == null ? null : trim(arg2.toString()).toUpperCase();

        return new EqualsBuilder().append(x, y).isEquals();
    }

    public static <T> T firstNonNull(final T... values) {
        if (values != null) {
            for (final T val : values) {
                if (val != null) {
                    return val;
                }
            }
        }
        return null;
    }

    private static String trim(CharSequence sequence) {
        return whitespace().trimFrom(sequence);
    }

}
