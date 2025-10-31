package com.bank.simulator;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.webapp.Configuration;
import org.eclipse.jetty.ee10.webapp.WebInfConfiguration;
import org.eclipse.jetty.ee10.webapp.WebXmlConfiguration;
import org.eclipse.jetty.ee10.webapp.MetaInfConfiguration;
import org.eclipse.jetty.ee10.webapp.FragmentConfiguration;

import java.nio.file.Path;

public class StartServer {
    public static void main(String[] args) throws Exception {
        // ✅ Start Jetty server on port 8080
        Server server = new Server(8080);

        // ✅ Create EE10 WebAppContext
        WebAppContext webApp = new WebAppContext();
        webApp.setContextPath("/");

        // ✅ Use base directory for your web resources
        Path webappDir = Path.of("src/main/webapp").toAbsolutePath();
        webApp.setBaseResourceAsPath(webappDir);   // 👈 new Jetty 12 method

        // ✅ Specify your web.xml
        webApp.setDescriptor(webappDir.resolve("WEB-INF/web.xml").toString());

        // ✅ Add default EE10 configurations
        // ✅ Add default EE10 configurations
        // ✅ Add default EE10 configurations
        // ✅ Add default EE10 configurations
        webApp.setConfigurations(new Configuration[] {
                new WebInfConfiguration(),
                new WebXmlConfiguration(),
                new MetaInfConfiguration(),
                new FragmentConfiguration()
        });

        System.out.println("🚀 Jetty 12 EE10 server starting at http://localhost:8080 ...");
        server.start();
        server.join();
    }
}
