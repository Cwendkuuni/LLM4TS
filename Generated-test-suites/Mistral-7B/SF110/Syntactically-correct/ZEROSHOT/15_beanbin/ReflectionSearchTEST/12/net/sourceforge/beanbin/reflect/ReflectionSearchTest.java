package net.sourceforge.beanbin.reflect;

import net.sourceforge.beanbin.search.*;
import org.junit.Assert;
import org.junit.Test;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

public class ReflectionSearchTest {

 @Test
 public void testHasAnnotation() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Annotation.class);

 // Test with exact match
 Assert.assertTrue(reflectionSearch.hasAnnotation("Annotation"));

 // Test with class name match
 Assert.assertTrue(reflectionSearch.hasAnnotation("java.lang.annotation.Annotation"));

 // Test with interface match
 ReflectionSearch interfaceSearch = new ReflectionSearch(Annotation.class);
 interfaceSearch.hasAnnotation("Annotation");
 Annotation[] interfaces = Annotation.class.getInterfaces();
 List<Method> methods = interfaceSearch.getMethods();
 Assert.assertEquals(interfaces.length, methods.size());
 for (Method method : methods) {
 Assert.assertTrue(method.isInterface());
 }

 // Test with wildcard match
 ReflectionSearch wildcardSearch = new ReflectionSearch(Annotation.class);
 wildcardSearch.methodsThatHave("Annot*");
 List<Method> methodsWithWildcard = wildcardSearch.getMethods();
 Assert.assertTrue(methodsWithWildcard.size() > 0);

 // Test with no match
 ReflectionSearch noMatchSearch = new ReflectionSearch(String.class);
 Assert.assertFalse(noMatchSearch.hasAnnotation("Annotation"));
 }

 @Test
 public void testHasWildcard() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 Assert.assertFalse(reflectionSearch.hasWildcard("ReflectionSearch"));
 ReflectionSearch wildcardSearch = new ReflectionSearch(ReflectionSearch.class);
 wildcardSearch.methodsThatHave("Reflectio*");
 Assert.assertTrue(wildcardSearch.hasWildcard("ReflectionSearch"));
 }

 @Test
 public void testMethodsThatHave() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatHave("search");
 List<Method> methodsWithSearch = reflectionSearch.getMethods();
 List<Method> expectedMethods = Arrays.asList(ReflectionSearch.class.getDeclaredMethods());
 for (Method expectedMethod : expectedMethods) {
 if (expectedMethod.getName().equals("search")) {
 Assert.assertTrue(methodsWithSearch.contains(expectedMethod));
 }
 }
 }

 @Test
 public void testMethodsThatDontHave() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatDontHave("equals");
 List<Method> methodsWithoutEquals = reflectionSearch.getMethods();
 List<Method> expectedMethods = Arrays.asList(ReflectionSearch.class.getDeclaredMethods());
 for (Method expectedMethod : expectedMethods) {
 if (!expectedMethod.getName().equals("equals")) {
 Assert.assertTrue(methodsWithoutEquals.contains(expectedMethod));
 }
 }
 }

 @Test
 public void testAnd() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatHave("search");
 reflectionSearch.and();
 reflectionSearch.methodsThatDontHave("toString");
 List<Method> methodsWithSearchAndDontHaveToString = reflectionSearch.getMethods();
 List<Method> expectedMethods = Arrays.asList(ReflectionSearch.class.getDeclaredMethods());
 for (Method expectedMethod : expectedMethods) {
 if (expectedMethod.getName().equals("search") && !expectedMethod.getName().equals("toString")) {
 Assert.assertTrue(methodsWithSearchAndDontHaveToString.contains(expectedMethod));
 }
 }
 }

 @Test
 public void testOr() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatHave("search");
 reflectionSearch.or();
 reflectionSearch.methodsThatHave("toString");
 List<Method> methodsWithSearchOrToString = reflectionSearch.getMethods();
 List<Method> expectedMethods = Arrays.asList(ReflectionSearch.class.getDeclaredMethods());
 for (Method expectedMethod : expectedMethods) {
 if (expectedMethod.getName().equals("search") || expectedMethod.getName().equals("toString")) {
 Assert.assertTrue(methodsWithSearchOrToString.contains(expectedMethod));
 }
 }
 }
}