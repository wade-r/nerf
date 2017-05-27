package com.ireul.nerf.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ryan on 5/27/17.
 */
public interface Controller {

    HttpServletRequest request();

    HttpServletResponse response();

    /*******************************************************************************************************************
     * Life Cycle
     ******************************************************************************************************************/

    void beforeAction();

    /*******************************************************************************************************************
     * Request
     ******************************************************************************************************************/

    default String method() {
        return request().getMethod();
    }

    default String queryString() {
        return request().getQueryString();
    }

    default String url() {
        return request().getRequestURI();
    }

}
