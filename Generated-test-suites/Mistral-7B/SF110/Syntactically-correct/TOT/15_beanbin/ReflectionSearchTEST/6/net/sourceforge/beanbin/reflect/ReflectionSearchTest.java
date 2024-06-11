package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;

import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.search.*;
import net.sourceforge.beanbin.query.*;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() throws Exception {
        ReflectionSearch reflectionSearch = new ReflectionSearch(YourClassWithAnnotation.class);
        // Test when annotation is present
        assertTrue(reflectionSearch.hasAnnotation("YourAnnotationName"));
        // Test when annotation is not present
        ReflectionSearch reflectionSearchForEmptyClass = new ReflectionSearch(EmptyClass.class);
        assertFalse(reflectionSearchForEmptyClass.hasAnnotation("YourAnnotationName"));
    }

    @Test
    public void testHasWildcard() {
        assertTrue(reflectionSearch.hasWildcard("YourAnnotationName*"));
        assertFalse(reflectionSearch.hasWildcard("YourAnnotationName"));
    }

    @Test
    public void testMethodsThatHave() throws Exception {
        ReflectionSearch reflectionSearch = new ReflectionSearch(YourClassWithMethods.class);
        reflectionSearch.methodsThatHave("YourMethodName");
        List<Method> methods = reflectionSearch.getMethods();
        assertTrue(methods.size() > 0);
        assertTrue(methods.get(0).getName().equals("yourMethodName"));
    }

    @Test
    public void testMethodsThatDontHave() throws Exception {
        ReflectionSearch reflectionSearch = new ReflectionSearch(YourClassWithMethods.class);
        reflectionSearch.methodsThatDontHave("YourMethodName");
        List<Method> methods = reflectionSearch.getMethods();
        assertTrue(methods.size() == 0);
    }

    @Test
    public void testAnd() throws Exception {
        ReflectionSearch reflectionSearch = new ReflectionSearch(YourClassWithMethods.class);
        reflectionSearch.methodsThatHave("YourMethodNamePrefix");
        reflectionSearch.and();
        reflectionSearch.methodsThatDontHave("YourMethodNameSuffix");
        List<Method> methods = reflectionSearch.getMethods();
        // The number of methods in the output depends on the class you're testing
        // Make sure to adjust the following assertions accordingly
        assertTrue(methods.size() == 1 || methods.size() == 0);
        if (methods.size() > 0) {
            String methodName = methods.get(0).getName();
            String[] methodNameParts = methodName.split("(?=([A-Z][^A-Z]*)(?=[A-Z]))");
            assertTrue(Arrays.asList(methodNameParts).get(0).equals("YourMethodNamePrefix"));
            assertTrue(Arrays.asList(methodNameParts).get(methodNameParts.length - 1).equals("YourMethodNameSuffix"));
        }
    }

    @Test
    public void testOr() throws Exception {
        ReflectionSearch reflectionSearch = new ReflectionSearch(YourClassWithMethods.class);
        reflectionSearch.methodsThatHave("YourMethodNamePrefix1");
        reflectionSearch.or();
        reflectionSearch.methodsThatHave("YourMethodNamePrefix2");
        List<Method> methods = reflectionSearch.getMethods();
        assertTrue(methods.size() > 0);
        for (Method method : methods) {
            String methodName = method.getName();
            String[] methodNameParts = methodName.split("(?=([A-Z][^A-Z]*)(?=[A-Z]))");
            assertTrue(Arrays.asList(methodNameParts).get(0).equals("YourMethodNamePrefix1") || Arrays.asList(methodNameParts).get(0).equals("YourMethodNamePrefix2"));
        }
    }

    class YourClassWithMethods {
        public void yourMethod1() {
        }

        public void yourMethod2() {
        }

        public void yourMethodWithoutPrefix1() {
        }

        public void yourMethodWithoutPrefix2() {
        }

        public void yourMethodWithPrefix1AndSuffix1() {
        }

        public void yourMethodWithPrefix1AndSuffix2() {
        }

        public void yourMethodWithPrefix2AndSuffix1() {
        }
    }

    class YourClassWithAnnotation {
        @YourAnnotationName
        public void yourMethod() {
        }
    }

    class EmptyClass {
    }

    class YourClassWithMethodNamePrefix {
        public void yourPrefixMethod1() {
        }

        public void yourPrefixMethod2() {
        }
    }

    class YourClassWithMethodNameSuffix {
        public void yourMethodSuffix1() {
        }

        public void yourMethodSuffix2() {
        }
    }
}