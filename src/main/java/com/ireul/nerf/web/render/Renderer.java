package com.ireul.nerf.web.render;

import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Created by ryan on 6/1/17.
 */
public interface Renderer {

    void render(String template, HashMap<String, Object> model, PrintWriter output);

}
