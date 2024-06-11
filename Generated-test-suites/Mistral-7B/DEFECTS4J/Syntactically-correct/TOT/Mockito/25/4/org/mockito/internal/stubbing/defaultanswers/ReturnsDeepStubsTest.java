package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs answer;
 private Object mock;

 @Before
 public void setup() {
 answer = new ReturnsDeepStubs();
 mock = mock(Map.class, answer);
 }

 @Test
 public void testDeepStubbingForMapEntrySetIteratorNext() {
 Map<String, Set<Number>> map = (Map<String, Set<Number>>) mock;
 Set<Number> set = mock(Set.class);
 when(map.entrySet().iterator().next().getValue()).thenReturn(set);

 Number number = map.entrySet().iterator().next().getValue().iterator().next();

 verify(set).iterator();
 verify(set).next();

 assertEquals(number, map.entrySet().iterator().next().getValue().iterator().next());
 }

 @Test
 public void testDeepStubbingForMapEntrySetIteratorNext_EmptySet() {
 Map<String, Set<Number>> map = (Map<String, Set<Number>>) mock;
 Set<Number> set = mock(Set.class, new ReturnsEmptyValues());
 when(map.entrySet().iterator().next().getValue()).thenReturn(set);

 assertNull(map.entrySet().iterator().next().getValue().iterator().next());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testDeepStubbingForMapEntrySetIteratorNext_UnsupportedOperationException() {
 Map<String, Set<Number>> map = (Map<String, Set<Number>>) mock;
 Set<Number> set = mock(Set.class, new ThrowsExceptionAnswer(UnsupportedOperationException.class));
 when(map.entrySet().iterator().next().getValue()).thenReturn(set);

 map.entrySet().iterator().next().getValue().iterator().next();
 }

 @Test
 public void testDeepStubbingForMapEntrySetIteratorNext_NestedMap() {
 Map<String, Map<String, Number>> map = (Map<String, Map<String, Number>>) mock;
 Map<String, Number> nestedMap = mock(Map.class);
 when(map.entrySet().iterator().next().getValue()).thenReturn(nestedMap);

 Map.Entry<String, Map<String, Number>> entry = map.entrySet().iterator().next();
 Map<String, Number> nestedEntry = entry.getValue().entrySet().iterator().next();
 Number number = nestedEntry.getValue();

 assertEquals(number, map.entrySet().iterator().next().getValue().entrySet().iterator().next().getValue());
 }

 @Test(expected = ClassCastException.class)
 public void testDeepStubbingForMapEntrySetIteratorNext_ClassCastException() {
 Map<String, Object> map = (Map<String, Object>) mock;
 when(map.entrySet().iterator().next().getValue()).thenReturn(42);

 try {
 Map.Entry<String, Number> entry = (Map.Entry<String, Number>) map.entrySet().iterator().next();
 } catch (ClassCastException e) {
 //expected
 }
 }

 @Test
 public void testDeepStubbingForDeepStubbingAnotherMock() {
 Object outerMock = mock(Object.class);
 Object innerMock = mock(Object.class);
 when(outerMock.getMock()).thenReturn(innerMock);

 Answer<Object> deepStubAnswer = new ReturnsDeepStubs();
 Object deepStubAnswerInstance = mock(Answer.class, deepStubAnswer);
 when(innerMock.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn(deepStubAnswerInstance);

 when(deepStubAnswerInstance.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn(outerMock);

 assertSame(outerMock, deepStubAnswerInstance.answer(ArgumentMatchers.any(InvocationOnMock.class)));
 }

 private static class ThrowsExceptionAnswer implements Answer<Object> {
 private final Class<? extends Throwable> exceptionType;

 public ThrowsExceptionAnswer(Class<? extends Throwable> exceptionType) {
 this.exceptionType = exceptionType;
 }

 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 throw new exceptionType();
 }
 }
}