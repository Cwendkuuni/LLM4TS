package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import java.lang.reflect.*;
import java.util.*;

import org.junit.Test;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);

        // Test with exact match
        assertTrue(reflectionSearch.hasAnnotation("Object"));
        // Test with class name match
        assertTrue(reflectionSearch.hasAnnotation("java.lang.Object"));
        // Test with interface match
        assertTrue(reflectionSearch.hasAnnotation("java.io.Serializable"));
        // Test with annotation name match
        assertTrue(reflectionSearch.hasAnnotation("javax.annotation.PostConstruct"));
        // Test with wildcard match
        assertTrue(reflectionSearch.hasAnnotation("java.*"));
        // Test with no match
        assertFalse(reflectionSearch.hasAnnotation("com.example.MyAnnotation"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);

        List<Method> methods = reflectionSearch.methodsThatHave("equals").getMethods();
        assertNotNull(methods);
        assertEquals(1, methods.size());
        assertEquals("equals", methods.get(0).getName());

        methods = reflectionSearch.methodsThatHave("toString").getMethods();
        assertNotNull(methods);
        assertEquals(1, methods.size());
        assertEquals("toString", methods.get(0).getName());

        methods = reflectionSearch.methodsThatHave("hashCode").getMethods();
        assertNotNull(methods);
        assertEquals(1, methods.size());
        assertEquals("hashCode", methods.get(0).getName());
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);

        List<Method> methods = reflectionSearch.methodsThatDontHave("equals").getMethods();
        assertNotNull(methods);
        assertTrue(methods.isEmpty());

        methods = reflectionSearch.methodsThatDontHave("toString").getMethods();
        assertNotNull(methods);
        assertTrue(methods.isEmpty());

        methods = reflectionSearch.methodsThatDontHave("hashCode").getMethods();
        assertNotNull(methods);
        assertTrue(methods.isEmpty());
    }

    @Test
    public void testAnd() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);

        List<Method> methods = reflectionSearch.methodsThatHave("equals").methodsThatHave("public").getMethods();
        assertNotNull(methods);
        assertEquals(1, methods.size());
        assertEquals("equals", methods.get(0).getName());
        assertEquals("public", methods.get(0).getModifiers());

        methods = reflectionSearch.methodsThatHave("equals").methodsThatDontHave("private").getMethods();
        assertNotNull(methods);
        assertTrue(methods.isEmpty());
    }

    @Test
    public void testOr() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);

        List<Method> methods = reflectionSearch.methodsThatHave("equals").or().methodsThatHave("toString").getMethods();
        assertNotNull(methods);
        assertEquals(2, methods.size());
        assertTrue(methods.containsAll(Arrays.asList(ReflectionSearchTest.class.getMethod("equals"), ReflectionSearchTest.class.getMethod("toString"))));

        methods = reflectionSearch.methodsThatHave("equals").or().methodsThatDontHave("toString").getMethods();
        assertNotNull(methods);
        assertTrue(methods.isEmpty());

        methods = reflectionSearch.methodsThatDontHave("equals").or().methodsThatHave("toString").getMethods();
        assertNotNull(methods);
        assertTrue(methods.isEmpty());
    }
}