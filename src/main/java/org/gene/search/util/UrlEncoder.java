package org.gene.search.util;

/**
 * UrlEncoder interface, assuming character coding problems are irrelevant.
 * Character set problems are assumed being a system setup problem, and as such they are only part of the problem
 * while creating a resource, not appearing after the resource has been created.
 * If this assumption is broken, an IllegalStateException will be thrown.
 */
public interface UrlEncoder {

    /**
     * Encodes a string value.
     * If unsuccessful, an {@see IllegalStateException} is thrown.
     *
     * @param value value to be encoded
     * @return the encoded value
     */
    String urlEncode(String value);

    /**
     * Decodes a string value.
     * If unsuccessful, an {@see IllegalStateException} is thrown.
     *
     * @param value value to be decoded
     * @return the decoded value
     */
    String urlDecode(String value);
}
