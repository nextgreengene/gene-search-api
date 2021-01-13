package org.gene.search.vertx.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

public class TooManyRequestsException extends HttpFailure {

	public TooManyRequestsException() {
		super(HttpResponseStatus.TOO_MANY_REQUESTS);
	}

	public TooManyRequestsException(String message) {
		super(message, HttpResponseStatus.TOO_MANY_REQUESTS);
	}

	public TooManyRequestsException(String message, boolean silent) {
		super(message, HttpResponseStatus.TOO_MANY_REQUESTS, silent);
	}

}
