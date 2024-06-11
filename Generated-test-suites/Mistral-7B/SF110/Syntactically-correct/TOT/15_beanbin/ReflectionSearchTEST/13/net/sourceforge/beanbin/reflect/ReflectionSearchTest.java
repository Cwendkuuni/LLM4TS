package net.sourceforge.beanbin.reflect;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.search.Criteria;
import net.sourceforge.beanbin.search.ReflectionCriteria;
import net.sourceforge.beanbin.search.SearchType;
import net.sourceforge.beanbin.query.Conditional;
import net.sourceforge.beanbin.query.Query;
import java.lang.reflect.Method;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class testClass;

 @Before
 public void setup() {
 testClass = TestClassWithAnnotations.class;
 reflectionSearch = new ReflectionSearch(testClass);
 }

 @Test
 public void testHasAnnotation() {
 // Test case for hasAnnotation method with exact match
 Assert.assertTrue(reflectionSearch.hasAnnotation("Annotation1"));

 // Test case for hasAnnotation method with class name match
 Assert.assertTrue(reflectionSearch.hasAnnotation("Annotation1Annotation"));

 // Test case for hasAnnotation method with wildcard match
 Assert.assertTrue(reflectionSearch.hasAnnotation("Annotation*"));

 // Test case for hasAnnotation method with no match
 Assert.assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
 }

 @Test
 public void testMethodsThatHave() throws BeanBinException {
 // Test case for methodsThatHave method with exact match
 reflectionSearch.methodsThatHave("testMethod");
 List<Method> methods = reflectionSearch.getMethods();
 Assert.assertEquals(1, methods.size());
 Assert.assertEquals("testMethod", methods.get(0).getName());

 // Test case for methodsThatHave method with wildcard match
 reflectionSearch.methodsThatHave("test*Method");
 methods = reflectionSearch.getMethods();
 Assert.assertEquals(2, methods.size());

 // Test case for methodsThatHave method with no match
 reflectionSearch.methodsThatHave("nonExistentMethod");
 methods = reflectionSearch.getMethods();
 Assert.assertEquals(0, methods.size());
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatDontHaveThrowsException() throws BeanBinException {
 // Test case for methodsThatDontHave method with no such method
 reflectionSearch.methodsThatDontHave("nonExistentMethod");
 }

 @Test
 public void testMethodsThatDontHave() throws BeanBinException {
 // Test case for methodsThatDontHave method with exact match
 reflectionSearch.methodsThatDontHave("nonTestMethod");
 List<Method> methods = reflectionSearch.getMethods();
 Assert.assertEquals(0, methods.size());

 // Test case for methodsThatDontHave method with wildcard match
 reflectionSearch.methodsThatDontHave("non*Method");
 methods = reflectionSearch.getMethods();
 Assert.assertEquals(1, methods.size());
 }

 @Test
 public void testAndCondition() throws BeanBinException {
 // Test case for and condition with exact match
 reflectionSearch.methodsThatHave("testMethod");
 reflectionSearch.methodsThatDontHave("getMethod");
 methods = reflectionSearch.getMethods();
 Assert.assertEquals(0, methods.size());

 // Test case for and condition with wildcard match
 reflectionSearch.methodsThatHave("test*Method");
 reflectionSearch.methodsThatDontHave("get*Method");
 methods = reflectionSearch.getMethods();
 Assert.assertEquals(1, methods.size());
 }

 @Test
 public void testOrCondition() throws BeanBinException {
 // Test case for or condition with exact match
 reflectionSearch.methodsThatHave("testMethod");
 reflectionSearch.methodsThatHave("getMethod");
 methods = reflectionSearch.getMethods();
 Assert.assertEquals(2, methods.size());

 // Test case for or condition with wildcard match
 reflectionSearch.methodsThatHave("test*Method");
 reflectionSearch.methodsThatHave("get*Method");
 methods = reflectionSearch.getMethods();
 Assert.assertEquals(3, methods.size());
 }
}

class TestClassWithAnnotations {
 @Annotation1
 public void testMethod() {
 }

 @Annotation2
 public void getMethod() {
 }
}