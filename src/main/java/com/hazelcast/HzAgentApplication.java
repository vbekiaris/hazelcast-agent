package com.hazelcast;

import com.hazelcast.api.AgentRegistration;
import com.hazelcast.resources.InstanceAdmin;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class HzAgentApplication extends Application<HzAgentConfiguration> {

    public static void main(final String[] args) throws Exception {
        new HzAgentApplication().run(args);
    }

    @Override
    public String getName() {
        return "hazelcast-agent";
    }

    @Override
    public void initialize(final Bootstrap<HzAgentConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final HzAgentConfiguration configuration,
                    final Environment environment) {

        // register resources
        InstanceAdmin instanceAdmin = new InstanceAdmin(configuration);
        environment.jersey().register(instanceAdmin);

        // register the agent to ManCenter
        Client c = ClientBuilder.newClient();
        WebTarget target = c.target(configuration.getManCenterBaseUrl());
        target.path("register-agent").request().post(Entity.entity(new AgentRegistration(
                configuration.getName(), configuration.getPort()), MediaType.APPLICATION_JSON_TYPE));
    }

}
