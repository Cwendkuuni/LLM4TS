package net.sourceforge.beanbin.reflect.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.beanbin.query.Criteria;
import net.sourceforge.beanbin.query.Conditional;
import net.sourceforge.beanbin.query.ReflectionCriteria;
import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.reflect.ReflectionCriteria.SearchType;

import org.junit.Test;

public class ReflectionSearchTest {

 private static final String CLASS_NAME = "net.sourceforge.beanbin.reflect.ReflectionSearch";

 @Test
 public void testReflectionSearchInstance() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 assertNotNull(reflectionSearch);
 }

 @Test(expected = BeanBinException.class)
 public void testReflectionSearchInvalidClass() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(null);
 fail("ReflectionSearch constructor should throw BeanBinException for null class");
 }

 @Test
 public void testHasAnnotationSimpleTerm() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
 assertTrue(reflectionSearch.hasAnnotation("String"));
 }

 @Test
 public void testHasAnnotationAnnotationName() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Annotation.class);
 assertTrue(reflectionSearch.hasAnnotation("Annotation"));
 }

 @Test
 public void testHasAnnotationWildcardTerm() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 assertTrue(reflectionSearch.hasAnnotation("*Annotation"));
 }

 @Test
 public void testHasAnnotationNoMatch() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 assertFalse(reflectionSearch.hasAnnotation("Integer"));
 }

 @Test
 public void testMethodsThatHaveSimpleTerm() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("toString");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(1, methods.size());
 assertEquals("toString", methods.get(0).getName());
 }

 @Test
 public void testMethodsThatHaveWildcardTerm() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("get*");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(1, methods.size());
 assertTrue(methods.get(0).getName().startsWith("get"));
 }

 @Test
 public void testMethodsThatHaveNoMatch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("setBoolean");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testMethodsThatDontHaveSimpleTerm() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatDontHave("toString");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(1, methods.size());
 assertFalse(methods.get(0).getName().equals("toString"));
 }

 @Test
 public void testMethodsThatDontHaveWildcardTerm() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatDontHave("get*");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testMethodsThatDontHaveNoMatch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatDontHave("setBoolean");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(1, methods.size());
 assertFalse(methods.get(0).getName().equals("setBoolean"));
 }

 @Test
 public void testAndSearch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("toString");
 reflectionSearch.and();
 reflectionSearch.methodsThatDontHave("equals");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testOrSearch() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);reflectionSearch.methodsThatHave("toString");
 reflectionSearch.or();
 reflectionSearch.methodsThatDontHave("equals");
 reflectionSearch.or();
 reflectionSearch.methodsThatHave("hashCode");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(2, methods.size());
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatHaveNullTerm() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave(null);
 fail("ReflectionSearch.methodsThatHave should not accept null term");
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatDontHaveNullTerm() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatDontHave(null);
 fail("ReflectionSearch.methodsThatDontHave should not accept null term");
 }

 @Test(expected = BeanBinException.class)
 public void testAndWithNullCriteria() throws BeanBinException {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("toString");
 reflectionSearch.and();
 reflectionSearch.setQuery(null);
 List<Method> methods = reflectionSearch.getMethods();
 fail("ReflectionSearch should throw BeanBinException when query is null");
 }

 @Test
 public void testToString() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 String toString = reflectionSearch.toString();
 assertTrue(toString.contains("ReflectionSearch"));
 assertTrue(toString.contains("class=" + CLASS_NAME));
 }
}