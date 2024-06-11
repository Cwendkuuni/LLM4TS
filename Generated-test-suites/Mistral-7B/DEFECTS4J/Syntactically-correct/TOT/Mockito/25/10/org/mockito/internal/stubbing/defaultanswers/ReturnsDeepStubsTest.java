package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private GenericsNest<Integer> genericsNest;
 private GenericsNest<String> stringGenericsNest;

 @Before
 public void setUp() {
 returnsDeepStubs = new ReturnsDeepStubs();
 genericsNest = mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);
 stringGenericsNest = mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);
 }

 @Test
 public void testReturnsDeepStubsForNestedGenericType() {
 // Given
 GenericsNest<GenericsNest<Integer>> nestedGenericsNest = genericsNest.entrySet().iterator().next().getValue();
 GenericsNest<Number> nestedNumber = nestedGenericsNest.iterator().next().getValue().iterator().next();

 // When
 Number number = nestedNumber.iterator().next();

 // Then
 Object deepStub = number; // This line will return the deep stub created for Number
 assertEquals(Number.class, deepStub.getClass());
 }

 @Test
 public void testReturnsEmptyValueForNonMockableType() {
 // Given
 List<String> list = Collections.singletonList("Mockito");
 // When
 String emptyString = returnsDeepStubs.answer(Mockito.mock(List.class), ArgumentMatchers.argThat(invocation -> invocation.getMethod().getReturnType() == String.class));

 // Then
 assertEquals("", emptyString);
 }

 @Test
 public void testReturnsMockForMatchingInvocation() {
 // Given
 GenericsNest<Integer> genericMock = mock(GenericsNest.class, returnsDeepStubs);
 GenericsNest<Integer> genericStub = mock(GenericsNest.class, returnsDeepStubs);

 // When
 // First invocation returns the mock
 Object firstMock = genericMock.entrySet().iterator().next().getValue().iterator().next();

 // Second invocation returns the stub
 genericStub.entrySet().iterator().next().setValue(new Object()); // Set a new value to the entry set to change the behavior
 Object secondStub = genericStub.entrySet().iterator().next().getValue().iterator().next();

 // Third invocation returns the mock again
 Object thirdMock = genericMock.entrySet().iterator().next().getValue().iterator().next();

 // Verify that the correct mocks were returned for each invocation
 verify(genericMock).answer(eq(ArgumentMatchers.isInstance(firstMock.getClass())), ArgumentMatchers.isInstance(firstMock));
 verify(genericStub).answer(eq(ArgumentMatchers.isInstance(secondStub.getClass())), ArgumentMatchers.isInstance(secondStub));
 verify(genericMock).answer(eq(ArgumentMatchers.isInstance(thirdMock.getClass())), ArgumentMatchers.isInstance(thirdMock));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testThrowsIllegalArgumentExceptionForNonGenericMock() {
 // Given
 Object nonGenericMock = mock(Object.class, returnsDeepStubs);

 // When & Then
 // This test case will throw an IllegalArgumentException because the class Object is not mockable with ReturnsDeepStubs
 returnsDeepStubs.answer(Mockito.mock(Object.class), ArgumentMatchers.any());
 }
}