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
        Class clazz = String[].class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        // Test exact match
        assertTrue(reflectionSearch.hasAnnotation("StringUtils"));

        // Test match against class name
        assertTrue(reflectionSearch.hasAnnotation("net.sourceforge.beanbin.utils.StringUtils"));

        // Test match against annotation name with wildcard
        assertTrue(reflectionSearch.hasAnnotation("*Utils"));

        // Test no match
        assertFalse(reflectionSearch.hasAnnotation("MyAnnotation"));
        assertFalse(reflectionSearch.hasAnnotation("java.lang.String"));

        // Test null input
        assertFalse(new ReflectionSearch(null).hasAnnotation("StringUtils"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        Class clazz = String[].class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        List<Method> methods = reflectionSearch.methodsThatHave("equals").getMethods();
        assertEquals(1, methods.size());
        assertEquals("equals", methods.get(0).getName());

        methods = reflectionSearch.methodsThatHave("toString").getMethods();
        assertEquals(1, methods.size());
        assertEquals("toString", methods.get(0).getName());

        methods = reflectionSearch.methodsThatHave("equals*").getMethods();
        assertEquals(1, methods.size());
        assertEquals("equals", methods.get(0).getName());

        methods = reflectionSearch.methodsThatHave("*equals").getMethods();
        assertEquals(1, methods.size());
        assertEquals("equals", methods.get(0).getName());

        methods = reflectionSearch.methodsThatHave("*").getMethods();
        assertNotNull(methods);
        assertTrue(methods.size() > 0);
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        Class clazz = String[].class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        List<Method> methods = reflectionSearch.methodsThatDontHave("equals").getMethods();
        assertTrue(methods.size() > 0);

        methods = reflectionSearch.methodsThatDontHave("hashCode").getMethods();
        assertEquals(1, methods.size());
        assertEquals("hashCode", methods.get(0).getName());
    }

    @Test
    public void testAnd() throws BeanBinException {
        Class clazz = String[].class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        List<Method> methods = reflectionSearch.methodsThatHave("equals").methodsThatDontHave("hashCode").getMethods();
        assertTrue(methods.size() == 0);

        methods = reflectionSearch.methodsThatHave("equals").and().methodsThatHave("toString").getMethods();
        assertEquals(1, methods.size());
        assertEquals("toString", methods.get(0).getName());
    }

    @Test
    public void testOr() throws BeanBinException {
        Class clazz = String[].class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        List<Method> methods = reflectionSearch.methodsThatHave("equals").or().methodsThatHave("toString").getMethods();
        assertEquals(2, methods.size());

        methods = reflectionSearch.methodsThatHave("equals").getMethods();
        assertEquals(1, methods.size());
        assertEquals("equals", methods.get(0).getName());

        methods = reflectionSearch.methodsThatHave("toString").getMethods();
        assertEquals(1, methods.size());
        assertEquals("toString", methods.get(0).getName());
    }
}