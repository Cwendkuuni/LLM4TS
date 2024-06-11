package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.beanbin.*;
import net.sourceforge.beanbin.query.*;
import org.junit.Test;

public class ReflectionSearchTest {

 private static final Class<ReflectionSearch> CLASS_REFLECTION_SEARCH = ReflectionSearch.class;

 @Test
 public void testHasAnnotation() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);

 // Test with exact match
 assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));

 // Test with class name match
 assertTrue(reflectionSearch.hasAnnotation("Annotation"));

 // Test with interface name match
 assertTrue(reflectionSearch.hasAnnotation("MyInterface"));

 // Test with wildcard match
 assertTrue(reflectionSearch.hasAnnotation("MyAnno*"));

 // Test with no match
 assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
 }

 @Test
 public void testHasWildcard() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);

 // Test with wildcard
 assertTrue(reflectionSearch.hasWildcard("MyAnno*"));

 // Test without wildcard
 assertFalse(reflectionSearch.hasWildcard("ReflectionSearch"));
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatHaveNullTerm() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatHave(null);
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatHaveEmptyTerm() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatHave("");
 }

 @Test
 public void testMethodsThatHave() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatHave("getMethodName");
 List<Method> methods = reflectionSearch.getMethods();

 // Test with exact match
 assertEquals(1, methods.size());
 Method method = methods.get(0);
 assertEquals("getMethodName", method.getName());

 // Test with multiple matches
 reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("get");
 methods = reflectionSearch.getMethods();
 assertEquals(11, methods.size()); // Java Object has 11 methods beginning with 'get'
 }

 @Test
 public void testMethodsThatDontHave() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatDontHave("getMethodName");
 List<Method> methods = reflectionSearch.getMethods();

 // Test with exact match
 assertTrue(methods.isEmpty());
 }

 @Test
 public void testAnd() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatHave("getMethodName");
 List<Method> methods1 = reflectionSearch.getMethods();
 reflectionSearch.and();
 reflectionSearch.methodsThatHave("setMethodName");
 List<Method> methods2 = reflectionSearch.getMethods();

 // Test intersection
 List<Method> expectedMethods = new ArrayList<>();
 expectedMethods.add(ReflectionSearch.class.getMethod("setMethodName", String.class));
 assertEquals(1, methods2.size());
 assertEquals(expectedMethods, methods2);

 // Test without intersection
 methods1 = reflectionSearch.getMethods();
 reflectionSearch.and();
 reflectionSearch.methodsThatDontHave("getMethodName");
 methods2 = reflectionSearch.getMethods();
 assertTrue(methods2.isEmpty());
 }

 @Test
 public void testOr() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatHave("getMethodName");
 List<Method> methods1 = reflectionSearch.getMethods();
 reflectionSearch.or();
 reflectionSearch.methodsThatDontHave("setMethodName");
 List<Method> methods2 = reflectionSearch.getMethods();

 // Test union
 List<Method> expectedMethods = new ArrayList<>(Arrays.asList(
 ReflectionSearch.class.getMethod("getMethodName", Integer.TYPE),
 ReflectionSearch.class.getMethod("getMethodName", String.class),
 ReflectionSearch.class.getMethod("getMethodName", Object[].class)
 ));
 assertEquals(3, methods2.size());
 assertTrue(methods2.containsAll(expectedMethods));
 }

 @Test
 public void testGetMethodsEmptyQuery() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testGetMethodsSingleCriteria() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatHave("getMethodName");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(3, methods.size());
 }

 @Test
 public void testGetMethodsMultipleCriteria() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatHave("getMethodName");
 reflectionSearch.and();
 reflectionSearch.methodsThatDontHave("setMethodName");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testGetMethodsMultipleCriteriaWithResults() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatHave("getMethodName");
 List<Method> methods1 = reflectionSearch.getMethods();
 reflectionSearch.and();
 reflectionSearch.methodsThatHave("setMethodName");
 List<Method> methods2 = reflectionSearch.getMethods();

 // Test intersection
 List<Method> expectedMethods = new ArrayList<>();
 expectedMethods.add(ReflectionSearch.class.getMethod("setMethodName", String.class));
 reflectionSearch.and();
 reflectionSearch.methodsThatDontHave("getMethodName");
 methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());

 // Test union
 methods1 = reflectionSearch.getMethods();
 reflectionSearch.or();
 reflectionSearch.methodsThatDontHave("setMethodName");
 methods2 = reflectionSearch.getMethods();
 List<Method> expectedMethodsUnion = new ArrayList<>(Arrays.asList(
 ReflectionSearch.class.getMethod("getMethodName", Integer.TYPE),
 ReflectionSearch.class.getMethod("getMethodName", String.class),
 ReflectionSearch.class.getMethod("getMethodName", Object[].class),
 ReflectionSearch.class.getMethod("setMethodName", String.class)
 ));
 assertEquals(4, methods2.size());
 assertTrue(methods2.containsAll(expectedMethodsUnion));
 }

 private static class AnnotationTestClass {
 @MyAnnotation
 public void myMethod() {
 }

 @MyInterface
 interface MyInterfaceAnnotation {
 }
 }
}