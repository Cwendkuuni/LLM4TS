package net.sourceforge.beanbin.reflect.test;

import static org.junit.Assert.*;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.beanbin.query.*;
import net.sourceforge.beanbin.search.*;
import net.sourceforge.beanbin.reflect.*;

import org.junit.Test;

public class ReflectionSearchTest {

 private static final ReflectionSearch TEST_REFLECTION_SEARCH;
 private static final Class<ReflectionSearch> TEST_CLASS;

 static {
 TEST_CLASS = ReflectionSearch.class;
 TEST_REFLECTION_SEARCH = new ReflectionSearch(TEST_CLASS);
 }

 @Test
 public void testHasAnnotation() throws Exception {
 final String annotationTerm = "AnnotationA";
 final boolean expectedResult = TEST_CLASS.getAnnotation(AnnotationA.class) != null;
 final boolean result = TEST_REFLECTION_Search.hasAnnotation(annotationTerm);
 assertTrue("hasAnnotation() should return true for exact match", expectedResult == result);

 final String annotationTermWildcard = "Annotation*";
 final boolean expectedResultWildcard = TEST_CLASS.isAnnotationPresent(AnnotationB.class);
 final boolean resultWildcard = TEST_REFLECTION_Search.hasAnnotation(annotationTermWildcard);
 assertTrue("hasAnnotation() should return true for wildcard match", expectedResultWildcard == resultWildcard);
 }

 @Test
 public void testMethodsThatHave() throws Exception {
 final String methodTerm = "getSomething";
 final ReflectionSearch reflectionSearch = TEST_REFLECTION_SEARCH.methodsThatHave(methodTerm);
 final List<Method> methods = reflectionSearch.getMethods();
 final Method expectedMethod = TEST_CLASS.getMethod("getSomething", new Class[0]);
 final List<Method> expectedMethods = Arrays.asList(expectedMethod);
 assertEquals("methodsThatHave() should return methods that match the given term", expectedMethods, methods);
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatHaveFail() throws Exception {
 final String methodTerm = "getSomethingElse";
 final ReflectionSearch reflectionSearch = TEST_REFLECTION_SEARCH.methodsThatHave(methodTerm);
 reflectionSearch.getMethods();
 fail("methodsThatHave() should throw an exception when no methods match the given term");
 }

 @Test
 public void testMethodsThatDontHave() throws Exception {
 final String methodTerm = "getSomething";
 final ReflectionSearch reflectionSearch = TEST_REFLECTION_SEARCH.methodsThatDontHave(methodTerm);
 final List<Method> methods = TEST_CLASS.getDeclaredMethods();
 final List<Method> methodsThatMatch = Collections.singletonList(TEST_CLASS.getMethod("setSomething", new Class[0]));
 final ReflectionSearch reflectionSearchExpectedMethods = TEST_REFLECTION_SEARCH.methodsThatHave(methodTerm).not();
 final List<Method> methodsThatDontMatch = reflectionSearchExpectedMethods.getMethods();
 assertEquals("methodsThatDontHave() should return methods that do not match the given term", methodsThatMatch, methodsThatDontMatch);
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatDontHaveFail() throws Exception {
 final String methodTerm = "getSomethingElse";
 final ReflectionSearch reflectionSearch = TEST_REFLECTION_SEARCH.methodsThatDontHave(methodTerm);
 reflectionSearch.getMethods();
 fail("methodsThatDontHave() should throw an exception when no methods match the given term");
 }

 @Test
 public void testAndOperatore() throws Exception {
 final String methodTerm1 = "getSomething";
 final String methodTerm2 = "setSomething";
 final ReflectionSearch reflectionSearch = TEST_REFLECTION_SEARCH.methodsThatHave(methodTerm1).and();
 reflectionSearch.methodsThatHave(methodTerm2);
 final List<Method> methods = reflectionSearch.getMethods();
 final List<Method> expectedMethods = Arrays.asList(TEST_CLASS.getMethod("getSomething", new Class[0]), TEST_CLASS.getMethod("setSomething", new Class[0]));
 assertEquals("and() should return methods that match both given terms", expectedMethods, methods);
 }

 @Test
 public void testOrOperator() throws Exception {
 final String methodTerm1 = "getSomething";
 final String methodTerm2 = "setSomethingElse";
 final ReflectionSearch reflectionSearch = TEST_REFLECTION_SEARCH.methodsThatHave(methodTerm1).or();
 reflectionSearch.methodsThatHave(methodTerm2);
 final List<Method> methods = reflectionSearch.getMethods();
 final List<Method> expectedMethods = Arrays.asList(TEST_CLASS.getMethod("getSomething", new Class[0]), TEST_CLASS.getMethod("setSomethingElse", new Class[0]));
 assertEquals("or() should return methods that match either given term", expectedMethods, methods);
 }

}