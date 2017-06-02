package com.ireul.nerf.application;

import com.ireul.nerf.command.Command;
import com.ireul.nerf.command.CommandUtils;
import com.ireul.nerf.command.Handle;
import com.ireul.nerf.utils.AnnotationUtils;
import com.ireul.nerf.web.WebContext;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

/**
 * This is a handy implementation of {@link Application}
 * <p>Most people may want to subclass this class, rather than implement {@link Application}</p>
 *
 * @author Ryan Wade
 */
public class BaseApplication implements Application {

    /**
     * context of web service
     */
    private WebContext webContext;

    /**
     * default {@link Application#boot()} implementation does nothing
     */
    @Override
    public void boot() {
    }

    /**
     * default {@link Application#shutdown()} implementation stop the {@link WebContext} if exists
     */
    @Override
    public void shutdown() {
        // stop the WebContext if not null
        if (this.webContext != null)
            this.webContext.stop();
    }

    /**
     * Handle {@link Command}s by searching methods annotated with {@link Handle}
     *
     * @param command the command to handle
     */
    public void execute(Command command) {
        CommandUtils.findHandles(this.getClass(), command.name).forEach(method -> {
            // set accessible to true
            if (!method.isAccessible()) method.setAccessible(true);
            try {
                // invoke
                if (method.getParameterCount() == 1) {
                    method.invoke(this, command);
                } else {
                    method.invoke(this);
                }
            } catch (IllegalAccessException | InvocationTargetException e1) {
                e1.printStackTrace();
                System.exit(1);
            }
        });
    }

    /**
     * Built-in {@link Handle} for "help" command, print all available commands and descriptions
     *
     * @param command the "help" {@link Command}
     */
    @Handle(value = Command.DEFAULT_NAME, desc = "print help")
    public void handleHelp(Command command) {
        PrintStream o = System.out;
        o.println();
        o.println("Welcome to Nerf Web Framework");
        o.println();
        o.println("available commands:");
        o.println();
        AnnotationUtils.findInstanceMethods(this.getClass(), Handle.class).forEach(maa -> {
            Handle handle = maa.annotation;
            for (String name : handle.value()) {
                if (handle.desc().length() > 0) {
                    o.println("  " + name + " - " + handle.desc());
                }
            }
        });
    }

    /**
     * Built-in {@link Handle} for "web" command, run the web service
     *
     * @param command the "web" {@link Command}
     */
    @Handle(value = "web", desc = "start web server")
    public void handleWeb(Command command) {
        // initialize webContext
        this.webContext = new WebContext(this);
        // setup
        this.webContext.setup(command.options);
        // start
        this.webContext.start();
    }

}
