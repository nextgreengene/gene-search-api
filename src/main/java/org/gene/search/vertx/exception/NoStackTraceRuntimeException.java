package org.gene.search.vertx.exception;

public class NoStackTraceRuntimeException extends RuntimeException {

	public NoStackTraceRuntimeException(String message) {
		super(message, null, false, false);
	}

	public NoStackTraceRuntimeException(String message, Throwable cause) {
		super(message, cause, false, false);
	}

}
