package com.ireul.nerf.schedule;

import com.ireul.nerf.utils.ClassAndAnnotation;
import org.quartz.Job;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author Ryan Wade
 */
public class ScheduleUtils {

    /**
     * Scan a package, returns a stream of all {@link Job} Class / {@link Schedule} pair
     *
     * @param basePackage package to scan
     * @return stream
     */
    public static Stream<ClassAndAnnotation<Job, Schedule>> findJobs(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getSubTypesOf(Job.class)
                .stream()
                .flatMap(clazz -> {
                    return Arrays.stream(clazz.getAnnotationsByType(Schedule.class))
                            .map(annotation -> new ClassAndAnnotation<>(clazz, annotation));
                });
    }


    public static Stream<ClassAndAnnotation<Job, Schedule>> findJobs(Class<?> clazz) {
        return findJobs(clazz.getPackage().getName());
    }

}
