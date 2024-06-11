package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.InvocationOnMock;

public class CallsRealMethodsTest {

    @Test
    public void testCallsRealMethods() {
        Object mockObject = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testClass = new TestClass();
        TestClass mockTestClass = mockObject;

        // Calling a method on the mock object that is not stubbed should call the real method
        Object result = answer.answer(new InvocationOnMock(testClass, "method1", new Object[0]));
        assertEquals("Real method return value", result);
        verify(testClass).method1();

        // Calling a method on the mock object that is stubbed should use the stubbed return value
        when(mockTestClass.method2()).thenReturn("Stubbed return value");
        result = answer.answer(new InvocationOnMock(mockTestClass, "method2", new Object[0]));
        assertEquals("Stubbed return value", result);
    }

    static class TestClass {
        public String method1() {
            return "Real method return value";
        }

        public String method2() {
            return "Another real method return value";
        }
    }
}