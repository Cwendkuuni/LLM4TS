package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import net.sourceforge.beanbin.*;
import net.sourceforge.beanbin.query.*;
import org.junit.Test;

public class ReflectionSearchTest {

 private Class testClass;
 private ReflectionSearch reflectionSearch;
 private Query query;

 @Before
 public void setUp() {
 testClass = mock(Class.class);
 query = mock(Query.class);
 reflectionSearch = new ReflectionSearch(testClass);
 }

 @Test
 public void testHasAnnotation_StringTerm_ReturnsTrueWhenAnnotationNameMatchesTerm() {
 final Annotation testAnnotation = mock(Annotation.class);
 when(testClass.getAnnotations()).thenReturn(new Annotation[]{testAnnotation});
 when(testAnnotation.annotationType().getName()).thenReturn("net.sourceforge.beanbin.MyAnnotation");
 reflectionSearch.hasAnnotation("MyAnnotation");
 assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));
 }

 @Test
 public void testHasAnnotation_StringTerm_ReturnsTrueWhenAnnotationClassnameMatchesTerm() {
 final Annotation testAnnotation = mock(Annotation.class);
 when(testClass.getAnnotations()).thenReturn(new Annotation[]{testAnnotation});
 when(testAnnotation.annotationType().getName()).thenReturn("net.sourceforge.beanbin.MyAnnotation");
 reflectionSearch.hasAnnotation("Annotation");
 assertFalse(reflectionSearch.hasAnnotation("Annotation"));
 reflectionSearch.hasAnnotation("MyAnnotation");
 assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));
 }

 @Test
 public void testHasAnnotation_StringTerm_ReturnsFalseWhenNoMatchingAnnotation() {
 reflectionSearch.hasAnnotation("NonExistentAnnotation");
 assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
 }

 @Test
 public void testHasWildcard_StringTerm_ReturnsTrueWhenTermContainsWildcardCharacter() {
 assertTrue(reflectionSearch.hasWildcard("MyAnnotation*"));
 assertTrue(reflectionSearch.hasWildcard("Annotation*"));
 }

 @Test
 public void testHasWildcard_StringTerm_ReturnsFalseWhenTermDoesNotContainWildcardCharacter() {
 assertFalse(reflectionSearch.hasWildcard("MyAnnotation"));
 assertFalse(reflectionSearch.hasWildcard("Annotation"));
 }

 @Test
 public void testMethodsThatHave_StringTerm_AddsMethodCriteriaToQuery() throws BeanBinException {
 reflectionSearch.methodsThatHave("testMethod");
 assertTrue(reflectionSearch.query.getCriterias().size() == 1);
 MethodCriteria methodCriteria = (MethodCriteria) reflectionSearch.query.getCriterias().get(0);
 assertEquals(SearchType.EQUALS, methodCriteria.getComparisonType());
 assertEquals("testMethod", methodCriteria.getSearchTerm());
 }

 @Test
 public void testMethodsThatDontHave_StringTerm_AddsMethodCriteriaToQuery() throws BeanBinException {
 reflectionSearch.methodsThatDontHave("testMethod");
 assertTrue(reflectionSearch.query.getCriterias().size() == 1);
 MethodCriteria methodCriteria = (MethodCriteria) reflectionSearch.query.getCriterias().get(0);
 assertEquals(SearchType.DOESNOTEQUAL, methodCriteria.getComparisonType());
 assertEquals("testMethod", methodCriteria.getSearchTerm());
 }

 @Test
 public void testAnd_AddsConditionalAndToQuery() {
 reflectionSearch.and();
 assertTrue(reflectionSearch.query.getNextConditional() instanceof Conditional);
 assertEquals(Conditional.AND, reflectionSearch.query.getNextConditional());
 }

 @Test
 public void testOr_AddsConditionalOrToQuery() {
 reflectionSearch.or();
 assertTrue(reflectionSearch.query.getNextConditional() instanceof Conditional);
 assertEquals(Conditional.OR, reflectionSearch.query.getNextConditional());
 }

 @Test
 public void testGetMethods_ReturnsEmptyListWhenNoMatchingMethods() throws BeanBinException {
 when(ReflectionShelf.getInstance().get(reflectionSearch.query)).thenReturn(Collections.emptyList());
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(0, methods.size());
 }

 @Test
 public void testGetMethods_ReturnsListOfMethodsMatchingQuery() throws BeanBinException {
 List<Method> testMethods = Arrays.asList(new Method() {}, new Method() {}, new Method() {});
 when(ReflectionShelf.getInstance().get(reflectionSearch.query)).thenReturn(testMethods);
 List<Method> methods = reflectionSearch.getMethods();
 assertEquals(3, methods.size());
 }

 @Test(expected = BeanBinException.class)
 public void testGetMethods_ThrowsBeanBinExceptionWhenQueryContainsError() throws BeanBinException {
 reflectionSearch.query.add(new InvalidCriteria());
 reflectionSearch.getMethods();
 }
}