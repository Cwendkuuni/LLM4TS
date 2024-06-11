package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.arrayWithSize;
import static org.mockito.internal.matchers.ArgumentsMatcher.capture;
import static org.mockito.internal.matchers.ArgumentsMatcher.eq;
import static org.mockito.internal.matchers.ArgumentsMatcher.is;
import static org.mockito.internal.matchers.ArgumentsMatcher.nullValue;
import static org.mockito.internal.reporting.PrintSettings.print;
import static org.mockito.internal.reporting.PrintSettings.printInvocation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.runners.MockitoJUnitRunnerDelegate;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.InvocationMatcher;
import org.mockito.invocation.Location;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue("toString() should not be null", invocationMatcher.toString() != null);
        assertEquals("", invocationMatcher.toString());
        assertFalse("getMatchers() should be empty", invocationMatcher.getMatchers().isEmpty());
        assertEquals(0, invocationMatcher.getMatchers().size());
    }

    @Test
    public void testConstructorWithInvocationAndCustomMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(eq("foo"), is(1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertTrue("toString() should not be null", invocationMatcher.toString() != null);
        assertEquals("Matcher[equals(foo)] and Matcher[is(1)]", invocationMatcher.toString());
        assertFalse("getMatchers() should contain provided matchers", invocationMatcher.getMatchers().isEmpty());
        assertEquals(2, invocationMatcher.getMatchers().size());
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertNotNull("getInvocation() should not be null", invocationMatcher.getInvocation());
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMethod() {
        Method method = Mockito.mock(Method.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());

        when(invocation.getMethod()).thenReturn(method);

        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testMatchesWithSameMethod() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);

        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertTrue("invocation matchers should match", invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesWithDifferentMethods() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);

        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertFalse("invocation matchers should not match", invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesWithDifferentMocks() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);

        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);
        when(invocation1.getMock()).thenReturn(Mockito.mock(Object.class));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertFalse("invocation matchers should not match", invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesWithUnverified() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);

        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);
        when(invocation1.isVerified()).thenReturn(false);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertTrue("invocation matchers should match when unverified", invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesWithSameArguments() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        Object[] arguments1 = new Object[]{"foo", 1};
        Object[] arguments2 = new Object[]{"foo", 1};

        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);
        when(invocation1.getArguments()).thenReturn(arguments1);
        when(invocation2.getArguments()).thenReturn(arguments2);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertTrue("invocation matchers should match when arguments are the same", invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesWithDifferentArguments() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        Object[] arguments1 = new Object[]{"foo", 1};
        Object[] arguments2 = new Object[]{"bar", 2};

        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);
        when(invocation1.getArguments()).thenReturn(arguments1);
        when(invocation2.getArguments()).thenReturn(arguments2);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertFalse("invocation matchers should not match when arguments are different", invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testSafelyArgumentsMatchWithSameArguments() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Object[] arguments = new Object[]{"foo", 1};

        when(invocation.getArguments()).thenReturn(arguments);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue("safelyArgumentsMatch() should return true when arguments are the same", invocationMatcher.safelyArgumentsMatch(arguments));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchWithDifferentArguments() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Object[] arguments1 = new Object[]{"foo", 1};
        Object[] arguments2 = new Object[]{"bar", 2};

        when(invocation.getArguments()).thenReturn(arguments1);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertFalse("safelyArgumentsMatch() should not throw exception when arguments are different", invocationMatcher.safelyArgumentsMatch(arguments2));

        invocationMatcher.safelyArgumentsMatch(arguments2);
    }

    @Test
    public void testHasSameMethodWithSameMethods() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);

        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertTrue("invocation matchers should have the same method", invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethodWithDifferentMethods() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);

        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertFalse("invocation matchers should not have the same method", invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testToString() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(eq("foo"), is(1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String expected = print(printInvocation(invocation), matchers);

        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Location location = Mockito.mock(Location.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());

        when(invocation.getLocation()).thenReturn(location);

        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Object arg1 = "foo";
        Object arg2 = 1;
        Object[] rawArguments = new Object[]{arg1, arg2};
        List<Matcher> matchers = Arrays.asList(eq(arg1), capture(argCaptor1), capture(argCaptor2));

        when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[]{arg1, arg2});

        ArgumentCaptor<String> argCaptor1 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> argCaptor2 = ArgumentCaptor.forClass(Integer.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(argCaptor1).capture(eq(arg1));
        verify(argCaptor2).capture(eq(arg2));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(Mockito.mock(Invocation.class), Mockito.mock(Invocation.class));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, invocationMatchers.size());
        assertTrue(invocationMatchers.get(0) instanceof InvocationMatcher);
        assertTrue(invocationMatchers.get(1) instanceof InvocationMatcher);
    }

    private ArgumentCaptor<String> argCaptor1;
    private ArgumentCaptor<Integer> argCaptor2;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
}