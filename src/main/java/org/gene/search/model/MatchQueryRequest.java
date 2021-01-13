package org.gene.search.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.annotations.ApiModelProperty;
import io.vertx.core.json.JsonObject;
import lombok.Builder;
import lombok.Getter;

import javax.validation.Valid;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Getter
@Builder
@JsonInclude(NON_EMPTY)
@JsonDeserialize(builder = MatchQueryRequest.MatchQueryRequestBuilder.class)
public class MatchQueryRequest implements SearchQueryRequest {

    @Valid
    @ApiModelProperty(value = "Terms to search", required = true)
    public final String match;

    @JsonPOJOBuilder(withPrefix = "")
    public static class MatchQueryRequestBuilder {
    }

    public JsonObject getQuery() {
        JsonObject termJson = new JsonObject().put("name", match);
        JsonObject matchJson = new JsonObject().put("match", termJson);
        return new JsonObject().put("query", matchJson);
    }

}
