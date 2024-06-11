package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.search.Criteria;
import net.sourceforge.beanbin.search.ReflectionCriteria;
import net.sourceforge.beanbin.search.SearchType;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);

        // Test when annotation is present
        assertEquals(true, reflectionSearch.hasAnnotation("java.lang.annotation.Annotation"));
        assertEquals(true, reflectionSearch.hasAnnotation("Object"));

        // Test when annotation is not present
        assertFalse(reflectionSearch.hasAnnotation("java.lang.String"));
        assertFalse(reflectionSearch.hasAnnotation("MyAnnotation"));

        // Test when term is a wildcard
        reflectionSearch = new ReflectionSearch(String.class);
        assertTrue(reflectionSearch.hasWildcard("*Annotation"));
        assertTrue(reflectionSearch.hasWildcard("String*"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);

        // Test with exact method name
        reflectionSearch = reflectionSearch.methodsThatHave("toString");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertEquals("toString", methods.get(0).getName());

        // Test with method name that doesn't exist
        reflectionSearch = reflectionSearch.methodsThatHave("nonExistentMethod");
        methods = reflectionSearch.getMethods();
        assertNull(methods);

        // Test with wildcard method name
        reflectionSearch = reflectionSearch.methodsThatHave("get*");
        methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertTrue(methods.get(0).getName().startsWith("get"));
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);

        // Test with exact method name
        reflectionSearch = reflectionSearch.methodsThatDontHave("toString");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(0, methods.size());

        // Test with method name that exists
        reflectionSearch = reflectionSearch.methodsThatDontHave("equals");
        methods = reflectionSearch.getMethods();
        assertNull(methods);

        // Test with wildcard method name
        reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch = reflectionSearch.methodsThatDontHave("set*");
        methods = reflectionSearch.getMethods();
        assertEquals(0, methods.size());
    }

    @Test
    public void testAnd() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);

        reflectionSearch = reflectionSearch.methodsThatHave("toString");
        reflectionSearch = reflectionSearch.and();
        reflectionSearch = reflectionSearch.methodsThatDontHave("equals");

        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(0, methods.size());
    }

    @Test
    public void testOr() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);

        reflectionSearch = reflectionSearch.methodsThatHave("toString");
        reflectionSearch = reflectionSearch.or();
        reflectionSearch = reflectionSearch.methodsThatHave("equals");

        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
        assertTrue(methods.containsAll(Arrays.asList(Object.class.getMethod("toString"), Object.class.getMethod("equals"))));
    }

    @Test
    public void testMethods() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);

        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(0, methods.size());

        reflectionSearch = reflectionSearch.methodsThatHave("getLength");
        methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertEquals("getLength", methods.get(0).getName());

        reflectionSearch = reflectionSearch.and();
        methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertEquals("length", methods.get(0).getName());

        reflectionSearch = reflectionSearch.or();
        methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
        assertTrue(methods.containsAll(Arrays.asList(String.class.getMethod("getLength"), String.class.getMethod("length"))));
    }
}