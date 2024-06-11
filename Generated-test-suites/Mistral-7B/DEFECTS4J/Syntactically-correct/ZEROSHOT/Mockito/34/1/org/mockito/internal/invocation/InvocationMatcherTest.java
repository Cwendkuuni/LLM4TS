import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationWithArguments;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.stubbing.invocations.InvocationCall;
import org.mockito.internal.stubbing.invocations.InvocationMatcherImpl;
import org.mockito.internal.util.reflection.Reflections;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.internal.invocation.InvocationMatcher.hasSameMethod;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private Invocation invocationWithArguments;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcherWithMatchers;
    private InvocationMatcher invocationMatcherEmpty;
    private Invocation invocationWithArgumentsAndMatchers;
    private Invocation invocationWithDifferentMethod;
    private Invocation invocationWithDifferentMock;
    private Invocation invocationWithDifferentArguments;
    private Invocation invocationWithSameMethodButDifferentArguments;
    private Invocation invocationWithOverloadedMethodButSameArguments;
    private Invocation invocationWithOverloadedMethodButDifferentArguments;

    @Before
    public void setUp() {
        matchers = Arrays.asList(new ArgumentsMatcher(new Object[0]), new CapturesArguments());
        invocation = new InvocationCall(Reflections.class.getMethod("getDeclaredField", String.class), mock(Object.class));
        invocationWithArguments = new InvocationOnMock(Reflections.class.getMethod("getDeclaredField", String.class), new Object[]{"test"}, mock(Object.class));
        invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcherWithMatchers = new InvocationMatcher(invocation, matchers);
        invocationMatcherEmpty = new InvocationMatcher(invocation);
        invocationWithArgumentsAndMatchers = new InvocationMatcher(invocationWithArguments, matchers);
        invocationWithDifferentMethod = new InvocationMatcher(new InvocationCall(Reflections.class.getMethod("getDeclaredMethod", Class.class, Class[].class), mock(Object.class)));
        invocationWithDifferentMock = new InvocationMatcher(invocationWithArguments.getMock(), new InvocationCall(Reflections.class.getMethod("getDeclaredField", String.class), new Object[]{"test"}, mock(Object.class)));
        invocationWithDifferentArguments = new InvocationMatcher(invocationWithArguments, Collections.singletonList(new CapturesArguments()));
        invocationWithSameMethodButDifferentArguments = new InvocationMatcher(new InvocationCall(Reflections.class.getMethod("getDeclaredField", String.class), new Object[]{"test1"}, mock(Object.class)), matchers);
        invocationWithOverloadedMethodButSameArguments = new InvocationMatcher(new InvocationCall(Reflections.class.getMethod("getDeclaredField", String.class), new Object[]{"test"}, mock(Object.class)), new InvocationCall(Reflections.class.getMethod("getDeclaredField", int.class, String.class), new Object[]{1, "test"}, mock(Object.class)));
        invocationWithOverloadedMethodButDifferentArguments = new InvocationMatcher(new InvocationCall(Reflections.class.getMethod("getDeclaredField", String.class), new Object[]{"test"}, mock(Object.class)), new InvocationCall(Reflections.class.getMethod("getDeclaredField", int.class, String.class), new Object[]{2, "test1"}, mock(Object.class)));
    }

    @Test
    public void testGetMethod() {
        assertThat(invocationMatcher.getMethod(), is(notNullValue()));
    }

    @Test
    public void testGetInvocation() {
        assertThat(invocationMatcher.getInvocation(), is(invocation));
    }

    @Test
    public void testGetMatchers_EmptyList() {
        assertThat(invocationMatcherEmpty.getMatchers(), is(Collections.emptyList()));
    }

    @Test
    public void testGetMatchers_ProvidedList() {
        assertThat(invocationMatcherWithMatchers.getMatchers(), is(matchers));
    }

    @Test
    public void testMatches_SameInvocation() {
        verify(invocationMatcher).matches(invocationMatcher);
    }

    @Test
    public void testMatches_DifferentInvocationSameMethod() {
        verifyZeroInteractions(invocationMatcher);
        verifyZeroInteractions(invocationWithArgumentsAndMatchers);
        invocationMatcher.matches(invocationWithArgumentsAndMatchers);
    }

    @Test
    public void testMatches_DifferentInvocationDifferentMethod() {
        verifyZeroInteractions(invocationMatcher);
        verifyZeroInteractions(invocationWithDifferentMethod);
        invocationMatcher.matches(invocationWithDifferentMethod);
    }

    @Test(expected = AssertionError.class)
    public void testMatches_DifferentInvocationSameMethodButDifferentArguments() {
        verifyZeroInteractions(invocationMatcher);
        verifyZeroInteractions(invocationWithSameMethodButDifferentArguments);
        invocationMatcher.matches(invocationWithSameMethodButDifferentArguments);
    }

    @Test
    public void testMatches_DifferentInvocationSameMethodSameArguments() {
        verifyZeroInteractions(invocationMatcher);
        verifyZeroInteractions(invocationWithArgumentsAndMatchers);
        invocationMatcher.matches(invocationWithArgumentsAndMatchers);
    }

    @Test
    public void testMatches_DifferentInvocationSameMethodButOverloaded() {
        verifyZeroInteractions(invocationMatcher);
        verifyZeroInteractions(invocationWithOverloadedMethodButSameArguments);
        invocationMatcher.matches(invocationWithOverloadedMethodButSameArguments);
    }

    @Test
    public void testMatches_DifferentInvocationButOverloadedMethodDifferentArguments() {
        verifyZeroInteractions(invocationMatcher);
        verifyZeroInteractions(invocationWithOverloadedMethodButDifferentArguments);
        invocationMatcher.matches(invocationWithOverloadedMethodButDifferentArguments);
    }

    @Test
    public void testHasSameMethod_SameInvocations() {
        assertThat(hasSameMethod(invocationMatcher, invocationMatcher), is(true));
    }

    @Test
    public void testHasSameMethod_DifferentInvocationsSameMethod() {
        assertThat(hasSameMethod(invocationMatcher, invocationWithArgumentsAndMatchers), is(true));
    }

    @Test
    public void testHasSameMethod_DifferentInvocationsDifferentMethods() {
        assertThat(hasSameMethod(invocationMatcher, invocationWithDifferentMethod), is(false));
    }

    @Test
    public void testHasSameMethod_DifferentInvocationsSameMethodButDifferentArguments() {
        assertThat(hasSameMethod(invocationMatcher, invocationWithSameMethodButDifferentArguments), is(false));
    }

    @Test
    public void testHasSameMethod_DifferentInvocationsSameMethodButOverloaded() {
        assertThat(hasSameMethod(invocationMatcher, invocationWithOverloadedMethodButSameArguments), is(true));
    }

    @Test
    public void testHasSameMethod_DifferentInvocationsOverloadedMethodsDifferentArguments() {
        assertThat(hasSameMethod(invocationMatcher, invocationWithOverloadedMethodButDifferentArguments), is(false));
    }

    @Test
    public void testToString() {
        String expected = "InvocationMatcher[method=getDeclaredField(java.lang.String), argumentsToMatchers=[ArgumentsMatcher[this=mock: 652e5a88], CapturesArguments[this=mock: 68ab295c]]]";
        String actual = invocationMatcherWithMatchers.toString();
        assertThat(actual, is(expected));
    }

    @Test
    public void testToString_EmptyList() {
        String expected = "InvocationMatcher[method=getDeclaredField(java.lang.String)]";
        String actual = invocationMatcherEmpty.toString();
        assertThat(actual, is(expected));
    }

    @Test
    public void testCaptureArgumentsFrom_TestCaptureArgumentsMatcher() {
        Answer answer = mock(Answer.class);
        Object capturedArgument = new Object();

        invocationMatcherWithMatchers.captureArgumentsFrom(invocationWithArguments);
        verify(answer).answer(invocationWithArguments.getArguments()[0]);
        verify(answer).answer(capturedArgument);
    }

    @Test(expected = AssertionError.class)
    public void testCaptureArgumentsFrom_WrongArgumentsLength() {
        Answer answer = mock(Answer.class);

        invocationMatcherWithMatchers.captureArgumentsFrom(invocationWithArguments);
        verify(answer).answer(mock(Object.class)); // should fail since there is only one argument
    }
}