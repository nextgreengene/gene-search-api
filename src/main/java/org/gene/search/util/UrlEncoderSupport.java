package org.gene.search.util;


import org.apache.commons.codec.CharEncoding;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * UrlEncoder interface, assuming character coding problems are irrelevant.
 * Character set problems are assumed being a system setup problem, and as such they are only part of the problem
 * while creating a resource, not appearing after the resource has been created.
 * If this assumption is broken, an IllegalStateException will be thrown while encoding/decoding.
 * At initialisation time, the charset encoding name will be verified using {@link Charset#forName(String)}.
 */
public final class UrlEncoderSupport {

    private static class UrlEncoderImpl implements UrlEncoder {

        private final String encoding;

        public UrlEncoderImpl(String encoding) {
            this.encoding = encoding;
        }

        @Override
        public String urlEncode(String value) {
            try {
                return URLEncoder.encode(value, encoding);
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override
        public String urlDecode(String value) {
            try {
                return URLDecoder.decode(value, encoding);
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public static UrlEncoder forEncoding(String encoding) {
        Charset charset = Charset.forName(encoding); // Fail fast
        return new UrlEncoderImpl(charset.name());
    }

    public static final UrlEncoder URL_ENCODER_UTF_8 = forEncoding(CharEncoding.UTF_8);

    private UrlEncoderSupport() {
        // Hidden constructor for utility class
    }
}
