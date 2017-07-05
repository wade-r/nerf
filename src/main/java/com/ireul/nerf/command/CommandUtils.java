package com.ireul.nerf.command;

import com.ireul.nerf.utils.AnnotatedMethod;
import com.ireul.nerf.utils.AnnotationUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.stream.Stream;

/**
 * This helper class provides {@link Command} related methods
 *
 * @author Ryan Wade
 */
public class CommandUtils {

    /**
     * Create a stream of all handle methods from given class with specified command name in order of priority
     *
     * @param clazz       basically your application class
     * @param commandName command name
     * @return Stream of methods
     */
    public static Stream<AnnotatedMethod<Handle>> findHandles(Class<?> clazz, String commandName) {
        // create a priority queue for multiple Handles
        PriorityQueue<AnnotatedMethod<Handle>> queue = new PriorityQueue<>(
                1,
                Comparator.comparingInt(m -> m.annotation.priority())
        );
        // append queue
        AnnotationUtils
                .findInstanceMethods(clazz, Handle.class)
                .filter(x -> Arrays.stream(x.annotation.value()).anyMatch(commandName::equalsIgnoreCase))
                .forEach(queue::add);
        return queue.stream();
    }

}
