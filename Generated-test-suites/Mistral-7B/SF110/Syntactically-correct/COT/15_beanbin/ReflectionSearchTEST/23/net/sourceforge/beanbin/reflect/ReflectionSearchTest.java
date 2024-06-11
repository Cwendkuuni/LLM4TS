package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import org.junit.*;

import net.sourceforge.beanbin.query.*;
import net.sourceforge.beanbin.search.*;
import net.sourceforge.beanbin.reflect.ReflectionSearch;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class testClass;

 @Before
 public void setUp() throws Exception {
 testClass = TestClassWithAnnotations.class;
 reflectionSearch = new ReflectionSearch(testClass);
 }

 @Test
 public void testHasAnnotation_SimpleTerm() {
 assertTrue(reflectionSearch.hasAnnotation("TestAnnotation"));
 }

 @Test
 public void testHasAnnotation_ClassTerm() {
 assertTrue(reflectionSearch.hasAnnotation("TestAnnotationClass"));
 }

 @Test
 public void testHasAnnotation_ClassNameWithPackage() {
 assertTrue(reflectionSearch.hasAnnotation("net.sourceforge.beanbin.reflect.TestAnnotationClass"));
 }

 @Test
 public void testHasAnnotation_NoMatch() {
 assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
 }

 @Test
 public void testHasAnnotation_WildcardTerm() {
 assertTrue(reflectionSearch.hasAnnotation("Test*Annotation"));
 }

 @Test
 public void testHasAnnotation_WildcardClassName() {
 assertTrue(reflectionSearch.hasAnnotation("TestAnnotation*"));
 }

 @Test
 public void testMethodsThatHave_EmptyQuery() throws BeanBinException {
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testMethodsThatHave_SingleMatch() throws BeanBinException {
 reflectionSearch.methodsThatHave("testMethod");
 methodsThatHave_TestMethodIsPresentInClass();
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(1, methods.size());
 }

 @Test
 public void testMethodsThatHave_MultipleMatches() throws BeanBinException {
 reflectionSearch.methodsThatHave("test").and();
 reflectionSearch.methodsThatHave("Method");
 methodsThatHave_TestAndMethodArePresentInClass();
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(2, methods.size());
 }

 @Test
 public void testMethodsThatHave_NoMatches() throws BeanBinException {
 reflectionSearch.methodsThatHave("NonExistentMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatHave_NullClass() {
 reflectionSearch = new ReflectionSearch(null);
 reflectionSearch.methodsThatHave("testMethod");
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatHave_NullTerm() {
 reflectionSearch.methodsThatHave(null);
 }

 @Test
 public void testMethodsThatDontHave_EmptyQuery() throws BeanBinException {
 reflectionSearch.methodsThatDontHave("testMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testMethodsThatDontHave_SingleMatch() throws BeanBinException {
 reflectionSearch.methodsThatDontHave("testMethod");
 methodsThatHave_TestMethodIsPresentInClass();
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testMethodsThatDontHave_MultipleMatches() throws BeanBinException {
 reflectionSearch.methodsThatDontHave("test").or();
 reflectionSearch.methodsThatDontHave("Method");
 methodsThatHave_TestAndMethodArePresentInClass();
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(3, methods.size()); // Three methods in the class, but we should get two that match the query
 }

 @Test
 public void testMethodsThatDontHave_NoMatches() throws BeanBinException {
 reflectionSearch.methodsThatDontHave("NonExistentMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatDontHave_NullClass() {
 reflectionSearch = new ReflectionSearch(null);
 reflectionSearch.methodsThatDontHave("testMethod");
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatDontHave_NullTerm() {
 reflectionSearch.methodsThatDontHave(null);
 }

 @Test
 public void testAndOperator() throws BeanBinException {
 reflectionSearch.methodsThatHave("test").and();
 reflectionSearch.methodsThatHave("Method");
 methodsThatHave_TestAndMethodArePresentInClass();
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(1, methods.size()); // Only one method in the class matches both conditions
 }

 @Test
 public void testOrOperator() throws BeanBinException {
 reflectionSearch.methodsThatHave("test").or();
 reflectionSearch.methodsThatHave("Method");
 methodsThatHave_TestOrMethodArePresentInClass();
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(2, methods.size()); // Two methods in the class match either condition
 }

 private void methodsThatHave_TestAndMethodArePresentInClass() throws Exception {
 Method testMethod = testClass.getMethod("testMethod");
 Method testMethod2 = testClass.getMethod("testMethod2");
 Method testMethod3 = testClass.getMethod("testMethod3");
 assertNotNull(testMethod);
 assertNotNull(testMethod2);
 assertNotNull(testMethod3);
 }

 private void methodsThatHave_TestOrMethodArePresentInClass() throws Exception {
 Method testMethod = testClass.getMethod("testMethod");
 Method testMethod2 = testClass.getMethod("testMethod2");
 Method testMethod3 = testClass.getMethod("testMethod3");
 assertNotNull(testMethod);
 assertNotNull(testMethod2);
 assertNotNull(testMethod3);
 }

}