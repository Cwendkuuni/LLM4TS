package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.invocation.Invocation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;

 @Before
 public void setUp() {
 returnsDeepStubs = new ReturnsDeepStubs();
 }

 @Test
 public void testReturnsDeepStubsForSimpleType() {
 Object mock = mock(String.class, Answers.RETURNS_DEEP_STUBS);
 verify(mock).toString();
 }

 @Test
 public void testReturnsDeepStubsForComplexType() {
 interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
 }

 GenericsNest<Object> mock = mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);

 Number number = mock.entrySet().iterator().next().getValue().iterator().next();

 verify(number).intValue();
 }

 @Test
 public void testReturnsExistingMockForInvocation() {
 Object mock1 = mock(String.class, Answers.RETURNS_DEEP_STUBS);
 Object mock2 = mock(String.class, Answers.RETURNS_DEEP_STUBS);

 when(mock1.toString()).thenReturn("mock1");

 String result = (String) returnsDeepStubs.answer(new InvocationOnMock(mock1, new Method[]{}, new Object[0], new Class[0], null));

 assertEquals("mock1", result);

 verify(mock2).toString();
 }

 @Test
 public void testReturnsNewMockForInvocationWithoutMatchingStub() {
 Object mock1 = mock(String.class, Answers.RETURNS_DEEP_STUBS);
 Object mock2 = mock(String.class, Answers.RETURNS_DEEP_STUBS);

 when(mock1.toString()).thenReturn("mock1");

 Object newMock = mock(String.class, Answers.RETURNS_DEEP_STUBS);
 StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.argThat(i -> i.getMethod().getName().equals("toString"));
 InvocationOnMock invocation = new InvocationOnMock(mock1, new Method[]{}, new Object[0], new Class[0], stubbedInvocationMatcher);

 Object result = returnsDeepStubs.answer(invocation);

 assertEquals(newMock, result);

 verify(newMock).toString();
 }

 @Test
 public void testCreatesNewMockWithSpecifiedAnswerForInvocationWithoutMatchingStub() {
 Object mock = mock(String.class, Answers.RETURNS_DEEP_STUBS);
 Object answer = new Object() {};

 StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.argThat(i -> i.getMethod().getName().equals("toString"));
 InvocationOnMock invocation = new InvocationOnMock(mock, new Method[]{}, new Object[0], new Class[0], stubbedInvocationMatcher);

 when(mock.toString()).thenReturn(answer);

 Object result = returnsDeepStubs.answer(invocation);

 assertEquals(answer, result);
 }

 @Test
 public void testCreatesNewMockWithCorrectTypeForInvocationWithoutMatchingStub() {
 Object mock = mock(Map.class, Answers.RETURNS_DEEP_STUBS);
 Object newMock = mock(Map.class, Answers.RETURNS_DEEP_STUBS);

 when(mock.entrySet().iterator().next().getValue()).thenReturn(new Mock(Number.class, Answers.RETURNS_DEEP_STUBS));
 StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.argThat(i -> i.getMethod().getName().equals("iterator"));
 InvocationOnMock invocation = new InvocationOnMock(mock, new Method[]{}, new Object[0], new Class[0], stubbedInvocationMatcher);

 Object result = returnsDeepStubs.answer(invocation);

 assertEquals(newMock, result);
 }

 @Test
 public void testCreatesNestedMocksForComplexType() {
 interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
 }

 interface NestedGenericsNest<K extends Comparable<K> & Cloneable, L extends Comparable<L> & Cloneable> extends GenericsNest<K> {
 }

 GenericsNest<Object> mockGenericsNest = mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);
 NestedGenericsNest<Object, Object> mockNestedGenericsNest = mock(NestedGenericsNest.class, Answers.RETURNS_DEEP_STUBS);

 Object nestedObject = mockNestedGenericsNest.entrySet().iterator().next().getValue().iterator().next();

 verify(nestedObject).intValue();
 }

 @Test
 public void testCreatesNestedMocksForComplexTypeWithNestedGenericTypes() {
 interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
 }

 interface NestedGenericsNest<K extends Comparable<K> & Cloneable, L extends Number> extends GenericsNest<K> {
 }

 GenericsNest<Object> mockGenericsNest = mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);
 NestedGenericsNest<Object, Integer> mockNestedGenericsNest = mock(NestedGenericsNest.class, Answers.RETURNS_DEEP_STUBS);

 Object nestedNumber = mockNestedGenericsNest.entrySet().iterator().next().getValue().iterator().next();

 verify(nestedNumber).intValue();
 }

 @Test
 public void testCreatesMocksForDeepNestedGenericTypes() {
 interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
 }

 interface NestedGenericsNest<K extends Comparable<K> & Cloneable, L extends Number> extends GenericsNest<K> {
 }

 interface DeepNestedGenericsNest<K extends Comparable<K> & Cloneable, L extends Number, M extends Number> extends NestedGenericsNest<K, L> {
 }

 GenericsNest<Object> mockGenericsNest = mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);
 NestedGenericsNest<Object, Integer> mockNestedGenericsNest = mock(NestedGenericsNest.class, Answers.RETURNS_DEEP_STUBS);
 DeepNestedGenericsNest<Object, Integer, Integer> mockDeepNestedGenericsNest = mock(DeepNestedGenericsNest.class, Answers.RETURNS_DEEP_STUBS);

 Object nestedNumber1 = mockDeepNestedGenericsNest.entrySet().iterator().next().getValue().iterator().next();
 Object nestedNumber2 = mockDeepNestedGenericsNest.entrySet().iterator().next().getValue().iterator().next();

 verify(nestedNumber1).intValue();
 verify(nestedNumber2).intValue();
 }
}