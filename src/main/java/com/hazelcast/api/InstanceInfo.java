package com.hazelcast.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class InstanceInfo {
    private String version;
    private int id;
    private int status;

    public InstanceInfo() {
    }

    public InstanceInfo(String version, int id, int status) {
        this.version = version;
        this.id = id;
        this.status = status;
    }

    @JsonProperty
    public String getVersion() {
        return version;
    }

    @JsonProperty
    public int getId() {
        return id;
    }

    @JsonProperty
    public int getStatus() {
        return status;
    }
}
