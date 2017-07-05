package com.ireul.nerf.web.route;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ryan on 6/1/17.
 */
public class RouteUtilsTest {

    @Test
    public void scanPackage() throws Exception {
        //TODO: add tests
    }

    @Test
    public void scanClass() throws Exception {
        //TODO: add tests
    }

    @Test
    public void matchPath() throws Exception {
        RouteResult result = new RouteResult();
        // 0. basic match
        boolean ok1 = RouteUtils.matchPath("/index", "/index", result);
        assertTrue(ok1);
        assertFalse(result.hasNamedPaths());
        // 1. named match
        result = new RouteResult();
        boolean ok2 = RouteUtils.matchPath("/index/:name", "/index/hello", result);
        assertTrue(ok2);
        assertTrue(result.hasNamedPaths());
        assertTrue(result.getNamedPaths().size() == 1);
        assertEquals(result.getNamedPaths().get("name"), "hello");
        // 2. multiple match
        result = new RouteResult();
        boolean ok3 = RouteUtils.matchPath("/index/:name/:value", "/index/hello/world", result);
        assertTrue(ok3);
        assertTrue(result.hasNamedPaths());
        assertTrue(result.getNamedPaths().size() == 2);
        assertEquals(result.getNamedPaths().get("name"), "hello");
        assertEquals(result.getNamedPaths().get("value"), "world");
    }

    @Test
    public void matchMime() throws Exception {
        //TODO: add tests
    }

}