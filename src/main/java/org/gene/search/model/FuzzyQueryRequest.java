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
@JsonDeserialize(builder = FuzzyQueryRequest.FuzzyQueryRequestBuilder.class)
public class FuzzyQueryRequest implements SearchQueryRequest {

    @Valid
    @ApiModelProperty(value = "Field to search", required = true)
    public final String field;

    @Valid
    @ApiModelProperty(value = "Value to search", required = true)
    public final String value;

    @JsonPOJOBuilder(withPrefix = "")
    public static class FuzzyQueryRequestBuilder {
    }

    public JsonObject getQuery() {
        JsonObject valueJson = new JsonObject().put("value", value);
        JsonObject fieldJson = new JsonObject().put(field, valueJson);
        JsonObject fuzzyJson = new JsonObject().put("fuzzy", fieldJson);
        return new JsonObject().put("query", fuzzyJson);
    }

}
