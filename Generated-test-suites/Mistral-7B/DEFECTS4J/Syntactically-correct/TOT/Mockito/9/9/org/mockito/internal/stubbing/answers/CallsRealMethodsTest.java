
package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InvocationOnMock;

public class CallsRealMethodsTest {

    private CallsRealMethods callsRealMethods;

    @Before
    public void setup() {
        callsRealMethods = new CallsRealMethods();
    }

    @Test
    public void testCallRealMethod_WhenMethodIsCalled_ReturnsValueFromRealMethod() throws Throwable {
        Object mockObject = mock(ClassWithRealMethod.class);
        Object expectedReturnValue = new Object();
        doReturn(expectedReturnValue).when(mockObject).realMethod();

        Object result = callsRealMethods.answer(new InvocationOnMock(mockObject, "realMethod", new Object[0]));

        verify(mockObject).realMethod();
        assertEquals(expectedReturnValue, result);
    }

    @Test
    public void testCallRealMethod_WhenMethodIsNotMocked_ReturnsValueFromRealMethod() {
        Object result = callsRealMethods.answer(new InvocationOnMock(new Object(), "realMethod", new Object[0]));

        Mockito.verify(any(Object.class), eq("realMethod")).callRealMethod();

        Object expectedValue = new Object();
        assertEquals(expectedValue, result);
    }

    @Test
    public void testCallRealMethod_WhenExceptionIsThrown_ThrowsTheSameException() throws Exception {
        Object mockObject = mock(ClassWithExceptionMethod.class);
        doThrow(new Exception("Test exception")).when(mockObject).exceptionMethod();

        InvocationOnMock invocation = new InvocationOnMock(mockObject, "exceptionMethod", new Object[0]);
        try {
            callsRealMethods.answer(invocation);
            fail("Expected exception not thrown");
        } catch (Exception e) {
            assertEquals(expectedException, e);
        }
    }

    @Test
    public void testCallRealMethod_WhenMultipleMethodCalls_CallsRealMethodsInOrder() throws Throwable {
        Object mockObject = mock(ClassWithMultipleMethods.class);

        // Given
        Object expectedFirstCallResult = "first result";
        doReturn(expectedFirstCallResult).when(mockObject).method1();
        Object expectedSecondCallResult = "second result";
        doReturn(expectedSecondCallResult).when(mockObject).method2();

        // When
        Object firstCallResult = callsRealMethods.answer(new InvocationOnMock(mockObject, "method1", new Object[0]));
        Object secondCallResult = callsRealMethods.answer(new InvocationOnMock(mockObject, "method2", new Object[0]));

        // Then
        verify(mockObject).method1();
        verifyNoMoreInteractions(mockObject);
        assertEquals(expectedFirstCallResult, firstCallResult);
        assertEquals(expectedSecondCallResult, secondCallResult);
    }
}

class ClassWithRealMethod {
    public Object realMethod() {
        return new Object();
    }
}

class ClassWithExceptionMethod {
    public void exceptionMethod() throws Exception {
        throw new Exception("Test exception");
    }
}

class ClassWithMultipleMethods {
    public Object method1() {
        return "first result";
    }

    public Object method2() {
        return "second result";
    }
}
