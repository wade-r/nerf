package com.ireul.nerf.web.server;

import com.ireul.nerf.web.route.Router;
import com.ireul.nerf.web.route.SimpleRouter;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.session.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * JettyHandler inherit Jetty {@link SessionHandler}, provides a bridge from Nerf to Jetty
 * Created by ryan on 5/31/17.
 */
public class JettyHandler extends SessionHandler {

    private final Logger logger = LoggerFactory.getLogger(JettyHandler.class);

    private Router router;

    public JettyHandler(SimpleRouter router) {
        this.router = router;
    }

    @Override
    public void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            baseRequest.setHandled(this.router.route(request, response));
        } catch (Exception e) {
            if (e instanceof InvocationTargetException) {
                this.logger.error("ActionError", e.getCause());
            } else {
                this.logger.error("InternalError", e);
            }
            this.doError(target, baseRequest, request, response);
        }
    }

}
