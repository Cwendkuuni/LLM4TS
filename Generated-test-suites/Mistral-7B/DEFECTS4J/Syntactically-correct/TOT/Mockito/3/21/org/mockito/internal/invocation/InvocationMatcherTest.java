import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.InvocationOnMockImpl;
import org.mockito.stub.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    @Test
    void testInvocationMatcher_emptyMatcherList() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = Collections.emptyList();

        List<Matcher> actualMatchers = matcher.getMatchers();
        Assertions.assertEquals(matchers, actualMatchers);

        Method method = mock(Method.class);
        Assertions.assertFalse(matcher.matches(new InvocationMatcher(new InvocationOnMockImpl(method, null), matchers)));
    }

    @Test
    void testInvocationMatcher_singleMatcher() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Object[] arguments = new Object[]{1, "two"};
        ArgumentCaptor<Integer> integerCaptor = ArgumentCaptor.forClass(Integer.class);

        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), integerCaptor);
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Object[] invocationArguments = invocation.getArguments();
        matcher.captureArgumentsFrom(invocation);
        Integer capturedArgument = integerCaptor.getValue();

        Assertions.assertArrayEquals(arguments, invocationArguments);
        Assertions.assertEquals(arguments[0], capturedArgument);

        Method method = mock(Method.class);
        Assertions.assertTrue(matcher.matches(new InvocationMatcher(new InvocationOnMockImpl(method, null), matchers)));
    }

    @Test
    void testInvocationMatcher_multipleMatchers() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Object[] arguments = new Object[]{1, "two", 3.0};

        List<Matcher> matchers = Arrays.asList(
                ArgumentMatchers.eq(1),
                ArgumentMatchers.eq("two"),
                ArgumentMatchers.instanceOf(Number.class)
        );

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        matcher.captureArgumentsFrom(invocation);

        Assertions.assertArrayEquals(arguments, invocation.getArguments());

        Method method = mock(Method.class);
        Assertions.assertTrue(matcher.matches(new InvocationMatcher(new InvocationOnMockImpl(method, null), matchers)));
    }

    @Test
    void testInvocationMatcher_doesNotMatch() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Object[] arguments = new Object[]{1, "two"};

        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq("three"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Object[] invocationArguments = invocation.getArguments();
        matcher.captureArgumentsFrom(invocation);

        Assertions.assertArrayEquals(arguments, invocationArguments);
        Assertions.assertFalse(matcher.matches(new InvocationMatcher(invocation, Collections.emptyList())));
    }

    @Test
    void testInvocationMatcher_argumentsComparison() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Object[] arguments = new Object[]{1, "two"};

        List<Matcher> matchers = Arrays.asList(
                ArgumentMatchers.eq(1),
                new ArgumentMatcher<Object>() {
                    @Override
                    public boolean matches(Object argument) {
                        return argument.equals("two");
                    }
                }
        );

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        matcher.captureArgumentsFrom(invocation);

        Object[] invocationArguments = invocation.getArguments();
        Assertions.assertArrayEquals(arguments, invocationArguments);

        MatcherDecorator decorator = new MatcherDecorator(matchers.get(1));
        matcher = new InvocationMatcher(invocation, Collections.singletonList(decorator));
        Assertions.assertTrue(matcher.matches(new InvocationMatcher(invocation, matchers)));
    }

    @Test
    void testInvocationMatcher_varargs() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Object[] arguments = new Object[]{1, 2, "three"};

        List<Matcher> matchers = Arrays.asList(
                ArgumentMatchers.eq(1),
                ArgumentMatchers.instanceOf(Number.class)
        );

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        matcher.captureArgumentsFrom(invocation);

        Assertions.assertArrayEquals(arguments, invocation.getArguments());

        Method method = mock(Method.class);
        Assertions.assertTrue(matcher.matches(new InvocationMatcher(new InvocationOnMockImpl(method, null), matchers)));
    }
}