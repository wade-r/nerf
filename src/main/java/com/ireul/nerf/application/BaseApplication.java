package com.ireul.nerf.application;

import com.ireul.nerf.command.Command;
import com.ireul.nerf.command.Entry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * Created by ryan on 5/27/17.
 */
public class BaseApplication implements Application {

    /*******************************************************************************************************************
     * Life Cycle
     ******************************************************************************************************************/

    public void setup() {
    }

    /**
     * Handle command-line arguments, search and execute command method
     *
     * @param args command-line arguments
     */
    public void execute(String[] args) {
        Command command = Command.decode(args);
        Arrays.stream(this.getClass().getMethods())
                .filter(m -> !Modifier.isStatic(m.getModifiers()))
                .forEach(method -> {
                    Entry entry = method.getAnnotation(Entry.class);
                    if (entry != null &&
                            Arrays.stream(entry.value()).anyMatch(command.name::equalsIgnoreCase)) {
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

    @Entry("run")
    public void handleRun(Command command) {
    }
}
