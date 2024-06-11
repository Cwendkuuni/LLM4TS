package net.sourceforge.beanbin.reflect;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class testClass;

 @Before
 public void setUp() throws Exception {
 testClass = Mockito.mock(Class.class);
 reflectionSearch = new ReflectionSearch(testClass);
 }

 @Test
 public void testHasAnnotation() {
 Assert.assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
 Annotation testAnnotation = Mockito.mock(Annotation.class);
 Mockito.when(testClass.getAnnotations()).thenReturn(new Annotation[]{testAnnotation});
 Assert.assertTrue(reflectionSearch.hasAnnotation("Annotation"));
 Assert.assertTrue(reflectionSearch.hasAnnotation("Annotation" + "*"));
 Assert.assertTrue(reflectionSearch.hasAnnotation("java.lang.annotation.Annotation"));
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatHaveThrowsException() throws BeanBinException {
 reflectionSearch.methodsThatHave("NonExistentMethod");
}

 @Test
 public void testMethodsThatHave() throws BeanBinException {
 List<Method> methods = Arrays.asList(testClass.getMethods());
 reflectionSearch.methodsThatHave("equals").getMethods();
 List<Method> methodsThatHave = reflectionSearch.getMethods();
 assertEquals(1, methodsThatHave.size());
 assertEquals(methods.get(0), methodsThatHave.get(0));
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("toString").getMethods();
 methodsThatHave = reflectionSearch.getMethods();
 assertEquals(1, methodsThatHave.size());
 assertEquals(methods.get(1), methodsThatHave.get(0));
 }

 @Test
 public void testMethodsThatDontHave() throws BeanBinException {
 List<Method> methods = Arrays.asList(testClass.getMethods());
 reflectionSearch.methodsThatDontHave("equals").getMethods();
 List<Method> methodsThatDontHave = reflectionSearch.getMethods();
 assertEquals(methods.size() - 1, methodsThatDontHave.size());
 for (Method method : methods) {
 if (!methodsThatDontHave.contains(method)) {
 assertEquals(method, methodsThatDontHave.get(0));
 break;
 }
 }
 }

 @Test
 public void testAnd() throws BeanBinException {
 List<Method> methods = Arrays.asList(new Method[]{testClass.getMethod("equals"), testClass.getMethod("toString")});
 reflectionSearch.methodsThatHave("equals").and().methodsThatDontHave("toString").getMethods();
 List<Method> methodsThatMatch = reflectionSearch.getMethods();
 assertEquals(0, methodsThatMatch.size());
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("equals").and().methodsThatHave("toString").getMethods();
 methodsThatMatch = reflectionSearch.getMethods();
 assertEquals(1, methodsThatMatch.size());
 assertEquals(methods.get(0), methodsThatMatch.get(0));
 }

 @Test
 public void testOr() throws BeanBinException {
 List<Method> methods = Arrays.asList(new Method[]{testClass.getMethod("equals"), testClass.getMethod("toString")});
 reflectionSearch.methodsThatHave("equals").or().methodsThatHave("toString").getMethods();
 List<Method> methodsThatMatch = reflectionSearch.getMethods();
 assertEquals(2, methodsThatMatch.size());
 for (Method method : methodsThatMatch) {
 if (method.getName().equals("equals")) {
 assertEquals(methods.get(0), method);
 break;
 }
 }
 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("toString").or().methodsThatHave("equals").getMethods();
 methodsThatMatch = reflectionSearch.getMethods();
 assertEquals(2, methodsThatMatch.size());
 for (Method method : methodsThatMatch) {
 if (method.getName().equals("toString")) {
 assertEquals(methods.get(1), method);
 break;
 }
 }
 }
}