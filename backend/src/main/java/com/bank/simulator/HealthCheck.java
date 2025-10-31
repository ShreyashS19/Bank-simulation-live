package com.bank.simulator;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/healthz")
public class HealthCheck {

    @GET
    public Response health() {
        return Response.ok("✅ Server is healthy").build();
    }
}
