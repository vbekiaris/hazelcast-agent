package com.hazelcast.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;

/**
 * Agent registration message sent to ManCenter
 */
public class AgentRegistration {

    private String name;
    @Max(65535)
    private int port;

    public AgentRegistration() {
    }

    public AgentRegistration(String name, int port) {
        this.name = name;
        this.port = port;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public int getPort() {
        return port;
    }
}
