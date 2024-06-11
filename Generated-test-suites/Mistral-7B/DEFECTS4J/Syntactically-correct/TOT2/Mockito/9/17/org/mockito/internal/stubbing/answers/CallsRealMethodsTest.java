package org.mockito.internal.stubbing.answers.test;

import org.junit.*;
import org.mockito.*;

public class CallsRealMethodsTest {

@Test
public void testCallsRealMethodsConstructor() {
CallsRealMethods callsRealMethods = new CallsRealMethods();
}

@Test
public void testAnswerMethod(Mockito mck) throws Throwable {
CallsRealMethods callsRealMethods = mck.mock(CallsRealMethods.class);
InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
Object result = callsRealMethods.answer(invocation);
Assert.assertNotNull(result);
}

@Test
public void testAnswerMethodWithNullInvocation() {
CallsRealMethods callsRealMethods = new CallsRealMethods();
Assert.assertNull(callsRealMethods.answer(null));
}

@Test(expected = Throwable.class)
public void testAnswerMethodThrowsExceptionFromRealMethod(Mockito mck) throws Throwable {
CallsRealMethods callsRealMethods = mck.mock(CallsRealMethods.class);
InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
Mockito.doThrow(new RuntimeException("Test Exception")).when(invocation).callRealMethod();
Assert.assertThrows(RuntimeException.class, () -> callsRealMethods.answer(invocation));
}
}