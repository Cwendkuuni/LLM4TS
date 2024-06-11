package net.sourceforge.beanbin.reflect;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class testClass;

 @Before
 public void setup() throws Exception {
 testClass = TestClassWithAnnotations.class;
 reflectionSearch = new ReflectionSearch(testClass);
 }

 @Test
 public void testHasAnnotation() throws Exception {
 // Test case for method hasAnnotation with a valid annotation name
 String annotationName = "TestAnnotation";
 assertTrue(reflectionSearch.hasAnnotation(annotationName));

 // Test case for method hasAnnotation with an invalid annotation name
 String invalidAnnotationName = "NonExistentAnnotation";
 assertFalse(reflectionSearch.hasAnnotation(invalidAnnotationName));
 }

 @Test
 public void testMethodsThatHave() throws Exception {
 // Test case for method methodsThatHave with a valid method name
 String methodName = "testMethod";
 reflectionSearch.methodsThatHave(methodName);
 List<Method> methods = reflectionSearch.getMethods();
 assertTrue(methods.size() > 0);
 Method expectedMethod = testClass.getMethod(methodName);
 assertTrue(methods.contains(expectedMethod));

 // Test case for method methodsThatHave with an invalid method name
 String invalidMethodName = "NonExistentMethod";
 reflectionSearch.methodsThatHave(invalidMethodName);
 List<Method> emptyMethods = reflectionSearch.getMethods();
 assertTrue(emptyMethods.isEmpty());
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatHaveWithException() throws Exception {
 // Test case for method methodsThatHave with a null class
 reflectionSearch = new ReflectionSearch(null);
 reflectionSearch.methodsThatHave("testMethod");
 }

 @Test
 public void testMethodsThatDontHave() throws Exception {
 // Test case for method methodsThatDontHave with a valid method name
 String methodName = "testAnotherMethod";
 reflectionSearch.methodsThatDontHave(methodName);
 List<Method> methods = reflectionSearch.getMethods();
 assertFalse(methods.contains(testClass.getMethod(methodName)));

 // Test case for method methodsThatDontHave with an invalid method name
 String invalidMethodName = "NonExistentMethod";
 reflectionSearch.methodsThatDontHave(invalidMethodName);
 List<Method> emptyMethods = reflectionSearch.getMethods();
 assertTrue(emptyMethods.isEmpty());
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatDontHaveWithException() throws Exception {
 // Test case for method methodsThatDontHave with a null class
 reflectionSearch = new ReflectionSearch(null);
 reflectionSearch.methodsThatDontHave("testMethod");
 }

 @Test
 public void testAndOperator() throws Exception {
 // Test case for and operator with a valid method name
 reflectionSearch.methodsThatHave("testMethod1");
 reflectionSearch.and();
 reflectionSearch.methodsThatHave("testMethod2");
 List<Method> methods = reflectionSearch.getMethods();
 List<String> expectedMethodsNames = Arrays.asList("testMethod1", "testMethod2");
 List<String> methodsNames = new ArrayList<>();
 for (Method method : methods) {
 methodsNames.add(method.getName());
 }
 assertTrue(methodsNames.equals(expectedMethodsNames));
 }

 @Test
 public void testOrOperator() throws Exception {
 // Test case for or operator with a valid method name
 reflectionSearch.methodsThatHave("testMethod1");
 reflectionSearch.or();
 reflectionSearch.methodsThatDontHave("testMethod3");
 List<Method> methods = reflectionSearch.getMethods();
 List<String> expectedMethodsNames = Arrays.asList("testMethod1");
 List<String> methodsNames = new ArrayList<>();
 for (Method method : methods) {
 methodsNames.add(method.getName());
 }
 assertTrue(methodsNames.equals(expectedMethodsNames));
 }
}

class TestClassWithAnnotations {
 @TestAnnotation
 public void testMethod() {
 // Test method implementation
 }

 @NonExistentAnnotation
 public void testAnotherMethod() {
 // Test method implementation
 }
}