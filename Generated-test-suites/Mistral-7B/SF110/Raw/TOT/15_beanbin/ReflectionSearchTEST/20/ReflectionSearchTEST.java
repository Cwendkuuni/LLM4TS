package net.sourceforge.beanbin.reflect;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class testClass;

 @Before
 public void setUp() {
 testClass = TestClass.class;
 reflectionSearch = new ReflectionSearch(testClass);
 }

 @Test
 public void testHasAnnotation() {
 // Test case for hasAnnotation method with a matching annotation
 reflectionSearch.methodsThatHave("TestAnnotation");
 List<Method> methods = reflectionSearch.getMethods();
 Method testMethod = testClass.getMethod("testMethodWithTestAnnotation");
 assertTrue(reflectionSearch.hasAnnotation("TestAnnotation"));
 assertTrue(methods.contains(testMethod));

 // Test case for hasAnnotation method with a non-matching annotation
 reflectionSearch = new ReflectionSearch(testClass);
 assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
 assertFalse(methods.contains(testMethod));
 }

 @Test
 public void testHasWildcard() {
 // Test case for hasWildcard method with a term containing a wildcard character
 assertTrue(reflectionSearch.hasWildcard("TestAnnotation*"));

 // Test case for hasWildcard method with a term not containing a wildcard character
 assertFalse(reflectionSearch.hasWildcard("TestAnnotation"));
 }

 @Test
 public void testMethodsThatHave() throws BeanBinException {
 // Test case for methodsThatHave method with a term matching exactly one method name
 reflectionSearch.methodsThatHave("testMethodWithTestAnnotation");
 List<Method> methods = reflectionSearch.getMethods();
 Method testMethod = testClass.getMethod("testMethodWithTestAnnotation");
 assertEquals(1, methods.size());
 assertEquals(testMethod, methods.get(0));

 // Test case for methodsThatHave method with a term matching multiple method names
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("test*");
 methods = reflectionSearch.getMethods();
 int expectedSize = 2; // Assuming there are two methods with names starting with 'test'
 assertEquals(expectedSize, methods.size());
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatDontHave() throws BeanBinException {
 // Test case for methodsThatDontHave method with a term not present in any method name
 reflectionSearch.methodsThatDontHave("NonExistentMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertTrue(methods.isEmpty());
 }

 @Test
 public void testAnd() throws BeanBinException {
 // Test case for and method with two criteria where both criteria are satisfied
 reflectionSearch.methodsThatHave("testMethodWithTestAnnotation");
 reflectionSearch.methodsThatDontHave("private");
 methods = reflectionSearch.getMethods();
 int expectedSize = 1; // Assuming there is one method with the name "testMethodWithTestAnnotation" that is not private
 assertEquals(expectedSize, methods.size());

 // Test case for and method with two criteria where one criterion is not satisfied
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("testMethodWithTestAnnotation");
 reflectionSearch.methodsThatHave("private");
 methods = reflectionSearch.getMethods();
 assertTrue(methods.isEmpty());
 }

 @Test
 public void testOr() throws BeanBinException {
 // Test case for or method with two criteria where one criterion is satisfied
 reflectionSearch.methodsThatHave("testMethodWithTestAnnotation");
 reflectionSearch.methodsThatDontHave("private");
 reflectionSearch.or();
 methods = reflectionSearch.getMethods();
 int expectedSize = 1; // Assuming there is one method with the name "testMethodWithTestAnnotation" that is not private
 assertEquals(expectedSize, methods.size());

 // Test case for or method with two criteria where both criteria are not satisfied
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("testMethodWithTestAnnotation");
 reflectionSearch.methodsThatHave("private");
 reflectionSearch.or();
 methods = reflectionSearch.getMethods();
 assertTrue(methods.isEmpty());
 }
}

class TestClass {
 @TestAnnotation
 public void testMethodWithTestAnnotation() {
 // Test method implementation
 }

 private void testMethodWithPrivateAnnotation() {
 // Test method implementation
 }
}