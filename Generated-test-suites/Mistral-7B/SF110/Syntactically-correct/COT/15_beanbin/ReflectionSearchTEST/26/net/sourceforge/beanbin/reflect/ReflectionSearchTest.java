package net.sourceforge.beanbin.reflect.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.search.BeanBinException;
import net.sourceforge.beanbin.search.Criteria;
import net.sourceforge.beanbin.search.ReflectionCriteria;
import net.sourceforge.beanbin.search.Conditional;

import org.junit.Test;

public class ReflectionSearchTest {

 private static final String CLASS_NAME = "net.sourceforge.beanbin.reflect.ReflectionSearch";

 @Test
 public void testConstructor() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 assertNotNull(reflectionSearch);
 }

 @Test
 public void testHasAnnotation_ExactMatch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(String[].class);
 assertTrue(reflectionSearch.hasAnnotation("String"));
 }

 @Test
 public void testHasAnnotation_AnnotationNameMatch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(String[].class);
 assertTrue(reflectionSearch.hasAnnotation("Annotation"));
 }

 @Test
 public void testHasAnnotation_ClassnameMatch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(String[].class);
 assertTrue(reflectionSearch.hasAnnotation("Array"));
 }

 @Test
 public void testHasAnnotation_WildcardMatch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(String[].class);
 assertTrue(reflectionSearch.hasAnnotation("Annotat*"));
 }

 @Test(expected = BeanBinException.class)
 public void testHasAnnotation_InvalidTerm() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(String[].class);
 reflectionSearch.hasAnnotation("InvalidTerm");
 fail("BeanBinException should be thrown");
 }

 @Test
 public void testMethodsThatHave_ExactMatch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("methodName");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(1, methods.size());
 Method method = methods.get(0);
 assertEquals("methodName", method.getName());
 }

 @Test
 public void testMethodsThatHave_WildcardMatch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("met*d");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(1, methods.size());
 }

 @Test
 public void testMethodsThatDontHave_ExactMatch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatDontHave("methodName");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testMethodsThatDontHave_WildcardMatch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatDontHave("met*d");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testAnd() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("method1");
 List<Method> methods1 = reflectionSearch.getMethods();
 reflectionSearch.methodsThatHave("method2");
 List<Method> methods2 = reflectionSearch.getMethods();
 reflectionSearch.and();
 List<Method> methods = reflectionSearch.getMethods();
 List<Method> expectedMethods = new ArrayList<Method>();
 expectedMethods.addAll(methods1);
 expectedMethods.addAll(methods2);
 assertEquals(expectedMethods.size(), methods.size());
 for (Method method : methods) {
 assertTrue(methods1.contains(method));
 assertTrue(methods2.contains(method));
 }
 }

 @Test
 public void testOr() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("method1");
 List<Method> methods1 = reflectionSearch.getMethods();
 reflectionSearch.methodsThatHave("method2");
 List<Method> methods2 = reflectionSearch.getMethods();
 reflectionSearch.or();
 List<Method> methods = reflectionSearch.getMethods();
 List<Method> expectedMethods = new ArrayList<Method>();
 expectedMethods.addAll(methods1);
 expectedMethods.addAll(methods2);
 assertEquals(expectedMethods.size(), methods.size());
 }

 @Test
 public void testGetMethods_EmptyQuery() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testGetMethods_SingleMethodMatch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("methodName");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(1, methods.size());
 }

 @Test
 public void testGetMethods_MultipleMethodMatch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("met*d");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(2, methods.size());
 }

 @Test(expected = BeanBinException.class)
 public void testGetMethods_InvalidQuery() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.query = null;
 reflectionSearch.getMethods();
 fail("BeanBinException should be thrown");
 }
}