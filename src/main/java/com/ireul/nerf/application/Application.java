package com.ireul.nerf.application;

import com.ireul.nerf.command.Command;
import com.ireul.nerf.inject.Injector;

import javax.validation.constraints.NotNull;

/**
 * This is the abstraction of a Nerf application
 * <p>Any implementation of {@link Application} can be ignited by {@link com.ireul.nerf.Nerf#ignite(Class, String[])}</p>
 * <p><b>Most people may want to subclass {@link BaseApplication} rather than implement this interface.</b></p>
 * <p>{@link Application} extends {@link Injector}, your application class will be the default {@link Injector} across
 * the whole application</p>
 *
 * @author Ryan Wade
 * @see Injector
 * @see BaseApplication
 */
public interface Application extends Injector {

    /**
     * Bootstrap the application
     */
    void boot();

    /**
     * Shutdown the application
     */
    void shutdown();

    /**
     * Decode command-line arguments, and delegate to {@link Application#execute(Command)}
     *
     * @param args command-line arguments
     */
    default void execute(@NotNull String[] args) {
        execute(Command.decode(args));
    }

    /**
     * Execute a {@link Command}
     *
     * @param command command to execute
     */
    void execute(@NotNull Command command);

}
