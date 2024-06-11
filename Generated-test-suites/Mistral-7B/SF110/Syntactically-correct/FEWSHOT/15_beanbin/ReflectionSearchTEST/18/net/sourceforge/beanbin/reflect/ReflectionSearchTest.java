package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.beanbin.query.Criteria;
import net.sourceforge.beanbin.query.Conditional;
import net.sourceforge.beanbin.search.MethodReflectionCriteria;
import net.sourceforge.beanbin.search.ReflectionCriteria;
import net.sourceforge.beanbin.search.ReflectionShelf;

import org.junit.Test;

public class ReflectionSearchTest {

    @Test
    public void testReflectionSearchHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        assertFalse(reflectionSearch.hasAnnotation("AnnotationA"));
        String annotatedClass = MyAnnotatedClass.class;
        reflectionSearch = new ReflectionSearch(annotatedClass);
        assertTrue(reflectionSearch.hasAnnotation("AnnotationA"));
    }

    @Test
    public void testReflectionSearchHasWildcard() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        assertFalse(reflectionSearch.hasWildcard("AnnotationA"));
        ReflectionSearch reflectionSearchWithWildcard = new ReflectionSearch(String.class);
        reflectionSearchWithWildcard.methodsThatHave("Annotation*");
        assertTrue(reflectionSearchWithWildcard.hasWildcard("AnnotationA"));
        assertFalse(reflectionSearchWithWildcard.hasWildcard("AnnotationB"));
        assertTrue(reflectionSearchWithWildcard.hasWildcard("AnnotationC"));
    }

    @Test
    public void testReflectionSearchMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
        assertFalse(methods.isEmpty());

        reflectionSearch.methodsThatHave("equals");
        methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        Method firstMethod = methods.get(0);
        assertEquals("equals", firstMethod.getName());

        reflectionSearch.methodsThatHave("equals");
        methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertSame(firstMethod, methods.get(0));

        reflectionSearch.methodsThatHave("toString");
        methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        Method toStringMethod = methods.get(0);
        assertEquals("toString", toStringMethod.getName());

        reflectionSearch.methodsThatHave("equals");
        methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
        Method equalsMethod = methods.get(0);
        Method toStringMethod2 = methods.get(1);
        assertSame(equalsMethod, methods.get(0));
        assertSame(toStringMethod, methods.get(1));
    }

    @Test
    public void testReflectionSearchMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
        assertFalse(methods.isEmpty());

        reflectionSearch.methodsThatDontHave("equals");
        methods = reflectionSearch.getMethods();
        assertEquals(0, methods.size());

        reflectionSearch.methodsThatHave("equals");
        methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());

        reflectionSearch.methodsThatDontHave("equals");
        methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        Method noEqualsMethod = methods.get(0);
        assertNotEquals("equals", noEqualsMethod.getName());
    }

    @Test
    public void testReflectionSearchAnd() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch.methodsThatHave("equals");
        reflectionSearch.and();
        reflectionSearch.methodsThatHave("toString");

        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(0, methods.size());

        reflectionSearch.methodsThatHave("equals");
        methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());

        reflectionSearch.methodsThatHave("equals");
        reflectionSearch.and();
        reflectionSearch.methodsThatHave("toString");

        methods = reflectionSearch.getMethods();
        assertEquals(0, methods.size());

        reflectionSearch.methodsThatHave("equals");
        reflectionSearch.and();
        reflectionSearch.methodsThatDontHave("toString");

        methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        Method noToStringMethod = methods.get(0);
        assertNotEquals("toString", noToStringMethod.getName());

        reflectionSearch.methodsThatHave("equals");
        reflectionSearch.and();
        reflectionSearch.methodsThatHave("toString");
        reflectionSearch.and();
        reflectionSearch.methodsThatDontHave("toString");

        methods = reflectionSearch.getMethods();
        assertEquals(0, methods.size());
    }

    @Test
    public void testReflectionSearchOr() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch.methodsThatHave("equals");
        reflectionSearch.or();
        reflectionSearch.methodsThatHave("toString");

        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
        Method equalsMethod = methods.get(0);
        Method toStringMethod = methods.get(1);
        assertNotSame(equalsMethod, toStringMethod);
        assertEquals("equals", equalsMethod.getName());
        assertEquals("toString", toStringMethod.getName());

        reflectionSearch.methodsThatHave("equals");
        reflectionSearch.or();
        reflectionSearch.methodsThatHave("toString");
        reflectionSearch.and();
        reflectionSearch.methodsThatHave("equals");

        methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
        Method firstEqualsMethod = methods.get(0);
        Method secondEqualsMethod = methods.get(1);
        assertNotSame(firstEqualsMethod, secondEqualsMethod);
        assertEquals("equals", firstEqualsMethod.getName());
        assertEquals("equals", secondEqualsMethod.getName());
    }

    @Test
    public void testReflectionSearchGetMethods() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
        assertFalse(methods.isEmpty());

        methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
        Method equalsMethod = methods.get(0);
        Method toStringMethod = methods.get(1);
        assertNotSame(equalsMethod, toStringMethod);
        assertEquals("equals", equalsMethod.getName());
        assertEquals("toString", toStringMethod.getName());

        reflectionSearch.methodsThatHave("equals");
        reflectionSearch.getMethods();
        methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        Method equalsMethod2 = methods.get(0);
        assertSame(equalsMethod, equalsMethod2);

        reflectionSearch.methodsThatHave("toString");
        reflectionSearch.getMethods();
        methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        Method toStringMethod2 = methods.get(0);
        assertSame(toStringMethod, toStringMethod2);

        reflectionSearch.methodsThatHave("equals");
        reflectionSearch.methodsThatHave("toString");
        reflectionSearch.getMethods();
        methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
        Method firstMethod = methods.get(0);
        Method secondMethod = methods.get(1);
        assertNotSame(firstMethod, secondMethod);
        assertSame(equalsMethod, firstMethod);
        assertSame(toStringMethod, secondMethod);

        reflectionSearch.methodsThatHave("equals");
        reflectionSearch.methodsThatDontHave("toString");
        reflectionSearch.getMethods();
        methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        Method noToStringMethod = methods.get(0);
        assertNotSame(equalsMethod, noToStringMethod);
        assertNotSame(toStringMethod, noToStringMethod);
        assertNotSame(firstMethod, noToStringMethod);
        assertNotSame(secondMethod, noToStringMethod);
    }

    @Test
    public void testReflectionSearchQuery() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        Criteria criteria = new MethodReflectionCriteria(String.class, "equals", SearchType.EQUALS);
        reflectionSearch.query.add(criteria);

        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        Method equalsMethod = methods.get(0);
        assertSame(equalsMethod, reflectionSearch.query.getCriterias().get(0).getResults().get(0));

        reflectionSearch.query.add(new MethodReflectionCriteria(String.class, "toString", SearchType.EQUALS));
        methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
        Method firstMethod = methods.get(0);
        Method secondMethod = methods.get(1);
        assertNotSame(firstMethod, secondMethod);
        assertSame(equalsMethod, firstMethod);
        assertSame(toStringMethod, secondMethod);
    }

    @Test
    public void testReflectionSearchEmptyQuery() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
        Method equalsMethod = methods.get(0);
        Method toStringMethod = methods.get(1);
        assertNotSame(equalsMethod, toStringMethod);
        assertEquals("equals", equalsMethod.getName());
        assertEquals("toString", toStringMethod.getName());

        reflectionSearch.query.clear();

        methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
        assertSame(equalsMethod, methods.get(0));
        assertSame(toStringMethod, methods.get(1));
    }

    @Test(expected = BeanBinException.class)
    public void testReflectionSearchException() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(String[].class);
    }

    @Test
    public void testReflectionSearchShelf() {
        ReflectionSearch reflectionSearch1 = new ReflectionSearch(String.class);
        ReflectionSearch reflectionSearch2 = new ReflectionSearch(String.class);

        reflectionSearch1.methodsThatHave("equals");
        List<Method> methods1 = reflectionSearch1.getMethods();
        assertEquals(1, methods1.size());
        Method equalsMethod1 = methods1.get(0);
        reflectionSearch1.query.put("key1", methods1);

        reflectionSearch2.methodsThatHave("toString");
        List<Method> methods2 = reflectionSearch2.getMethods();
        assertEquals(1, methods2.size());
        Method toStringMethod2 = methods2.get(0);
        reflectionSearch2.query.put("key2", methods2);

        ReflectionShelf reflectionShelf = ReflectionShelf.getInstance();
        List<Method> methodsFromShelf1 = (List<Method>) reflectionShelf.get("key1");
        List<Method> methodsFromShelf2 = (List<Method>) reflectionShelf.get("key2");

        assertEquals(1, methodsFromShelf1.size());
        assertSame(equalsMethod1, methodsFromShelf1.get(0));
        assertEquals(1, methodsFromShelf2.size());
        assertSame(toStringMethod2, methodsFromShelf2.get(0));
    }

    private static class MyAnnotatedClass {
        @AnnotationA
        public void foo() {
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    private @interface AnnotationA {
    }
}