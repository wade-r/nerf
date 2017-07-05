package com.ireul.nerf.web.server;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import java.util.concurrent.TimeUnit;

/**
 * This class wraps a {@link Server}
 *
 * @author Ryan Wade
 */
public class JettyServer {

    private String bindHost;

    private int bindPort;

    private Handler handler;

    private Server server;

    /**
     * Initialize a server
     *
     * @param bindAddress address to bind
     * @param handler     handler for request handling
     */
    public JettyServer(String bindAddress, Handler handler) {
        if (bindAddress == null || bindAddress.length() == 0) {
            this.bindHost = "0.0.0.0";
            this.bindPort = 8080;
        } else {
            String[] split = bindAddress.split(":");
            if (split.length == 2) {
                this.bindHost = split[0];
                this.bindPort = Integer.valueOf(split[1]);
            } else {
                this.bindHost = bindAddress;
                this.bindPort = 8080;
            }
        }

        this.handler = handler;
    }

    /**
     * Starts the server
     *
     * @throws Exception any exception
     */
    public void start() throws Exception {
        // Create server
        this.server = new Server(new QueuedThreadPool(200, 8, 60000));
        // Create connection factory
        HttpConfiguration configuration = new HttpConfiguration();
        configuration.setSecureScheme("https");
        configuration.addCustomizer(new ForwardedRequestCustomizer());
        HttpConnectionFactory factory = new HttpConnectionFactory(configuration);
        // Create connector
        ServerConnector connector = new ServerConnector(this.server, factory);
        connector.setIdleTimeout(TimeUnit.HOURS.toMillis(1));
        connector.setSoLingerTime(-1);
        connector.setHost(this.bindHost);
        connector.setPort(this.bindPort);
        // Start server
        this.server = connector.getServer();
        this.server.setConnectors(new Connector[]{connector});
        this.server.setHandler(this.handler);
        this.server.start();
    }

    /**
     * Stop the server
     *
     * @throws Exception any exception
     */
    public void stop() throws Exception {
        if (this.server != null) {
            this.server.stop();
        }
    }

}
