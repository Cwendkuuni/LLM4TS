import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mockitoSession;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {

    private List<Matcher> matchers;
    private Invocation invocation;
    private InvocationMatcher invocationMatcher;
    private Object mock;

    @Before
    public void setUp() throws Exception {
        matchers = Arrays.asList(
                ArgumentMatchers.isA(String.class),
                ArgumentMatchers.isNull(),
                ArgumentMatchers.eq("test"),
                ArgumentMatchers.notNullValue(),
                ArgumentMatchers.instanceOf(List.class)
        );

        mock = mock(MockitoTestClass.class);
        invocation = new InvocationOnMock(mock, new MethodTestClass(), new Object[0]);
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testSameInvocationMatches() {
        Invocation testInvocation = new InvocationOnMock(mock, new MethodTestClass(), new Object[0]);

        assertThat(invocationMatcher.matches(testInvocation), is(true));
    }

    @Test
    public void testDifferentInvocationDoesNotMatch() {
        Invocation differentInvocation = new InvocationOnMock(mock, new OtherMethodTestClass(), new Object[0]);

        assertThat(invocationMatcher.matches(differentInvocation), is(false));
    }

    @Test
    public void testNullInvocationDoesNotMatch() {
        Invocation nullInvocation = null;

        assertThat(invocationMatcher.matches(nullInvocation), is(false));
    }

    @Test
    public void testEmptyMatcherListMatchesAllInvocations() {
        Invocation emptyInvocation = new InvocationOnMock(mock, new MethodTestClass(), new Object[0]);
        InvocationMatcher emptyMatcherInvocationMatcher = new InvocationMatcher(emptyInvocation);

        List<Invocation> invocations = Arrays.asList(emptyInvocation, invocation, new InvocationOnMock(mock, new MethodTestClass(), new Object[0]));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        for (InvocationMatcher invocationMatcherFromList : invocationMatchers) {
            assertThat(invocationMatcherFromList.matches(invocationMatcher), is(true));
        }
    }

    @Test
    public void testEmptyMatcherListMatchesNullInvocation() {
        InvocationMatcher emptyMatcherInvocationMatcher = new InvocationMatcher(mock);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(Collections.emptyList());

        assertThat(emptyMatcherInvocationMatcher.matches(invocationMatcher), is(true));
    }

    @Test
    public void testMatcherWithCapturesArguments() {
        given(mock.someMethod()).willReturn("test");

        matchers.set(2, new CapturesArguments() {
            @Override
            public void captureFrom(Invocation invocation) {
                capturedArgument = invocation.getArgumentAt(1, String.class);
            }
        });

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(mock).someMethod();
        verify(mock).someMethod("test");
        assertThat(capturedArgument, is("test"));
    }

    @Test
    public void testVarArgsMatcher() {
        given(mock.testMethod(1, 2, "test")).willReturn("test");

        matchers.set(2, ArgumentMatchers.contains("test"));

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(mock).testMethod(1, 2, "test");
        verifyNoMoreInteractions(mock);
        assertThat(Arrays.asList(invocation.getArguments()), is(Arrays.asList(1, 2, "test")));
        assertThat(Arrays.asList(invocationMatcher.getMatchers().get(2).getValue()), is(Arrays.asList("test")));
    }

    @Test
    public void testToString() {
        String expected = String.format("InvocationMatcher%n" +
                "invocation = %s%n" +
                "matchers = [%s, %s, %s, %s, %s]", invocation, matchers.get(0), matchers.get(1), matchers.get(2), matchers.get(3), matchers.get(4));
        assertThat(invocationMatcher.toString(), is(expected));
    }
}

class MockitoTestClass {
    public String someMethod() {
        return "test";
    }
}

class OtherMethodTestClass {
    public void someOtherMethod() {
    }
}

class MethodTestClass {
    public String testMethod(String arg1, int arg2, String arg3) {
        return arg3;
    }
}