
package net.sourceforge.beanbin.reflect;

import org.junit.*;
import static org.junit.Assert.*;

public class ReflectionSearchTest {
    static Class<?> someClassWithAnnotation;
    static Class<?> someClassWithoutAnnotation;
    static Class<?> someClassWithMethods;

    @BeforeClass
    public static void setUp() {
        someClassWithAnnotation = SomeClassWithAnnotation.class;
        someClassWithoutAnnotation = SomeClassWithoutAnnotation.class;
        someClassWithMethods = SomeClassWithMethods.class;
    }

    @Test
    public void testHasAnnotation() {
        // Test annotation match
        ReflectionSearch reflectionSearch = new ReflectionSearch(someClassWithAnnotation);
        assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));

        // Test no annotation match
        reflectionSearch = new ReflectionSearch(someClassWithoutAnnotation);
        assertFalse(reflectionSearch.hasAnnotation("AnyAnnotation"));

        // Test wildcard match
        reflectionSearch = new ReflectionSearch(someClassWithAnnotation);
        assertTrue(reflectionSearch.hasAnnotation("*Annotation"));
        assertTrue(reflectionSearch.hasAnnotation("MyAnnotation*"));

        // Test empty query
        ReflectionSearch emptySearch = new ReflectionSearch(someClassWithoutAnnotation);
        assertFalse(emptySearch.hasAnnotation("AnyAnnotation"));
    }

    @Test
    public void testMethods() throws Exception {
        // Test methods that have the specified term
        ReflectionSearch reflectionSearch = new ReflectionSearch(someClassWithMethods);
        reflectionSearch.methodsThatHave("getFoo");
        List<Method> methodsThatHave = reflectionSearch.getMethods();
        assertNotNull(methodsThatHave);
        assertEquals(1, methodsThatHave.size());
        Method expectedMethod = someClassWithMethods.getMethod("getFoo");
        assertEquals(expectedMethod, methodsThatHave.get(0));

        // Test methods that don't have the specified term
        reflectionSearch = new ReflectionSearch(someClassWithMethods);
        reflectionSearch.methodsThatDontHave("getFoo");
        List<Method> methodsThatDontHave = reflectionSearch.getMethods();
        assertNotNull(methodsThatDontHave);
        assertEquals(0, methodsThatDontHave.size());

        // Test methods that have or don't have the specified term
        reflectionSearch = new ReflectionSearch(someClassWithMethods);
        reflectionSearch.methodsThatHave("get");
        reflectionSearch.and();
        reflectionSearch.methodsThatDontHave("Foo");
        List<Method> methodsWithGetAndNoFoo = reflectionSearch.getMethods();
        assertNotNull(methodsWithGetAndNoFoo);
        assertEquals(1, methodsWithGetAndNoFoo.size());
        Method expectedMethodWithGetAndNoFoo = someClassWithMethods.getMethod("getBar");
        assertEquals(expectedMethodWithGetAndNoFoo, methodsWithGetAndNoFoo.get(0));
    }

    @Test
    public void testGetMethods() throws Exception {
        // Test empty search
        ReflectionSearch reflectionSearch = new ReflectionSearch(someClassWithMethods);
        List<Method> emptyMethods = reflectionSearch.getMethods();
        assertNotNull(emptyMethods);
        assertEquals(0, emptyMethods.size());

        // Test simple search
        reflectionSearch = new ReflectionSearch(someClassWithMethods);
        reflectionSearch.methodsThatHave("get");
        List<Method> methodsThatHave = reflectionSearch.getMethods();
        assertNotNull(methodsThatHave);
        assertEquals(2, methodsThatHave.size());

        // Test complex search
        reflectionSearch = new ReflectionSearch(someClassWithMethods);
        reflectionSearch.methodsThatHave("get");
        reflectionSearch.and();
        reflectionSearch.methodsThatDontHave("Foo");
        List<Method> methodsWithGetAndNoFoo = reflectionSearch.getMethods();
        assertNotNull(methodsWithGetAndNoFoo);
        assertEquals(1, methodsWithGetAndNoFoo.size());
        Method expectedMethodWithGetAndNoFoo = someClassWithMethods.getMethod("getBar");
        assertEquals(expectedMethodWithGetAndNoFoo, methodsWithGetAndNoFoo.get(0));
    }
}

class SomeClassWithAnnotation {
    @MyAnnotation
    public void someMethod() {}
}

class SomeClassWithoutAnnotation {
}

class SomeClassWithMethods {
    public void getFoo() {}
    public void getBar() {}
}

interface MyAnnotation {}
