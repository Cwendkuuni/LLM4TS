package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import net.sourceforge.beanbin.query.*;
import net.sourceforge.beanbin.search.*;
import org.junit.*;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class testClass;

 @Before
 public void setup() throws Exception {
 testClass = SampleClassWithAnnotations.class;
 reflectionSearch = new ReflectionSearch(testClass);
 }

 @Test
 public void testHasAnnotation_EmptyTerm() {
 assertFalse(reflectionSearch.hasAnnotation(""));
 }

 @Test
 public void testHasAnnotation_SingleMatch() {
 assertTrue(reflectionSearch.hasAnnotation("AnnotationName"));
 }

 @Test
 public void testHasAnnotation_MultipleMatches() {
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.hasAnnotation("Anno*");
 assertTrue(reflectionSearch.hasAnnotation("AnnotationName"));
 assertTrue(reflectionSearch.hasAnnotation("AnotherAnnotationName"));
 }

 @Test
 public void testHasAnnotation_NoMatch() {
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.hasAnnotation("NonExistentAnnotation");
 assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
 }

 @Test
 public void testMethodsThatHave_EmptyTerm() throws BeanBinException {
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testMethodsThatHave_SingleMatch() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("MethodName");
 methodsThatHave_Verification(methodsThatHave_SingleMatch, "MethodName");
 }

 @Test
 public void testMethodsThatHave_MultipleMatches() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("Met*");
 methodsThatHave_Verification(methodsThatHave_MultipleMatches, "MethodOne", "MethodTwo");
 }

 @Test
 public void testMethodsThatHave_NoMatch() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("NonExistentMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testMethodsThatDontHave_EmptyTerm() throws BeanBinException {
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(testClass.getMethods().length, methods.size());
 }

 @Test
 public void testMethodsThatDontHave_SingleMatch() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatDontHave("MethodName");
 methodsThatDontHave_Verification(methodsThatDontHave_SingleMatch, "MethodName");
 }

 @Test
 public void testMethodsThatDontHave_MultipleMatches() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatDontHave("Met*");
 methodsThatDontHave_Verification(methodsThatDontHave_MultipleMatches, "MethodThree", "MethodFour");
 }

 @Test
 public void testMethodsThatDontHave_AllMatches() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("*");
 reflectionSearch.methodsThatDontHave("MethodOne");
 reflectionSearch.methodsThatDontHave("MethodTwo");
 methodsThatDontHave_Verification(methodsThatDontHave_AllMatches, "MethodThree", "MethodFour");
 }

 @Test
 public void testAndOperator() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("MethodName");
 reflectionSearch.and();
 reflectionSearch.methodsThatDontHave("MethodOne");
 methodsThatAnd_Verification(methodsThatAnd_SingleMatch);
 }

 @Test
 public void testOrOperator() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("MethodName1");
 reflectionSearch.or();
 reflectionSearch.methodsThatDontHave("MethodName2");
 methodsThatOr_Verification(methodsThatOr_SingleMatch);
 }

 @Test
 public void testQueryWithEmptySearch() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(testClass);
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test(expected = BeanBinException.class)
 public void testQueryWithNullSearch() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.query = null;
 reflectionSearch.getMethods();
 }

 private void methodsThatHave_Verification(List<Method> expectedMethods, String... methodNames) {
 List<Method> methods = reflectionSearch.getMethods();
 for (String methodName : methodNames) {
 assertTrue(methods.contains(testClass.getMethod(methodName)));
 }
 assertEquals(methodNames.length, methods.size());
 }

 private void methodsThatDontHave_Verification(List<Method> expectedMethods, String... methodNames) {
 List<Method> methods = reflectionSearch.getMethods();
 for (String methodName : methodNames) {
 assertFalse(methods.contains(testClass.getMethod(methodName)));
 }
 assertEquals(testClass.getMethods().length, methods.size());
 }

 private void methodsThatAnd_Verification(List<Method> expectedMethods) {
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(expectedMethods.size(), methods.size());
 for (Method method : methods) {
 assertTrue(expectedMethods.contains(method));
 }
 }

 private void methodsThatOr_Verification(List<Method> expectedMethods) {
 List<Method> methods = reflectionSearch.getMethods();
 for (Method method : expectedMethods) {
 assertTrue(methods.contains(method));
 }
 for (Method method : methods) {
 if (!expectedMethods.contains(method)) {
 fail("Method ['" + method.getName() + "'] was not expected.");
 }
 }
 assertEquals(expectedMethods.size(), methods.size());
 }
}