package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_NullReturnType() {
        Object mockedObject = mock(Object.class, new ReturnsSmartNulls());
        assertNull(mockedObject.answer(new InvocationOnMock(null, null, new Object[0], new Method[0])));
    }

    @Test
    public void testAnswer_NonMockableReturnType() {
        Object mockedObject = mock(Object.class, new ReturnsSmartNulls());
        assertNull(mockedObject.answer(new InvocationOnMock(new Object() {
            public int getInt() {
                return 0;
            }
        }, new Method(new Class[]{}, "getInt"), new Object[0])));
    }

    @Test
    public void testAnswer_MockableReturnType_EmptyReturn() {
        Object mockedObject = mock(Object.class, new ReturnsSmartNulls());
        when(mockedObject.answer(new InvocationOnMock(spy(new Object() {}), new Method(new Class[]{}, "getEmptyArray"), new Object[0]))).thenReturn(new Object[0]);
        assertArrayEquals(new Object[0], mockedObject.answer(new InvocationOnMock(spy(new Object() {}), new Method(new Class[]{}, "getEmptyArray"), new Object[0])));
    }

    @Test
    public void testAnswer_MockableReturnType_NonEmptyReturn() {
        Object mockedObject = mock(Object.class, new ReturnsSmartNulls());
        when(mockedObject.answer(new InvocationOnMock(spy(new Object() {
            public String getString() {
                return "foo";
            }
        }), new Method(new Class[]{}, "getString"), new Object[0]))).thenReturn("foo");
        assertEquals("foo", mockedObject.answer(new InvocationOnMock(spy(new Object() {}), new Method(new Class[]{}, "getString"), new Object[0])));
    }

    @Test
    public void testAnswer_MockableReturnType_SmartNull() {
        Object mockedObject = mock(Object.class, new ReturnsSmartNulls());
        when(mockedObject.answer(new InvocationOnMock(spy(new Object() {}), new Method(new Class[]{}, "getSmartNull"), new Object[0]))).thenReturn(null);
        verify(mockedObject).throwException(new org.mockito.exceptions.SmartNullPointerException(new Location(1, 1, "getSmartNull")));
    }

    @Test
    public void testAnswer_MockableReturnType_ToStringMethod() {
        Object mockedObject = mock(Object.class, new ReturnsSmartNulls());
        when(mockedObject.answer(new InvocationOnMock(spy(new Object() {}), new Method(new Class[]{}, "toString"), new Object[0]))).thenReturn("SmartNull returned by unstubbed toString method on mock");
        assertEquals("SmartNull returned by unstubbed toString method on mock", mockedObject.answer(new InvocationOnMock(spy(new Object() {}), new Method(new Class[]{}, "toString"), new Object[0])));
    }
}