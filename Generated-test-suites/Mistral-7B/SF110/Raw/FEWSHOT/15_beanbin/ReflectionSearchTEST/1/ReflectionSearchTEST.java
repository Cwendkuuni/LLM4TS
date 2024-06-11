package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.beanbin.query.*;
import net.sourceforge.beanbin.search.*;
import org.junit.Before;
import org.junit.Test;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class clazz;
 private Query query;
 private List<Method> methods;
 private MethodReflectionCriteria methodCriteria;
 private ReflectionCriteria reflectionCriteria;
 private List<Method> methodsToAdd;
 private List<Method> methodsToIntersect;
 private List<Method> methodsToUnion;

 @Before
 public void setup() {
 clazz = mock(Class.class);
 query = mock(Query.class);
 methods = new ArrayList<>();
 reflectionSearch = new ReflectionSearch(clazz);

 methodCriteria = mock(MethodReflectionCriteria.class);
 reflectionCriteria = mock(ReflectionCriteria.class);
 methodsToAdd = Arrays.asList(mock(Method.class), mock(Method.class));
 methodsToIntersect = Arrays.asList(mock(Method.class), mock(Method.class));
 methodsToUnion = Arrays.asList(mock(Method.class), mock(Method.class), mock(Method.class));
 }

 @Test
 public void testHasAnnotation() {
 final Annotation annotation = mock(Annotation.class);
 when(clazz.getAnnotations()).thenReturn(new Annotation[]{annotation});
 when(annotation.annotationType().getName()).thenReturn("com.example.MyAnnotation");

 assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));
 }

 @Test
 public void testHasAnnotationWildcard() {
 when(clazz.getAnnotations()).thenReturn(new Annotation[]{mock(Annotation.class)});
 when(reflectionSearch.hasWildcard("Annot*")).thenReturn(true);

 assertTrue(reflectionSearch.hasAnnotation("Annotation"));
 assertTrue(reflectionSearch.hasAnnotation("AnnotType"));
 assertTrue(reflectionSearch.hasAnnotation("AnnotClass"));
 }

 @Test
 public void testHasAnnotationFalse() {
 when(clazz.getAnnotations()).thenReturn(new Annotation[]{});

 assertFalse(reflectionSearch.hasAnnotation("MyAnnotation"));
 }

 @Test
 public void testMethodsThatHave() throws BeanBinException {
 reflectionSearch.methodsThatHave("getName");

 methodCriteria = mock(MethodReflectionCriteria.class);
 when(methodCriteria.getResults()).thenReturn(methodsToAdd);
 when(query.getCriterias()).thenReturn(Arrays.asList(methodCriteria));

 List<Method> result = reflectionSearch.getMethods();
 assertEquals(methodsToAdd, result);
 }

 @Test
 public void testMethodsThatDontHave() throws BeanBinException {
 reflectionSearch.methodsThatDontHave("getName");

 methodCriteria = mock(MethodReflectionCriteria.class);
 when(methodCriteria.getResults()).thenReturn(methodsToAdd);
 when(query.getCriterias()).thenReturn(Arrays.asList(methodCriteria));

 List<Method> result = reflectionSearch.getMethods();
 assertTrue(result.isEmpty());
 }

 @Test
 public void testAnd() throws BeanBinException {
 reflectionSearch.methodsThatHave("getName");
 reflectionSearch.and();
 reflectionSearch.methodsThatHave("getAge");

 methodCriteria = mock(MethodReflectionCriteria.class);
 reflectionCriteria = mock(ReflectionCriteria.class);

 when(methodCriteria.getResults()).thenReturn(methodsToIntersect);
 when(reflectionCriteria.getResults()).thenReturn(methodsToAdd);
 when(query.getCriterias()).thenReturn(Arrays.asList(methodCriteria, reflectionCriteria));

 List<Method> result = reflectionSearch.getMethods();
 assertEquals(Arrays.asList(methodsToIntersect.get(0)), result);
 }

 @Test
 public void testOr() throws BeanBinException {
 reflectionSearch.methodsThatHave("getName");
 reflectionSearch.or();
 reflectionSearch.methodsThatHave("getAge");
 reflectionSearch.methodsThatHave("getSize");

 methodCriteria = mock(MethodReflectionCriteria.class);
 reflectionCriteria = mock(ReflectionCriteria.class);

 when(methodCriteria.getResults()).thenReturn(methodsToUnion.subList(0, 2));
 when(reflectionCriteria.getResults()).thenReturn(methodsToUnion.subList(2, methodsToUnion.size()));
 when(query.getCriterias()).thenReturn(Arrays.asList(methodCriteria, reflectionCriteria));

 List<Method> result = reflectionSearch.getMethods();
 assertEquals(methodsToUnion, result);
 }
}