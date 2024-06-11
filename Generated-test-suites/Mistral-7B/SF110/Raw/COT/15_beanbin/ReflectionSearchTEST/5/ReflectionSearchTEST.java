package net.sourceforge.beanbin.reflect.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.reflect.ReflectionCriteria;
import net.sourceforge.beanbin.reflect.ReflectionShelf;
import net.sourceforge.beanbin.query.Conditional;
import net.sourceforge.beanbin.query.Criteria;
import net.sourceforge.beanbin.query.MethodReflectionCriteria;

import org.junit.Test;

public class ReflectionSearchTest {

 private static final Class<ReflectionSearch> CLASS = ReflectionSearch.class;

 @Test
 public void testReflectionSearchConstructor() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(CLASS);
 assertNotNull(reflectionSearch);
 }

 @Test
 public void testHasAnnotationWithExactMatch() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(CLASS);
 assertTrue(reflectionSearch.hasAnnotation("AnnotationName"));
 }

 @Test
 public void testHasAnnotationWithClassNameMatch() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(CLASS);
 assertTrue(reflectionSearch.hasAnnotation("StringBuilder"));
 }

 @Test
 public void testHasAnnotationWithNoMatch() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(CLASS);
 assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
 }

 @Test
 public void testHasWildcardWithMatch() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(CLASS);
 assertTrue(reflectionSearch.hasWildcard("Annotation*"));
 }

 @Test
 public void testHasWildcardWithNoMatch() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(CLASS);
 assertFalse(reflectionSearch.hasWildcard("NonExistentAnnotation"));
 }

 @Test
 public void testMethodsThatHaveWithEmptyQuery() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(CLASS);
 reflectionSearch.methodsThatHave("");
 List<Method> methods = reflectionSearch.getMethods();
 assertTrue(methods.isEmpty());
 }

 @Test
 public void testMethodsThatHaveWithSingleMatch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(CLASS);
 reflectionSearch.methodsThatHave("testMethod");
 List<Method> methods = reflectionSearch.getMethods();
 Method expectedMethod = CLASS.getMethod("testMethod");
 assertEquals(Collections.singletonList(expectedMethod), methods);
 }

 @Test
 public void testMethodsThatHaveWithMultipleMatches() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(CLASS);
 reflectionSearch.methodsThatHave("test*");
 List<Method> methods = reflectionSearch.getMethods();
 List<Method> expectedMethods = new ArrayList<Method>();
 expectedMethods.add(CLASS.getMethod("testMethod1"));
 expectedMethods.add(CLASS.getMethod("testMethod2"));
 assertEquals(expectedMethods, methods);
 }

 @Test
 public void testMethodsThatDontHaveWithEmptyQuery() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(CLASS);
 reflectionSearch.methodsThatDontHave("");
 List<Method> methods = reflectionSearch.getMethods();
 assertTrue(methods.isEmpty());
 }

 @Test
 public void testMethodsThatDontHaveWithSingleMatch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(CLASS);
 reflectionSearch.methodsThatDontHave("testMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(Collections.emptyList(), methods);
 }

 @Test
 public void testMethodsThatDontHaveWithMultipleMatches() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(CLASS);
 reflectionSearch.methodsThatDontHave("test*");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(Collections.emptyList(), methods);
 }

 @Test
 public void testAndCondition() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(CLASS);
 reflectionSearch.methodsThatHave("test*");
 reflectionSearch.and();
 reflectionSearch.methodsThatHave("get*");
 List<Method> methods = reflectionSearch.getMethods();
 List<Method> expectedMethods = new ArrayList<Method>();
 expectedMethods.add(CLASS.getMethod("getTextest"));
 expectedMethods.add(CLASS.getMethod("getTestGet"));
 assertEquals(expectedMethods, methods);
 }

 @Test
 public void testOrCondition() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(CLASS);reflectionSearch.methodsThatHave("test*");
 reflectionSearch.or();
 reflectionSearch.methodsThatHave("get*");
 reflectionSearch.methodsThatHave("set*");
 List<Method> methods = reflectionSearch.getMethods();
 List<Method> expectedMethods = new ArrayList<Method>();
 expectedMethods.add(CLASS.getMethod("getTextest"));
 expectedMethods.add(CLASS.getMethod("getTestGet"));
 expectedMethods.add(CLASS.getMethod("setTestSet"));
 assertEquals(expectedMethods, methods);
 }

 @Test(expected = BeanBinException.class)
 public void testInvalidQuery() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(CLASS);
 reflectionSearch.methodsThatHave("invalidQuery");
 reflectionSearch.getMethods();
 }

}