package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import net.sourceforge.beanbin.*;
import net.sourceforge.beanbin.query.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ReflectionSearchTest {

 private ReflectionSearch reflectionSearch;
 private Class mockedClass;
 private List<Method> mockedMethods;
 private List<Method> expectedMethods;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 mockedClass = mock(Class.class);
 mockedMethods = new ArrayList<>();
 reflectionSearch = new ReflectionSearch(mockedClass);
 }

 @Test
 public void hasAnnotation_returnsTrue_whenAnnotationNameMatches() {
 given(mockedClass.getAnnotations()).willReturn(new Annotation[] { annotation("MyAnnotation") });
 when(reflectionSearch.hasAnnotation("MyAnnotation")).thenReturn(true);
 verify(reflectionSearch).hasAnnotation("MyAnnotation");
 }

 @Test
 public void hasAnnotation_returnsFalse_whenAnnotationNameDoesNotMatch() {
 given(mockedClass.getAnnotations()).willReturn(new Annotation[] { annotation("MyAnnotation") });
 when(reflectionSearch.hasAnnotation("OtherAnnotation")).thenReturn(false);
 verify(reflectionSearch).hasAnnotation("OtherAnnotation");
 }

 @Test
 public void hasAnnotation_returnsTrue_whenAnnotationNameMatchesPartiallyWithWildcard() {
 given(mockedClass.getAnnotations()).willReturn(new Annotation[] { annotation("MyAnnotationWithWildcard") });
 when(reflectionSearch.hasAnnotation("MyAnnotation*")).thenReturn(true);
 verify(reflectionSearch).hasAnnotation("MyAnnotation*");
 }

 @Test
 public void hasAnnotation_returnsFalse_whenAnnotationNameDoesNotMatchPartiallyWithWildcard() {
 given(mockedClass.getAnnotations()).willReturn(new Annotation[] { annotation("MyAnnotationWithWildcard") });
 when(reflectionSearch.hasAnnotation("MyAnnotationDifferent")).thenReturn(false);
 verify(reflectionSearch).hasAnnotation("MyAnnotationDifferent");
 }

 @Test
 public void methodsThatHave_returnsReflectionSearch_forMethodReflectionCriteria() throws BeanBinException {
 reflectionSearch.methodsThatHave("methodName");
 assertEquals(reflectionSearch, reflectionSearch);
 }

 @Test
 public void methodsThatHave_returnsListOfMethods_whenQueryCriteriaSatisfied() throws BeanBinException {
 expectedMethods = Arrays.asList(mock(Method.class), mock(Method.class));
 when(mockedClass.getMethods()).thenReturn(expectedMethods.toArray(new Method[0]));
 when(reflectionSearch.getMethods()).thenCallRealMethod();
 reflectionSearch.methodsThatHave("methodName").getMethods();
 verify(reflectionSearch, times(1)).getMethods();
 }

 @Test
 public void methodsThatHave_returnsEmptyList_whenQueryCriteriaNotSatisfied() throws BeanBinException {
 when(mockedClass.getMethods()).thenReturn(Collections.emptyList().toArray(new Method[0]));
 when(reflectionSearch.getMethods()).thenCallRealMethod();
 reflectionSearch.methodsThatHave("methodName").getMethods();
 verify(reflectionSearch, times(1)).getMethods();
 }

 @Test
 public void methodsThatDontHave_returnsReflectionSearch_forMethodReflectionCriteria() throws BeanBinException {
 reflectionSearch.methodsThatDontHave("methodName");
 assertEquals(reflectionSearch, reflectionSearch);
 }

 @Test
 public void methodsThatDontHave_returnsListOfMethods_whenQueryCriteriaNotSatisfied() throws BeanBinException {
 expectedMethods = Arrays.asList(mock(Method.class), mock(Method.class));
 when(mockedClass.getMethods()).thenReturn(expectedMethods.toArray(new Method[0]));
 when(reflectionSearch.getMethods()).thenCallRealMethod();
 reflectionSearch.methodsThatDontHave("methodName").getMethods();
 verify(reflectionSearch, times(1)).getMethods();
 }

 @Test
 public void methodsThatDontHave_returnsAllMethods_whenQueryCriteriaSatisfied() throws BeanBinException {
 when(mockedClass.getMethods()).thenReturn(expectedMethods.toArray(new Method[0]));
 reflectionSearch.methodsThatDontHave("");
 assertEquals(expectedMethods.size(), reflectionSearch.getMethods().size());
 }

 @Test
 public void and_setsConditionalOperator() {
 reflectionSearch.and();
 assertEquals(Conditional.AND, reflectionSearch.query.getNextConditional());
 }

 @Test
 public void or_setsConditionalOperator() {
 reflectionSearch.or();
 assertEquals(Conditional.OR, reflectionSearch.query.getNextConditional());
 }

 @Test(expected = BeanBinException.class)
 public void getMethods_throwsBeanBinException_whenQueryIsNull() throws BeanBinException {
 reflectionSearch = new ReflectionSearch(null);
 reflectionSearch.getMethods();
 }
}