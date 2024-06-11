package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.invocation;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class CallsRealMethodsTest {

    @Test
    public void testCallsRealMethod() {
        Object target = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testClass = new TestClass();

        // Test callRealMethod on a method that returns a primitive type
        int result = 0;
        when(target.method1()).then(answer);
        assertEquals(testClass.method1(), result = target.method1());

        // Test callRealMethod on a method that returns an object
        String resultStr = "";
        when(target.method2()).then(answer);
        assertEquals(testClass.method2(), resultStr = target.method2());

        // Test callRealMethod on a method that returns null
        assertNull(when(target.method3()).thenReturn(answer).thenReturn(answer).thenReturn(answer).thenCallRealMethod().get());

        // Test callRealMethod on a method that throws an exception
        try {
            when(target.method4()).then(answer);
            target.method4();
            fail("Expected exception not thrown");
        } catch (Exception e) {
            assertEquals(TestClass.EXCEPTION_CLASS, e.getClass());
        }
    }

    @Test
    public void testCallRealMethodWithArguments() {
        Object target = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testClass = new TestClass();

        // Test callRealMethod on a method with arguments
        Object[] arguments = new Object[]{1, "two"};
        when(target.method5(anyInt(), eq("two"))).then(answer);
        assertEquals(testClass.method5(1, "two"), target.method5(arguments[0], arguments[1]));
    }

    private static class TestClass {
        public int method1() {
            return 42;
        }

        public String method2() {
            return "forty-two";
        }

        public Object method3() {
            return null;
        }

        public void method4() throws Exception {
            throw new Exception(EXCEPTION_MESSAGE);
        }

        public int method5(int arg1, String arg2) {
            return arg1 + arg2.length();
        }

        private static final String EXCEPTION_MESSAGE = "An error occurred";
        private static final Class<RuntimeException> EXCEPTION_CLASS = RuntimeException.class;
    }
}