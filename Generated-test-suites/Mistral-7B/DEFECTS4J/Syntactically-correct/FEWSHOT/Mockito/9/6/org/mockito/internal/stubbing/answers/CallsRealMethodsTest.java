package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class CallsRealMethodsTest {

    @Test
    public void testCallsRealMethod() {
        Object mockObject = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testClass = new TestClass() {
            public int method1() {
                return 42;
            }
        };

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setTarget(testClass);
        invocation.setMethod(TestClass.class.getMethod("method1"));

        Object result = answer.answer(invocation);
        assertEquals(42, result);

        verify(testClass).method1();
    }

    @Test
    public void testCallsRealMethodWithArguments() {
        Object mockObject = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testClass = new TestClass() {
            public int method1(int arg1, String arg2) {
                return arg1 + arg2.length();
            }
        };

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setTarget(testClass);
        invocation.setMethod(TestClass.class.getMethod("method1", int.class, String.class));
        invocation.setArguments(new Object[]{1, "test"});

        Object result = answer.answer(invocation);
        assertEquals(5, result);

        verify(testClass).method1(1, "test");
    }

    @Test(expected = Throwable.class)
    public void testCallsRealMethodWithThrowable() {
        Object mockObject = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testClass = new TestClass() {
            public void method1() throws Exception {
                throw new Exception("Test exception");
            }
        };

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setTarget(testClass);
        invocation.setMethod(TestClass.class.getMethod("method1"));

        answer.answer(invocation);
    }

    private static class TestClass {
        public int method1() {
            return 42;
        }

        public int method1(int arg1, String arg2) {
            return arg1 + arg2.length();
        }

        public void method1() throws Exception {
            throw new Exception("Test exception");
        }
    }
}