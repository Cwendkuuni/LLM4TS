package net.sourceforge.beanbin.reflect;

import org.junit.Before;
import org.junit.Test;
import net.sourceforge.beanbin.search.*;
import static org.junit.Assert.*;
import java.lang.reflect.*;
import java.util.*;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class testClass;

 @Before
 public void setUp() throws Exception {
 testClass = Class.forName("com.example.TestClass");
 reflectionSearch = new ReflectionSearch(testClass);
 }

 @Test
 public void testHasAnnotation() {
 // Test with a valid annotation
 reflectionSearch.methodsThatHave("Annotation1");
 assertTrue(reflectionSearch.hasAnnotation("Annotation1"));

 // Test with a non-existent annotation
 reflectionSearch.methodsThatHave("NonExistentAnnotation");
 assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));

 // Test with a wildcard annotation
 reflectionSearch.methodsThatHave("*Annotation");
 // Assuming there is an annotation named Annotation2 in the testClass
 // which is a subclass of Annotation1, this test should pass
 assertTrue(reflectionSearch.hasAnnotation("Annotation2"));
 }

 @Test (expected = BeanBinException.class)
 public void testInvalidMethodCall() {
 // Test with an invalid method call
 reflectionSearch.methodsThatHave("invalidMethod");
 }

 @Test
 public void testMethodsThatHave() throws BeanBinException {
 // Test with no methods that match the given term
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());

 // Test with methods that match the given term
 reflectionSearch.methodsThatHave("test");
 methods = reflectionSearch.getMethods();
 // Assuming there are methods named testMethod1 and testMethod2 in the testClass
 // this test should return both methods
 assertEquals(2, methods.size());

 // Test with methods that partially match the given term
 reflectionSearch.methodsThatHave("testMeth");
 methods = reflectionSearch.getMethods();
 // Assuming there is a method named testMethod3 in the testClass starting with "testMeth"
 // this test should return method testMethod3
 assertEquals(1, methods.size());
 }

 @Test
 public void testMethodsThatDontHave() throws BeanBinException {
 // Test with no methods that don't match the given term
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());

 // Test with methods that don't match the given term
 reflectionSearch.methodsThatHave("nonTest");
 methods = reflectionSearch.getMethods();
 // Assuming there are methods named nonTestMethod1 and nonTestMethod2 in the testClass
 // this test should return both methods
 assertEquals(2, methods.size());

 // Test with methods that partially don't match the given term
 reflectionSearch.methodsThatDontHave("nonTestMeth");
 methods = reflectionSearch.getMethods();
 // Assuming there is a method named testMethod3 in the testClass that doesn't contain "nonTestMeth"
 // this test should return method testMethod3
 assertEquals(1, methods.size());
 }

 @Test
 public void testAndOperator() throws BeanBinException {
 // Test with no methods that match both conditions
 reflectionSearch.methodsThatHave("test").and();
 reflectionSearch.methodsThatHave("nonTest");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());

 // Test with methods that match both conditions
 reflectionSearch.methodsThatHave("test");
 List<Method> methods1 = reflectionSearch.getMethods();
 reflectionSearch.methodsThatHave("nonTest").and();
 List<Method> methods2 = reflectionSearch.getMethods();
 // Assuming there is a method named testAndNonTestMethod in the testClass
 // that matches both conditions
 // this test should return method testAndNonTestMethod
 assertEquals(1, methods2.size());
 // The list should be empty after this call, as we're testing the behavior after the and operator is called
 assertEquals(0, methods1.size());
 }

 @Test
 public void testOrOperator() throws BeanBinException {
 // Test with methods that match either condition
 reflectionSearch.methodsThatHave("test").or();
 reflectionSearch.methodsThatHave("nonTest");
 List<Method> methods = reflectionSearch.getMethods();
 // Assuming there is a method named testMethod1 that matches the first condition
 // and a method named nonTestMethod1 that matches the second condition
 // this test should return both methods
 assertEquals(2, methods.size());
 }
}