package com.ireul.nerf.web.controller;

import com.ireul.nerf.web.server.Request;
import com.ireul.nerf.web.server.Response;

/**
 * Basic implementation of Controller
 * <p>
 * Just storage request, response for later fetch
 * <p>
 * Created by ryan on 5/30/17.
 */
public class BaseController implements Controller {

    private Response response;

    private Request request;

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
    public void beforeAction() {
        header("Server", "Nerf");
    }

}
