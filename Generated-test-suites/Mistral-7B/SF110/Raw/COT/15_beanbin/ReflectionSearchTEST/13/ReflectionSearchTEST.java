package net.sourceforge.beanbin.reflect.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class testClass;

 @Before
 public void setup() throws Exception {
 testClass = MockClass.class;
 reflectionSearch = new ReflectionSearch(testClass);
 }

 @Test
 public void testHasAnnotation() {
 // Test normal case
 assertTrue(reflectionSearch.hasAnnotation("TestAnnotation"));
 // Test case insensitive
 assertTrue(reflectionSearch.hasAnnotation("testannotation"));
 // Test empty class
 ReflectionSearch emptyClassSearch = new ReflectionSearch(Object.class);
 assertFalse(emptyClassSearch.hasAnnotation("TestAnnotation"));
 // Test null term
 assertFalse(reflectionSearch.hasAnnotation(null));
 // Test invalid term
 assertFalse(reflectionSearch.hasAnnotation("$%^&*"));
 }

 @Test(expected = BeanBinException.class)
 public void testHasAnnotationThrowsExceptionWithInvalidSearchTerm() throws Exception {
 reflectionSearch.hasAnnotation("*");
 }

 @Test
 public void testMethodsThatHave() throws Exception {
 // Test normal case
 List<Method> expectedMethods = new ArrayList<>();
 expectedMethods.add(MockClass.method1);
 expectedMethods.add(MockClass.method2);
 reflectionSearch.methodsThatHave("method");
 List<Method> actualMethods = reflectionSearch.getMethods();
 assertEquals(expectedMethods, actualMethods);
 // Test case insensitive
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("Method");
 actualMethods = reflectionSearch.getMethods();
 assertEquals(expectedMethods, actualMethods);
 // Test empty search
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("");
 actualMethods = reflectionSearch.getMethods();
 assertTrue(actualMethods.isEmpty());
 // Test null search
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave(null);
 actualMethods = reflectionSearch.getMethods();
 assertTrue(actualMethods.isEmpty());
 // Test invalid search term
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("$%^&*");
 actualMethods = reflectionSearch.getMethods();
 assertTrue(actualMethods.isEmpty());
 }

 @Test
 public void testMethodsThatDontHave() throws Exception {
 // Test normal case
 List<Method> expectedMethods = new ArrayList<>();
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatDontHave("method");
 expectedMethods.addAll(Collections.list(testClass.getMethods()));
 expectedMethods.removeAll(Collections.singletonList(MockClass.method1));
 expectedMethods.removeAll(Collections.singletonList(MockClass.method2));
 List<Method> actualMethods = reflectionSearch.getMethods();
 assertEquals(expectedMethods, actualMethods);
 // Test empty search
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatDontHave("");
 actualMethods = reflectionSearch.getMethods();
 assertEquals(Collections.list(testClass.getMethods()), actualMethods);
 // Test null search
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatDontHave(null);
 actualMethods = reflectionSearch.getMethods();
 assertEquals(Collections.list(testClass.getMethods()), actualMethods);
 // Test invalid search term
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatDontHave("$%^&*");
 actualMethods = reflectionSearch.getMethods();
 assertEquals(Collections.list(testClass.getMethods()), actualMethods);
 }

 @Test
 public void testAnd() throws Exception {
 // Test normal case
 List<Method> expectedMethods = new ArrayList<>();
 reflectionSearch.methodsThatHave("method");
 reflectionSearch.and();
 reflectionSearch.methodsThatHave("annotation");
 expectedMethods.addAll(Collections.singletonList(MockClass.method3));
 List<Method> actualMethods = reflectionSearch.getMethods();
 assertEquals(expectedMethods, actualMethods);
 // Test empty search
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("method");
 reflectionSearch.and();
 reflectionSearch.methodsThatHave("");
 actualMethods = reflectionSearch.getMethods();
 assertTrue(actualMethods.isEmpty());
 // Test null search
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("method");
 reflectionSearch.and();
 reflectionSearch.methodsThatHave(null);
 actualMethods = reflectionSearch.getMethods();
 assertTrue(actualMethods.isEmpty());
 // Test invalid search term
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("method");
 reflectionSearch.and();
 reflectionSearch.methodsThatHave("$%^&*");
 actualMethods = reflectionSearch.getMethods();
 assertTrue(actualMethods.isEmpty());
 }

 @Test
 public void testOr() throws Exception {
 // Test normal case
 List<Method> expectedMethods = new ArrayList<>();
 reflectionSearch.methodsThatHave("method1");
 reflectionSearch.or();
 reflectionSearch.methodsThatHave("method2");
 expectedMethods.addAll(Collections.singletonList(MockClass.method1));
 expectedMethods.addAll(Collections.singletonList(MockClass.method2));
 List<Method> actualMethods = reflectionSearch.getMethods();
 assertEquals(expectedMethods, actualMethods);
 // Test empty search
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("method1");
 reflectionSearch.or();
 reflectionSearch.methodsThatHave("");
 actualMethods = reflectionSearch.getMethods();
 assertEquals(Collections.singletonList(MockClass.method1), actualMethods);
 // Test null search
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("method1");
 reflectionSearch.or();
 reflectionSearch.methodsThatHave(null);
 actualMethods = reflectionSearch.getMethods();
 assertEquals(Collections.singletonList(MockClass.method1), actualMethods);
 // Test invalid search term
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("method1");
 reflectionSearch.or();
 reflectionSearch.methodsThatHave("$%^&*");
 actualMethods = reflectionSearch.getMethods();
 assertEquals(Collections.singletonList(MockClass.method1), actualMethods);
 }

 @Test(expected = BeanBinException.class)
 public void testGetMethodsThrowsException() throws Exception {
 reflectionSearch.getMethods();
 }
}

class MockClass {
 @TestAnnotation
 static void method1() {
 }
 @TestAnnotation
 static void method2() {
 }
 @TestAnnotation
 static void method3() {
 }
}