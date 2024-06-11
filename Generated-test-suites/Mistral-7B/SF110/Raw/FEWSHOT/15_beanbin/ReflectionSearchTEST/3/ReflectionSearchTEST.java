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

import org.junit.Test;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);

        // Test with annotation match
        Class<MyAnnotation> annotationClass = MyAnnotation.class;
        MyAnnotation annotation = annotationClass.getAnnotation(MyAnnotation.class);
        reflectionSearch.clazz = annotationClass;
        assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));

        // Test with class name match
        reflectionSearch.clazz = Object[].class;
        assertTrue(reflectionSearch.hasAnnotation("Object"));

        // Test with wildcard match
        reflectionSearch.clazz = String[].class;
        assertTrue(reflectionSearch.hasAnnotation("String*"));

        // Test with no match
        reflectionSearch.clazz = ReflectionSearch.class;
        assertFalse(reflectionSearch.hasAnnotation("InvalidAnnotation"));
        assertFalse(reflectionSearch.hasAnnotation("ReflectionSearch*"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);

        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(0, methods.size());

        reflectionSearch.methodsThatHave("equals");
        methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertEquals(Object.class.equalsMethod(), methods.get(0));

        reflectionSearch.methodsThatHave("equals*");
        methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertEquals(Object.class.equalsMethod(), methods.get(0));

        reflectionSearch.methodsThatHave("hashCode");
        methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertEquals(Object.class.hashCodeMethod(), methods.get(0));
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);

        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());

        reflectionSearch.methodsThatDontHave("equals");
        methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertNull(methods.get(0));

        reflectionSearch.methodsThatDontHave("equals*");
        methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertNull(methods.get(0));

        reflectionSearch.methodsThatDontHave("hashCode");
        methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
        assertEquals(Object.class.equalsMethod(), methods.get(0));
        assertEquals(Object.class.hashCodeMethod(), methods.get(1));
    }

    @Test
    public void testAnd() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);

        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());

        reflectionSearch.methodsThatHave("equals");
        methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertEquals(Object.class.equalsMethod(), methods.get(0));

        reflectionSearch.and();
        methods = reflectionSearch.getMethods();
        assertEquals(0, methods.size());

        reflectionSearch.methodsThatHave("equals");
        reflectionSearch.and();
        methods = reflectionSearch.getMethods();
        assertEquals(0, methods.size());

        reflectionSearch.methodsThatHave("equals");
        methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertEquals(Object.class.equalsMethod(), methods.get(0));
    }

    @Test
    public void testOr() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);

        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());

        reflectionSearch.methodsThatHave("equals");
        methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertEquals(Object.class.equalsMethod(), methods.get(0));

        reflectionSearch.or();
        methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
        assertEquals(Object.class.equalsMethod(), methods.get(0));
        assertEquals(Object.class.hashCodeMethod(), methods.get(1));

        reflectionSearch.or();
        methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
        assertEquals(Object.class.equalsMethod(), methods.get(0));
        assertEquals(Object.class.hashCodeMethod(), methods.get(1));
    }
}