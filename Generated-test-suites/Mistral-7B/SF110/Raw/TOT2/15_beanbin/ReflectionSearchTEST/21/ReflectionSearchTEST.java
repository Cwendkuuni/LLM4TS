
package net.sourceforge.beanbin.reflect;

import net.sourceforge.beanbin.search.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

public class ReflectionSearchTest {

    @BeforeClass
    public static void setUp() {
        ReflectionShelf.getInstance().clear();
    }

    @Test
    public void testHasAnnotation() {
        Class clazz = TestClassWithAnnotation.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));
    }

    @Test
    public void testMethodsThatHave() throws Exception {
        Class clazz = TestClassWithMethods.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch.methodsThatHave("test");
        List<Method> methods = reflectionSearch.getMethods();
        Method expectedMethod = TestClassWithMethods.class.getMethod("testMethod1");
        assertTrue(methods.contains(expectedMethod));
    }

    @Test
    public void testMethodsThatDontHave() throws Exception {
        Class clazz = TestClassWithMethods.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch.methodsThatDontHave("test");
        List<Method> methods = reflectionSearch.getMethods();
        Method expectedMethod = TestClassWithMethods.class.getMethod("testMethod2");
        assertFalse(methods.contains(expectedMethod));
    }

    @Test
    public void testMethodsWithAndOperator() throws Exception {
        Class clazz = TestClassWithMethods.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch.methodsThatHave("test");
        reflectionSearch.and();
        reflectionSearch.methodsThatDontHave("get");
        List<Method> methods = reflectionSearch.getMethods();
        Method expectedMethod = TestClassWithMethods.class.getMethod("testMethod3");
        assertTrue(methods.contains(expectedMethod));
    }

    @Test
    public void testMethodsWithOrOperator() throws Exception {
        Class clazz = TestClassWithMethods.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch.methodsThatHave("test");
        reflectionSearch.or();
        reflectionSearch.methodsThatDontHave("set");
        List<Method> methods = reflectionSearch.getMethods();
        Method expectedMethod1 = TestClassWithMethods.class.getMethod("testMethod1");
        Method expectedMethod2 = TestClassWithMethods.class.getMethod("testMethod4");
        assertTrue(methods.contains(expectedMethod1));
        assertTrue(methods.contains(expectedMethod2));
    }

    @Test(expected = BeanBinException.class)
    public void testMethodsWithInvalidQuery() throws Exception {
        Class clazz = TestClassWithMethods.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch.methodsThatHave("INVALID_QUERY");
        reflectionSearch.getMethods();
        fail("Expected BeanBinException");
    }

    @AfterClass
    public static void tearDown() {
        ReflectionShelf.getInstance().clear();
    }

    static class TestClassWithAnnotation {
        @MyAnnotation
        public void testMethod() {
        }
    }

    static class TestClassWithMethods {
        public void testMethod1() {
        }

        public void testMethod2() {
        }

        public void testMethod3() {
        }

        public void testMethod4() {
        }

        public void testMethod5() {
        }
    }

    interface MyAnnotation {
    }
}
