package org.gene.search.router.listener;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.gene.search.model.SeedResponse;

import java.io.IOException;

import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.gene.search.vertx.common.json.JsonCodec.encode;

@Slf4j
public class GetResponseListener extends SeedResponseListener {

    public GetResponseListener(RoutingContext routingContext) {
        super(routingContext);
    }

    @Override
    public void onSuccess(Response response) {
        try {
            String responseBody = EntityUtils.toString(response.getEntity());
            JsonObject json = new JsonObject(responseBody);
            SeedResponse seedResponse = json.getJsonObject("_source").mapTo(SeedResponse.class);
            routingContext.response()
                          .putHeader(CONTENT_TYPE, APPLICATION_JSON)
                          .setStatusCode(200)
                          .end(encode(seedResponse));
        } catch (IOException e) {
            log.error("error on elastic response success", e);
        }
    }

}
