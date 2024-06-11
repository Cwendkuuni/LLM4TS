package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.*;
import java.util.*;

import net.sourceforge.beanbin.query.*;
import net.sourceforge.beanbin.search.*;
import org.junit.Before;
import org.junit.Test;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class testClass;
 private List<Method> methods;
 private Query query;

 @Before
 public void setUp() {
 testClass = Mockito.mock(Class.class);
 query = new Query();
 reflectionSearch = new ReflectionSearch(testClass);
 methods = new ArrayList<>();
 }

 @Test
 public void testHasAnnotation() {
 Method testMethod = mock(Method.class);
 when(testMethod.getAnnotationTypes()).thenReturn(new Class[0]);
 when(testClass.getAnnotations()).thenReturn(new Annotation[0]);
 when(testClass.getDeclaredMethods()).thenReturn(methods.toArray(new Method[0]));

 reflectionSearch.hasAnnotation("Test");
 assertFalse(reflectionSearch.hasAnnotation("Test"));

 when(testClass.getAnnotations()).thenReturn(new Annotation[]{new TestAnnotation()});
 reflectionSearch.hasAnnotation("Test");
 assertTrue(reflectionSearch.hasAnnotation("Test"));
 }

 @Test
 public void testHasWildcard() {
 assertTrue(reflectionSearch.hasWildcard("*Test"));
 assertFalse(reflectionSearch.hasWildcard("Test"));
 }

 @Test
 public void testMethodsThatHave() throws BeanBinException {
 Method testMethod = mock(Method.class);
 Method testMethod2 = mock(Method.class);
 when(testMethod.getName()).thenReturn("testMethod");
 when(testMethod2.getName()).thenReturn("testMethod2");
 when(testClass.getDeclaredMethods()).thenReturn(new Method[]{testMethod, testMethod2});
 reflectionSearch.methodsThatHave("testMethod");
 assertEquals(1, reflectionSearch.getMethods().size());
 assertEquals(testMethod, reflectionSearch.getMethods().get(0));
 }

 @Test
 public void testMethodsThatDontHave() throws BeanBinException {
 Method testMethod = mock(Method.class);
 Method testMethod2 = mock(Method.class);
 when(testMethod.getName()).thenReturn("testMethod");
 when(testMethod2.getName()).thenReturn("testMethod2");
 when(testClass.getDeclaredMethods()).thenReturn(new Method[]{testMethod, testMethod2});
 reflectionSearch.methodsThatHave("testMethod2");
 reflectionSearch.methodsThatDontHave("testMethod");
 assertEquals(1, reflectionSearch.getMethods().size());
 assertFalse(reflectionSearch.getMethods().get(0).getName().equals("testMethod"));
 }

 @Test
 public void testAnd() throws BeanBinException {
 Method testMethod = mock(Method.class);
 Method testMethod2 = mock(Method.class);
 when(testMethod.getName()).thenReturn("testMethod");
 when(testMethod2.getName()).thenReturn("testMethod2");
 when(testClass.getDeclaredMethods()).thenReturn(new Method[]{testMethod, testMethod2});
 reflectionSearch.methodsThatHave("testMethod");
 reflectionSearch.and();
 reflectionSearch.methodsThatDontHave("testMethod2");
 List<Method> expected = Arrays.asList(testMethod);
 assertEquals(1, reflectionSearch.getMethods().size());
 assertEquals(expected, reflectionSearch.getMethods());
 }

 @Test
 public void testOr() throws BeanBinException {
 Method testMethod = mock(Method.class);
 Method testMethod2 = mock(Method.class);
 when(testMethod.getName()).thenReturn("testMethod");
 when(testMethod2.getName()).thenReturn("testMethod2");
 when(testClass.getDeclaredMethods()).thenReturn(new Method[]{testMethod, testMethod2});
 reflectionSearch.methodsThatHave("testMethod");
 reflectionSearch.or();
 reflectionSearch.methodsThatDontHave("testMethod2");
 List<Method> expected = new ArrayList<>();
 expected.add(testMethod);
 expected.add(testMethod2);
 assertEquals(2, reflectionSearch.getMethods().size());
 assertFalse(expected.containsAll(reflectionSearch.getMethods()));
 }
}