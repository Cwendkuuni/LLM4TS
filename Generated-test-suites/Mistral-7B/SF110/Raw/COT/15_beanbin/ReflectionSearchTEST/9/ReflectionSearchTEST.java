package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.search.Criteria;
import net.sourceforge.beanbin.search.Conditional;
import net.sourceforge.beanbin.search.MethodReflectionCriteria;
import net.sourceforge.beanbin.search.ReflectionCriteria;
import net.sourceforge.beanbin.search.SearchType;
import net.sourceforge.beanbin.query.Query;

import org.junit.Test;

public class ReflectionSearchTest {

 private Class testClass;
 private ReflectionSearch reflectionSearch;

 @Before
 public void setup() throws Exception {
 testClass = TestClass.class;
 reflectionSearch = new ReflectionSearch(testClass);
 }

 @Test
 public void testHasAnnotation_EmptyAnnotation_ReturnsFalse() {
 assertFalse(reflectionSearch.hasAnnotation(""));
 }

 @Test
 public void testHasAnnotation_MatchesAnnotationName_ReturnsTrue() {
 Annotation testAnnotation = new MyAnnotation();
 testClass.getDeclaredField("testField").annotate(testAnnotation);
 assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));
 }

 @Test
 public void testHasAnnotation_MatchesClassName_ReturnsTrue() {
 Annotation testAnnotation = new MyAnnotation();
 testClass.getDeclaredField("testField").annotate(testAnnotation);
 assertTrue(reflectionSearch.hasAnnotation("net.sourceforge.beanbin.reflect.MyAnnotation"));
 }

 @Test
 public void testHasAnnotation_MatchesWildcardAnnotationName_ReturnsTrue() {
 assertTrue(reflectionSearch.hasAnnotation("MyAnnotation*"));
 }

 @Test
 public void testHasAnnotation_MatchesWildcardClassName_ReturnsTrue() {
 assertTrue(reflectionSearch.hasAnnotation("*MyAnnotation"));
 }

 @Test
 public void testHasAnnotation_DoesNotMatch_ReturnsFalse() {
 assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
 }

 @Test
 public void testHasWildcard_ReturnsFalse_NoWildcard() {
 assertFalse(reflectionSearch.hasWildcard("MyAnnotation"));
 }

 @Test
 public void testHasWildcard_ReturnsTrue_HasWildcard() {
 assertTrue(reflectionSearch.hasWildcard("MyAnnotation*"));
 }

 @Test
 public void testMethodsThatHave_ReturnsReflectionSearchObject() throws BeanBinException {
 assertNotNull(reflectionSearch.methodsThatHave("test"));
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatHave_ThrowsException_InvalidTerm() throws BeanBinException {
 reflectionSearch.methodsThatHave("");
 }

 @Test
 public void testMethodsThatHave_ReturnsEmptyList_NoMatches() throws BeanBinException {
 List<Method> methods = reflectionSearch.getMethods();
 assertNotNull(methods);
 assertFalse(methods.size() > 0);
 }

 @Test
 public void testMethodsThatHave_ReturnsMatchingMethods() throws BeanBinException {
 List<Method> methods = reflectionSearch.methodsThatHave("test").getMethods();
 Method testMethod = testClass.getDeclaredMethod("testMethod");
 methods.add(testMethod);
 List<Method> expectedMethods = new ArrayList<Method>();
 expectedMethods.add(testMethod);
 assertEquals(expectedMethods, methods);
 }

 @Test
 public void testMethodsThatDontHave_ReturnsReflectionSearchObject() throws BeanBinException {
 assertNotNull(reflectionSearch.methodsThatDontHave("test"));
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatDontHave_ThrowsException_InvalidTerm() throws BeanBinException {
 reflectionSearch.methodsThatDontHave("");
 }

 @Test
 public void testMethodsThatDontHave_ReturnsDifferentMethods() throws BeanBinException {
 List<Method> methods1 = reflectionSearch.methodsThatHave("test").getMethods();
 List<Method> methods2 = reflectionSearch.methodsThatDontHave("test").getMethods();
 assertNotNull(methods1);
 assertNotNull(methods2);
 assertFalse(methods1.containsAll(methods2));
 assertFalse(methods2.containsAll(methods1));
 }

 @Test
 public void testAnd() throws BeanBinException {
 List<Method> methods1 = reflectionSearch.methodsThatHave("test").getMethods();
 List<Method> methods2 = reflectionSearch.methodsThatHave("public").getMethods();
 List<Method> expectedMethods = new ArrayList<Method>();
 Method testMethod = testClass.getDeclaredMethod("testMethod");
 Method publicMethod = testClass.getDeclaredMethod("publicMethod", int.class);
 expectedMethods.add(testMethod);
 expectedMethods.add(publicMethod);
 reflectionSearch.and().getMethods();
 List<Method> actualMethods = reflectionSearch.getMethods();
 assertEquals(expectedMethods, actualMethods);
 }

 @Test
 public void testOr() throws BeanBinException {
 List<Method> methods1 = reflectionSearch.methodsThatHave("test").getMethods();
 List<Method> methods2 = reflectionSearch.methodsThatHave("public").getMethods();
 List<Method> expectedMethods = new ArrayList<Method>();
 Method testMethod = testClass.getDeclaredMethod("testMethod");
 Method publicMethod = testClass.getDeclaredMethod("publicMethod", int.class);
 expectedMethods.add(testMethod);
 expectedMethods.add(publicMethod);
 expectedMethods.add(testClass.getDeclaredMethod("anotherMethod"));
 reflectionSearch.or().getMethods();
 List<Method> actualMethods = reflectionSearch.getMethods();
 assertEquals(expectedMethods, actualMethods);
 }

 @Test
 public void testGetMethods_ReturnsEmptyList_NoMatches() throws BeanBinException {
 List<Method> methods = reflectionSearch.getMethods();
 assertNotNull(methods);
 assertFalse(methods.size() > 0);
 }

 @Test
 public void testGetMethods_ReturnsMatchingMethods() throws BeanBinException {
 List<Method> methods = reflectionSearch.methodsThatHave("test").getMethods();
 Method testMethod = testClass.getDeclaredMethod("testMethod");
 methods.add(testMethod);
 List<Method> expectedMethods = new ArrayList<Method>();
 expectedMethods.add(testMethod);
 assertEquals(expectedMethods, methods);
 }

 @Test(expected = BeanBinException.class)
 public void testGetMethods_ThrowsException_EmptyQuery() throws BeanBinException {
 reflectionSearch.getMethods();
 }

 @Test
 public void testIntersect() {
 List<Method> list1 = new ArrayList<Method>();
 List<Method> list2 = new ArrayList<Method>();
 list1.add(new MethodReflectionCriteria(testClass, "test", SearchType.EQUALS).getResults());
 list2.add(new MethodReflectionCriteria(testClass, "test", SearchType.EQUALS).getResults());
 reflectionSearch.intersect(list1, list2);
 assertNotNull(list1);
 assertNotNull(list2);
 assertTrue(list1.size() <= list2.size());
 }

 @Test
 public void testUnion() {
 List<Method> list1 = new ArrayList<Method>();
 List<Method> list2 = new ArrayList<Method>();
 list1.add(new MethodReflectionCriteria(testClass, "test", SearchType.EQUALS).getResults());
 list2.add(new MethodReflectionCriteria(testClass, "public", SearchType.EQUALS).getResults());
 reflectionSearch.union(list1, list2);
 assertNotNull(list1);
 assertNotNull(list2);
 assertTrue(list1.size() >= list2.size());
 }

 @Test
 public void testGetMethods_EmptyList() throws BeanBinException {
 Query query = new Query();
 ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.query = query;
 List<Method> methods = reflectionSearch.getMethods();
 assertNotNull(methods);
 assertFalse(methods.size() > 0);
 }

 @Test
 public void testGetMethods_NullList() throws BeanBinException {
 Query query = new Query();
 ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.query = query;
 List<Method> methods = null;
 reflectionSearch.getMethods(methods);
 assertNull(methods);
 }

 @Test
 public void testGetMethods_EmptyQuery() throws BeanBinException {
 Query query = new Query();
 ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.query = query;
 reflectionSearch.getMethods();
 }

 @Test
 public void testGetMethods_NullCriteria() throws BeanBinException {
 Query query = new Query();
 ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.query = query;
 List<Criteria> criterias = new ArrayList<Criteria>();
 Criteria criteria = new ReflectionCriteria(testClass, "test", SearchType.EQUALS);
 criterias.add(criteria);
 reflectionSearch.query.setCriterias(criterias);
 reflectionSearch.getMethods();
 }

 @Test
 public void testGetMethods_EmptyCriteria() throws BeanBinException {
 Query query = new Query();
 ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.query = query;
 List<Criteria> criterias = new ArrayList<Criteria>();
 reflectionSearch.query.setCriterias(criterias);
 reflectionSearch.getMethods();
 }

 @Test
 public void testGetMethods_NullResultList() throws BeanBinException {
 Query query = new Query();
 ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.query = query;
 List<Method> methods = new ArrayList<Method>();
 reflectionSearch.getMethods(methods);
 assertNull(methods);
 }
}