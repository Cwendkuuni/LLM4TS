package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mockA;
 private Object mockB;
 private Object mockC;

 @Before
 public void setUp() {
 returnsDeepStubs = new ReturnsDeepStubs();
 mockA = Mockito.mock(Object.class);
 mockB = Mockito.mock(Object.class);
 mockC = Mockito.mock(Object.class);
 }

 @Test
 public void testAnswerWithSameClass() {
 // Given
 Object returnedMockA = returnsDeepStubs.answer(Mockito.argThat(invocation -> "getClass".equals(invocation.getMethod().getName())));
 // When
 assertEquals(mockA, returnedMockA);
 // Then
 verify(mockA).answer(ArgumentMatchers.any());
 }

 @Test
 public void testAnswerWithDifferentClasses() {
 // Given
 Object mockList = Mockito.mock(List.class);
 Object mockMap = Mockito.mock(Map.class);
 Object returnedMockMap = returnsDeepStubs.answer(Mockito.argThat(invocation -> Map.class.isAssignableFrom(invocation.getMethod().getReturnType())));

 Mockito.when(mockMap.entrySet()).thenReturn((Map.Entry) Mockito.mock(Map.Entry.class));
 Mockito.when(Mockito.mock(Map.Entry.class).getValue()).thenReturn(mockList);

 // When
 Object returnedMockList = ((Map.Entry<Object, Object>) returnedMockMap.entrySet().iterator().next()).getValue();

 // Then
 assertEquals(mockList, returnedMockList);
 verify(mockMap).entrySet();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAnswerWithUnmockableClass() {
 // Given
 Object mockUnmockableClass = Mockito.mock(String.class);

 // When & Then
 returnsDeepStubs.answer(Mockito.argThat(invocation -> String.class.isAssignableFrom(invocation.getMethod().getReturnType())));
 }

 @Test
 public void testRecordDeepStubMock() {
 // Given
 Object mockD = Mockito.mock(Object.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

 Mockito.when(container.getInvocationForStubbing()).thenReturn(invocation);
 Mockito.when(container.addAnswer(ArgumentMatchers.any(Answer.class), ArgumentMatchers.eq(false))).thenReturn(null);

 // When
 Object deepStubMock = returnsDeepStubs.getMock(invocation);

 // Then
 verify(container).addAnswer(ArgumentMatchers.any(Answer.class), ArgumentMatchers.eq(false));
 assertEquals(mockD, deepStubMock);
 }
}