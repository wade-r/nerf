package com.ireul.nerf;

import com.ireul.nerf.application.Application;
import com.ireul.nerf.command.Command;

/**
 * This is a helper class for booting a Nerf application.
 * <p>
 * <p>Most common usage may looks like below:</p>
 * <p>
 * <pre>
 *     public class MyMain extends BaseApplication {
 *
 *         public static void main(String[] args) {
 *             Nerf.ignite(Main.class, args);
 *         }
 *
 *     }
 * </pre>
 * <p>
 * <p>Remember, <code>MyMain</code> MUST be at the TOP of ALL your packages, component searching are based on
 * sub-packages of <code>MyMain</code>.</p>
 * <p>
 * <p>Command-line arguments will be parsed into a {@link com.ireul.nerf.command.Command} instance, and send to
 * {@link Application#execute(Command)}. In most case, <code>MyMain</code> is a subclass of
 * {@link com.ireul.nerf.application.BaseApplication}, it will find its instance methods marked by
 * {@link com.ireul.nerf.command.Handle} annotation and invoke that method.</p>
 * <p>
 * <p><code>MyClass</code> will also be the default {@link com.ireul.nerf.inject.Injector}, during initialization of
 * {@link com.ireul.nerf.web.controller.Controller} classes, fields marked by {@link com.ireul.nerf.inject.Inject} will be assigned from <code>MyClass</code></p>
 *
 * @author Ryan Wade
 * @see Application
 * @see com.ireul.nerf.command.Command
 * @see com.ireul.nerf.command.Handle
 */
public class Nerf {

    /**
     * Boot a Nerf application
     *
     * @param clazz implementation of {@link Application}
     * @param args  command-line arguments
     */
    public static void ignite(Class<? extends Application> clazz, String[] args) {
        // initialize a instance
        Application application = null;
        try {
            application = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            System.exit(1);
        }
        // register shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(application::shutdown));
        // boot
        application.boot();
        // execute command-line arguments
        application.execute(args);
    }

}
