package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.beanbin.reflect.query.*;
import net.sourceforge.beanbin.reflect.ReflectionCriteria;
import net.sourceforge.beanbin.reflect.ReflectionShelf;
import org.junit.Before;
import org.junit.Test;

public class ReflectionSearchTest {

    private ReflectionSearch search;
    private Class testClass;
    private List<Method> methods;
    private List<Method> emptyList;

    @Before
    public void setup() {
        testClass = TestClassWithMethods.class;
        search = new ReflectionSearch(testClass);
        methods = Arrays.asList(testClass.getMethods());
        emptyList = Collections.emptyList();
    }

    @Test
    public void testHasAnnotationWithExactMatch() {
        assertTrue(search.hasAnnotation("AnnotationType1"));
    }

    @Test
    public void testHasAnnotationWithClassNameMatch() {
        assertTrue(search.hasAnnotation("AnnotationType1$AnnotationType2"));
    }

    @Test
    public void testHasAnnotationWithWildcardMatch() {
        assertTrue(search.hasAnnotation("AnnotationType1*"));
        assertTrue(search.hasAnnotation("AnnotationType1.*"));
    }

    @Test
    public void testHasAnnotationWithNoMatch() {
        assertFalse(search.hasAnnotation("NonExistentAnnotation"));
    }

    @Test
    public void testMethodsThatHaveWithExactMatch() {
        search.methodsThatHave("testMethod1");
        List<Method> result = search.getMethods();
        assertEquals(1, result.size());
        assertEquals(TestClassWithMethods.class.getMethod("testMethod1"), result.get(0));
    }

    @Test
    public void testMethodsThatHaveWithWildcardMatch() {
        search.methodsThatHave("testMethod*");
        List<Method> result = search.getMethods();
        assertEquals(2, result.size());
        assertNotSame(TestClassWithMethods.class.getMethod("testMethod1"), result.get(1));
    }

    @Test
    public void testMethodsThatDontHaveWithExactMatch() {
        search.methodsThatDontHave("testMethod1");
        List<Method> result = search.getMethods();
        assertEquals(methods.size() - 1, result.size());
        assertFalse(result.contains(TestClassWithMethods.class.getMethod("testMethod1")));
    }

    @Test
    public void testMethodsThatDontHaveWithEmptyList() {
        search.methodsThatDontHave("");
        List<Method> result = search.getMethods();
        assertEquals(methods.size(), result.size());
        assertTrue(result.containsAll(methods));
    }

    @Test
    public void testAnd() {
        search.methodsThatHave("testMethod1");
        search.and();
        search.methodsThatDontHave("testMethod2");
        List<Method> result = search.getMethods();
        assertEquals(0, result.size());
    }

    @Test
    public void testOr() {
        search.methodsThatHave("testMethod1");
        search.or();
        search.methodsThatDontHave("testMethod2");
        List<Method> result = search.getMethods();
        assertEquals(methods.size(), result.size());
        assertTrue(result.containsAll(methods));
    }

    @Test(expected = BeanBinException.class)
    public void testGetMethodsWithEmptyQuery() {
        ReflectionSearch emptySearch = new ReflectionSearch(testClass);
        emptySearch.getMethods();
    }

    @Test(expected = BeanBinException.class)
    public void testGetMethodsWithNullQuery() {
        ReflectionSearch nullSearch = new ReflectionSearch(testClass);
        nullSearch.query = null;
        nullSearch.getMethods();
    }

    @Test
    public void testGetMethodsWithSimpleQuery() {
        List<Method> result = search.getMethods();
        assertEquals(methods.size(), result.size());
        assertTrue(result.containsAll(methods));
    }

    @Test
    public void testGetMethodsWithComplexQuery() {
        search.methodsThatHave("testMethod*");
        search.and();
        search.methodsThatHave("set*");
        List<Method> result = search.getMethods();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetMethodsWithEmptyResultList() {
        search.query.setResults(emptyList);
        List<Method> result = search.getMethods();
        assertEquals(0, result.size());
    }
}

class TestClassWithMethods {
    public void testMethod1() {
        // empty method body
    }

    public void testMethod2() {
        // empty method body
    }

    @AnnotationType1
    public void testMethod3() {
        // empty method body
    }
}

class AnnotationType1 {
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AnnotationType2 {
        String value() default "";
    }
}