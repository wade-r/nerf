package com.ireul.nerf.web.render;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * This interface abstract various template engines
 *
 * @author Ryan Wade
 */
// TODO: 2017/6/1 Provide a auto-select Renderer
public interface Renderer {

    /**
     * Render a template with given model into given output
     *
     * @param template template name, relative file path to "/views" without file extension
     * @param model    model
     * @param output   output, basically {@link HttpServletResponse#getWriter()}
     */
    void render(String template, HashMap<String, Object> model, PrintWriter output);

}
