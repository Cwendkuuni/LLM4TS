package net.sourceforge.beanbin.reflect;

import org.junit.Assert;
import org.junit.Test;
import net.sourceforge.beanbin.query.*;
import java.lang.reflect.*;
import java.util.*;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private List<Method> methods;

 @Test
 public void testHasAnnotation() throws Exception {
  reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);
  Assert.assertTrue(reflectionSearch.hasAnnotation("javax.annotation.PostConstruct"));
  Assert.assertFalse(reflectionSearch.hasAnnotation("RandomString"));
  reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
  Assert.assertTrue(reflectionSearch.hasAnnotation("ReflectionSearch"));
  Assert.assertFalse(reflectionSearch.hasAnnotation("RandomString"));
  reflectionSearch = new ReflectionSearch(Object.class);
  Assert.assertFalse(reflectionSearch.hasAnnotation("AnyAnnotation"));
 }

 @Test
 public void testHasWildcard() {
  Assert.assertTrue(reflectionSearch.hasWildcard("*Annotation"));
  Assert.assertFalse(reflectionSearch.hasWildcard("AnyClass"));
 }

 @Test
 public void testMethodsThatHave() throws BeanBinException {
  reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);
  reflectionSearch.methodsThatHave("init");
  methods = reflectionSearch.getMethods();
  Assert.assertEquals(2, methods.size());
  Assert.assertEquals("init", methods.get(0).getName());
  Assert.assertEquals("init", methods.get(1).getName());
 }

 @Test
 public void testMethodsThatDontHave() throws BeanBinException {
  reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);
  reflectionSearch.methodsThatDontHave("get");
  methods = reflectionSearch.getMethods();
  Assert.assertEquals(1, methods.size());
  Assert.assertEquals("setName", methods.get(0).getName());
 }

 @Test
 public void testAnd() throws BeanBinException {
  reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);
  reflectionSearch.methodsThatHave("init");
  reflectionSearch.methodsThatHave("setValue");
  methods = reflectionSearch.getMethods();
  Assert.assertEquals(1, methods.size());
  Assert.assertEquals("initValue", methods.get(0).getName());
 }

 @Test
 public void testOr() throws BeanBinException {
  reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);
  reflectionSearch.methodsThatHave("init");
  reflectionSearch.methodsThatDontHave("setValue");
  methods = reflectionSearch.getMethods();
  Assert.assertEquals(1, methods.size());
  Assert.assertEquals("finalize", methods.get(0).getName());
 }

 @Test(expected = BeanBinException.class)
 public void testException() throws BeanBinException {
  reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);
  reflectionSearch.methodsThatHave("nonExistentMethod");
 }

 @Test
 public void testEmptyQuery() throws BeanBinException {
  reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);
  methods = reflectionSearch.getMethods();
  Assert.assertEquals(0, methods.size());
 }
}

class AnnotationTestClass {
 @PostConstruct
 public void init() {
 }

 public void initValue() {
 }

 public void setName(String name) {
 }

 public String getName() {
 return null;
 }

 public void finalize() {
 }
}