package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import org.junit.*;

import net.sourceforge.beanbin.query.*;
import static net.sourceforge.beanbin.reflect.ReflectionSearch.*;

public class ReflectionSearchTest {

 @BeforeClass
 public static void init() {
 ReflectionShelf.getInstance().clear();
 }

 @Test
 public void testHasAnnotation() throws Exception {
 Class clazz = Class.forName("java.lang.String");
 ReflectionSearch search = new ReflectionSearch(clazz);

 // Test exact match
 assertTrue(search.hasAnnotation("StringUtil"));

 // Test class name match
 assertTrue(search.hasAnnotation("java.lang.StringUtils"));

 // Test wildcard match
 assertTrue(search.hasAnnotation("*Utils"));

 // Test negative case
 assertFalse(search.hasAnnotation("MyAnnotation"));
 }

 @Test
 public void testHasWildcard() {
 assertTrue(hasWildcard("MyAnnotation.*"));
 }

 @Test
 public void testMethodsThatHave() throws Exception {
 Class clazz = Class.forName("java.util.ArrayList");
 ReflectionSearch search = new ReflectionSearch(clazz);

 List<Method> methods = search.methodsThatHave("add").getMethods();
 assertEquals(1, methods.size());
 assertEquals("add", methods.get(0).getName());
 }

 @Test
 public void testMethodsThatDontHave() throws Exception {
 Class clazz = Class.forName("java.util.ArrayList");
 ReflectionSearch search = new ReflectionSearch(clazz);

 List<Method> methods = search.methodsThatDontHave("add").getMethods();
 assertTrue(methods.isEmpty());
 }

 @Test
 public void testAnd() throws Exception {
 Class clazz = Class.forName("java.util.ArrayList");
 ReflectionSearch search = new ReflectionSearch(clazz);

 search.methodsThatHave("add");
 search.and();
 search.methodsThatDontHave("remove");

 List<Method> methods = search.getMethods();
 assertTrue(methods.isEmpty());
 }

 @Test
 public void testOr() throws Exception {
 Class clazz = Class.forName("java.util.ArrayList");
 ReflectionSearch search = new ReflectionSearch(clazz);

 search.methodsThatHave("add");
 search.or();
 search.methodsThatDontHave("remove");
 search.methodsThatHave("clear");

 List<Method> methods = search.getMethods();
 assertEquals(1, methods.size());
 assertFalse(methods.get(0).getName().equals("remove"));
 assertFalse(methods.get(0).getName().equals("clear"));
 // It's hard to predict the exact name of the method that matches,
 // but it should not be "remove" or "clear"
 }
}