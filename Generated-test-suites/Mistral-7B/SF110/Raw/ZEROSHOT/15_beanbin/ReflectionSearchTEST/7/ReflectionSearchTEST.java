package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.search.Criteria;
import net.sourceforge.beanbin.search.Conditional;
import net.sourceforge.beanbin.search.MethodReflectionCriteria;
import net.sourceforge.beanbin.search.Query;
import net.sourceforge.beanbin.search.ReflectionCriteria;
import net.sourceforge.beanbin.search.SearchType;

import org.junit.Test;

public class ReflectionSearchTest {

 private static final Class<ReflectionSearch> CLASS_TO_TEST = ReflectionSearch.class;

 @Test
 public void testHasAnnotationWithExactMatch() throws Exception {
 ReflectionSearch search = new ReflectionSearch(CLASS_TO_TEST);
 assertTrue(search.hasAnnotation("AnnotationType"));
 }

 @Test
 public void testHasAnnotationWithClassMatch() throws Exception {
 ReflectionSearch search = new ReflectionSearch(CLASS_TO_TEST);
 assertTrue(search.hasAnnotation("ReflectionSearch"));
 }

 @Test
 public void testHasAnnotationWithWildcardMatch() throws Exception {
 ReflectionSearch search = new ReflectionSearch(CLASS_TO_TEST);
 assertTrue(search.hasAnnotation("Annotation*"));
 assertTrue(search.hasAnnotation("Reflectio*Search"));
 }

 @Test(expected = BeanBinException.class)
 public void testHasAnnotationWithNoMatch() throws Exception {
 ReflectionSearch search = new ReflectionSearch(CLASS_TO_TEST);
 assertFalse(search.hasAnnotation("NonExistentAnnotation"));
 }

 @Test
 public void testMethodsThatHave() throws Exception {
 ReflectionSearch search = new ReflectionSearch(CLASS_TO_TEST);
 String term = "test";
 search.methodsThatHave(term);
 List<Method> methods = search.getMethods();
 List<Method> expectedMethods = Arrays.asList(ReflectionSearchTest.class.getDeclaredMethods());
 for (Method method : expectedMethods) {
 String name = method.getName();
 assertTrue(name.contains(term) || name.startsWith(term));
 }
 for (Method method : methods) {
 assertEquals("Unexpected method found", expectedMethods.get(0).getName(), method.getName());
 }
 }

 @Test
 public void testMethodsThatDontHave() throws Exception {
 ReflectionSearch search = new ReflectionSearch(CLASS_TO_TEST);
 String term = "test";
 search.methodsThatDontHave(term);
 List<Method> methods = search.getMethods();
 List<Method> expectedMethods = Arrays.asList(ReflectionSearchTest.class.getDeclaredMethods());
 for (Method method : expectedMethods) {
 String name = method.getName();
 assertFalse(name.contains(term) || name.startsWith(term));
 }
 for (Method method : methods) {
 assertTrue("Unexpected method found", method.getName().contains(term) || method.getName().startsWith(term));
 }
 }

 @Test
 public void testMethodsThatHaveAndMethodsThatDontHave() throws Exception {
 ReflectionSearch search = new ReflectionSearch(CLASS_TO_TEST);
 String term = "test";
 search.methodsThatHave(term).methodsThatDontHave(term);
 List<Method> methods = search.getMethods();
 assertEquals("Unexpected number of methods found", 0, methods.size());
 }

 @Test
 public void testMethodsThatHaveAndMethodsThatHave() throws Exception {
 ReflectionSearch search = new ReflectionSearch(CLASS_TO_TEST);
 String term = "test";
 List<Method> methods = search.methodsThatHave(term).getMethods();
 List<Method> expectedMethods = Arrays.asList(ReflectionSearchTest.class.getDeclaredMethods());
 for (Method method : methods) {
 String name = method.getName();
 assertTrue(name.contains(term) || name.startsWith(term));
 }
 for (Method method : expectedMethods) {
 String name = method.getName();
 assertTrue(name.contains(term) || name.startsWith(term));
 }
 }

 @Test
 public void testMethodsThatDontHaveAndMethodsThatHave() throws Exception {
 ReflectionSearch search = new ReflectionSearch(CLASS_TO_TEST);
 String term = "test";
 List<Method> methods = search.methodsThatDontHave(term).getMethods();
 List<Method> expectedMethods = Arrays.asList(ReflectionSearchTest.class.getDeclaredMethods());
 for (Method method : expectedMethods) {
 String name = method.getName();
 assertFalse(name.contains(term) || name.startsWith(term));
 }
 for (Method method : methods) {
 String name = method.getName();
 assertTrue(name.contains(term) || name.startsWith(term));
 }
 }

 @Test
 public void testMethodsThatHaveAndOrMethodsThatHave() throws Exception {
 ReflectionSearch search = new ReflectionSearch(CLASS_TO_TEST);
 String term1 = "test";
 String term2 = "anotherTest";
 search.methodsThatHave(term1).or();
 List<Method> methods = search.methodsThatHave(term2).getMethods();
 List<Method> expectedMethods = Arrays.asList(ReflectionSearchTest.class.getDeclaredMethods());
 int counter = 0;
 for (Method method : methods) {
 String name = method.getName();
 assertTrue(name.contains(term1) || name.startsWith(term1));
 assertTrue(name.contains(term2) || name.startsWith(term2));
 counter++;
 }
 for (Method method : expectedMethods) {
 String name = method.getName();
 assertTrue(name.contains(term1) || name.startsWith(term1));
 assertTrue(name.contains(term2) || name.startsWith(term2));
 counter++;
 }
 assertEquals("Unexpected number of methods found", expectedMethods.size(), counter);
 }

