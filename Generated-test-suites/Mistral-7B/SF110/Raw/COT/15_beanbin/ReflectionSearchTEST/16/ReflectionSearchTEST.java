package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.beanbin.*;
import net.sourceforge.beanbin.query.*;
import net.sourceforge.beanbin.reflect.ReflectionSearch;
import org.junit.Before;
import org.junit.Test;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class testClass;

 @Before
 public void setup() {
 testClass = TestClassWithMethods.class;
 reflectionSearch = new ReflectionSearch(testClass);
 }

 @Test
 public void testHasAnnotationWithNoWildcard() {
 String term = "Test";
 assertTrue(reflectionSearch.hasAnnotation(term));
 }

 @Test
 public void testHasAnnotationWithWildcard() {
 String term = "Test*";
 assertTrue(reflectionSearch.hasAnnotation(term));
 }

 @Test
 public void testHasAnnotationWithNoMatch() {
 String term = "AnotherClass";
 assertFalse(reflectionSearch.hasAnnotation(term));
 }

 @Test
 public void testMethodsThatHaveWithSingleTerm() throws BeanBinException {
 reflectionSearch.methodsThatHave("testMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertNotNull(methods);
 assertTrue(methods.size() > 0);
 Method method = methods.get(0);
 assertTrue(method.getName().equals("testMethod"));
 }

 @Test
 public void testMethodsThatHaveWithMultipleTerms() throws BeanBinException {
 reflectionSearch.methodsThatHave("test", "Method");
 List<Method> methods = reflectionSearch.getMethods();
 assertNotNull(methods);
 assertTrue(methods.size() > 0);
 for (Method method : methods) {
 String methodName = method.getName();
 assertTrue(methodName.startsWith("test") && methodName.endsWith("Method"));
 }
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatHaveWithNoMatch() throws BeanBinException {
 reflectionSearch.methodsThatHave("NonExistentMethod");
 }

 @Test
 public void testMethodsThatDontHaveWithSingleTerm() throws BeanBinException {
 reflectionSearch.methodsThatDontHave("testMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertNotNull(methods);
 assertTrue(methods.size() > 0);
 for (Method method : methods) {
 assertFalse(method.getName().equals("testMethod"));
 }
 }

 @Test
 public void testMethodsThatDontHaveWithMultipleTerms() throws BeanBinException {
 reflectionSearch.methodsThatDontHave("test", "Method");
 List<Method> methods = reflectionSearch.getMethods();
 assertNotNull(methods);
 assertTrue(methods.size() > 0);
 for (Method method : methods) {
 String methodName = method.getName();
 assertFalse(methodName.startsWith("test") && methodName.endsWith("Method"));
 }
 }

 @Test
 public void testAndOperators() throws BeanBinException {
 reflectionSearch.methodsThatHave("test");
 reflectionSearch.methodsThatDontHave("Method");
 List<Method> methods = reflectionSearch.getMethods();
 assertNotNull(methods);
 assertTrue(methods.size() == 0);
 }

 @Test
 public void testOrOperators() throws BeanBinException {
 reflectionSearch.methodsThatDontHave("test");
 reflectionSearch.methodsThatHave("Method");
 List<Method> methods = reflectionSearch.getMethods();
 assertNotNull(methods);
 assertTrue(methods.size() > 0);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMethodsNullQuery() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(null);
 reflectionSearch.getMethods();
 }

 @Test
 public void testGetMethodsEmptyQuery() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(testClass);
 List<Method> methods = reflectionSearch.getMethods();
 assertNotNull(methods);
 assertTrue(methods.size() == 0);
 }

 @Test
 public void testReflectionsShelf() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("test");
 List<Method> methods = reflectionSearch.getMethods();
 ReflectionSearch reflectionSearch2 = new ReflectionSearch(testClass);
 reflectionSearch2.methodsThatHave("Method");
 List<Method> methods2 = reflectionSearch2.getMethods();
 reflectionSearch.query.addAll(reflectionSearch2.query.getCriterias());
 List<Method> mergedMethods = reflectionSearch.getMethods();
 assertNotNull(mergedMethods);
 assertTrue(mergedMethods.size() > 0);
 for (Method method : mergedMethods) {
 if (methods.contains(method)) {
 continue;
 }
 if (methods2.contains(method)) {
 assertTrue(methods.containsAll(Arrays.asList(method)));
 assertTrue(methods2.containsAll(Arrays.asList(method)));
 }
 }
 }
}

class TestClassWithMethods {
 @TestAnnotation1
 public void testMethod1() {
 }

 @TestAnnotation2
 public void testMethod2() {
 }

 public void testMethod3() {
 }
}