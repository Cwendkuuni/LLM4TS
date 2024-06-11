package net.sourceforge.beanbin.reflect;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class testClass;

 @Before
 public void setUp() {
 testClass = TestClass.class;
 reflectionSearch = new ReflectionSearch(testClass);
 }

 @Test
 public void testHasAnnotation() {
 // Test positive case with exact match
 reflectionSearch.hasAnnotation("MyAnnotation");
 // Test negative case with exact class name match
 reflectionSearch.hasAnnotation("TestAnnotation");
 // Test negative case with exact annotation name match
 reflectionSearch = new ReflectionSearch(TestAnnotation.class);
 reflectionSearch.hasAnnotation("TestAnnotation");
 // Test positive case with wildcard character
 reflectionSearch.hasAnnotation("*Annotation");
 // Test positive case with wildcard character in class name
 reflectionSearch = new ReflectionSearch(WildcardTestClass.class);
 reflectionSearch.hasAnnotation("*TestAnnotation");
 // Test positive case with wildcard character in annotation name
 reflectionSearch = new ReflectionSearch(MyWildcardAnnotation.class);
 reflectionSearch.hasAnnotation("My*Annotation");
 }

 @Test
 public void testMethodsThatHave() throws BeanBinException {
 // Test positive case
 reflectionSearch.methodsThatHave("testMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(1, methods.size());
 assertEquals("testMethod", methods.get(0).getName());
 // Test negative case
 reflectionSearch = new ReflectionSearch(TestClass.class);
 reflectionSearch.methodsThatHave("nonExistentMethod");
 List<Method> emptyList = reflectionSearch.getMethods();
 assertEquals(0, emptyList.size());
 }

 @Test
 public void testMethodsThatDontHave() throws BeanBinException {
 // Test positive case
 reflectionSearch.methodsThatDontHave("testMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 // Test negative case
 reflectionSearch = new ReflectionSearch(TestClass.class);
 reflectionSearch.methodsThatDontHave("nonExistentMethod");
 List<Method> emptyList2 = reflectionSearch.getMethods();
 assertEquals(0, emptyList2.size());
 }

 @Test
 public void testAnd() throws BeanBinException {
 // Test positive case
 reflectionSearch.methodsThatHave("test");
 reflectionSearch.methodsThatDontHave("get");
 reflectionSearch.and();
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 // Test negative case
 reflectionSearch = new ReflectionSearch(TestClass.class);
 reflectionSearch.methodsThatHave("test");
 reflectionSearch.methodsThatHave("get");
 reflectionSearch.and();
 List<Method> methods2 = reflectionSearch.getMethods();
 assertEquals(1, methods2.size());
 // Test edge case
 reflectionSearch = new ReflectionSearch(TestClass.class);
 reflectionSearch.methodsThatHave("test");
 reflectionSearch.methodsThatDontHave("get");
 reflectionSearch.and();
 reflectionSearch.methodsThatHave("set");
 List<Method> methods3 = reflectionSearch.getMethods();
 assertEquals(0, methods3.size());
 }

 @Test
 public void testOr() throws BeanBinException {
 // Test positive case
 reflectionSearch.methodsThatHave("test");
 reflectionSearch.methodsThatHave("get");
 reflectionSearch.or();
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(2, methods.size());
 // Test negative case
 reflectionSearch = new ReflectionSearch(TestClass.class);
 reflectionSearch.methodsThatHave("test");
 reflectionSearch.methodsThatDontHave("get");
 reflectionSearch.or();
 reflectionSearch.methodsThatDontHave("set");
 List<Method> methods2 = reflectionSearch.getMethods();
 assertEquals(0, methods2.size());
 }
}

class TestClass {
 @MyAnnotation
 public void testMethod() {
 // Test code
 }

 public void nonExistentMethod() {
 // Test code
 }
}

class TestAnnotation {
 @TestAnnotation
 public void testAnnotationMethod() {
 // Test code
 }
}

class WildcardTestClass {
 @WildcardAnnotation(value = "TestAnnotation")
 public void wildcardTestMethod() {
 // Test code
 }
}

class MyWildcardAnnotation {
 @Retention(RetentionPolicy.RUNTIME)
 @Target(ElementType.METHOD)
 public @interface MyWildcardAnnotation {
 String value();
 }
}