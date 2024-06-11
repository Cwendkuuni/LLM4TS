package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.beanbin.search.*;
import net.sourceforge.beanbin.query.*;

import org.junit.Test;

public class ReflectionSearchTest {

 private static final String ANNOTATION_TEST = "TestAnnotation";
 private static final String ANNOTATION_TEST_2 = "TestAnnotation2";
 private static final String WILDCARD_TEST = "TestAnnotat*";
 private static final String WILDCARD_TEST_2 = "TestAnnotat*2";
 private static final String METHOD_TEST = "testMethod";
 private static final String METHOD_TEST_2 = "testOtherMethod";
 private static final String METHOD_TEST_3 = "testAnotherMethod";

 @Test
 public void testHasAnnotation() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);

 // Test when annotation is present
 assertTrue(reflectionSearch.hasAnnotation("TestAnnotation"));

 // Test when annotation is present but term contains wildcard
 assertTrue(reflectionSearch.hasAnnotation(WILDCARD_TEST));

 // Test when annotation is not present
 assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));

 reflectionSearch = new ReflectionSearch(AnnotationTestClassWithAnnotations2.class);

 // Test when annotation is present with different package
 assertTrue(reflectionSearch.hasAnnotation("TestAnnotation2"));

 // Test when annotation is present but term contains wildcard
 assertTrue(reflectionSearch.hasAnnotation(WILDCARD_TEST_2));
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatHaveException() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);
 reflectionSearch.methodsThatHave("NonExistentMethod");
 }

 @Test
 public void testMethodsThatHave() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);
 reflectionSearch.methodsThatHave(METHOD_TEST);
 List<Method> methods = reflectionSearch.getMethods();

 // Test when method is present
 assertTrue(methods.contains(AnnotationTestClass.class.getMethod(METHOD_TEST)));

 // Test when method is not present
 assertFalse(methods.contains(AnnotationTestClass.class.getMethod(METHOD_TEST_2)));
 }

 @Test
 public void testMethodsThatDontHave() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);
 reflectionSearch.methodsThatDontHave(METHOD_TEST);
 List<Method> methods = reflectionSearch.getMethods();

 // Test when method is not present
 assertFalse(methods.contains(AnnotationTestClass.class.getMethod(METHOD_TEST)));

 // Test when method is present
 assertTrue(methods.contains(AnnotationTestClass.class.getMethod(METHOD_TEST_3)));
 }

 @Test
 public void testAnd() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);
 reflectionSearch.methodsThatHave(METHOD_TEST);
 reflectionSearch.and();
 reflectionSearch.methodsThatHave("get");
 List<Method> methods = reflectionSearch.getMethods();

 // Test when method name starts with "get" and method name equals "testMethod"
 assertTrue(methods.contains(AnnotationTestClass.class.getMethod("getTestMethod")));
 }

 @Test
 public void testOr() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);
 reflectionSearch.methodsThatHave(METHOD_TEST);
 reflectionSearch.or();
 reflectionSearch.methodsThatHave("set");
 List<Method> methods = reflectionSearch.getMethods();

 // Test when method name starts with "get" or method name equals "testMethod"
 assertTrue(methods.contains(AnnotationTestClass.class.getMethod("getTestMethod")) || methods.contains(AnnotationTestClass.class.getMethod("setTestMethod")));
 }

}

 class AnnotationTestClass {
 @TestAnnotation
 public void testMethod() {
 // Test method
 }
}

 class AnnotationTestClassWithAnnotations2 {
 @TestAnnotation
 @TestAnnotation2
 public void testOtherMethod() {
 // Test method
 }
}