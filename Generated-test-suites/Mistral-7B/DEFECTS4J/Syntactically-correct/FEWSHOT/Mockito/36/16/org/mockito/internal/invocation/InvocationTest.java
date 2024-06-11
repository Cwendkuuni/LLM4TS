package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class InvocationTest {

    @Test
    public void testEquals() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{"arg1", "arg2"};
        Invocation invocation1 = new Invocation(mock, method, args, 1, null);
        Invocation invocation2 = new Invocation(mock, method, args, 1, null);

        assertEquals(invocation1, invocation2);
    }

    @Test
    public void testNotEqualsDifferentMock() {
        Object mock1 = new Object();
        Object mock2 = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{"arg1", "arg2"};

        Invocation invocation1 = new Invocation(mock1, method, args, 1, null);
        Invocation invocation2 = new Invocation(mock2, method, args, 1, null);

        assertNotEquals(invocation1, invocation2);
    }

    @Test
    public void testNotEqualsDifferentMethod() {
        Object mock = new Object();
        MockitoMethod method1 = mock(MockitoMethod.class);
        MockitoMethod method2 = mock(MockitoMethod.class);
        Object[] args = new Object[]{"arg1", "arg2"};

        Invocation invocation1 = new Invocation(mock, method1, args, 1, null);
        Invocation invocation2 = new Invocation(mock, method2, args, 1, null);

        assertNotEquals(invocation1, invocation2);
    }

    @Test
    public void testNotEqualsDifferentArguments() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args1 = new Object[]{"arg1", "arg2"};
        Object[] args2 = new Object[]{"arg1", "arg3"};

        Invocation invocation1 = new Invocation(mock, method, args1, 1, null);
        Invocation invocation2 = new Invocation(mock, method, args2, 1, null);

        assertNotEquals(invocation1, invocation2);
    }

    @Test
    public void testToString() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{"arg1", new Object[]{1, 2, 3}};

        Invocation invocation = new Invocation(mock, method, args, 1, null);

        String expected = "org.mockito.internal.invocation.Invocation@6d1c6c1d [1]"
                + "\nMockitoMockitoMethod{mock=org.mockito.internal.invocation.MockitoMethodSpy@34d6c48c,"
                + " methodName='testMethod', arguments=[arg1, [1, 2, 3]], invocationSite=MockitoInvocationSiteProxy@35852e3a}"
                + "\n@1";

        assertEquals(expected, invocation.toString());
    }

    @Test
    public void testToStringWithMatchers() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{"arg1", new Object[]{1, 2, 3}};

        Invocation invocation = new Invocation(mock, method, args, 1, null);

        List<Matcher> matchers = invocation.argumentsToMatchers();
        matchers.set(0, argThat(isA(String.class)));
        matchers.set(1, argThat(isA(Collection.class)).and(hasItem(isA(Integer.class))));

        String expected = "org.mockito.internal.invocation.Invocation@1c35522a [1]"
                + "\nMockitoMockitoMethod{mock=org.mockito.internal.invocation.MockitoMethodSpy@34d6c48c,"
                + " methodName='testMethod', arguments=[<arg1> <Collection> [1, 2, 3]], invocationSite=MockitoInvocationSiteProxy@35852e3a}"
                + "\n@1";

        assertEquals(expected, invocation.toString(new PrintSettings()));
    }

    @Test
    public void testIsToString() {
        MockitoMethod method = mock(MockitoMethod.class);
        assertTrue(Invocation.isToString(mock(InvocationOnMock.class), method));
    }

    @Test
    public void testIsNotToString() {
        MockitoMethod method = mock(MockitoMethod.class);
        MockitoMethod anotherMethod = mock(MockitoMethod.class);

        Invocation invocation = new Invocation(new Object(), method, new Object[0], 1, null);

        assertFalse(Invocation.isToString(invocation, anotherMethod));
    }

    @Test
    public void testIsValidException() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        Invocation invocation = new Invocation(mock, method, new Object[0], 1, null);

        RuntimeException exception = new RuntimeException("Exception message");
        assertTrue(invocation.isValidException(exception));
    }

    @Test
    public void testIsNotValidException() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        Invocation invocation = new Invocation(mock, method, new Object[0], 1, null);

        IllegalArgumentException exception = new IllegalArgumentException("Exception message");
        assertFalse(invocation.isValidException(exception));
    }

    @Test
    public void testIsValidReturnType() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);

        Invocation invocation = new Invocation(mock, method, new Object[0], 1, null);

        assertTrue(invocation.isValidReturnType(Integer.class));
        assertTrue(invocation.isValidReturnType(int.class));
        assertTrue(invocation.isValidReturnType(String.class));
        assertTrue(invocation.isValidReturnType(MockitoMethod.class));
        assertFalse(invocation.isValidReturnType(Float.class));
    }

    @Test
    public void testIsVoid() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        MockitoMethod voidMethod = mock(MockitoMethod.class, Void.TYPE);

        Invocation invocation1 = new Invocation(mock, method, new Object[0], 1, null);
        Invocation invocation2 = new Invocation(mock, voidMethod, new Object[0], 1, null);

        assertFalse(invocation1.isVoid());
        assertTrue(invocation2.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);

        Invocation invocation = new Invocation(mock, method, new Object[0], 1, null);

        assertEquals("Integer", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);

        Invocation invocation = new Invocation(mock, method, new Object[0], 1, null);

        assertEquals("testMethod", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);

        Invocation invocation = new Invocation(mock, method, new Object[0], 1, null);

        assertFalse(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Object mock = new Object();
        Location location = mock(Location.class);

        Invocation invocation = new Invocation(mock, null, new Object[0], 1, null);
        invocation.setLocation(location);

        assertEquals(location, invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{"arg1", "arg2"};

        Invocation invocation = new Invocation(mock, method, args, 1, null);

        assertEquals(2, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{"arg1", "arg2"};

        Invocation invocation = new Invocation(mock, method, args, 1, null);

        Object[] rawArgs = invocation.getRawArguments();

        assertArrayEquals(args, rawArgs);
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] rawArguments = new Object[]{"arg1"};

        Invocation invocation = new Invocation(mock, method, rawArguments, 1, null);

        assertEquals("result", invocation.callRealMethod());
    }
}