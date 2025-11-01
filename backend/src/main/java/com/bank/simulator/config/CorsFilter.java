// package com.bank.simulator.config;

// import jakarta.ws.rs.container.ContainerRequestContext;
// import jakarta.ws.rs.container.ContainerResponseContext;
// import jakarta.ws.rs.container.ContainerResponseFilter;
// import jakarta.ws.rs.ext.Provider;
// import java.io.IOException;

// @Provider
// public class CorsFilter implements ContainerResponseFilter {

//     private static final String FRONTEND_URL = "https://bank-simulation-live-1.onrender.com";

//     @Override
//     public void filter(ContainerRequestContext requestContext, 
//                       ContainerResponseContext responseContext) throws IOException {
        
//         responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", FRONTEND_URL);
//         responseContext.getHeaders().putSingle("Access-Control-Allow-Credentials", "true");
//         responseContext.getHeaders().putSingle("Access-Control-Allow-Headers", 
//             "origin, content-type, accept, authorization, x-requested-with");
//         responseContext.getHeaders().putSingle("Access-Control-Allow-Methods", 
//             "GET, POST, PUT, DELETE, OPTIONS, HEAD");
//         responseContext.getHeaders().putSingle("Access-Control-Max-Age", "3600");
//     }
// }
package com.bank.simulator.config;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext,
                      ContainerResponseContext responseContext) throws IOException {

        // ✅ Allow only your frontend domain (Render)
        responseContext.getHeaders().add("Access-Control-Allow-Origin", "https://bank-simulation-live-1.onrender.com");

        responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
        responseContext.getHeaders().add("Access-Control-Allow-Headers",
                "origin, content-type, accept, authorization, x-requested-with");
        responseContext.getHeaders().add("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        responseContext.getHeaders().add("Access-Control-Max-Age", "3600");
    }
}
