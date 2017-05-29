package com.ireul.nerfweb;

import com.ireul.nerf.Nerf;
import com.ireul.nerf.application.BaseApplication;

/**
 * Created by ryan on 5/27/17.
 */
public class Main extends BaseApplication {

    public static void main(String[] args) {
        Nerf.ignite(Main.class, args);
    }

}
