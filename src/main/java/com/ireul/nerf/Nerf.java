package com.ireul.nerf;

import com.ireul.nerf.application.Application;

/**
 * Created by ryan on 5/27/17.
 */
public class Nerf {

    /**
     * Boot a Nerf application
     *
     * @param clazz subclass of BaseApplication or any implementation of Application
     * @param args  command-line arguments
     */
    public static void ignite(Class<? extends Application> clazz, String[] args) {
        try {
            Application application = clazz.newInstance();
            application.boot();
            application.execute(args);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
