package net.sourceforge.beanbin.reflect.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.beanbin.query.Conditional;
import net.sourceforge.beanbin.query.Criteria;
import net.sourceforge.beanbin.search.MethodReflectionCriteria;
import net.sourceforge.beanbin.reflect.ReflectionSearch;

import org.junit.Test;

public class ReflectionSearchTest {

 private static final String ANNOTATION_NAME = "MyAnnotation";
 private static final String ANNOTATION_CLASS_NAME = "MyAnnotationClass";
 private static final String METHOD_NAME = "methodThatMatches";
 private static final String METHOD_NAME_WILD = "methodThatMatches*";
 private static final String NON_EXISTENT_METHOD_NAME = "nonExistentMethod";
 private static final String NON_EXISTENT_ANNOTATION_NAME = "NonExistentAnnotation";
 private static final String NON_EXISTENT_ANNOTATION_CLASS_NAME = "NonExistentAnnotationClass";

 @Test
 public void testHasAnnotation() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);

 // Test when term is exact match for annotation name
 assertTrue(reflectionSearch.hasAnnotation(ANNOTATION_NAME));

 // Test when term is exact match for annotation class name
 reflectionSearch = new ReflectionSearch(Annotation.class);
 assertTrue(reflectionSearch.hasAnnotation(ANNOTATION_CLASS_NAME));

 // Test when term is wildcard for class name
 reflectionSearch = new ReflectionSearch(Object.class);
 assertTrue(reflectionSearch.hasAnnotation(ANNOTATION_NAME_WILD));

 // Test when term is wildcard for annotation name
 reflectionSearch = new ReflectionSearch(Annotation.class);
 assertTrue(reflectionSearch.hasAnnotation(NON_EXISTENT_ANNOTATION_NAME_WILD));

 // Test when term does not match any annotation
 reflectionSearch = new ReflectionSearch(Object.class);
 assertFalse(reflectionSearch.hasAnnotation("NonExistentTerm"));
 }

 @Test
 public void testMethodsThatHave() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave(METHOD_NAME);
 List<Method> methods = reflectionSearch.getMethods();
 assertNotNull(methods);
 Method method = methods.get(0);
 assertEquals(METHOD_NAME, method.getName());

 // Test when multiple methods with the same name exist
 reflectionSearch = new ReflectionSearch(Object[].class);
 reflectionSearch.methodsThatHave(METHOD_NAME);
 methods = reflectionSearch.getMethods();
 assertEquals(2, methods.size());
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatHaveThrowsException() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave(NON_EXISTENT_METHOD_NAME);
 }

 @Test
 public void testMethodsThatDontHave() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatDontHave(METHOD_NAME);
 List<Method> methods = reflectionSearch.getMethods();
 assertNotNull(methods);
 assertEquals(0, methods.size());
 }

 @Test
 public void testAnd() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave(METHOD_NAME);
 reflectionSearch.and();
 reflectionSearch.methodsThatHave(ANNOTATION_NAME);
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testOr() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave(METHOD_NAME);
 reflectionSearch.or();
 reflectionSearch.methodsThatHave(ANNOTATION_NAME);
 reflectionSearch.methodsThatHave(METHOD_NAME_WILD);
 List<Method> methods = reflectionSearch.getMethods();
 assertFalse(methods.isEmpty());
 }

 @Test
 public void testIntersect() throws Exception {
 List<Method> masterMethods = new ArrayList<>();
 List<Method> toaddMethods = new ArrayList<>();
 toaddMethods.add(new Method() {});
 Method method = new Method() {};
 masterMethods.add(method);
 toaddMethods.add(method);
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.intersect(masterMethods, toaddMethods);
 assertEquals(1, masterMethods.size());
 }

 @Test
 public void testUnion() throws Exception {
 List<Method> masterMethods = new ArrayList<>();
 List<Method> toaddMethods = new ArrayList<>();
 Method method = new Method() {};
 masterMethods.add(method);
 toaddMethods.add(new Method() {});
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.union(masterMethods, toaddMethods);
 assertEquals(2, masterMethods.size());
 }

}