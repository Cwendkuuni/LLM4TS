import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(org.mockito.runners.JUnit4.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithEmptyMatchers() {
        // given
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // when
        Method method = methodThat(isA(Method.class));
        List<Matcher> matchers = Collections.singletonList(method);
        InvocationMatcher withMatchers = new InvocationMatcher(invocation, matchers);

        // then
        assertThat(withMatchers.getMatchers(), is(sameInstance(matchers)));
    }

    @Test
    public void testInvocationMatcherWithMatchers() {
        // given
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("testMethod");
        List<Matcher> matchers = Arrays.asList(argumentCaptor(), is("expectedValue"));

        // when
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        // then
        assertThat(matcher.getMethod(), is(sameInstance(method)));
        assertThat(matcher.matches(new InvocationOnMock(invocation)), is(true));
    }

    @Test
    public void testInvocationMatcherWithDifferentMethod() {
        // given
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(method1.getName()).thenReturn("testMethod1");
        when(method2.getName()).thenReturn("testMethod2");

        // when
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, Arrays.asList(method1));
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, Arrays.asList(method2));

        // then
        assertThat(matcher1, is(not(sameInstance(matcher2))));
    }

    @Test
    public void testInvocationMatcherWithSameMethodAndSameArguments() {
        // given
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("testMethod");
        Object[] arguments = new Object[]{"testArgument1", "testArgument2"};
        when(invocation.getArguments()).thenReturn(arguments);

        // when
        InvocationMatcher matcher1 = new InvocationMatcher(invocation);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation);

        // then
        assertThat(matcher1, is(sameInstance(matcher2)));
        assertThat(matcher1.matches(matcher2), is(true));
    }

    @Test
    public void testInvocationMatcherWithSameMethodButDifferentArguments() {
        // given
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("testMethod");
        Object[] arguments1 = new Object[]{"testArgument1", "testArgument2"};
        Object[] arguments2 = new Object[]{"testArgument1", "testArgument3"};
        when(invocation.getArguments()).thenReturn(arguments1).thenReturn(arguments2);

        // when
        InvocationMatcher matcher1 = new InvocationMatcher(invocation);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation);

        // then
        assertThat(matcher1, is(not(sameInstance(matcher2))));
        assertThat(matcher1.matches(matcher2), is(false));
    }

    @Test
    public void testInvocationMatcherWithDifferentMethods() {
        // given
        Invocation invocation = mock(Invocation.class);
        Method method1 = mock(Method.class);
        when(method1.getName()).thenReturn("testMethod1");
        Method method2 = mock(Method.class);
        when(method2.getName()).thenReturn("testMethod2");

        // when
        InvocationMatcher matcher1 = new InvocationMatcher(invocation, Arrays.asList(method1));
        InvocationMatcher matcher2 = new InvocationMatcher(invocation, Arrays.asList(method2));

        // then
        assertThat(matcher1, is(not(sameInstance(matcher2))));
        assertThat(matcher1.matches(matcher2), is(false));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        // given
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(isA(String.class), any(Integer.class));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        String arg1 = "testArg1";
        Integer arg2 = 1;
        Object[] arguments = new Object[]{arg1, arg2};
        when(invocation.getArguments()).thenReturn(arguments);

        // when
        matcher.captureArgumentsFrom(invocation);

        // then
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> integerCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(matcher).captureArgumentsFrom(invocation);
        verify(matcher).captureArgumentsFrom(invocation);
        verify(matcher).matches(matcher);
        verify(matcher).matches(matcher);
        verify(matcher).getMatchers().get(0).matches(arg1);
        verify(matcher.getMatchers().get(1), stringCaptor.capture()).matches(arg2);
        String capturedArg1 = stringCaptor.getValue();
        verify(matcher.getMatchers().get(1), isA(CapturesArguments.class)).captureFrom(arg1);
        verify(matcher.getMatchers().get(1), isA(CapturesArguments.class)).captureFrom(invocation.getArgumentAt(0, Object.class));
        verify(matcher.getMatchers().get(1), isA(CapturesArguments.class)).setValue(capturedArg1);
        verify(matcher.getMatchers().get(1), isA(CapturesArguments.class)).getValue().equals(arg1);
        Integer capturedArg2 = (Integer) matcher.getMatchers().get(1).getValue();
        verify(matcher.getMatchers().get(1), isA(CapturesArguments.class)).captureFrom(arg2);
        verify(matcher.getMatchers().get(1), isA(CapturesArguments.class)).captureFrom(invocation.getArgumentAt(1, Object.class));
        verify(matcher.getMatchers().get(1), isA(CapturesArguments.class)).setValue(capturedArg2);
        verify(matcher.getMatchers().get(1), isA(CapturesArguments.class)).getValue().equals(arg2);
    }

    @Test
    public void testCreateFromInvocations() {
        // given
        List<Invocation> invocations = Arrays.asList(mock(Invocation.class), mock(Invocation.class));

        // when
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        // then
        assertThat(matchers.size(), is(equalTo(invocations.size())));
    }
}