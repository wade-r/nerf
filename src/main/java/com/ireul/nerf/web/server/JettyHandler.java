package com.ireul.nerf.web.server;

import com.ireul.nerf.web.route.Router;
import com.ireul.nerf.web.route.SimpleRouter;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.session.SessionHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JettyHandler inherit Jetty {@link SessionHandler}, provides a bridge from Nerf to Jetty
 * Created by ryan on 5/31/17.
 */
public class JettyHandler extends SessionHandler {

    private Router router;

    public JettyHandler(SimpleRouter router) {
        this.router = router;
    }

    @Override
    public void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        baseRequest.setHandled(this.router.route(request, response));
    }

}
