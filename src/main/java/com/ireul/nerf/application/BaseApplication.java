package com.ireul.nerf.application;

import com.ireul.nerf.command.Command;
import com.ireul.nerf.command.Handle;
import com.ireul.nerf.utils.AnnotationUtils;
import com.ireul.nerf.web.route.Router;
import com.ireul.nerf.web.server.JettyHandler;
import com.ireul.nerf.web.server.JettyServer;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * Created by ryan on 5/27/17.
 */
public class BaseApplication implements Application {

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
        AnnotationUtils.findInstanceMethod(this, Handle.class, (method, handle) -> {
            if (Arrays.stream(handle.value()).anyMatch(command.name::equalsIgnoreCase)) {
                try {
                    method.invoke(this, command);
                } catch (IllegalAccessException | InvocationTargetException e1) {
                    e1.printStackTrace();
                }
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
        Router router = Router.scan(this.getClass().getPackage().getName());
        this.server = new JettyServer("127.0.0.1:7788", new JettyHandler(router));
        try {
            this.server.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(100);
        }
    }

}
