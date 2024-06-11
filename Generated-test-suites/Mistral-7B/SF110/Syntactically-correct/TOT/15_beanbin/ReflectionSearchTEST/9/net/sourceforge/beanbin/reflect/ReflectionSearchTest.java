package net.sourceforge.beanbin.reflect;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class testClass;

 @Before
 public void setup() {
 testClass = AnnotatedElementTest.class;
 reflectionSearch = new ReflectionSearch(testClass);
 }

 @Test
 public void testHasAnnotationWithExactMatch() {
 Annotation annotation = testClass.getAnnotation(RetainAnnotations.class);
 assertTrue(reflectionSearch.hasAnnotation("RetainAnnotations"));
 }

 @Test
 public void testHasAnnotationWithClassMatch() {
 assertTrue(reflectionSearch.hasAnnotation("Annotation"));
 }

 @Test
 public void testHasAnnotationWithWildcardMatch() {
 assertTrue(reflectionSearch.hasAnnotation("Annotat*"));
 }

 @Test
 public void testHasAnnotationWithNoMatch() {
 assertFalse(reflectionSearch.hasAnnotation("RandomString"));
 }

 @Test
 public void testMethodsThatHaveWithExactMatch() throws BeanBinException {
 reflectionSearch.methodsThatHave("test");
 List<Method> methods = reflectionSearch.getMethods();
 List<String> expectedMethodNames = Arrays.asList("test1", "test2", "test3");
 List<String> actualMethodNames = new ArrayList<>();
 for (Method method : methods) {
 actualMethodNames.add(method.getName());
 }
 assertTrue(Collections.equals(expectedMethodNames, actualMethodNames));
 }

 @Test
 public void testMethodsThatHaveWithWildcardMatch() throws BeanBinException {
 reflectionSearch.methodsThatHave("tes*");
 List<Method> methods = reflectionSearch.getMethods();
 List<String> expectedMethodNames = Arrays.asList("test1", "test2", "test3");
 List<String> actualMethodNames = new ArrayList<>();
 for (Method method : methods) {
 actualMethodNames.add(method.getName());
 }
 assertTrue(Collections.equals(expectedMethodNames, actualMethodNames));
 }

 @Test
 public void testMethodsThatDontHaveWithExactMatch() throws BeanBinException {
 reflectionSearch.methodsThatDontHave("test");
 List<Method> methods = reflectionSearch.getMethods();
 assertTrue(methods.isEmpty());
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatDontHaveWithNoSuchMethod() throws BeanBinException {
 reflectionSearch.methodsThatDontHave("nonExistentMethod");
 }

 @Test
 public void testAnd() throws BeanBinException {
 reflectionSearch.methodsThatHave("test");
 reflectionSearch.methodsThatHave("public");
 reflectionSearch.and();
 List<Method> methods = reflectionSearch.getMethods();
 List<String> expectedMethodNames = Arrays.asList("test1", "test2");
 List<String> actualMethodNames = new ArrayList<>();
 for (Method method : methods) {
 actualMethodNames.add(method.getName());
 }
 assertTrue(Collections.equals(expectedMethodNames, actualMethodNames));
 }

 @Test
 public void testOr() throws BeanBinException {
 reflectionSearch.methodsThatHave("test");
 reflectionSearch.methodsThatDontHave("private");
 reflectionSearch.or();
 List<Method> methods = reflectionSearch.getMethods();
 List<String> expectedMethodNames = Arrays.asList("test1", "test2", "test3");
 List<String> actualMethodNames = new ArrayList<>();
 for (Method method : methods) {
 actualMethodNames.add(method.getName());
 }
 assertTrue(Collections.equals(expectedMethodNames, actualMethodNames));
 }
}