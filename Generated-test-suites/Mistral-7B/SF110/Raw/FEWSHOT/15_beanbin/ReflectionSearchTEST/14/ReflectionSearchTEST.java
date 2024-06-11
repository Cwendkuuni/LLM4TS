package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.beanbin.query.Criteria;
import net.sourceforge.beanbin.query.Conditional;
import net.sourceforge.beanbin.query.ReflectionCriteria;
import net.sourceforge.beanbin.query.ReflectionShelf;
import net.sourceforge.beanbin.reflect.SearchType;

import org.junit.Before;
import org.junit.Test;

public class ReflectionSearchTest {

    private ReflectionSearch reflectionSearch;
    private Class testClass;
    private List<Method> methods;

    @Before
    public void setUp() throws Exception {
        testClass = TestClassWithMethods.class;
        reflectionSearch = new ReflectionSearch(testClass);
        methods = Arrays.asList(testClass.getMethods());
    }

    @Test
    public void testHasAnnotationWithAnnotation() {
        assertTrue(reflectionSearch.hasAnnotation("AnnotationTest"));
    }

    @Test
    public void testHasAnnotationWithoutAnnotation() {
        assertFalse(reflectionSearch.hasAnnotation("NonExistingAnnotation"));
    }

    @Test
    public void testHasAnnotationWithWildcard() {
        assertTrue(reflectionSearch.hasAnnotation("Anno*"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch search = reflectionSearch.methodsThatHave("getFoo");
        List<Method> result = search.getMethods();
        assertEquals(1, result.size());
        assertEquals("getFoo", result.get(0).getName());
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch search = reflectionSearch.methodsThatDontHave("getFoo");
        List<Method> result = search.getMethods();
        assertEquals(3, result.size());
        for (Method method : result) {
            assertFalse(method.getName().equals("getFoo"));
        }
    }

    @Test
    public void testAnd() throws BeanBinException {
        ReflectionSearch search1 = reflectionSearch.methodsThatHave("getFoo");
        ReflectionSearch search2 = search1.and();
        search2.methodsThatDontHave("setBar");
        List<Method> result = search2.getMethods();
        assertEquals(0, result.size());
    }

    @Test
    public void testOr() throws BeanBinException {
        ReflectionSearch search1 = reflectionSearch.methodsThatHave("getFoo");
        ReflectionSearch search2 = search1.or();
        search2.methodsThatHave("getBar");
        List<Method> result = search2.getMethods();
        assertEquals(2, result.size());
        assertTrue(result.contains(testClass.getMethod("getFoo", new Class[]{String.class})));
        assertTrue(result.contains(testClass.getMethod("getBar", new Class[]{})));
    }

    @Test(expected = BeanBinException.class)
    public void testNullClass() throws BeanBinException {
        new ReflectionSearch(null);
    }

    @Test(expected = BeanBinException.class)
    public void testNullQuery() throws BeanBinException {
        new ReflectionSearch(testClass);
        reflectionSearch.query = null;
        reflectionSearch.getMethods();
    }

    @Test
    public void testEmptyQuery() throws BeanBinException {
        reflectionSearch.query = new Query();
        List<Method> result = reflectionSearch.getMethods();
        assertEquals(methods.size(), result.size());
        for (Method method : result) {
            assertTrue(methods.contains(method));
        }
    }

    @Test
    public void testEmptyList() throws BeanBinException {
        reflectionSearch.query = new Query();
        ReflectionSearch search = reflectionSearch.and();
        search.query.setCriterias(new ArrayList<>());
        List<Method> result = search.getMethods();
        assertEquals(methods.size(), result.size());
        for (Method method : result) {
            assertTrue(methods.contains(method));
        }
    }

    @Test
    public void testMethodsThatHaveWithEmptyString() throws BeanBinException {
        ReflectionSearch search = reflectionSearch.methodsThatHave("");
        List<Method> result = search.getMethods();
        assertEquals(methods.size(), result.size());
        for (Method method : result) {
            assertTrue(methods.contains(method));
        }
    }

    @Test
    public void testMethodsThatHaveWithNullString() throws BeanBinException {
        ReflectionSearch search = reflectionSearch.methodsThatHave(null);
        List<Method> result = search.getMethods();
        assertEquals(methods.size(), result.size());
        for (Method method : result) {
            assertTrue(methods.contains(method));
        }
    }

    @Test
    public void testReflectionShelf() throws BeanBinException {
        ReflectionSearch search = reflectionSearch.methodsThatHave("getFoo");
        List<Method> result = search.getMethods();
        assertEquals(1, result.size());
        assertEquals("getFoo", result.get(0).getName());

        ReflectionShelf reflectionShelf = ReflectionShelf.getInstance();
        List<Method> cachedResult = (List<Method>)reflectionShelf.get(reflectionSearch.query);
        assertEquals(1, cachedResult.size());
        assertEquals("getFoo", cachedResult.get(0).getName());

        List<Method> newResult = search.getMethods();
        assertEquals(1, newResult.size());
        assertEquals("getFoo", newResult.get(0).getName());
        assertSame(cachedResult, newResult);
    }
}

class TestClassWithMethods {
    @AnnotationTest
    public void test() {
    }

    public String getFoo(String bar) {
        return "foo";
    }

    public void setFoo(String foo) {
    }

    public void test2() {
    }

    public int getBar() {
        return 42;
    }
}