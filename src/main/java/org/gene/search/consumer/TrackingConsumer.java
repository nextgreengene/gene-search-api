package org.gene.search.consumer;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.lang.String.format;

/**
 * Consume events through Vertx Event Bus asynchronously.
 * Send event to different stakeholder (systems): Kafka, Datadog, internal logging, AWS, etc.
 */
@Slf4j
@ApplicationScoped
public class TrackingConsumer {

    @Inject
    @Channel("new_seeds_kafka_out")
    Emitter<String> sessionsKafkaEmitter;

    @ConsumeEvent("new_seeds")
    public void consume(String message) {

        // Logging
        log.info(format("New Seed created %s", message));
        // To Kafka
        sessionsKafkaEmitter.send(message);
        // To Datadog
        // To S3
        // To any other external system
    }


    /**
     * Consuming Kafka event to simulate the service interested in a new seed created
     *
     * @param msg
     * @return
     */
    @Incoming("new_seeds_kafka_in")
    public CompletionStage<Void> consume(KafkaRecord<String, String> msg) {
        log.info("Received message (topic: {}, partition: {}) with key {}: {}", msg.getTopic(), msg.getPartition(), msg.getKey(), msg.getPayload());
        return msg.ack();
    }
}
