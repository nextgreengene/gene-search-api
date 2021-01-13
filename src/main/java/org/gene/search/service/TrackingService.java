package org.gene.search.service;

import io.vertx.mutiny.core.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import static org.gene.search.vertx.common.json.JsonCodec.encode;

@Slf4j
@Dependent
public class TrackingService {

    @Inject
    private EventBus bus;
    @ConfigProperty(name = "event-bus.session.create")
    private String eventAddress;

    public void track() {
        //bus.publish(eventAddress, encode(message));
        //return message;
    }
}
