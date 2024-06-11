package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class CallsRealMethodsTest {

    @Test
    public void testCallsRealMethods() {
        FakeClass fakeClass = mock(FakeClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        final String result = "result";
        when(fakeClass.method1()).thenReturn(result);

        Object result1 = answer.answer(new InvocationOnMock(fakeClass, "method1", new Object[0]));
        assertEquals(result, result1);
        verify(fakeClass).method1();

        Object result2 = answer.answer(new InvocationOnMock(fakeClass, "method2", new Object[0]));
        assertNull(result2);
        verify(fakeClass).method2();
    }

    @Test
    public void testCallsSpyMethods() {
        FakeClass fakeClass = spy(FakeClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        final String result = "result";
        doReturn(result).when(fakeClass).method1();

        Object result1 = answer.answer(new InvocationOnMock(fakeClass, "method1", new Object[0]));
        assertEquals(result, result1);
        verify(fakeClass).method1();

        Object result2 = answer.answer(new InvocationOnMock(fakeClass, "method2", new Object[0]));
        assertNull(result2);
        verify(fakeClass).method2();
    }

    interface FakeClass {
        String method1();
        String method2();
    }
}