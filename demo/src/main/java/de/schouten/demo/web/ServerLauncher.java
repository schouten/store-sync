package de.schouten.demo.web;

import java.io.File;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import de.schouten.store.Store;
import de.schouten.store.web.StoreSyncContext;
import de.schouten.store.web.StoreSyncSocketServlet;

public class ServerLauncher {

    private Server server;

    public void start(File appHome, Store store) throws Exception {
        StoreSyncContext<DemoSession> demoContext = new DemoContext(store);
        StoreSyncSocketServlet<DemoSession> syncServlet = new StoreSyncSocketServlet<DemoSession>();
        syncServlet.setContext(demoContext);

        server = new Server();
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(8080);
        server.addConnector(connector);

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setWelcomeFiles(new String[] {"index.html"});
        resource_handler.setResourceBase(new File(appHome, "static").getAbsolutePath());

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/socket");

        context.addServlet(new ServletHolder(syncServlet), "/storesync");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] {context, resource_handler, new DefaultHandler()});
        server.setHandler(handlers);

        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }

}
