package net.sourceforge.beanbin.reflect.test;

import org.junit.Assert;
import org.junit.Test;
import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.reflect.annotation.TestAnnotation;
import net.sourceforge.beanbin.reflect.query.BeanBinException;
import net.sourceforge.beanbin.reflect.query.Criteria;
import net.sourceforge.beanbin.reflect.query.ReflectionCriteria;
import net.sourceforge.beanbin.reflect.query.SearchType;
import net.sourceforge.beanbin.reflect.query.WildcardSearch;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        Class clazz = TestAnnotation.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        ReflectionSearchTest test = new ReflectionSearchTest();

        // Check when term matches annotation name
        Assert.assertTrue(reflectionSearch.hasAnnotation("TestAnnotation"));

        // Check when term matches annotation class name
        Assert.assertTrue(reflectionSearch.hasAnnotation("Annotation"));

        // Check when term matches annotation name with wildcard
        Assert.assertTrue(reflectionSearch.hasAnnotation("Annot*"));

        // Check when term does not match annotation name
        Assert.assertFalse(reflectionSearch.hasAnnotation("AnotherAnnotation"));

        // Check when term does not match annotation class name
        Assert.assertFalse(reflectionSearch.hasAnnotation("OtherClass"));

        // Check when there is no annotation
        ReflectionSearch reflectionSearchNoAnnotation = new ReflectionSearch(String.class);
        Assert.assertFalse(reflectionSearchNoAnnotation.hasAnnotation("TestAnnotation"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        Class clazz = TestAnnotation.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        // Test when query returns methods with exact match
        reflectionSearch.methodsThatHave("testMethod");
        List<Method> methods = reflectionSearch.getMethods();
        Method expected = TestAnnotation.class.getMethod("testMethod");
        Assert.assertEquals(Collections.singletonList(expected), methods);

        // Test when query returns methods with wildcard match
        reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch.methodsThatHave("test*");
        methods = reflectionSearch.getMethods();
        List<Method> expectedMethods = Arrays.asList(
                TestAnnotation.class.getMethod("testMethod1"),
                TestAnnotation.class.getMethod("testMethod2")
        );
        Assert.assertEquals(expectedMethods, methods);

        // Test when query returns an empty list
        reflectionSearch = new ReflectionSearch(String.class);
        reflectionSearch.methodsThatHave("nonExistentMethod");
        methods = reflectionSearch.getMethods();
        Assert.assertEquals(Collections.emptyList(), methods);
    }

    @Test(expected = BeanBinException.class)
    public void testMethodsThatDontHaveThrowsException() throws BeanBinException {
        Class clazz = TestAnnotation.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch.methodsThatDontHave("testMethod");
    }

    @Test
    public void testAndOperator() throws BeanBinException {
        Class clazz = TestAnnotation.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        reflectionSearch.methodsThatHave("test");
        reflectionSearch.and();
        reflectionSearch.methodsThatDontHave("Method1");

        List<Method> methods = reflectionSearch.getMethods();
        List<Method> expectedMethods = new ArrayList<>();
        expectedMethods.add(TestAnnotation.class.getMethod("testMethod2"));
        Assert.assertEquals(expectedMethods, methods);
    }

    @Test
    public void testOrOperator() throws BeanBinException {
        Class clazz = TestAnnotation.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        reflectionSearch.methodsThatHave("test");
        reflectionSearch.or();
        reflectionSearch.methodsThatDontHave("Method2");

        List<Method> methods = reflectionSearch.getMethods();
        List<Method> expectedMethods = new ArrayList<>();
        expectedMethods.addAll(Arrays.asList(
                TestAnnotation.class.getMethod("testMethod1"),
                TestAnnotation.class.getMethod("testMethod3")
        ));
        Assert.assertEquals(expectedMethods, methods);
    }

}