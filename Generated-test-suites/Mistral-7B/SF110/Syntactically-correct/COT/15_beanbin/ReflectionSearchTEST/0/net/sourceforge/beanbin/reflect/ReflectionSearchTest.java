package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import net.sourceforge.beanbin.search.BeanBinException;
import net.sourceforge.beanbin.search.Criteria;
import net.sourceforge.beanbin.search.Conditional;
import net.sourceforge.beanbin.search.MethodReflectionCriteria;
import net.sourceforge.beanbin.search.ReflectionCriteria;
import net.sourceforge.beanbin.search.ReflectionShelf;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;

 @Test
 public void testReflectionSearchConstructor() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 assertNotNull(reflectionSearch);
 }

 @Test
 public void testHasAnnotation_EmptyAnnotations_ReturnsFalse() {
 reflectionSearch = new ReflectionSearch(Object.class);
 assertFalse(reflectionSearch.hasAnnotation(""));
 }

 @Test
 public void testHasAnnotation_AnnotatedClassName_ReturnsTrue() {
 Class annotatedClass = AnnotationTestClass.class;
 reflectionSearch = new ReflectionSearch(annotatedClass);
 assertTrue(reflectionSearch.hasAnnotation("AnnotationTest"));
 }

 @Test
 public void testHasAnnotation_AnnotatedClassNameWithWildcard_ReturnsTrue() {
 Class annotatedClass = AnnotatedWithWildcardClass.class;
 reflectionSearch = new ReflectionSearch(annotatedClass);
 assertTrue(reflectionSearch.hasAnnotation("Annot*"));
 }

 @Test
 public void testHasAnnotation_AnnotatedClassNameWithSubstring_ReturnsFalse() {
 Class annotatedClass = AnnotatedWithSubstringClass.class;
 reflectionSearch = new ReflectionSearch(annotatedClass);
 assertFalse(reflectionSearch.hasAnnotation("AnnotSubstring"));
 }

 @Test
 public void testHasAnnotation_AnnotatedClassWithNoAnnotation_ReturnsFalse() {
 Class noAnnotationClass = NoAnnotationClass.class;
 reflectionSearch = new ReflectionSearch(noAnnotationClass);
 assertFalse(reflectionSearch.hasAnnotation(""));
 }

 @Test
 public void testHasAnnotation_AnnotatedField_ReturnsFalse() {
 Class annotatedClass = AnnotatedFieldClass.class;
 reflectionSearch = new ReflectionSearch(annotatedClass);
 assertFalse(reflectionSearch.hasAnnotation(""));
 }

 @Test
 public void testMethodsThatHave_MethodsMatchingQuery_ReturnsReflectionSearchInstance() {
 reflectionSearch = new ReflectionSearch(TestClassWithMethods.class);
 reflectionSearch.methodsThatHave("getFoo");
 assertSame(reflectionSearch, reflectionSearch);
 }

 @Test
 public void testMethodsThatHave_NoMethodsMatchingQuery_ThrowsBeanBinException() {
 reflectionSearch = new ReflectionSearch(TestClassWithMethods.class);
 try {
 reflectionSearch.methodsThatHave("getBar");
 fail("BeanBinException expected");
 } catch (BeanBinException e) {
 // Expected
 }
 }

 @Test
 public void testMethodsThatHave_MethodsMatchingQuery_AddsMethodsToQuery() {
 reflectionSearch = new ReflectionSearch(TestClassWithMethods.class);
 reflectionSearch.methodsThatHave("getFoo");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(1, methods.size());
 assertNotNull(methods.get(0));
 assertEquals("getFoo", methods.get(0).getName());
 }

 @Test
 public void testMethodsThatDontHave_MethodsNotMatchingQuery_ReturnsReflectionSearchInstance() {
 reflectionSearch = new ReflectionSearch(TestClassWithMethods.class);
 reflectionSearch.methodsThatDontHave("getBar");
 assertSame(reflectionSearch, reflectionSearch);
 }

 @Test
 public void testMethodsThatDontHave_MethodsMatchingQuery_DoesNotAddMethodsToQuery() {
 reflectionSearch = new ReflectionSearch(TestClassWithMethods.class);
 reflectionSearch.methodsThatDontHave("getFoo");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testAnd() {
 reflectionSearch = new ReflectionSearch(TestClassWithMethods.class);
 reflectionSearch.methodsThatHave("getFoo");
 reflectionSearch.and();
 reflectionSearch.methodsThatHave("getBar");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testOr() {
 reflectionSearch = new ReflectionSearch(TestClassWithMethods.class);
 reflectionSearch.methodsThatHave("getFoo");
 reflectionSearch.or();
 reflectionSearch.methodsThatHave("getBar");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(2, methods.size());
 }

 @Test
 public void testGetMethods_EmptyQuery_ReturnsEmptyList() {
 reflectionSearch = new ReflectionSearch(TestClassWithMethods.class);
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testGetMethods_QueryWithNoResults_ReturnsEmptyList() {
 reflectionSearch = new ReflectionSearch(TestClassWithMethods.class);
 reflectionSearch.methodsThatHave("getNonExistentMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testGetMethods_QueryWithResults_ReturnsListWithMethods() {
 reflectionSearch = new ReflectionSearch(TestClassWithMethods.class);
 reflectionSearch.methodsThatHave("getFoo");
 reflectionSearch.methodsThatHave("getBar");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(2, methods.size());
 assertNotNull(methods.get(0));
 assertNotNull(methods.get(1));
 }

 @Test
 public void testIntersect() {
 List<Method> master = Arrays.asList(new Method[] { new Method() {}, new Method() {} });
 List<Method> toadd = Arrays.asList(new Method[] { new Method() {} });
 ReflectionSearch reflectionSearch = new ReflectionSearch();
 reflectionSearch.intersect(master, toadd);
 assertEquals(0, master.size());
 }

 @Test
 public void testUnion() {
 List<Method> master = Collections.emptyList();
 List<Method> toadd = Arrays.asList(new Method[] { new Method() {} });
 ReflectionSearch reflectionSearch = new ReflectionSearch();
 reflectionSearch.union(master, toadd);
 assertEquals(1, master.size());
 assertNotNull(master.get(0));
 }

 @Test
 public void testUnion_ListsWithOverlappingElements_ReturnsMergedList() {
 List<Method> master = Arrays.asList(new Method[] { new Method() {}, new Method() {} });
 List<Method> toadd = Arrays.asList(new Method[] { new Method() {} });
 ReflectionSearch reflectionSearch = new ReflectionSearch();
 reflectionSearch.union(master, toadd);
 assertEquals(3, master.size());
 assertNotNull(master.get(0));
 assertNotNull(master.get(1));
 assertNotNull(master.get(2));
 }
}