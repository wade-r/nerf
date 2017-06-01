package com.ireul.nerf.web.render;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * This is a {@link Renderer} implementation with FreeMarker template engine.
 *
 * @author Ryan Wade
 * @see Renderer
 */
public class FreeMarkerRenderer implements Renderer {

    Configuration configuration;

    /**
     * Initialize a FreeMarker instance
     *
     * @param resourceClass class for templates loading, basically your application class
     */
    public FreeMarkerRenderer(Class<?> resourceClass) {
        this.configuration = new Configuration(Configuration.VERSION_2_3_26);
        this.configuration.setClassForTemplateLoading(resourceClass, "/views");
        this.configuration.setDefaultEncoding("UTF-8");
        this.configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        this.configuration.setLogTemplateExceptions(false);
    }

    @Override
    public void render(String template, HashMap<String, Object> model, PrintWriter output) {
        try {
            Template tpl = this.configuration.getTemplate(template + ".ftl");
            tpl.process(model, output);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }
}
