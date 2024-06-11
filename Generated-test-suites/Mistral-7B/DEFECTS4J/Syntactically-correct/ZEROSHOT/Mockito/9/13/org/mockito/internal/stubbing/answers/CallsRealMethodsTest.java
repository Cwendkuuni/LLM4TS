package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.Mockito;
import org.mockito.Runners;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(Runners.JUnit4.class)
public class CallsRealMethodsTest {

 private Object realObject;
 private CallsRealMethods callsRealMethods;

 @Before
 public void setUp() {
 realObject = mock(Object.class);
 callsRealMethods = new CallsRealMethods();
 }

 @Test
 public void testCallRealMethod() {
 Object result = callsRealMethods.answer(Mockito.argumentCaptor(InvocationOnMock.class).capture(invocation));

 verify(realObject).callRealMethod();
 assertSame(result, invocation.getMethod().invoke(realObject));
 }

 @Test
 public void testAnswerWithNull() {
 ArgumentCaptor<InvocationOnMock> invocationCaptor = ArgumentCaptor.forClass(InvocationOnMock.class);
 callsRealMethods.answer(invocationCaptor.capture(invocation));

 verify(realObject).callRealMethod();
 assertNull(invocation.getArguments()[0]);
 }

 @Test(expected = Throwable.class)
 public void testAnswerWithThrowable() {
 ArgumentCaptor<InvocationOnMock> invocationCaptor = ArgumentCaptor.forClass(InvocationOnMock.class);
 doThrow(new RuntimeException("Test Exception")).when(realObject).callRealMethod();
 callsRealMethods.answer(invocationCaptor.capture(invocation));

 callsRealMethods.answer(invocationCaptor.capture(invocation));

 verify(realObject).callRealMethod();
 verify(realObject).callRealMethod();

 throw invocation.getTarget();
 }
}