package org.mockito.tests.internal.invocation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.ArgumentsMatchers;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
import org.mockito.internal.util.PrintingFriendlyInvocation;
import org.mockito.internal.util.MockitoMethod;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.MockitoTypeMatchers;
import org.mockito.internal.reporting.MatchersPrinter;
import org.mockito.internal.realmethod.RealMethod;
import org.mockito.exceptions.base.MockitoException;

import java.lang.reflect.Method;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

@RunWith(org.mockito.runners.JUnit4.class)
public class InvocationTest {

    @Test
    public void testNewInvocation() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod mockitoMethod = Mockito.method(Object.class, "testMethod", new Class[]{Integer.TYPE, String.class});
        Object[] args = new Object[]{1, "test"};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod(mockitoMethod));

        assertEquals(mock, invocation.getMock());
        assertEquals(mockitoMethod, invocation.getMethod());
        assertArrayEquals(args, invocation.getArguments());
        assertEquals(1, invocation.getSequenceNumber());
        assertEquals(new Location(), invocation.getLocation());
        assertTrue(invocation instanceof InvocationOnMock);
        assertTrue(invocation instanceof PrintingFriendlyInvocation);
    }

    @Test
    public void testExpandVarArgs() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod mockitoMethod = Mockito.method(Object.class, "testMethod", new Class[]{Integer.TYPE, Integer[].class});
        Object[] args = new Object[]{1, new Integer[]{1, 2, 3}};
        Object[] expectedArgs = new Object[]{1, 1, 2, 3};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod(mockitoMethod));

        assertArrayEquals(expectedArgs, invocation.getArguments());
        assertArrayEquals(args, invocation.getRawArguments());
    }

    @Test
    public void testToString() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod mockitoMethod = Mockito.method(Object.class, "testMethod", new Class[]{Integer.TYPE, String.class});
        Object[] args = new Object[]{1, "test"};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod(mockitoMethod));

        assertEquals("test(int arg0, String arg1)", invocation.toString());
    }

    @Test
    public void testToStringWithMatchers() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod mockitoMethod = Mockito.method(Object.class, "testMethod", new Class[]{Integer.TYPE, String.class});
        Object[] args = new Object[]{1, "test"};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod(mockitoMethod));

        PrintSettings printSettings = new PrintSettings();
        printSettings.setPrintStacktrace(true);
        printSettings.setMultiline(true);

        List<Matcher> matchers = invocation.argumentsToMatchers();
        MatchersPrinter matchersPrinter = new MatchersPrinter();

        String expectedOutput = "test(int arg0, String arg1)\n" +
                "arg0 = <1>\n" +
                "arg1 = <\"test\">\n" +
                "at Object.testMethod(Object.java:0)\n";

        String output = invocation.toString(matchers, printSettings);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testIsToString() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod mockitoMethod = Mockito.method(Object.class, "toString", new Class[0]);

        assertTrue(Invocation.isToString(Mockito.invocationOn(mock).method(MockitoTypeMatchers.isMethod(mockitoMethod))));
    }

    @Test(expected = MockitoException.class)
    public void testIsToStringNegative() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod mockitoMethod = Mockito.method(Object.class, "testMethod", new Class[]{Integer.TYPE, String.class});

        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, new RealMethod(mockitoMethod));

        Invocation.isToString(Mockito.invocationOn(mock).method(MockitoTypeMatchers.isMethod(mockitoMethod)));
    }

    @Test
    public void testIsValidException() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod mockitoMethod = Mockito.method(Object.class, "testMethod", new Class[0]);
        Throwable expectedException = new RuntimeException();

        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, new RealMethod(mockitoMethod));

        assertTrue(invocation.isValidException(expectedException));
    }

    @Test
    public void testIsValidReturnType() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod mockitoMethod = Mockito.method(Object.class, "testMethod", new Class[]{Integer.TYPE, String.class});
        Class expectedReturnType = String.class;

        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, new RealMethod(mockitoMethod));

        assertTrue(invocation.isValidReturnType(expectedReturnType));
    }

    @Test
    public void testIsVoid() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod mockitoMethod = Mockito.method(Object.class, "testMethod", new Class[0]);

        assertTrue(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod mockitoMethod = Mockito.method(Object.class, "testMethod", new Class[]{Integer.TYPE, String.class});
        Class expectedReturnType = String.class;

        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, new RealMethod(mockitoMethod));

        assertEquals("String", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod mockitoMethod = Mockito.method(Object.class, "testMethod", new Class[]{Integer.TYPE, String.class});

        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, new RealMethod(mockitoMethod));

        assertEquals("testMethod", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod mockitoMethod = Mockito.method(Object.class, "testMethod", new Class[]{Boolean.TYPE});

        assertTrue(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod mockitoMethod = Mockito.method(Object.class, "testMethod", new Class[]{Integer.TYPE, String.class});

        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, new RealMethod(mockitoMethod));

        assertEquals(new Location(), invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod mockitoMethod = Mockito.method(Object.class, "testMethod", new Class[]{Integer.TYPE, String.class});

        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, new RealMethod(mockitoMethod));

        assertEquals(2, invocation.getArgumentsCount());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod mockitoMethod = Mockito.method(Object.class, "testMethod", new Class[]{Integer.TYPE});
        Object[] args = new Object[]{1};
        RealMethod realMethod = new RealMethod(mockitoMethod) {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                return 42;
            }
        };
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, realMethod);

        assertEquals(42, invocation.callRealMethod());
    }
}