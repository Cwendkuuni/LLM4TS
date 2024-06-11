package org.example.junit.test.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class ReturnsSmartNullsTest {

    @Test
    public void testReturnsSmartNullsConstructor() {
        ReturnsSmartNulls smartNulls = mock(ReturnsSmartNulls.class);
        // No need to assert anything as the constructor does not take any arguments
    }

    @Test
    public void testConstructorCallsVerifyNoMoreInteractions() {
        ReturnsSmartNulls smartNulls = mock(ReturnsSmartNulls.class);
        verifyNoMoreInteractions(smartNulls);
    }

    @Test
    public void testAnswerMethodWithStringReturnType() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        when(returnsSmartNulls.answer(any(InvocationOnMock.class))).thenReturn("Test String");

        String result = (String) returnsSmartNulls.answer(mock(InvocationOnMock.class));
        assertEquals("Test String", result);
    }

    @Test
    public void testAnswerMethodWithObjectReturnType() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        when(returnsSmartNulls.answer(any(InvocationOnMock.class))).thenReturn(new Object());

        Object result = returnsSmartNulls.answer(mock(InvocationOnMock.class));
        assertNotNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testAnswerMethodWithNullInvocation() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);

        returnsSmartNulls.answer(null);
    }

    @Test
    public void testIsToStringMethodWithStringReturnType() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Method method = ReflectionUtils.methodWithName(ReturnsSmartNulls.class, "formatMethodCall");

        boolean result = returnsSmartNulls.new ObjectMethodsGuru().isToString(method);
        assertTrue(result);
    }

    @Test
    public void testIsToStringMethodWithNonStringReturnType() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Method method = ReflectionUtils.methodWithName(ReturnsSmartNulls.class, "answer");

        boolean result = returnsSmartNulls.new ObjectMethodsGuru().isToString(method);
        assertFalse(result);
    }

    @Test
    public void testSmartNullPointerExceptionMethod() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Location location = mock(Location.class);

        new Reporter().smartNullPointerException(location);

        verify(location).toString();
    }

    @Test
    public void testImposteriseMethodWithThrowingInterceptor() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        MethodInterceptor throwingInterceptor = mock(MethodInterceptor.class);
        Class<?> type = Mockito.class;

        when(returnsSmartNulls.delegate.answer(any(InvocationOnMock.class))).thenReturn(throwingInterceptor);

        Object result = returnsSmartNulls.answer(mock(InvocationOnMock.class));

        verify(throwingInterceptor).intercept(any(Object.class), any(Method.class), anyArray(), any(MethodProxy.class));
        assertEquals("SmartNull returned by unstubbed method on mock", result.toString());
    }

    @Test
    public void testCanImposteriseMethodWithMockableClass() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Class<?> mockableClass = Mockito.class;

        assertTrue(ClassImposterizer.INSTANCE.canImposterise(mockableClass));
    }

    @Test
    public void testCanImposteriseMethodWithNonMockableClass() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        Class<?> nonMockableClass = String.class;

        assertFalse(ClassImposterizer.INSTANCE.canImposterise(nonMockableClass));
    }
}