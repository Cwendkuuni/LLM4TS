package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import org.junit.Test;

import net.sourceforge.beanbin.*;
import net.sourceforge.beanbin.query.*;

public class ReflectionSearchTest {

 private static final Class<ReflectionSearch> REFLECTION_SEARCH_CLASS = ReflectionSearch.class;

 @Test
 public void testConstructor() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
 assertNotNull(reflectionSearch);
 }

 @Test
 public void testHasAnnotationWithExactMatch() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
 assertTrue(reflectionSearch.hasAnnotation("AnnotationTest"));
 }

 @Test
 public void testHasAnnotationWithClassNameMatch() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
 assertTrue(reflectionSearch.hasAnnotation("AnnotationTest1"));
 }

 @Test
 public void testHasAnnotationWithTermNotFound() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
 assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
 }

 @Test
 public void testHasAnnotationWithWildcardMatch() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
 assertTrue(reflectionSearch.hasAnnotation("AnnotationTest*"));
 }

 @Test
 public void testMethodsThatHaveWithExactMatch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatHave("testMethod1");
 List<Method> expectedMethods = Arrays.asList(ReflectionSearch.class.getMethod("testMethod1"), ReflectionSearch.class.getMethod("testMethod2"));
 List<Method> actualMethods = reflectionSearch.getMethods();
 assertEquals(expectedMethods.size(), actualMethods.size());
 for (int i = 0; i < expectedMethods.size(); i++) {
 assertEquals(expectedMethods.get(i), actualMethods.get(i));
 }
 }

 @Test
 public void testMethodsThatHaveWithPartialMatch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatHave("testMetho");
 List<Method> expectedMethods = Arrays.asList(ReflectionSearch.class.getMethod("testMethod1"), ReflectionSearch.class.getMethod("testMethod2"));
 List<Method> actualMethods = reflectionSearch.getMethods();
 assertEquals(expectedMethods.size(), actualMethods.size());
 for (int i = 0; i < expectedMethods.size(); i++) {
 assertEquals(expectedMethods.get(i), actualMethods.get(i));
 }
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatHaveWithNoMatch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatHave("nonExistentMethod");
 fail("BeanBinException should be thrown");
 }

 @Test
 public void testMethodsThatDontHaveWithExactMatch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatDontHave("testMethod1");
 List<Method> expectedMethods = Arrays.asList(ReflectionSearch.class.getMethod("testMethod2"));
 List<Method> actualMethods = reflectionSearch.getMethods();
 assertEquals(expectedMethods.size(), actualMethods.size());
 for (int i = 0; i < expectedMethods.size(); i++) {
 assertEquals(expectedMethods.get(i), actualMethods.get(i));
 }
 }

 @Test
 public void testMethodsThatDontHaveWithPartialMatch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatDontHave("testMet");
 List<Method> expectedMethods = Arrays.asList(ReflectionSearch.class.getMethod("testMethod2"));
 List<Method> actualMethods = reflectionSearch.getMethods();
 assertEquals(expectedMethods.size(), actualMethods.size());
 for (int i = 0; i < expectedMethods.size(); i++) {
 assertEquals(expectedMethods.get(i), actualMethods.get(i));
 }
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatDontHaveWithNoMatch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatDontHave("testMethod1");
 fail("BeanBinException should be thrown");
 }

 @Test
 public void testAndConditional() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatHave("testMethod1");
 reflectionSearch.and();
 reflectionSearch.methodsThatDontHave("testMethod2");
 List<Method> expectedMethods = Arrays.asList(ReflectionSearch.class.getMethod("testMethod1"));
 List<Method> actualMethods = reflectionSearch.getMethods();
 assertEquals(expectedMethods.size(), actualMethods.size());
 for (int i = 0; i < expectedMethods.size(); i++) {
 assertEquals(expectedMethods.get(i), actualMethods.get(i));
 }
 }

 @Test
 public void testOrConditional() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);reflectionSearch.methodsThatHave("testMethod1");
 reflectionSearch.or();
 reflectionSearch.methodsThatDontHave("testMethod2");
 reflectionSearch.methodsThatHave("testMethod3");
 List<Method> expectedMethods = Arrays.asList(ReflectionSearch.class.getMethod("testMethod1"), ReflectionSearch.class.getMethod("testMethod3"));
 List<Method> actualMethods = reflectionSearch.getMethods();
 assertEquals(expectedMethods.size(), actualMethods.size());
 for (int i = 0; i < expectedMethods.size(); i++) {
 assertEquals(expectedMethods.get(i), actualMethods.get(i));
 }
 }

 @Test
 public void testEmptyQuery() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 List<Method> actualMethods = reflectionSearch.getMethods();
 assertTrue(actualMethods.isEmpty());
 }
}