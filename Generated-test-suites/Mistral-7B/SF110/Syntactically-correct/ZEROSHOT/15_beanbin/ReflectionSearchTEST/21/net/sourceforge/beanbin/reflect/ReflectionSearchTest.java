package net.sourceforge.beanbin.reflect;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class testClass;

 @Before
 public void setup() {
 testClass = Mockito.mock(Class.class);
 reflectionSearch = new ReflectionSearch(testClass);
 }

 @Test
 public void testHasAnnotation() {
 reflectionSearch.query.add(new AnnotationCriteria(AnnotationType.classThatAnnotatesTest, SearchType.EQUALS));
 assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
 reflectionSearch.query.add(new AnnotationCriteria(AnnotationType.classThatAnnotatesTest.getClass(), SearchType.EQUALS));
 assertTrue(reflectionSearch.hasAnnotation("AnnotationName"));
 }

 @Test
 public void testMethodsThatHave() throws BeanBinException {
 reflectionSearch.methodsThatHave("testMethod");
 List<Method> expectedMethods = Arrays.asList(testMethod1, testMethod2);
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(expectedMethods, methods);
 }

 @Test
 public void testMethodsThatDontHave() throws BeanBinException {
 reflectionSearch.methodsThatDontHave("testMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertTrue(methods.isEmpty());
 }

 @Test
 public void testAnd() throws BeanBinException {
 reflectionSearch.methodsThatHave("testMethod");
 reflectionSearch.and();
 reflectionSearch.methodsThatHave("private");
 List<Method> methods = reflectionSearch.getMethods();
 List<Method> expectedMethods = Arrays.asList(testMethod2);
 assertTrue(methods.size() == 1);
 assertTrue(methods.get(0).isAccessible());
 assertEquals(expectedMethods.get(0).getName(), methods.get(0).getName());
 }

 @Test
 public void testOr() throws BeanBinException {
 reflectionSearch.methodsThatHave("testMethod");
 reflectionSearch.or();
 reflectionSearch.methodsThatHave("public");
 List<Method> methods = reflectionSearch.getMethods();
 List<Method> expectedMethods = Arrays.asList(testMethod1, testMethod2);
 assertTrue(methods.size() == 2);
 assertTrue(methods.get(0).isAccessible());
 assertEquals(expectedMethods.get(0).getName(), methods.get(0).getName());
 assertTrue(methods.get(1).isAccessible());
 assertEquals(expectedMethods.get(1).getName(), methods.get(1).getName());
 }

 @Test(expected = BeanBinException.class)
 public void testGetMethodsWithInvalidQuery() throws BeanBinException {
 reflectionSearch.query = null;
 reflectionSearch.getMethods();
 }

 private static class AnnotationType {
 @Retention(RetentionPolicy.RUNTIME)
 @Target(ElementType.TYPE)
 public @interface classThatAnnotatesTest {
 String value();
 }
 }

 private static class AnnotationCriteria extends ReflectionCriteria {
 private final Class annotationType;

 public AnnotationCriteria(Class annotationType, SearchType searchType) {
 super(SearchType.ANNOTATION, searchType);
 this.annotationType = annotationType;
 }

 @Override
 public List<Method> getResults() {
 List<Method> methods = new ArrayList<>();
 for (Method method : testClass.getMethods()) {
 if (method.isAnnotationPresent(annotationType)) {
 methods.add(method);
 }
 }
 return methods;
 }
 }

 private static Method testMethod1 = new Method("testMethod1", String[].class);
 private static Method testMethod2 = new Method("testMethod2", void.class);
}