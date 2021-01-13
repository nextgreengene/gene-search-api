package org.gene.search.service;

import javax.enterprise.context.Dependent;
import java.util.UUID;

@Dependent
public class IdGenerator {

    public String createId() {
        return UUID.randomUUID().toString();
    }

}