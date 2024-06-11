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
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<MockitoInvocationHandler> mockHandler;
 private MockSettingsImpl mockSettings;
 private MockitoInvocationHandler mockitoInvocationHandler;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<>(mockSettings);
 mockitoInvocationHandler = mock(MockitoInvocationHandler.class);
 }

 @Test
 public void testHandleVoidMethodInvocationWithStubbing() {
 // Given
 when(mockitoInvocationHandler.handle(ArgumentMatchers.any(InvocationOnMock.class)))
 .thenAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
 System.out.println("Handled void method invocation with stubbing.");
 return null;
 }
 });

 // When
 Mockito.doAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
 System.out.println("Invocation with void method handled by doAnswer().");
 return null;
 }
 }).when(mockitoInvocationHandler).handle(ArgumentMatchers.any(InvocationOnMock.class));

 // Then
 InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
 mockHandler.handle(invocationOnMock);

 verify(mockitoInvocationHandler).handle(ArgumentMatchers.eq(invocationOnMock));
 }

 @Test
 public void testHandleNonVoidMethodInvocationWithStubbing() {
 // Given
 when(mockitoInvocationHandler.handle(ArgumentMatchers.any(InvocationOnMock.class)))
 .thenReturn(1);

 // When
 when(mockitoInvocationHandler.handle(ArgumentMatchers.any(InvocationOnMock.class)))
 .thenAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
 System.out.println("Handled non-void method invocation with doAnswer().");
 return 2;
 }
 });

 // Then
 InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
 Object result = mockHandler.handle(invocationOnMock);

 assertEquals(2, result);
 verify(mockitoInvocationHandler).handle(ArgumentMatchers.eq(invocationOnMock));
 }

 @Test
 public void testHandleInvocationWithVerificationMode() {
 // Given
 InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
 mockHandler = new MockHandler<MockitoInvocationHandler>() {};
 VerificationMode verificationMode = Mockito.verify(invocationOnMock);

 // When
 mockHandler.handle(invocationOnMock);

 // Then
 verifyZeroInteractions(invocationOnMock);
 }

 @Test(expected = IllegalStateException.class)
 public void testHandleInvocationWithNullInvocation() {
 // Given
 mockHandler = new MockHandler<MockitoInvocationHandler>() {};

 // When
 mockHandler.handle(null);

 // Then
 // Exception is expected
 }
}