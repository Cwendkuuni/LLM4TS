package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;
import java.lang.reflect.*;
import java.util.*;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch search = new ReflectionSearch(String.class);
        assertTrue(search.hasAnnotation("ToString"));
        assertTrue(search.hasAnnotation("Deprecated"));
        assertFalse(search.hasAnnotation("RandomAnnotation"));
    }

    @Test
    public void testHasAnnotationWithWildcard() {
        ReflectionSearch search = new ReflectionSearch(String.class);
        assertTrue(search.hasAnnotation("Anno*"));
        assertTrue(search.hasAnnotation("AnnoInterface"));
        assertFalse(search.hasAnnotation("RandomAnnotation"));
    }

    @Test(expected = BeanBinException.class)
    public void testInvalidAnnotationQuery() {
        ReflectionSearch search = new ReflectionSearch(String.class);
        search.methodsThatHave("InvalidAnnotation");
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch search = new ReflectionSearch(String.class);

        List<Method> methods = search.getMethods();
        assertEquals(0, methods.size());

        search.methodsThatHave("toString");
        methods = search.getMethods();
        assertEquals(1, methods.size());
        assertNotNull(methods.get(0));
        assertEquals("toString", methods.get(0).getName());

        search.methodsThatHave("equals");
        methods = search.getMethods();
        assertEquals(1, methods.size());
        assertNotNull(methods.get(0));
        assertEquals("equals", methods.get(0).getName());

        search.methodsThatHave("hashCode");
        methods = search.getMethods();
        assertEquals(1, methods.size());
        assertNotNull(methods.get(0));
        assertEquals("hashCode", methods.get(0).getName());

        search.methodsThatHave("compareTo");
        methods = search.getMethods();
        assertEquals(1, methods.size());
        assertNotNull(methods.get(0));
        assertEquals("compareTo", methods.get(0).getName());
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch search = new ReflectionSearch(String.class);

        List<Method> methods = search.getMethods();
        assertEquals(10, methods.size());

        search.methodsThatDontHave("equals");
        methods = search.getMethods();
        assertEquals(9, methods.size());
        assertFalse(methods.contains(String.class.getMethod("equals", Object.class)));

        search.methodsThatDontHave("hashCode");
        methods = search.getMethods();
        assertEquals(8, methods.size());
        assertFalse(methods.contains(String.class.getMethod("hashCode", (Class[]) null)));

        search.methodsThatDontHave("compareTo");
        methods = search.getMethods();
        assertEquals(7, methods.size());
        assertFalse(methods.contains(String.class.getMethod("compareTo", String.class)));

        search.methodsThatDontHave("toString");
        methods = search.getMethods();
        assertEquals(6, methods.size());
        assertFalse(methods.contains(String.class.getMethod("toString", (Class[]) null)));

        search.methodsThatDontHave("length");
        methods = search.getMethods();
        assertEquals(5, methods.size());
        assertFalse(methods.contains(String.class.getMethod("length", (Class[]) null)));

        search.methodsThatDontHave("charAt");
        methods = search.getMethods();
        assertEquals(4, methods.size());
        assertFalse(methods.contains(String.class.getMethod("charAt", int.class)));

        search.methodsThatDontHave("regionMatches");
        methods = search.getMethods();
        assertEquals(3, methods.size());
        assertFalse(methods.contains(String.class.getMethod("regionMatches", String.class, int.class, int.class, int.class)));

        search.methodsThatDontHave("matches");
        methods = search.getMethods();
        assertEquals(2, methods.size());
        assertFalse(methods.contains(String.class.getMethod("matches", Pattern.class)));

        search.methodsThatDontHave("startsWith");
        methods = search.getMethods();
        assertEquals(1, methods.size());
        assertFalse(methods.contains(String.class.getMethod("startsWith", String.class)));
    }

    @Test
    public void testAndCondition() throws BeanBinException {
        ReflectionSearch search = new ReflectionSearch(String.class);

        List<Method> methods = search.getMethods();
        assertEquals(10, methods.size());

        search.methodsThatHave("equals");
        search.methodsThatDontHave("compareTo");
        methods = search.getMethods();
        assertEquals(8, methods.size());
        assertFalse(methods.contains(String.class.getMethod("equals", Object.class)));
        assertFalse(methods.contains(String.class.getMethod("compareTo", String.class)));
        assertTrue(methods.contains(String.class.getMethod("hashCode", (Class[]) null)));
        assertTrue(methods.contains(String.class.getMethod("length", (Class[]) null)));
        assertTrue(methods.contains(String.class.getMethod("charAt", int.class)));
        assertTrue(methods.contains(String.class.getMethod("regionMatches", String.class, int.class, int.class, int.class)));
        assertTrue(methods.contains(String.class.getMethod("matches", Pattern.class)));
        assertTrue(methods.contains(String.class.getMethod("startsWith", String.class)));
    }

    @Test
    public void testOrCondition() throws BeanBinException {
        ReflectionSearch search = new ReflectionSearch(String.class);

        List<Method> methods = search.getMethods();
        assertEquals(10, methods.size());

        search.methodsThatHave("equals");
        search.methodsThatHave("hashCode");
        methods = search.getMethods();
        assertEquals(9, methods.size());
        assertFalse(methods.contains(String.class.getMethod("compareTo", String.class)));
        assertTrue(methods.contains(String.class.getMethod("equals", Object.class)));
        assertTrue(methods.contains(String.class.getMethod("hashCode", (Class[]) null)));
        assertTrue(methods.contains(String.class.getMethod("length", (Class[]) null)));
        assertTrue(methods.contains(String.class.getMethod("charAt", int.class)));
        assertTrue(methods.contains(String.class.getMethod("regionMatches", String.class, int.class, int.class, int.class)));
        assertTrue(methods.contains(String.class.getMethod("matches", Pattern.class)));
        assertTrue(methods.contains(String.class.getMethod("startsWith", String.class)));
        assertTrue(methods.contains(String.class.getMethod("compareTo", Object.class)));
    }

    @Test
    public void testEmptyQuery() throws BeanBinException {
        ReflectionSearch search = new ReflectionSearch(String.class);
        List<Method> methods = search.getMethods();
        assertEquals(10, methods.size());
    }
}