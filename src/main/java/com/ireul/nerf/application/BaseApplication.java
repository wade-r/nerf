package com.ireul.nerf.application;

import com.ireul.nerf.command.Command;
import com.ireul.nerf.command.Handle;
import com.ireul.nerf.inject.Injector;
import com.ireul.nerf.utils.AnnotationUtils;
import com.ireul.nerf.web.WebContext;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * Created by ryan on 5/27/17.
 */
public class BaseApplication implements Application, Injector {

    private WebContext webContext;

    /*******************************************************************************************************************
     * Life Cycle
     ******************************************************************************************************************/

    public void boot() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    public void shutdown() {
        if (this.webContext != null)
            this.webContext.stop();
    }

    /**
     * Handle Command by searching Handle annotated method
     *
     * @param command the command to handle
     */
    public void execute(Command command) {
        AnnotationUtils.forEachInstanceMethod(this.getClass(), Handle.class, (method, handle) -> {
            if (Arrays.stream(handle.value()).noneMatch(command.name::equalsIgnoreCase)) {
                return;
            }
            try {
                method.invoke(this, command);
            } catch (IllegalAccessException | InvocationTargetException e1) {
                e1.printStackTrace();
            }
        });
    }

    /*******************************************************************************************************************
     * Command
     ******************************************************************************************************************/

    @Handle(value = Command.DEFAULT_NAME, desc = "print help")
    public void handleHelp(Command command) {
        PrintStream o = System.out;
        o.println();
        o.println("Welcome to Nerf Web Framework");
        o.println();
        o.println("available commands:");
        o.println();
        AnnotationUtils.forEachInstanceMethod(this.getClass(), Handle.class, (method, handle) -> {
            for (String name : handle.value()) {
                o.println("  " + name + " - " + handle.desc());
            }
        });
    }

    @Handle(value = "web", desc = "start web server")
    public void handleWeb(Command command) {
        this.webContext = new WebContext(this);
        this.webContext.setup(command.options);
        this.webContext.start();
    }

}
