package org.gene.search.vertx.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

public class UnauthorizedException extends HttpFailure {

	public UnauthorizedException() {
		super(HttpResponseStatus.UNAUTHORIZED);
	}

	public UnauthorizedException(String message) {
		super(message, HttpResponseStatus.UNAUTHORIZED);
	}

	public UnauthorizedException(String message, boolean silent) {
		super(message, HttpResponseStatus.UNAUTHORIZED, silent);
	}

}
