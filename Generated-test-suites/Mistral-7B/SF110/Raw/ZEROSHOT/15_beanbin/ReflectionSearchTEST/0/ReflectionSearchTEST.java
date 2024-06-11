package net.sourceforge.beanbin.reflect.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.reflect.annotation.AnnotationTest;
import net.sourceforge.beanbin.search.Criteria;
import net.sourceforge.beanbin.search.Conditional;
import net.sourceforge.beanbin.search.MethodReflectionCriteria;
import net.sourceforge.beanbin.search.ReflectionCriteria;
import net.sourceforge.beanbin.search.SearchType;

import org.junit.Test;

public class ReflectionSearchTest {

 private static final String ANNOTATION_TEST_CLASS = "net.sourceforge.beanbin.reflect.annotation.AnnotationTest";

 @Test
 public void testHasAnnotation() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Class.forName(ANNOTATION_TEST_CLASS));

 assertTrue(reflectionSearch.hasAnnotation("AnnotationTest"));
 assertTrue(reflectionSearch.hasAnnotation("testAnnotation"));
 assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
 }

 @Test
 public void testHasWildcard() {
 assertTrue(reflectionSearch.hasWildcard("Annotation*Test"));
 assertTrue(reflectionSearch.hasWildcard("AnnotationTest*"));
 }

 @Test
 public void testMethodsThatHave() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Class.forName(ANNOTATION_TEST_CLASS));

 reflectionSearch.methodsThatHave("test");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(3, methods.size());
 for (Method method : methods) {
 String methodName = method.getName();
 assertTrue(methodName.startsWith("test"));
 }
 }

 @Test
 public void testMethodsThatDontHave() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Class.forName(ANNOTATION_TEST_CLASS));

 reflectionSearch.methodsThatDontHave("test");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(1, methods.size());
 assertNotNull(methods.get(0));
 }

 @Test
 public void testAnd() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Class.forName(ANNOTATION_TEST_CLASS));

 reflectionSearch.methodsThatHave("test");
 reflectionSearch.and();
 reflectionSearch.methodsThatHave("int");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(1, methods.size());

 Method method = methods.get(0);
 assertEquals("testInt", method.getName());
 }

 @Test
 public void testOr() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Class.forName(ANNOTATION_TEST_CLASS));

 reflectionSearch.methodsThatHave("test");
 reflectionSearch.or();
 reflectionSearch.methodsThatHave("void");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(3, methods.size());
 }
}