package org.gene.search.vertx.exception;

import org.gene.search.vertx.common.env.RuntimeEnv;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Failure handler that provides a neat json message mapping the response
 */
public class HttpFailureHandler implements Handler<RoutingContext> {

    private static final Logger log = LoggerFactory.getLogger("HttpFailure");

    private final boolean displayExceptionDetails;

    public static HttpFailureHandler create() {
        boolean displayExceptionDetails = RuntimeEnv.get().dev();
        return new HttpFailureHandler(displayExceptionDetails);
    }

    public static HttpFailureHandler create(boolean displayExceptionDetails) {
        return new HttpFailureHandler(displayExceptionDetails);
    }

    private HttpFailureHandler(boolean displayExceptionDetails) {
        this.displayExceptionDetails = displayExceptionDetails;
    }

    @Override
    public void handle(RoutingContext context) {
        HttpServerResponse response = context.response();

        Throwable failure = context.failure();

        String msg;
        boolean silent = false;
        if (context.statusCode() != -1) {
            response.setStatusCode(context.statusCode());
            msg = response.getStatusMessage();
        } else if (failure instanceof HttpFailure) {
            response.setStatusCode(((HttpFailure) failure).status().code());
            msg = getMessage(failure);
            silent = ((HttpFailure) failure).silent();
        } else {
            response.setStatusCode(500);
            msg = getMessage(failure);
            log.error("Unexpected error ", failure);
        }

        JsonObject jsonError = new JsonObject().put("error", new JsonObject()
                .put("code", response.getStatusCode())
                .put("message", msg));

        if (context.failure() != null && displayExceptionDetails) {
            JsonArray stack = new JsonArray();
            for (StackTraceElement elem : context.failure().getStackTrace()) {
                stack.add(elem.toString());
            }
            jsonError.put("stack", stack);
        }

        if (!ignoredError(response.getStatusCode()) && !silent) {
            noticeToNewRelic(failure, msg);
        }

        response.putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        response.end(jsonError.encode());
    }

    private void noticeToNewRelic(Throwable failure, String message) {
        log.error(message, failure);
//		NewRelic.noticeError(failure);
    }

    private boolean ignoredError(int statusCode) {
        return statusCode == HttpResponseStatus.NOT_FOUND.code();
    }

    private String getMessage(Throwable throwable) {
        return throwable.toString().replaceAll("\\r|\\n", " ");
    }
}
