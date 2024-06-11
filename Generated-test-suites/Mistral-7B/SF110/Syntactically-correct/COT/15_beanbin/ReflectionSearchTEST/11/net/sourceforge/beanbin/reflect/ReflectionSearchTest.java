package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import net.sourceforge.beanbin.Query;
import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.search.Criteria;
import net.sourceforge.beanbin.search.ReflectionCriteria;
import net.sourceforge.beanbin.search.Conditional;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;

 @Test
 public void testReflectionSearchInstance() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 assertNotNull(reflectionSearch);
 }

 @Test
 public void testHasAnnotation_EmptyAnnotation_ReturnsFalse() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 assertFalse(reflectionSearch.hasAnnotation(""));
 }

 @Test
 public void testHasAnnotation_MatchesAnnotationName() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(MyAnnotation.class);
 assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));
 }

 @Test
 public void testHasAnnotation_MatchesAnnotationClassName() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(MyAnnotation.class);
 assertTrue(reflectionSearch.hasAnnotation(MyAnnotation.class.getName()));
 }

 @Test
 public void testHasAnnotation_MatchesAnnotationTermWithWildcard() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(MyOtherAnnotation.class);
 assertTrue(reflectionSearch.hasAnnotation("My*Annotation"));
 }

 @Test
 public void testHasAnnotation_DoesNotMatchAnnotationTerm() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(MyAnnotation.class);
 assertFalse(reflectionSearch.hasAnnotation("MyOtherAnnotation"));
 }

 @Test
 public void testHasAnnotation_DoesNotMatchClassName() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(MyAnnotation.class);
 assertFalse(reflectionSearch.hasAnnotation("String"));
 }

 @Test
 public void testMethodsThatHave_ReturnsReflectionSearchObjectForChaining() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("test");
 assertSame(reflectionSearch, reflectionSearch.methodsThatHave("test"));
 }

 @Test
 public void testMethodsThatHave_ReturnsEmptyListWhenNoMethodsMatch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("nonExistentMethod");
 assertTrue(reflectionSearch.getMethods().isEmpty());
 }

 @Test
 public void testMethodsThatHave_ReturnsListContainingMethodsWhichMatch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("test");
 List<Method> methods = reflectionSearch.getMethods();
 List<String> methodNames = Arrays.asList("test1", "test2", "test3");
 List<Method> expectedMethods = new ArrayList<Method>();
 for (String methodName : methodNames) {
 expectedMethods.add(ReflectionUtil.getMethod(Object.class, methodName));
 }
 assertEquals(expectedMethods, methods);
 }

 @Test
 public void testMethodsThatDontHave_ReturnsReflectionSearchObjectForChaining() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatDontHave("test");
 assertSame(reflectionSearch, reflectionSearch.methodsThatDontHave("test"));
 }

 @Test
 public void testMethodsThatDontHave_ReturnsEmptyListWhenAllMethodsMatch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatDontHave("");
 assertTrue(reflectionSearch.getMethods().isEmpty());
 }

 @Test
 public void testMethodsThatDontHave_ReturnsListContainingMethodsWhichDoNotMatch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatDontHave("test");
 List<Method> methods = reflectionSearch.getMethods();
 List<Method> expectedMethods = new ArrayList<Method>();
 // Add methods which have names that do not match "test"
 expectedMethods.add(ReflectionUtil.getMethod(Object.class, "anotherMethod"));
 // Add methods which have the same name but different return types
 expectedMethods.add(ReflectionUtil.getMethod(Object.class, "test", int.class));
 // Add methods which have the same name but different parameter lists
 expectedMethods.add(ReflectionUtil.getMethod(Object.class, "test", int.class, String.class));
 assertEquals(expectedMethods, methods);
 }

 @Test
 public void testAndOperator() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("test");
 reflectionSearch.and();
 reflectionSearch.methodsThatDontHave("anotherTest");
 List<Method> methods = reflectionSearch.getMethods();
 List<Method> expectedMethods = new ArrayList<Method>();
 // Add methods which have names that match "test" but do not have names that match "anotherTest"
 expectedMethods.add(ReflectionUtil.getMethod(Object.class, "test1"));
 expectedMethods.add(ReflectionUtil.getMethod(Object.class, "test2"));
 // Add methods which have names that match "test" and "anotherTest" but are not the same method
 expectedMethods.add(ReflectionUtil.getMethod(Object.class, "testAnotherTest"));
 assertEquals(expectedMethods, methods);
 }

 @Test
 public void testOrOperator() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("test1");
 reflectionSearch.or();
 reflectionSearch.methodsThatHave("test2");
 List<Method> methods = reflectionSearch.getMethods();
 List<Method> expectedMethods = new ArrayList<Method>();
 // Add methods which have names that match "test1" or "test2"
 expectedMethods.add(ReflectionUtil.getMethod(Object.class, "test1"));
 expectedMethods.add(ReflectionUtil.getMethod(Object.class, "test2"));
 // Add methods which have names that match neither "test1" nor "test2"
 expectedMethods.add(ReflectionUtil.getMethod(Object.class, "anotherMethod"));
 assertEquals(expectedMethods, methods);
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatHave_ThrowsBeanBinExceptionWhenNoMethodsMatch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("nonExistentMethod");
 reflectionSearch.getMethods();
 fail("BeanBinException was not thrown");
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatDontHave_ThrowsBeanBinExceptionWhenAllMethodsMatch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatDontHave("");
 reflectionSearch.getMethods();
 fail("BeanBinException was not thrown");
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatDontHave_ThrowsBeanBinExceptionWhenNoMethodsMatchCriteria() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatDontHave("nonExistentMethodName");
 reflectionSearch.getMethods();
 fail("BeanBinException was not thrown");
 }

}