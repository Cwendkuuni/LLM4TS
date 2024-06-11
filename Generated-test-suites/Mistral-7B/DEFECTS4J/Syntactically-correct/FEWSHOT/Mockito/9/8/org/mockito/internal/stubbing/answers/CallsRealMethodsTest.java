package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.mockito.InvocationOnMock.callRealMethod;
import static org.mockito.InvocationOnMock.invocationWith;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class CallsRealMethodsTest {

    @Test
    public void testCallRealMethod() {
        Object object = mock(TestClass.class);
        TestClass testClass = (TestClass) object;

        String result = testClass.method1();
        assertEquals("Method 1 result from real implementation", result, callRealMethod(testClass, invocationWith(null)));

        result = testClass.method2("arg1", "arg2");
        assertEquals("Method 2 result from real implementation with args arg1, arg2", result, callRealMethod(testClass, invocationWith(new Object[]{"arg1", "arg2"})));
    }

    private static interface TestClass {
        String method1();
        String method2(String arg1, String arg2);
    }
}