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

import net.sourceforge.beanbin.query.Criteria;
import net.sourceforge.beanbin.query.Conditional;
import net.sourceforge.beanbin.query.ReflectionCriteria;
import net.sourceforge.beanbin.reflect.ReflectionSearch;

import org.junit.Before;
import org.junit.Test;

public class ReflectionSearchTest {

    private ReflectionSearch reflectionSearch;
    private Class<Object> clazz;

    @Before
    public void setUp() {
        clazz = Object.class;
        reflectionSearch = new ReflectionSearch(clazz);
    }

    @Test
    public void testHasAnnotation_EmptyAnnotationArray() {
        assertFalse(reflectionSearch.hasAnnotation(""));
    }

    @Test
    public void testHasAnnotation_NoAnnotationMatches() {
        assertFalse(reflectionSearch.hasAnnotation("TestAnnotation"));
    }

    @Test
    public void testHasAnnotation_SingleAnnotationMatches() {
        clazz = AnnotationTestClass.class;
        reflectionSearch = new ReflectionSearch(clazz);
        assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));
    }

    @Test
    public void testHasAnnotation_WildcardAnnotationMatches() {
        clazz = AnnotationTestClass.class;
        reflectionSearch = new ReflectionSearch(clazz);
        assertTrue(reflectionSearch.hasAnnotation("An*Annotation"));
    }

    @Test
    public void testMethodsThatHave_NoMethodsMatch() throws BeanBinException {
        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
        assertEquals(0, methods.size());

        reflectionSearch = reflectionSearch.methodsThatHave("NonExistentMethod");
        methods = reflectionSearch.getMethods();
        assertEquals(0, methods.size());
    }

    @Test
    public void testMethodsThatHave_SingleMethodMatch() throws BeanBinException {
        clazz = AnnotationTestClass.class;
        reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch = reflectionSearch.methodsThatHave("testMethod");

        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
        assertEquals(1, methods.size());
        Method method = methods.get(0);
        assertEquals("testMethod", method.getName());
    }

    @Test
    public void testMethodsThatHave_MultipleMethodMatches() throws BeanBinException {
        clazz = AnnotationTestClass.class;
        reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch = reflectionSearch.methodsThatHave("test");

        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
        assertEquals(2, methods.size());
        List<String> methodNames = new ArrayList<>(Arrays.asList("testMethod1", "testMethod2"));
        for (Method method : methods) {
            assertTrue(methodNames.remove(method.getName()));
        }
        assertTrue(methodNames.isEmpty());
    }

    @Test
    public void testMethodsThatDontHave_NoMethodsMatch() throws BeanBinException {
        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
        assertEquals(0, methods.size());

        reflectionSearch = reflectionSearch.methodsThatDontHave("NonExistentMethod");
        methods = reflectionSearch.getMethods();
        assertEquals(0, methods.size());
    }

    @Test
    public void testMethodsThatDontHave_SingleMethodDoesNotMatch() throws BeanBinException {
        clazz = AnnotationTestClass.class;
        reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch = reflectionSearch.methodsThatDontHave("testMethod");

        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
        assertEquals(3, methods.size());
        List<String> methodNames = new ArrayList<>(Arrays.asList("testMethod1", "testMethod2", "testMethod3"));
        for (Method method : methods) {
            assertTrue(methodNames.contains(method.getName()));
        }
    }

    @Test
    public void testMethodsThatDontHave_MultipleMethodsDoNotMatch() throws BeanBinException {
        clazz = AnnotationTestClass.class;
        reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch = reflectionSearch.methodsThatDontHave("testMethod1");

        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
        assertEquals(2, methods.size());
        List<String> methodNames = new ArrayList<>(Arrays.asList("testMethod2", "testMethod3"));
        for (Method method : methods) {
            assertTrue(methodNames.contains(method.getName()));
        }
    }

    @Test
    public void testAnd() throws BeanBinException {
        clazz = AnnotationTestClass.class;
        reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch = reflectionSearch.methodsThatHave("test");
        reflectionSearch = reflectionSearch.and();
        reflectionSearch = reflectionSearch.methodsThatDontHave("testMethod1");

        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
        assertEquals(1, methods.size());
        Method method = methods.get(0);
        assertEquals("testMethod2", method.getName());
    }

    @Test
    public void testOr() throws BeanBinException {
        clazz = AnnotationTestClass.class;
        reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch = reflectionSearch.methodsThatHave("test");
        reflectionSearch = reflectionSearch.or();
        reflectionSearch = reflectionSearch.methodsThatDontHave("testMethod1");
        reflectionSearch = reflectionSearch.methodsThatHave("testMethod3");

        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
        assertEquals(2, methods.size());
        List<String> methodNames = new ArrayList<>(Arrays.asList("testMethod2", "testMethod3"));
        for (Method method : methods) {
            assertTrue(methodNames.contains(method.getName()));
        }
    }
}

class AnnotationTestClass {
    @MyAnnotation
    public void testMethod1() {
    }

    @MyAnnotation
    public void testMethod2() {
    }

    public void testMethod3() {
    }

    @interface MyAnnotation {
    }
}