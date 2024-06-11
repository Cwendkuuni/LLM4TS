package net.sourceforge.beanbin.reflect.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.reflect.ReflectionShelf;
import net.sourceforge.beanbin.search.Criteria;
import net.sourceforge.beanbin.search.MethodReflectionCriteria;
import net.sourceforge.beanbin.search.Query;
import net.sourceforge.beanbin.search.ReflectionCriteria;
import net.sourceforge.beanbin.search.Conditional;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class testClass;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 testClass = this.getClass();
 reflectionSearch = new ReflectionSearch(testClass);
 }

 @Test
 public void testHasAnnotation() {
 Assert.assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
 Annotation annotation = testClass.getAnnotation(Before.class);
 Assert.assertTrue(reflectionSearch.hasAnnotation(annotation.annotationType().getName()));
 Assert.assertTrue(reflectionSearch.hasAnnotation("Before"));
 }

 @Test(expected = BeanBinException.class)
 public void testHasAnnotationWithException() throws Exception {
 reflectionSearch.hasAnnotation("InvalidAnnotationName");
 }

 @Test
 public void testHasWildcard() {
 Assert.assertFalse(reflectionSearch.hasWildcard("NoWildcard"));
 Assert.assertTrue(reflectionSearch.hasWildcard("*Annotation"));
 }

 @Test
 public void testMethodsThatHave() throws Exception {
 List<Method> methods = reflectionSearch.getMethods();
 Method method = testClass.getMethod("setUp");
 reflectionSearch.methodsThatHave("setUp").and();
 List<Method> filteredMethods = reflectionSearch.getMethods();
 Assert.assertEquals(1, filteredMethods.size());
 Assert.assertEquals(method, filteredMethods.get(0));
 }

 @Test
 public void testMethodsThatDontHave() throws Exception {
 List<Method> methods = reflectionSearch.getMethods();
 Method method = testClass.getMethod("testHasAnnotationWithException");
 reflectionSearch.methodsThatDontHave("setUp").and();
 List<Method> filteredMethods = reflectionSearch.getMethods();
 Assert.assertEquals(methods.size() - 1, filteredMethods.size());
 Assert.assertFalse(filteredMethods.contains(method));
 }

 @Test
 public void testIntersect() throws Exception {
 List<Method> list1 = Arrays.asList(testClass.getMethods());
 List<Method> list2 = Arrays.asList(testClass.getMethods());
 reflectionSearch.intersect(list1, list2);
 Assert.assertEquals(list1.size(), list1.size());
 }

 @Test
 public void testUnion() throws Exception {
 List<Method> list1 = Arrays.asList(testClass.getMethods());
 List<Method> list2 = new ArrayList<>();
 list2.add(testClass.getMethod("testIntersect"));
 reflectionSearch.union(list1, list2);
 Assert.assertEquals(list1.size() + 1, list1.size());
 Assert.assertTrue(list1.contains(testClass.getMethod("testIntersect")));
 }

 @Test
 public void testAnd() throws Exception {
 List<Method> methods1 = reflectionSearch.methodsThatHave("test").getMethods();
 List<Method> methods2 = reflectionSearch.methodsThatHave("And").getMethods();
 reflectionSearch = reflectionSearch.and();
 List<Method> filteredMethods = reflectionSearch.getMethods();
 Assert.assertEquals(methods1.size() * methods2.size(), filteredMethods.size());
 for (Method method1 : methods1) {
 for (Method method2 : methods2) {
 Assert.assertTrue(filteredMethods.contains(method1));
 Assert.assertTrue(filteredMethods.contains(method2));
 }
 }
 }

 @Test
 public void testOr() throws Exception {
 List<Method> methods1 = reflectionSearch.methodsThatHave("test").getMethods();
 List<Method> methods2 = reflectionSearch.methodsThatHave("Or").getMethods();
 reflectionSearch = reflectionSearch.or();
 List<Method> filteredMethods = reflectionSearch.getMethods();
 Assert.assertEquals(methods1.size() + methods2.size(), filteredMethods.size());
 for (Method method1 : methods1) {
 Assert.assertTrue(filteredMethods.contains(method1));
 }
 for (Method method2 : methods2) {
 Assert.assertTrue(filteredMethods.contains(method2));
 }
 }

 @Test
 public void testQueryExecutionWithEmptyQuery() throws Exception {
 reflectionSearch = new ReflectionSearch(testClass);
 List<Method> methods = reflectionSearch.getMethods();
 Assert.assertEquals(0, methods.size());
 }
}