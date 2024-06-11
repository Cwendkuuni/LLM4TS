package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private InvocationContainerImpl container;
 private Object mock;

 @Before
 public void setup() {
 mock = mock(Map.class);
 container = new InvocationContainerImpl();
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Mockito.doReturn(returnsDeepStubs).when(container).getAnswer();
 }

 @Test
 public void testAnswerWithDeepStubs() throws Throwable {
 // given
 Map<Integer, List<Number>> map = (Map<Integer, List<Number>>) returnsDeepStubs.answer(ArgumentMatchers.any(InvocationOnMock.class));

 // when
 List<Number> list = map.get(1);
 Number number = list.get(0);
 Object deepStub = number.getClass().newInstance();
 Mockito.doReturn(deepStub).when(container).getAnswer(ArgumentMatchers.any(InvocationOnMock.class));

 // then
 assertEquals(deepStub, number);
 verify(container).getAnswer(ArgumentMatchers.any(InvocationOnMock.class));
 }

 @Test
 public void testAnswerWithEmptyValues() throws Throwable {
 // given
 Map<Integer, List<Number>> map = (Map<Integer, List<Number>>) returnsEmptyValues.answer(ArgumentMatchers.any(InvocationOnMock.class));

 // when
 List<Number> list = map.get(1);
 Number number = list.get(0);

 // then
 assertEquals(Collections.emptyList(), list);
 assertEquals(null, number);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAnswerWithUnmockableType() throws Throwable {
 // given
 Object unmockableType = new Object() {};

 // when
 returnsDeepStubs.answer(ArgumentMatchers.any(InvocationOnMock.class));

 // then
 // UnsupportedOperationException is thrown by the MockCreationValidator
 }

 @Test
 public void testRecordDeepStubMock() throws Throwable {
 // given
 Class<List<Number>> listClass = List.class.getGenericType();
 Class<Map<Integer, List<Number>>> mapClass = Map.class.getGenericType();
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 InvocationOnMock invocationWithDeepStub = Mockito.mock(InvocationOnMock.class);
 InvocationOnMock invocationWithDeepStubMap = Mockito.mock(InvocationOnMock.class);
 InvocationOnMock invocationWithDeepStubList = Mockito.mock(InvocationOnMock.class);

 Mockito.doReturn(mapClass).when(container).getTypeToMock();
 Mockito.doReturn(listClass).when(container).resolveGenericReturnType(ArgumentMatchers.any(Method.class));
 Mockito.doReturn(invocationWithDeepStub).when(container).getInvocationForStubbing();
 Mockito.doReturn(invocation).when(container).getInvocation(ArgumentMatchers.anyInt());
 Mockito.doReturn(invocationWithDeepStubList).when(container).getInvocation(ArgumentMatchers.eq(1));
 Mockito.doReturn(invocationWithDeepStubMap).when(container).getInvocation(ArgumentMatchers.anyInt()).get(0);

 // when
 Object deepStubMap = returnsDeepStubs.answer(ArgumentMatchers.any(InvocationOnMock.class));
 List<Number> list = (List<Number>) deepStubMap.get(1);
 Number number = list.get(0);
 Object deepStubList = number.getClass().newInstance();
 Mockito.doReturn(deepStubList).when(container).getAnswer(ArgumentMatchers.any(InvocationOnMock.class));

 // then
 assertEquals(deepStubList, number);
 verify(container).getAnswer(ArgumentMatchers.any(InvocationOnMock.class));
 verify(container).recordDeepStubMock(ArgumentMatchers.any(InvocationOnMock.class), ArgumentMatchers.eq(container));
 }
}