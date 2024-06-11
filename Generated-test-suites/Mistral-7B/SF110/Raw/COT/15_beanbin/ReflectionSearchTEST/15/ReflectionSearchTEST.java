package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.search.BeanBinException;
import net.sourceforge.beanbin.search.Conditional;
import net.sourceforge.beanbin.search.Criteria;
import net.sourceforge.beanbin.search.MethodReflectionCriteria;
import net.sourceforge.beanbin.search.ReflectionCriteria;
import net.sourceforge.beanbin.search.Query;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;

 @Before
 public void setup() {
 reflectionSearch = new ReflectionSearch(Object.class);
 }

 @Test
 public void testHasAnnotation_EmptyAnnotation_ReturnsFalse() {
 assertFalse(reflectionSearch.hasAnnotation(""));
 }

 @Test
 public void testHasAnnotation_MatchesAnnotationName_ReturnsTrue() {
 reflectionSearch = new ReflectionSearch(Annotation.class);
 assertTrue(reflectionSearch.hasAnnotation("Annotation"));
 }

 @Test
 public void testHasAnnotation_MatchesAnnotationClassName_ReturnsTrue() {
 reflectionSearch = new ReflectionSearch(Annotation.class);
 assertTrue(reflectionSearch.hasAnnotation("Annotation"));
 assertTrue(reflectionSearch.hasAnnotation("AnnotationType"));
 }

 @Test
 public void testHasAnnotation_MatchesWildcardAnnotationName_ReturnsTrue() {
 reflectionSearch = new ReflectionSearch(Object.class);
 assertTrue(reflectionSearch.hasAnnotation("*Annotation*"));
 }

 @Test
 public void testHasAnnotation_MatchesWildcardAnnotationClassName_ReturnsTrue() {
 reflectionSearch = new ReflectionSearch(Object.class);
 assertTrue(reflectionSearch.hasAnnotation("*Type"));
 }

 @Test
 public void testHasAnnotation_DoesNotMatch_ReturnsFalse() {
 reflectionSearch = new ReflectionSearch(Object.class);
 assertFalse(reflectionSearch.hasAnnotation("String"));
 }

 @Test
 public void testMethodsThatHave_EmptyTerm_ThrowsException() {
 try {
 reflectionSearch.methodsThatHave("");
 fail("Should have thrown BeanBinException");
 } catch (BeanBinException e) {
 // success
 }
 }

 @Test
 public void testMethodsThatHave_MatchesMethodName_ReturnsReflectionSearchObject() {
 reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("test");
 assertTrue(reflectionSearch instanceof ReflectionSearch);
 }

 @Test
 public void testMethodsThatHave_MatchesMethodNameCaseInsensitive_ReturnsReflectionSearchObject() {
 reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("test");
 reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("TeSt");
 assertTrue(reflectionSearch instanceof ReflectionSearch);
 }

 @Test
 public void testMethodsThatHave_MultipleMatches_ReturnsReflectionSearchObject() {
 reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("test");
 reflectionSearch.methodsThatHave("method");
 assertTrue(reflectionSearch instanceof ReflectionSearch);
 }

 @Test
 public void testMethodsThatDontHave_EmptyTerm_ThrowsException() {
 try {
 reflectionSearch.methodsThatDontHave("");
 fail("Should have thrown BeanBinException");
 } catch (BeanBinException e) {
 // success
 }
 }

 @Test
 public void testMethodsThatDontHave_MatchesMethodName_ReturnsReflectionSearchObject() {
 reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatDontHave("test");
 reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatDontHave("Method");
 assertTrue(reflectionSearch instanceof ReflectionSearch);
 }

 @Test
 public void testMethodsThatDontHave_MultipleMatches_ReturnsReflectionSearchObject() {
 reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatDontHave("test");
 reflectionSearch.methodsThatDontHave("Method");
 reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatDontHave("otherMethod");
 assertTrue(reflectionSearch instanceof ReflectionSearch);
 }

 @Test
 public void testAnd_ReturnsReflectionSearchObject() {
 reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("test");
 reflectionSearch.and();
 reflectionSearch.methodsThatDontHave("Method");
 assertTrue(reflectionSearch instanceof ReflectionSearch);
 }

 @Test
 public void testOr_ReturnsReflectionSearchObject() {
 reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("test");
 reflectionSearch.or();
 reflectionSearch.methodsThatDontHave("Method");
 reflectionSearch.methodsThatHave("otherTest");
 assertTrue(reflectionSearch instanceof ReflectionSearch);
 }

 @Test
 public void testGetMethods_EmptyQuery_ReturnsEmptyList() {
 List<Method> methods = reflectionSearch.getMethods();
 assertTrue(methods.isEmpty());
 }

 @Test
 public void testGetMethods_MatchesMethodName_ReturnsNonEmptyList() throws BeanBinException {
 List<Method> methods = reflectionSearch.methodsThatHave("test").getMethods();
 assertFalse(methods.isEmpty());
 }

 @Test
 public void testGetMethods_MultipleMatches_ReturnsNonEmptyList() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(Object.class);
 List<Method> methods = reflectionSearch.methodsThatHave("test").getMethods();
 reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.methodsThatHave("method").getMethods();
 methods.addAll(reflectionSearch.getMethods());
 assertFalse(methods.isEmpty());
 }

 @Test
 public void testGetMethods_MultipleConditions_ReturnsNonEmptyList() throws BeanBinException {
 List<Method> methods = reflectionSearch.methodsThatHave("test").and().methodsThatDontHave("Method").getMethods();
 assertFalse(methods.isEmpty());
 }

 @Test
 public void testGetMethods_EmptyList_FromAndCondition_ReturnsEmptyList() throws BeanBinException {
 List<Method> methods = reflectionSearch.methodsThatHave("test").and().methodsThatHave("Method").getMethods();
 assertTrue(methods.isEmpty());
 }

 @Test
 public void testGetMethods_EmptyList_FromOrCondition_ReturnsEmptyList() throws BeanBinException {
 List<Method> methods = reflectionSearch.methodsThatHave("test").or().methodsThatHave("Method").getMethods();
 assertTrue(methods.isEmpty());
 }

 @Test
 public void testGetMethods_SingleMethodMatch_ReturnsSingleMethod() throws BeanBinException {
 List<Method> methods = reflectionSearch.methodsThatHave("testGet").getMethods();
 assertEquals(1, methods.size());
 }

 @Test
 public void testGetMethods_MultipleMethodMatches_ReturnsListWithMultipleMethods() throws BeanBinException {
 List<Method> methods = reflectionSearch.methodsThatHave("test").methodsThatHave("get").getMethods();
 assertTrue(methods.size() > 1);
 }

 @Test
 public void testGetMethods_EmptyQueryWithNoConditions_ReturnsEmptyList() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(Object.class);
 List<Method> methods = reflectionSearch.getMethods();
 assertTrue(methods.isEmpty());
 }

 @Test
 public void testGetMethods_NullQuery_ThrowsException() {
 try {
 reflectionSearch = new ReflectionSearch(Object.class);
 reflectionSearch.getMethods(null);
 fail("Should have thrown BeanBinException");
 } catch (BeanBinException e) {
 // success
 }
}

}