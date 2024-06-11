package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import net.sourceforge.beanbin.*;
import net.sourceforge.beanbin.query.*;
import org.junit.Test;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch search = new ReflectionSearch(Object.class);

        // Test exact matches
        assertTrue(search.hasAnnotation("Object"));
        assertTrue(search.hasAnnotation("java.lang.annotation.Annotation"));

        // Test wildcard matches
        assertTrue(search.hasAnnotation("java.lang.*"));
        assertTrue(search.hasAnnotation("java.lang.annotation.*"));
        assertFalse(search.hasAnnotation("java.util.*"));

        // Test negative matches
        assertFalse(search.hasAnnotation("java.lang.String"));
        assertFalse(search.hasAnnotation("net.sourceforge.beanbin.reflect.*"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch search = new ReflectionSearch(String.class);
        search.methodsThatHave("test");

        List<Method> methods = search.getMethods();

        assertNotNull(methods);
        assertTrue(methods.size() > 0);
        for (Method method : methods) {
            assertTrue(method.getName().contains("test"));
        }
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch search = new ReflectionSearch(String.class);
        search.methodsThatDontHave("test");

        List<Method> methods = search.getMethods();

        assertNotNull(methods);
        assertTrue(methods.size() > 0);
        for (Method method : methods) {
            assertFalse(method.getName().contains("test"));
        }
    }

    @Test
    public void testAnd() throws BeanBinException {
        ReflectionSearch search = new ReflectionSearch(String.class);
        search.methodsThatHave("test");
        search.and();
        search.methodsThatHave("public");

        List<Method> methods = search.getMethods();

        assertNotNull(methods);
        assertTrue(methods.size() > 0);
        for (Method method : methods) {
            assertTrue(method.getName().contains("test"));
            assertTrue(Modifier.isPublic(method.getModifiers()));
        }
    }

    @Test
    public void testOr() throws BeanBinException {
        ReflectionSearch search = new ReflectionSearch(String.class);
        search.methodsThatHave("test");
        search.or();
        search.methodsThatHave("get");

        List<Method> methods = search.getMethods();

        assertNotNull(methods);
        assertTrue(methods.size() > 0);
        for (Method method : methods) {
            assertTrue(method.getName().matches("(get|test)"));
        }
    }
}