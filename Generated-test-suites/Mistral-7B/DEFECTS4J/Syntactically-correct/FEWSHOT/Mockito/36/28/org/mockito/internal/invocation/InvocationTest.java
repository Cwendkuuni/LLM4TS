package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.internal.matchers.Arguments.*;
import static org.mockito.internal.matchers.ArrayEquals.*;
import static org.mockito.internal.matchers.Equals.*;

import java.util.Arrays;

import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectsMethodsGuru;
import org.mockito.internal.util.PrintSettings.Printable;

public class InvocationTest {

    @Test
    public void testExpandVarArgs() {
        Object[] args1 = new Object[] { 1 };
        Object[] args2 = new Object[] { 1, new Object[] { "foo", "bar" } };

        Object[] result1 = Invocation.expandVarArgs(false, args1);
        Object[] result2 = Invocation.expandVarArgs(true, args2);

        assertArrayEquals(args1, result1);
        assertArrayEquals(args2, result2);
    }

    @Test
    public void testNewInstance() {
        Object mock = new Object();
        MockitoMethod method = MockitoMethod.mockedMethod(String.class, "method", new Class[] { int.class });

        Invocation invocation = new Invocation(mock, method, new Object[] { 1 }, 0, null);
        assertEquals(mock, invocation.getMock());
        assertEquals(method, invocation.getMethod());
        assertEquals(new Object[] { 1 }, invocation.getArguments());
        assertEquals(new Object[] { 1 }, invocation.getRawArguments());
        assertEquals(0, invocation.getSequenceNumber());
        assertFalse(invocation.isVerified());
        assertFalse(invocation.isVerifiedInOrder());
        assertNull(invocation.getLocation());
        assertFalse(invocation.isVoid());
        assertFalse(invocation.isValidException(new RuntimeException()));
        assertFalse(invocation.isValidReturnType(String.class));
        assertFalse(invocation.isValidReturnType(Double.TYPE));
        assertFalse(invocation.returnsPrimitive());
        assertEquals(0, invocation.getLocation().getLineNumber());
        assertEquals(0, invocation.getLocation().getClassName());
        assertEquals(0, invocation.getArgumentsCount());
        assertFalse(invocation.callRealMethod().equals("result"));

        invocation.markVerified();
        assertTrue(invocation.isVerified());
        assertFalse(invocation.isVerifiedInOrder());

        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerified());
        assertTrue(invocation.isVerifiedInOrder());

        MockitoMethod method2 = MockitoMethod.mockedMethod(String.class, "method", new Class[] { int.class, int.class });
        Invocation invocation2 = new Invocation(mock, method2, new Object[] { 1, 2 }, 1, null);
        assertEquals(mock, invocation2.getMock());
        assertEquals(method2, invocation2.getMethod());
        assertEquals(new Object[] { 1, 2 }, invocation2.getArguments());
        assertEquals(new Object[] { 1, 2 }, invocation2.getRawArguments());
        assertEquals(1, invocation2.getSequenceNumber());
        assertFalse(invocation2.isVerified());
        assertFalse(invocation2.isVerifiedInOrder());
        assertNull(invocation2.getLocation());
        assertFalse(invocation2.isVoid());
        assertFalse(invocation2.isValidException(new RuntimeException()));
        assertFalse(invocation2.isValidReturnType(String.class));
        assertFalse(invocation2.isValidReturnType(Double.TYPE));
        assertFalse(invocation2.returnsPrimitive());
        assertEquals(0, invocation2.getLocation().getLineNumber());
        assertEquals(0, invocation2.getLocation().getClassName());
        assertEquals(2, invocation2.getArgumentsCount());
        assertFalse(invocation2.callRealMethod().equals("result"));

        invocation2.markVerified();
        assertTrue(invocation2.isVerified());
        assertFalse(invocation2.isVerifiedInOrder());

        InvocationOnMock invocation3 = mockitoMock(Mockito.mock(String.class)).method("method", 1);
        assertEquals(String.class, invocation3.getMethod().getReturnType());
        assertTrue(Invocation.isToString(invocation3));

        Printable printableInvocation = mockitoMock(Printable.class);
        assertFalse(Invocation.isToString(printableInvocation));

        PrintSettings printSettings = new PrintSettings();
        printSettings.setPrintStacktrace(true);
        Invocation invocation4 = new Invocation(mock, method, new Object[] { 1 }, 0, null);
        String expected = "invocation.getMock()\n" +
                "org.mockito.internal.invocation.Invocation:method(int)\n" +
                "    at org.mockito.internal.invocation.Invocation.<init>(Invocation.java:69)\n" +
                "    at org.mockito.internal.invocation.Invocation.<init>(Invocation.java:71)\n" +
                "    at org.mockito.internal.invocation.Invocation.<init>(Invocation.java:98)\n" +
                "    at org.mockito.internal.spy.Spyer.createInvocation(Spyer.java:51)\n" +
                "    at org.mockito.internal.spy.Spyer.createInvocation(Spyer.java:36)\n" +
                "    at org.mockito.internal.spy.Spyer.spy(Spyer.java:47)\n" +
                "    at org.mockito.internal.spy.Spyer.<init>(Spyer.java:30)\n" +
                "    at org.mockito.internal.spy.Spy.doReturn(Spy.java:23)\n" +
                "    at org.mockito.api.Mockito.<clinit>(Mockito.java:27)\n" +
                "    at org.mockito.Mockito.<clinit>(Mockito.java:18)\n" +
                "    at org.mockito.Mockito.<clinit>(Mockito.java:13)\n" +
                "    at org.mockito.Mockito.<clinit>(Mockito.java:11)\n" +
                "    at org.mockito.Mockito.<clinit>(Mockito.java:8)\n" +
                "    at org.mockito.internal.invocation.InvocationTest.main(InvocationTest.java:106)\n";
        assertEquals(expected, invocation4.toString(printSettings));

        String expected2 = "invocation.getMock()\n" +
                "method(int)\n" +
                "Argument(s):\n" +
                "1\n";
        String result = invocation4.toString(Arrays.<Matcher>asList(eq(1)), printSettings);
        assertEquals(expected2, result);

        String expected3 = "invocation.getMock()\n" +
                "method(int)\n" +
                "Arguments:\n" +
                "Arguments(\n" +
                "    equals(int)\n" +
                ")\n" +
                "    at 1\n";
        PrintingFriendlyInvocation printableInvocation2 = new PrintingFriendlyInvocation(invocation4);
        String result2 = printableInvocation2.toString();
        assertEquals(expected3, result2);
    }
}