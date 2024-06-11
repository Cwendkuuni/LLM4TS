package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.internal.matchers.ArgumentMatchers.eq;
import static org.mockito.internal.matchers.ArgumentMatchers.isNull;
import static org.mockito.internal.matchers.ArgumentMatchers.same;
import static org.mockito.internal.matchers.Arguments.arrayWith;
import static org.mockito.internal.matchers.Arguments.not;
import static org.mockito.internal.matchers.Arguments.contains;
import static org.mockito.internal.reporting.PrintSettings.printError;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InvocationOnMock;
import org.mockito.Mock;
import org.mockito.Mockito;

public class InvocationMatcherTest {
    @Mock
    private Object mock;

    @Mock
    private Object anotherMock;

    private List<Matcher> matchers;

    private Invocation invocation;
    private InvocationMatcher invocationMatcher;
    private Invocation candidate;

    @Before
    public void setUp() {
        matchers = new LinkedList<Matcher>() {{
            add(eq("foo"));
            add(isNull());
            add(same(mock));
            add(not(same(anotherMock)));
            add(contains(eq("bar")));
        }};

        invocation = Mockito.mock(Invocation.class);
        invocationMatcher = new InvocationMatcher(invocation, matchers);
        candidate = Mockito.mock(Invocation.class);
    }

    @Test
    public void testHasSameMethod() {
        Method method1 = Mockito.methodThat(invocation1 -> invocation1.getMethod().getName().equals("testMethod"));
        Method method2 = Mockito.methodThat(invocation2 -> invocation2.getMethod().getName().equals("testMethod"));

        Invocation invocation1 = Mockito.mock(Invocation.class, new Object[] { mock, method1 });
        Invocation invocation2 = Mockito.mock(Invocation.class, new Object[] { anotherMock, method2 });

        assertEquals(method1, invocation1.getMethod());
        assertEquals(method2, invocation2.getMethod());

        assertTrue(invocationMatcher.hasSameMethod(invocation1));
        assertTrue(invocationMatcher.hasSameMethod(invocation2));
    }

    @Test
    public void testHasDifferentMethod() {
        Method method1 = Mockito.methodThat(invocation1 -> invocation1.getMethod().getName().equals("testMethod1"));
        Method method2 = Mockito.methodThat(invocation2 -> invocation2.getMethod().getName().equals("testMethod2"));

        Invocation invocation1 = Mockito.mock(Invocation.class, new Object[] { mock, method1 });
        Invocation invocation2 = Mockito.mock(Invocation.class, new Object[] { anotherMock, method2 });

        assertFalse(invocationMatcher.hasSameMethod(invocation1));
        assertFalse(invocationMatcher.hasSameMethod(invocation2));
    }

