import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private Object mock;
    private Stubber stubber;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setup() {
        mock = mock(Object.class);
        stubber = mock(Stubber.class);
    }

    @Test
    public void testInvocationMatcherWithEmptyList() {
        Invocation invocation = new InvocationOnMock(mock, new Method(getClass(), "testMethod"), new Object[0]);
        invocationMatcher = new InvocationMatcher(invocation);
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation);
        assertThat(invocationMatcher, is(expectedMatcher));
    }

    @Test
    public void testInvocationMatcherWithCustomMatchers() {
        Invocation invocation = new InvocationOnMock(mock, new Method(getClass(), "testMethod", Integer.TYPE), new Object[]{1});
        Matcher<Integer> integerMatcher = ArgumentMatchers.is(1);
        List<Matcher> customMatchers = Arrays.asList(integerMatcher);
        invocationMatcher = new InvocationMatcher(invocation, customMatchers);
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation, customMatchers);
        assertThat(invocationMatcher, is(expectedMatcher));
    }

    @Test
    public void testHasSameMethod() {
        Method method1 = new Method(getClass(), "testMethod", new Class[0]);
        Method method2 = new Method(getClass(), "testMethod", new Class[0]);
        Invocation invocation1 = new InvocationOnMock(mock, method1, new Object[0]);
        Invocation invocation2 = new InvocationOnMock(mock, method2, new Object[0]);

        invocationMatcher = new InvocationMatcher(invocation1);
        InvocationMatcher otherMatcher = new InvocationMatcher(invocation2);
        boolean result = invocationMatcher.hasSameMethod(otherMatcher);
        assertThat(result, is(true));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethods() {
        Method method1 = new Method(getClass(), "testMethod1", new Class[0]);
        Method method2 = new Method(getClass(), "testMethod2", new Class[0]);
        Invocation invocation1 = new InvocationOnMock(mock, method1, new Object[0]);
        Invocation invocation2 = new InvocationOnMock(mock, method2, new Object[0]);

        invocationMatcher = new InvocationMatcher(invocation1);
        InvocationMatcher otherMatcher = new InvocationMatcher(invocation2);
        boolean result = invocationMatcher.hasSimilarMethod(otherMatcher);
        assertThat(result, is(false));
    }

    @Test
    public void testMatchesInvocation() {
        Method method = new Method(getClass(), "testMethod", new Class[]{Integer.TYPE});
        Object[] arguments = new Object[]{1};
        Invocation invocation = new InvocationOnMock(mock, method, arguments);

        Matcher<Object> integerMatcher = ArgumentMatchers.is(1);
        List<Matcher> matchers = Arrays.asList(integerMatcher);
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation testInvocation = new InvocationOnMock(mock, method, arguments);
        boolean result = invocationMatcher.matches(testInvocation);
        assertThat(result, is(true));
    }

    @Test
    public void testMatchesInvocationDifferentArguments() {
        Method method = new Method(getClass(), "testMethod", new Class[]{Integer.TYPE});
        Object[] arguments = new Object[]{1};
        Invocation invocation = new InvocationOnMock(mock, method, arguments);

        Matcher<Object> integerMatcher = ArgumentMatchers.is(2);
        List<Matcher> matchers = Arrays.asList(integerMatcher);
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation testInvocation = new InvocationOnMock(mock, method, arguments);
        boolean result = invocationMatcher.matches(testInvocation);
        assertThat(result, is(false));
    }

    @Test
    public void testCaptureArgumentsFromInvocationVarArgs() {
        Method method = new Method(getClass(), "testMethodVarArgs", new Class[0]);
        Object[] arguments = new Object[]{"test1", "test2"};
        Invocation invocation = new InvocationOnMock(mock, method, arguments);

        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.is("test1"), ArgumentMatchers.is("test2"));
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        List<Matcher> capturedMatchers = Collections.emptyList();
        invocationMatcher.captureArgumentsFrom(invocation);
        capturedMatchers = invocationMatcher.getMatchers();

        Matcher<Object> firstCapturedMatcher = capturedMatchers.get(0);
        Matcher<Object> secondCapturedMatcher = capturedMatchers.get(1);

        assertThat(firstCapturedMatcher, is(ArgumentMatchers.is("test1")));
        assertThat(secondCapturedMatcher, is(ArgumentMatchers.is("test2")));
    }

    @Test
    public void testCaptureArgumentsFromInvocationNoVarArgs() {
        Method method = new Method(getClass(), "testMethodNoArgs", new Class[0]);
        Object[] arguments = new Object[]{1};
        Invocation invocation = new InvocationOnMock(mock, method, arguments);

        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.is(1));
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        List<Matcher> capturedMatchers = Collections.emptyList();
        invocationMatcher.captureArgumentsFrom(invocation);
        capturedMatchers = invocationMatcher.getMatchers();

        Matcher<Object> capturedMatcher = capturedMatchers.get(0);
        assertThat(capturedMatcher, is(ArgumentMatchers.is(1)));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(new InvocationOnMock(mock, new Method(getClass(), "testMethod"), new Object[0]),
                new InvocationOnMock(mock, new Method(getClass(), "testMethod", Integer.TYPE), new Object[]{1}));

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher firstMatcher = invocations.get(0).getInvocationMatcher();
        InvocationMatcher secondMatcher = invocations.get(1).getInvocationMatcher();

        assertThat(invocationMatchers.get(0), is(firstMatcher));
        assertThat(invocationMatchers.get(1), is(secondMatcher));
    }
}