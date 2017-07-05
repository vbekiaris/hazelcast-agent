package com.hazelcast;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class HzAgentConfiguration extends Configuration {

    @NotEmpty
    private String manCenterBaseUrl;

    @NotEmpty
    private String name;

    @Max(65535)
    @Min(1)
    private int port;

    public HzAgentConfiguration() {
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public String getManCenterBaseUrl() {
        return manCenterBaseUrl;
    }

    @JsonProperty
    public int getPort() {
        return port;
    }
}
