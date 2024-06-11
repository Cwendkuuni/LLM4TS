import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*; // for stubbing and verification

public class MockHandlerTest {

 private MockHandler<MockitoTestMock> mockHandler;
 private MockitoTestMock mock;

 @Before
 public void setUp() {
 mock = mock(MockitoTestMock.class);
 mockHandler = new MockHandler<MockitoTestMock>(mock);
 }

 @Test
 public void testHandle_noStubbingOrVerification_returnsDefaultAnswer() {
 // given
 Invocation invocation = mock(InvocationOnMock.class);
 when(invocation.getMock()).thenReturn(mock);
 when(invocation.getMethod()).thenReturn(Object.class.getMethod("methodToMock", int.class));

 // when
 Object result = mockHandler.handle(invocation);

 // then
 assertEquals(mockSettings.getDefaultAnswer().answer(invocation), result);
 }

 @Test
 public void testHandle_stubbing_returnsStubbedValue() {
 // given
 Invocation invocation = mock(InvocationOnMock.class);
 when(invocation.getMock()).thenReturn(mock);
 when(invocation.getMethod()).thenReturn(Object.class.getMethod("methodToMock", int.class));
 Answer stubAnswer = arg -> 42;
 mockHandler.setAnswersForStubbing(List.of(stubAnswer));

 // when
 Object result = mockHandler.handle(invocation);

 // then
 verify(mockHandler).setAnswersForStubbing(List.of(stubAnswer));
 verifyNoMoreInteractions(mockHandler); // to ensure no unintended interaction
 assertEquals(42, result);
 }

 @Test
 public void testHandle_verification_throwsException() {
 // given
 Invocation invocation = mock(InvocationOnMock.class);
 when(invocation.getMock()).thenReturn(mock);
 when(invocation.getMethod()).thenReturn(Object.class.getMethod("methodToMock", int.class));
 VerificationMode verificationMode = verify(mock, times(1)).methodToMock(anyInt());

 // when / then
 assertThrows(AssertionError.class, () -> mockHandler.handle(invocation));
 }

 @Test
 public void testHandle_verification_onDifferentMock_doesNotThrowException() {
 // given
 MockitoTestMock mock1 = mock(MockitoTestMock.class);
 MockitoTestMock mock2 = mock(MockitoTestMock.class);
 Invocation invocation = mock(InvocationOnMock.class);
 when(invocation.getMock()).thenReturn(mock1);
 when(invocation.getMethod()).thenReturn(Object.class.getMethod("methodToMock", int.class));
 VerificationMode verificationMode = verify(mock2, times(1)).methodToMock(anyInt());

 // when
 Object result = mockHandler.handle(invocation);

 // then
 assertDoesNotThrow(() -> mockHandler.handle(invocation));
 }

 @Test
 public void testHandle_voidMethodStubbing() {
 // given
 MockitoTestMock mock = mock(MockitoTestMock.class);
 MockHandler<MockitoTestMock> mockHandler = new MockHandler<>(mock);
 VoidMethodStubbable<MockitoTestMock> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);

 // when
 voidMethodStubbable.stubVoid("methodToStub");
 Invocation invocation = mock(InvocationOnMock.class);
 when(invocation.getMock()).thenReturn(mock);
 when(invocation.getMethod()).thenReturn(Object.class.getMethod("methodToStub"));

 // then
 verify(mockHandler).setAnswersForStubbing(Collections.singletonList(ArgumentMatchers.<Answer>any()));
 verifyNoMoreInteractions(mockHandler); // to ensure no unintended interaction
 }
}

class MockitoTestMock {
 // empty class for testing purposes
}