    @Test
    public void testMatchesWithSameInvocation() {
        Invocation actual = invocation;

        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatchesWithDifferentInvocationSameMock() {
        Invocation actual = new InvocationOnMock(mock, getMethod(), Arrays.asList(new Object[] { "foo" }), null, null);

        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatchesWithDifferentInvocationDifferentMock() {
        Invocation actual = new InvocationOnMock(anotherMock, getMethod(), Arrays.asList(new Object[] { "foo" }), null, null);

        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatchesWithOverloadedMethod() {
        Method method1 = Mockito.methodThat(invocation1 -> invocation1.getMethod().getName().equals("testMethod"));
        Method method2 = Mockito.methodThat(invocation2 -> invocation2.getMethod().getName().equals("testMethod2"));

        Invocation invocation1 = Mockito.mock(Invocation.class, new Object[] { mock, method1 });
        Invocation invocation2 = Mockito.mock(Invocation.class, new Object[] { mock, method2 });

        assertFalse(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testMatchesWithOverloadedMethodSameArguments() {
        Method method1 = Mockito.methodThat(invocation1 -> invocation1.getMethod().getName().equals("testMethod"));
        Method method2 = Mockito.methodThat(invocation2 -> invocation2.getMethod().getName().equals("testMethod2"));
        Method method3 = Mockito.methodThat(invocation3 -> invocation3.getMethod().getName().equals("testMethod3"));

        Invocation invocation1 = Mockito.mock(Invocation.class, new Object[] { mock, method1 });
        Invocation invocation2 = Mockito.mock(Invocation.class, new Object[] { mock, method2 });
        Invocation invocation3 = Mockito.mock(Invocation.class, new Object[] { mock, method3, new Object[] { "bar" } });

        assertTrue(invocationMatcher.matches(invocation3));
    }

    @Test
    public void testMatchesWithOverloadedMethodDifferentArguments() {
        Method method1 = Mockito.methodThat(invocation1 -> invocation1.getMethod().getName().equals("testMethod"));
        Method method2 = Mockito.methodThat(invocation2 -> invocation2.getMethod().getName().equals("testMethod2"));

        Invocation invocation1 = Mockito.mock(Invocation.class, new Object[] { mock, method1 });
        Invocation invocation2 = Mockito.mock(Invocation.class, new Object[] { mock, method2, new Object[] { "bar" } });

        assertFalse(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testToString() {
        assertEquals(
            "InvocationMatcher[invocation=Invocation[method=public void org.apache.commons.cli.HelpParser.parseArgs(java.lang.String[], java.io.PrintWriter), argumentsToMatchers=[org.hamcrest.Matcher@42290633, org.hamcrest.CoreMatchers.nullValue(), org.mockito.internal.matchers.Arguments$CaptureArgumentMatcher@37866733, org.hamcrest.MatcherAssert.not(org.mockito.internal.matchers.Arguments$CaptureArgumentMatcher@50867634), org.hamcrest.CoreMatchers.contains(org.hamcrest.Matcher@68279648)], location=InvocationLocation[line=-1, methodName=<not available>, file=<not available>]], printSettings=MockitoPrintSettings[suppressDefaultMessages=false, printStackTrace=true]",
            invocationMatcher.toString());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);

        invocationMatcher.captureArgumentsFrom(Mockito.mock(Invocation.class));

        assertNull(invocationMatcher.getMatchers().get(0).match(null));
        assertEquals("foo", invocationMatcher.getMatchers().get(1).match("foo"));
        assertSame(mock, invocationMatcher.getMatchers().get(2).match(mock));
        assertFalse(invocationMatcher.getMatchers().get(3).match(anotherMock));
        assertTrue(invocationMatcher.getMatchers().get(4).match(Arrays.asList("bar")));

        invocationMatcher.captureArgumentsFrom(Mockito.argThat(invocation -> Arrays.deepEquals(invocation.getArguments(), arrayWith(eq("bar")).getArguments())));

        ArgumentCaptor<List<Object>> listCaptor = ArgumentCaptor.forClass(List.class);
        assertEquals(Arrays.asList("bar"), listCaptor.capture(invocationMatcher.getMatchers().get(4).match(invocation)));

        assertEquals(Arrays.asList("bar"), listCaptor.getValue());
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<Invocation>() {{
            add(Mockito.mock(Invocation.class));
            add(Mockito.mock(Invocation.class));
        }};

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, invocationMatchers.size());
        assertNotNull(invocationMatchers.get(0));
        assertNotNull(invocationMatchers.get(1));
    }

    @Test
    public void testCreateFromWithEmptyList() {
        List<Invocation> invocations = Collections.emptyList();

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(0, invocationMatchers.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFromWithNullList() {
        List<Invocation> invocations = null;

        InvocationMatcher.createFrom(invocations);
    }

    private Method getMethod() {
        return Mockito.methodThat(invocation -> {
            assertEquals(InvocationMatcher.class, invocation.getClass());
            printError(printSettings(), "Unexpected invocation. Should be mocked.", invocation);
            return null;
        });
    }
}