package org.gene.search.vertx.exception;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.impl.NoStackTraceThrowable;

import java.util.Optional;

/**
 * Special exception type used for failures that may be propagated over HTTP.
 * <p>
 * Exceptions extending this class are not meant to be used as standard exception that are thrown and catched,
 * but as types for specific error conditions that will be passed as values
 * (probably in {@link io.vertx.core.Future}s).
 * <p>
 * This exception type lacks a stack trace and suppressed exception list, so if you don't throw and catch it, it is
 * as lightweight as any other object, and allows consistent processing with standard exception.
 */
public class HttpFailure extends NoStackTraceThrowable {

	private final HttpResponseStatus status;

	/**
	 * Indicates whether the exception should be reported to the monitoring tools
	 */
	private final boolean silent;

	public HttpFailure(HttpResponseStatus status) {
		this(status.reasonPhrase(), status);
	}

	public HttpFailure(String message, HttpResponseStatus status) {
		this(message, status, false);
	}

	public HttpFailure(String message, HttpResponseStatus status, boolean silent) {
		super(Optional.ofNullable(message)
				.filter(value -> !value.trim().isEmpty())
				.orElseGet(status::reasonPhrase));
		this.status = status;
		this.silent = silent;
	}

	public HttpResponseStatus status() {
		return status;
	}

	public boolean silent() {
		return silent;
	}

}
