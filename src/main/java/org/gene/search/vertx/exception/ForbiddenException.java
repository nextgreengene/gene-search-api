package org.gene.search.vertx.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

public class ForbiddenException extends HttpFailure {

	public ForbiddenException() {
		super(HttpResponseStatus.FORBIDDEN);
	}

	public ForbiddenException(String message) {
		super(message, HttpResponseStatus.FORBIDDEN);
	}

	public ForbiddenException(String message, boolean silent) {
		super(message, HttpResponseStatus.FORBIDDEN, silent);
	}
}
