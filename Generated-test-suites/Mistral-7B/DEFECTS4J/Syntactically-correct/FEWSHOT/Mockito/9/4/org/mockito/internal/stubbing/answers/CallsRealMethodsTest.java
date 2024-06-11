package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class CallsRealMethodsTest {

    @Test
    public void testCallRealMethod() {
        Object target = mock(TestClass.class);
        CallsRealMethods callsRealMethods = new CallsRealMethods();

        TestClass testClass = new TestClass();
        TestClass spy = mock(TestClass.class, callRealMethod -> callsRealMethods);

        // Test callRealMethod on mocked object
        verify(target).method1();
        verify(target).method2("arg1", "arg2");
        verify(target).method3(1, 2, 3);

        // Test callRealMethod on spy
        assertEquals("result", spy.method4("arg1", "arg2"));
        verify(spy).method5(1, 2, 3);

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.getMethod().setAccessible(true);

        try {
            Object result = callsRealMethods.answer(invocation);
            assertEquals(invocation.getTarget(), result);
        } catch (Throwable e) {
            // Test exception handling
            assertEquals(e.getClass(), invocation.getOriginalMethod().getExceptionTypes()[0]);
        }
    }

    static class TestClass {
        public void method1() {
            // Some implementation
        }

        public void method2(String arg1, String arg2) {
            // Some implementation
        }

        public void method3(int arg1, int arg2, int arg3) {
            // Some implementation
        }

        public String method4(String arg1, String arg2) {
            return "result";
        }

        public void method5(int arg1, int arg2, int arg3) {
            // Some implementation
        }
    }
}