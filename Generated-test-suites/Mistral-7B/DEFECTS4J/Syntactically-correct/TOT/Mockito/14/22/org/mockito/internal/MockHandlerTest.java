package org.mockito.internal.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<DummyMock> mockHandler;
 private DummyMock mock;

 @Before
 public void setup() {
 mock = mock(DummyMock.class);
 mockHandler = new MockHandler<>(mock.getClass());
 }

 @Test
 public void testHandle_InvocationWithMatchingAnswer() {
 // Given
 when(mock.someMethod(1)).thenAnswer(invocation -> 2);

 // When
 Object result = mockHandler.handle(new InvocationOnMock(mock, "someMethod", new Object[]{1}));

 // Then
 verify(mock).someMethod(ArgumentMatchers.eq(1));
 verify(mock).someMethod(ArgumentMatchers.isA(Integer.class));
 verifyNoMoreInteractions(mock);

 // Assert
 Assert.assertEquals(2, result);
 }

 @Test
 public void testHandle_InvocationWithNoMatchingAnswer() {
 // Given
 when(mock.someMethod(1)).thenAnswer(null);

 // When
 Object result = mockHandler.handle(new InvocationOnMock(mock, "someMethod", new Object[]{1}));

 // Then
 verify(mock).someMethod(ArgumentMatchers.eq(1));
 verify(mock).someMethod(ArgumentMatchers.isA(Integer.class));
 verifyNoMoreInteractions(mock);

 // Assert
 Assert.assertNull(result);
 }

 @Test
 public void testHandle_InvocationWithVerificationMode() {
 // Given
 verify(mock).someMethod(ArgumentMatchers.eq(1));

 // When
 Object result = mockHandler.handle(new InvocationOnMock(mock, "someMethod", new Object[]{1}));

 // Then
 // Verification mode should be handled before any other logic
 // No need to assert on the result, as it will be null
 }

 @Test
 public void testHandle_InvocationWithMultipleAnswers() {
 // Given
 List<Answer> answers = new ArrayList<>();
 answers.add(invocation -> 1);
 answers.add(invocation -> 2);
 when(mock.someMethod(ArgumentMatchers.eq(1))).thenAnswer(answers);

 // When
 Object result1 = mockHandler.handle(new InvocationOnMock(mock, "someMethod", new Object[]{1}));
 Object result2 = mockHandler.handle(new InvocationOnMock(mock, "someMethod", new Object[]{1}));

 // Then
 verify(mock).someMethod(ArgumentMatchers.eq(1));
 verify(mock).someMethod(ArgumentMatchers.isA(Integer.class));
 verifyNoMoreInteractions(mock);

 // Assert
 Assert.assertEquals(1, result1);
 Assert.assertEquals(2, result2);
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_InvocationWithIllegalStateException() {
 // Given
 when(mock.throwingMethod()).thenThrow(new RuntimeException());
 when(mock.someMethod(ArgumentMatchers.anyInt())).thenCallRealMethod();

 // When / Then
 // Handle method should throw IllegalStateException if real method is called
 // and there is a stubbed method for the same invocation
 mockHandler.handle(new InvocationOnMock(mock, "throwingMethod", new Object[0]));
 mockHandler.handle(new InvocationOnMock(mock, "someMethod", new Object[]{1}));
 }
}

class DummyMock {
 void someMethod(int arg) {}
 void throwingMethod() throws RuntimeException {}
}