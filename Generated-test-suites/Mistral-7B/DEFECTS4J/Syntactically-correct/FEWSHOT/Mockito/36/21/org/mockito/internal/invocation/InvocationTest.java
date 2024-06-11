package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class InvocationTest {

    @Test
    public void testEquals() {
        Object mock1 = new Object();
        Object mock2 = new Object();
        MockitoMethod method = new MockitoMethod(Mockito.method(String.class, "foo", new Class[0]), new Location());
        Object[] arguments1 = new Object[]{"foo1"};
        Object[] arguments2 = new Object[]{"foo2"};

        Invocation invocation1 = new Invocation(mock1, method, arguments1, 1, null);
        Invocation invocation2 = new Invocation(mock2, method, arguments2, 2, null);
        Invocation invocation3 = new Invocation(mock1, method, arguments1, 1, null);

        assertTrue(invocation1.equals(invocation3));
        assertFalse(invocation1.equals(invocation2));
    }

    @Test
    public void testGetArgumentsCount() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(Mockito.method(String.class, "foo", new Class[0]), new Location());
        Object[] arguments = new Object[]{"arg1", "arg2"};

        Invocation invocation = new Invocation(mock, method, arguments, 1, null);
        assertEquals(2, invocation.getArgumentsCount());
    }

    @Test
    public void testGetMock() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(Mockito.method(String.class, "foo", new Class[0]), new Location());
        Object[] arguments = new Object[]{"arg1", "arg2"};

        Invocation invocation = new Invocation(mock, method, arguments, 1, null);
        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(Mockito.method(String.class, "foo", new Class[0]), new Location());
        Object[] arguments = new Object[]{"arg1", "arg2"};

        Invocation invocation = new Invocation(mock, method, arguments, 1, null);
        assertEquals(method, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(Mockito.method(String.class, "foo", new Class[0]), new Location());
        Object[] arguments = new Object[]{"arg1", "arg2"};

        Invocation invocation = new Invocation(mock, method, arguments, 1, null);
        assertArrayEquals(new Object[]{"arg1", "arg2"}, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(Mockito.method(String.class, "foo", new Class[0]), new Location());
        Object[] arguments = new Object[]{"arg1", "arg2"};

        Invocation invocation = new Invocation(mock, method, arguments, 1, null);
        assertFalse(invocation.isVerified());
    }

    @Test
    public void testMarkVerified() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(Mockito.method(String.class, "foo", new Class[0]), new Location());
        Object[] arguments = new Object[]{"arg1", "arg2"};

        Invocation invocation = new Invocation(mock, method, arguments, 1, null);
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testToString() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(Mockito.method(String.class, "foo", new Class[0]), new Location());
        Object[] arguments = new Object[]{"arg1", "arg2"};

        Invocation invocation = new Invocation(mock, method, arguments, 1, null);
        String expected = "invocation on mock [mockito-test] of method " + method.getName() + "(";
        for (int i = 0; i < arguments.length; i++) {
            expected += (i > 0 ? ", " : "") + arguments[i].toString();
        }
        expected += ")";
        assertEquals(expected, invocation.toString());
    }

    @Test
    public void testToStringWithPrintSettings() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(Mockito.method(String.class, "foo", new Class[0]), new Location());
        Object[] arguments = new Object[]{"arg1", "arg2"};

        Invocation invocation = new Invocation(mock, method, arguments, 1, null);

        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);

        String expected = "invocation on mock [mockito-test] of method " + method.getName() + "(";
        for (int i = 0; i < arguments.length; i++) {
            expected += (i > 0 ? "\n\t" : "") + "arg" + i;
        }
        expected += ")";
        assertEquals(expected, invocation.toString(printSettings));
    }

    @Test
    public void testToStringWithMatchers() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(Mockito.method(String.class, "foo", new Class[0]), new Location());
        Object[] arguments = new Object[]{"arg1", "arg2"};

        Invocation invocation = new Invocation(mock, method, arguments, 1, null);

        List<Matcher> matchers = Arrays.asList(eq("arg1"), eq("arg2"));

        String expected = "invocation on mock [mockito-test] of method foo(";
        for (int i = 0; i < arguments.length; i++) {
            expected += (i > 0 ? ", " : "") + matchers.get(i).toString();
        }
        expected += ")";
        assertEquals(expected, invocation.toString(matchers, new PrintSettings()));
    }

    @Test
    public void testGetSequenceNumber() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(Mockito.method(String.class, "foo", new Class[0]), new Location());
        Object[] arguments = new Object[]{"arg1", "arg2"};

        Invocation invocation = new Invocation(mock, method, arguments, 1, null);
        assertEquals(1, invocation.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(Mockito.method(String.class, "foo", new Class[0]), new Location());
        Object[] arguments = new Object[]{"arg1", "arg2"};

        Invocation invocation1 = new Invocation(mock, method, arguments, 1, null);
        Invocation invocation2 = new Invocation(mock, method, arguments, 2, null);

        assertFalse(invocation1.isVerifiedInOrder());
        assertFalse(invocation2.isVerifiedInOrder());

        invocation1.markVerifiedInOrder();
        assertTrue(invocation1.isVerifiedInOrder());
        assertFalse(invocation2.isVerifiedInOrder());

        invocation2.markVerifiedInOrder();
        assertTrue(invocation1.isVerifiedInOrder());
        assertTrue(invocation2.isVerifiedInOrder());
    }

    @Test
    public void testIsVoid() {
        MockitoMethod method = new MockitoMethod(Mockito.method(Void.TYPE, "foo", new Class[0]), new Location());

        Invocation invocation = new Invocation(null, method, new Object[0], 0, null);
        assertTrue(invocation.isVoid());
    }

    @Test
    public void testIsValidReturnType() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(Mockito.method(String.class, "foo", new Class[0]), new Location());

        Invocation invocation = new Invocation(mock, method, new Object[0], 0, null);
        assertTrue(invocation.isValidReturnType(String.class));
        assertFalse(invocation.isValidReturnType(Integer.class));
    }

    @Test
    public void testIsValidException() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(Mockito.method(String.class, "foo", new Class[]{IllegalArgumentException.class}), new Location());

        Invocation invocation = new Invocation(mock, method, new Object[0], 0, null);
        assertTrue(invocation.isValidException(new IllegalArgumentException()));
        assertFalse(invocation.isValidException(new RuntimeException()));
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object mock = new Object() {
            public String foo(String arg1, String arg2) {
                return "result";
            }
        };
        MockitoMethod method = new MockitoMethod(Mockito.method(String.class, "foo", new Class[]{String.class, String.class}), new Location());
        Object[] arguments = new Object[]{"arg1", "arg2"};

        Invocation invocation = new Invocation(mock, method, arguments, 1, null);
        assertEquals("result", invocation.callRealMethod());
    }
}