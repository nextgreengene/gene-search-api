package org.gene.search.vertx.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

public class RemoteServiceException extends RuntimeException {

	public RemoteServiceException(String message) {
		super(message, null, false, false);
	}

	public RemoteServiceException(String message, int status) {
		this(message, HttpResponseStatus.valueOf(status));
	}

	public RemoteServiceException(String message, HttpResponseStatus status) {
		super(message + " - " + status.toString(), null, false, false);
	}

	public RemoteServiceException(String message, Throwable cause) {
		super(message, cause, false, false);
	}

}
