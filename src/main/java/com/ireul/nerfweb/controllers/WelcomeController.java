package com.ireul.nerfweb.controllers;

import com.ireul.nerf.web.controller.Action;
import com.ireul.nerf.web.controller.BaseController;

/**
 * Created by ryan on 5/30/17.
 */
public class WelcomeController extends BaseController {

    @Action("/")
    @Action("/index")
    public void index() {
        bodyPlain("Welcome to Nerfweb");
    }

    @Action("/index_old")
    public void alterIndex() {
        redirect("/");
    }

}
