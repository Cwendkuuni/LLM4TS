package net.sourceforge.beanbin.reflect;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class testClass;

 @Before
 public void setup() {
 testClass = TestClass.class;
 reflectionSearch = new ReflectionSearch(testClass);
 }

 @Test
 public void testHasAnnotation() {
 reflectionSearch.methodsThatHave("AnnotationTest");
 assertTrue(reflectionSearch.hasAnnotation("AnnotationTest"));
 reflectionSearch.hasAnnotation("NonExistentAnnotation");
 assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
 }

 @Test
 public void testHasAnnotationWithWildcard() {
 reflectionSearch.methodsThatHave("Anno*");
 List<Method> methods = reflectionSearch.getMethods();
 List<String> expectedMethodNames = Arrays.asList("testAnnotatedMethod", "testAnnotationMethod");
 List<String> actualMethodNames = new ArrayList<>();
 for (Method method : methods) {
 actualMethodNames.add(method.getName());
 }
 assertTrue(expectedMethodNames.containsAll(actualMethodNames));
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatHaveWithNonExistentTerm() {
 reflectionSearch.methodsThatHave("NonExistentTerm");
 }

 @Test
 public void testMethodsThatDontHave() {
 reflectionSearch.methodsThatDontHave("NonExistentMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertTrue(methods.isEmpty());
 }

 @Test
 public void testMethodsThatHaveAndMethodsThatDontHave() {
 reflectionSearch.methodsThatHave("NonExistentMethod");
 reflectionSearch.methodsThatDontHave("testGetName");
 List<Method> methods = reflectionSearch.getMethods();
 List<String> expectedMethodNames = Arrays.asList("testAnotherMethod", "testGetAnotherName");
 List<String> actualMethodNames = new ArrayList<>();
 for (Method method : methods) {
 actualMethodNames.add(method.getName());
 }
 assertFalse(expectedMethodNames.containsAll(actualMethodNames));
 }

 @Test
 public void testAndCondition() {
 reflectionSearch.methodsThatHave("Test");
 reflectionSearch.and();
 reflectionSearch.methodsThatHave("Method");
 List<Method> methods = reflectionSearch.getMethods();
 List<String> expectedMethodNames = Arrays.asList("testTestMethod");
 List<String> actualMethodNames = new ArrayList<>();
 for (Method method : methods) {
 actualMethodNames.add(method.getName());
 }
 assertTrue(expectedMethodNames.containsAll(actualMethodNames));
 }

 @Test
 public void testOrCondition() {
 reflectionSearch.methodsThatHave("Test");
 reflectionSearch.or();
 reflectionSearch.methodsThatHave("Method");
 reflectionSearch.methodsThatHave("AnotherTest");
 List<Method> methods = reflectionSearch.getMethods();
 List<String> expectedMethodNames = Arrays.asList("testTestMethod", "testAnotherTestMethod");
 List<String> actualMethodNames = new ArrayList<>();
 for (Method method : methods) {
 actualMethodNames.add(method.getName());
 }
 assertTrue(expectedMethodNames.containsAll(actualMethodNames));
 }
}

class TestClass {
 @AnnotationTest
 public void testAnnotatedMethod() {}

 public void testAnnotationMethod() {}

 public void testAnotherMethod() {}

 public void testGetAnotherName() {}

 @InterfaceAnnotation
 public void testGetName() {}
}