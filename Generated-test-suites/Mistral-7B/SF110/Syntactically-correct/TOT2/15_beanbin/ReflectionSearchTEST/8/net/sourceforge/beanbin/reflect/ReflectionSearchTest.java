package net.sourceforge.beanbin.reflect;

import org.junit.Test;
import org.junit.Assert;
import java.lang.reflect.Method;
import static org.junit.Assert.*;

public class ReflectionSearchTest {
// ... (Previously defined test cases)

    @Test
    public void testMethodsThatHave_ReturnsCorrectMethods() throws Exception {
        Class clazz = SomeClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        String term = "someMethod";

        List<Method> expectedMethods = Arrays.asList(clazz.getMethods());
        List<Method> methods = reflectionSearch.methodsThatHave(term).getMethods();
        List<Method> actualMethods = new ArrayList<>();
        for (Method method : methods) {
            actualMethods.add(method);
        }

        assertTrue(methods.size() > 0);
        assertEquals(expectedMethods.size(), methods.size());
        for (int i = 0; i < expectedMethods.size(); i++) {
            Method expectedMethod = expectedMethods.get(i);
            Method actualMethod = actualMethods.get(i);
            assertEquals(expectedMethod.getName(), actualMethod.getName());
            assertEquals(expectedMethod.getParameterTypes(), actualMethod.getParameterTypes());
            assertEquals(expectedMethod.getReturnType(), actualMethod.getReturnType());
        }
    }

    @Test
    public void testMethodsThatDontHave_ReturnsEmptyList_WhenNoMethodsSatisfyQuery() throws Exception {
        Class clazz = SomeClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        String term = "nonexistentMethod";
        List<Method> methods = reflectionSearch.methodsThatDontHave(term).getMethods();
        assertTrue(methods.isEmpty());
    }

    @Test(expected = BeanBinException.class)
    public void testMethodsThatHave_ThrowsException_WhenNoMethodsSatisfyQuery() throws Exception {
        Class clazz = SomeClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        String term = "nonexistentMethod";
        reflectionSearch.methodsThatHave(term).getMethods();
    }

    @Test
    public void testGetMethods_ReturnsCorrectMethods_WhenQueryReturnsMultipleMethods() throws Exception {
        Class clazz = SomeClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        String term = "get";

        List<Method> expectedMethods = Arrays.asList(clazz.getMethods());
        List<Method> methods = reflectionSearch.getMethods().getMethods();
        List<Method> actualMethods = new ArrayList<>();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                actualMethods.add(method);
            }
        }

        assertTrue(methods.size() > 0);
        assertEquals(expectedMethods.size(), methods.size());
        for (int i = 0; i < expectedMethods.size(); i++) {
            Method expectedMethod = expectedMethods.get(i);
            Method actualMethod = actualMethods.get(i);
            assertEquals(expectedMethod.getName(), actualMethod.getName());
            assertEquals(expectedMethod.getParameterTypes(), actualMethod.getParameterTypes());
            assertEquals(expectedMethod.getReturnType(), actualMethod.getReturnType());
        }
    }

    @Test
    public void testGetMethods_ReturnsEmptyList_WhenNoMethodsSatisfyQuery() throws Exception {
        Class clazz = SomeClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        String term = "nonExistentTerm";
        List<Method> methods = reflectionSearch.getMethods().getMethods();
        assertTrue(methods.isEmpty());
    }
}