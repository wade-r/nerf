package com.ireul.nerf.web;

import com.ireul.nerf.application.Application;
import com.ireul.nerf.web.controller.Controller;
import com.ireul.nerf.web.render.FreeMarkerRenderer;
import com.ireul.nerf.web.render.Renderer;
import com.ireul.nerf.web.route.*;
import com.ireul.nerf.web.server.JettyServer;
import com.ireul.nerf.web.server.Request;
import com.ireul.nerf.web.server.Response;
import org.eclipse.jetty.server.session.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * This class provides a bridge from Nerf application to Jetty framework
 *
 * @author Ryan Wade
 */
public class WebContext {

    private final Logger logger = LoggerFactory.getLogger(WebContext.class);

    private final Application application;

    private JettyServer server;

    private Router router;

    private Renderer renderer;

    /**
     * Initialize from {@link Application}
     *
     * @param application application
     */
    public WebContext(Application application) {
        this.application = application;
    }

    /**
     * Setup with options from command-line
     *
     * @param options options from command-line
     */
    public void setup(HashMap<String, String> options) {
        String bind = options.get("bind");
        if (bind == null) {
            bind = "127.0.0.1:7788";
        }
        List<Route> routes = RouteUtils.scanClass(applicationClass());
        this.router = new SimpleRouter(routes);
        this.server = new JettyServer(bind, new JettyHandler());
        this.renderer = new FreeMarkerRenderer(applicationClass());
    }

    /**
     * Start the internal Jetty server
     */
    public void start() {
        try {
            this.server.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(100);
        }
    }

    /**
     * Stop the internal Jetty server
     */
    public void stop() {
        if (this.server == null) return;
        try {
            this.server.stop();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to stop Jetty gracefully");
            System.exit(100);
        }
    }

    /**
     * Get the {@link Application}
     *
     * @return application instance
     */
    public Application application() {
        return this.application;
    }

    /**
     * Get the application class
     *
     * @return application class
     */
    public Class<? extends Application> applicationClass() {
        return application().getClass();
    }

    /**
     * Get the server
     *
     * @return the server
     */
    public JettyServer server() {
        return this.server;
    }

    /**
     * Render the template
     *
     * @param template template id
     * @param model    model
     * @param output   output
     */
    public void render(String template, HashMap<String, Object> model, PrintWriter output) {
        this.renderer.render(template, model, output);
    }

    /**
     * JettyHandler inherit Jetty {@link SessionHandler}, provides a bridge from Nerf to Jetty
     *
     * @author Ryan Wade
     */
    public class JettyHandler extends SessionHandler {

        @Override
        public void doHandle(String target, org.eclipse.jetty.server.Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            try {
                RouteResult result = router.route(request);
                Route route = result.route();
                if (route != null) {
                    // create wrapped request
                    Request wrappedRequest = new Request(request);
                    // set namedPaths if needed
                    if (result.hasNamedPaths()) {
                        wrappedRequest.namedPaths(result.namedPaths());
                    }
                    // create wrapped response
                    Response wrappedResponse = new Response(response);
                    // initialize a controller
                    Controller controller = route.controllerClass().newInstance();
                    // inject fields
                    application().injectTo(controller);
                    // set request/response/context
                    controller.request(wrappedRequest);
                    controller.response(wrappedResponse);
                    controller.context(WebContext.this);
                    // invoke beforeAction
                    controller.beforeAction();
                    // invoke method
                    Method method = route.method();
                    if (!method.isAccessible()) method.setAccessible(true);
                    method.invoke(controller);
                    // mark handled
                    baseRequest.setHandled(true);
                } else {
                    // mark not handled
                    baseRequest.setHandled(false);
                }
            } catch (Exception e) {
                if (e instanceof InvocationTargetException) {
                    WebContext.this.logger.error("ActionError", e.getCause());
                } else {
                    WebContext.this.logger.error("InternalError", e);
                }
                this.doError(target, baseRequest, request, response);
            }
        }

    }
}
