package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import net.sourceforge.beanbin.*;
import net.sourceforge.beanbin.query.*;
import org.junit.Test;
import org.mockito.Mockito;

public class ReflectionSearchTest {

 class TestReflectionSearch extends ReflectionSearch {
 private TestReflectionSearch(Class clazz) {
 super(clazz);
 }
 }

 @Test
 public void testHasAnnotationWithExactMatch() throws Exception {
 Class clazz = Mockito.mock(Class.class);
 ReflectionSearch reflectionSearch = new TestReflectionSearch(clazz);
 Annotation annotation = mock(Annotation.class);
 reflectionSearch.clazz.getAnnotations()[0] = annotation;
 String term = "MyAnnotation";
 assertTrue(reflectionSearch.hasAnnotation(term));
 }

 @Test
 public void testHasAnnotationWithAnnotationNameMatch() throws Exception {
 Class clazz = Mockito.mock(Class.class);
 ReflectionSearch reflectionSearch = new TestReflectionSearch(clazz);
 Annotation annotation = mock(MyAnnotation.class);
 reflectionSearch.clazz.getAnnotations()[0] = annotation;
 String term = "MyAnnotation";
 assertTrue(reflectionSearch.hasAnnotation(term));
 }

 @Test
 public void testHasAnnotationWithClassnameMatch() throws Exception {
 Class clazz = Mockito.mock(Class.class);
 ReflectionSearch reflectionSearch = new TestReflectionSearch(clazz);
 Annotation annotation = mock(MyAnnotation.class);
 reflectionSearch.clazz.getAnnotations()[0] = annotation;
 String term = "MyAnnotationClass";
 assertTrue(reflectionSearch.hasAnnotation(term));
 }

 @Test
 public void testHasAnnotationWithWildcardMatch() throws Exception {
 Class clazz = Mockito.mock(Class.class);
 ReflectionSearch reflectionSearch = new TestReflectionSearch(clazz);
 Annotation annotation1 = mock(MyAnnotation1.class);
 Annotation annotation2 = mock(MyAnnotation2.class);
 reflectionSearch.clazz.getAnnotations()[0] = annotation1;
 reflectionSearch.clazz.getAnnotations()[1] = annotation2;
 String term = "*Annotation";
 assertTrue(reflectionSearch.hasAnnotation(term));
 }

 @Test
 public void testHasAnnotationWithNoMatch() throws Exception {
 Class clazz = Mockito.mock(Class.class);
 ReflectionSearch reflectionSearch = new TestReflectionSearch(clazz);
 reflectionSearch.clazz.getAnnotations()[0] = mock(Annotation.class);
 String term = "NoMatch";
 assertFalse(reflectionSearch.hasAnnotation(term));
 }

 @Test(expected = BeanBinException.class)
 public void testHasAnnotationWithNullClass() {
 ReflectionSearch reflectionSearch = new TestReflectionSearch(null);
 String term = "MyAnnotation";
 reflectionSearch.hasAnnotation(term);
 fail("Should have thrown BeanBinException");
 }

 @Test
 public void testMethodsThatHaveWithExactMatch() throws Exception {
 Class clazz = Mockito.mock(Class.class);
 ReflectionSearch reflectionSearch = new TestReflectionSearch(clazz);
 Method method = Mockito.mock(Method.class);
 reflectionSearch.clazz.getMethods()[0] = method;
 String term = "testMethod";
 ReflectionSearch search = reflectionSearch.methodsThatHave(term);
 List<Method> methods = search.getMethods();
 assertEquals(1, methods.size());
 assertEquals(method, methods.get(0));
 }

 @Test
 public void testMethodsThatHaveWithWildcardMatch() throws Exception {
 Class clazz = Mockito.mock(Class.class);
 ReflectionSearch reflectionSearch = new TestReflectionSearch(clazz);
 Method method1 = Mockito.mock(Method.class);
 Method method2 = Mockito.mock(Method.class);
 reflectionSearch.clazz.getMethods()[0] = method1;
 reflectionSearch.clazz.getMethods()[1] = method2;
 String term = "test*";
 ReflectionSearch search = reflectionSearch.methodsThatHave(term);
 List<Method> methods = search.getMethods();
 assertEquals(2, methods.size());
 assertTrue(methods.contains(method1));
 assertTrue(methods.contains(method2));
 }

 @Test
 public void testMethodsThatDontHaveWithExactMatch() throws Exception {
 Class clazz = Mockito.mock(Class.class);
 ReflectionSearch reflectionSearch = new TestReflectionSearch(clazz);
 Method method = Mockito.mock(Method.class);
 reflectionSearch.clazz.getMethods()[0] = method;
 String term = "testMethod";
 ReflectionSearch search = reflectionSearch.methodsThatDontHave(term);
 List<Method> methods = search.getMethods();
 assertEquals(1, methods.size());
 assertFalse(methods.contains(method));
 }

 @Test
 public void testMethodsThatDontHaveWithNoMatch() throws Exception {
 Class clazz = Mockito.mock(Class.class);
 ReflectionSearch reflectionSearch = new TestReflectionSearch(clazz);
 Method method = Mockito.mock(Method.class);
 reflectionSearch.clazz.getMethods()[0] = method;
 String term = "nonExistentMethod";
 ReflectionSearch search = reflectionSearch.methodsThatDontHave(term);
 List<Method> methods = search.getMethods();
 assertEquals(1, methods.size());
 assertTrue(methods.contains(method));
 }

 @Test
 public void testAndOperator() throws Exception {
 Class clazz = Mockito.mock(Class.class);
 ReflectionSearch reflectionSearch = new TestReflectionSearch(clazz);
 Method method1 = Mockito.mock(Method.class);
 Method method2 = Mockito.mock(Method.class);
 reflectionSearch.clazz.getMethods()[0] = method1;
 reflectionSearch.clazz.getMethods()[1] = method2;
 String term1 = "testMethod1";
 String term2 = "public";
 ReflectionSearch search = reflectionSearch.methodsThatHave(term1).and();
 search = search.methodsThatHave(term2);
 List<Method> methods = search.getMethods();
 assertEquals(1, methods.size());
 assertTrue(methods.contains(method1));
 assertTrue(methods.contains(method2));
 }

 @Test
 public void testOrOperator() throws Exception {
 Class clazz = Mockito.mock(Class.class);
 ReflectionSearch reflectionSearch = new TestReflectionSearch(clazz);
 Method method1 = Mockito.mock(Method.class);
 Method method2 = Mockito.mock(Method.class);
 reflectionSearch.clazz.getMethods()[0] = method1;
 reflectionSearch.clazz.getMethods()[1] = method2;
 String term1 = "testMethod1";
 String term2 = "public";
 ReflectionSearch search = reflectionSearch.methodsThatHave(term1);
 search = search.or();
 search = search.methodsThatHave(term2);
 List<Method> methods = search.getMethods();
 assertEquals(2, methods.size());
 assertTrue(methods.contains(method1));
 assertTrue(methods.contains(method2));
 }

 @Test(expected = BeanBinException.class)
 public void testGetMethodsWithNullClass() throws Exception {
 ReflectionSearch reflectionSearch = new TestReflectionSearch(null);
 reflectionSearch.getMethods();
 fail("Should have thrown BeanBinException");
 }
}