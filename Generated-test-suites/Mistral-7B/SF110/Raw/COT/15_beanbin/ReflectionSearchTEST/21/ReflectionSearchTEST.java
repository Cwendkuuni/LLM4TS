package net.sourceforge.beanbin.reflect.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.beanbin.*;
import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.search.*;
import org.junit.Test;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;

 @Test
 public void testReflectionSearchConstructor() {
 reflectionSearch = new ReflectionSearch(Object.class);
 assertNotNull(reflectionSearch);
 }

 @Test
 public void testHasAnnotation_EmptyAnnotation_ReturnsFalse() {
 reflectionSearch = new ReflectionSearch(Object.class);
 assertFalse(reflectionSearch.hasAnnotation(""));
 }

 @Test
 public void testHasAnnotation_MatchesAnnotatedClassName() {
 Class testClass = new TestAnnotation().getClass();
 reflectionSearch = new ReflectionSearch(testClass);
 assertTrue(reflectionSearch.hasAnnotation("TestAnnotation"));
 }

 @Test
 public void testHasAnnotation_MatchesAnnotatedMethodName() {
 Class testClass = new TestAnnotationMethod().getClass();
 reflectionSearch = new ReflectionSearch(testClass);
 assertTrue(reflectionSearch.hasAnnotation("TestAnnotation"));
 }

 @Test
 public void testHasAnnotation_MatchesWildcardClassName() {
 Class testClass = new WildcardTestAnnotation().getClass();
 reflectionSearch = new ReflectionSearch(testClass);
 assertTrue(reflectionSearch.hasAnnotation("*Annotation"));
 }

 @Test
 public void testHasAnnotation_MatchesWildcardMethodName() {
 Class testClass = new WildcardTestAnnotationMethod().getClass();
 reflectionSearch = new ReflectionSearch(testClass);
 assertTrue(reflectionSearch.hasAnnotation("*MethodAnnotation"));
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatHave_NonExistentAnnotation_ThrowsException() {
 reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("NonExistentAnnotation");
 fail("Exception expected");
 }

 @Test
 public void testMethodsThatHave_MatchesAnnotatedMethod() {
 Class testClass = new TestAnnotationMethod().getClass();
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("TestAnnotation");
 List<Method> result = reflectionSearch.getMethods();
 assertEquals(1, result.size());
 assertNotNull(result.get(0));
 assertEquals("testAnnotationMethod", result.get(0).getName());
 }

 @Test
 public void testMethodsThatHave_MultipleMatches() {
 Class testClass = new TestAnnotationMethodMultiMatch().getClass();
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("TestAnnotation");
 List<Method> result = reflectionSearch.getMethods();
 assertEquals(2, result.size());
 }

 @Test
 public void testMethodsThatDontHave_MatchesNonExistentMethod() {
 Class testClass = new TestAnnotationMethod().getClass();
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatDontHave("NonExistentMethod");
 List<Method> result = reflectionSearch.getMethods();
 assertTrue(result.isEmpty());
 }

 @Test
 public void testMethodsThatDontHave_MatchesExistentMethod() {
 Class testClass = new TestAnnotationMethod().getClass();
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatDontHave("TestMethod");
 List<Method> result = reflectionSearch.getMethods();
 assertEquals(1, result.size());
 assertNotNull(result.get(0));
 assertEquals("testOtherMethod", result.get(0).getName());
 }

 @Test
 public void testAndOperator() {
 Class testClass = new TestAnnotationMethodAnd().getClass();
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("TestAnnotation");
 reflectionSearch.and();
 reflectionSearch.methodsThatDontHave("testAnnotationMethod");
 List<Method> result = reflectionSearch.getMethods();
 assertTrue(result.isEmpty());
 }

 @Test
 public void testOrOperator() {
 Class testClass = new TestAnnotationMethodOr().getClass();
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("TestAnnotation");
 reflectionSearch.or();
 reflectionSearch.methodsThatDontHave("testAnnotationMethod");
 reflectionSearch.methodsThatHave("TestAnnotation2");
 List<Method> result = reflectionSearch.getMethods();
 assertEquals(2, result.size());
 }

 @Test
 public void testGetMethods_EmptyQuery_ReturnsEmptyList() {
 reflectionSearch = new ReflectionSearch(Object.class);
 List<Method> result = reflectionSearch.getMethods();
 assertTrue(result.isEmpty());
 }

 @Test
 public void testGetMethods_SimpleQuery_ReturnsCorrespondingMethods() {
 Class testClass = new TestAnnotationMethod().getClass();
 reflectionSearch = new ReflectionSearch(testClass);
 List<Method> result = reflectionSearch.getMethods();
 assertEquals(1, result.size());
 assertNotNull(result.get(0));
 assertEquals("testAnnotationMethod", result.get(0).getName());
 }

 @Test
 public void testGetMethods_ComplexQuery_ReturnsCorrespondingMethods() {
 Class testClass = new TestAnnotationMethodComplexQuery().getClass();
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("TestAnnotation");
 reflectionSearch.and();
 reflectionSearch.methodsThatDontHave("testAnnotationMethod");
 reflectionSearch.or();
 reflectionSearch.methodsThatHave("TestAnnotation2");
 List<Method> result = reflectionSearch.getMethods();
 assertEquals(2, result.size());
 }
}