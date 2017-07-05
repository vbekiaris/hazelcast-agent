package com.hazelcast.health;

import com.codahale.metrics.health.HealthCheck;

/**
 *
 */
public class HazelcastAgentHealth extends HealthCheck {

    @Override
    protected Result check()
            throws Exception {
        return Result.healthy();
    }
}
