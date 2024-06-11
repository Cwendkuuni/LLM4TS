package net.sourceforge.beanbin.reflect.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.reflect.annotation.AnnotationTest;
import net.sourceforge.beanbin.reflect.query.BeanBinException;
import net.sourceforge.beanbin.reflect.query.Criteria;
import net.sourceforge.beanbin.reflect.query.Conditional;
import net.sourceforge.beanbin.reflect.query.MethodReflectionCriteria;
import net.sourceforge.beanbin.reflect.query.ReflectionCriteria;
import net.sourceforge.beanbin.reflect.query.ReflectionShelf;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class testClass;

 @Before
 public void setUp() {
 testClass = AnnotationTest.class;
 reflectionSearch = new ReflectionSearch(testClass);
 }

 @Test
 public void testHasAnnotation() {
 assertTrue(reflectionSearch.hasAnnotation("AnnotationTest"));
 assertTrue(reflectionSearch.hasAnnotation("net.sourceforge.beanbin.reflect.annotation.AnnotationTest"));
 assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
 }

 @Test
 public void testHasWildcard() {
 assertTrue(reflectionSearch.hasWildcard("Annotation*"));
 assertFalse(reflectionSearch.hasWildcard("AnnotationTest"));
 }

 @Test
 public void testMethodsThatHave() throws BeanBinException {
 List<Method> methods = reflectionSearch.methodsThatHave("test").getMethods();
 List<Method> expectedMethods = new ArrayList<>();
 expectedMethods.add(AnnotationTest.class.getMethod("testAnnotation"));
 expectedMethods.add(AnnotationTest.class.getMethod("testAnnotation2"));
 assertEquals(expectedMethods, methods);
 }

 @Test
 public void testMethodsThatDontHave() throws BeanBinException {
 List<Method> methods = reflectionSearch.methodsThatDontHave("test").getMethods();
 List<Method> expectedMethods = new ArrayList<>();
 expectedMethods.add(AnnotationTest.class.getMethod("testAnnotation3"));
 assertEquals(expectedMethods, methods);
 }

 @Test
 public void testAnd() throws BeanBinException {
 reflectionSearch.methodsThatHave("test");
 reflectionSearch.and();
 reflectionSearch.methodsThatDontHave("get").getMethods();
 List<Method> expectedMethods = new ArrayList<>();
 expectedMethods.add(AnnotationTest.class.getMethod("testAnnotation2"));
 assertEquals(expectedMethods, reflectionSearch.getMethods());
 }

 @Test
 public void testOr() throws BeanBinException {
 reflectionSearch.methodsThatHave("test");
 reflectionSearch.or();
 reflectionSearch.methodsThatHave("get").getMethods();
 List<Method> expectedMethods = new ArrayList<>();
 expectedMethods.add(AnnotationTest.class.getMethod("testAnnotation"));
 expectedMethods.add(AnnotationTest.class.getMethod("getAnnotationTest"));
 assertEquals(expectedMethods, reflectionSearch.getMethods());
 }

 @After
 public void tearDown() {
 reflectionSearch = null;
 testClass = null;
 }

}