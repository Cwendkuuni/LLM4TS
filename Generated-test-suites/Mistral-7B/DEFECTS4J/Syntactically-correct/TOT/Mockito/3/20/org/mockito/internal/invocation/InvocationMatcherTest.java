import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private Stubber stubber;
    private InvocationMatcher matcher;

    @Before
    public void setUp() {
        stubber = mock(Stubber.class);
    }

    @Test
    public void testInvocationMatcherWithEmptyMatchers() {
        // given
        Invocation invocation = mock(InvocationOnMock.class);

        // when
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // then
        List<Matcher> matchers = matcher.getMatchers();
        assertThat(matchers, is(Collections.emptyList()));
    }

    @Test
    public void testInvocationMatcherWithSpecifiedMatchers() {
        // given
        Invocation invocation = mock(InvocationOnMock.class);
        Matcher<Integer> argMatcher = argThat(isA(Integer.class));
        Matcher<String> stringMatcher = argThat(isInstanceOf(String.class));

        List<Matcher> matchers = Arrays.asList(argMatcher, stringMatcher);

        // when
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        // then
        List<Matcher> actualMatchers = matcher.getMatchers();
        assertThat(actualMatchers, is(equalTo(matchers)));
    }

    @Test
    public void testInvocationMatcherMatches() {
        // given
        Invocation invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(stubber.createMock(Object.class));
        when(invocation.getMethod()).thenReturn(new Method(InvocationMatcher.class.getDeclaredMethod("toString")));
        when(invocation.getArguments()).thenReturn(new Object[]{1, "test"});

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        Invocation testInvocation = mock(InvocationOnMock.class);
        when(testInvocation.getMock()).thenReturn(stubber.createMock(Object.class));
        when(testInvocation.getMethod()).thenReturn(new Method(InvocationMatcher.class.getDeclaredMethod("toString")));
        when(testInvocation.getArguments()).thenReturn(new Object[]{1, "test"});

        // when
        boolean result = matcher.matches(testInvocation);

        // then
        verify(invocation).getMock();
        verify(testInvocation).getMock();
        verifyNoMoreInteractions(stubber);
        verify(invocation).isVerified();
        verify(testInvocation).isVerified();
        assertTrue(result);
    }

    @Test
    public void testInvocationMatcherDoesNotMatch() {
        // given
        Invocation invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(stubber.createMock(Object.class));
        when(invocation.getMethod()).thenReturn(new Method(InvocationMatcher.class.getDeclaredMethod("toString")));
        when(invocation.getArguments()).thenReturn(new Object[]{1, "test"});

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        Invocation testInvocation = mock(InvocationOnMock.class);
        when(testInvocation.getMock()).thenReturn(stubber.createMock(Object.class));
        when(testInvocation.getMethod()).thenReturn(new Method(InvocationMatcher.class.getDeclaredMethod("toString"), new Class[]{String.class}));
        when(testInvocation.getArguments()).thenReturn(new Object[]{2, "test2"});

        // when
        boolean result = matcher.matches(testInvocation);

        // then
        verify(invocation).getMock();
        verify(testInvocation).getMock();
        verifyNoMoreInteractions(stubber);
        verify(invocation).isVerified();
        verify(testInvocation).isVerified();
        assertFalse(result);
    }

    @Test
    public void testInvocationMatcherWithVarargs() {
        // given
        Invocation invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(stubber.createMock(Object.class));
        when(invocation.getMethod()).thenReturn(new Method(InvocationMatcher.class.getDeclaredMethod("testWithVarargs", String[].class)));
        when(invocation.getRawArguments()).thenReturn(new Object[]{new Object[]{"test1", "test2"}});

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        Invocation testInvocation = mock(InvocationOnMock.class);
        when(testInvocation.getMock()).thenReturn(stubber.createMock(Object.class));
        when(testInvocation.getMethod()).thenReturn(new Method(InvocationMatcher.class.getDeclaredMethod("testWithVarargs", String[].class)));
        when(testInvocation.getRawArguments()).thenReturn(new Object[]{new Object[]{"test3", "test4"}});

        Answer<Object> answer = answer("testResult");
        doAnswer(answer).when(stubber).verifyNoMoreInteractions();

        // when
        matcher.captureArgumentsFrom(testInvocation);

        // then
        verify(invocation).getMock();
        verify(testInvocation).getMock();
        verify(testInvocation).getRawArguments();
        verifyNoMoreInteractions(stubber);
        verify(invocation).isVerified();
        verify(testInvocation).isVerified();
        assertThat(matcher.getMatchers().get(0), is(instanceOf(org.mockito.internal.matchers.ArgumentsCaptor.class)));
    }
}