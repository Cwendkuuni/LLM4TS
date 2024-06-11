package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.beanbin.query.Criteria;
import net.sourceforge.beanbin.query.Conditional;
import net.sourceforge.beanbin.query.ReflectionCriteria;
import net.sourceforge.beanbin.query.ReflectionShelf;
import net.sourceforge.beanbin.search.MethodReflectionCriteria;
import org.junit.Before;
import org.junit.Test;

public class ReflectionSearchTest {

 private Class clazz;
 private ReflectionSearch reflectionSearch;
 private List<Method> methods;

 @Before
 public void setUp() {
 clazz = TestClass.class;
 reflectionSearch = new ReflectionSearch(clazz);
 methods = new ArrayList<>();
 }

 @Test
 public void testHasAnnotation() {
 Class<AnnotatedAnnotation> annotatedAnnotationClass = AnnotatedAnnotation.class;
 Annotation annotation = clazz.getAnnotation(annotatedAnnotationClass);
 assertTrue(reflectionSearch.hasAnnotation(AnnotatedAnnotation.class.getName()));
 assertTrue(reflectionSearch.hasAnnotation(annotation.annotationType().getName()));
 assertFalse(reflectionSearch.hasAnnotation("NotAnAnnotation"));
 assertFalse(reflectionSearch.hasAnnotation("Annot*ation"));
 }

 @Test
 public void testHasWildcard() {
 assertTrue(reflectionSearch.hasWildcard("Annot*ation"));
 assertFalse(reflectionSearch.hasWildcard("Annotation"));
 }

 @Test
 public void testMethodsThatHave() throws BeanBinException {
 reflectionSearch.methodsThatHave("getName").getMethods();
 methods.add(clazz.getMethod("getName", new Class[0]));
 reflectionSearch.getMethods();
 assertEquals(1, methods.size());
 }

 @Test
 public void testMethodsThatDontHave() throws BeanBinException {
 reflectionSearch.methodsThatDontHave("getName").getMethods();
 List<Method> expectedMethods = new ArrayList<>();
 methods.remove(0);
 reflectionSearch.getMethods();
 assertEquals(methods.size(), expectedMethods.size());
 for (Method method : expectedMethods) {
 assertTrue(methods.contains(method));
 }
 }

 @Test
 public void testAnd() throws BeanBinException {
 reflectionSearch.methodsThatHave("getName").and();
 reflectionSearch.methodsThatHave("setName");
 methods.clear();
 reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testOr() throws BeanBinException {
 reflectionSearch.methodsThatHave("getName").or();
 reflectionSearch.methodsThatHave("setName");
 methods.clear();
 reflectionSearch.getMethods();
 methods.add(clazz.getMethod("getName", new Class[0]));
 methods.add(clazz.getMethod("setName", new Class[0]));
 reflectionSearch.getMethods();
 assertEquals(2, methods.size());
 }

 @Test
 public void testGetMethods() throws BeanBinException {
 methods.clear();
 reflectionSearch.getMethods();
 assertNull(methods);

 methods.add(clazz.getMethod("getName", new Class[0]));
 reflectionSearch.methodsThatHave("getName");
 methods.clear();
 reflectionSearch.getMethods();
 assertNotNull(methods);
 assertEquals(1, methods.size());
 }

 @Test
 public void testReflectionShelf() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatHave("getName");
 methods.clear();
 reflectionSearch.getMethods();
 ReflectionShelf reflectionShelf = ReflectionShelf.getInstance();
 assertNotNull(reflectionShelf);
 List<Method> ShelfMethods = (List<Method>) reflectionShelf.get(reflectionSearch.query);
 assertNull(ShelfMethods);
 reflectionSearch.getMethods();
 reflectionShelf = ReflectionShelf.getInstance();
 ShelfMethods = (List<Method>) reflectionShelf.get(reflectionSearch.query);
 assertNotNull(ShelfMethods);
 assertEquals(1, ShelfMethods.size());
 assertEquals(methods.get(0), ShelfMethods.get(0));
 }
}

class TestClass {
 String name;
}