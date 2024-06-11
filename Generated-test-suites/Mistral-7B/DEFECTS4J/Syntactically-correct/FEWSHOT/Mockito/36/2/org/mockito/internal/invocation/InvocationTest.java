package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.internal.matchers.ArgumentsMatchers.*;
import static org.mockito.internal.reporting.PrintSettings.printSettings;

import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.invocation.InvocationOnMock;

import java.util.Arrays;

/**
 * Test class for Invocation.
 */
public class InvocationTest {

    @Test
    public void testConstructor() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new MockitoMethodImpl(), new Class[0]);
        Object[] arguments = new Object[0];
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod(mockitoMethod, mock));
        assertEquals(mock, invocation.getMock());
        assertEquals(mockitoMethod, invocation.getMethod());
        assertArrayEquals(arguments, invocation.getArguments());
        assertEquals(1, invocation.getSequenceNumber());
    }

    @Test
    public void testExpandVarArgs() {
        Object[] args = new Object[]{new Object[]{1, 2}, new Object[]{"foo", "bar"}};
        Object[] expandedArgs = Invocation.expandVarArgs(true, args);
        assertEquals(args.length, expandedArgs.length);
        assertEquals(args[0].getClass(), expandedArgs[0].getClass());
        assertEquals(args[1].getClass(), expandedArgs[args[0].length].getClass());
    }

    @Test
    public void testGetMock() {
        Object mock = new Object();
        Invocation invocation = new Invocation(mock, null, null, 1, null);
        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        MockitoMethod mockitoMethod = new MockitoMethod(new MockitoMethodImpl(), new Class[0]);
        Invocation invocation = new Invocation(null, mockitoMethod, null, 1, null);
        assertEquals(mockitoMethod, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        Object[] arguments = new Object[]{1, 2};
        Invocation invocation = new Invocation(null, null, arguments, 1, null);
        assertArrayEquals(arguments, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        Invocation invocation = new Invocation(null, null, null, 1, null);
        assertFalse(invocation.isVerified());

        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Invocation invocation = new Invocation(null, null, null, 1, null);
        assertFalse(invocation.isVerifiedInOrder());

        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerifiedInOrder());
    }

    @Test
    public void testToString() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new MockitoMethodImpl(), new Class[0]);
        Object[] arguments = new Object[]{1, 2};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, null);

        List<Matcher> matchers = invocation.argumentsToMatchers();
        String expected = String.format("%s(%s)", mockitoMethod.getName(), Arrays.toString(arguments));
        PrintSettings printSettings = printSettings();
        assertEquals(expected, invocation.toString(matchers, printSettings));
    }

    @Test
    public void testToStringWithMultiline() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new MockitoMethodImpl(), new Class[0]);
        Object[] arguments = new Object[]{new Object[3], new Object[]{1, 2, new Object[3][3]}};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, null);

        List<Matcher> matchers = invocation.argumentsToMatchers();
        String expected = String.format("%s(%s)%n%s%n", mockitoMethod.getName(), Arrays.toString(arguments),
                "Arguments:\n" +
                        "- an array:\n  [1, 2, ...]\n" +
                        "- an array of arrays:\n  [[\n    [1, 2, ...],\n    [...],\n    [...]\n  ]]");
        PrintSettings printSettings = printSettings();
        printSettings.setMultiline(true);
        assertEquals(expected, invocation.toString(matchers, printSettings));
    }

    @Test
    public void testIsToString() {
        MockitoMethod mockitoMethod = new MockitoMethod(new MockitoMethodImpl(), new Class[0]);
        assertTrue(Invocation.isToString(new Invocation(null, mockitoMethod, null, 1, null)));
        assertFalse(Invocation.isToString(new Invocation(null, mockitoMethod, new Object[0], 1, null)));
    }

    @Test
    public void testIsValidException() {
        MockitoMethod mockitoMethod = new MockitoMethod(new MockitoMethodImpl(), new Class[]{Throwable.class});
        Invocation invocation = new Invocation(null, mockitoMethod, null, 1, null);
        assertTrue(invocation.isValidException(new Throwable()));

        mockitoMethod = new MockitoMethod(new MockitoMethodImpl(), new Class[0]);
        assertFalse(invocation.isValidException(new Throwable()));
    }

    @Test
    public void testIsValidReturnType() {
        Invocation invocation = new Invocation(null, null, null, 1, null);
        assertTrue(invocation.isValidReturnType(int.class));
        assertFalse(invocation.isValidReturnType(String.class));
    }

    @Test
    public void testIsVoid() {
        MockitoMethod mockitoMethod = new MockitoMethod(new MockitoMethodImpl(), new Class[0], Void.TYPE);
        Invocation invocation = new Invocation(null, mockitoMethod, null, 1, null);
        assertTrue(invocation.isVoid());

        mockitoMethod = new MockitoMethod(new MockitoMethodImpl(), new Class[0]);
        assertFalse(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        MockitoMethod mockitoMethod = new MockitoMethod(new MockitoMethodImpl(), new Class[]{Integer.class}, Integer.class);
        Invocation invocation = new Invocation(null, mockitoMethod, null, 1, null);
        assertEquals("Integer", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        MockitoMethod mockitoMethod = new MockitoMethod(new MockitoMethodImpl(), new Class[0]);
        Invocation invocation = new Invocation(null, mockitoMethod, null, 1, null);
        assertEquals(mockitoMethod.getName(), invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        MockitoMethod mockitoMethod = new MockitoMethod(new MockitoMethodImpl(), new Class[]{boolean.class});
        Invocation invocation = new Invocation(null, mockitoMethod, null, 1, null);
        assertTrue(invocation.returnsPrimitive());

        mockitoMethod = new MockitoMethod(new MockitoMethodImpl(), new Class[]{Integer.class});
        assertFalse(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(null, null, null, 1, null);
        assertNull(invocation.getLocation());

        Location location = new Location();
        invocation = new Invocation(null, null, null, 1, null);
        invocation.setLocation(location);
        assertEquals(location, invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        Invocation invocation = new Invocation(null, null, new Object[0], 1, null);
        assertEquals(0, invocation.getArgumentsCount());

        Object[] arguments = new Object[]{1, 2};
        invocation = new Invocation(null, null, arguments, 1, null);
        assertEquals(arguments.length, invocation.getArgumentsCount());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        MockitoMethod mockitoMethod = new MockitoMethod(new MockitoMethodImpl(), new Class[0], Integer.class);
        Object mock = new Object() {
            public int method() {
                return 42;
            }
        };
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, null);
        assertEquals(42, invocation.callRealMethod());
    }

    @Test(expected = RuntimeException.class)
    public void testHashCode() {
        Invocation invocation = new Invocation(null, null, null, 1, null);
        invocation.hashCode();
    }
}