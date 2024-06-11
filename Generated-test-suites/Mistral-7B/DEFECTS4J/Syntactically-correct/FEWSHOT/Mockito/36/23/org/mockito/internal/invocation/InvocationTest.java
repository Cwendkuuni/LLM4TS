package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.internal.matchers.ArgumentsMatchers.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.MethodType;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.Arrays;

public class InvocationTest {

    @Test
    public void testConstructor() {
        Object mock = Mockito.mock(String.class);
        Method method = String.class.getMethod("length", new Class[0]);
        Invocation invocation = new Invocation(mock, new MethodInvocation(method, null), new Object[0], 1, new RealMethodStub());

        assertEquals(mock, invocation.getMock());
        assertEquals(method, invocation.getMethod());
        assertArrayEquals(new Object[0], invocation.getArguments());
        assertEquals(1, invocation.getSequenceNumber());
        assertNull(invocation.getLocation());
        assertEquals(false, invocation.isVerified());
        assertEquals(false, invocation.isVerifiedInOrder());
    }

    @Test
    public void testExpandVarArgs() {
        Object[] args = new Object[]{1, new Object[]{2, 3}, 4};
        Object[] expected = new Object[]{1, 2, 3, 4};

        Invocation invocation = new Invocation(null, null, args, 1, new RealMethodStub());
        Object[] result = invocation.expandVarArgs(true, args);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testExpandVarArgs_emptyVarArgs() {
        Object[] args = new Object[]{1, null};
        Object[] expected = new Object[]{1};

        Invocation invocation = new Invocation(null, null, args, 1, new RealMethodStub());
        Object[] result = invocation.expandVarArgs(true, args);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testEquals() {
        Object mock = Mockito.mock(String.class);
        Method method = String.class.getMethod("length", new Class[0]);
        Invocation invocation1 = new Invocation(mock, new MethodInvocation(method, null), new Object[0], 1, new RealMethodStub());
        Invocation invocation2 = new Invocation(mock, new MethodInvocation(method, null), new Object[0], 1, new RealMethodStub());

        Invocation invocation3 = new Invocation(Mockito.mock(Integer.class), new MethodInvocation(method, null), new Object[0], 1, new RealMethodStub());
        Invocation invocation4 = new Invocation(mock, new MethodInvocation(new Method("test", Integer.TYPE), null), new Object[0], 1, new RealMethodStub());

        assertTrue(invocation1.equals(invocation2));
        assertFalse(invocation1.equals(invocation3));
        assertFalse(invocation1.equals(invocation4));
    }

    @Test
    public void testToString() {
        Object mock = Mockito.mock(String.class);
        Method method = String.class.getMethod("length", new Class[0]);
        Invocation invocation = new Invocation(mock, new MethodInvocation(method, null), new Object[0], 1, new RealMethodStub());

        String expected = "MockitoMock{mock=[Mock for test], method=public int java.lang.String.length()}"
                + System.lineSeparator()
                + "    at org.mockito.internal.invocation.Invocation$MethodInvocation.getMethod(Invocation.java:130)"
                + System.lineSeparator()
                + "    at org.mockito.internal.invocation.Invocation.<init>(Invocation.java:41)"
                + System.lineSeparator()
                + "    at org.mockito.internal.invocation.Invocation.<init>(Invocation.java:46)"
                + System.lineSeparator();

        assertEquals(expected, invocation.toString());
    }

    @Test
    public void testToString_withArgumentsToMatchers() {
        Object mock = Mockito.mock(String.class);
        Method method = String.class.getMethod("length", new Class[0]);
        Invocation invocation = new Invocation(mock, new MethodInvocation(method, null), new Object[0], 1, new RealMethodStub());

        List<Matcher> matchers = Arrays.asList(isNull(), isA(Integer.class));
        String expected = "MockitoMock{mock=[Mock for test], method=public int java.lang.String.length()}"
                + System.lineSeparator()
                + "    at org.mockito.internal.invocation.Invocation$MethodInvocation.getMethod(Invocation.java:130)"
                + System.lineSeparator()
                + "    at org.mockito.internal.invocation.Invocation.<init>(Invocation.java:41)"
                + System.lineSeparator()
                + "    at org.mockito.internal.invocation.Invocation.<init>(Invocation.java:46)"
                + System.lineSeparator()
                + "    at org.mockito.internal.matchers.Matchers.argumentsToMatchers(Matchers.java:121)"
                + System.lineSeparator()
                + "    at org.mockito.internal.invocation.Invocation.<init>(Invocation.java:51)"
                + System.lineSeparator()
                + "    at org.mockito.internal.invocation.Invocation.<init>(Invocation.java:55)"
                + System.lineSeparator();

        assertEquals(expected, invocation.toString(matchers, new PrintSettings()));
    }

    @Test
    public void testToString_multiline() {
        Object mock = Mockito.mock(String.class);
        Method method = String.class.getMethod("length", new Class[0]);
        Invocation invocation = new Invocation(mock, new MethodInvocation(method, null), new Object[0], 1, new RealMethodStub());

        List<Matcher> matchers = Arrays.asList(isNull(), isA(Integer.class));
        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);

        String expected = "MockitoMock{mock=[Mock for test], method=public int java.lang.String.length()}"
                + System.lineSeparator()
                + "    at org.mockito.internal.invocation.Invocation$MethodInvocation.getMethod(Invocation.java:130)"
                + System.lineSeparator()
                + "    at org.mockito.internal.invocation.Invocation.<init>(Invocation.java:41)"
                + System.lineSeparator()
                + "    at org.mockito.internal.invocation.Invocation.<init>(Invocation.java:46)"
                + System.lineSeparator()
                + "    at org.mockito.internal.matchers.Matchers.argumentsToMatchers(Matchers.java:121)"
                + System.lineSeparator()
                + "    at org.mockito.internal.invocation.Invocation.<init>(Invocation.java:51)"
                + System.lineSeparator()
                + "    at org.mockito.internal.invocation.Invocation.<init>(Invocation.java:55)"
                + System.lineSeparator()
                + System.lineSeparator()
                + "Arguments:"
                + System.lineSeparator()
                + "   0: $0"
                + System.lineSeparator()
                + "   1: <1> [null, class java.lang.Integer]"
                + System.lineSeparator();

        assertEquals(expected, invocation.toString(matchers, printSettings));
    }

    @Test
    public void testIsToString() {
        MockitoMethod mockitoMethod = Mockito.method(String.class, "toString", new Class[0]);
        assertTrue(Invocation.isToString(Mockito.mock(InvocationOnMock.class, mockitoMethod)));
    }

    @Test
    public void testIsValidException() {
        Object mock = Mockito.mock(String.class);
        Method method = String.class.getMethod("length", new Class[0]);
        Invocation invocation = new Invocation(mock, new MethodInvocation(method, null), new Object[0], 1, new RealMethodStub());

        Throwable throwable = new RuntimeException();

        assertTrue(invocation.isValidException(throwable));

        assertFalse(invocation.isValidException(new IllegalArgumentException()));
    }

    @Test
    public void testIsValidReturnType() {
        Object mock = Mockito.mock(String.class);
        Method method = String.class.getMethod("length", new Class[0]);
        Invocation invocation = new Invocation(mock, new MethodInvocation(method, null), new Object[0], 1, new RealMethodStub());

        assertTrue(invocation.isValidReturnType(int.class));
        assertTrue(invocation.isValidReturnType(Integer.class));
        assertTrue(invocation.isValidReturnType(int[].class));
        assertFalse(invocation.isValidReturnType(String.class));
    }

    @Test
    public void testIsVoid() {
        Object mock = Mockito.mock(Void.TYPE);
        Method method = Void.TYPE.getMethod("toString", new Class[0]);
        Invocation invocation = new Invocation(mock, new MethodInvocation(method, null), new Object[0], 1, new RealMethodStub());

        assertTrue(invocation.isVoid());

        Object mockString = Mockito.mock(String.class);
        Method methodLength = String.class.getMethod("length", new Class[0]);
        Invocation invocationLength = new Invocation(mockString, new MethodInvocation(methodLength, null), new Object[0], 1, new RealMethodStub());

        assertFalse(invocationLength.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Object mock = Mockito.mock(String.class);
        Method method = String.class.getMethod("length", new Class[0]);
        Invocation invocation = new Invocation(mock, new MethodInvocation(method, null), new Object[0], 1, new RealMethodStub());

        assertEquals("int", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        Object mock = Mockito.mock(String.class);
        Method method = String.class.getMethod("length", new Class[0]);
        Invocation invocation = new Invocation(mock, new MethodInvocation(method, null), new Object[0], 1, new RealMethodStub());

        assertEquals("length", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        Object mock = Mockito.mock(boolean.class);
        Method method = boolean.class.getMethod("booleanValue", new Class[0]);
        Invocation invocation = new Invocation(mock, new MethodInvocation(method, null), new Object[0], 1, new RealMethodStub());

        assertTrue(invocation.returnsPrimitive());

        Object mockString = Mockito.mock(String.class);
        Method methodLength = String.class.getMethod("length", new Class[0]);
        Invocation invocationLength = new Invocation(mockString, new MethodInvocation(methodLength, null), new Object[0], 1, new RealMethodStub());

        assertFalse(invocationLength.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Object mock = Mockito.mock(String.class);
        Method method = String.class.getMethod("length", new Class[0]);
        Invocation invocation = new Invocation(mock, new MethodInvocation(method, null), new Object[0], 1, new RealMethodStub());

        assertEquals(new Location(), invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        Object mock = Mockito.mock(String.class);
        Method method = String.class.getMethod("length", new Class[0]);
        Invocation invocation = new Invocation(mock, new MethodInvocation(method, null), new Object[0], 1, new RealMethodStub());

        assertEquals(0, invocation.getArgumentsCount());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object mock = Mockito.mock(String.class);
        Method method = String.class.getMethod("length", new Class[0]);
        Invocation invocation = new Invocation(mock, new MethodInvocation(method, null), new Object[0], 1, new RealMethodStub());

        assertEquals(4, invocation.callRealMethod());
    }
}