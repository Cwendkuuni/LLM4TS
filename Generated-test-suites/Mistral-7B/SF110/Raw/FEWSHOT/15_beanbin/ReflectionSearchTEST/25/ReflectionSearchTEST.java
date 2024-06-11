package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.beanbin.reflect.Query;
import net.sourceforge.beanbin.reflect.ReflectionCriteria;
import net.sourceforge.beanbin.reflect.ReflectionShelf;
import net.sourceforge.beanbin.reflect.SearchType;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() throws Exception {
        ReflectionSearch search = new ReflectionSearch(ReflectionSearch.class);
        assertFalse(search.hasAnnotation(null));
        assertFalse(search.hasAnnotation(""));
        assertFalse(search.hasAnnotation("NonExistentAnnotation"));
        assertTrue(search.hasAnnotation("Annotation"));
        assertTrue(search.hasAnnotation("net.sourceforge.beanbin.reflect.Annotation"));
        assertTrue(search.hasAnnotation("Annotation*"));
    }

    @Test
    public void testHasWildcard() {
        assertTrue(ReflectionSearch.this.hasWildcard("Annotation*"));
        assertFalse(ReflectionSearch.this.hasWildcard("Annotation"));
    }

    @Test
    public void testMethodsThatHave() throws Exception {
        ReflectionSearch search = new ReflectionSearch(ReflectionSearch.class);
        search.methodsThatHave("equals");
        List<Method> methods = search.getMethods();
        assertNotNull(methods);
        assertEquals(1, methods.size());
        Method method = methods.get(0);
        assertEquals("equals", method.getName());
    }

    @Test
    public void testMethodsThatDontHave() throws Exception {
        ReflectionSearch search = new ReflectionSearch(ReflectionSearch.class);
        search.methodsThatDontHave("equals");
        List<Method> methods = search.getMethods();
        assertNotNull(methods);
        assertEquals(0, methods.size());
    }

    @Test
    public void testMethodsThatHaveAndMethodsThatDontHave() throws Exception {
        ReflectionSearch search = new ReflectionSearch(ReflectionSearch.class);
        search.methodsThatHave("equals");
        search.methodsThatDontHave("toString");
        List<Method> methods = search.getMethods();
        assertNotNull(methods);
        assertEquals(1, methods.size());
        Method method = methods.get(0);
        assertEquals("equals", method.getName());
    }

    @Test
    public void testMethodsThatHaveWithWildcard() throws Exception {
        ReflectionSearch search = new ReflectionSearch(ReflectionSearch.class);
        search.methodsThatHave("get*");
        List<Method> methods = search.getMethods();
        assertNotNull(methods);
        assertEquals(2, methods.size());
        List<String> methodNames = Arrays.asList("getAnnotation", "getAnnotations");
        for (int i = 0; i < methods.size(); ++i) {
            String methodName = methods.get(i).getName();
            assertTrue(methodNames.contains(methodName));
        }
    }

    @Test
    public void testMethodsThatDontHaveWithWildcard() throws Exception {
        ReflectionSearch search = new ReflectionSearch(ReflectionSearch.class);
        search.methodsThatDontHave("get*");
        List<Method> methods = search.getMethods();
        assertNotNull(methods);
        assertEquals(9, methods.size());
        List<String> methodNames = Arrays.asList("clazz", "query", "hasAnnotation", "hasWildcard", "methodsThatHave",
                "methodsThatDontHave", "and", "or", "getMethods");
        for (int i = 0; i < methods.size(); ++i) {
            String methodName = methods.get(i).getName();
            assertFalse(methodNames.contains(methodName));
        }
    }

    @Test
    public void testAndOperator() throws Exception {
        ReflectionSearch search = new ReflectionSearch(ReflectionSearch.class);
        search.methodsThatHave("getAnnotations").and();
        search.methodsThatHave("isAnnotationPresent");
        List<Method> methods = search.getMethods();
        assertNotNull(methods);
        assertEquals(1, methods.size());
        Method method = methods.get(0);
        assertEquals("isAnnotationPresent", method.getName());
    }

    @Test
    public void testOrOperator() throws Exception {
        ReflectionSearch search = new ReflectionSearch(ReflectionSearch.class);
        search.methodsThatHave("getAnnotation").or();
        search.methodsThatHave("getAnnotations");
        List<Method> methods = search.getMethods();
        assertNotNull(methods);
        assertEquals(2, methods.size());
        List<String> methodNames = Arrays.asList("getAnnotation", "getAnnotations");
        for (int i = 0; i < methods.size(); ++i) {
            String methodName = methods.get(i).getName();
            assertTrue(methodNames.contains(methodName));
        }
    }

}