package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.beanbin.search.BeanBinException;
import net.sourceforge.beanbin.search.Conditional;
import net.sourceforge.beanbin.search.Criteria;
import net.sourceforge.beanbin.search.ReflectionCriteria;
import net.sourceforge.beanbin.search.ReflectionShelf;
import net.sourceforge.beanbin.reflect.ReflectionSearch;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        Class clazz = String.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        assertFalse(reflectionSearch.hasAnnotation("AnnonymousAnnotation"));
        assertTrue(reflectionSearch.hasAnnotation("Object"));
        assertFalse(reflectionSearch.hasAnnotation("String"));
        Annotation annotation = new MyAnnotation();
        clazz = annotation.getClass();
        reflectionSearch = new ReflectionSearch(clazz);
        assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));
    }

    @Test
    public void testHasWildcard() {
        assertTrue(ReflectionSearch.hasWildcard("MyAnnotation*"));
        assertTrue(ReflectionSearch.hasWildcard("MyAnnotation.*"));
        assertTrue(ReflectionSearch.hasWildcard("MyAnnotation.Annotation"));
        assertFalse(ReflectionSearch.hasWildcard("MyAnnotation"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        Class clazz = String.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch.methodsThatHave("toString");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertEquals("toString", methods.get(0).getName());
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        Class clazz = String.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch.methodsThatDontHave("toString");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertNull(methods.get(0));
    }

    @Test
    public void testAnd() throws BeanBinException {
        Class clazz = String.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch.methodsThatHave("toString");
        reflectionSearch.and();
        reflectionSearch.methodsThatHave("equals");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(0, methods.size());

        clazz = Object.class;
        reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch.methodsThatHave("equals");
        reflectionSearch.and();
        reflectionSearch.methodsThatHave("hashCode");
        methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
        assertEquals("hashCode", methods.get(0).getName());
    }

    @Test
    public void testOr() throws BeanBinException {
        Class clazz = Object.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch.methodsThatHave("equals");
        reflectionSearch.or();
        reflectionSearch.methodsThatHave("hashCode");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(2, methods.size());
        assertTrue(methods.contains(Arrays.asList("equals", Object.class)));
        assertTrue(methods.contains(Arrays.asList("hashCode", Object.class)));
    }

    @Test(expected = BeanBinException.class)
    public void testGetMethodsWithNoCriteria() throws BeanBinException {
        Class clazz = Object.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch.getMethods();
    }

    @Test
    public void testIntersect() {
        List<Method> master = new ArrayList<>();
        List<Method> toadd = Arrays.asList(new Method[] {new Method("toString", String.class), new Method("equals", Object.class)});
        ReflectionSearch reflectionSearch = mock(ReflectionSearch.class);
        when(reflectionSearch.getMethods()).thenReturn(master);
        reflectionSearch.intersect(master, toadd);
        assertEquals(1, master.size());
        assertEquals("toString", master.get(0).getName());
    }

    @Test
    public void testUnion() {
        List<Method> master = new ArrayList<>();
        List<Method> toadd = Arrays.asList(new Method[] {new Method("toString", String.class), new Method("hashCode", Object.class)});
        ReflectionSearch reflectionSearch = mock(ReflectionSearch.class);
        when(reflectionSearch.getMethods()).thenReturn(Collections.emptyList());
        reflectionSearch.union(master, toadd);
        assertEquals(2, master.size());
        assertTrue(master.contains(Arrays.asList("toString", String.class)));
        assertTrue(master.contains(Arrays.asList("hashCode", Object.class)));
    }
}

class MyAnnotation {}