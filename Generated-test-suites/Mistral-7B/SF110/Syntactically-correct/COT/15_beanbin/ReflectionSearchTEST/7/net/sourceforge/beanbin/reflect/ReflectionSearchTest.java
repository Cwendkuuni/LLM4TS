package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.search.*;
import org.junit.Test;

public class ReflectionSearchTest {

 private static final Class<ReflectionSearch> REFLECTION_SEARCH_CLASS = ReflectionSearch.class;

 @Test
 public void testConstructor() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
 assertNotNull(reflectionSearch);
 }

 @Test
 public void testHasAnnotation_EmptyAnnotations_ReturnsFalse() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
 assertFalse(reflectionSearch.hasAnnotation(""));
 }

 @Test
 public void testHasAnnotation_MatchesAnnotationName_ReturnsTrue() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
 assertTrue(reflectionSearch.hasAnnotation("AnnotationTest"));
 }

 @Test
 public void testHasAnnotation_MatchesClassName_ReturnsTrue() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
 assertTrue(reflectionSearch.hasAnnotation("Test"));
 }

 @Test
 public void testHasAnnotation_DoesNotMatchAnnotationNameOrClassName_ReturnsFalse() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
 assertFalse(reflectionSearch.hasAnnotation("OtherClass"));
 }

 @Test
 public void testHasAnnotation_MatchesWildcardAnnotationName_ReturnsTrue() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
 assertTrue(reflectionSearch.hasAnnotation("Anno*"));
 }

 @Test
 public void testHasAnnotation_MatchesWildcardClassName_ReturnsTrue() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
 assertTrue(reflectionSearch.hasAnnotation("Test*"));
 }

 @Test
 public void testMethodsThatHave_ReturnsReflectionSearchInstance() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
 ReflectionSearch methodsThatHave = reflectionSearch.methodsThatHave("test");
 assertSame(reflectionSearch, methodsThatHave);
 }

 @Test
 public void testMethodsThatHave_MatchesMethods_ReturnsReflectionSearchInstance() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
 ReflectionSearch methodsThatHave = reflectionSearch.methodsThatHave("test");
 List<Method> methods = reflectionSearch.getMethods();
 List<Method> expectedMethods = Arrays.asList(ReflectionSearchTest.class.getMethods());
 assertEquals(expectedMethods, methodsThatHave.getMethods());
 }

 @Test
 public void testMethodsThatDontHave_ReturnsReflectionSearchInstance() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
 ReflectionSearch methodsThatDontHave = reflectionSearch.methodsThatDontHave("test");
 assertSame(reflectionSearch, methodsThatDontHave);
 }

 @Test
 public void testMethodsThatDontHave_DoesNotMatchMethods_ReturnsEmptyList() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
 List<Method> methods = reflectionSearch.getMethods();
 ReflectionSearch methodsThatDontHave = reflectionSearch.methodsThatDontHave("test");
 List<Method> expectedMethods = new ArrayList<>();
 assertEquals(expectedMethods, methodsThatDontHave.getMethods());
 }

 @Test
 public void testAndOperator_MatchesIntersectingMethods_ReturnsReflectionSearchInstance() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
 ReflectionSearch methodsThatHaveTest = reflectionSearch.methodsThatHave("test");
 ReflectionSearch methodsThatHaveAnno = reflectionSearch.methodsThatHave("Annotation");
 ReflectionSearch and = methodsThatHaveTest.and();
 ReflectionSearch andMethods = and.methodsThatHave("Annotation");
 List<Method> expectedMethods = Arrays.asList(ReflectionSearchTest.class.getMethod("testAnnotationTest"),
 ReflectionSearchTest.class.getMethod("testAnnotationAnnotationTest"));
 assertEquals(expectedMethods, andMethods.getMethods());
 }

 @Test
 public void testOrOperator_MatchesUnionMethods_ReturnsReflectionSearchInstance() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
 ReflectionSearch methodsThatHaveTest = reflectionSearch.methodsThatHave("test");
 ReflectionSearch methodsThatHaveAnno = reflectionSearch.methodsThatHave("Annotation");
 ReflectionSearch or = methodsThatHaveTest.or();
 ReflectionSearch orMethods = or.methodsThatHave("Annotation");List<Method> expectedMethods = Arrays.asList(ReflectionSearchTest.class.getMethod("testAnnotationTest"),
 ReflectionSearchTest.class.getMethod("testAnnotationAnnotationTest"),
 ReflectionSearchTest.class.getMethod("testAnnotationTest2"));
 assertEquals(expectedMethods, orMethods.getMethods());
 }

 @Test(expected = BeanBinException.class)
 public void testInvalidQuery_EmptyCriteriaList_ThrowsBeanBinException() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
 List<Method> methods = reflectionSearch.getMethods();
 ReflectionSearch emptyQuery = new ReflectionSearch(methods.getClass());
 List<Method> emptyCriteriaList = new ArrayList<>();
 emptyQuery.setQuery(emptyCriteriaList);
 emptyQuery.getMethods();
 }

 @Test(expected = BeanBinException.class)
 public void testInvalidQuery_NullCriteria_ThrowsBeanBinException() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
 List<Method> methods = reflectionSearch.getMethods();
 ReflectionSearch nullCriteriaQuery = new ReflectionSearch(methods.getClass());
 nullCriteriaQuery.setQuery(null);
 nullCriteriaQuery.getMethods();
 }

 @Test
 public void testGetMethods_ReturnsEmptyList_WhenNoCriteriaAreSet() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
 List<Method> methods = reflectionSearch.getMethods();
 assertTrue(methods.isEmpty());
 }

 @Test
 public void testGetMethods_ReturnsMethodsMatchingCriteria_WhenCriteriaAreSet() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
 ReflectionSearch methodsThatHaveTest = reflectionSearch.methodsThatHave("test");
 List<Method> methods = methodsThatHaveTest.getMethods();
 List<Method> expectedMethods = Arrays.asList(ReflectionSearchTest.class.getMethod("testAnnotationTest"),
 ReflectionSearchTest.class.getMethod("testAnnotationAnnotationTest"),
 ReflectionSearchTest.class.getMethod("testAnnotationTest2"));
 assertEquals(expectedMethods, methods);
 }
}