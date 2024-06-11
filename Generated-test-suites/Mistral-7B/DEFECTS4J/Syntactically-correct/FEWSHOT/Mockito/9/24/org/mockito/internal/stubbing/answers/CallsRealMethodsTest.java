package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CallsRealMethodsTest {

    @Test
    public void testCallRealMethod() {
        Object mockObject = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        when(mockObject.method1()).then(answer);

        mockObject.method1();
        verify(mockObject).method1();
    }

    @Test
    public void testCallRealMethodWithArguments() {
        Object mockObject = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        when(mockObject.method2(1, "string")).then(answer);

        mockObject.method2(1, "string");
        verify(mockObject).method2(1, "string");
    }

    @Test
    public void testCallRealMethodWithException() throws Exception {
        Object mockObject = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        when(mockObject.method3()).then(answer);

        try {
            mockObject.method3();
            fail("Expected exception");
        } catch (Exception e) {
            //expected exception
        }

        verify(mockObject).method3();
    }

    public static class TestClass {
        public void method1() {}

        public int method2(int i, String s) {
            return i + s.length();
        }

        public void method3() throws Exception {
            throw new Exception("Exception message");
        }
    }
}