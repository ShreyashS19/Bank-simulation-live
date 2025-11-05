package com.bank.simulator;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import com.bank.simulator.config.DatabaseInitializerListener;

public class StartServer {
    public static void main(String[] args) throws Exception {
        // Use Render's provided PORT or default to 10000 for production, 8080 for local
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        Server server = new Server(port);

        // Create servlet context (no web.xml needed)
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");
        
        // Register Jersey servlet container programmatically
        ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/api/*");
        jerseyServlet.setInitOrder(1);
        
        // Tell Jersey where to find REST resources and providers
        jerseyServlet.setInitParameter(
            "jersey.config.server.provider.packages",
            "com.bank.simulator.controller," +
            "com.bank.simulator.config," +
            "com.bank.simulator"
        );
        
        // Enable JSON support
        jerseyServlet.setInitParameter(
            "jersey.config.server.provider.classnames",
            "org.glassfish.jersey.jackson.JacksonFeature"
        );
        
        // Add database initializer listener
        context.addEventListener(new DatabaseInitializerListener());
        
        server.setHandler(context);

        System.out.println("🚀 Jetty 12 EE10 server running on port " + port);
        System.out.println("📍 Health check: http://localhost:" + port + "/api/healthz");
        System.out.println("📍 API base URL: http://localhost:" + port + "/api/");
        
        server.start();
        server.join();
    }
}
