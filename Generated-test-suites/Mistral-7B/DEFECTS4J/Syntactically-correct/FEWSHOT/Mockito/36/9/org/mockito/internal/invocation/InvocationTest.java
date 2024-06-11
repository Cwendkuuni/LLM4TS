package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.matchers.*;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.*;
import org.mockito.invocation.InvocationOnMock;

public class InvocationTest {

    @Test
    public void testIsToString() {
        Invocation invocation = mock(InvocationOnMock.class);
        assertTrue(Invocation.isToString(invocation));
    }

    @Test(expected = MockitoException.class)
    public void testIsToString_nonMockitoMethod() {
        Object mock = new Object();
        Method method = mock(Method.class);
        Invocation invocation = new Invocation(mock, null, new Object[0], 0, null);
        assertFalse(Invocation.isToString(invocation));
    }

    @Test
    public void testGetMock() {
        Object mock = new Object();
        Invocation invocation = new Invocation(mock, null, new Object[0], 0, null);
        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Invocation invocation = new Invocation(new Object(), mockitoMethod, new Object[0], 0, null);
        assertEquals(mockitoMethod, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        Object[] arguments = new Object[]{1, 2, 3};
        Invocation invocation = new Invocation(new Object(), null, arguments, 0, null);
        assertArrayEquals(arguments, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        Invocation invocation = new Invocation(new Object(), null, new Object[0], 0, null);
        assertFalse(invocation.isVerified());

        doAnswer(invocation1 -> invocation1.markVerified()).when(invocation).callRealMethod();
        invocation.callRealMethod();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Invocation invocation1 = new Invocation(new Object(), null, new Object[0], 0, null);
        Invocation invocation2 = new Invocation(new Object(), null, new Object[0], 1, null);

        invocation1.markVerifiedInOrder();
        assertFalse(invocation2.isVerifiedInOrder());

        invocation2.markVerifiedInOrder();
        assertTrue(invocation1.isVerifiedInOrder());
        assertTrue(invocation2.isVerifiedInOrder());
    }

    @Test
    public void testToString() {
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[]{1, new String[]{"foo"}};
        Invocation invocation = new Invocation(new Object(), mockitoMethod, arguments, 0, null);

        assertEquals("Invocation [mock=MockForTest, method=MockMethod(foo(int, java.lang.String[]), org.mockito.internal.invocation.realmethod.RealMethod@1), arguments=[1, [foo]], sequenceNumber=0]",
                invocation.toString());
    }

    @Test
    public void testEquals() {
        Object mock = new Object();
        MockitoMethod mockitoMethod1 = mock(MockitoMethod.class);
        MockitoMethod mockitoMethod2 = mock(MockitoMethod.class);
        Object[] arguments1 = new Object[]{1};
        Object[] arguments2 = new Object[]{1};
        Object[] arguments3 = new Object[]{2};

        Invocation invocation1 = new Invocation(mock, mockitoMethod1, arguments1, 0, null);
        Invocation invocation2 = new Invocation(mock, mockitoMethod1, arguments1, 0, null);
        Invocation invocation3 = new Invocation(mock, mockitoMethod2, arguments1, 0, null);
        Invocation invocation4 = new Invocation(mock, mockitoMethod1, arguments2, 0, null);
        Invocation invocation5 = new Invocation(mock, mockitoMethod1, arguments3, 0, null);

        assertTrue(invocation1.equals(invocation1));
        assertTrue(invocation1.equals(invocation2));
        assertFalse(invocation1.equals(invocation3));
        assertFalse(invocation1.equals(invocation4));
        assertFalse(invocation1.equals(invocation5));
        assertFalse(invocation1.equals(null));
        assertFalse(invocation1.equals("aString"));
    }

    @Test
    public void testIsValidException() {
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Invocation invocation = new Invocation(new Object(), mockitoMethod, new Object[0], 0, null);

        assertFalse(invocation.isValidException(new IllegalArgumentException()));
        assertFalse(invocation.isValidException(new RuntimeException()));
        assertTrue(invocation.isValidException(new Exception()));
    }

    @Test
    public void testIsValidReturnType() {
        Invocation invocation = new Invocation(new Object(), null, new Object[0], 0, null);

        assertFalse(invocation.isValidReturnType(int.class));
        assertFalse(invocation.isValidReturnType(String.class));
        assertTrue(invocation.isValidReturnType(Integer.class));
        assertTrue(invocation.isValidReturnType(List.class));
        assertTrue(invocation.isValidReturnType(Void.TYPE));
    }

    @Test
    public void testIsVoid() {
        Invocation invocation = new Invocation(new Object(), null, new Object[0], 0, null);
        assertFalse(invocation.isVoid());

        MockitoMethod mockitoMethod = mock(MockitoMethod.class, ReflectionUtils.withReturnType(Void.TYPE));
        Invocation voidInvocation = new Invocation(new Object(), mockitoMethod, new Object[0], 0, null);
        assertTrue(voidInvocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Invocation invocation = new Invocation(new Object(), null, new Object[0], 0, null);
        assertEquals("", invocation.printMethodReturnType());

        MockitoMethod mockitoMethod = mock(MockitoMethod.class, ReflectionUtils.withReturnType(int.class));
        Invocation intInvocation = new Invocation(new Object(), mockitoMethod, new Object[0], 0, null);
        assertEquals("int", intInvocation.printMethodReturnType());

        MockitoMethod mockitoMethodString = mock(MockitoMethod.class, ReflectionUtils.withReturnType(String.class));
        Invocation stringInvocation = new Invocation(new Object(), mockitoMethodString, new Object[0], 0, null);
        assertEquals("String", stringInvocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Invocation invocation = new Invocation(new Object(), mockitoMethod, new Object[0], 0, null);
        assertEquals("", invocation.getMethodName());

        MockitoMethod mockitoMethodWithName = mockitoMethodWithName("foo");
        Invocation invocationWithName = new Invocation(new Object(), mockitoMethodWithName, new Object[0], 0, null);
        assertEquals("foo", invocationWithName.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        Invocation invocation = new Invocation(new Object(), null, new Object[0], 0, null);
        assertFalse(invocation.returnsPrimitive());

        MockitoMethod mockitoMethodWithBooleanReturnType = mockitoMethodWithReturnType(boolean.class);
        Invocation invocationWithBooleanReturnType = new Invocation(new Object(), mockitoMethodWithBooleanReturnType, new Object[0], 0, null);
        assertTrue(invocationWithBooleanReturnType.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(new Object(), null, new Object[0], 0, null);
        assertNull(invocation.getLocation());

        Location location = new Location();
        Invocation invocationWithLocation = new Invocation(new Object(), null, new Object[0], 0, null);
        invocationWithLocation.setLocation(location);
        assertEquals(location, invocationWithLocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        Invocation invocation = new Invocation(new Object(), null, new Object[0], 0, null);
        assertEquals(0, invocation.getArgumentsCount());

        Object[] arguments = new Object[]{1, 2, 3};
        Invocation invocationWithArguments = new Invocation(new Object(), null, arguments, 0, null);
        assertEquals(3, invocationWithArguments.getArgumentsCount());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object mock = new Object();
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[]{1};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 0, null);

        assertEquals(1, invocation.callRealMethod());
    }

    private MockitoMethod mockitoMethodWithName(String name) {
        return mock(MockitoMethod.class, ReflectionUtils.withName(name));
    }

    private MockitoMethod mockitoMethodWithReturnType(Class<?> clazz) {
        return mock(MockitoMethod.class, ReflectionUtils.withReturnType(clazz));
    }
}