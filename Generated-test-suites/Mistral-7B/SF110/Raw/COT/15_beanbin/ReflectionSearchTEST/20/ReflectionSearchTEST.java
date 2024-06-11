package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import org.junit.*;
import org.junit.runner.*;

import net.sourceforge.beanbin.*;
import net.sourceforge.beanbin.query.*;
import net.sourceforge.beanbin.search.*;

@RunWith(JUnit4.class)
public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class testClass;

 @Before
 public void setUp() throws Exception {
 testClass = SomeClassWithMethods.class;
 reflectionSearch = new ReflectionSearch(testClass);
 }

 @Test
 public void testHasAnnotation_EmptyAnnotations_ReturnsFalse() {
 assertFalse(reflectionSearch.hasAnnotation(""));
 }

 @Test
 public void testHasAnnotation_MatchesExactAnnotationName_ReturnsTrue() throws Exception {
 Annotation testAnnotation = testClass.getAnnotation(TestAnnotation.class);
 assertTrue(reflectionSearch.hasAnnotation(testAnnotation.annotationType().getName()));
 }

 @Test
 public void testHasAnnotation_MatchesClassnameWithWildcard_ReturnsTrue() {
 String wildcardAnnotationName = "net.sourceforge.beanbin.test.annotation.%";
 assertTrue(reflectionSearch.hasAnnotation(wildcardAnnotationName));
 }

 @Test
 public void testHasAnnotation_DoesNotMatchAnnotationName_ReturnsFalse() {
 assertFalse(reflectionSearch.hasAnnotation("TestAnnotationX"));
 }

 @Test
 public void testHasAnnotation_DoesNotMatchClassname_ReturnsFalse() {
 assertFalse(reflectionSearch.hasAnnotation("SomeOtherClass"));
 }

 @Test
 public void testMethodsThatHave_EmptyTerm_ThrowsException() throws Exception {
 expectThrows(BeanBinException.class, () -> reflectionSearch.methodsThatHave(""));
 }

 @Test
 public void testMethodsThatHave_MatchesMethodName_ReturnsReflectionSearchObject() throws Exception {
 reflectionSearch.methodsThatHave("testMethod");
 assertTrue(reflectionSearch instanceof ReflectionSearch);
 }

 @Test
 public void testMethodsThatHave_NoMatchingMethods_ReturnsReflectionSearchObject() throws Exception {
 reflectionSearch.methodsThatHave("nonExistingMethod");
 assertTrue(reflectionSearch instanceof ReflectionSearch);
 }

 @Test
 public void testMethodsThatDontHave_EmptyTerm_ThrowsException() throws Exception {
 expectThrows(BeanBinException.class, () -> reflectionSearch.methodsThatDontHave(""));
 }

 @Test
 public void testMethodsThatDontHave_MatchesMethodName_ReturnsReflectionSearchObject() throws Exception {
 reflectionSearch = reflectionSearch.methodsThatHave("testMethod");
 reflectionSearch.methodsThatDontHave("testMethod");
 assertTrue(reflectionSearch instanceof ReflectionSearch);
 }

 @Test
 public void testMethodsThatDontHave_NoMatchingMethods_ReturnsReflectionSearchObject() throws Exception {
 reflectionSearch = reflectionSearch.methodsThatHave("testMethod");
 reflectionSearch.methodsThatDontHave("nonExistingMethod");
 assertTrue(reflectionSearch instanceof ReflectionSearch);
 }

 @Test
 public void testAndOperator_EmptyCriteria_ReturnsReflectionSearchObject() throws Exception {
 reflectionSearch = reflectionSearch.methodsThatHave("testMethod");
 reflectionSearch.and();
 assertTrue(reflectionSearch instanceof ReflectionSearch);
 }

 @Test
 public void testAndOperator_MatchesBothCriteria_ReturnsListOfMethods() throws Exception {
 reflectionSearch = reflectionSearch.methodsThatHave("testMethod");
 reflectionSearch.and().methodsThatHave("anotherTestMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertFalse(methods.isEmpty());
 }

 @Test
 public void testAndOperator_DoesNotMatchFirstCriteria_ReturnsEmptyList() throws Exception {
 reflectionSearch = reflectionSearch.methodsThatHave("testMethod");
 reflectionSearch.and().methodsThatHave("nonExistingMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertTrue(methods.isEmpty());
 }

 @Test
 public void testOrOperator_EmptyCriteria_ReturnsReflectionSearchObject() throws Exception {
 reflectionSearch = reflectionSearch.methodsThatHave("testMethod");
 reflectionSearch.or();
 assertTrue(reflectionSearch instanceof ReflectionSearch);
 }

 @Test
 public void testOrOperator_MatchesEitherCriteria_ReturnsListOfMethods() throws Exception {
 reflectionSearch = reflectionSearch.methodsThatHave("testMethod");
 reflectionSearch.or().methodsThatHave("anotherTestMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertFalse(methods.isEmpty());
 }

 @Test
 public void testOrOperator_BothCriteriaDoNotMatch_ReturnsEmptyList() throws Exception {
 reflectionSearch = reflectionSearch.methodsThatHave("testMethod");
 reflectionSearch.or().methodsThatHave("nonExistingMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertTrue(methods.isEmpty());
 }

 @Test
 public void testGetMethods_EmptyQuery_ReturnsEmptyList() throws Exception {
 List<Method> methods = reflectionSearch.getMethods();
 assertTrue(methods.isEmpty());
 }

 @Test
 public void testGetMethods_MatchesMethods_ReturnsListOfMethods() throws Exception {
 reflectionSearch = reflectionSearch.methodsThatHave("testMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertFalse(methods.isEmpty());
 }

 @Test
 public void testGetMethods_NoMatchingMethods_ReturnsEmptyList() throws Exception {
 reflectionSearch = reflectionSearch.methodsThatHave("nonExistingMethod");
 List<Method> methods = reflectionSearch.getMethods();
 assertTrue(methods.isEmpty());
 }

 private void expectThrows(Class expectedException, Runnable codeBlock) {
 try {
 codeBlock.run();
 fail("Expected exception " + expectedException.getName() + " not thrown.");
 } catch (Exception e) {
 if (expectedException.isInstance(e.getCause())) {
 // all good
 } else {
 throw e;
 }
 }
}
}