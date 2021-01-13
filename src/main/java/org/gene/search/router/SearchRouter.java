package org.gene.search.router;

import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.elasticsearch.client.ResponseListener;
import org.gene.search.model.FuzzyQueryRequest;
import org.gene.search.model.SearchQueryRequest;
import org.gene.search.router.listener.GetResponseListener;
import org.gene.search.router.listener.SearchResponseListener;
import org.gene.search.service.SearchService;

import javax.inject.Inject;

import static io.vertx.core.http.HttpMethod.GET;
import static io.vertx.core.http.HttpMethod.POST;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.gene.search.vertx.common.json.JsonCodec.decode;

// TODO: add ETag support for Session Optimistic Locking

@Slf4j
@RouteBase(path = "api/seed")
public class SearchRouter {

    @Inject
    private SearchService searchService;

    @APIResponse(responseCode = "200",
                 description = "Get document.",
                 content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = String.class)))
    @APIResponse(responseCode = "400", description = "We were unable to find a document with the provided data. Some field constraint was violated.")
    @APIResponse(responseCode = "403", description = "You were not authorized to execute this operation.")
    @APIResponse(responseCode = "404", description = "The document does not exist.")
    @Route(path = "/:id", produces = APPLICATION_JSON, methods = GET)
    public void get(RoutingContext context) {
        HttpServerRequest httpRequest = context.request();
        final String id = httpRequest.getParam("id");
        ResponseListener responseListener = new GetResponseListener(context);
        searchService.get(responseListener, "seed", id)
                     .subscribe()
                     .with(item -> log.info("successful request"), failure -> log.error("failure", failure.getMessage()));
    }

    @APIResponse(responseCode = "200",
                 description = "Search content.",
                 content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = String.class)))
    @APIResponse(responseCode = "400", description = "We were unable to find any document with the provided data. Some field constraint was violated.")
    @APIResponse(responseCode = "403", description = "You were not authorized to execute this operation.")
    @APIResponse(responseCode = "404", description = "The document does not exist.")
    @Route(path = "/search", produces = APPLICATION_JSON, methods = POST)
    public void search(RoutingContext context) {
        SearchQueryRequest request = decode(context.getBodyAsString(), FuzzyQueryRequest.class);
        ResponseListener responseListener = new SearchResponseListener(context);
        searchService.search(responseListener, request)
                     .subscribe()
                     .with(item -> log.info("successful request"), failure -> log.error("failure", failure.getMessage()));
    }

}
