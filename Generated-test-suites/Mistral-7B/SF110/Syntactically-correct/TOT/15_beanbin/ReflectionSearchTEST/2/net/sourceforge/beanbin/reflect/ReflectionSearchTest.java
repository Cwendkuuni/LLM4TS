package net.sourceforge.beanbin.reflect;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class clazzWithAnnotations;
 private Class clazzWithoutAnnotations;

 @Before
 public void setUp() {
 clazzWithAnnotations = AnnotatedClass.class;
 clazzWithoutAnnotations = SimpleClass.class;
 reflectionSearch = new ReflectionSearch(clazzWithAnnotations);
 }

 @Test
 public void testHasAnnotation_ExactMatch() {
 reflectionSearch = new ReflectionSearch(clazzWithAnnotations);
 assertTrue(reflectionSearch.hasAnnotation("Annotation1"));
 }

 @Test
 public void testHasAnnotation_ClassMatch() {
 reflectionSearch = new ReflectionSearch(clazzWithAnnotations);
 assertTrue(reflectionSearch.hasAnnotation("Annotation1ClassName"));
 }

 @Test
 public void testHasAnnotation_Wildcard_AnnotationName() {
 reflectionSearch = new ReflectionSearch(clazzWithAnnotations);
 assertTrue(reflectionSearch.hasAnnotation("Annotation*"));
 }

 @Test
 public void testHasAnnotation_Wildcard_ClassName() {
 reflectionSearch = new ReflectionSearch(clazzWithAnnotations);
 assertTrue(reflectionSearch.hasAnnotation("*Annotation"));
 }

 @Test
 public void testHasAnnotation_False() {
 reflectionSearch = new ReflectionSearch(clazzWithoutAnnotations);
 assertFalse(reflectionSearch.hasAnnotation("Annotation1"));
 }

 @Test
 public void testMethodsThatHave() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(clazzWithAnnotations);
 reflectionSearch.methodsThatHave("test");
 List<Method> expectedMethods = Arrays.asList(clazzWithAnnotations.getMethod("testMethod1"), clazzWithAnnotations.getMethod("testMethod2"));
 List<Method> actualMethods = reflectionSearch.getMethods();
 assertEquals(expectedMethods, actualMethods);
 }

 @Test
 public void testMethodsThatDontHave() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(clazzWithAnnotations);
 reflectionSearch.methodsThatDontHave("test");
 List<Method> expectedEmptyList = Collections.emptyList();
 List<Method> actualMethods = reflectionSearch.getMethods();
 assertEquals(expectedEmptyList, actualMethods);
 }

 @Test
 public void testMethodsThatHave_MultipleCriteria() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(clazzWithAnnotations);
 reflectionSearch.methodsThatHave("test").methodsThatHave("private");
 List<Method> expectedMethods = Arrays.asList(clazzWithAnnotations.getMethod("privateTestMethod1"), clazzWithAnnotations.getMethod("privateTestMethod2"));
 List<Method> actualMethods = reflectionSearch.getMethods();
 assertEquals(expectedMethods, actualMethods);
 }

 @Test
 public void testMethodsThatDontHave_MultipleCriteria() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(clazzWithAnnotations);
 reflectionSearch.methodsThatDontHave("test").methodsThatDontHave("private");
 List<Method> expectedEmptyList = Collections.emptyList();
 List<Method> actualMethods = reflectionSearch.getMethods();
 assertEquals(expectedEmptyList, actualMethods);
 }

 @Test
 public void testMethodsThatHave_EmptySearch() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(clazzWithAnnotations);
 reflectionSearch.methodsThatHave("");
 List<Method> expectedEmptyList = Collections.emptyList();
 List<Method> actualMethods = reflectionSearch.getMethods();
 assertEquals(expectedEmptyList, actualMethods);
 }
}