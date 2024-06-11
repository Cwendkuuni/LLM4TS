package org.mockito.internal.stubbing.defaultanswers.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mock;

 @Before
 public void setup() {
 mock = Mockito.mock(Object.class);
 returnsDeepStubs = new ReturnsDeepStubs();
 }

 @Test
 public void testReturnsDeepStubsForMatchingInvocation() {
 // Given
 when(mock.someMethod(ArgumentMatchers.anyInt())).thenReturn(42);

 // When
 Object returnValue = mock.someMethod(1);

 // Then
 verify(mock).someMethod(ArgumentMatchers.eq(1));

 // deep stub is returned
 Object deepStub = mock.someDeepNestedMethod(Arrays.asList(1, 2, 3));
 Object deepStubReturnValue = deepStub.getFirstElement();

 // deep stub is used instead of original mock for the next invocation
 verify(deepStub).someMethod(ArgumentMatchers.eq(1));
 assertSame(deepStubReturnValue, deepStub.getFirstElement());
 }

 @Test
 public void testReturnsDeepStubsForNestedGenericTypes() {
 // Given
 interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
 }
 GenericsNest<Number> mockGenericsNest = Mockito.mock(GenericsNest.class, new ReturnsDeepStubs());
 Number number = mockGenericsNest.entrySet().iterator().next().getValue().iterator().next();

 // When
 Number numberValue = number.intValue();

 // Then
 verify(mockGenericsNest).entrySet();
 verify(mockGenericsNest).iterator();
 verify(mockGenericsNest).getValue();
 verify(mockGenericsNest.getValue()).iterator();
 verify(mockGenericsNest.getValue().iterator()).next();

 // deep stub is used instead of original mock for the next invocation
 verify(number).intValue();
 assertSame(numberValue, number.intValue());
 }

 @Test(expected = IllegalStateException.class)
 public void testThrowsExceptionForNonMockableType() {
 // Given
 when(mock.someMethod(ArgumentMatchers.anyInt())).thenReturn(42);

 // When and Then
 mock.someMethod(1); // will return a deep stub

 // This test case should throw an exception because String is not mockable
 mock.someMethod((String) "someString");
 }

 @Test
 public void testDoesNotReturnDeepStubsForNonMatchingInvocation() {
 // Given
 when(mock.someMethod(ArgumentMatchers.anyInt())).thenReturn(42);

 // When
 Object returnValue = mock.someOtherMethod(1);

 // Then
 verify(mock).someMethod(ArgumentMatchers.never());
 verify(mock).someOtherMethod(ArgumentMatchers.eq(1));
 assertNotSame(mock, returnValue);
 }

 @Test
 public void testCreatesNewDeepStubMockWhenInvocationDoesNotHaveAnswer() {
 // Given
 when(mock.someMethod(ArgumentMatchers.anyInt())).thenReturn(42);

 // When
 Object deepStub = mock.someNonExistentMethod(1);

 // Then
 verify(mock).someMethod(ArgumentMatchers.never());
 verify(mock).someNonExistentMethod(ArgumentMatchers.eq(1));
 assertNotSame(mock, deepStub);
 }

 @Test
 public void testCreatesNewDeepStubMockWhenInvocationDoesNotMatchAnyStubbedInvocations() {
 // Given
 when(mock.someMethod(ArgumentMatchers.anyInt())).thenReturn(42);
 mock.someOtherMethod(1); // this invocation is stubbed for verification, but does not return a deep stub

 // When
 Object deepStub = mock.someNonExistentMethod(1);

 // Then
 verify(mock).someMethod(ArgumentMatchers.never());
 verify(mock).someOtherMethod(ArgumentMatchers.never());
 verify(mock).someNonExistentMethod(ArgumentMatchers.eq(1));
 assertNotSame(mock, deepStub);
 }
}