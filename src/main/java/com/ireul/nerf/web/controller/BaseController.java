package com.ireul.nerf.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * Basic implementation of Controller
 * <p>
 * Just storage request, response for later fetch
 * <p>
 * Created by ryan on 5/30/17.
 */
public class BaseController implements Controller {

    private HttpServletResponse response;

    private HttpServletRequest request;

    private HashMap<String, Object> locals = new HashMap<>();

    @Override
    public HttpServletRequest request() {
        return request;
    }

    @Override
    public HttpServletResponse response() {
        return response;
    }

    @Override
    public void request(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void response(HttpServletResponse response) {
        this.response = response;
    }

    @Override
    public void local(String key, Object value) {
        this.locals.put(key, value);
    }

    @Override
    public <T> T local(String key) {
        return (T) this.locals.get(key);
    }

    @Override
    public void beforeAction() {
        header("Server", "Nerf");
    }

}
