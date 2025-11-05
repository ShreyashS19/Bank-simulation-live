package com.bank.simulator;

import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.servlet.ServletContainer;
import com.bank.simulator.config.DatabaseInitializerListener;

public class StartServer {
    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        Server server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");
        
        // Add database listener - but don't let it fail startup
        try {
            context.addEventListener(new DatabaseInitializerListener());
        } catch (Exception e) {
            System.out.println("⚠️  Database listener error (non-blocking): " + e.getMessage());
        }
        
        ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/api/*");
        jerseyServlet.setInitOrder(1);
        jerseyServlet.setInitParameter(
            "jersey.config.server.provider.packages",
            "com.bank.simulator.controller,com.bank.simulator.config,com.bank.simulator"
        );
        
        server.setHandler(context);

        System.out.println("🚀 Jetty 12 EE10 server running on port " + port);
        System.out.println("📍 Health check: http://localhost:" + port + "/api/healthz");
        System.out.println("📍 API base URL: http://localhost:" + port + "/api/");
        
        server.start();
        server.join();
    }
}
