package com.hazelcast.resources;

import com.hazelcast.HzAgentConfiguration;
import com.hazelcast.api.InstanceInfo;
import com.hazelcast.api.InstanceStartResponse;
import com.hazelcast.core.HazelcastStarterInfo;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Hazelcast instance administration
 */
@Path("/instance")
@Produces(MediaType.APPLICATION_JSON)
public class InstanceAdmin {

    private final HzAgentConfiguration configuration;
    private final AtomicInteger instanceId = new AtomicInteger();

    private Map<Integer, HazelcastStarterInfo> versionStarterInfo = new ConcurrentHashMap<>();

    public InstanceAdmin(HzAgentConfiguration configuration) {
        this.configuration = configuration;
    }

    @GET
    public List<InstanceInfo> getInstances() {
        List<InstanceInfo> instances = new ArrayList<>(versionStarterInfo.size());
        for (Map.Entry<Integer, HazelcastStarterInfo> entry : versionStarterInfo.entrySet()) {
            InstanceInfo info = new InstanceInfo(entry.getValue().getVersion(), entry.getKey(),
                    entry.getValue().getStatus().getId());
            instances.add(info);
        }
        return instances;
    }

    @Path("/start/{version}")
    @POST
    public Response startInstance(@PathParam("version") String version)
            throws IOException, InterruptedException {
        String workingDir = Files.createTempDirectory("hz-" + version, new FileAttribute[] {}).toString();
        String command = String.format("%s%shazelcastStarter.sh %s %s/configuration %s",
                configuration.getScriptPath(), File.separator, version, configuration.getManCenterBaseUrl(),
                workingDir);
        Process starter = Runtime.getRuntime().exec(command);
        ////
        // Process will get stuck if it fills its out/err stream buffers
        // for now there's no problem since everything is set to silent but just taking a note here
        ////
        HazelcastStarterInfo starterInfo = new HazelcastStarterInfo(version, workingDir, starter);
        int thisInstance = instanceId.incrementAndGet();
        versionStarterInfo.put(thisInstance, starterInfo);
        return Response.ok().entity(new InstanceStartResponse(thisInstance)).build();
    }

    @Path("/{id}/status")
    @GET
    public Response getStatus(@PathParam("id") int id) {
        HazelcastStarterInfo info = versionStarterInfo.get(id);
        if (info == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok().entity(info.getStatus().getId()).build();
    }
}