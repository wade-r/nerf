package com.ireul.nerf.web.server;

import com.ireul.nerf.web.route.Route;
import com.ireul.nerf.web.route.Router;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.session.SessionHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ryan on 5/31/17.
 */
public class JettyHandler extends SessionHandler {

    private Router router;

    public JettyHandler(Router router) {
        this.router = router;
    }

    @Override
    public void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        for (Route route : this.router.routes()) {
            if (route.match(baseRequest)) {
                route.execute(baseRequest, response);
                baseRequest.setHandled(true);
                return;
            }
        }
        baseRequest.setHandled(false);
    }

}
