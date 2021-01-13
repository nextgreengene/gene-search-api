package org.gene.search.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.gene.search.qualifiers.ElasticSearchRestClient;
import org.gene.search.qualifiers.Environment;
import org.gene.search.vertx.common.json.mapping.VertxJsonModule;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

@ApplicationScoped
public class ApplicationConfiguration {

    @Produces
    @Singleton
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .setSerializationInclusion(JsonInclude.Include.NON_DEFAULT)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false)
                .configure(WRITE_DATES_AS_TIMESTAMPS, false)
                .registerModule(new Jdk8Module())
                .registerModule(new VertxJsonModule())
                .registerModule(new JavaTimeModule());
    }

    @Produces
    @Singleton
    @Environment
    public String environment() {
        return "local";
    }

    @Produces
    @Singleton
    @ElasticSearchRestClient
    public RestClient elasticsearchClient() {
        HttpHost httpHost = new HttpHost("localhost", 9200);
        RestClientBuilder.RequestConfigCallback requestConfigCallback = getRequestConfigCallback(5000, 60000);
        return RestClient.builder(httpHost)
                         .setRequestConfigCallback(requestConfigCallback)
                         .build();
    }

    private RestClientBuilder.RequestConfigCallback getRequestConfigCallback(int connectTimeout, int socketTimeout) {
        return requestConfigBuilder -> requestConfigBuilder
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout);
    }
}
