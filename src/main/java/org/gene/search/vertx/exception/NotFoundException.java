package org.gene.search.vertx.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 */
public class NotFoundException extends HttpFailure {

	public NotFoundException() {
		super(HttpResponseStatus.NOT_FOUND);
	}

	public NotFoundException(String message) {
		super(message, HttpResponseStatus.NOT_FOUND);
	}

}
