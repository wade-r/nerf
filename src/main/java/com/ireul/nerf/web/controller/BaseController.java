package com.ireul.nerf.web.controller;

import com.ireul.nerf.inject.Inject;
import com.ireul.nerf.web.WebContext;
import com.ireul.nerf.web.server.Request;
import com.ireul.nerf.web.server.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a handy implementation of {@link Controller}, basically provides storage fields and getter-setters.
 * <p>Most people should subclass this class, rather than implement {@link Controller}</p>
 *
 * @author Ryan Wade
 * @see Controller
 */
public class BaseController implements Controller {

    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    private Response response;

    private Request request;

    @Inject
    private WebContext context;

    @Override
    public Request request() {
        return this.request;
    }

    @Override
    public Response response() {
        return this.response;
    }

    @Override
    public void request(Request request) {
        this.request = request;
    }

    @Override
    public void response(Response response) {
        this.response = response;
    }

    @Override
    public WebContext context() {
        return this.context;
    }

    /**
     * default implementation sets the "Server" header to "Nerf"
     */
    @Override
    public void beforeAction() {
        header("Server", "Nerf");
        response().encoding("UTF-8");
    }

}
