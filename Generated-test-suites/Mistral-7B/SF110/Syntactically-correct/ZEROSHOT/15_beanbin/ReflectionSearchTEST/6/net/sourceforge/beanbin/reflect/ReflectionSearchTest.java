package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import org.junit.Test;

import net.sourceforge.beanbin.query.*;
import net.sourceforge.beanbin.search.*;

public class ReflectionSearchTest {

 @Test
 public void testHasAnnotation() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);

 // Test exact match
 assertTrue(reflectionSearch.hasAnnotation("ToString"));

 // Test wildcard match
 assertTrue(reflectionSearch.hasAnnotation("Comparator*"));

 // Test no match
 assertFalse(reflectionSearch.hasAnnotation("Random"));
 }

 @Test
 public void testMethodsThatHave() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
 reflectionSearch.methodsThatHave("equals");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(1, methods.size());
 Method equalsMethod = methods.get(0);
 assertEquals("equals", equalsMethod.getName());
 }

 @Test
 public void testMethodsThatDontHave() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
 reflectionSearch.methodsThatDontHave("equals");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testAnd() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
 reflectionSearch.methodsThatHave("equals");
 reflectionSearch.and();
 reflectionSearch.methodsThatHave("hashCode");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(1, methods.size());
 Method equalsMethod = methods.get(0);
 assertEquals("equals", equalsMethod.getName());
 Method hashCodeMethod = methods.get(1);
 assertEquals("hashCode", hashCodeMethod.getName());
 }

 @Test
 public void testOr() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
 reflectionSearch.methodsThatHave("equals");
 reflectionSearch.or();
 reflectionSearch.methodsThatHave("hashCode");
 reflectionSearch.methodsThatHave("toString");
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(3, methods.size());
 }
}