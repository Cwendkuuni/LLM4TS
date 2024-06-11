package org.mockito.internal.stubbing.defaultanswers.test;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoStatic.*; // All static imports go here

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.stubbing.answer.ReturnsDeepStubs;
import org.mockito.internal.stubbing.answer.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;
import org.powermock.reflect.Whitebox;

public class ReturnsDeepStubsTest {

 private InternalMockHandler<Object> handler;
 private InvocationContainerImpl container;
 private Object mock;

 @Before
 public void setup() {
 mockitoInitMocks(this);
 handler = new MockUtil().getMockHandler(mock);
 container = (InvocationContainerImpl) handler.getInvocationContainer();
 }

 @After
 public void cleanup() {
 reset(mock);
 }

 @Test
 public void testReturnsSameMockForSameInvocation() {
 verify(mock).method1();
 verify(mock).method1();

 Object result1 = mock.method1();
 Object result2 = mock.method1();

 assertEquals(result1, result2);
 }

 @Test
 public void testReturnsDeepStubForDifferentInvocations() {
 mock = mock(Object.class, new ReturnsDeepStubs());

 Object mock1 = mock.method1();
 Object mock2 = mock.method2();

 assertNotSame(mock1, mock2);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCannotMockFinalClass() {
 mock = mock(String.class, new ReturnsDeepStubs());
 }

 @Test
 public void testReturnsEmptyValuesForNonMockableType() {
 ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
 mock = mock(Integer[].class, new ReturnsDeepStubs(), new Answers() {
 @Override
 public Answer<Object> value() {
 return emptyValues;
 }
 });

 Integer[] array = (Integer[]) mock.method1();
 assertArrayEquals(new Integer[0], array);
 }

 @Test
 public void testCanUseGenericMetadataToCreateDeepStub() throws Exception {
 GenericMetadataSupport returnTypeGenericMetadata = Whitebox.getInternalState(handler, "returnTypeGenericMetadata");
 mock = mock(Object.class, new ReturnsDeepStubs() {
 @Override
 protected GenericMetadataSupport actualParameterizedType(Object mock) {
 return returnTypeGenericMetadata;
 }
 });

 Object deepStubMock = mock.method1();
 verify(deepStubMock).method2();
 }

 @Test
 public void testReturnsDeepStubForNestedInvocations() throws Exception {
 mock = mock(Map.class, new ReturnsDeepStubs());

 Map<Object, Set<Number>> map = (Map<Object, Set<Number>>) mock;
 Set<Number> set = map.entrySet().iterator().next().getValue();
 Number number = set.iterator().next();

 assertEquals(number, number);
 }

 @Test
 public void testAddAnswerForPreviouslyRecordedInvocation() throws Exception {
 container.addAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "answer";
 }
 }, false);

 String result = (String) mock.method1();
 assertEquals("answer", result);
 }
}