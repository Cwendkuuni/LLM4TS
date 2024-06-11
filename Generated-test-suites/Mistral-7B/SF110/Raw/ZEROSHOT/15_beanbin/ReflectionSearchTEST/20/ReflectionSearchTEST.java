package net.sourceforge.beanbin.reflect.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.reflect.query.Criteria;
import net.sourceforge.beanbin.reflect.query.MethodReflectionCriteria;
import net.sourceforge.beanbin.reflect.query.ReflectionCriteria;
import net.sourceforge.beanbin.reflect.query.SearchType;

import org.junit.Test;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() throws Exception {
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        assertFalse(reflectionSearch.hasAnnotation("Test"));
        assertFalse(reflectionSearch.hasAnnotation("AnnotatedInterface"));
        assertTrue(reflectionSearch.hasAnnotation("Annotation"));
        assertTrue(reflectionSearch.hasAnnotation("java.lang.annotation.Annotation"));
        Class<Annotation> annotationType = Annotation.class;
        ReflectionSearch reflectionSearchAnnotation = new ReflectionSearch(annotationType);
        assertTrue(reflectionSearchAnnotation.hasAnnotation("Annotation"));
        assertFalse(reflectionSearchAnnotation.hasAnnotation("Test"));
        assertTrue(reflectionSearchAnnotation.hasAnnotation("java.lang.annotation.Annotation"));
        Class<String> stringClass = String.class;
        ReflectionSearch reflectionSearchString = new ReflectionSearch(stringClass);
        String term = "String";
        assertTrue(reflectionSearchString.hasAnnotation(term));
        term = "string";
        assertTrue(reflectionSearchString.hasAnnotation(term));
        term = "*String";
        assertTrue(reflectionSearchString.hasAnnotation(term));
        term = "*string";
        assertTrue(reflectionSearchString.hasAnnotation(term));
    }

    @Test
    public void testMethodsThatHave() throws Exception {
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch.methodsThatHave("equals");
        List<Method> methods = reflectionSearch.getMethods();
        Method equals = String.class.getMethod("equals", Object.class);
        assertTrue(methods.contains(equals));
        reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch.methodsThatHave("toString");
        methods = reflectionSearch.getMethods();
        Method toString = String.class.getMethod("toString", null);
        assertTrue(methods.contains(toString));
        reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch.methodsThatHave("hashCode");
        methods = reflectionSearch.getMethods();
        Method hashCode = String.class.getMethod("hashCode", null);
        assertTrue(methods.contains(hashCode));
        reflectionSearch = new ReflectionSearch(String.class);
        List<Method> methodsWithoutHashCode = new ArrayList<>();
        methodsWithoutHashCode.add(String.class.getMethod("length", null));
        reflectionSearch.methodsThatHave("hashCode").and().methodsThatDontHave("hashCode").getMethods();
        List<Method> methodsWithLength = reflectionSearch.getMethods();
        List<Method> expectedMethods = new ArrayList<>();
        expectedMethods.add(String.class.getMethod("length", null));
        assertTrue(methodsWithLength.equals(expectedMethods));
    }

    @Test(expected = BeanBinException.class)
    public void testMethodsThatHaveWithException() throws Exception {
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch.methodsThatHave("nonExistentMethod");
        reflectionSearch.getMethods();
    }

    @Test
    public void testMethodsThatDontHave() throws Exception {
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch.methodsThatDontHave("equals");
        List<Method> methods = reflectionSearch.getMethods();
        Method equals = String.class.getMethod("equals", Object.class);
        assertNull(methods.get(0));
        reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch.methodsThatDontHave("toString");
        methods = reflectionSearch.getMethods();
        Method toString = String.class.getMethod("toString", null);
        assertNull(methods.get(0));
        reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch.methodsThatDontHave("hashCode");
        methods = reflectionSearch.getMethods();
        Method hashCode = String.class.getMethod("hashCode", null);
        assertNull(methods.get(0));
        reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch.methodsThatHave("equals").methodsThatDontHave("equals");
        methods = reflectionSearch.getMethods();
        assertNull(methods.get(0));
    }

    @Test
    public void testAnd() throws Exception {
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch.methodsThatHave("equals").and().methodsThatHave("toString");
        List<Method> methods = reflectionSearch.getMethods();
        Method equals = String.class.getMethod("equals", Object.class);
        Method toString = String.class.getMethod("toString", null);
        List<Method> expectedMethods = new ArrayList<>();
        expectedMethods.add(equals);
        expectedMethods.add(toString);
        assertTrue(methods.equals(expectedMethods));
    }

    @Test
    public void testOr() throws Exception {
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch.methodsThatHave("equals").or().methodsThatHave("toString");
        List<Method> methods = reflectionSearch.getMethods();
        Method equals = String.class.getMethod("equals", Object.class);
        Method toString = String.class.getMethod("toString", null);
        List<Method> expectedMethods = new ArrayList<>();
        expectedMethods.add(equals);
        expectedMethods.add(toString);
        List<Method> methodsWithoutEquals = new ArrayList<>();
        methodsWithoutEquals.add(String.class.getMethod("length", null));
        reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch.methodsThatHave("equals").or().methodsThatDontHave("equals");
        methods = reflectionSearch.getMethods();
        List<Method> expectedMethodsWithoutEquals = new ArrayList<>();
        expectedMethodsWithoutEquals.addAll(Arrays.asList(String.class.getDeclaredMethods()));
        expectedMethodsWithoutEquals.remove(String.class.getMethod("equals", Object.class));
        assertTrue(methods.equals(expectedMethodsWithoutEquals));
    }

    @Test
    public void testQueryWithCriteria() throws Exception {
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        List<Criteria> criteriaList = new ArrayList<>();
        criteriaList.add(new MethodReflectionCriteria(String.class, "equals", SearchType.EQUALS));
        criteriaList.add(new MethodReflectionCriteria(String.class, "toString", SearchType.DOESNOTEQUAL));
        reflectionSearch.query.addAll(criteriaList);
        List<Method> methods = reflectionSearch.getMethods();
        Method equals = String.class.getMethod("equals", Object.class);
        assertFalse(methods.contains(equals));
        Method toString = String.class.getMethod("toString", null);
        assertNull(methods.get(0));
        Criteria equalsCriteria = new MethodReflectionCriteria(String.class, "equals", SearchType.EQUALS);
        reflectionSearch.query.remove(equalsCriteria);
        methods = reflectionSearch.getMethods();
        assertTrue(methods.isEmpty());
    }
}