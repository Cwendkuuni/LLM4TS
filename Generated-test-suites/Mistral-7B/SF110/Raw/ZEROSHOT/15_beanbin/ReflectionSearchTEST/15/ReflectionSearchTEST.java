package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.beanbin.query.*;
import org.junit.Test;

public class ReflectionSearchTest {

 private static final String CLASS_NAME = "net.sourceforge.beanbin.MyClass";

 @Test
 public void testHasAnnotation() throws Exception {
 ReflectionSearch search = new ReflectionSearch(Class.forName(CLASS_NAME));

 // Test exact match
 assertTrue(search.hasAnnotation("MyAnnotation"));

 // Test class name match
 assertTrue(search.hasAnnotation("MyAnnotationClass"));

 // Test method name match
 Annotation annotation = Class.forName(CLASS_NAME).getMethod("myMethod", new Class[0]).getAnnotation(Annotation.class);
 String annotationName = annotation.annotationType().getName();
 assertTrue(search.hasAnnotation(annotationName.substring(annotationName.lastIndexOf(".") + 1)));

 // Test wildcard match
 assertTrue(search.hasAnnotation("MyAnnotation*"));
 assertTrue(search.hasAnnotation("*Annotation"));

 // Test no match
 assertFalse(search.hasAnnotation("OtherAnnotation"));
 }

 @Test
 public void testMethodsThatHave() throws Exception {
 ReflectionSearch search = new ReflectionSearch(Class.forName(CLASS_NAME));

 // Test exact match
 search.methodsThatHave("methodName");
 List<Method> methods = search.getMethods();
 assertEquals(1, methods.size());
 assertEquals("methodName", methods.get(0).getName());

 // Test wildcard match
 search.methodsThatHave("get*");
 methods = search.getMethods();
 assertEquals(2, methods.size());

 // Test no match
 search.methodsThatHave("setWrongName");
 methods = search.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testMethodsThatDontHave() throws Exception {
 ReflectionSearch search = new ReflectionSearch(Class.forName(CLASS_NAME));

 // Test exact match
 search.methodsThatDontHave("methodName");
 List<Method> methods = search.getMethods();
 assertEquals(0, methods.size());

 // Test wildcard match
 search.methodsThatDontHave("set*");
 methods = search.getMethods();
 assertEquals(1, methods.size());

 // Test no match
 search.methodsThatDontHave("getWrongName");
 methods = search.getMethods();
 assertEquals(12, methods.size()); // Assuming 12 methods in the class
 }

 @Test
 public void testAnd() throws Exception {
 ReflectionSearch search = new ReflectionSearch(Class.forName(CLASS_NAME));

 // Test exact match
 search.methodsThatHave("methodName");
 List<Method> methods = search.getMethods();
 assertEquals(1, methods.size());

 search.and();
 search.methodsThatHave("returnType");
 methods = search.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testOr() throws Exception {
 ReflectionSearch search = new ReflectionSearch(Class.forName(CLASS_NAME));

 // Test exact match
 search.methodsThatHave("methodName");
 List<Method> methods = search.getMethods();
 assertEquals(1, methods.size());

 search.or();
 search.methodsThatDontHave("wrongMethodName");
 methods = search.getMethods();
 assertEquals(11, methods.size()); // Assuming 12 methods in the class and one doesn't match the search criteria
 }
}