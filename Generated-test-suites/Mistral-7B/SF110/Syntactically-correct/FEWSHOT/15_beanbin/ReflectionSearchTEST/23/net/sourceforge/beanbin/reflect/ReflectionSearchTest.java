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
import java.util.List;

import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.search.BeanBinException;
import net.sourceforge.beanbin.search.Criteria;
import net.sourceforge.beanbin.search.ReflectionCriteria;
import net.sourceforge.beanbin.search.SearchType;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch search = new ReflectionSearch(String.class);

        // Test null clazz
        assertFalse(search.hasAnnotation(null));

        // Test empty Annotation array
        assertFalse(search.hasAnnotation(""));

        // Test no matching Annotation
        search.clazz = Object.class;
        assertFalse(search.hasAnnotation("Annotation1"));

        // Test exact matching Annotation name
        search.clazz = String[].class;
        assertTrue(search.hasAnnotation("ArrayStoreAnnotation"));

        // Test exact matching Annotation class name
        search.clazz = String.class;
        assertTrue(search.hasAnnotation("DeprecatedAnnotation"));

        // Test wildcard Annotation name
        search.clazz = MyInterface.class;
        String interfaceName = MyInterface.class.getName();
        assertTrue(search.hasAnnotation("I*" + interfaceName.substring(interfaceName.lastIndexOf("."))));

        // Test wildcard Annotation class name
        search.clazz = MyClassWithAnnotation.class;
        String className = MyClassWithAnnotation.class.getName();
        assertTrue(search.hasAnnotation("A*" + className.substring(className.lastIndexOf("."))));
    }

    @Test(expected = BeanBinException.class)
    public void testHasAnnotationWithNullQuery() {
        ReflectionSearch search = new ReflectionSearch(Object.class);
        search.hasAnnotation(null);
    }

    @Test
    public void testMethodsThatHave() {
        ReflectionSearch search = new ReflectionSearch(Object.class);

        // Test empty search
        List<Method> methods = search.getMethods();
        assertNotNull(methods);
        assertTrue(methods.isEmpty());

        // Test exact method name match
        search = new ReflectionSearch(Object.class);
        search.methodsThatHave("toString");
        methods = search.getMethods();
        assertFalse(methods.isEmpty());
        assertEquals(1, methods.size());
        assertEquals("toString", methods.get(0).getName());

        // Test multiple exact method name matches
        search = new ReflectionSearch(Object.class);
        search.methodsThatHave("toString");
        search.methodsThatHave("getClass");
        methods = search.getMethods();
        assertEquals(2, methods.size());

        // Test method name wildcard match
        search = new ReflectionSearch(Object.class);
        search.methodsThatHave("get*");
        methods = search.getMethods();
        assertEquals(2, methods.size());
        assertSame(Object.class.getMethod("getClass"), methods.get(0));
        assertSame(Object.class.getMethod("getClass0"), methods.get(1)); // getClass0 is a duplicate name for getClass in Object class

        // Test method name and return type wildcard match
        search = new ReflectionSearch(Object.class);
        search.methodsThatHave("get*", void.class);
        methods = search.getMethods();
        assertEquals(0, methods.size());

        // Test method name and parameter types wildcard match
        search = new ReflectionSearch(Object.class);
        Class[] types = { String.class, Integer.TYPE };
        search.methodsThatHave("set*", types);
        methods = search.getMethods();
        assertEquals(1, methods.size());
        assertSame(Object.class.getMethod("setStringInt", new Class[] { String.class, Integer.TYPE }), methods.get(0));

        // Test multiple method name and return type wildcard matches
        search = new ReflectionSearch(Object.class);
        search.methodsThatHave("get*", void.class);
        search.methodsThatHave("set*", String.class);
        methods = search.getMethods();
        assertEquals(2, methods.size());
        assertSame(Object.class.getMethod("toString"), methods.get(0));
        assertSame(Object.class.getMethod("setBoolean", new Class[] { Boolean.TYPE }), methods.get(1));
    }

    @Test
    public void testMethodsThatDontHave() {
        ReflectionSearch search = new ReflectionSearch(Object.class);

        // Test empty search
        search.methodsThatDontHave(null);
        List<Method> methods = search.getMethods();
        assertNotNull(methods);
        assertTrue(methods.isEmpty());

        // Test exact method name match
        search.methodsThatHave("toString");
        search.methodsThatDontHave("toString");
        methods = search.getMethods();
        assertEquals(0, methods.size());

        // Test multiple exact method name matches
        search = new ReflectionSearch(Object.class);
        search.methodsThatHave("toString");
        search.methodsThatDontHave("toString");
        search.methodsThatHave("getClass");
        methods = search.getMethods();
        assertEquals(1, methods.size());
        assertSame(Object.class.getMethod("getClass"), methods.get(0));

        // Test method name wildcard match (no matching methods)
        search = new ReflectionSearch(Object.class);
        search.methodsThatHave("get*");
        search.methodsThatDontHave("get*");
        methods = search.getMethods();
        assertEquals(0, methods.size());

        // Test method name and return type wildcard match (one matching method)
        search = new ReflectionSearch(Object.class);
        search.methodsThatHave("get*", void.class);
        search.methodsThatDontHave("get*", void.class);
        methods = search.getMethods();
        assertEquals(0, methods.size());

        // Test method name and parameter types wildcard match (one matching method)
        search = new ReflectionSearch(Object.class);
        Class[] types = { String.class, Integer.TYPE };
        search.methodsThatHave("set*", types);
        search.methodsThatDontHave("set*", types);
        methods = search.getMethods();
        assertEquals(0, methods.size());

        // Test multiple method name and return type wildcard matches (one common method)
        search = new ReflectionSearch(Object.class);
        search.methodsThatHave("get*", void.class);
        search.methodsThatDontHave("get*", String.class);
        methods = search.getMethods();
        assertEquals(1, methods.size());
        assertSame(Object.class.getMethod("toString"), methods.get(0));

        // Test multiple method name and return type wildcard matches (no common method)
        search = new ReflectionSearch(Object.class);
        search.methodsThatHave("get*", void.class);
        search.methodsThatDontHave("set*", String.class);
        methods = search.getMethods();
        assertEquals(0, methods.size());
    }

    @Test
    public void testAnd() {
        ReflectionSearch search = new ReflectionSearch(Object.class);

        // Test empty search
        search.methodsThatHave("toString");
        search.and();
        List<Method> methods = search.getMethods();
        assertEquals(0, methods.size());

        // Test exact method name matches
        search.methodsThatHave("toString");
        search.methodsThatHave("getClass");
        search.and();
        methods = search.getMethods();
        assertEquals(0, methods.size());

        // Test method name wildcard matches
        search.methodsThatHave("get*");
        search.methodsThatDontHave("set*");
        search.and();
        methods = search.getMethods();
        assertEquals(0, methods.size());

        // Test method name and return type wildcard matches
        search.methodsThatHave("get*", void.class);
        search.methodsThatDontHave("set*", String.class);
        search.and();
        methods = search.getMethods();
        assertEquals(0, methods.size());

        // Test method name, return type, and parameter types wildcard matches
        Class[] types1 = { String.class, Integer.TYPE };
        Class[] types2 = { String.class, Float.TYPE };
        search.methodsThatHave("set*", types1);
        search.methodsThatDontHave("set*", types2);
        search.and();
        methods = search.getMethods();
        assertEquals(0, methods.size());

        // Test multiple method name, return type, and parameter types wildcard matches (no common method)
        search.methodsThatHave("get*", void.class);
        search.methodsThatDontHave("set*", String.class);
        search.methodsThatHave("set*", types1);
        search.and();
        methods = search.getMethods();
        assertEquals(0, methods.size());

        // Test multiple method name, return type, and parameter types wildcard matches (one common method)
        search.methodsThatHave("get*", void.class);
        search.methodsThatDontHave("set*", String.class);
        search.methodsThatHave("set*", types1);
        search.methodsThatHave("set*", types2);
        search.and();
        methods = search.getMethods();
        assertEquals(1, methods.size());
        assertSame(Object.class.getMethod("setStringInt", new Class[] { String.class, Integer.TYPE }), methods.get(0));
    }

    @Test
    public void testOr() {
        ReflectionSearch search = new ReflectionSearch(Object.class);

        // Test empty search
        search.methodsThatHave("toString");
        search.or();
        List<Method> methods = search.getMethods();
        assertEquals(0, methods.size());

        // Test exact method name matches
        search.methodsThatHave("toString");
        search.methodsThatHave("getClass");
        search.or();
        methods = search.getMethods();
        assertEquals(2, methods.size());

        // Test method name wildcard matches
        search.methodsThatHave("get*");
        search.methodsThatHave("set*");
        search.or();
        methods = search.getMethods();
        assertEquals(3, methods.size());

        // Test method name and return type wildcard matches
        search.methodsThatHave("get*", void.class);
        search.methodsThatHave("set*", String.class);
        search.or();
        methods = search.getMethods();
        assertEquals(4, methods.size());

        // Test method name, return type, and parameter types wildcard matches
        Class[] types1 = { String.class, Integer.TYPE };
        Class[] types2 = { String.class, Float.TYPE };
        search.methodsThatHave("set*", types1);
        search.methodsThatHave("set*", types2);
        search.or();
        methods = search.getMethods();
        assertEquals(5, methods.size());
        List<Method> filteredMethods = new ArrayList<>(Arrays.asList(Object.class.getMethod("setStringInt", new Class[] { String.class, Integer.TYPE }), Object.class.getMethod("setStringFloat", new Class[] { String.class, Float.TYPE })));
        assertSame(filteredMethods, methods);

        // Test multiple method name, return type, and parameter types wildcard matches (no common method)
        search.methodsThatHave("get*", void.class);
        search.methodsThatHave("set*", String.class);
        search.methodsThatHave("set*", types1);
        search.or();
        methods = search.getMethods();
        assertEquals(4, methods.size());

        // Test multiple method name, return type, and parameter types wildcard matches (one common method)
        search.methodsThatHave("get*", void.class);
        search.methodsThatHave("set*", String.class);
        search.methodsThatHave("set*", types1);
        search.methodsThatHave("set*", types2);
        search.or();
        methods = search.getMethods();
        assertEquals(5, methods.size());
        assertSame(filteredMethods, methods);
    }
}