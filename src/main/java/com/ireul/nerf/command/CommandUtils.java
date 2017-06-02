package com.ireul.nerf.command;

import com.ireul.nerf.utils.AnnotationUtils;
import com.ireul.nerf.utils.MethodAndAnnotation;

import java.lang.reflect.Method;
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
    public static Stream<Method> findHandles(Class<?> clazz, String commandName) {
        // create a priority queue for multiple methods invocation
        PriorityQueue<MethodAndAnnotation<Handle>> queue = new PriorityQueue<>(1, Comparator.comparingInt(m -> m.annotation.priority()));
        // append queue
        AnnotationUtils
                .findInstanceMethods(clazz, Handle.class)
                .filter(maa -> Arrays.stream(maa.annotation.value()).anyMatch(commandName::equalsIgnoreCase))
                .forEach(queue::add);
        return queue.stream().map(m -> m.method);
    }

}
