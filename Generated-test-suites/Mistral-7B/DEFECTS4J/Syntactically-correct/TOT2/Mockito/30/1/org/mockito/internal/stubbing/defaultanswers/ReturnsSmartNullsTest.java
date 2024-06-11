package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.isA;
import org.mockito.exceptions.VerificationException;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerMethod() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(SomeClass.class);
        when(mock.someMethod()).thenAnswer(returnsSmartNulls);
        assertNull(returnsSmartNulls.answer(argThat(isA(InvocationOnMock.class))));
        verify(mock).someMethod();
    }

    @Test
    public void testSmartNullForUnstubbedMethods() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(SomeFinalClass.class);
        when(mock.someMethod()).thenCallRealMethod();
        assertNotNull(returnsSmartNulls.answer(argThat(isA(InvocationOnMock.class))));
        verifyZeroInteractions(mock);
    }

    @Test(expected = VerificationException.class)
    public void testToStringMethod() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(SomeClassWithToStringMethod.class);
        when(mock.someMethod()).thenAnswer(returnsSmartNulls);
        returnsSmartNulls.answer(argThat(isA(InvocationOnMock.class)));
    }

    @Test
    public void testExceptionHandling() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(SomeClassWithException.class);
        when(mock.someMethod()).thenThrow(new RuntimeException("Exception Occurred"));
        assertNotNull(returnsSmartNulls.answer(argThat(isA(InvocationOnMock.class))));
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testFormatMethodCall() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(SomeClassWithException.class);
        when(mock.someMethod()).thenThrow(new RuntimeException("Exception Occurred"));
        try {
            returnsSmartNulls.answer(argThat(isA(InvocationOnMock.class)));
        } catch (Throwable e) {
            String formattedMethodCall = returnsSmartNulls.formatMethodCall();
            assertEquals("someMethod(SomeClassWithException)", formattedMethodCall);
        }
    }
}