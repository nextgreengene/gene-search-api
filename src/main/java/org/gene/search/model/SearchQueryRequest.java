package org.gene.search.model;

import io.vertx.core.json.JsonObject;

public interface SearchQueryRequest {

    JsonObject getQuery();

}
