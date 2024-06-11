import org.junit.Before;
import org.junit.Test;
import org.mockito.Arguments;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;

import java.lang.reflect.Method;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Mockito mockito;
    private Object mock;

    @Before
    public void setup() {
        mockito = new Mockito();
        mock = mockito.mock(Object.class);
    }

    @Test
    public void testInvocationMatcherCreationWithMatchers() {
        // given
        Invocation invocation = new InvocationOnMock(mockito, Mockito.method(Object.class, "testMethod", int.class, int.class), Arrays.asList(new Integer(1), new Integer(2)));
        List<Matcher> matchers = Arrays.asList(is(1), is(2));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // then
        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testInvocationMatcherCreationWithoutMatchers() {
        // given
        Invocation invocation = new InvocationOnMock(mockito, Mockito.method(Object.class, "testMethod", int.class), new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testInvocationMatcherMatchesSameMethod() {
        // given
        Invocation invocation = new InvocationOnMock(mockito, Mockito.method(Object.class, "testMethod", int.class), new Object[0]);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation);

        // then
        assertTrue(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcherDoesNotMatchDifferentMethod() {
        // given
        Invocation invocation1 = new InvocationOnMock(mockito, Mockito.method(Object.class, "testMethod1", int.class), new Object[0]);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = new InvocationOnMock(mockito, Mockito.method(Object.class, "testMethod2", int.class), new Object[0]);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // then
        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcherDoesNotMatchDifferentMocks() {
        // given
        Object mock1 = mockito.mock(Object.class);
        Object mock2 = mockito.mock(Object.class);
        Invocation invocation = new InvocationOnMock(mockito, Mockito.method(Object.class, "testMethod", int.class), new Object[0]);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new InvocationOnMock(mockito, invocation.getMethod(), mock1, new Object[0]));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mockito, invocation.getMethod(), mock2, new Object[0]));

        // then
        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcherDoesNotMatchUnverifiedInvocation() {
        // given
        Invocation invocation = new InvocationOnMock(mockito, Mockito.method(Object.class, "testMethod", int.class), new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation unverifiedInvocation = new InvocationOnMock(mockito, invocation.getMethod(), new Object[0]);

        // then
        assertFalse(invocationMatcher.matches(unverifiedInvocation));
    }

    @Test
    public void testInvocationMatcherCaptureArguments() {
        // given
        Invocation invocation = new InvocationOnMock(mockito, Mockito.method(Object.class, "testMethod", int.class, int.class), Arrays.asList(new Integer(1), new Integer(2)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = Arrays.asList(is(1), new CapturesArguments());

        // when
        invocationMatcher.captureArgumentsFrom(invocation);

        // then
        Matcher capturedArguments = matchers.get(1);
        ArgumentsMatcher argumentsMatcher = (ArgumentsMatcher) capturedArguments;
        Object[] capturedArgs = argumentsMatcher.getArguments();
        assertTrue(capturedArgs.length > 0);
        assertTrue(capturedArgs[0] instanceof Integer);
        assertTrue(capturedArgs[0].equals(invocation.getArguments()[0]));
    }

    @Test
    public void testInvocationMatcherHasSimilarMethodWithSameArguments() {
        // given
        Invocation invocation1 = new InvocationOnMock(mockito, Mockito.method(Object.class, "testMethod", int[].class), Arrays.asList(new Integer[0]));
        Invocation invocation2 = new InvocationOnMock(mockito, Mockito.method(Object.class, "testMethod", int[].class), Arrays.asList(new Integer[0]));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // then
        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcherHasSimilarMethodWithDifferentArguments() {
        // given
        Invocation invocation1 = new InvocationOnMock(mockito, Mockito.method(Object.class, "testMethod", int.class, int.class), Arrays.asList(new Integer(1), new Integer(2)));
        Invocation invocation2 = new InvocationOnMock(mockito, Mockito.method(Object.class, "testMethod", int.class, int.class), Arrays.asList(new Integer(1), new Integer(3)));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // then
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcherHasSameMethodWithDifferentParameterTypes() {
        // given
        Invocation invocation1 = new InvocationOnMock(mockito, Mockito.method(Object.class, "testMethod1", int.class), new Object[0]);
        Invocation invocation2 = new InvocationOnMock(mockito, Mockito.method(Object.class, "testMethod2", long.class), new Object[0]);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // then
        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcherHasSameMethodWithSameParameterTypes() {
        // given
        Invocation invocation1 = new InvocationOnMock(mockito, Mockito.method(Object.class, "testMethod", int.class), new Object[0]);
        Invocation invocation2 = new InvocationOnMock(mockito, Mockito.method(Object.class, "testMethod", int.class), new Object[0]);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // then
        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcherToString() {
        // given
        Invocation invocation = new InvocationOnMock(mockito, Mockito.method(Object.class, "testMethod", int.class), new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        String print = new PrintSettings().print(invocationMatcher.getMatchers(), invocationMatcher);
        assertTrue(print.contains("testMethod"));
        assertTrue(print.contains("Object"));
    }
}