package net.sourceforge.beanbin.reflect;

import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReflectionSearchTest {

 @Test
 public void testHasAnnotation() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);

 // Positive test case - annotation is present in the class
 Assert.assertTrue(reflectionSearch.hasAnnotation("AnnotationTest"));

 // Negative test case - annotation is not present in the class
 Assert.assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));

 // Test with wildcard character
 Assert.assertTrue(reflectionSearch.hasAnnotation("*Annotation"));
 }

 @Test
 public void testMethodsThatHave() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(TestClassWithMethods.class);
 List<Method> methods = reflectionSearch.methodsThatHave("get").getMethods();

 // Positive test case - method with the given name exists
 Assert.assertEquals(2, methods.size());
 Assert.assertEquals("getA", methods.get(0).getName());
 Assert.assertEquals("getB", methods.get(1).getName());

 // Negative test case - method with the given name does not exist
 reflectionSearch = new ReflectionSearch(TestClassWithoutMethod.class);
 methods = reflectionSearch.methodsThatHave("get").getMethods();
 Assert.assertEquals(0, methods.size());
 }

 @Test
 public void testMethodsThatDontHave() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(TestClassWithMethods.class);
 List<Method> methods = reflectionSearch.methodsThatDontHave("set").getMethods();

 // Positive test case - method with the given name does not exist
 Assert.assertEquals(0, methods.size());

 // Negative test case - method with the given name exists
 reflectionSearch = new ReflectionSearch(TestClassWithSetter.class);
 methods = reflectionSearch.methodsThatDontHave("set").getMethods();
 Assert.assertEquals(1, methods.size());
 Assert.assertEquals("setC", methods.get(0).getName());
 }

 @Test
 public void testAndCondition() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(TestClassWithMethodsAndAnnotations.class);
 reflectionSearch.methodsThatHave("get").and();
 reflectionSearch.hasAnnotation("AnnotationTest");
 List<Method> methods = reflectionSearch.getMethods();

 // Positive test case - method has the given name and annotation is present
 Assert.assertEquals(1, methods.size());
 Assert.assertEquals("getB", methods.get(0).getName());
 }

 @Test(expected = BeanBinException.class)
 public void testInvalidQuery() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(TestClassWithMethods.class);
 reflectionSearch.methodsThatHave("get").and().hasAnnotation("NonExistentAnnotation");
 }

 @Test
 public void testOrCondition() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(TestClassWithMethodsOrAnnotations.class);
 reflectionSearch.methodsThatHave("get").or();
 reflectionSearch.hasAnnotation("AnnotationTestA");
 reflectionSearch.hasAnnotation("AnnotationTestB");
 List<Method> methods = reflectionSearch.getMethods();

 // Positive test case - method has the given name or annotation is present
 Assert.assertEquals(3, methods.size());
 Assert.assertEquals("getA", methods.get(0).getName());
 Assert.assertEquals("getB", methods.get(1).getName());
 Assert.assertEquals("getD", methods.get(2).getName());
 }
}