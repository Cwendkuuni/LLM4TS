package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.internal.matchers.ArgumentsMatchers.*;
import static org.mockito.internal.util.MockUtil.getMockName;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.exceptions.printing.PrintingFriendlyString;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.matchers.*;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.OngoingStubbing;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class InvocationTest {

    @Test
    public void testConstructor() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod method = Mockito.method(String.class, "length");
        Object[] args = new Object[]{new String("foo")};
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertEquals(mock, invocation.getMock());
        assertEquals(method, invocation.getMethod());
        assertArrayEquals(args, invocation.getArguments());
        assertEquals(sequenceNumber, invocation.getSequenceNumber());
        assertEquals(realMethod, invocation.getRealMethod());
    }

    @Test
    public void testExpandVarArgs() {
        Object[] args = new Object[]{null, new Object[]{1, 2, 3}};
        Object[] expandedArgs = Invocation.expandVarArgs(true, args);

        assertEquals(3, expandedArgs.length);
        assertArrayEquals(new Object[]{null, 1, 2, 3}, expandedArgs);

        args = new Object[]{null, new int[0]};
        expandedArgs = Invocation.expandVarArgs(true, args);

        assertEquals(1, expandedArgs.length);
        assertNull(expandedArgs[0]);

        args = new Object[]{null, null};
        expandedArgs = Invocation.expandVarArgs(true, args);

        assertEquals(1, expandedArgs.length);
        assertNull(expandedArgs[0]);

        args = new Object[]{null, "abcd"};
        expandedArgs = Invocation.expandVarArgs(true, args);

        assertEquals(1, expandedArgs.length);
        assertArrayEquals(new Object[]{"abcd"}, expandedArgs);
    }

    @Test
    public void testEquals() {
        Object mock1 = Mockito.mock(String.class);
        Object mock2 = Mockito.mock(String.class);
        MockitoMethod method = Mockito.method(String.class, "length");
        Object[] args1 = new Object[]{new String("foo")};
        Object[] args2 = new Object[]{new String("bar")};

        Invocation invocation1 = new Invocation(mock1, method, args1, 1, null);
        Invocation invocation2 = new Invocation(mock2, method, args2, 1, null);

        assertFalse(invocation1.equals(invocation2));

        invocation2 = new Invocation(mock1, method, args1, 1, null);

        assertTrue(invocation1.equals(invocation2));

        MockitoMethod method1 = Mockito.method(String.class, "length", int.class);
        Object[] args3 = new Object[]{new String("foo"), 5};

        invocation1 = new Invocation(mock1, method1, args3, 2, null);

        assertFalse(invocation1.equals(invocation2));
    }

    @Test
    public void testToString() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod method = Mockito.method(String.class, "length");
        Object[] args = new Object[]{new String("foo")};
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        String expected = getQualifiedMethodName(mock, method) + "(" + printArguments(args) + ")";
        String result = invocation.toString();

        assertEquals(expected, result);
    }

    @Test
    public void testToStringWithMatchers() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod method = Mockito.method(String.class, "length");
        Object[] args = new Object[]{new String("foo")};
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        List<Matcher> matchers = Arrays.asList(argThat(isNull()), argThat(isA(String.class)), argThat(isNumber()));

        String expected = getQualifiedMethodName(mock, method) + "(" + printMatchers(matchers) + ")";
        String result = invocation.toString(matchers, new PrintSettings());

        assertEquals(expected, result);
    }

    @Test
    public void testToStringWithMultilinePrintSettings() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod method = Mockito.method(String.class, "length");
        Object[] args = new Object[]{new String("foo")};
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        List<Matcher> matchers = Arrays.asList(argThat(isNull()), argThat(isA(String.class)), argThat(isNumber()));

        String expected = getQualifiedMethodName(mock, method) + "\n" + printMatchers(matchers);
        String result = invocation.toString(matchers, new PrintSettings(true));

        assertEquals(expected, result);
    }

    @Test
    public void testIsToString() {
        MockitoMethod method = Mockito.method(Object.class, "toString");
        assertTrue(Invocation.isToString(MockitoOnMock.createInvocation(method)));
    }

    @Test
    public void testIsValidException() {
        MockitoMethod method = Mockito.method(String.class, "length", Exception.class);
        Throwable throwable = new RuntimeException();

        assertTrue(Invocation.isValidException(new InvocationOnMock(method, new Object[0], new Object[0], 1, null, throwable)));
    }

    @Test
    public void testIsValidReturnType() {
        InvocationOnMock invocation = MockitoOnMock.createInvocation(Mockito.method(String.class, "length"));
        assertFalse(Invocation.isValidReturnType(int[].class, invocation));
        assertTrue(Invocation.isValidReturnType(int.class, invocation));
        assertTrue(Invocation.isValidReturnType(String.class, invocation));
    }

    @Test
    public void testIsVoid() {
        MockitoMethod method = Mockito.method(String.class, "length", Void.TYPE);
        assertTrue(Invocation.isVoid(new InvocationOnMock(method, new Object[0], new Object[0], 1, null)));
    }

    @Test
    public void testPrintMethodReturnType() {
        MockitoMethod method = Mockito.method(String.class, "length", int.class);
        assertEquals("int", Invocation.printMethodReturnType(method));
    }

    @Test
    public void testGetMethodName() {
        MockitoMethod method = Mockito.method(String.class, "length");
        assertEquals("length", Invocation.getMethodName(method));
    }

    @Test
    public void testReturnsPrimitive() {
        MockitoMethod method = Mockito.method(String.class, "length");
        assertFalse(Invocation.returnsPrimitive(new InvocationOnMock(method, new Object[0], new Object[0], 1, null)));
        assertTrue(Invocation.returnsPrimitive(new InvocationOnMock(Mockito.method(String.class, "charAt", char.class), new Object[0], new Object[0], 1, null)));
    }

    @Test
    public void testGetLocation() {
        MockitoMethod method = Mockito.method(String.class, "length");
        Invocation invocation = new Invocation(null, method, new Object[0], 1, null);

        assertNull(Invocation.getLocation(invocation));

        OngoingStubbing<String> stubbing = Mockito.doReturn("foo").when(Mockito.mock(String.class));
        invocation = new Invocation(stubbing.getMock(), method, new Object[0], 1, null);

        Location location = Invocation.getLocation(invocation);

        assertEquals(getMockName(stubbing.getMock()), location.getClassName());
        assertEquals(method.getDeclaringClass().getName(), location.getClassName());
        assertEquals(method.getName(), location.getMethodName());
        assertEquals(method.getDeclaringClass().getDeclaringClass().getName(), location.getClassNameInMethod());
        assertEquals(method.getName() + "(" + printArguments(new Object[0]) + ")", location.getMethodWithArguments());
    }

    @Test
    public void testGetArgumentsCount() {
        MockitoMethod method = Mockito.method(String.class, "length");
        Invocation invocation = new Invocation(null, method, new Object[0], 1, null);

        assertEquals(0, Invocation.getArgumentsCount(invocation));
    }

    @Test
    public void testGetRawArguments() {
        MockitoMethod method = Mockito.method(String.class, "length");
        Invocation invocation = new Invocation(null, method, new Object[0], 1, null);

        assertArrayEquals(new Object[0], Invocation.getRawArguments(invocation));
    }

    @Test(expected = RuntimeException.class)
    public void testCallRealMethod() throws Throwable {
        MockitoMethod method = Mockito.method(String.class, "length");
        Object mock = Mockito.mock(String.class);
        Invocation invocation = new Invocation(mock, method, new Object[0], 1, null);

        invocation.callRealMethod();
    }

    private String getQualifiedMethodName(Object mock, MockitoMethod method) {
        return getMockName(mock) + "." + printMethodName(method);
    }

    private String printMethodName(MockitoMethod method) {
        String name = method.getName();
        if (method.getParameterTypes().length > 0) {
            name += "<" + Arrays.toString(method.getParameterTypes()) + ">";
        }
        return name;
    }

    private String printArguments(Object[] arguments) {
        if (arguments == null) {
            return "[]";
        }
        if (arguments.length == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[');
        for (int i = 0; i < arguments.length; i++) {
            if (i > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(printObject(arguments[i]));
        }
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    private String printObject(Object object) {
        if (object == null) {
            return "null";
        }
        if (object.getClass().isArray()) {
            return printArguments((Object[]) object);
        }
        return object.toString();
    }

    private String printMatchers(List<Matcher> matchers) {
        if (matchers.isEmpty()) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[');
        boolean first = true;
        for (Matcher matcher : matchers) {
            if (first) {
                first = false;
            } else {
                stringBuilder.append(", ");
            }
            stringBuilder.append(PrintingFriendlyString.print(matcher.description()));
        }
        stringBuilder.append(']');
        return stringBuilder.toString();
    }
}