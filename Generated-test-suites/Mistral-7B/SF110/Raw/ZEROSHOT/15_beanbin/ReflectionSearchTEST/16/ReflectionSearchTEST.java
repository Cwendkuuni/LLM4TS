package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import org.junit.Test;

import net.sourceforge.beanbin.*;
import net.sourceforge.beanbin.query.*;

public class ReflectionSearchTest {

 private static final Class<ReflectionSearch> REFLECTION_SEARCH_CLASS = ReflectionSearch.class;

 @Test
 public void testHasAnnotation() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);

 // Test with exact match
 assertTrue(reflectionSearch.hasAnnotation("AnnotationTest"));

 reflectionSearch = new ReflectionSearch(AnnotationTest.class);
 assertTrue(reflectionSearch.hasAnnotation("AnnotationTest"));

 // Test with class name match
 reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 assertTrue(reflectionSearch.hasAnnotation("ReflectionSearch"));

 reflectionSearch = new ReflectionSearch(AnnotationTest.class);
 assertTrue(reflectionSearch.hasAnnotation("AnnotationTest"));
 assertTrue(reflectionSearch.hasAnnotation("Test"));

 // Test with name match
 reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 assertTrue(reflectionSearch.hasAnnotation("Search"));

 reflectionSearch = new ReflectionSearch(AnnotationTest.class);
 assertTrue(reflectionSearch.hasAnnotation("Annotation"));

 // Test with wildcard match
 reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 assertTrue(reflectionSearch.hasAnnotation("Search*"));

 reflectionSearch = new ReflectionSearch(AnnotationTest.class);
 assertTrue(reflectionSearch.hasAnnotation("AnnotationTest*"));
 assertTrue(reflectionSearch.hasAnnotation("Test*"));

 reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 assertFalse(reflectionSearch.hasAnnotation("AnnotatiionTest"));

 reflectionSearch = new ReflectionSearch(AnnotationTest.class);
 assertFalse(reflectionSearch.hasAnnotation("AnnotaionTest"));
 }

 @Test
 public void testMethodsThatHave() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);

 // Test with exact match
 reflectionSearch.methodsThatHave("testMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertTrue(methods.size() > 0);
 Method method = methods.get(0);
 assertEquals("testMethod", method.getName());

 // Test with class name match
 reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatHave("Search");
 methods = reflectionSearch.getMethods();
 assertEquals(3, methods.size());
 for (Method method1 : methods) {
 if (method1.getName().equals("searchMethod")) {
 assertEquals("searchMethod", method1.getName());
 return;
 }
 }
 fail();

 // Test with wildcard match
 reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatHave("Search*");
 methods = reflectionSearch.getMethods();
 assertEquals(4, methods.size());
 for (Method method1 : methods) {
 if (method1.getName().equals("searchMethod")) {
 assertEquals("searchMethod", method1.getName());
 return;
 }
 if (method1.getName().equals("searchMethods")) {
 assertEquals("searchMethods", method1.getName());
 return;
 }
 }
 fail();
 }

 @Test
 public void testMethodsThatDontHave() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);

 // Test with exact match
 reflectionSearch.methodsThatDontHave("testMethod");
 methods = reflectionSearch.getMethods();
 assertTrue(methods.isEmpty());

 // Test with class name match
 reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatDontHave("Search");
 methods = reflectionSearch.getMethods();
 assertEquals(4, methods.size());

 // Test with wildcard match
 reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatDontHave("Search*");
 methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testAnd() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatHave("test");
 reflectionSearch.and();
 reflectionSearch.methodsThatHave("Method");
 methods = reflectionSearch.getMethods();
 assertEquals(1, methods.size());
 Method method = methods.get(0);
 assertEquals("testMethod", method.getName());
 }

 @Test
 public void testOr() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatHave("test");
 reflectionSearch.or();
 reflectionSearch.methodsThatHave("Field");
 methods = reflectionSearch.getMethods();
 assertEquals(2, methods.size());
 }
}