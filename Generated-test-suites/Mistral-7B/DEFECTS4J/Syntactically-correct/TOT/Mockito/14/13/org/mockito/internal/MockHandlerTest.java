import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMockMethodInterceptor;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<MyMock> mockHandler;
 private MyMock mock;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 mock = mock(MyMock.class);
 mockHandler = new MockHandler<>(mock);
 }

 @Test
 public void testHandle_StubbingVoidMethod() {
 AtomicInteger counter = new AtomicInteger(0);
 Answer<Void> answer = invocation -> {
 counter.incrementAndGet();
 return null;
 };
 stub(mockHandler.getClass()).method("setAnswersForStubbing", ArgumentMatchers.isA(List.class)).andReturn(null);
 when(mockHandler.handle(ArgumentMatchers.any(InvocationOnMock.class))).thenAnswer(answer);

 mockHandler.voidMethodStubbable(mock).stubVoid("methodToStubWithVoidReturnType");

 MyMock spy = spy(mock);
 MyMock otherMock = mock(MyMock.class);

 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 InvocationOnMockMethodInterceptor interceptor = new InvocationOnMockMethodInterceptor(invocation);

 try {
 invocation.invokeMethod(mockHandler, "handle", Arrays.asList(invocation), null);
 } finally {
 Thread.currentThread().setContextClassLoader(Thread.currentThread().getContextClassLoader());
 }

 verify(mock).methodToStubWithVoidReturnType();
 verifyNoMoreInteractions(otherMock);
 verify(mockHandler).setAnswersForStubbing(Arrays.asList(answer));
 verify(mockHandler, never()).handle(ArgumentMatchers.any(InvocationOnMock.class));

 // Check that stubbing was done only once
 verify(mockHandler, times(1)).setAnswersForStubbing(Arrays.asList(answer));

 // Check that stubbing did not affect other mocks
 verify(otherMock, never()).methodToStubWithVoidReturnType();

 // Check that counter was incremented
 assertEquals(1, counter.get());
 }

 @Test
 public void testHandle_Verification() {
 MyMock spy = spy(mock);
 MyMock otherMock = mock(MyMock.class);

 when(spy.methodToVerify()).thenReturn("returnValue");
 when(otherMock.methodToVerify()).thenReturn("otherReturnValue");
 when(mockHandler.handle(ArgumentMatchers.any(InvocationOnMock.class))).thenAnswer(invocation -> invocation.getMethod().invoke(spy, invocation.getArgs()));

 verify(spy).methodToVerify();
 verify(otherMock).methodToVerify();

 // Check that verification was done only on the correct mock
 verify(spy, times(1)).methodToVerify();
 verify(otherMock, never()).methodToVerify();
 }

 @Test
 public void testHandle_PartialMock() {
 MyMock partialMock = spy(mock);
 mockito3Mockito4Proxy(partialMock);

 when(partialMock.methodToVerify()).thenThrow(new RuntimeException());
 when(mockHandler.handle(ArgumentMatchers.any(InvocationOnMock.class))).thenAnswer(invocation -> invocation.getMethod().invoke(partialMock, invocation.getArgs()));

 try {
 partialMock.methodToVerify();
 fail();
 } catch (RuntimeException e) {
 // expected
 }

 // Reset invocation for potential stubbing in case of partial mocks / spies
 mockHandler.invocationContainerImpl.resetInvocationForPotentialStubbing(eq(ArgumentMatchers.any(InvocationMatcher.class)));

 // Check that verification was not affected by the exception
 verify(partialMock, times(1)).methodToVerify();
 }

 @Test
 public void testHandle_DefaultAnswer() {
 MyMock mock = mock(MyMock.class);
 when(mockHandler.handle(ArgumentMatchers.any(InvocationOnMock.class))).thenAnswer(invocation -> invocation.getMethod().invoke(mock, invocation.getArgs()));

 assertEquals("defaultReturnValue", mock.methodToReturnDefaultValue());
 }
}

 class MyMock {
 String methodToVerify();
 String methodToReturnDefaultValue();
 }