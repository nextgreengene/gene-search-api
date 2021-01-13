package org.gene.search.vertx.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

public class BadRequestException extends HttpFailure {

	public BadRequestException() {
		super(HttpResponseStatus.BAD_REQUEST);
	}

	public BadRequestException(String message) {
		super(message, HttpResponseStatus.BAD_REQUEST);
	}

	public BadRequestException(String message, boolean silent) {
		super(message, HttpResponseStatus.BAD_REQUEST, silent);
	}

}
