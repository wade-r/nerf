package com.ireul.nerf.schedule;

import com.ireul.nerf.utils.AnnotatedClass;
import org.quartz.Job;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author Ryan Wade
 */
public class ScheduleUtils {

    /**
     * Scan a package, returns a stream of all {@link org.quartz.Job} Class / {@link Schedule} pair
     *
     * @param basePackage package to scan
     * @return stream
     */
    public static Stream<AnnotatedClass<Schedule, Job>> findJobs(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getSubTypesOf(Job.class)
                .stream()
                .flatMap(clazz -> {
                    return Arrays.stream(clazz.getAnnotationsByType(Schedule.class))
                            .map(annotation -> new AnnotatedClass<>(clazz, annotation));
                });
    }


    public static Stream<AnnotatedClass<Schedule, Job>> findJobs(Class<?> clazz) {
        return findJobs(clazz.getPackage().getName());
    }

}
