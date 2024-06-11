package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.beanbin.query.*;
import net.sourceforge.beanbin.search.ReflectionCriteria;
import net.sourceforge.beanbin.search.ReflectionShelf;
import org.junit.Before;
import org.junit.Test;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class testClass;
 private ReflectionShelf reflectionShelf = mock(ReflectionShelf.class);
 private List<Method> methods;

 @Before
 public void setup() {
 testClass = Mockito.mock(Class.class);
 reflectionSearch = new ReflectionSearch(testClass);
 ReflectionShelf.setInstance(reflectionShelf);
 methods = new ArrayList<>();
 }

 @Test
 public void testHasAnnotation() {
 reflectionSearch.hasAnnotation("Annotation1");
 verify(testClass, times(1)).getAnnotations();

 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.hasAnnotation("annotation1");
 assertFalse(reflectionSearch.hasAnnotation("annotation1"));

 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.hasAnnotation("Annotation1*");
 assertTrue(reflectionSearch.hasAnnotation("Annotation1Test"));

 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.hasAnnotation("*Annotation1");
 assertTrue(reflectionSearch.hasAnnotation(new Annotation() {}.getClass().getAnnotation(Annotation1.class)));
 }

 @Test
 public void testMethodsThatHave() throws BeanBinException {
 Method method1 = mock(Method.class);
 Method method2 = mock(Method.class);
 when(testClass.getMethods()).thenReturn(new Object[] { method1, method2 });
 reflectionSearch.methodsThatHave("method1");
 assertEquals(Arrays.asList(method1), reflectionSearch.getMethods());

 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("Method1");
 assertEquals(Arrays.asList(method1), reflectionSearch.getMethods());

 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("method*");
 assertEquals(Arrays.asList(method1, method2), reflectionSearch.getMethods());

 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("*method");
 assertFalse(reflectionSearch.getMethods().isEmpty());
 }

 @Test
 public void testMethodsThatDontHave() throws BeanBinException {
 Method method1 = mock(Method.class);
 Method method2 = mock(Method.class);
 when(testClass.getMethods()).thenReturn(new Object[] { method1, method2 });
 reflectionSearch.methodsThatDontHave("method1");
 assertEquals(Arrays.asList(method2), reflectionSearch.getMethods());

 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatDontHave("Method1");
 assertEquals(Arrays.asList(method1), reflectionSearch.getMethods());

 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatDontHave("method*");
 assertEquals(Arrays.asList(method1, method2), reflectionSearch.getMethods());

 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatDontHave("*method");
 assertFalse(reflectionSearch.getMethods().isEmpty());
 }

 @Test
 public void testAnd() throws BeanBinException {
 Method method1 = mock(Method.class);
 Method method2 = mock(Method.class);
 Method method3 = mock(Method.class);
 when(testClass.getMethods()).thenReturn(new Object[] { method1, method2, method3 });
 reflectionSearch.methodsThatHave("method1");
 reflectionSearch.and();
 reflectionSearch.methodsThatHave("method2");
 assertEquals(Arrays.asList(method1, method2), reflectionSearch.getMethods());

 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("Method1");
 reflectionSearch.and();
 reflectionSearch.methodsThatHave("Method2");
 assertEquals(Arrays.asList(method1), reflectionSearch.getMethods());

 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("method*");
 reflectionSearch.and();
 reflectionSearch.methodsThatDontHave("method3");
 assertEquals(Arrays.asList(method1), reflectionSearch.getMethods());
 }

 @Test
 public void testOr() throws BeanBinException {
 Method method1 = mock(Method.class);
 Method method2 = mock(Method.class);
 Method method3 = mock(Method.class);
 when(testClass.getMethods()).thenReturn(new Object[] { method1, method2, method3 });
 reflectionSearch.methodsThatHave("method1");
 reflectionSearch.or();
 reflectionSearch.methodsThatHave("method2");
 assertEquals(Arrays.asList(method1, method2, method3), reflectionSearch.getMethods());

 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("Method1");
 reflectionSearch.or();
 reflectionSearch.methodsThatHave("Method3");
 assertEquals(Arrays.asList(method1, method3), reflectionSearch.getMethods());

 reflectionSearch = new ReflectionSearch(testClass);
 reflectionSearch.methodsThatHave("method*");
 reflectionSearch.or();
 reflectionSearch.methodsThatDontHave("method3");
 assertEquals(Arrays.asList(method1, method2, method3), reflectionSearch.getMethods());
 }
}