package net.sourceforge.beanbin.reflect;

import org.junit.Test;
import org.junit.Assert.*;
import net.sourceforge.beanbin.search.*;
import java.lang.annotation.*;
import net.sourceforge.beanbin.*;
import net.sourceforge.beanbin.query.*;
import java.lang.reflect.*;
import java.util.*;

public class ReflectionSearchTest {

 @Test
 public void testHasAnnotation() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);

 // Test when term is exactly the name of an annotation
 assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));

 // Test when term is the name of a class that contains an annotation
 assertTrue(reflectionSearch.hasAnnotation("MyAnnotationType"));

 // Test when term is a wildcard character (*)
 assertTrue(reflectionSearch.hasAnnotation("*Annotation"));

 // Test when term does not match any annotation
 assertFalse(reflectionSearch.hasAnnotation("NotAnAnnotation"));
 }

 @Test
 public void testMethodsThatHave() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
 reflectionSearch.methodsThatHave("test");
 List<Method> result = reflectionSearch.getMethods();

 // Test when method name matches the term
 assertEquals(new MethodTestMethod(), result.get(0));

 // Test when method name does not match the term
 assertNull(result.get(1));
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatHaveWithEmptyQuery() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
 reflectionSearch.methodsThatHave("");
 }

 @Test
 public void testMethodsThatDontHave() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
 reflectionSearch.methodsThatDontHave("test");
 List<Method> result = reflectionSearch.getMethods();

 // Test when method name does not match the term
 assertEquals(new MethodTestMethod2(), result.get(0));

 // Test when method name matches the term
 assertNull(result.get(1));
 }

 @Test
 public void testAnd() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
 reflectionSearch.methodsThatHave("test").and().methodsThatHave("public");
 List<Method> result = reflectionSearch.getMethods();

 // Test when method name matches both terms
 assertEquals(new MethodTestMethod(), result.get(0));

 // Test when method name matches only one term
 assertNull(result.get(1));
 }

 @Test
 public void testOr() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
 reflectionSearch.methodsThatHave("test").or().methodsThatHave("private");
 List<Method> result = reflectionSearch.getMethods();

 // Test when method name matches either term
 assertEquals(new MethodTestMethod(), result.get(0));

 // Test when method name matches neither term
 assertNull(result.get(1));
 }
}

class AnnotationTestClass {
 @MyAnnotation
 public void test() {
 }

 @MyAnnotationType
 private static class InnerClass {
 }
}

class TestClass {
 @MyAnnotation
 public void test() {
 }

 @MyAnnotation
 public void test2() {
 }

 public void test3() {
 }

 @MyAnnotation
 private void test4() {
 }

 public static void test5() {
 }

 private static void test6() {
 }

 @MyAnnotation
 static class InnerStaticClass {
 }
}