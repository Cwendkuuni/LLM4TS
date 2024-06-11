package net.sourceforge.beanbin.reflect;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.annotation.ElementType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class testClass;

 @Before
 public void setUp() {
 testClass = SampleClass.class;
 reflectionSearch = new ReflectionSearch(testClass);
 }

 @Test
 public void testHasAnnotation() {
 // Test case for a class with an exact match annotation
 reflectionSearch.hasAnnotation("MyAnnotation");
 assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));

 // Test case for a class with an annotation that has a subclass name as its term
 reflectionSearch = new ReflectionSearch(SubClass.class);
 reflectionSearch.hasAnnotation("MySuperAnnotation");
 assertTrue(reflectionSearch.hasAnnotation("MySuperAnnotation"));

 // Test case for a class with an annotation that has a term with wildcards
 reflectionSearch = new ReflectionSearch(WildcardClass.class);
 reflectionSearch.hasAnnotation("My*Annotation");
 assertTrue(reflectionSearch.hasAnnotation("MySubAnnotation"));

 // Test case for a class without the given annotation
 reflectionSearch = new ReflectionSearch(NoAnnotationClass.class);
 assertFalse(reflectionSearch.hasAnnotation("MyAnnotation"));
 }

 @Test
 public void testMethodsThatHave() throws BeanBinException {
 // Test case for a class with methods that match the search term
 List<Method> methods = reflectionSearch.methodsThatHave("getName").getMethods();
 assertEquals(2, methods.size());
 assertNotNull(methods.get(0));
 assertNotNull(methods.get(1));

 // Test case for a class with no methods that match the search term
 reflectionSearch = new ReflectionSearch(EmptyMethodsClass.class);
 methods = reflectionSearch.methodsThatHave("getName").getMethods();
 assertEmpty(methods);
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatHaveWithEmptyClass() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(null);
 reflectionSearch.methodsThatHave("getName");
 fail("Expected BeanBinException to be thrown");
 }

 @Test
 public void testMethodsThatDontHave() throws BeanBinException {
 // Test case for a class with methods that do not match the search term
 List<Method> methods = reflectionSearch.methodsThatDontHave("setName").getMethods();
 assertEquals(2, methods.size());
 assertNotNull(methods.get(0));
 assertNotNull(methods.get(1));

 // Test case for a class with no methods that do not match the search term
 reflectionSearch = new ReflectionSearch(NoSuchMethodClass.class);
 methods = reflectionSearch.methodsThatDontHave("getName").getMethods();
 assertEmpty(methods);
 }

 @Test
 public void testAnd() throws BeanBinException {
 // Test case for a class with methods that match the first search term and the second search term
 List<Method> methods = reflectionSearch.methodsThatHave("getName").and().methodsThatHave("isEnabled").getMethods();
 assertEquals(1, methods.size());
 assertNotNull(methods.get(0));

 // Test case for a class with no methods that match both search terms
 reflectionSearch = new ReflectionSearch(EmptyIntersectionClass.class);
 methods = reflectionSearch.methodsThatHave("getName").and().methodsThatHave("isEnabled").getMethods();
 assertEmpty(methods);
 }

 @Test
 public void testOr() throws BeanBinException {
 // Test case for a class with methods that match either search term
 List<Method> methods = reflectionSearch.methodsThatHave("getName").or().methodsThatHave("isEnabled").getMethods();
 assertEquals(3, methods.size());
 assertNotNull(methods.get(0));
 assertNotNull(methods.get(1));
 assertNotNull(methods.get(2));

 // Test case for a class with no methods that match either search term
 reflectionSearch = new ReflectionSearch(NoSuchMethodClass.class);
 methods = reflectionSearch.methodsThatHave("getName").or().methodsThatHave("isEnabled").getMethods();
 assertEmpty(methods);
 }
}

class SampleClass {
 @MyAnnotation
 public String getName() {
 return "Sample";
 }

 public void setName(String name) {
 // empty
 }
}

class SubClass extends SampleClass {
 @MySuperAnnotation
 public String getName() {
 return "SubClass";
 }
}

class WildcardClass {
 @MyWildcardAnnotation
 public String getName() {
 return "WildCard";
 }
}

class NoAnnotationClass {
 // no annotations
}

class EmptyMethodsClass {
 // empty class
}

class NoSuchMethodClass {
 public String getName() {
 return "Name";
 }

 public void setName(String name) {
 // empty
 }

 @Deprecated
 public void deprecatedMethod() {
 // empty
 }
}

class EmptyIntersectionClass {
 public static class InnerClass {
 public String getName() {
 return "Inner";
 }
 }

 public String getName() {
 return "Empty";
 }
}