package org.gene.search.router.listener;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.RequestLine;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.ResponseListener;

import java.io.IOException;

import static java.util.Arrays.asList;

@Slf4j
public abstract class SeedResponseListener implements ResponseListener {

    protected final RoutingContext routingContext;

    public SeedResponseListener(RoutingContext routingContext) {
        this.routingContext = routingContext;
    }

    @Override
    public void onFailure(Exception exception) {
        HttpServerResponse httpServerResponse = routingContext.response();
        Response response = ((ResponseException) exception).getResponse();
        RequestLine requestLine = response.getRequestLine();
        HttpHost host = response.getHost();
        int statusCode = response.getStatusLine().getStatusCode();
        Header[] headers = response.getHeaders();

        String responseBody;
        JsonObject responseMessage = new JsonObject().put("host", host.toURI())
                                                     .put("request_line", requestLine.toString());

        try {
            responseBody = EntityUtils.toString(response.getEntity());
            responseMessage.put("message", responseBody);
        } catch (IOException e) {
            log.error("Error getting response body from failure", e);
            responseMessage.put("message", "unable to parse failure cause");
        }

        asList(headers).stream().map(header -> httpServerResponse.putHeader(header.getName(), header.getValue()));

        httpServerResponse.setStatusCode(statusCode)
                          .end(responseMessage.encode());

    }
}