 @Test
 public void testMethodsThatHaveAndAndMethodsThatHave() throws Exception {
 ReflectionSearch search = new ReflectionSearch(CLASS_TO_TEST);
 String term1 = "test";
 String term2 = "anotherTest";
 search.methodsThatHave(term1).and();
 List<Method> methods = search.methodsThatHave(term2).getMethods();
 List<Method> expectedMethods = Arrays.asList(ReflectionSearchTest.class.getDeclaredMethods());
 int counter = 0;
 for (Method method : methods) {
 String name = method.getName();
 assertTrue(name.contains(term1) && name.contains(term2));
 counter++;
 }
 for (Method method : expectedMethods) {
 String name = method.getName();
 assertTrue(name.contains(term1) && name.contains(term2));
 counter++;
 }
 assertEquals("Unexpected number of methods found", expectedMethods.size(), counter);
 }

 @Test
 public void testMethodsThatDontHaveAndAndMethodsThatDontHave() throws Exception {
 ReflectionSearch search = new ReflectionSearch(CLASS_TO_TEST);
 String term1 = "test";
 String term2 = "anotherTest";
 search.methodsThatDontHave(term1).and();
 List<Method> methods = search.methodsThatDontHave(term2).getMethods();
 List<Method> expectedMethods = Arrays.asList(ReflectionSearchTest.class.getDeclaredMethods());
 for (Method method : expectedMethods) {
 String name = method.getName();
 assertFalse(name.contains(term1) && name.contains(term2));
 }
 for (Method method : methods) {
 String name = method.getName();
 assertTrue(name.contains(term1) && name.contains(term2));
 fail("Unexpected method found");
 }
 }

 @Test
 public void testMethodsThatDontHaveAndOrMethodsThatHave() throws Exception {
 ReflectionSearch search = new ReflectionSearch(CLASS_TO_TEST);
 String term1 = "test";
 String term2 = "anotherTest";
 List<Method> methods = search.methodsThatDontHave(term1).or().getMethods();
 List<Method> expectedMethods = Arrays.asList(ReflectionSearchTest.class.getDeclaredMethods());
 int counter = 0;
 for (Method method : methods) {
 String name = method.getName();
 assertFalse(name.contains(term1) || name.startsWith(term1));
 assertTrue(name.contains(term2) || name.startsWith(term2));
 counter++;
 }
 for (Method method : expectedMethods) {
 String name = method.getName();
 assertFalse(name.contains(term1) || name.startsWith(term1));
 assertTrue(name.contains(term2) || name.startsWith(term2));
 counter++;
 }
 assertEquals("Unexpected number of methods found", expectedMethods.size(), counter);
 }

 @Test
 public void testMethodsThatHaveAndOrMethodsThatDontHave() throws Exception {
 ReflectionSearch search = new ReflectionSearch(CLASS_TO_TEST);
 String term1 = "test";
 String term2 = "anotherTest";
 List<Method> methods = search.methodsThatHave(term1).or().getMethods();
 List<Method> expectedMethods = Arrays.asList(ReflectionSearchTest.class.getDeclaredMethods());
 int counter = 0;
 for (Method method : methods) {
 String name = method.getName();
 assertTrue(name.contains(term1) || name.startsWith(term1));
 assertFalse(name.contains(term2) || name.startsWith(term2));
 counter++;
 }
 for (Method method : expectedMethods) {
 String name = method.getName();
 assertTrue(name.contains(term1) || name.startsWith(term1));
 assertFalse(name.contains(term2) || name.startsWith(term2));
 counter++;
 }
 assertEquals("Unexpected number of methods found", expectedMethods.size(), counter);
 }

 @Test
 public void testMethodsWithComplexSearchQuery() throws Exception {
 ReflectionSearch search = new ReflectionSearch(CLASS_TO_TEST);
 search.methodsThatHave("get").and().methodsThatDontHave("set");
 List<Method> methods = search.getMethods();
 List<Method> expectedMethods = new ArrayList<>();
 expectedMethods.add(ReflectionSearchTest.class.getDeclaredMethod("getTestAnnotation"));
 expectedMethods.add(ReflectionSearchTest.class.getDeclaredMethod("getTestAnnotation2"));
 expectedMethods.add(ReflectionSearchTest.class.getDeclaredMethod("getTestAnnotation3"));
 expectedMethods.add(ReflectionSearchTest.class.getDeclaredMethod("getTestAnnotation4"));
 expectedMethods.add(ReflectionSearchTest.class.getDeclaredMethod("getTestAnnotation5"));
 expectedMethods.add(ReflectionSearchTest.class.getDeclaredMethod("getTestAnnotation6"));
 expectedMethods.add(ReflectionSearchTest.class.getDeclaredMethod("getTestAnnotation7"));
 expectedMethods.add(ReflectionSearchTest.class.getDeclaredMethod("getTestAnnotation8"));
 expectedMethods.add(ReflectionSearchTest.class.getDeclaredMethod("getTestAnnotation9"));
 expectedMethods.add(ReflectionSearchTest.class.getDeclaredMethod("getTestAnnotation10"));
 for (Method method : methods) {
 String name = method.getName();
 assertTrue(name.startsWith("get") && !name.endsWith("Set"));
 }
 for (Method method : expectedMethods) {
 String name = method.getName();
 assertTrue(name.startsWith("get") && !name.endsWith("Set"));
 }
 assertEquals("Unexpected number of methods found", expectedMethods.size(), methods.size());
 }

}