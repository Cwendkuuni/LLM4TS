import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.MethodInterceptor;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.util.collections.ArrayListUtil;
import org.mockito.internal.util.collections.LinkedHashSet;
import org.mockito.stub.Stubber;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private Stubber stubber;
    private Object mock;

    @Before
    public void setup() {
        mock = Mockito.mock(Object.class);
        stubber = mockito(mock);
    }

    @Test
    public void testDefaultConstruction() {
        Invocation invocation = createInvocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        Method method = invocation.getMethod();
        List<Matcher> matchers = matcher.getMatchers();

        assertThat(matchers, is(invocation.argumentsToMatchers()));
        assertThat(matcher.getMethod(), is(method));
        assertThat(matcher.getInvocation(), is(invocation));
    }

    @Test
    public void testCustomConstruction() {
        Invocation invocation = createInvocation();
        List<Matcher> customMatchers = ArrayListUtil.asList(ArgumentMatchers.eq("Test"), ArgumentMatchers.anyInt());
        InvocationMatcher matcher = new InvocationMatcher(invocation, customMatchers);

        Method method = invocation.getMethod();
        List<Matcher> matchers = matcher.getMatchers();

        assertThat(matchers, not(is(invocation.argumentsToMatchers())));
        assertThat(matchers, is(customMatchers));
        assertThat(matcher.getMethod(), is(method));
        assertThat(matcher.getInvocation(), is(invocation));
    }

    @Test
    public void testMatches() {
        Invocation invocation = createInvocation();
        List<Matcher> matchers = ArrayListUtil.asList(ArgumentMatchers.eq("Test"), ArgumentMatchers.anyInt());
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        InvocationOnMock actual = createInvocationWithSameMethod(invocation);
        Object[] actualArgs = actual.getArguments();

        when(mock.someMethod(ArgumentMatchers.eq("Test"), ArgumentMatchers.anyInt())).thenReturn("Result");

        matcher.captureArgumentsFrom(actual);

        assertThat(matcher.matches(actual), is(true));
    }

    @Test
    public void testMismatchedMethod() {
        Invocation invocation = createInvocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        InvocationOnMock actual = createInvocationWithDifferentMethod(invocation);

        assertThat(matcher.matches(actual), is(false));
    }

    @Test
    public void testDifferentMocks() {
        Invocation invocation1 = createInvocation();
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = createInvocation();
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertThat(matcher1.matches(matcher2), is(false));
    }

    @Test
    public void testToString() {
        Invocation invocation = createInvocation();
        List<Matcher> matchers = ArrayListUtil.asList(ArgumentMatchers.eq("Test"), ArgumentMatchers.anyInt());
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String expected = String.format("InvocationMatcher{method=%s, invocation=%s, matchers=[%s]}",
                invocation.getMethod().toGenericString(), invocation, matchers);

        assertThat(matcher.toString(), is(expected));
    }

    @Test
    public void testPrintingFriendlyInvocation() {
        Invocation invocation = createInvocation();
        List<Matcher> matchers = ArrayListUtil.asList(ArgumentMatchers.eq("Test"), ArgumentMatchers.anyInt());
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        PrintSettings printSettings = new PrintSettings();
        String expected = invocation.toString(matchers, printSettings);

        assertThat(matcher.toString(printSettings), is(expected));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        Invocation invocation = createInvocation();
        List<Matcher> matchers = ArrayListUtil.asList(argThat(new CaptureArgumentsMatcher()));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        InvocationOnMock actual = createInvocationWithSameMethod(invocation);
        Object[] actualArgs = actual.getArguments();

        matcher.captureArgumentsFrom(actual);

        Set<Object> capturedArguments = ((CaptureArguments) matchers.get(0)).getArguments();
        assertThat(capturedArguments, is(actualArgs[0]));
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation = createInvocation();
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(Collections.singletonList(invocation));

        InvocationMatcher matcher = matchers.get(0);

        Method method = invocation.getMethod();
        List<Matcher> matchersList = matcher.getMatchers();

        assertThat(matchersList, is(invocation.argumentsToMatchers()));
        assertThat(matcher.getMethod(), is(method));
        assertThat(matcher.getInvocation(), is(invocation));
    }

    private Invocation createInvocation() {
        Method method = getClass().getMethod("createInvocation", (Class[]) null);
        return (Invocation) method.invoke(this, (Object[]) null);
    }

    private Invocation createInvocationWithSameMethod(Invocation invocation) {
        Method method = invocation.getMethod();
        MethodInterceptor interceptor = mock(MethodInterceptor.class);
        when(interceptor.intercept(any(), any(), any(), any())).thenCallRealMethod();
        when(mock.method(method.getParameterTypes())).thenReturn(interceptor);

        return new InvocationOnMock(mock, method);
    }

    private Invocation createInvocationWithDifferentMethod(Invocation invocation) {
        Method method = getClass().getMethod("createInvocationWithDifferentMethod", (Class[]) null);
        return (Invocation) method.invoke(this, invocation.getMethod());
    }

    private static class CaptureArguments implements CapturesArguments {
        private Set<Object> arguments = new LinkedHashSet<>();

        @Override
        public void captureFrom(Object argument) {
            arguments.add(argument);
        }

        @Override
        public Set<Object> getArguments() {
            return arguments;
        }
    }

    private static class CaptureArgumentsMatcher implements Matcher<InvocationMatcher> {
        @Override
        public boolean matches(Object argument) {
            InvocationMatcher matcher = (InvocationMatcher) argument;
            InvocationMatcher capturingMatcher = new InvocationMatcher(createInvocation(), ArrayListUtil.asList(new CaptureArguments()));

            matcher.captureArgumentsFrom(capturingMatcher);

            return capturingMatcher.matches(matcher);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("an InvocationMatcher that captures arguments");
        }

        @Override
        public void describeMismatch(Object actual, Description description) {
            description.appendText("InvocationMatcher did not match, but no explanation is given because the method is designed to capture arguments, not to test them");
        }

        @Override
        public void describeNegatedMismatch(Object actual, Description description) {
            description.appendText("InvocationMatcher did not match, but no explanation is given because the method is designed to capture arguments, not to test them");
        }

        @Override
        public void describePerformed(Description description) {
            description.appendText("An InvocationMatcher that captures arguments");
        }
    }
}