package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito. ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object deepMock;

 @Before
 public void setup() {
 returnsDeepStubs = new ReturnsDeepStubs();
 deepMock = Mockito.mock(DeepMock.class, Answers.RETURNS_DEEP_STUBS);
 }

 @Test
 public void testAnswerWithMatchingInvocation() throws Throwable {
 // Given
 when(deepMock.getDeepValue()).thenReturn(42);

 // When & Then
 Object returnedValue = deepMock.getDeepValue();
 verify(deepMock).getDeepValue();

 // Then the answer should be the same mock instance
 assertEquals(deepMock, returnedValue);
 }

 @Test
 public void testAnswerWithDifferentInvocation() throws Throwable {
 // Given
 when(deepMock.getDeepValue()).thenReturn(42);

 // When
 Object differentMock = Mockito.mock(DifferentMock.class, Answers.RETURNS_DEEP_STUBS);

 // Then the answer should be the same mock instance for the original invocation, but a new one for the different invocation
 assertEquals(deepMock, deepMock.getDeepValue());
 assertEquals(differentMock, differentMock.getDeepValue());
 }

 @Test(expected = Throwable.class)
 public void testAnswerWithException() throws Throwable {
 // Given
 when(deepMock.getDeepValue()).thenThrow(new IOException());

 // When
 // Then an exception should be thrown
 deepMock.getDeepValue();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAnswerWithUnsupportedType() throws Throwable {
 // Given
 when(deepMock.getDeepValue()).thenAnswer(returnsDeepStubs);

 // When
 // Then an UnsupportedOperationException should be thrown since the type is not mockable
 deepMock.getClass().getMethod("unsupportedMethod").invoke(deepMock);
 }

 @Test
 public void testSerialization() throws Throwable {
 // Given
 Object originalDeepMock = Mockito.mock(DeepMock.class, Answers.RETURNS_DEEP_STUBS);
 ReturnsDeepStubsSerializationFallback deepStubsAnswer = new ReturnsDeepStubsSerializationFallback(
 GenericMetadataSupport.inferFrom(originalDeepMock.getClass()));
 when(originalDeepMock.getDeepValue()).thenAnswer(deepStubsAnswer);

 // When & Then
 Object serializedDeepMock = Mockito.serialize(originalDeepMock);
 Object deserializedDeepMock = Mockito.deserialize(serializedDeepMock);

 // Then the deserialized mock should be the same instance as the original one
 assertEquals(originalDeepMock, deserializedDeepMock);
 }

}

class DeepMock {
 interface DeepNestedInterface<Integer> extends Map<Integer, Set<Number>> {}
 interface DifferentMock {}

 Integer getDeepValue() throws IOException {}
}