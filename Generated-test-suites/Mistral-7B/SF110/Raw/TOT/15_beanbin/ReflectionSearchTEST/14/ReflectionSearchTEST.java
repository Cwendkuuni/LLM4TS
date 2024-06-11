package net.sourceforge.beanbin.reflect;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.AnnotatedClassLoader;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(PowerMockRunner.class)
public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() throws Exception {
        Class clazz = new AnnotatedClassLoader().loadClass("net.sourceforge.beanbin.reflect.TestAnnotation");
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        // Test when annotation is present
        Assert.assertTrue(reflectionSearch.hasAnnotation("TestAnnotation"));

        // Test when annotation is present in class name
        Assert.assertTrue(reflectionSearch.hasAnnotation("TestAnnotation.*"));

        // Test when annotation is present in class name with wildcard on the start
        Assert.assertTrue(reflectionSearch.hasAnnotation("*.TestAnnotation"));

        // Test when annotation is not present
        Class clazzWithoutAnnotation = new AnnotatedClassLoader().loadClass("net.sourceforge.beanbin.reflect.NoAnnotation");
        ReflectionSearch reflectionSearchWithoutAnnotation = new ReflectionSearch(clazzWithoutAnnotation);
        Assert.assertFalse(reflectionSearchWithoutAnnotation.hasAnnotation("TestAnnotation"));

        // Test when term is empty
        ReflectionSearch reflectionSearchEmpty = new ReflectionSearch(clazz);
        Assert.assertFalse(reflectionSearchEmpty.hasAnnotation(""));
    }

    @Test
    public void testMethodsThatHave() throws Exception {
        Class clazz = new AnnotatedClassLoader().loadClass("net.sourceforge.beanbin.reflect.ReflectionSearch");
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        List<Method> expectedMethods = Arrays.asList(ReflectionSearch.class.getMethods());

        reflectionSearch.methodsThatHave("getMethods");
        List<Method> methods = reflectionSearch.getMethods();
        Assert.assertEquals(expectedMethods.size(), methods.size());
        Collections.sort(expectedMethods);
        Collections.sort(methods);
        for (int i = 0; i < methods.size(); i++) {
            Assert.assertEquals(expectedMethods.get(i), methods.get(i));
        }

        reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch.methodsThatHave("doesNotExist");
        methods = reflectionSearch.getMethods();
        Assert.assertEquals(0, methods.size());
    }

    @Test
    public void testMethodsThatDontHave() throws Exception {
        Class clazz = new AnnotatedClassLoader().loadClass("net.sourceforge.beanbin.reflect.ReflectionSearch");
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        List<Method> allMethods = Arrays.asList(ReflectionSearch.class.getMethods());
        reflectionSearch.methodsThatDontHave("getMethods");
        List<Method> methods = reflectionSearch.getMethods();
        Assert.assertEquals(allMethods.size() - ReflectionSearch.class.getMethodCount(), methods.size());

        reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch.methodsThatDontHave("getAnnotations");
        methods = reflectionSearch.getMethods();
        Assert.assertEquals(ReflectionSearch.class.getMethodCount(), methods.size());
    }

    @Test
    public void testAndCondition() throws Exception {
        Class clazz = new AnnotatedClassLoader().loadClass("net.sourceforge.beanbin.reflect.ReflectionSearch");
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        reflectionSearch.methodsThatHave("getMethods");
        reflectionSearch.and();
        reflectionSearch.methodsThatDontHave("hasAnnotation");

        List<Method> methods = reflectionSearch.getMethods();
        Assert.assertEquals(0, methods.size());
    }

    @Test
    public void testOrCondition() throws Exception {
        Class clazz = new AnnotatedClassLoader().loadClass("net.sourceforge.beanbin.reflect.ReflectionSearch");
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

        reflectionSearch.methodsThatHave("getMethods");
        reflectionSearch.or();
        reflectionSearch.methodsThatHave("hasAnnotation");

        List<Method> methods = reflectionSearch.getMethods();
        Assert.assertEquals(ReflectionSearch.class.getMethodCount(), methods.size());
    }
}