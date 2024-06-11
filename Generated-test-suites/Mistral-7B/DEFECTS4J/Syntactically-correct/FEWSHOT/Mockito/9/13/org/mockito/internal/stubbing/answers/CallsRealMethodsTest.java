package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class CallsRealMethodsTest {

    @Test
    public void testCallRealMethod() {
        Object mock = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testClass = new TestClass();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setTarget(testClass);
        invocation.setMethod(TestClass.class.getMethod("methodA"), new Object[0]);

        Object result = answer.answer(invocation);
        assertEquals(testClass.methodA(), result);

        verify(testClass).methodA();
    }

    @Test
    public void testCallRealMethodWithArguments() {
        Object mock = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testClass = new TestClass();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setTarget(testClass);
        invocation.setMethod(TestClass.class.getMethod("methodB"), new Object[]{1, "test"});

        Object[] arguments = new Object[]{1, "test"};
        invocation.setArguments(arguments);

        Object result = answer.answer(invocation);
        assertEquals(testClass.methodB(1, "test"), result);

        verify(testClass).methodB(1, "test");
    }

    @Test
    public void testCallRealMethodWithReturnTypeVoid() {
        Object mock = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testClass = new TestClass();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setTarget(testClass);
        invocation.setMethod(TestClass.class.getMethod("methodC"), new Object[0]);

        answer.answer(invocation);

        verify(testClass).methodC();
        assertNull(invocation.getReturnValue());
    }

    @Test
    public void testCallRealMethodWithException() {
        Object mock = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testClass = new TestClass();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setTarget(testClass);
        invocation.setMethod(TestClass.class.getMethod("methodD"), new Object[0]);

        try {
            answer.answer(invocation);
        } catch (Exception e) {
            assertEquals(TestClass.class.getMethod("methodD").getExceptionTypes()[0], e.getClass());
        }

        verify(testClass).methodD();
    }

    public static class TestClass {
        public int methodA() {
            return 42;
        }

        public String methodB(int i, String s) {
            return "result";
        }

        public void methodC() {
            // empty method
        }

        public void methodD() throws Exception {
            throw new Exception("test exception");
        }
    }
}