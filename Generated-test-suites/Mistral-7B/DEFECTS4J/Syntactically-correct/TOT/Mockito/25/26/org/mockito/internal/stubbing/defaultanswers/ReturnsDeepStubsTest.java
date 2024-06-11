package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mockMap;
 private Object mockNumber;
 private Object mockGenericsNest;

 @Before
 public void setUp() {
 returnsDeepStubs = Mockito.spy(new ReturnsDeepStubs());
 mockMap = Mockito.mock(Map.class);
 mockNumber = Mockito.mock(Number.class);
 mockGenericsNest = Mockito.mock(GenericsNest.class);
 }

 @Test
 public void testAnswerWithSameClass() {
 // given
 when(mockMap.iterator()).thenReturn(Mockito.mock(Iterator.class).iterator());
 when(mockMap.iterator().next()).thenReturn(Mockito.mock(Map.Entry.class).value(mockNumber));

 // when
 Object result1 = returnsDeepStubs.answer(Mockito.mock(InvocationOnMock.class).forMethodCall(mockMap, "iterator"));
 Object result2 = returnsDeepStubs.answer(Mockito.mock(InvocationOnMock.class).forMethodCall(mockMap, "iterator"));

 // then
 assertEquals(mockNumber, result1);
 assertEquals(mockNumber, result2);
 }

 @Test
 public void testAnswerWithDifferentClasses() {
 // given
 when(mockMap.iterator()).thenReturn(Mockito.mock(Iterator.class).iterator());
 when(mockMap.iterator().next()).thenReturn(Mockito.mock(Map.Entry.class).value(mockNumber));
 when(mockNumber.iterator()).thenReturn(Mockito.mock(Iterator.class).iterator());
 when(mockNumber.iterator().next()).thenReturn(1);

 // when
 Object result1 = returnsDeepStubs.answer(Mockito.mock(InvocationOnMock.class).forMethodCall(mockMap, "iterator").get(0).get(1));
 Object result2 = returnsDeepStubs.answer(Mockito.mock(InvocationOnMock.class).forMethodCall(mockNumber, "iterator").get(0).get(1));

 // then
 assertEquals(1, result1);
 assertEquals(1, result2);
 }

 @Test(expected = IllegalStateException.class)
 public void testAnswerWithNonMockableClass() {
 // given
 when(mockMap.iterator()).thenReturn(Mockito.mock(Iterator.class).iterator());
 when(mockMap.iterator().next()).thenReturn(Mockito.mock(Map.Entry.class).value(Mockito.mock(String.class)));

 // when, then
 returnsDeepStubs.answer(Mockito.mock(InvocationOnMock.class).forMethodCall(mockMap, "iterator").get(0).get(1));
 }

 @Test
 public void testGetMock() {
 // given
 when(mockMap.iterator()).thenReturn(Mockito.mock(Iterator.class).iterator());
 when(mockMap.iterator().next()).thenReturn(Mockito.mock(Map.Entry.class).value(mockNumber));

 // when
 Object deepStubMap = returnsDeepStubs.getMock(Mockito.mock(InvocationOnMock.class).forMethodCall(mockMap, "iterator"));

 // then
 Mockito.verify(deepStubMap).iterator();
 Mockito.verify(deepStubMap).iterator().hasNext();
 Mockito.verify(deepStubMap.iterator().next()).getKey();
 Mockito.verify(deepStubMap.iterator().next()).getValue();
 }

 @Test
 public void testRecordDeepStubMock() {
 // given
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 InvocationContainerImpl container = Mockito.spy(Mockito.mock(InvocationContainerImpl.class));
 when(container.getInvocationForStubbing()).thenReturn(ArgumentMatchers.any());

 // when
 Object deepStubMock = returnsDeepStubs.recordDeepStubMock(invocation, container);

 // then
 Mockito.verify(container).addAnswer(ArgumentMatchers.any(Answer.class), ArgumentMatchers.eq(false));
 Mockito.verify(container.getInvocationForStubbing()).equals(invocation);
 assertEquals(deepStubMock, container.getAnswer(0).answer(ArgumentMatchers.any(InvocationOnMock.class)));
 }

 @Test
 public void testActualParameterizedType() {
 // given
 Object mockMockito = Mockito.mock(Object.class);
 CreationSettings mockSettings = new CreationSettings();
 mockSettings.setTypeToMock(mockMockito.getClass());

 // when
 GenericMetadataSupport actualParameterizedType = returnsDeepStubs.actualParameterizedType(mockMockito);

 // then
 assertEquals(mockMockito.getClass(), actualParameterizedType.getType());
 }
}