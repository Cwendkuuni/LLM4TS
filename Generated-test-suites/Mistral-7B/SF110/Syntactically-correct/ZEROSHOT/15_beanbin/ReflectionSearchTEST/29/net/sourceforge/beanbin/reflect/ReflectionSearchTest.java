package net.sourceforge.beanbin.reflect.test;

import org.junit.Assert;
import org.junit.Test;
import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.reflect.annotation.MyAnnotation;
import net.sourceforge.beanbin.search.*;
import java.lang.annotation.ElementType;
import java.lang.reflect.*;
import java.util.*;

public class ReflectionSearchTest {

 @Test
 public void testHasAnnotation() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(MyAnnotation.class);

 // Test with exact match
 Assert.assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));

 // Test with class name match
 Assert.assertTrue(reflectionSearch.hasAnnotation("net.sourceforge.beanbin.reflect.annotation.MyAnnotation"));

 // Test with wildcard match
 reflectionSearch = new ReflectionSearch(MyWildcardAnnotation.class);
 Assert.assertTrue(reflectionSearch.hasAnnotation("net.sourceforge.beanbin.reflect.annotation.MyAnnotation*"));
 Assert.assertTrue(reflectionSearch.hasAnnotation("net.sourceforge.beanbin.reflect.annotation.*Annotation"));

 // Test with no match
 reflectionSearch = new ReflectionSearch(OtherClass.class);
 Assert.assertFalse(reflectionSearch.hasAnnotation("MyAnnotation"));
 }

 @Test
 public void testMethodsThatHaveAndMethodsThatDontHave() throws Exception {
 Class clazz = MyClassWithMethods.class;
 ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

 // Test methods that have
 reflectionSearch.methodsThatHave("methodName");
 List<Method> methodsThatHave = reflectionSearch.getMethods();
 Assert.assertEquals(1, methodsThatHave.size());
 Method methodThatHas = methodsThatHave.get(0);
 Assert.assertEquals("methodName", methodThatHas.getName());

 // Test methods that don't have
 reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatDontHave("methodName");
 List<Method> methodsThatDontHave = reflectionSearch.getMethods();
 Assert.assertEquals(2, methodsThatDontHave.size());
 Assert.assertFalse(methodsThatDontHave.contains(methodThatHas));

 // Test methods that have and methods that don't have
 reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatHave("methodName");
 reflectionSearch.methodsThatDontHave("otherMethodName");
 List<Method> methodsThatHaveAndDontHave = reflectionSearch.getMethods();
 Assert.assertEquals(1, methodsThatHaveAndDontHave.size());
 Assert.assertFalse(methodsThatHaveAndDontHave.contains(methodThatHas));
 }

 @Test
 public void testAndOrConditions() throws Exception {
 Class clazz = MyClassWithMethods.class;
 ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);

 // Test and condition
 reflectionSearch.methodsThatHave("methodName");
 reflectionSearch.and();
 reflectionSearch.methodsThatDontHave("otherMethodName");
 List<Method> methodsWithAnd = reflectionSearch.getMethods();
 Assert.assertEquals(0, methodsWithAnd.size());

 // Test or condition
 reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatHave("methodName");
 reflectionSearch.or();
 reflectionSearch.methodsThatDontHave("otherMethodName");
 List<Method> methodsWithOr = reflectionSearch.getMethods();
 Assert.assertEquals(2, methodsWithOr.size());
 }
}

class MyAnnotation {
 @Retention(RetentionPolicy.RUNTIME)
 @Target(ElementType.TYPE)
 public @interface MyAnnotation {
 String value();
 }
}

class MyWildcardAnnotation {
 @Retention(RetentionPolicy.RUNTIME)
 @Target(ElementType.TYPE)
 public @interface MyAnnotation {
 String value();
}
}

class MyClassWithMethods {
 public void methodName() {
 // Method implementation
 }

 public void otherMethodName() {
 // Method implementation
 }
}