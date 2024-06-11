package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.beanbin.query.Criteria;
import net.sourceforge.beanbin.query.Conditional;
import net.sourceforge.beanbin.query.ReflectionCriteria;
import net.sourceforge.beanbin.query.ReflectionShelf;
import net.sourceforge.beanbin.reflect.ReflectionSearch;
import org.junit.Test;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class testClass;

 @Before
 public void setup() {
 testClass = TestClassWithMethods.class;
 reflectionSearch = new ReflectionSearch(testClass);
 }

 @Test
 public void testHasAnnotationWithExactMatch() {
 Class testAnnotation = TestAnnotation.class;
 assertTrue(reflectionSearch.hasAnnotation(testAnnotation.getName()));
 }

 @Test
 public void testHasAnnotationWithClassNameMatch() {
 String testAnnotationClassName = TestAnnotation.class.getName();
 assertTrue(reflectionSearch.hasAnnotation(testAnnotationClassName.substring(testAnnotationClassName.lastIndexOf(".") + 1)));
 }

 @Test
 public void testHasAnnotationWithNoMatch() {
 assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
 }

 @Test
 public void testHasAnnotationWithWildcardMatch() {
 String testAnnotationName = "TestAnnot*";
 assertTrue(reflectionSearch.hasAnnotation(testAnnotationName));
 }

 @Test
 public void testHasWildcardWithNoWildcard() {
 assertFalse(reflectionSearch.hasWildcard("TestAnnotation"));
 }

 @Test
 public void testHasWildcardWithWildcard() {
 String testAnnotationName = "TestAnnot*";
 assertTrue(reflectionSearch.hasWildcard(testAnnotationName));
 }

 @Test
 public void testMethodsThatHaveWithEmptyQuery() throws BeanBinException {
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testMethodsThatHaveWithSingleTerm() throws BeanBinException {
 reflectionSearch.methodsThatHave("testMethod");
 List<Method> methods = reflectionSearch.getMethods();
 List<Method> expectedMethods = Arrays.asList(testClass.getMethods());
 assertEquals(expectedMethods.size(), methods.size());
 for (int i = 0; i < expectedMethods.size(); i++) {
 Method expectedMethod = expectedMethods.get(i);
 String expectedMethodName = expectedMethod.getName();
 Method actualMethod = methods.get(i);
 String actualMethodName = actualMethod.getName();
 assertEquals(expectedMethodName, actualMethodName);
 }
 }

 @Test
 public void testMethodsThatHaveWithMultipleTerms() throws BeanBinException {
 reflectionSearch.methodsThatHave("testMethod1").methodsThatHave("testMethod2");
 List<Method> methods = reflectionSearch.getMethods();
 List<Method> expectedMethods = new ArrayList<>();
 expectedMethods.addAll(Arrays.asList(testClass.getMethods()));
 expectedMethods.removeIf(method -> !method.getName().matches("testMethod[12]"));
 assertEquals(expectedMethods.size(), methods.size());
 for (int i = 0; i < expectedMethods.size(); i++) {
 Method expectedMethod = expectedMethods.get(i);
 String expectedMethodName = expectedMethod.getName();
 Method actualMethod = methods.get(i);
 String actualMethodName = actualMethod.getName();
 assertEquals(expectedMethodName, actualMethodName);
 }
 }

 @Test
 public void testMethodsThatDontHaveWithEmptyQuery() throws BeanBinException {
 reflectionSearch.methodsThatDontHave("");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(testClass.getMethods().length, methods.size());
 }

 @Test
 public void testMethodsThatDontHaveWithSingleTerm() throws BeanBinException {
 reflectionSearch.methodsThatDontHave("nonExistentMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(testClass.getMethods().length - 1, methods.size());
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatDontHaveWithNonExistentClass() throws BeanBinException {
 reflectionSearch.methodsThatDontHave("NonExistentClass");
 }

 @Test
 public void testAndOperator() throws BeanBinException {
 reflectionSearch.methodsThatHave("testMethod1").and();
 reflectionSearch.methodsThatHave("testMethod3");
 List<Method> methods = reflectionSearch.getMethods();
 List<Method> expectedMethods = new ArrayList<>();
 Method testMethod1 = testClass.getMethod("testMethod1");
 Method testMethod3 = testClass.getMethod("testMethod3");
 expectedMethods.add(testMethod1);
 expectedMethods.add(testMethod3);
 assertEquals(expectedMethods.size(), methods.size());
 for (int i = 0; i < expectedMethods.size(); i++) {
 Method expectedMethod = expectedMethods.get(i);
 Method actualMethod = methods.get(i);
 assertEquals(expectedMethod.getName(), actualMethod.getName());
 }
 }

 @Test
 public void testOrOperator() throws BeanBinException {
 reflectionSearch.methodsThatHave("testMethod1");
 reflectionSearch.or();
 reflectionSearch.methodsThatHave("testMethod2");
 List<Method> methods = reflectionSearch.getMethods();
 List<Method> expectedMethods = new ArrayList<>();
 Method testMethod1 = testClass.getMethod("testMethod1");
 Method testMethod2 = testClass.getMethod("testMethod2");
 expectedMethods.add(testMethod1);
 expectedMethods.add(testMethod2);
 assertEquals(expectedMethods.size(), methods.size());
 for (int i = 0; i < expectedMethods.size(); i++) {
 Method expectedMethod = expectedMethods.get(i);
 Method actualMethod = methods.get(i);
 assertEquals(expectedMethod.getName(), actualMethod.getName());
 }
 }

 @Test
 public void testGetMethodsWithEmptyQuery() throws BeanBinException {
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testGetMethodsWithSingleTerm() throws BeanBinException {
 reflectionSearch.methodsThatHave("testMethod");
 List<Method> methods = reflectionSearch.getMethods();
 List<Method> expectedMethods = Arrays.asList(testClass.getMethods());
 assertEquals(expectedMethods.size(), methods.size());
 }

 @Test
 public void testGetMethodsWithMultipleTerms() throws BeanBinException {
 reflectionSearch.methodsThatHave("testMethod1").methodsThatHave("testMethod2");
 List<Method> methods = reflectionSearch.getMethods();
 List<Method> expectedMethods = new ArrayList<>();
 expectedMethods.addAll(Arrays.asList(testClass.getMethods()));
 expectedMethods.removeIf(method -> !method.getName().matches("testMethod[12]"));
 assertEquals(expectedMethods.size(), methods.size());
 for (int i = 0; i < expectedMethods.size(); i++) {
 Method expectedMethod = expectedMethods.get(i);
 Method actualMethod = methods.get(i);
 assertEquals(expectedMethod.getName(), actualMethod.getName());
 }
 }

 @Test
 public void testIntersect() {
 List<Method> master = Arrays.asList(new Method[] { testClass.getMethod("testMethod1"), testClass.getMethod("testMethod2") });
 List<Method> toadd = Arrays.asList(new Method[] { testClass.getMethod("testMethod2") });
 reflectionSearch.intersect(master, toadd);
 List<Method> result = new ArrayList<>(master);
 result.remove(result.indexOf(testClass.getMethod("testMethod2")));
 List<Method> expectedResult = result;
 assertEquals(expectedResult.size(), master.size());
 for (int i = 0; i < expectedResult.size(); i++) {
 Method expectedMethod = expectedResult.get(i);
 Method actualMethod = master.get(i);
 assertEquals(expectedMethod.getName(), actualMethod.getName());
 }
 }

 @Test
 public void testUnion() {
 List<Method> master = Collections.singletonList(testClass.getMethod("testMethod1"));
 List<Method> toadd = Arrays.asList(new Method[] { testClass.getMethod("testMethod2") });
 reflectionSearch.union(master, toadd);
 List<Method> result = new ArrayList<>();
 result.addAll(master);
 result.addAll(toadd);
 List<Method> expectedResult = result;
 assertEquals(expectedResult.size(), master.size() + toadd.size());
 for (int i = 0; i < expectedResult.size(); i++) {
 Method expectedMethod = expectedResult.get(i);
 Method actualMethod = result.get(i);
 assertEquals(expectedMethod.getName(), actualMethod.getName());
 }
 }
}