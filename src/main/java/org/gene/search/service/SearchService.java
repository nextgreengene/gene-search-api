package org.gene.search.service;

import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.Cancellable;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;
import org.gene.search.model.SearchQueryRequest;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import static java.text.MessageFormat.format;

@Slf4j
@Dependent
@AllArgsConstructor
public class SearchService {

    @Inject
    RestClient restClient;

    public Uni<Cancellable> get(ResponseListener responseListener, String index, String id) {
        Request request = new Request("GET", format("{0}/_doc/{1}", index, id));
        Cancellable cancellable = restClient.performRequestAsync(request, responseListener);
        return Uni.createFrom().item(cancellable);
    }

    public Uni<Cancellable> search(ResponseListener responseListener, SearchQueryRequest searchQueryRequest) {
        Request request = new Request(
                "GET",
                "/seed/_search");
        JsonObject queryJson = searchQueryRequest.getQuery();
        request.setJsonEntity(queryJson.encode());
        Cancellable cancellable = restClient.performRequestAsync(request, responseListener);
        return Uni.createFrom().item(cancellable);
    }

}
