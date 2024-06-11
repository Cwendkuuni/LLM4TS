import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithEmptyMatcherList() {
        Object mockObject = Mockito.mock(Object.class);
        Invocation invocation = mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = invocation.getMethod();

        assertThat(invocationMatcher.getMethod(), is(sameInstance(method)));
        assertThat(invocationMatcher.getInvocation(), is(sameInstance(invocation)));
        assertThat(invocationMatcher.getMatchers(), is(empty()));
        assertThat(invocationMatcher.toString(), is(notNullValue()));
        assertThat(invocationMatcher.matches(invocation), is(true));
    }

    @Test
    public void testInvocationMatcherWithMatchers() {
        Object mockObject = Mockito.mock(Object.class, stricterSettings());
        Invocation invocation = mock(Invocation.class);
        Matcher matcher1 = ArgumentMatchers.isA(Integer.class);
        Matcher matcher2 = ArgumentMatchers.eq(123);

        List<Matcher> matchers = Arrays.asList(matcher1, matcher2);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = invocation.getMethod();

        assertThat(invocationMatcher.getMethod(), is(sameInstance(method)));
        assertThat(invocationMatcher.getInvocation(), is(sameInstance(invocation)));
        assertThat(invocationMatcher.getMatchers(), is(sameInstance(matchers)));
        assertThat(invocationMatcher.toString(), is(notNullValue()));
        verify(mockObject).methodWithArguments(matcher1.getValue(), matcher2.getValue());
    }

    @Test
    public void testInvocationMatcherMatches() {
        Object mockObject = Mockito.mock(Object.class, stricterSettings());
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocation.getMethod();
        Object[] arguments = new Object[]{1, 2, 3};

        InvocationMatcher testMatcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.any(), ArgumentMatchers.any(Integer.class), ArgumentMatchers.any(Long.class)));

        assertThat(invocationMatcher.matches(invocation), is(true));
        assertThat(invocationMatcher.matches(invocationWithArguments(method, arguments)), is(true));
        assertThat(testMatcher.matches(invocationWithArguments(method, arguments)), is(true));
    }

    @Test
    public void testInvocationMatcherDoesNotMatchDifferentMethod() {
        Object mockObject = Mockito.mock(Object.class, stricterSettings());
        Invocation invocation = mock(Invocation.class);
        Invocation invocationWithDifferentMethod = mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method1 = invocation.getMethod();
        Method method2 = invocationWithDifferentMethod.getMethod();

        assertThat(invocationMatcher.matches(invocation), is(true));
        assertThat(invocationMatcher.matches(invocationWithDifferentMethod), is(false));
    }

    @Test
    public void testInvocationMatcherDoesNotMatchDifferentMock() {
        Object mockObject1 = Mockito.mock(Object.class, stricterSettings());
        Object mockObject2 = Mockito.mock(Object.class, stricterSettings());
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());

        assertThat(invocationMatcher.matches(invocation), is(true));
        assertThat(invocationMatcher.matches(new InvocationOnMock(mockObject2)), is(false));
    }

    @Test
    public void testInvocationMatcherCaptureArguments() {
        Object mockObject = Mockito.mock(Object.class, stricterSettings());
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Matcher matcher = ArgumentMatchers.argThat(new MyArgumentMatcher());
        invocationMatcher.captureArgumentsFrom(invocationWithArguments(invocation.getMethod(), new Object[]{1, 2, 3}));

        assertThat(matcher.getValue(), is(oneOf(1, 2, 3)));
    }

    @Test
    public void testInvocationMatcherWithVarargs() {
        Object mockObject = Mockito.mock(Object.class, stricterSettings());
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocation.getMethod();
        Object[] arguments = new Object[]{1, 2, 3, 4, 5};

        Matcher varargMatcher = ArgumentMatchers.arrayContaining(ArgumentMatchers.isA(Integer.class));
        InvocationMatcher testMatcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.any(), varargMatcher));

        assertThat(invocationMatcher.matches(invocationWithArguments(method, arguments)), is(true));
        assertThat(testMatcher.matches(invocationWithArguments(method, arguments)), is(true));
    }

    private Invocation invocationWithArguments(Method method, Object[] arguments) {
        try {
            Invocation invocation = Mockito.spy(Mockito.mock(Invocation.class));
            Mockito.doReturn(method).when(invocation).getMethod();
            Mockito.doReturn(arguments).when(invocation).getArguments();
            return invocation;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Matcher<Object> oneOf(Object... values) {
        return new VarargMatcher<Object>(Arrays.asList(values));
    }

    private static Matcher<Object> isOneOf(Object... values) {
        return ArgumentMatchers.isA(isA(Object[].class)).and(new VarargMatcher<Object>(Arrays.asList(values)));
    }

    private static Strictness stricterSettings() {
        return Strictness.STRICT_STUBS;
    }

    private static class MyArgumentMatcher implements ArgumentMatcher<Object> {
        @Override
        public boolean matches(Object argument) {
            return argument instanceof Integer && ((Integer) argument) > 0;
        }
    }
}