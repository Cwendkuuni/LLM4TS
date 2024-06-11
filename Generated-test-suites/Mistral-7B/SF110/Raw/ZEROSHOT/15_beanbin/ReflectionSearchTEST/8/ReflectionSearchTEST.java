package net.sourceforge.beanbin.reflect;

import org.junit.Assert;
import org.junit.Test;
import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.search.AnnotationSearchCriteria;
import net.sourceforge.beanbin.search.MethodReflectionCriteria;
import net.sourceforge.beanbin.search.Query;
import net.sourceforge.beanbin.search.ReflectionCriteria;
import net.sourceforge.beanbin.search.ReflectionShelf;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Retention(Retention.RUNTIME)
@Target(ElementType.TYPE)
@interface TestAnnotation {
}

public class ReflectionSearchTest {

 private static final String PACKAGE = "net.sourceforge.beanbin.reflect";
 private static final String CLASS_NAME = "ReflectionSearch";
 private static final String ANNOTATION_TERM = "TestAnnotation";
 private static final String METHOD_TERM = "hasAnnotation";
 private static final String QUERY_TERM = "TestAnnotation";

 @Test
 public void testHasAnnotation() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Class.forName(PACKAGE + "." + CLASS_NAME));

 // Test with exact match
 Assert.assertTrue(reflectionSearch.hasAnnotation(ANNOTATION_TERM));

 // Test with class name match
 Assert.assertTrue(reflectionSearch.hasAnnotation(CLASS_NAME.substring(CLASS_NAME.lastIndexOf(".") + 1)));

 // Test with annotation name match
 reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);
 Assert.assertTrue(reflectionSearch.hasAnnotation(ANNOTATION_TERM));

 // Test with wildcard match
 reflectionSearch = new ReflectionSearch(AnnotationWildcardClass.class);
 Assert.assertTrue(reflectionSearch.hasAnnotation(ANNOTATION_TERM + "*"));
 Assert.assertTrue(reflectionSearch.hasAnnotation("*TestAnnotation"));

 // Test with no match
 reflectionSearch = new ReflectionSearch(NormalClass.class);
 Assert.assertFalse(reflectionSearch.hasAnnotation(ANNOTATION_TERM));
 }

 @Test
 public void testMethodsThatHave() throws Exception {
 Class<ReflectionSearch> clazz = ReflectionSearch.class;
 ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
 Query query = new Query();

 // Test with exact match
 reflectionSearch.methodsThatHave(METHOD_TERM).methodsThatHave(ANNOTATION_TERM);
 List<Method> methods = reflectionSearch.getMethods();
 Assert.assertEquals(2, methods.size());
 for (Method method : methods) {
 Assert.assertTrue(method.getName().equals(METHOD_TERM) || method.getName().equals("hasAnnotation"));
 Assert.assertTrue(method.isAnnotationPresent(TestAnnotation.class));
 }

 // Test with class name match
 reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatHave(CLASS_NAME.substring(CLASS_NAME.lastIndexOf(".") + 1) + "." + METHOD_TERM);
 methods = reflectionSearch.getMethods();
 Assert.assertEquals(1, methods.size());
 Assert.assertTrue(methods.get(0).getName().equals(METHOD_TERM));

 // Test with wildcard match
 reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatHave(ANNOTATION_TERM + "*." + METHOD_TERM);
 methods = reflectionSearch.getMethods();
 Assert.assertEquals(1, methods.size());
 Assert.assertTrue(methods.get(0).getName().equals(METHOD_TERM));

 // Test with no match
 reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatHave("not_a_method");
 methods = reflectionSearch.getMethods();
 Assert.assertEquals(0, methods.size());
 }

 @Test
 public void testMethodsThatDontHave() throws Exception {
 Class<ReflectionSearch> clazz = ReflectionSearch.class;
 ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
 Query query = new Query();

 // Test with exact match
 reflectionSearch.methodsThatDontHave(METHOD_TERM).methodsThatHave(ANNOTATION_TERM);
 methods = reflectionSearch.getMethods();
 Assert.assertEquals(0, methods.size());

 // Test with class name match
 reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatDontHave(CLASS_NAME.substring(CLASS_NAME.lastIndexOf(".") + 1) + "." + METHOD_TERM);
 methods = reflectionSearch.getMethods();
 Assert.assertEquals(1, methods.size());
 Assert.assertTrue(methods.get(0).getName().equals("intersect"));

 // Test with wildcard match
 reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatDontHave(ANNOTATION_TERM + "*." + METHOD_TERM);
 methods = reflectionSearch.getMethods();
 Assert.assertEquals(1, methods.size());
 Assert.assertTrue(methods.get(0).getName().equals("union"));

 // Test with no match
 reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatDontHave("not_a_method");
 methods = reflectionSearch.getMethods();
 Assert.assertEquals(ReflectionSearch.class.getDeclaredMethods().length, methods.size());
 }

 @Test
 public void testAnd() throws Exception {
 Class<ReflectionSearch> clazz = ReflectionSearch.class;
 ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
 Query query = new Query();

 // Test with exact match
 reflectionSearch.methodsThatHave(METHOD_TERM).and();
 reflectionSearch.methodsThatHave(ANNOTATION_TERM);
 methods = reflectionSearch.getMethods();
 Assert.assertEquals(0, methods.size());

 // Test with class name match
 reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatHave(CLASS_NAME.substring(CLASS_NAME.lastIndexOf(".") + 1) + "." + METHOD_TERM).and();
 reflectionSearch.methodsThatHave(ANNOTATION_TERM);
 methods = reflectionSearch.getMethods();
 Assert.assertEquals(1, methods.size());
 Assert.assertTrue(methods.get(0).getName().equals("intersect"));

 // Test with wildcard match
 reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatHave(ANNOTATION_TERM + "*." + METHOD_TERM).and();
 reflectionSearch.methodsThatHave(ANNOTATION_TERM);
 methods = reflectionSearch.getMethods();
 Assert.assertEquals(0, methods.size());

 // Test with no match
 reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatHave("not_a_method").and();
 reflectionSearch.methodsThatHave(ANNOTATION_TERM);
 methods = reflectionSearch.getMethods();
 Assert.assertEquals(0, methods.size());
 }

 @Test
 public void testOr() throws Exception {
 Class<ReflectionSearch> clazz = ReflectionSearch.class;
 ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
 Query query = new Query();

 // Test with exact match
 reflectionSearch.methodsThatHave(METHOD_TERM).or();
 reflectionSearch.methodsThatHave(ANOTATION_TERM);
 methods = reflectionSearch.getMethods();
 Assert.assertEquals(3, methods.size());
 for (Method method : methods) {
 Assert.assertTrue(method.getName().equals(METHOD_TERM) || method.getName().equals("hasAnnotation"));
 Assert.assertTrue(method.isAnnotationPresent(TestAnnotation.class));
 }

 // Test with class name match
 reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatHave(CLASS_NAME.substring(CLASS_NAME.lastIndexOf(".") + 1) + "." + METHOD_TERM).or();
 reflectionSearch.methodsThatHave(ANNOTATION_TERM);
 methods = reflectionSearch.getMethods();
 Assert.assertEquals(2, methods.size());
 for (Method method : methods) {
 Assert.assertTrue(method.getName().equals(METHOD_TERM) || method.getName().equals("intersect"));
 Assert.assertTrue(method.isAnnotationPresent(TestAnnotation.class));
 }

 // Test with wildcard match
 reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatHave(ANNOTATION_TERM + "*." + METHOD_TERM).or();
 reflectionSearch.methodsThatHave(ANNOTATION_TERM);
 methods = reflectionSearch.getMethods();
 Assert.assertEquals(3, methods.size());
 for (Method method : methods) {
 Assert.assertTrue(method.getName().equals(METHOD_TERM) || method.getName().equals("hasAnnotation") || method.getName().equals("intersect") || method.getName().equals("union"));
 Assert.assertTrue(method.isAnnotationPresent(TestAnnotation.class));
 }

 // Test with no match
 reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatHave("not_a_method").or();
 reflectionSearch.methodsThatHave(ANNOTATION_TERM);
 methods = reflectionSearch.getMethods();
 Assert.assertEquals(ReflectionSearch.class.getDeclaredMethods().length, methods.size());
 }

 private static class AnnotationTestClass {
 @TestAnnotation
 public void test() {
 }
 }

 private static class AnnotationWildcardClass {
 @TestAnnotation
 public void test() {
 }
 }

 private static class NormalClass {
 // No @TestAnnotation annotation
 }
}