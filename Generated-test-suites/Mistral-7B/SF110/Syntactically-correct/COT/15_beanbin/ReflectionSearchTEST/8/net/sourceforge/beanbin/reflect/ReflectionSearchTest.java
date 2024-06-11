package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import net.sourceforge.beanbin.query.Criteria;
import net.sourceforge.beanbin.query.Conditional;
import net.sourceforge.beanbin.query.ReflectionCriteria;
import net.sourceforge.beanbin.query.ReflectionShelf;
import net.sourceforge.beanbin.reflect.ReflectionSearch;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;

 @Test
 public void testReflectionSearchConstructor() {
 reflectionSearch = new ReflectionSearch(Object.class);
 assertNotNull(reflectionSearch);
 }

 @Test(expected = NullPointerException.class)
 public void testReflectionSearchWithNullClass() {
 reflectionSearch = new ReflectionSearch(null);
 }

 @Test
 public void testHasAnnotation_EmptyAnnotation_False() {
 reflectionSearch = new ReflectionSearch(Object.class);
 assertFalse(reflectionSearch.hasAnnotation(""));
 }

 @Test
 public void testHasAnnotation_ExactMatch_True() {
 reflectionSearch = new ReflectionSearch(String[].class);
 assertTrue(reflectionSearch.hasAnnotation("String"));
 }

 @Test
 public void testHasAnnotation_ExactMatchClass_True() {
 reflectionSearch = new ReflectionSearch(String[].class);
 assertTrue(reflectionSearch.hasAnnotation("Array"));
 }

 @Test
 public void testHasAnnotation_WildcardMatch_True() {
 reflectionSearch = new ReflectionSearch(String[].class);
 assertTrue(reflectionSearch.hasAnnotation("Array*"));
 assertTrue(reflectionSearch.hasAnnotation("*Array"));
 }

 @Test
 public void testHasAnnotation_NoMatch_False() {
 reflectionSearch = new ReflectionSearch(String[].class);
 assertFalse(reflectionSearch.hasAnnotation("StringBin"));
 }

 @Test
 public void testMethodsThatHave_EmptyQuery_EmptyList() {
 reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatHave("");
 List<Method> methods = reflectionSearch.getMethods();
 assertTrue(methods.isEmpty());
 }

 @Test
 public void testMethodsThatHave_SingleMatch_NonEmptyList() {
 reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatHave("test");
 methods = reflectionSearch.getMethods();
 assertEquals(1, methods.size());
 assertNotNull(methods.get(0));
 }

 @Test
 public void testMethodsThatDontHave_SingleMatch_EmptyList() {
 reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatDontHave("test");
 methods = reflectionSearch.getMethods();
 assertTrue(methods.isEmpty());
 }

 @Test
 public void testMethodsThatDontHave_MultipleMatches_EmptyList() {
 reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatHave("test").methodsThatDontHave("test");
 methods = reflectionSearch.getMethods();
 assertTrue(methods.isEmpty());
 }

 @Test
 public void testMethodsThatHaveAndMethodsThatDontHave_SingleMatches_EmptyList() {
 reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatHave("test").methodsThatDontHave("test");
 methods = reflectionSearch.getMethods();
 assertTrue(methods.isEmpty());
 }

 @Test
 public void testMethodsThatHaveAndMethodsThatDontHave_EmptyQueries_EmptyList() {
 reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.methodsThatHave("").methodsThatDontHave("");
 methods = reflectionSearch.getMethods();
 assertTrue(methods.isEmpty());
 }

 @Test
 public void testMethodsThatHaveAndMethodsThatDontHave_Intersect_EmptyList() {
 reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 List<Method> methods1 = Arrays.asList(ReflectionSearch.class.getMethods());
 reflectionSearch.methodsThatHave("test");
 reflectionSearch.methodsThatDontHave("test");
 methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testMethodsThatHaveAndMethodsThatDontHave_Union_NonEmptyList() {
 reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 List<Method> methods1 = Arrays.asList(ReflectionSearch.class.getMethods());
 reflectionSearch.methodsThatHave("*");
 methods = reflectionSearch.getMethods();
 assertEquals(methods1.size(), methods.size());
 for (Method method : methods1) {
 assertTrue(methods.contains(method));
 }
 }

 @Test
 public void testMethodsThatHaveAndMethodsThatDontHave_WithExceptions() {
 reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 try {
 reflectionSearch.methodsThatHave("test").methodsThatDontHave("test");
 fail("expected BeanBinException");
 } catch (BeanBinException e) {
 // expected
 }
 }

 @Test
 public void testMethodsThatHaveAndMethodsThatDontHave_WithNullClass() {
 reflectionSearch = new ReflectionSearch(null);
 try {
 reflectionSearch.methodsThatHave("test");
 reflectionSearch.methodsThatDontHave("test");
 fail("expected BeanBinException");
 } catch (BeanBinException e) {
 // expected
 }
 }

 @Test
 public void testIntersect() {
 List<Method> master = new ArrayList<>();
 List<Method> toadd = new ArrayList<>();
 toadd.add(ReflectionSearch.class.getMethod("intersect", List.class, List.class));
 intersect(master, toadd);
 assertEquals(0, master.size());
 }

 @Test
 public void testUnion() {
 List<Method> master = new ArrayList<>();
 List<Method> toadd = new ArrayList<>();
 toadd.add(ReflectionSearch.class.getMethod("union", List.class, List.class));
 union(master, toadd);
 assertEquals(1, master.size());
 master.add(ReflectionSearch.class.getMethod("intersect", List.class, List.class));
 assertEquals(2, master.size());
 }

 private void intersect(List<Method> master, List<Method> toadd) {
 reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 master = reflectionSearch.getMethods();
 toadd = Arrays.asList(ReflectionSearch.class.getMethods());
 reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.query.addAll(reflectionSearch.query.getCriterias());
 reflectionSearch.query.removeIf(c -> c instanceof ReflectionCriteria);
 ReflectionCriteria crit = new ReflectionCriteria(ReflectionSearch.class, "intersect", SearchType.EQUALS, null);
 reflectionSearch.query.add(crit);
 List<Method> results = Arrays.asList(ReflectionSearch.class.getMethod("getResults", List.class));
 reflectionSearch.query.setNextConditional(Conditional.AND);
 reflectionSearch.intersect(master, results);
 }

 private void union(List<Method> master, List<Method> toadd) {
 reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 master = reflectionSearch.getMethods();
 toadd = Arrays.asList(ReflectionSearch.class.getMethods());
 reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
 reflectionSearch.query.addAll(reflectionSearch.query.getCriterias());
 reflectionSearch.query.removeIf(c -> c instanceof ReflectionCriteria);
 ReflectionCriteria crit = new ReflectionCriteria(ReflectionSearch.class, "union", SearchType.EQUALS, null);
 reflectionSearch.query.add(crit);
 List<Method> results = Arrays.asList(ReflectionSearch.class.getMethod("getResults", List.class));
 reflectionSearch.query.setNextConditional(Conditional.OR);
 reflectionSearch.union(master, results);
 }

}