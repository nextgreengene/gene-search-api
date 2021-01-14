package org.gene.search.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.annotations.ApiModelProperty;
import io.vertx.core.json.JsonArray;
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
    @ApiModelProperty(value = "Text to search", required = true)
    public final String text;

    @JsonPOJOBuilder(withPrefix = "")
    public static class FuzzyQueryRequestBuilder {
    }

    public JsonObject getQuery() {
        return new JsonObject().put("query", getFunctionScoreQuery());
    }

    private JsonObject getFunctionScoreQuery() {
        JsonObject functionScoreBoolQuery = new JsonObject().put("query", getBoolQuery())
                                                            .put("field_value_factor", getFieldValueFactorQuery());
        return new JsonObject().put("function_score", functionScoreBoolQuery);
    }

    private JsonObject getFieldValueFactorQuery() {
        return new JsonObject().put("field", "score")
                               .put("factor", 1.2)
                               .put("modifier", "sqrt")
                               .put("missing", 1);
    }

    private JsonObject getBoolQuery() {
        JsonArray shouldArray = new JsonArray().add(getFuzzyNameQuery())
                                               .add(getFuzzyDescriptionQuery());
        JsonObject shouldJson = new JsonObject().put("should", shouldArray);
        return new JsonObject().put("bool", shouldJson);
    }

    private JsonObject getFuzzyNameQuery() {
        JsonObject valueNameJson = new JsonObject().put("value", text);
        JsonObject fieldNameJson = new JsonObject().put("name", valueNameJson);
        return new JsonObject().put("fuzzy", fieldNameJson);
    }

    private JsonObject getFuzzyDescriptionQuery() {
        JsonObject valueDescriptionJson = new JsonObject().put("value", text);
        JsonObject fieldDescriptionJson = new JsonObject().put("description", valueDescriptionJson);
        return new JsonObject().put("fuzzy", fieldDescriptionJson);
    }


}
