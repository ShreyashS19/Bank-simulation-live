package com.bank.simulator;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.ee10.webapp.Configuration;
import org.eclipse.jetty.ee10.webapp.WebInfConfiguration;
import org.eclipse.jetty.ee10.webapp.WebXmlConfiguration;
import org.eclipse.jetty.ee10.webapp.MetaInfConfiguration;
import org.eclipse.jetty.ee10.webapp.FragmentConfiguration;
import java.nio.file.Path;

public class StartServer {
    public static void main(String[] args) throws Exception {
        // ✅ Use Render's provided PORT environment variable or default to 8080 for local
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        Server server = new Server(port);

        WebAppContext webApp = new WebAppContext();
        webApp.setContextPath("/");

        Path webappDir = Path.of("src/main/webapp").toAbsolutePath();
        webApp.setBaseResourceAsPath(webappDir);
        webApp.setDescriptor(webappDir.resolve("WEB-INF/web.xml").toString());

        webApp.setConfigurations(new Configuration[]{
            new WebInfConfiguration(),
            new WebXmlConfiguration(),
            new MetaInfConfiguration(),
            new FragmentConfiguration()
        });

        server.setHandler(webApp);
        System.out.println("🚀 Jetty 12 EE10 server running on port " + port);
        server.start();
        server.join();
    }
}
