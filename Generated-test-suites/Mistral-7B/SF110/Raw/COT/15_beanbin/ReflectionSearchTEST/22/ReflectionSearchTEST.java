package net.sourceforge.beanbin.reflect.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.search.*;
import org.junit.Test;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 reflectionSearch = new ReflectionSearch(Object.class);
 }

 @Test
 public void testHasAnnotation_EmptyAnnotations_ReturnsFalse() {
 assertFalse(reflectionSearch.hasAnnotation(""));
 }

 @Test
 public void testHasAnnotation_MatchesAnnotationName_ReturnsTrue() {
 reflectionSearch = new ReflectionSearch(AnnotationTest.class);
 assertTrue(reflectionSearch.hasAnnotation("AnnotationTest"));
 }

 @Test
 public void testHasAnnotation_MatchesClassAnnotationName_ReturnsTrue() {
 reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);
 assertTrue(reflectionSearch.hasAnnotation("AnnotationTestClass"));
 }

 @Test
 public void testHasAnnotation_MatchesAnnotationWithWildcard_ReturnsTrue() {
 reflectionSearch = new ReflectionSearch(TestAnnotation.class);
 assertTrue(reflectionSearch.hasAnnotation("Annota*"));
 }

 @Test(expected = BeanBinException.class)
 public void testHasAnnotation_ThrowsException_WhenClassHasNoSuchAnnotation() {
 reflectionSearch = new ReflectionSearch(NonExistentAnnotation.class);
 fail("Should have thrown BeanBinException");
 }

 @Test
 public void testMethodsThatHave_EmptyTerm_ReturnsOriginalObject() {
 List<Method> methods = reflectionSearch.getMethods();
 assertNotNull(methods);
 assertEquals(0, methods.size());
 }

 @Test
 public void testMethodsThatHave_MatchesMethodName_ReturnsMethodsWithMatchingName() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(TestClassWithMethods.class);
 reflectionSearch.methodsThatHave("testMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(1, methods.size());
 Method method = methods.get(0);
 assertEquals("testMethod", method.getName());
 }

 @Test
 public void testMethodsThatHave_MatchesMethodNameWithWildcard_ReturnsMethodsWithMatchingNameStartingWithGivenString() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(TestClassWithMethods.class);
 reflectionSearch = reflectionSearch.methodsThatHave("test*");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(2, methods.size());
 Method method1 = methods.get(0);
 assertEquals("testMethod1", method1.getName());
 Method method2 = methods.get(1);
 assertEquals("testMethod2", method2.getName());
 }

 @Test
 public void testMethodsThatHave_MatchesMethodNameWithDifferentCase_ReturnsMethodsWithMatchingName() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(TestClassWithMethods.class);
 reflectionSearch = reflectionSearch.methodsThatHave("TeStMeThOd");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(1, methods.size());
 Method method = methods.get(0);
 assertEquals("testMethod", method.getName());
 }

 @Test
 public void testMethodsThatDontHave_EmptyTerm_ReturnsOriginalObject() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(TestClassWithMethods.class);
 reflectionSearch.methodsThatDontHave("");
 List<Method> methods = reflectionSearch.getMethods();
 assertNotNull(methods);
 assertEquals(3, methods.size());
 }

 @Test
 public void testMethodsThatDontHave_MatchesMethodName_ReturnsMethodsWithoutMatchingName() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(TestClassWithMethods.class);
 reflectionSearch.methodsThatDontHave("testMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(2, methods.size());
 Method method1 = methods.get(0);
 assertNotEquals("testMethod", method1.getName());
 Method method2 = methods.get(1);
 assertNotEquals("testMethod", method2.getName());
 }

 @Test
 public void testAndOperator() throws BeanBinException {
 List<Method> methods = new ArrayList<>();
 methods.add(new MethodReflectionCriteria(Object.class, "testMethod1", SearchType.EQUALS).getResults());
 methods.add(new MethodReflectionCriteria(Object.class, "testMethod2", SearchType.EQUALS).getResults());
 reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("testMethod1");
 reflectionSearch.and();
 reflectionSearch.methodsThatHave("testMethod2");
 List<Method> resultMethods = reflectionSearch.getMethods();
 assertEquals(0, resultMethods.size());
 }

 @Test
 public void testOrOperator() throws BeanBinException {
 List<Method> methods1 = new ArrayList<>();
 methods1.add(new MethodReflectionCriteria(Object.class, "testMethod1", SearchType.EQUALS).getResults());
 reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("testMethod1");
 List<Method> resultMethods1 = reflectionSearch.getMethods();
 assertEquals(1, resultMethods1.size());

 List<Method> methods2 = new ArrayList<>();
 methods2.add(new MethodReflectionCriteria(Object.class, "testMethod2", SearchType.EQUALS).getResults());
 reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("testMethod2");
 List<Method> resultMethods2 = reflectionSearch.getMethods();
 assertEquals(1, resultMethods2.size());

 reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("testMethod1");
 reflectionSearch.or();
 reflectionSearch.methodsThatHave("testMethod2");
 List<Method> resultMethods = reflectionSearch.getMethods();
 assertEquals(2, resultMethods.size());
 assertTrue(Arrays.asList(resultMethods).containsAll(Arrays.asList(resultMethods1.get(0), resultMethods2.get(0))));
 }
}