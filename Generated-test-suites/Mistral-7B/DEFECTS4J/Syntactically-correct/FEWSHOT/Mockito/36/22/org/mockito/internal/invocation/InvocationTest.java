package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.internal.matchers.ArgumentsMatchers.*;
import static org.mockito.internal.reporting.PrintSettings.printSettings;

import java.util.Arrays;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.matchers.*;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.util.*;
import org.mockito.invocation.InvocationType;

public class InvocationTest {

    @Test
    public void testGetSequenceNumber() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod("testMethod", new Class[0], InvocationType.METHOD);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 0, null);
        assertEquals(0, invocation.getSequenceNumber());
    }

    @Test
    public void testGetMethod() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod("testMethod", new Class[0], InvocationType.METHOD);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 0, null);
        assertEquals(mockitoMethod, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod("testMethod", new Class[]{String.class, int.class}, InvocationType.METHOD);
        Object[] arguments = new Object[]{"arg1", 1};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 0, null);
        assertEquals(arguments, invocation.getArguments());
    }

    @Test
    public void testEquals() {
        Object mock1 = new Object();
        Object mock2 = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod("testMethod", new Class[]{String.class, int.class}, InvocationType.METHOD);
        Object[] arguments1 = new Object[]{"arg1", 1};
        Object[] arguments2 = new Object[]{"arg2", 2};
        Invocation invocation1 = new Invocation(mock1, mockitoMethod, arguments1, 0, null);
        Invocation invocation2 = new Invocation(mock2, mockitoMethod, arguments2, 0, null);

        assertFalse(invocation1.equals(invocation2));

        Object[] arguments3 = new Object[]{"arg1"};
        Invocation invocation3 = new Invocation(mock1, mockitoMethod, arguments3, 0, null);
        assertFalse(invocation1.equals(invocation3));

        Object[] arguments4 = new Object[]{"arg1", 1};
        Invocation invocation4 = new Invocation(mock1, mockitoMethod, arguments4, 0, null);
        assertTrue(invocation1.equals(invocation4));
    }

    @Test
    public void testToString() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod("testMethod", new Class[]{String.class, int.class}, InvocationType.METHOD);
        Object[] arguments = new Object[]{"arg1", 1};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 0, null);

        List<Matcher> matchers = invocation.argumentsToMatchers();
        PrintSettings printSettings = printSettings();
        String result = invocation.toString(matchers, printSettings);

        String expected = "testMethod([arg1], [int 1])";
        assertEquals(expected, result);
    }

    @Test
    public void testToString_Multiline() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod("testMethod", new Class[]{String[].class}, InvocationType.METHOD);
        Object[] arguments = new Object[]{new String[]{"arg1", "arg2"}};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 0, null);

        List<Matcher> matchers = invocation.argumentsToMatchers();
        PrintSettings printSettings = printSettings();
        printSettings.setMultiline(true);
        String result = invocation.toString(matchers, printSettings);

        String expected = "testMethod([[arg1], [arg2]])";
        assertEquals(expected, result);
    }

    @Test
    public void testToString_ArgumentsBlock() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod("testMethod", new Class[]{String[].class}, InvocationType.METHOD);
        Object[] arguments = new Object[]{new String[]{"arg1", "arg2"}};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 0, null);

        List<Matcher> matchers = invocation.argumentsToMatchers();
        PrintSettings printSettings = printSettings();
        printSettings.setMultiline(false);
        String result = invocation.toString(matchers, printSettings);

        String expected = "testMethod([[arg1], [arg2]])";
        assertEquals(expected, result);
    }

    @Test
    public void testQualifiedMethodName() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod("testMethod", new Class[0], InvocationType.METHOD);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 0, null);

        String result = invocation.qualifiedMethodName();
        String expected = "Object.testMethod";
        assertEquals(expected, result);
    }

    @Test
    public void testArgumentsToMatchers() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod("testMethod", new Class[]{String.class, int.class}, InvocationType.METHOD);
        Object[] arguments = new Object[]{"arg1", 1};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 0, null);

        List<Matcher> matchers = invocation.argumentsToMatchers();
        assertEquals(2, matchers.size());
        assertEquals(argThat(instanceOf(String.class)), matchers.get(0));
        assertEquals(argThat(equalTo(1)), matchers.get(1));
    }

    @Test
    public void testIsToString() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod("toString", new Class[0], InvocationType.METHOD);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 0, null);

        assertTrue(Invocation.isToString(invocation));
    }

    @Test
    public void testIsValidException() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod("testMethod", new Class[0], InvocationType.METHOD);
        Throwable throwable = new Exception();
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 0, null);

        assertFalse(invocation.isValidException(throwable));

        Class[] exceptions = new Class[]{Exception.class};
        Invocation invocationWithException = new Invocation(mock, mockitoMethod, new Object[0], 0, null);
        invocationWithException.markVerified();
        invocationWithException.markVerifiedInOrder();
        assertTrue(invocationWithException.isValidException(throwable));
    }

    @Test
    public void testIsValidReturnType() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod("testMethod", new Class[]{String.class}, InvocationType.METHOD);
        Class clazz = String.class;
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 0, null);

        assertTrue(invocation.isValidReturnType(clazz));
    }

    @Test
    public void testIsVoid() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod("testMethod", new Class[0], InvocationType.METHOD);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 0, null);

        assertFalse(invocation.isVoid());

        MockitoMethod mockitoMethodVoid = new MockitoMethod("voidMethod", new Class[0], InvocationType.METHOD);
        Invocation invocationVoid = new Invocation(mock, mockitoMethodVoid, new Object[0], 0, null);

        assertTrue(invocationVoid.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod("testMethod", new Class[]{String.class}, InvocationType.METHOD);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 0, null);

        assertEquals("String", invocation.printMethodReturnType());
    }

    @Test
    public void testGetLocation() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod("testMethod", new Class[]{String.class}, InvocationType.METHOD);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 0, null);

        assertNull(invocation.getLocation());

        invocation.setLocation(new Location());
        Location location = invocation.getLocation();

        assertNotNull(location);
    }

    @Test
    public void testGetArgumentsCount() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod("testMethod", new Class[]{String.class, int.class}, InvocationType.METHOD);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[]{"arg1", 1}, 0, null);

        assertEquals(2, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod("testMethod", new Class[]{String.class, int.class}, InvocationType.METHOD);
        Object[] rawArguments = new Object[]{"arg1", 1};
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[]{"arg1", 1}, 0, null);

        Object[] result = invocation.getRawArguments();

        assertArrayEquals(rawArguments, result);
    }

    @Test
    public void testCallRealMethod() throws Exception {
        Object mock = new Object() {
            public String testMethod(String arg1, int arg2) {
                return "result";
            }
        };
        MockitoMethod mockitoMethod = new MockitoMethod("testMethod", new Class[]{String.class, int.class}, InvocationType.METHOD);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[]{"arg1", 1}, 0, null);

        assertEquals("result", invocation.callRealMethod());
    }
}