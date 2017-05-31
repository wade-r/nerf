package com.ireul.nerf.application;

import com.ireul.nerf.command.Command;
import com.ireul.nerf.command.Handle;
import com.ireul.nerf.inject.Injector;
import com.ireul.nerf.utils.AnnotationUtils;
import com.ireul.nerf.web.route.SimpleRouter;
import com.ireul.nerf.web.server.JettyHandler;
import com.ireul.nerf.web.server.JettyServer;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * Created by ryan on 5/27/17.
 */
public class BaseApplication implements Application, Injector {

    private JettyServer server;

    /*******************************************************************************************************************
     * Life Cycle
     ******************************************************************************************************************/

    public void boot() {
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

    @Handle(Command.DEFAULT_NAME)
    public void handleHelp(Command command) {
    }

    @Handle("web")
    public void handleWeb(Command command) {
        String bind = command.options.get("bind");
        if (bind == null) {
            bind = "127.0.0.1:7788";
        }
        SimpleRouter router = SimpleRouter.scan(this);
        this.server = new JettyServer(bind, new JettyHandler(router));
        try {
            this.server.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(100);
        }
    }

}
