package net.sourceforge.beanbin.reflect;

import net.sourceforge.beanbin.search.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.lang.reflect.*;

public class ReflectionSearchTest {
    @Test
    public void testHasAnnotation() {
        Class testClass = MyAnnotation.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);
        String annotationName = MyAnnotation.class.getSimpleName();
        assertTrue(reflectionSearch.hasAnnotation(annotationName));

        Class otherClass = AnotherClass.class;
        ReflectionSearch reflectionSearchOther = new ReflectionSearch(otherClass);
        assertFalse(reflectionSearchOther.hasAnnotation(annotationName));

        Class anotherTestClass = MyAnnotation2.class;
        ReflectionSearch reflectionSearchAnother = new ReflectionSearch(anotherTestClass);
        String term2 = "Annotation2";
        assertTrue(reflectionSearchAnother.hasAnnotation(term2));
    }

    @Test
    public void testHasAnnotationWithWildcard() {
        Class testClass = MyAnnotation.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);
        String term = "Annotation*";
        assertTrue(reflectionSearch.hasAnnotation(term));

        Class otherClass = AnotherClassWithAnnotation.class;
        ReflectionSearch reflectionSearchOther = new ReflectionSearch(otherClass);
        assertFalse(reflectionSearchOther.hasAnnotation(term));

        Class anotherTestClass = MyAnnotation2.class;
        ReflectionSearch reflectionSearchAnother = new ReflectionSearch(anotherTestClass);
        String term2 = "Annotation2";
        assertTrue(reflectionSearchAnother.hasAnnotation(term2));
    }

    @Test(expected = BeanBinException.class)
    public void testHasAnnotationWithException() throws BeanBinException {
        Class testClass = ReflectionSearch.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);
        reflectionSearch.methodsThatHave("doesnotexist");
        reflectionSearch.getMethods();
    }

    @Test
    public void testMethodsThatHave() throws Exception {
        Class testClass = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);
        String term = "test";
        reflectionSearch.methodsThatHave(term);
        List<Method> methods = reflectionSearch.getMethods();

        Method method1 = testClass.getMethod("testMethod1");
        Method method2 = testClass.getMethod("testMethod2");
        Method method3 = testClass.getMethod("testMethod3");

        assertTrue(methods.contains(method1));
        assertTrue(methods.contains(method2));
        assertFalse(methods.contains(method3));
    }

    @Test
    public void testMethodsThatDontHave() throws Exception {
        Class testClass = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);
        String term = "test";
        reflectionSearch.methodsThatDontHave(term);
        List<Method> methods = reflectionSearch.getMethods();

        Method method1 = testClass.getMethod("testMethod1");
        Method method2 = testClass.getMethod("testMethod2");
        Method method3 = testClass.getMethod("testMethod3");

        assertFalse(methods.contains(method1));
        assertFalse(methods.contains(method2));
        assertTrue(methods.contains(method3));
    }

    @Test
    public void testMethodsThatHaveAnd() throws Exception {
        Class testClass = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);
        String term1 = "test";
        reflectionSearch.methodsThatHave(term1);
        reflectionSearch.and();
        String term2 = "get";
        reflectionSearch.methodsThatHave(term2);
        List<Method> methods = reflectionSearch.getMethods();

        Method method1 = testClass.getMethod("testMethod1");
        Method method2 = testClass.getMethod("testGetMethod1");
        Method method3 = testClass.getMethod("testMethod3");

        assertFalse(methods.contains(method1));
        assertTrue(methods.contains(method2));
        assertFalse(methods.contains(method3));
    }

    @Test
    public void testMethodsThatHaveOr() throws Exception {
        Class testClass = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);
        String term1 = "test";
        reflectionSearch.methodsThatHave(term1);
        reflectionSearch.or();
        String term2 = "set";
        reflectionSearch.methodsThatHave(term2);
        List<Method> methods = reflectionSearch.getMethods();

        Method method1 = testClass.getMethod("testMethod1");
        Method method2 = testClass.getMethod("testSetMethod1");
        Method method3 = testClass.getMethod("testMethod3");

        assertFalse(methods.contains(method1));
        assertTrue(methods.contains(method2));
        assertTrue(methods.contains(method3));
    }
}