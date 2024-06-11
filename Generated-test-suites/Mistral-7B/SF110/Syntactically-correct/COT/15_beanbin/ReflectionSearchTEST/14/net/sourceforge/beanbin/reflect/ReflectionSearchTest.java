package net.sourceforge.beanbin.reflect.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.beanbin.query.Conditional;
import net.sourceforge.beanbin.query.Criteria;
import net.sourceforge.beanbin.query.ReflectionCriteria;
import net.sourceforge.beanbin.reflect.ReflectionSearch;

import org.junit.Before;
import org.junit.Test;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class testClass;

 @Before
 public void setUp() {
 testClass = TestClassWithMethods.class;
 reflectionSearch = new ReflectionSearch(testClass);
 }

 @Test
 public void testHasAnnotation() {
 // Test case for hasAnnotation method with an exact match
 assertTrue(reflectionSearch.hasAnnotation("TestAnnotation"));

 // Test case for hasAnnotation method with a matched class name
 TestAnnotation annotation = testClass.getAnnotation(TestAnnotation.class);
 assertTrue(reflectionSearch.hasAnnotation(annotation.annotationType().getName()));

 // Test case for hasAnnotation method with a wildcard term
 assertTrue(reflectionSearch.hasAnnotation("TestAnno*"));

 // Test case for hasAnnotation method with an unmatched term
 assertFalse(reflectionSearch.hasAnnotation("UnmatchedTerm"));
 }

 @Test
 public void testHasWildcard() {
 // Test case for hasWildcard method with a term containing wildcard
 assertTrue(reflectionSearch.hasWildcard("TestAnno*"));

 // Test case for hasWildcard method with a term without wildcard
 assertFalse(reflectionSearch.hasWildcard("TestAnnotation"));
 }

 @Test
 public void testMethodsThatHave() throws Exception {
 // Test case for methodsThatHave method with an exact match
 List<Method> methods = reflectionSearch.methodsThatHave("test").getMethods();
 assertEquals(5, methods.size());
 for (Method method : methods) {
 assertTrue(method.getName().contains("test"));
 }

 // Test case for methodsThatHave method with a wildcard term
 methods = reflectionSearch.methodsThatHave("test*").getMethods();
 assertEquals(7, methods.size());
 for (Method method : methods) {
 assertTrue(method.getName().startsWith("test"));
 }

 // Test case for methodsThatHave method with an empty term
 methods = reflectionSearch.methodsThatHave("").getMethods();
 assertTrue(methods.isEmpty());
}

 @Test
 public void testMethodsThatDontHave() throws Exception {
 // Test case for methodsThatDontHave method with an exact match
 List<Method> methods = reflectionSearch.methodsThatDontHave("test").getMethods();
 assertEquals(3, methods.size());
 for (Method method : methods) {
 assertFalse(method.getName().contains("test"));
 }

 // Test case for methodsThatDontHave method with a wildcard term
 methods = reflectionSearch.methodsThatDontHave("test*").getMethods();
 assertTrue(methods.isEmpty());
}

 @Test
 public void testAnd() throws Exception {
 List<Method> methods = reflectionSearch.methodsThatHave("test").getMethods();
 reflectionSearch.and();
 methods = reflectionSearch.methodsThatHave("public").getMethods();
 List<Method> expectedMethods = new ArrayList<>();
 expectedMethods.add(TestClassWithMethods.class.getMethod("publicTest"));
 methods.addAll(expectedMethods);
 assertEquals(6, methods.size());
 }

 @Test
 public void testOr() throws Exception {
 List<Method> methods = reflectionSearch.methodsThatHave("test").getMethods();
 reflectionSearch.or();
 methods = reflectionSearch.methodsThatDontHave("private").getMethods();
 methods.addAll(methodsThatHave("test"));
 List<Method> expectedMethods = new ArrayList<>();
 expectedMethods.add(TestClassWithMethods.class.getMethod("privateTest"));
 expectedMethods.add(TestClassWithMethods.class.getMethod("publicTest"));
 methods.addAll(expectedMethods);
 assertEquals(7, methods.size());
 }

}

 class TestAnnotation {
 @Test
 public void testAnnotation() {
 }
}

 class TestClassWithMethods {
 @Test
 public void publicTest() {
 }

 @Test
 public void privateTest() {
 }

 @Test
 public void test1() {
 }

 @Test
 public void test2() {
 }

 @Test
 public void test3() {
 }

 @Test
 public void test4() {
 }

 @Test
 public void test5() {
 }
}