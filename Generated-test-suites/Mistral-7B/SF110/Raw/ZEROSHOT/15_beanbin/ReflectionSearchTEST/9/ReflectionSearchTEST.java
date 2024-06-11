package net.sourceforge.beanbin.reflect.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.beanbin.query.Conditional;
import net.sourceforge.beanbin.query.Criteria;
import net.sourceforge.beanbin.query.ReflectionCriteria;
import net.sourceforge.beanbin.query.SearchType;
import net.sourceforge.beanbin.reflect.ReflectionSearch;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class testClass;

 @Before
 public void setUp() throws Exception {
 testClass = TestClassWithMethods.class;
 reflectionSearch = new ReflectionSearch(testClass);
 }

 @After
 public void tearDown() throws Exception {
 reflectionSearch = null;
 testClass = null;
 }

 @Test
 public void testHasAnnotationWithExactMatch() {
 assertTrue(reflectionSearch.hasAnnotation("TestAnnotation"));
 }

 @Test
 public void testHasAnnotationWithClassNameMatch() {
 assertTrue(reflectionSearch.hasAnnotation("AnnotationTest"));
 }

 @Test
 public void testHasAnnotationWithNoMatch() {
 assertFalse(reflectionSearch.hasAnnotation("NoMatchAnnotation"));
 }

 @Test
 public void testHasAnnotationWithWildcardMatch() {
 assertTrue(reflectionSearch.hasAnnotation("Annotation*"));
 }

 @Test
 public void testMethodsThatHaveWithExactMatch() throws BeanBinException {
 List<Method> methods = reflectionSearch.methodsThatHave("testMethod").getMethods();
 assertEquals(2, methods.size());
 }

 @Test
 public void testMethodsThatHaveWithWildcardMatch() throws BeanBinException {
 List<Method> methods = reflectionSearch.methodsThatHave("test*").getMethods();
 assertEquals(3, methods.size());
 }

 @Test
 public void testMethodsThatDontHaveWithExactMatch() throws BeanBinException {
 List<Method> methods = reflectionSearch.methodsThatDontHave("testMethod").getMethods();
 assertEquals(1, methods.size());
 }

 @Test
 public void testMethodsThatDontHaveWithWildcardMatch() throws BeanBinException {
 List<Method> methods = reflectionSearch.methodsThatDontHave("test*").getMethods();
 assertEquals(1, methods.size());
 }

 @Test
 public void testAndConditional() throws BeanBinException {
 List<Method> methods = reflectionSearch.methodsThatHave("testMethod").and().methodsThatDontHave("testGet").getMethods();
 assertEquals(1, methods.size());
 }

 @Test
 public void testOrConditional() throws BeanBinException {
 List<Method> methods = reflectionSearch.methodsThatHave("testMethod").or().methodsThatHave("testSet").getMethods();
 assertEquals(3, methods.size());
 }
}

class TestClassWithMethods {
 @TestAnnotation
 public void testMethod() {
 // Method implementation
 }

 @AnnotationTest
 public void testGet() {
 // Method implementation
 }

 @AnnotationTest
 public void testSet(String param) {
 // Method implementation
 }
}

class TestAnnotation {
 // Annotation implementation
}

class AnnotationTest {
 // Annotation implementation
}