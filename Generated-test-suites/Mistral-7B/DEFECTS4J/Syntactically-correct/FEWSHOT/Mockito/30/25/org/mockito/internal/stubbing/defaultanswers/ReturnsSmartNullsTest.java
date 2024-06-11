package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls;
    private Answer<Object> returnsMoreEmptyValues;
    private ObjectMethodsGuru objectMethodsGuru;
    private Reporter reporter;
    private Location location;

    @Before
    public void setup() {
        returnsSmartNulls = new ReturnsSmartNulls();
        returnsMoreEmptyValues = mock(Answer.class);
        objectMethodsGuru = spy(new ObjectMethodsGuru());
        reporter = mock(Reporter.class);
        location = new Location();
    }

    @Test
    public void testAnswerNull() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        assertEquals(null, returnsSmartNulls.answer(invocation));
        verify(reporter).smartNullPointerException(location);
    }

    @Test
    public void testAnswerNotNull() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(returnsMoreEmptyValues.answer(invocation)).thenReturn("foo");
        when(objectMethodsGuru.isToString(anyMethod())).thenReturn(true);

        Object result = returnsSmartNulls.answer(invocation);
        assertEquals("SmartNull returned by unstubbed method on mock", result.toString());
    }

    @Test
    public void testAnswerNotNullWithThrowingInterceptor() throws Exception {
        Class<?> stringClass = String.class;
        Method toStringMethod = stringClass.getMethod("toString");
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(toStringMethod);
        when(objectMethodsGuru.isToString(anyMethod())).thenReturn(true);

        ClassImposterizer classImposterizer = mock(ClassImposterizer.class);
        when(ClassImposterizer.INSTANCE).thenReturn(classImposterizer);
        when(classImposterizer.imposterise(any(), eq(String.class))).thenReturn("SmartNull");

        Object result = returnsSmartNulls.answer(invocation);
        assertEquals("SmartNull", result);
    }

    @Test
    public void testAnswerNullWithThrowingInterceptor() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Class<?> nonMockableClass = int[].class;

        when(invocation.getMethod().getReturnType()).thenReturn(nonMockableClass);
        when(ClassImposterizer.INSTANCE.canImposterise(nonMockableClass)).thenReturn(false);

        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testThrowingInterceptorToString() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        String expected = "SmartNull returned by unstubbed method on mock";
        when(objectMethodsGuru.isToString(anyMethod())).thenReturn(true);

        ReturnsSmartNulls returnsSmartNullsSpy = spy(returnsSmartNulls);
        Object result = returnsSmartNullsSpy.answer(invocation);
        assertEquals(expected, result.toString());
    }

    @Test
    public void testThrowingInterceptorNotToString() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(objectMethodsGuru.isToString(anyMethod())).thenReturn(false);

        ReturnsSmartNulls returnsSmartNullsSpy = spy(returnsSmartNulls);
        Object result = returnsSmartNullsSpy.answer(invocation);
        assertNull(result);
    }

    private Method anyMethod() {
        return any(Method.class);
    }
}