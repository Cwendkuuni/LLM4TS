import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Invocation mockInvocation;
    private Object mock;

    @Before
    public void setUp() {
        mock = mock(Object.class);
        mockInvocation = new InvocationOnMock(mock, new MethodTestMethod(), new Object[0]);
        matcher = new InvocationMatcher(mockInvocation);
    }

    @Test
    public void testInvocationMatcherWithEmptyMatchers() {
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher matcherWithEmptyMatchers = new InvocationMatcher(mockInvocation, matchers);

        Method method = mockInvocation.getMethod();
        List<Matcher> actualMatchers = matcherWithEmptyMatchers.getArgumentsToMatchers();

        assertThat(actualMatchers, is(matcher.getMatchers()));
        assertThat(method, is(matcherWithEmptyMatchers.getMethod()));
    }

    @Test
    public void testInvocationMatcherWithCustomMatchers() {
        Matcher<Integer> argMatcher = ArgumentsMatcher.eq(1);
        List<Matcher> customMatchers = Arrays.asList(argMatcher);

        InvocationMatcher matcherWithCustomMatchers = new InvocationMatcher(mockInvocation, customMatchers);

        Object[] arguments = new Object[]{1};

        matcherWithCustomMatchers.captureArgumentsFrom(mockInvocation);

        verify(mock).methodCall(mockInvocation.getMethod(), arguments[0]);
    }

    @Test(expected = AssertionError.class)
    public void testInvocationMatcherWithDifferentMethods() {
        Invocation mockInvocation2 = new InvocationOnMock(mock, new MethodTestMethod2(), new Object[0]);
        InvocationMatcher matcherWithDifferentMethods = new InvocationMatcher(mockInvocation2);

        InvocationMatcher matcher = new InvocationMatcher(mockInvocation);

        assertThat(matcher.matches(matcherWithDifferentMethods), is(false));
    }

    @Test
    public void testInvocationMatcherWithSameMethodButDifferentArguments() {
        Invocation mockInvocation2 = new InvocationOnMock(mock, mockInvocation.getMethod(), new Object[]{1});
        InvocationMatcher matcherWithSameMethodButDifferentArguments = new InvocationMatcher(mockInvocation2);

        InvocationMatcher matcher = new InvocationMatcher(mockInvocation);

        assertThat(matcher.matches(matcherWithSameMethodButDifferentArguments), is(false));
    }

    @Test
    public void testInvocationMatcherWithSameMethodAndSameArguments() {
        Object[] arguments = new Object[]{1};
        Invocation mockInvocation2 = new InvocationOnMock(mock, mockInvocation.getMethod(), arguments);
        InvocationMatcher matcherWithSameMethodAndSameArguments = new InvocationMatcher(mockInvocation2);

        InvocationMatcher matcher = new InvocationMatcher(mockInvocation);

        assertThat(matcher.matches(matcherWithSameMethodAndSameArguments), is(true));
    }

    @Test
    public void testToString() {
        String expectedToString = "InvocationMatcher [method=MethodTestMethod(public void testMethod()), invocation=InvocationOnMock [mock=MockMockito, method=MethodTestMethod, arguments=[], target=MockMockito]]";

        String actualToString = matcher.toString();

        assertThat(actualToString, is(expectedToString));
    }

    private class MethodTestMethod {
        public void testMethod() {
        }
    }

    private class MethodTestMethod2 {
        public void testMethod2() {
        }
    }

}