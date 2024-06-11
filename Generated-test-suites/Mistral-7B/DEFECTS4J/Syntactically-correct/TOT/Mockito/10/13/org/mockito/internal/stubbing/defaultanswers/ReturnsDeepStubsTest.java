package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mock1;
 private Object mock2;
 private Object mock3;

 @Before
 public void setUp() {
 mock1 = mock(Map.class, Answers.RETURNS_DEEP_STUBS);
 mock2 = mock(Number.class, Answers.RETURNS_DEEP_STUBS);
 mock3 = mock(Number.class, Answers.RETURNS_DEEP_STUBS);
 returnsDeepStubs = Mockito.spy(ReturnsDeepStubs.class);
 }

 @Test
 public void shouldReturnDeepStubOfTypeMapWhenInvokingMapMethod() {
 Map<Integer, Set<Number>> map = (Map<Integer, Set<Number>>) mock1;
 Set<Number> set = mock2;
 Set<Number> nestedSet = mock3;
 map.put(1, set);
 set.add(nestedSet);

 InvocationOnMock invocation = Mockito.mockMethod(Map.class, "get", Integer.class);
 invocation.argThat(ArgumentMatchers.eq(1));

 Object deepStubMap = returnsDeepStubs.answer(invocation);

 verify(deepStubMap).put(ArgumentMatchers.eq(1), ArgumentMatchers.isA(Set.class));
 }

 @Test
 public void shouldReturnDeepStubOfTypeNumberWhenInvokingNumberMethod() {
 Number number = (Number) mock1;
 number.compareTo(mock2);

 InvocationOnMock invocation = Mockito.mockMethod(Number.class, "compareTo", Number.class);
 invocation.argThat(ArgumentMatchers.eq(mock2));

 Object deepStubNumber = returnsDeepStubs.answer(invocation);

 verify(deepStubNumber).compareTo(ArgumentMatchers.eq(mock2));
 }

 @Test(expected = IllegalStateException.class)
 public void shouldThrowExceptionWhenTypeIsNotMockable() {
 Object nonMockableType = mock(String.class);
 InvocationOnMock invocation = Mockito.mockMethod(String.class, "length");

 try {
 returnsDeepStubs.answer(invocation);
 } catch (Throwable e) {
 throw new IllegalStateException("Should not reach here, as String is not mockable", e);
 }
 }

 @Test
 public void shouldReturnDelegateAnswerWhenTypeIsAlreadyAMock() {
 Object alreadyAMock = mock(Map.class);
 InvocationOnMock invocation = Mockito.mockMethod(Map.class, "get", Integer.class);
 invocation.argThat(ArgumentMatchers.eq(1));

 Object deepStubMap = returnsDeepStubs.answer(invocation);
 Object delegateAnswer = delegate().returnValueFor(Map.class);

 assertEquals(delegateAnswer, deepStubMap);
 }

 @Test
 public void shouldReturnDeepStubForNestedGenericTypes() {
 interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
 }

 GenericsNest<Integer> genericNest = (GenericsNest<Integer>) mock1;
 Number number = genericNest.entrySet().iterator().next().getValue().iterator().next();

 InvocationOnMock nestedInvocation = Mockito.mockMethod(Number.class, "intValue");

 Object deepStubNumber = returnsDeepStubs.answer(nestedInvocation);

 verify(deepStubNumber).intValue();
 }

 @Test
 public void shouldReturnDeepStubInstanceWhenInvokingWithSameArgumentsMultipleTimes() throws Exception {
 Object deepStub = newDeepStub(Map.class);
 InvocationOnMock invocation = Mockito.mockMethod(Map.class, "put", Object.class, Object.class);

 Object answer = returnsDeepStubs.answer(invocation);
 Object answer2 = returnsDeepStubs.answer(invocation);

 assertEquals(answer, answer2);
 }

 @Test
 public void shouldRecordDeepStubAnswer() throws Exception {
 Object deepStub = newDeepStub(Map.class);
 InvocationOnMock invocation = Mockito.mockMethod(Map.class, "put", Object.class, Object.class);

 returnsDeepStubs.recordDeepStubAnswer(deepStub, null);
 Object answer = returnsDeepStubs.answer(invocation);
 verify(deepStub).answer(invocation);
 }

 private Object newDeepStub(Class<?> type) {
 return mockitoCore().mock(type, withSettings().defaultAnswer(returnsDeepStubs));
 }

 private MockitoCore mockitoCore() {
 return LazyHolder.MOCKITO_CORE;
 }

 private ReturnsEmptyValues delegate() {
 return LazyHolder.DELEGATE;
 }

 private static class LazyHolder {
 private static final MockitoCore MOCKITO_CORE = new MockitoCore();
 private static final ReturnsEmptyValues DELEGATE = new ReturnsEmptyValues();
 }
}