package net.sourceforge.beanbin.reflect.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.reflect.annotation.AnnotationTest;
import net.sourceforge.beanbin.reflect.query.BeanBinException;
import net.sourceforge.beanbin.reflect.query.Criteria;
import net.sourceforge.beanbin.reflect.query.ReflectionCriteria;
import net.sourceforge.beanbin.reflect.query.ReflectionShelf;
import net.sourceforge.beanbin.reflect.query.SearchType;

public class ReflectionSearchTest {

 private static final String ANNOTATION_CLASS = "net.sourceforge.beanbin.reflect.annotation.TestAnnotation";
 private static final String ANNOTATION_TERM = "TestAnnotation";
 private static final String INVALID_TERM = "InvalidTerm";
 private static final String INVALID_CLASS = "net.sourceforge.beanbin.reflect.test.InvalidClass";
 private static final String INVALID_ANNOTATION_TERM = "InvalidAnnotationTerm";
 private static final String EMPTY_TERM = "";
 private ReflectionSearch reflectionSearch;
 private List<Method> methods;

 @BeforeClass
 public static void setupClass() {
 ReflectionShelf.getInstance().clear();
 }

 @Before
 public void setup() throws BeanBinException {
 Class<?> clazz = Class.forName(ReflectionSearch.class.getPackage().getName() + ".ReflectionSearch");
 reflectionSearch = new ReflectionSearch(clazz);
 methods = new ArrayList<Method>();
 }

 @Test
 public void testConstructor() {
 reflectionSearch = new ReflectionSearch(AnnotationTest.class);
 assertNotNull(reflectionSearch);
 }

 @Test
 public void testHasAnnotation_EmptyString() {
 assertFalse(reflectionSearch.hasAnnotation(EMPTY_TERM));
 }

 @Test
 public void testHasAnnotation_ValidAnnotationName() throws Exception {
 Class<?> clazz = Class.forName(AnnotationTest.class.getPackage().getName() + ".AnnotationTest");
 reflectionSearch = new ReflectionSearch(clazz);
 assertTrue(reflectionSearch.hasAnnotation(ANNOTATION_TERM));
 }

 @Test
 public void testHasAnnotation_ValidClassName() throws Exception {
 Class<?> clazz = Class.forName(AnnotationTest.class.getPackage().getName() + ".AnnotationTest");
 reflectionSearch = new ReflectionSearch(clazz);
 assertTrue(reflectionSearch.hasAnnotation(ANNOTATION_TERM.substring(1)));
 }

 @Test
 public void testHasAnnotation_InvalidTerm() {
 assertFalse(reflectionSearch.hasAnnotation(INVALID_TERM));
 }

 @Test(expected = BeanBinException.class)
 public void testHasAnnotation_InvalidClass() {
 reflectionSearch = new ReflectionSearch(Class.forName(INVALID_CLASS));
 fail("Should have thrown BeanBinException");
 }

 @Test
 public void testHasAnnotation_WildcardAnnotationName() {
 reflectionSearch = new ReflectionSearch(AnnotationTest.class);
 assertTrue(reflectionSearch.hasAnnotation("*Annotation"));
 }

 @Test
 public void testHasAnnotation_WildcardClassName() {
 reflectionSearch = new ReflectionSearch(AnnotationTest.class);
 assertTrue(reflectionSearch.hasAnnotation("*Test"));
 }

 @Test
 public void testMethodsThatHave_ValidTerm() throws BeanBinException {
 reflectionSearch.methodsThatHave(ANNOTATION_TERM);
 List<Method> result = reflectionSearch.getMethods();
 assertEquals(2, result.size());
 for (Method method : result) {
 assertEquals("testAnnotation", method.getName());
 }
 }

 @Test
 public void testMethodsThatHave_EmptyTerm() throws BeanBinException {
 reflectionSearch.methodsThatHave(EMPTY_TERM);
 assertEquals(0, methods.size());
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatHave_InvalidTerm() throws BeanBinException {
 reflectionSearch.methodsThatHave(INVALID_TERM);
 fail("Should have thrown BeanBinException");
 }

 @Test
 public void testMethodsThatDontHave_ValidTerm() throws BeanBinException {
 reflectionSearch.methodsThatDontHave(ANNOTATION_TERM);
 assertEquals(1, methods.size());
 }

 @Test(expected = BeanBinException.class)
 public void testMethodsThatDontHave_InvalidTerm() throws BeanBinException {
 reflectionSearch.methodsThatDontHave(INVALID_TERM);
 fail("Should have thrown BeanBinException");
 }

 @Test
 public void testAnd() throws BeanBinException {
 reflectionSearch.methodsThatHave("get").and();
 reflectionSearch.methodsThatHave("Test");
 List<Method> result = reflectionSearch.getMethods();
 assertEquals(1, result.size());
 assertEquals("getTest", result.get(0).getName());
 }

 @Test(expected = BeanBinException.class)
 public void testAnd_InvalidCriterion() throws BeanBinException {
 reflectionSearch.methodsThatHave("get").and();
 reflectionSearch.methodsThatHave("InvalidCriterion");
 fail("Should have thrown BeanBinException");
 }

 @Test
 public void testOr() throws BeanBinException {
 reflectionSearch.methodsThatHave("get").or();
 reflectionSearch.methodsThatHave("set");
 List<Method> result = reflectionSearch.getMethods();
 assertEquals(2, result.size());
 }

 @Test(expected = BeanBinException.class)
 public void testOr_InvalidCriterion() throws BeanBinException {
 reflectionSearch.methodsThatHave("get").or();
 reflectionSearch.methodsThatHave("InvalidCriterion");
 fail("Should have thrown BeanBinException");
 }

 @After
 public void teardown() {
 methods.clear();
 reflectionSearch = null;
 }

}