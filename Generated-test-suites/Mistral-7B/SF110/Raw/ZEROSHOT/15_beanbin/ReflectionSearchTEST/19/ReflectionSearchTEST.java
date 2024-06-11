package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import org.junit.Test;

import net.sourceforge.beanbin.*;
import net.sourceforge.beanbin.search.*;
import net.sourceforge.beanbin.reflect.ReflectionSearch;

/**
 * JUnit 4 test file for the ReflectionSearch class.
 */
public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() throws Exception {
        Class clazz = Class.forName("net.sourceforge.beanbin.reflect.ReflectionSearch");
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        // Test when term matches exactly with annotation name or class name
        assertTrue(reflectionSearch.hasAnnotation("Annotation1"));
        assertTrue(reflectionSearch.hasAnnotation("ReflectionSearch"));

        // Test when term matches with wildcard character
        assertTrue(reflectionSearch.hasAnnotation("Annotation*"));
        assertTrue(reflectionSearch.hasAnnotation("*Search"));

        // Test when term does not match with any annotation
        assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
        assertFalse(reflectionSearch.hasAnnotation("NonExistentClass"));
    }

    @Test
    public void testMethodsThatHave() throws Exception {
        Class clazz = Class.forName("net.sourceforge.beanbin.reflect.ReflectionSearch");
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        // Test when method name matches exactly with the term
        reflectionSearch.methodsThatHave("getMethods");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertEquals(ReflectionSearch.class.getMethod("getMethods"), methods.get(0));

        // Test when method name matches with wildcard character
        reflectionSearch.methodsThatHave("getM*ths");
        methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertEquals(ReflectionSearch.class.getMethod("getMethods"), methods.get(0));

        methods = reflectionSearch.getMethods(); // Reset the list

        // Test when method name does not match any method
        reflectionSearch.methodsThatHave("doesNotExist");
        methods = reflectionSearch.getMethods();
        assertTrue(methods.isEmpty());
    }

    @Test
    public void testMethodsThatDontHave() throws Exception {
        Class clazz = Class.forName("net.sourceforge.beanbin.reflect.ReflectionSearch");
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        // Test when method name does not match any method
        reflectionSearch.methodsThatDontHave("getMethods");
        List<Method> methods = reflectionSearch.getMethods();
        assertFalse(methods.isEmpty());

        // Test when method name matches exactly with the term
        reflectionSearch.methodsThatDontHave("getMethods");
        methods = reflectionSearch.getMethods();
        assertTrue(methods.isEmpty());
    }

    @Test
    public void testAndConditional() throws Exception {
        Class clazz = Class.forName("net.sourceforge.beanbin.reflect.ReflectionSearch");
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        reflectionSearch.methodsThatHave("get");
        reflectionSearch.and();
        reflectionSearch.methodsThatHave("Methods");

        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(0, methods.size());

        reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch.methodsThatHave("get");
        reflectionSearch.and();
        reflectionSearch.methodsThatHave("Methods");

        methods = reflectionSearch.getMethods();
        assertEquals(0, methods.size());

        reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch.methodsThatHave("get");
        List<Method> methods1 = reflectionSearch.getMethods();
        reflectionSearch.and();
        List<Method> methods2 = reflectionSearch.getMethods();

        assertEquals(methods1.size(), methods2.size());
        for (Method method1 : methods1) {
            boolean found = false;
            for (Method method2 : methods2) {
                if (method1.equals(method2)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    public void testOrConditional() throws Exception {
        Class clazz = Class.forName("net.sourceforge.beanbin.reflect.ReflectionSearch");
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        reflectionSearch.methodsThatHave("get");
        reflectionSearch.or();
        reflectionSearch.methodsThatHave("set");

        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());

        reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch.methodsThatHave("get");
        reflectionSearch.or();
        reflectionSearch.methodsThatHave("set");
        reflectionSearch.and();

        methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());

        methods = reflectionSearch.getMethods(); // Reset the list

        reflectionSearch.methodsThatHave("get");
        reflectionSearch.or();
        reflectionSearch.methodsThatHave("set");
        List<Method> methods1 = reflectionSearch.getMethods();
        reflectionSearch.and();
        List<Method> methods2 = reflectionSearch.getMethods();

        assertEquals(methods1.size(), methods2.size());
        for (Method method1 : methods1) {
            boolean found = false;
            for (Method method2 : methods2) {
                if (method1.equals(method2)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
    }
}