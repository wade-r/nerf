package com.ireul.nerf.command;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * {@link Command} is a decoded command-line
 * <p>Supported format is listed below:</p>
 * <pre>
 *     java -jar your-application.jar command_name --option1 --option2 value target1 target2
 * </pre>
 * <p>All options will be parsed as a String to String HashMap, boolean option (option without value) will have a value
 * "true".</p>
 * <p>Default command name is "help"</p>
 *
 * @author Ryan Wade
 * @see com.ireul.nerf.application.Application
 * @see com.ireul.nerf.Nerf
 * @see Handle
 */
public class Command {

    public static final String DEFAULT_NAME = "help";

    public String name;

    public HashMap<String, String> options = new HashMap<>();

    public ArrayList<String> targets = new ArrayList<>();

    /**
     * Decode command-line arguments to {@link Command}
     *
     * @param args command-line arguments, basically from main method
     * @return {@link Command} instance
     */
    public static Command decode(String[] args) {
        Command command = new Command();
        String lastOption = null;
        for (String e : args) {
            if (e.startsWith("-")) {
                if (lastOption == null) {
                    lastOption = e.substring(e.startsWith("--") ? 2 : 1);
                } else {
                    command.options.put(lastOption, "true");
                    lastOption = e.substring(e.startsWith("--") ? 2 : 1);
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
