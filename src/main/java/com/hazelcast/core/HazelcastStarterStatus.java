package com.hazelcast.core;

/**
 *
 */
public enum HazelcastStarterStatus {
    STARTING(1),
    STARTED(2),
    STOPPING(3),
    STOPPED(4),
    FAILED_STARTUP(5),
    UNKNOWN(6);

    int id;

    HazelcastStarterStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
