package com.hazelcast.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response from POST /instance/start/{version}
 */
public class InstanceStartResponse {

    private int id;

    public InstanceStartResponse() {
    }

    public InstanceStartResponse(int id) {
        this.id = id;
    }

    @JsonProperty
    public int getId() {
        return id;
    }
}
