package com.ireul.nerf.web.server;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by ryan on 5/31/17.
 */
public class JettyServer {

    private String bindHost;

    private int bindPort;

    private JettyHandler handler;

    private Server server;

    private final Logger logger = LoggerFactory.getLogger(JettyServer.class);

    public JettyServer(String bindAddress, JettyHandler handler) {
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

    public void start() throws Exception {
        this.server = new Server(new QueuedThreadPool(200, 8, 60000));
        HttpConfiguration configuration = new HttpConfiguration();
        configuration.setSecureScheme("https");
        configuration.addCustomizer(new ForwardedRequestCustomizer());
        HttpConnectionFactory factory = new HttpConnectionFactory(configuration);
        ServerConnector connector = new ServerConnector(this.server, factory);
        connector.setIdleTimeout(TimeUnit.HOURS.toMillis(1));
        connector.setSoLingerTime(-1);
        connector.setHost(this.bindHost);
        connector.setPort(this.bindPort);
        this.server = connector.getServer();
        this.server.setConnectors(new Connector[]{connector});
        this.server.setHandler(this.handler);
        this.server.start();
    }

    public void stop() throws Exception {
        if (this.server != null) {
            this.server.stop();
        }
    }

}
