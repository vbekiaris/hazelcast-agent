package com.hazelcast.core;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class HazelcastStarterInfo {

    private static final String PID_FILE = "hazelcast_instance.pid";

    private static final String HAZELCAST_DOWNLOAD_PATTERN =
            "http://download.hazelcast.com/download.jsp?version=hazelcast-%s&type=tar&p=";

    private final String version;
    private final String distributionFileName;
    private final String distributionUrl;
    private final String hazelcastHome;
    private final Process starterProcess;
    private Process stopperProcess;

    public HazelcastStarterInfo(String version, String workingDir, Process starterProcess)
            throws IOException {
        this.version = version;
        this.distributionUrl = String.format(HAZELCAST_DOWNLOAD_PATTERN, version);
        this.distributionFileName = String.format("hazelcast-%s.tar.gz", version);
        this.hazelcastHome = String.format("%s%shazelcast-%s",
                workingDir.toString(), File.separator, version);
        this.starterProcess = starterProcess;
    }

    public String getVersion() {
        return version;
    }

    public String getDistributionFileName() {
        return distributionFileName;
    }

    public String getDistributionUrl() {
        return distributionUrl;
    }

    public String getHazelcastHome() {
        return hazelcastHome;
    }

    public HazelcastStarterStatus getStatus() {
        try {
            int starterExitValue = starterProcess.exitValue();

            if (starterExitValue == 0) {
                // check if we are currently running the stopper
                if (stopperProcess != null) {
                    try {
                        if (stopperProcess.exitValue() == 0) {
                            return HazelcastStarterStatus.STOPPED;
                        } else {
                            return HazelcastStarterStatus.UNKNOWN;
                        }
                    } catch (IllegalThreadStateException e) {
                        return HazelcastStarterStatus.STOPPING;
                    }
                } else {
                    // if not, check PID file existence
                    File pidFile = new File(hazelcastHome + File.separator + "bin", PID_FILE);
                    if (pidFile.exists()) {
                        return HazelcastStarterStatus.STARTED;
                    } else {
                        return HazelcastStarterStatus.UNKNOWN;
                    }
                }
            } else {
                return HazelcastStarterStatus.FAILED_STARTUP;
            }
        } catch (IllegalThreadStateException e) {
            // not yet starter
            return HazelcastStarterStatus.STARTING;
        }
    }

    public boolean isStarted() {
        try {
            if (starterProcess.exitValue() != 0) {
                return false;
            }

            // also check for PID file
            File pidFile = new File(hazelcastHome + File.separator + "bin", PID_FILE);
            if (pidFile.exists()) {
                return true;
            }

            return false;
        } catch (IllegalThreadStateException e) {
            // not yet terminated
            return false;
        }
    }

    public void stop()
            throws IOException, InterruptedException {
        if (!isStarted()) {
            throw new IllegalStateException("Instance is not started");
        }
        String stopCommand = String.format("%s%sbin/stop.sh", hazelcastHome, File.separator);
        stopperProcess = Runtime.getRuntime().exec(stopCommand);
        stopperProcess.waitFor(30, TimeUnit.SECONDS);
    }
}
