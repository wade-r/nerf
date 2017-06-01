package com.ireul.nerf.web.controller;

import com.ireul.nerf.web.WebContext;
import com.ireul.nerf.web.server.Request;
import com.ireul.nerf.web.server.Response;

/**
 * Basic implementation of Controller
 * Just storage request, response for later fetch
 * Created by ryan on 5/30/17.
 */
public class BaseController implements Controller {

    private Response response;

    private Request request;

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
    public void context(WebContext context) {
        this.context = context;
    }

    @Override
    public WebContext context() {
        return this.context;
    }

    @Override
    public void beforeAction() {
        header("Server", "Nerf");
    }

}
