package net.sourceforge.beanbin.reflect;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
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
 Assert.assertTrue(reflectionSearch.hasAnnotation("AnnotationTest"));
 reflectionSearch.hasAnnotation("NonExistent");
 Assert.assertFalse(reflectionSearch.hasAnnotation("NonExistent"));
 }

 @Test
 public void testHasWildcard() {
 reflectionSearch.methodsThatHave("Anno*");
 reflectionSearch.hasAnnotation("AnnotationTest");
 reflectionSearch.hasAnnotation("AnotherAnnotation");
 Assert.assertTrue(reflectionSearch.hasAnnotation("AnnotationTest") || reflectionSearch.hasAnnotation("AnotherAnnotation"));
 reflectionSearch.hasAnnotation("NonExistent");
 Assert.assertFalse(reflectionSearch.hasAnnotation("NonExistent"));
 }

 @Test
 public void testMethodsThatHave() throws BeanBinException {
 List<Method> methodsThatHave = reflectionSearch.getMethods();
 Method expectedMethod = testClass.getMethod("testAnnotationMethod");
 Assert.assertTrue(methodsThatHave.contains(expectedMethod));
 methodsThatHave = reflectionSearch.methodsThatHave("NonExistent");
 Assert.assertTrue(methodsThatHave.isEmpty());
 }

 @Test
 public void testMethodsThatDontHave() throws BeanBinException {
 List<Method> methodsThatDontHave = reflectionSearch.getMethods();
 Method expectedMethod = testClass.getMethod("testAnnotationMethod");
 methodsThatDontHave = reflectionSearch.methodsThatDontHave(expectedMethod.getName());
 Assert.assertFalse(methodsThatDontHave.contains(expectedMethod));
 methodsThatDontHave = reflectionSearch.methodsThatDontHave("NonExistent");
 Assert.assertTrue(methodsThatDontHave.isEmpty());
 }

 @Test
 public void testAndCondition() throws BeanBinException {
 reflectionSearch.methodsThatHave("Anno*");
 List<Method> methodsThatHaveAnnotation = reflectionSearch.getMethods();
 reflectionSearch.methodsThatDontHave("get*");
 methodsThatHaveAnnotation = reflectionSearch.getMethods();
 Assert.assertEquals(methodsThatHaveAnnotation.size(), 1);
 Method expectedMethod = testClass.getMethod("testAnnotationWithGet");
 Assert.assertTrue(methodsThatHaveAnnotation.contains(expectedMethod));
 methodsThatHaveAnnotation = reflectionSearch.methodsThatHave("Anno*");
 methodsThatHaveAnnotation = reflectionSearch.and().methodsThatDontHave("get*").getMethods();
 Assert.assertTrue(methodsThatHaveAnnotation.isEmpty());
 }

 @Test
 public void testOrCondition() throws BeanBinException {
 reflectionSearch.methodsThatHave("Anno*");
 List<Method> methodsThatHaveAnnotation = reflectionSearch.getMethods();
 reflectionSearch.methodsThatDontHave("get*");
 methodsThatHaveAnnotation = reflectionSearch.getMethods();
 reflectionSearch.or().methodsThatHave("set*");
 methodsThatHaveAnnotation = reflectionSearch.getMethods();
 Assert.assertEquals(methodsThatHaveAnnotation.size(), 2);
 Method expectedMethod1 = testClass.getMethod("testAnnotationWithGet");
 Method expectedMethod2 = testClass.getMethod("testAnnotationWithSet");
 Assert.assertTrue(methodsThatHaveAnnotation.contains(expectedMethod1) || methodsThatHaveAnnotation.contains(expectedMethod2));
 methodsThatHaveAnnotation = reflectionSearch.methodsThatHave("Anno*");
 methodsThatHaveAnnotation = reflectionSearch.or().methodsThatDontHave("get*").getMethods();
 Assert.assertTrue(methodsThatHaveAnnotation.contains(expectedMethod1) || methodsThatHaveAnnotation.contains(expectedMethod2));
 }

 @Test(expected = BeanBinException.class)
 public void testInvalidQuery() throws BeanBinException {
 reflectionSearch.methodsThatHave("*NonExistent*");
 }
}

class TestClass {
 @AnnotationTest
 public void testAnnotationMethod() {
 }

 public void testAnnotationWithGet() {
 }

 public void testAnnotationWithSet() {
 }
}