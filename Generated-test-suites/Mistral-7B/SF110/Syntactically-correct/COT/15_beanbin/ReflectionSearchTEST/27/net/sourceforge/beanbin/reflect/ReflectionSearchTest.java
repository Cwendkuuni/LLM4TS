package net.sourceforge.beanbin.reflect.test;

import org.junit.Assert;
import org.junit.Test;
import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.reflect.Query;
import net.sourceforge.beanbin.search.Criteria;
import net.sourceforge.beanbin.search.ReflectionCriteria;
import net.sourceforge.beanbin.search.Conditional;
import net.sourceforge.beanbin.query.MethodReflectionCriteria;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionSearchTest {

 private static final String ANNOTATION_TEST = "MyAnnotation";
 private static final String ANNOTATION_TEST_PN = "MyAnnotation";
 private static final String ANNOTATION_TEST_CS = "MyAnnotationClass";
 private static final String ANNOTATION_TEST_WILDCARD = "*Annotation";
 private static final String METHOD_TEST = "testMethod";
 private static final String METHOD_TEST_ALIAS = "myMethod";
 private static final String METHOD_TEST_WILDCARD = "test*";

 @Test
 public void testConstructor() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 }

 @Test
 public void testHasAnnotation_NoAnnotation() {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 Assert.assertFalse(reflectionSearch.hasAnnotation(ANNOTATION_TEST));
 }

 @Test
 public void testHasAnnotation_AnnotationPresent() throws Exception {
 Class clazz = MyAnnotation.class;
 ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
 Assert.assertTrue(reflectionSearch.hasAnnotation(ANNOTATION_TEST_PN));
 Assert.assertTrue(reflectionSearch.hasAnnotation(ANNOTATION_TEST_CS));
 }

 @Test
 public void testHasAnnotation_WildcardAnnotation() throws Exception {
 Class clazz = MyAnnotation.class;
 ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
 Assert.assertTrue(reflectionSearch.hasAnnotation(ANNOTATION_TEST_WILDCARD));
 }

 @Test
 public void testMethodsThatHave_NoMatchingMethods() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave(METHOD_TEST).getMethods();
 }

 @Test
 public void testMethodsThatHave_SingleMatchingMethod() throws Exception {
 Class clazz = MyClassWithMethod.class;
 ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatHave(METHOD_TEST).getMethods();
 }

 @Test
 public void testMethodsThatHave_MultipleMatchingMethods() throws Exception {
 Class clazz = MyClassWithMultipleMethods.class;
 ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
 List<Method> methods = reflectionSearch.methodsThatHave(METHOD_TEST).getMethods();
 Assert.assertEquals(2, methods.size());
 }

 @Test
 public void testMethodsThatHave_Alias() throws Exception {
 Class clazz = MyClassWithAliasMethod.class;
 ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatHave(METHOD_TEST_ALIAS).getMethods();
 }

 @Test
 public void testMethodsThatHave_Wildcard() throws Exception {
 Class clazz = MyClassWithMethods.class;
 ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
 List<Method> methods = reflectionSearch.methodsThatHave(METHOD_TEST_WILDCARD).getMethods();
 Assert.assertEquals(2, methods.size());
 }

 @Test
 public void testMethodsThatDontHave_NoMatchingMethods() throws Exception {
 Class clazz = MyClassWithMethod.class;
 ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
 reflectionSearch.methodsThatDontHave(METHOD_TEST).getMethods();
 Assert.assertEquals(0, reflectionSearch.getMethods().size());
 }

 @Test
 public void testMethodsThatDontHave_SingleMatchingMethod() throws Exception {
 Class clazz = MyClassWithMethod.class;
 ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
 List<Method> methods = reflectionSearch.methodsThatDontHave(METHOD_TEST).getMethods();
 Assert.assertEquals(0, methods.size());
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatHave_NullTerm() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave(null);
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatHave_NullQuery() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.query = null;
 reflectionSearch.methodsThatHave(METHOD_TEST).getMethods();
 }

 @Test(expected = BeanBinException.class)
 public void testGetMethods_NullQuery() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.query = new Query();
 reflectionSearch.getMethods();
 }

 @Test(expected = BeanBinException.class)
 public void testGetMethods_EmptyQuery() throws Exception {
 ReflectionSearch reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.query.setCriterias(new ArrayList<Criteria>());
 reflectionSearch.getMethods();
 }

 @Test
 public void testIntersect() throws Exception {
 List<Method> master = Arrays.asList(new Method[] { new Method() {}, new Method() {} });
 List<Method> toadd = Arrays.asList(new Method[] { new Method() {} });
 ReflectionSearch reflectionSearch = new ReflectionSearch();
 reflectionSearch.intersect(master, toadd);
 Assert.assertEquals(0, master.size());
 }

 @Test
 public void testUnion() throws Exception {
 List<Method> master = Arrays.asList(new Method[] { new Method() {}});
 List<Method> toadd = Arrays.asList(new Method[] { new Method() {}, new Method() {} });
 ReflectionSearch reflectionSearch = new ReflectionSearch();
 reflectionSearch.union(master, toadd);
 Assert.assertEquals(2, master.size());
 }
}