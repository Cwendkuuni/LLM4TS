package net.sourceforge.beanbin.reflect.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.search.BeanBinException;
import net.sourceforge.beanbin.search.Criteria;
import net.sourceforge.beanbin.search.ReflectionCriteria;
import net.sourceforge.beanbin.search.Conditional;

import org.junit.Test;

public class ReflectionSearchTest {

 private static final String ANNOTATION_TEST = "MyAnnotation";
 private static final String ANNOTATION_TEST_WILDCARD = "MyAnno*";
 private static final String METHOD_NAME_TEST = "testMethod";
 private static final String METHOD_NAME_TEST_WILDCARD = "testMetho*";
 private static final String NON_EXISTING_METHOD_NAME = "nonExistingMethod";

 @Test
 public void testConstructor() {
 ReflectionSearch search = new ReflectionSearch(Object.class);
 assertNotNull(search);
 }

 @Test
 public void testHasAnnotation_ExactMatch() throws BeanBinException {
 ReflectionSearch search = new ReflectionSearch(AnnotationTestClass.class);
 assertTrue(search.hasAnnotation("AnnotationTest"));
 }

 @Test
 public void testHasAnnotation_ClassMatch() throws BeanBinException {
 ReflectionSearch search = new ReflectionSearch(AnnotationTestClass.class);
 assertTrue(search.hasAnnotation("Test"));
 }

 @Test
 public void testHasAnnotation_NoMatch() throws BeanBinException {
 ReflectionSearch search = new ReflectionSearch(AnnotationTestClass.class);
 assertFalse(search.hasAnnotation("NonExistingAnnotation"));
 }

 @Test
 public void testHasAnnotation_WildcardMatch() throws BeanBinException {
 ReflectionSearch search = new ReflectionSearch(AnnotationTestClass.class);
 assertTrue(search.hasAnnotation("AnnotationTest*"));
 }

 @Test
 public void testHasAnnotation_WildcardClassMatch() throws BeanBinException {
 ReflectionSearch search = new ReflectionSearch(AnnotationTestClass.class);
 assertTrue(search.hasAnnotation("Test*"));
 }

 @Test
 public void testHasWildcard_True() {
 assertTrue(ReflectionSearch.hasWildcard("AnnotationTest*"));
 }

 @Test
 public void testHasWildcard_False() {
 assertFalse(ReflectionSearch.hasWildcard("AnnotationTest"));
 }

 @Test
 public void testMethodsThatHave_EmptyQuery() throws BeanBinException {
 ReflectionSearch search = new ReflectionSearch(AnnotationTestClass.class);
 search.methodsThatHave("");
 List<Method> methods = search.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testMethodsThatHave_SingleMatch() throws BeanBinException {
 ReflectionSearch search = new ReflectionSearch(AnnotationTestClass.class);
 search.methodsThatHave(METHOD_NAME_TEST);
 List<Method> methods = search.getMethods();
 assertEquals(1, methods.size());
 assertSame(AnnotationTestClass.class.getMethod(METHOD_NAME_TEST), methods.get(0));
 }

 @Test
 public void testMethodsThatHave_MultipleMatches() throws BeanBinException {
 ReflectionSearch search = new ReflectionSearch(AnnotationTestClass.class);
 Method method1 = AnnotationTestClass.class.getMethod("method1");
 Method method2 = AnnotationTestClass.class.getMethod("method2");
 search.methodsThatHave(METHOD_NAME_TEST_WILDCARD);
 List<Method> methods = search.getMethods();
 assertTrue(methods.contains(method1));
 assertTrue(methods.contains(method2));
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatHave_NoMatches() throws BeanBinException {
 ReflectionSearch search = new ReflectionSearch(AnnotationTestClass.class);
 search.methodsThatHave(NON_EXISTING_METHOD_NAME);
 }

 @Test
 public void testMethodsThatDontHave_EmptyQuery() throws BeanBinException {
 ReflectionSearch search = new ReflectionSearch(AnnotationTestClass.class);
 search.methodsThatDontHave("");
 List<Method> methods = search.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testMethodsThatDontHave_SingleMatch() throws BeanBinException {
 ReflectionSearch search = new ReflectionSearch(AnnotationTestClass.class);
 search.methodsThatDontHave(METHOD_NAME_TEST);
 List<Method> methods = search.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testMethodsThatDontHave_MultipleMatches() throws BeanBinException {
 ReflectionSearch search = new ReflectionSearch(AnnotationTestClass.class);
 Method method1 = AnnotationTestClass.class.getMethod("method1");
 Method method2 = AnnotationTestClass.class.getMethod("method2");
 search.methodsThatDontHave(METHOD_NAME_TEST);
 List<Method> methods = search.getMethods();
 assertFalse(methods.contains(method1));
 assertFalse(methods.contains(method2));
 }

 @Test
 public void testAndOperation() throws BeanBinException {
 ReflectionSearch search = new ReflectionSearch(AnnotationTestClass.class);
 search.methodsThatHave(METHOD_NAME_TEST);
 List<Method> methods1 = search.getMethods();
 search.methodsThatDontHave("method2");
 List<Method> methods2 = search.getMethods();
 assertEquals(1, methods1.size());
 assertEquals(1, methods2.size());
 search.and();
 List<Method> methods3 = search.getMethods();
 assertEquals(0, methods3.size());
 }

 @Test
 public void testOrOperation() throws BeanBinException {
 ReflectionSearch search = new ReflectionSearch(AnnotationTestClass.class);
 search.methodsThatHave(METHOD_NAME_TEST);
 List<Method> methods1 = search.getMethods();
 search.methodsThatDontHave("method2");
 List<Method> methods2 = search.getMethods();
 assertEquals(1, methods1.size());
 assertEquals(1, methods2.size());
 search.or();
 List<Method> methods3 = search.getMethods();
 assertEquals(2, methods3.size());
 List<Method> expectedMethods = new ArrayList<Method>(Arrays.asList(methods1.get(0), methods2.get(0)));
 assertArrayEquals(methods3.toArray(), expectedMethods.toArray());
 }

}

class AnnotationTestClass {
 @AnnotationTest
 public void testMethod() {
 }

 @AnnotationTest
 public void method1() {
 }

 @AnnotationTest
 public void method2() {
 }

 @AnnotationTest
 public void testAnnotatedClass() {
 }

 @interface AnnotationTest {
 }

}