import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.stubs.MockitoStubs;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private Method method;
    private List<Matcher> matchers;
    private ArgumentsComparator argumentsComparator;

    @Before
    public void setup() {
        method = MockitoStubs.method(String.class, int.class, double.class);
        Object[] arguments = new Object[]{1, 2.0};
        invocation = MockitoStubs.invocation(method, arguments);
        matchers = Collections.singletonList(is(1));
        argumentsComparator = new ArgumentsComparator();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testNewInstanceWithMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertThat(invocationMatcher.getMatchers(), is(matchers));
        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMethod(), is(method));
    }

    @Test
    public void testNewInstanceWithoutMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertThat(invocationMatcher.getMatchers(), nullValue());
        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMethod(), is(method));
    }

    @Test
    public void testGetMethod() {
        assertThat(invocationMatcher.getMethod(), is(method));
    }

    @Test
    public void testGetInvocation() {
        assertThat(invocationMatcher.getInvocation(), is(invocation));
    }

    @Test
    public void testGetMatchers() {
        assertThat(invocationMatcher.getMatchers(), is(matchers));
    }

    @Test
    public void testToString() {
        String printSettingsString = "PrintSettings{outputTemplate=ToString, printStacktrace=false, printClassNames=false, printVerified=false}";
        String expectedToString = String.format("Invocation [%s %s %s]", method.getName(), invocation.getMock(), printSettingsString);
        assertThat(invocationMatcher.toString(), is(expectedToString));
    }

    @Test
    public void testMatches_sameInvocation() {
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, matchers);
        assertTrue(invocationMatcher.matches(invocationMatcher2));
    }

    @Test
    public void testMatches_differentInvocations_sameMethod_sameArguments() {
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, Collections.emptyList());
        Invocation invocation2 = MockitoStubs.invocation(method, arguments);
        assertTrue(invocationMatcher.matches(invocationMatcher2));
        assertTrue(invocationMatcher.matches(invocationMatcher2));
    }

    @Test
    public void testMatches_differentInvocations_differentMethod() {
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, Collections.emptyList());
        Invocation invocation2 = MockitoStubs.invocation(new Method(String.class, int.class, int.class), arguments);
        assertFalse(invocationMatcher.matches(invocationMatcher2));
    }

    @Test
    public void testMatches_differentInvocations_sameMethod_differentArguments() {
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, Collections.emptyList());
        Invocation invocation2 = MockitoStubs.invocation(method, new Object[]{1, 3.0});
        assertFalse(invocationMatcher.matches(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethod_sameInvocations() {
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, matchers);
        assertTrue(invocationMatcher.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethod_differentInvocations() {
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, Collections.emptyList());
        Invocation invocation2 = MockitoStubs.invocation(new Method(String.class, int.class, double.class), arguments);
        assertFalse(invocationMatcher.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethod() {
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, matchers);
        assertTrue(invocationMatcher.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, matchers);
        Object[] arguments2 = new Object[]{1, 2.0};
        assertTrue(invocationMatcher.safelyArgumentsMatch(arguments2));
    }

    @Test(expected = AssertionError.class)
    public void testSafelyArgumentsMatch_throwsException() {
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, matchers);
        Object[] arguments2 = new Object[]{1, "not a number"};
        invocationMatcher.safelyArgumentsMatch(arguments2);
    }

    @Test
    public void testCompareArguments() {
        Object[] arguments = new Object[]{1, 2.0};
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, matchers);
        assertTrue(argumentsComparator.argumentsMatch(invocationMatcher, invocation.getArguments()));
        assertTrue(argumentsComparator.argumentsMatch(invocationMatcher, arguments));
        assertFalse(argumentsComparator.argumentsMatch(invocationMatcher, new Object[]{1, 3.0}));
        assertFalse(argumentsComparator.argumentsMatch(invocationMatcher, new Object[]{1, "not a number"}));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        int expectedCapturedValue = 1;
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        when(capturesArguments.captureFrom((Object) anyObject())).thenReturn(expectedCapturedValue);
        invocationMatcher.captureArgumentsFrom(invocation);
        verify(capturesArguments).captureFrom(arguments[0]);
        assertEquals(expectedCapturedValue, capturesArguments.getValue());
    }
}