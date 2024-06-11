package net.sourceforge.beanbin.reflect;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ReflectionSearch.class })
public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;

 @Test
 public void testHasAnnotation_NoAnnotation_ReturnsFalse() throws Exception {
 Class clazz = Mockito.mock(Class.class);
 reflectionSearch = new ReflectionSearch(clazz);
 Assert.assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
 }

 @Test
 public void testHasAnnotation_AnnotationPresent_ReturnsTrue() throws Exception {
 Class clazz = Mockito.mock(Class.class);
 Annotation annotation = PowerMockito.mock(Annotation.class);
 PowerMockito.when(clazz.getAnnotations()).thenReturn(new Annotation[]{annotation});
 reflectionSearch = new ReflectionSearch(clazz);
 PowerMockito.verifyStatic();
 String name = annotation.annotationType().getName();
 Assert.assertTrue(reflectionSearch.hasAnnotation("AnnotationType"));
 PowerMockito.verifyStatic();
 String className = name.substring(name.lastIndexOf(".") + 1);
 Assert.assertTrue(reflectionSearch.hasAnnotation("AnnotationType"));
 PowerMockito.verifyStatic();
 String termName = "AnnotationType";
 Assert.assertTrue(reflectionSearch.hasWildcard(termName));
 PowerMockito.verifyStatic();
 WildcardSearch wild = new WildcardSearch(termName);
 PowerMockito.verifyStatic();
 boolean result = wild.doesMatch(name);
 PowerMockito.verifyStatic();
 Assert.assertTrue(result);
 PowerMockito.verifyStatic();
 result = wild.doesMatch(className);
 PowerMockito.verifyStatic();
 Assert.assertTrue(result);
 }

 @Test
 public void testHasAnnotation_AnnotationWithDifferentName_ReturnsFalse() throws Exception {
 Class clazz = Mockito.mock(Class.class);
 Annotation annotation = PowerMockito.mock(Annotation.class);
 PowerMockito.when(clazz.getAnnotations()).thenReturn(new Annotation[]{annotation});
 reflectionSearch = new ReflectionSearch(clazz);
 PowerMockito.verifyStatic();
 String name = annotation.annotationType().getName();
 Assert.assertFalse(reflectionSearch.hasAnnotation("DifferentAnnotationType"));
 }

 @Test
 public void testHasAnnotation_WildcardAnnotation_ReturnsTrue() throws Exception {
 Class clazz = Mockito.mock(Class.class);
 Annotation annotation = PowerMockito.mock(Annotation.class);
 PowerMockito.when(clazz.getAnnotations()).thenReturn(new Annotation[]{annotation});
 PowerMockito.when(annotation.annotationType().getName()).thenReturn("net.sourceforge.beanbin.annotations.MyAnnotation*");
 reflectionSearch = new ReflectionSearch(clazz);
 PowerMockito.verifyStatic();
 Assert.assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));
 PowerMockito.verifyStatic();
 Assert.assertTrue(reflectionSearch.hasAnnotation("MyAnnotation2"));
 }

 @Test
 public void testMethodsThatHave_EmptyQuery_ReturnsEmptyList() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(Mockito.mock(Class.class));
 List<Method> methods = reflectionSearch.getMethods();
 Assert.assertEquals(0, methods.size());
 }

 @Test
 public void testMethodsThatHave_MethodWithGivenName_ReturnsListWithOneElement() throws BeanBinException {
 Class clazz = Mockito.mock(Class.class);
 Method method = PowerMockito.mock(Method.class);
 PowerMockito.when(clazz.getMethod("testMethod")).thenReturn(method);
 reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatHave("testMethod");
 List<Method> methods = reflectionSearch.getMethods();
 Assert.assertEquals(1, methods.size());
 Assert.assertEquals(method, methods.get(0));
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatHave_InvalidMethodName_ThrowsException() throws BeanBinException {
 Class clazz = Mockito.mock(Class.class);
 reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatHave("invalidMethodName");
 }

 @Test
 public void testMethodsThatDontHave_EmptyQuery_ReturnsAllMethods() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(Mockito.mock(Class.class));
 reflectionSearch.methodsThatDontHave("");
 List<Method> methods = reflectionSearch.getMethods();
 Assert.assertEquals(0, methods.size());
 }

 @Test
 public void testMethodsThatDontHave_MethodWithGivenName_ReturnsListWithoutElement() throws BeanBinException {
 Class clazz = Mockito.mock(Class.class);
 Method method = PowerMockito.mock(Method.class);
 PowerMockito.when(clazz.getMethod("testMethod")).thenReturn(method);
 reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatDontHave("testMethod");
 List<Method> methods = reflectionSearch.getMethods();
 Assert.assertEquals(0, methods.size());
 }

 @Test
 public void testMethodsThatHaveAndMethodsThatDontHave_Intersection_ReturnsEmptyList() throws BeanBinException {
 Class clazz = Mockito.mock(Class.class);
 Method method1 = PowerMockito.mock(Method.class);
 Method method2 = PowerMockito.mock(Method.class);
 PowerMockito.when(clazz.getMethods()).thenReturn(new Method[]{method1, method2});
 reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatHave("testMethod");
 reflectionSearch.methodsThatDontHave("testMethod");
 List<Method> methods = reflectionSearch.getMethods();
 Assert.assertEquals(0, methods.size());
 }

 @Test
 public void testMethodsThatHaveAndMethodsThatDontHave_Union_ReturnsAllMethods() throws BeanBinException {
 Class clazz = Mockito.mock(Class.class);
 Method method1 = PowerMockito.mock(Method.class);
 Method method2 = PowerMockito.mock(Method.class);
 PowerMockito.when(clazz.getMethods()).thenReturn(new Method[]{method1, method2});
 reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatHave("testMethod");
 reflectionSearch.methodsThatDontHave("");
 List<Method> methods = reflectionSearch.getMethods();
 Assert.assertEquals(2, methods.size());
 Assert.assertNotNull(methods.get(0));
 Assert.assertNotNull(methods.get(1));
 }
}