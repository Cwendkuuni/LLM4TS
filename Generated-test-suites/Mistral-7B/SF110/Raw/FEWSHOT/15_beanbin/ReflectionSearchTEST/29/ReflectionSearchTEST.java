package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.beanbin.reflect.SearchType;
import net.sourceforge.beanbin.query.Criteria;
import net.sourceforge.beanbin.query.ReflectionCriteria;
import net.sourceforge.beanbin.query.ReflectionShelf;
import net.sourceforge.beanbin.search.MethodReflectionCriteria;
import net.sourceforge.beanbin.search.SearchType;

import org.junit.Before;
import org.junit.Test;

public class ReflectionSearchTest {

    private Class testClass;
    private ReflectionSearch reflectionSearch;
    private List<Method> methods;

    @Before
    public void setup() throws Exception {
        testClass = Class.forName("net.sourceforge.beanbin.TestClass");
        reflectionSearch = new ReflectionSearch(testClass);
        methods = Arrays.asList(testClass.getMethods());
    }

    @Test
    public void testHasAnnotationWithExactMatch() {
        assertTrue(reflectionSearch.hasAnnotation("AnnotationTest"));
    }

    @Test
    public void testHasAnnotationWithClassNameMatch() {
        assertTrue(reflectionSearch.hasAnnotation("TestAnnotation"));
    }

    @Test
    public void testHasAnnotationWithTermMatch() {
        assertTrue(reflectionSearch.hasAnnotation("Annotation"));
    }

    @Test
    public void testHasAnnotationWithWildcardMatch() {
        assertTrue(reflectionSearch.hasAnnotation("Annotation*"));
    }

    @Test
    public void testHasAnnotationWithNoMatch() {
        assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
    }

    @Test
    public void testMethodsThatHave_SimpleTerm() throws Exception {
        reflectionSearch.methodsThatHave("test");
        List<Method> resultMethods = reflectionSearch.getMethods();

        for (Method method : resultMethods) {
            assertTrue(method.getName().contains("test"));
        }
    }

    @Test
    public void testMethodsThatHave_MultipleTerms() throws Exception {
        reflectionSearch.methodsThatHave("test", "method");
        List<Method> resultMethods = reflectionSearch.getMethods();

        for (Method method : resultMethods) {
            assertTrue(method.getName().contains("test"));
            assertTrue(method.getName().contains("method"));
        }
    }

    @Test
    public void testMethodsThatDontHave_SingleTerm() throws Exception {
        reflectionSearch.methodsThatDontHave("test");
        List<Method> resultMethods = reflectionSearch.getMethods();

        for (Method method : resultMethods) {
            assertFalse(method.getName().contains("test"));
        }
    }

    @Test
    public void testMethodsThatDontHave_MultipleTerms() throws Exception {
        reflectionSearch.methodsThatDontHave("test", "method");
        List<Method> resultMethods = reflectionSearch.getMethods();

        for (Method method : resultMethods) {
            assertFalse(method.getName().contains("test"));
            assertFalse(method.getName().contains("method"));
        }
    }

    @Test
    public void testAndCondition() throws Exception {
        reflectionSearch.methodsThatHave("test");
        reflectionSearch.methodsThatDontHave("void");
        List<Method> resultMethods = reflectionSearch.getMethods();

        for (Method method : resultMethods) {
            assertTrue(method.getName().contains("test"));
            assertFalse(method.getReturnType().equals(void.class));
        }
    }

    @Test
    public void testOrCondition() throws Exception {
        reflectionSearch.methodsThatHave("test");
        reflectionSearch.methodsThatHave("method");
        List<Method> resultMethods = reflectionSearch.getMethods();

        for (Method method : resultMethods) {
            assertTrue(method.getName().contains("test") || method.getName().contains("method"));
        }
    }

    @Test(expected = BeanBinException.class)
    public void testGetMethods_InvalidQuery() throws Exception {
        reflectionSearch = new ReflectionSearch(testClass);
        reflectionSearch.query = null;
        reflectionSearch.getMethods();
    }

    @Test
    public void testGetMethods_EmptyQuery() throws Exception {
        reflectionSearch = new ReflectionSearch(testClass);
        reflectionSearch.query = new Query();
        List<Method> resultMethods = reflectionSearch.getMethods();
        assertEquals(methods.size(), resultMethods.size());
    }

    @Test
    public void testGetMethods_MultipleConditions() throws Exception {
        reflectionSearch.methodsThatHave("test");
        reflectionSearch.methodsThatDontHave("void");
        List<Method> resultMethods = reflectionSearch.getMethods();

        assertEquals(methods.size(), resultMethods.size());
        for (Method method : resultMethods) {
            assertTrue(method.getName().contains("test"));
            assertFalse(method.getReturnType().equals(void.class));
        }
    }

    @Test
    public void testReflectionShelf() throws Exception {
        ReflectionSearch reflectionSearch1 = new ReflectionSearch(testClass);
        reflectionSearch1.query = new Query();
        reflectionSearch1.methodsThatHave("test");
        List<Method> resultMethods1 = reflectionSearch1.getMethods();

        ReflectionSearch reflectionSearch2 = new ReflectionSearch(testClass);
        reflectionSearch2.query = new Query();
        reflectionSearch2.methodsThatDontHave("void");
        List<Method> resultMethods2 = reflectionSearch2.getMethods();

        ReflectionShelf reflectionShelf = ReflectionShelf.getInstance();
        List<Method> master = reflectionShelf.get(reflectionSearch1.query);
        List<Method> toadd = reflectionShelf.get(reflectionSearch2.query);

        reflectionSearch1.intersect(master, toadd);
        List<Method> resultMethods1Intersect = reflectionShelf.get(reflectionSearch1.query);

        reflectionSearch2.union(master, toadd);
        List<Method> resultMethods1Union = reflectionShelf.get(reflectionSearch1.query);

        assertEquals(resultMethods1Intersect.size(), resultMethods1Union.size());
        for (int i = 0; i < resultMethods1Intersect.size(); i++) {
            assertEquals(resultMethods1Intersect.get(i).getName(), resultMethods1Union.get(i).getName());
        }
    }
}