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
 ReflectionSearch reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);

 // Test exact match
 Assert.assertTrue(reflectionSearch.hasAnnotation("AnnotationTest"));

 // Test class name match
 Assert.assertTrue(reflectionSearch.hasAnnotation("Test"));

 // Test interface name match
 ReflectionSearch reflectionSearchForInterface = new ReflectionSearch(AnnotationTestInterface.class);
 Assert.assertTrue(reflectionSearchForInterface.hasAnnotation("InterfaceTest"));

 // Test wildcard match for annotation type
 ReflectionSearch reflectionSearchForAnnotation = new ReflectionSearch(AnnotationTestClass.class);
 reflectionSearchForAnnotation.methodsThatHave("*Annotation");
 List<Method> methods = reflectionSearchForAnnotation.getMethods();
 Assert.assertEquals(2, methods.size());
 Assert.assertTrue(methods.get(0).isAnnotationPresent(MyAnnotation.class));
 Assert.assertTrue(methods.get(1).isAnnotationPresent(OtherAnnotation.class));

 // Test wildcard match for class name
 ReflectionSearch reflectionSearchForClass = new ReflectionSearch(AnnotationTestClass.class);
 reflectionSearchForClass.methodsThatHave("*Test*");
 methods = reflectionSearchForClass.getMethods();
 Assert.assertEquals(3, methods.size());

 // Test negative cases
 ReflectionSearch reflectionSearchForNone = new ReflectionSearch(NoAnnotationTestClass.class);
 Assert.assertFalse(reflectionSearchForNone.hasAnnotation("Test"));
 ReflectionSearch reflectionSearchForNonExistent = new ReflectionSearch(NonExistentClass.class);
 Assert.assertFalse(reflectionSearchForNonExistent.hasAnnotation("Test"));
 }

 @Test
 public void testMethodsThatHave() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);
 reflectionSearch.methodsThatHave("getAnnotationTest");
 List<Method> methods = reflectionSearch.getMethods();
 Assert.assertEquals(1, methods.size());
 Assert.assertEquals("getAnnotationTest", methods.get(0).getName());
 }

 @Test
 public void testMethodsThatDontHave() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);
 reflectionSearch.methodsThatDontHave("getAnnotationTest");
 methods = reflectionSearch.getMethods();
 Assert.assertEquals(0, methods.size());
 }

 @Test
 public void testAnd() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);
 reflectionSearch.methodsThatHave("getAnnotationTest");
 reflectionSearch.and();
 reflectionSearch.methodsThatDontHave("setAnnotationTest");
 methods = reflectionSearch.getMethods();
 Assert.assertEquals(0, methods.size());
 }

 @Test
 public void testOr() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);
 reflectionSearch.methodsThatHave("getAnnotationTest");
 reflectionSearch.or();
 reflectionSearch.methodsThatDontHave("setAnnotationTest");
 reflectionSearch.methodsThatHave("getOtherAnnotationTest");
 methods = reflectionSearch.getMethods();
 Assert.assertEquals(1, methods.size());
 Assert.assertEquals("getOtherAnnotationTest", methods.get(0).getName());
 }

}

class AnnotationTestClass {
 @AnnotationTest
 public void testMethod() {
 }

 @OtherAnnotation
 public void otherMethod() {
 }

 @MyAnnotation
 public void myMethod() {
 }
}

interface AnnotationTestInterface {
 @InterfaceTest
 void testMethod();
}

class NoAnnotationTestClass {
}

class NonExistentClass {
}