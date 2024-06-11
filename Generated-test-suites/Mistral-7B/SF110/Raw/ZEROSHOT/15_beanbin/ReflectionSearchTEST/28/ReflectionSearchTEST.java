package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.beanbin.*;
import net.sourceforge.beanbin.query.*;
import net.sourceforge.beanbin.reflect.*;

import org.junit.Before;
import org.junit.Test;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class testClass;

 @Before
 public void setUp() throws Exception {
 testClass = TestClassWithMethods.class;
 reflectionSearch = new ReflectionSearch(testClass);
 }

 @Test
 public void testHasAnnotation() {
 final String annotationTerm = "TestAnnotation";
 final boolean expectedResult = true;

 reflectionSearch = new ReflectionSearch(testClass);
 final boolean result = reflectionSearch.hasAnnotation(annotationTerm);

 assertEquals(expectedResult, result);
 }

 @Test
 public void testHasAnnotationWithWildcard() {
 final String annotationTerm = "TestAnnotation*";
 final boolean expectedResult = true;

 reflectionSearch = new ReflectionSearch(testClass);
 final boolean result = reflectionSearch.hasAnnotation(annotationTerm);

 assertEquals(expectedResult, result);
 }

 @Test
 public void testHasAnnotationWithNoMatch() {
 final String annotationTerm = "NoMatchAnnotation";
 final boolean expectedResult = false;

 reflectionSearch = new ReflectionSearch(testClass);
 final boolean result = reflectionSearch.hasAnnotation(annotationTerm);

 assertEquals(expectedResult, result);
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatHaveWithNoMatch() throws Exception {
 final String methodName = "noMatchMethod";
 final ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave(methodName);
 reflectionSearch.getMethods();
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatDontHaveWithNoMatch() throws Exception {
 final String methodName = "noMatchMethod";
 final ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatDontHave(methodName);
 reflectionSearch.getMethods();
 }

 @Test
 public void testMethodsThatHave() throws Exception {
 final String methodName = "testMethod";
 final ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave(methodName);
 final List<Method> methods = reflectionSearch.getMethods();
 final int expectedSize = 1;
 final Method expectedMethod = testClass.getDeclaredMethod(methodName);

 assertEquals(expectedSize, methods.size());
 assertEquals(expectedMethod, methods.get(0));
 }

 @Test
 public void testMethodsThatHaveWithMultipleMatches() throws Exception {
 final String methodName = "testMethod1";
 final String methodName2 = "testMethod2";
 final ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave(methodName);
 reflectionSearch.methodsThatHave(methodName2);
 final List<Method> methods = reflectionSearch.getMethods();
 final int expectedSize = 2;
 final Method method1 = testClass.getDeclaredMethod(methodName);
 final Method method2 = testClass.getDeclaredMethod(methodName2);

 assertEquals(expectedSize, methods.size());
 assertTrue(methods.contains(method1));
 assertTrue(methods.contains(method2));
 }

 @Test
 public void testMethodsThatDontHave() throws Exception {
 final String methodName = "testMethod";
 final ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatDontHave(methodName);
 final List<Method> methods = reflectionSearch.getMethods();
 final int expectedSize = 3;

 assertEquals(expectedSize, methods.size());
 assertFalse(methods.contains(testClass.getDeclaredMethod(methodName)));
 }

 @Test
 public void testMethodsThatDontHaveWithMultipleMatches() throws Exception {
 final String methodName1 = "testMethod1";
 final String methodName2 = "testMethod2";
 final ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatDontHave(methodName1);
 reflectionSearch.methodsThatDontHave(methodName2);
 final List<Method> methods = reflectionSearch.getMethods();
 final int expectedSize = 1;
 final Method method1 = testClass.getDeclaredMethod(methodName1);
 final Method method2 = testClass.getDeclaredMethod(methodName2);

 assertEquals(expectedSize, methods.size());
 assertFalse(methods.contains(method1));
 assertFalse(methods.contains(method2));
 }

 @Test
 public void testAnd() throws Exception {
 final String methodName1 = "testMethod1";
 final String methodName2 = "testMethod2";
 final ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave(methodName1);
 reflectionSearch.methodsThatDontHave(methodName2);
 reflectionSearch.and();
 final List<Method> methods = reflectionSearch.getMethods();
 final int expectedSize = 0;

 assertEquals(expectedSize, methods.size());
 }

 @Test
 public void testOr() throws Exception {
 final String methodName1 = "testMethod1";
 final String methodName2 = "testMethod2";
 final ReflectionSearch reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave(methodName1);
 reflectionSearch.methodsThatDontHave(methodName2);
 reflectionSearch.or();
 final List<Method> methods = reflectionSearch.getMethods();
 final int expectedSize = 3;
 final Method method1 = testClass.getDeclaredMethod(methodName1);
 final Method method2 = testClass.getDeclaredMethod("testMethod3");

 assertEquals(expectedSize, methods.size());
 assertTrue(methods.contains(method1));
 assertTrue(methods.contains(method2));
 }
}

class TestClassWithMethods {
 @TestAnnotation
 public void testMethod() {}

 @TestAnnotation
 public void testMethod1() {}

 public void testMethod2() {}

 public void testMethod3() {}

 @TestAnnotation
 public void testMethod4() {}
}