package com.ireul.nerf.application;

import com.ireul.nerf.command.Command;
import com.ireul.nerf.command.Handle;
import com.ireul.nerf.utils.AnnotationUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * Created by ryan on 5/27/17.
 */
public class BaseApplication implements Application {

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
    }
}
