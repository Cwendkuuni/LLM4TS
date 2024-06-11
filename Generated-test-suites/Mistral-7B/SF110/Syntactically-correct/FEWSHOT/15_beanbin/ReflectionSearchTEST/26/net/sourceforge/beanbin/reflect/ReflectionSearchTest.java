package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.beanbin.query.Criteria;
import net.sourceforge.beanbin.query.ReflectionCriteria;
import net.sourceforge.beanbin.query.SearchType;
import net.sourceforge.beanbin.search.BeanBinException;
import net.sourceforge.beanbin.search.ReflectionShelf;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        String annotationTerm = "Annotation";
        assertTrue(reflectionSearch.hasAnnotation(annotationTerm));

        String packageTerm = "java.lang.annotation.";
        assertTrue(reflectionSearch.hasAnnotation(packageTerm));

        String nonExistentTerm = "NonExistentAnnotation";
        assertFalse(reflectionSearch.hasAnnotation(nonExistentTerm));

        ReflectionSearch reflectionSearchWithAnnotation = new ReflectionSearch(Annotation.class);
        assertTrue(reflectionSearchWithAnnotation.hasAnnotation(Annotation.class.getName()));
        assertTrue(reflectionSearchWithAnnotation.hasAnnotation("annotation"));
    }

    @Test
    public void testHasWildcard() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        String termWithWildcard = "*Annotation";
        assertTrue(reflectionSearch.hasWildcard(termWithWildcard));

        String termWithoutWildcard = "Annotation";
        assertFalse(reflectionSearch.hasWildcard(termWithoutWildcard));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        String term = "equals";

        List<Method> methods = reflectionSearch.methodsThatHave(term).getMethods();
        List<Method> expectedMethods = Arrays.asList(Object.class.getMethod("equals", Object.class), Object.class.getMethod("equals"));
        assertEquals(expectedMethods, methods);
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        String term = "equals";

        List<Method> methods = reflectionSearch.methodsThatDontHave(term).getMethods();
        List<Method> allMethods = Arrays.asList(Object.class.getDeclaredMethods(), Object.class.getMethods());
        List<Method> expectedMethods = new ArrayList<>();
        for (Method method : allMethods) {
            if (!method.getName().equals(term)) {
                expectedMethods.add(method);
            }
        }
        assertEquals(expectedMethods, methods);
    }

    @Test
    public void testAnd() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        String term1 = "equals";
        String term2 = "int";

        ReflectionSearch reflectionSearchAnd = reflectionSearch.methodsThatHave(term1).and();
        reflectionSearchAnd = reflectionSearchAnd.methodsThatHave(term2);
        List<Method> methods = reflectionSearchAnd.getMethods();
        List<Method> expectedMethods = new ArrayList<>();
        for (Method method : Object.class.getMethods()) {
            if (method.getName().equals(term1) && method.getParameterTypes()[0].getTypeName().equals("int")) {
                expectedMethods.add(method);
            }
        }
        assertEquals(expectedMethods, methods);
    }

    @Test
    public void testOr() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        String term1 = "equals";
        String term2 = "hashCode";

        ReflectionSearch reflectionSearchOr = reflectionSearch.methodsThatHave(term1).or();
        reflectionSearchOr = reflectionSearchOr.methodsThatHave(term2);
        List<Method> methods = reflectionSearchOr.getMethods();
        List<Method> expectedMethods = new ArrayList<>();
        for (Method method : Object.class.getMethods()) {
            if (method.getName().equals(term1) || method.getName().equals(term2)) {
                expectedMethods.add(method);
            }
        }
        assertEquals(expectedMethods, methods);
    }

    @Test
    public void testGetMethods() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(26, methods.size());

        ReflectionSearch reflectionSearchWithFilter = reflectionSearch.methodsThatHave("toString").getMethods();
        List<Method> filteredMethods = reflectionSearchWithFilter.getMethods();
        assertEquals(1, filteredMethods.size());
        assertEquals(Object.class.getMethod("toString"), filteredMethods.get(0));
    }

    @Test
    public void testIntersect() {
        List<Method> master = Arrays.asList(Object.class.getMethod("toString"), Object.class.getMethod("equals"));
        List<Method> toadd = Arrays.asList(Object.class.getMethod("hashCode"));
        List<Method> result = new ArrayList<>();
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);

        reflectionSearch.intersect(master, toadd);
        assertEquals(1, master.size()); // The master list should not change
        assertEquals(Arrays.asList(Object.class.getMethod("toString"), Object.class.getMethod("equals")), master);
        assertEquals(1, result.size());
        assertEquals(Object.class.getMethod("equals"), result.get(0));
    }

    @Test
    public void testUnion() {
        List<Method> master = new ArrayList<>();
        List<Method> toadd = Arrays.asList(Object.class.getMethod("hashCode"));
        List<Method> result = new ArrayList<>();
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);

        reflectionSearch.union(master, toadd);
        assertEquals(2, master.size());
        assertEquals(Arrays.asList(Object.class.getMethod("toString"), Object.class.getMethod("equals"), Object.class.getMethod("hashCode")), master);
        assertEquals(1, result.size());
        assertEquals(Object.class.getMethod("hashCode"), result.get(0));
    }

    @Test
    public void testReflectionSearchWithNullQuery() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        Query query = null;
        List<Method> methods = reflectionSearch.getMethods(query);
        assertNull(methods);
    }

    @Test(expected = BeanBinException.class)
    public void testReflectionSearchWithEmptyQuery() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
        Query query = new Query();
        reflectionSearch.getMethods(query);
    }

    @Test
    public void testReflectionSearchInstance() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        Query query = new Query();
        ReflectionSearch reflectionSearchInstance = reflectionSearch.getMethods(query).get(0).invoke(reflectionSearch);
        assertEquals(reflectionSearch, reflectionSearchInstance);
    }
}