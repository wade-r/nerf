package com.ireul.nerf.command;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ryan on 5/27/17.
 */
public class Command {

    public static final String DEFAULT_NAME = "help";

    public String name;

    public HashMap<String, String> options = new HashMap<>();

    public ArrayList<String> targets = new ArrayList<>();

    public static Command decode(String[] args) {
        Command command = new Command();
        String lastOption = null;
        for (String e : args) {
            if (e.startsWith("-")) {
                if (lastOption == null) {
                    lastOption = e.substring(1);
                } else {
                    command.options.put(lastOption, "true");
                    lastOption = e.substring(1);
                }
            } else {
                if (lastOption == null) {
                    if (command.name == null) {
                        command.name = e;
                    } else {
                        command.targets.add(e);
                    }
                } else {
                    command.options.put(lastOption, e);
                    lastOption = null;
                }
            }
        }
        if (lastOption != null) {
            command.options.put(lastOption, "true");
        }
        if (command.name == null) {
            command.name = DEFAULT_NAME;
        }
        return command;
    }
}
