package org.gene.search.router.listener;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.gene.search.model.SeedResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.gene.search.vertx.common.json.JsonCodec.encode;

@Slf4j
public class SearchResponseListener extends SeedResponseListener {

    public SearchResponseListener(RoutingContext routingContext) {
        super(routingContext);
    }

    @Override
    public void onSuccess(Response response) {
        try {
            String responseBody = EntityUtils.toString(response.getEntity());
            JsonObject json = new JsonObject(responseBody);
            JsonArray hits = json.getJsonObject("hits").getJsonArray("hits");
            List<SeedResponse> results = new ArrayList<>(hits.size());
            for (int i = 0; i < hits.size(); i++) {
                JsonObject hit = hits.getJsonObject(i);
                SeedResponse seedResponse = hit.getJsonObject("_source").mapTo(SeedResponse.class);
                results.add(seedResponse);
            }
            routingContext.response()
                          .putHeader(CONTENT_TYPE, APPLICATION_JSON)
                          .setStatusCode(200)
                          .end(encode(results));
        } catch (IOException e) {
            log.error("error on elastic response success", e);
        }
    }

}
