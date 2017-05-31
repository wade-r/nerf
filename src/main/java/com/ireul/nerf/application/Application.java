package com.ireul.nerf.application;

import com.ireul.nerf.command.Command;
import com.ireul.nerf.inject.Injector;

/**
 * Application abstract a Nerf application
 * Contains two method, boot and execute
 * <p>
 * Created by ryan on 5/27/17.
 */
public interface Application extends Injector {

    /**
     * Bootstrap the Application
     */
    void boot();

    /**
     * Execute command-line arguments
     *
     * @param args arguments
     */
    default void execute(String[] args) {
        execute(Command.decode(args));
    }

    /**
     * Execute a command
     *
     * @param command command to execute
     */
    void execute(Command command);

}
