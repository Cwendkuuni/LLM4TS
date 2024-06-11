package net.sourceforge.beanbin.reflect.test;

import static org.junit.Assert.*;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import net.sourceforge.beanbin.*;
import net.sourceforge.beanbin.query.*;
import net.sourceforge.beanbin.reflect.*;

import org.junit.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class testClass;

 @Before
 public void setup() throws Exception {
 testClass = Class.forName("net.sourceforge.beanbin.reflect.test.TestClass");
 reflectionSearch = new ReflectionSearch(testClass);
 }

 @Test
 public void testHasAnnotation_ExactMatch() {
 assertTrue(reflectionSearch.hasAnnotation("TestClass"));
 }

 @Test
 public void testHasAnnotation_AnnotationNameMatch() {
 Annotation annotation = testClass.getAnnotation(MyAnnotation.class);
 assertTrue(reflectionSearch.hasAnnotation(annotation.annotationType().getName()));
 }

 @Test
 public void testHasAnnotation_ClassNameMatch() {
 assertTrue(reflectionSearch.hasAnnotation(MyAnnotation.class.getName()));
 }

 @Test
 public void testHasAnnotation_NoMatch() {
 assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
 }

 @Test
 public void testHasWildcard_NoWildcard() {
 assertFalse(reflectionSearch.hasWildcard("NoWildcard"));
 }

 @Test
 public void testHasWildcard_SingleWildcard() {
 assertTrue(reflectionSearch.hasWildcard("TestClass*"));
 assertFalse(reflectionSearch.hasWildcard("TestClassNoWildcard"));
 }

 @Test
 public void testHasWildcard_MultipleWildcards() {
 assertTrue(reflectionSearch.hasWildcard("TestClass**"));
 assertFalse(reflectionSearch.hasWildcard("TestClassNoWildcard"));
 }

 @Test
 public void testMethodsThatHave_EmptyQuery() throws BeanBinException {
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testMethodsThatHave_SingleMatch() throws BeanBinException {
 reflectionSearch.methodsThatHave("testMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(1, methods.size());
 assertEquals("testMethod", methods.get(0).getName());
 }

 @Test
 public void testMethodsThatHave_MultipleMatches() throws BeanBinException {
 reflectionSearch.methodsThatHave("test");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(2, methods.size());
 assertTrue(methods.contains(testClass.getMethod("testMethod1")));
 assertTrue(methods.contains(testClass.getMethod("testMethod2")));
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatHave_NoMatches() throws BeanBinException {
 reflectionSearch.methodsThatHave("NoSuchMethod");
 }

 @Test
 public void testMethodsThatDontHave_EmptyQuery() throws BeanBinException {
 reflectionSearch.methodsThatDontHave("");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testMethodsThatDontHave_SingleMatch() throws BeanBinException {
 reflectionSearch.methodsThatDontHave("testMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(1, methods.size());
 assertFalse(methods.contains(testClass.getMethod("testMethod1")));
 }

 @Test
 public void testMethodsThatDontHave_MultipleMatches() throws BeanBinException {
 reflectionSearch.methodsThatDontHave("test");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(1, methods.size());
 assertFalse(methods.contains(testClass.getMethod("testMethod1")));
 assertFalse(methods.contains(testClass.getMethod("testMethod2")));
 }

 @Test
 public void testMethodsThatDontHave_NoMatches() throws BeanBinException {
 reflectionSearch.methodsThatDontHave("NonExistentMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testAnd() throws BeanBinException {
 reflectionSearch.methodsThatHave("prefix");
 reflectionSearch.and();
 reflectionSearch.methodsThatDontHave("suffix");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testOr() throws BeanBinException {
 reflectionSearch.methodsThatHave("prefix");
 reflectionSearch.or();
 reflectionSearch.methodsThatDontHave("prefix");
 reflectionSearch.methodsThatHave("suffix");
 List<Method> methods = reflectionSearch.getMethods();
 assertTrue(methods.size() > 0);
 }

}