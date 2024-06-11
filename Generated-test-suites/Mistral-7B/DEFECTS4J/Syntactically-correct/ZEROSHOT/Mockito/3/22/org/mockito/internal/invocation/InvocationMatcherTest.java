package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher_whenEmptyListPassed_thenArgumentsAreProcessed() {
        // given
        Invocation mockInvocation = Mockito.mock(InvocationOnMock.class);
        Object[] mockArguments = new Object[]{1, 2, 3};

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, Arrays.asList(Matchers.anyInt()));

        // then
        assertThat(invocationMatcher.getMatchers().size(), is(1));
        assertThat(invocationMatcher.getMatchers().get(0), is(Matchers.anyInt()));
        assertThat(invocationMatcher.matches(mockInvocation), is(true));
        invocationMatcher.captureArgumentsFrom(mockInvocation);
        ArgumentCaptor<Integer> integerCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(mockInvocation).getArguments();
        verify(mockInvocation).getArgumentAt(Matchers.anyInt(), Matchers.equalTo(Integer.class));
        verify(mockInvocation).getRawArguments();
        verify(mockInvocation).getArgumentAt(Matchers.eq(0), Matchers.instanceOf(Integer.class));
        verify(mockInvocation).getArgumentAt(Matchers.eq(1), Matchers.instanceOf(Integer.class));
        verify(mockInvocation).getArgumentAt(Matchers.eq(2), Matchers.instanceOf(Integer.class));
        assertThat(Arrays.asList(invocationMatcher.getArguments()), is(Arrays.asList(1, 2, 3)));
    }

    @Test
    public void testInvocationMatcher_whenCustomListPassed_thenArgumentsAreProcessed() {
        // given
        Invocation mockInvocation = Mockito.mock(InvocationOnMock.class);
        Object[] mockArguments = new Object[]{1, 2, 3};
        List<Matcher> customMatchers = Arrays.asList(Matchers.argThat(new MyCustomMatcher()));

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, customMatchers);

        // then
        assertThat(invocationMatcher.getMatchers().size(), is(1));
        assertThat(invocationMatcher.getMatchers().get(0), is(new MyCustomMatcher()));
        assertThat(invocationMatcher.matches(mockInvocation), is(true));
        invocationMatcher.captureArgumentsFrom(mockInvocation);
        // ... rest of the assertions would depend on MyCustomMatcher
    }

    @Test
    public void testInvocationMatcher_whenNoMatchersPassed_thenArgumentsAreProcessed() {
        // given
        Invocation mockInvocation = Mockito.mock(InvocationOnMock.class);
        Object[] mockArguments = new Object[]{1, 2, 3};

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);

        // then
        assertThat(invocationMatcher.getMatchers().size(), is(0));
        assertThat(invocationMatcher.matches(mockInvocation), is(true));
        invocationMatcher.captureArgumentsFrom(mockInvocation);
        verify(mockInvocation).getArguments();
        verify(mockInvocation).getArgumentAt(Matchers.anyInt(), Matchers.equalTo(Object.class));
        verify(mockInvocation).getRawArguments();
        verify(mockInvocation).getArgumentAt(Matchers.eq(0), Matchers.instanceOf(Object.class));
        verify(mockInvocation).getArgumentAt(Matchers.eq(1), Matchers.instanceOf(Object.class));
        verify(mockInvocation).getArgumentAt(Matchers.eq(2), Matchers.instanceOf(Object.class));
        assertThat(Arrays.asList(invocationMatcher.getArguments()), is(Arrays.asList(1, 2, 3)));
    }

    @Test
    public void testHasSameMethod() {
        // given
        Invocation mockInvocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation mockInvocation2 = Mockito.mock(InvocationOnMock.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(mockInvocation1.getMethod()).thenReturn(method1);
        when(mockInvocation2.getMethod()).thenReturn(method2);

        // when
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mockInvocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockInvocation2);

        // then
        assertThat(invocationMatcher1.hasSameMethod(invocationMatcher2), is(false));

        // given
        when(method1.getName()).thenReturn("testMethod");
        when(method2.getName()).thenReturn("testMethod");
        when(method1.getParameterTypes()).thenReturn(new Class[]{Integer.class, String.class});
        when(method2.getParameterTypes()).thenReturn(new Class[]{String.class, Integer.class});

        // when
        InvocationMatcher invocationMatcher3 = new InvocationMatcher(mockInvocation1);
        InvocationMatcher invocationMatcher4 = new InvocationMatcher(mockInvocation2);

        // then
        assertThat(invocationMatcher3.hasSameMethod(invocationMatcher4), is(false));

        // given
        when(method1.getParameterTypes()).thenReturn(new Class[]{Object.class, Object.class});
        when(method2.getParameterTypes()).thenReturn(new Class[]{Object.class, Object.class});

        // when
        InvocationMatcher invocationMatcher5 = new InvocationMatcher(mockInvocation1);
        InvocationMatcher invocationMatcher6 = new InvocationMatcher(mockInvocation2);

        // then
        assertThat(invocationMatcher5.hasSameMethod(invocationMatcher6), is(true));
    }

    @Test
    public void testToString() {
        // given
        Invocation mockInvocation = Mockito.mock(InvocationOnMock.class);
        Object[] mockArguments = new Object[]{1, 2, 3};
        List<Matcher> matchers = Arrays.asList(Matchers.anyInt());

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, matchers);

        // then
        String result = invocationMatcher.toString();

        // then
        String expected = String.format(
                "InvocationMatcher%n" +
                        "invocation: %s%n" +
                        "matchers: [%s]",
                mockInvocation.toString(), matchers.toString());
        assertThat(result, is(expected));
    }

    @Test
    public void testMatches() {
        // given
        Invocation mockInvocation = Mockito.mock(InvocationOnMock.class);
        Object[] mockArguments = new Object[]{1, 2, 3};
        List<Matcher> matchers = Arrays.asList(Matchers.anyInt());

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, matchers);

        // then
        Invocation mockInvocation2 = Mockito.mock(InvocationOnMock.class);
        Object[] mockArguments2 = new Object[]{1, 2, 3};
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockInvocation2, matchers);
        InvocationMatcher invocationMatcher3 = new InvocationMatcher(mockInvocation, Collections.emptyList());

        // then
        assertThat(invocationMatcher.matches(invocationMatcher), is(true));
        assertThat(invocationMatcher.matches(invocationMatcher2), is(true));
        assertThat(invocationMatcher.matches(invocationMatcher3), is(false));

        // given
        Invocation mockInvocation4 = Mockito.mock(InvocationOnMock.class);
        Object[] mockArguments4 = new Object[]{1, "hello", 3};

        // when
        InvocationMatcher invocationMatcher4 = new InvocationMatcher(mockInvocation4, matchers);

        // then
        assertThat(invocationMatcher.matches(invocationMatcher4), is(false));

        // given
        when(mockInvocation.getMock()).thenReturn(Mockito.mock(Object.class));
        when(mockInvocation2.getMock()).thenReturn(Mockito.mock(Object.class));
        when(mockInvocation3.getMock()).thenReturn(Mockito.mock(Object.class));
        when(mockInvocation4.getMock()).thenReturn(Mockito.mock(Object.class));

        // then
        assertThat(invocationMatcher.matches(invocationMatcher), is(true));
        assertThat(invocationMatcher.matches(invocationMatcher2), is(true));
        assertThat(invocationMatcher.matches(invocationMatcher3), is(false));
        assertThat(invocationMatcher.matches(invocationMatcher4), is(false));

        // given
        when(mockInvocation.getMethod()).thenReturn(mock(Method.class));
        when(mockInvocation2.getMethod()).thenReturn(mock(Method.class));
        when(mockInvocation3.getMethod()).thenReturn(mock(Method.class));
        when(mockInvocation4.getMethod()).thenReturn(mock(Method.class));

        // when
        InvocationMatcher invocationMatcher5 = new InvocationMatcher(mockInvocation, matchers);
        InvocationMatcher invocationMatcher6 = new InvocationMatcher(mockInvocation2, matchers);

        // then
        when(mockInvocation.getLocation()).thenReturn(Mockito.mock(Location.class));
        when(mockInvocation2.getLocation()).thenReturn(Mockito.mock(Location.class));

        // then
        InvocationMatcher invocationMatcher7 = new InvocationMatcher(mockInvocation, matchers);
        InvocationMatcher invocationMatcher8 = new InvocationMatcher(mockInvocation2, matchers);

        // then
        InvocationMatcher invocationMatcher9 = new InvocationMatcher(mockInvocation, matchers);
        InvocationMatcher invocationMatcher10 = new InvocationMatcher(mockInvocation2, matchers);

        // then
        InvocationMatcher invocationMatcher11 = new InvocationMatcher(mockInvocation, Collections.singletonList(Matchers.anyInt()));
        InvocationMatcher invocationMatcher12 = new InvocationMatcher(mockInvocation2, Collections.singletonList(Matchers.anyInt()));

        // then
        List<InvocationMatcher> invocationMatchers = Arrays.asList(invocationMatcher5, invocationMatcher6, invocationMatcher7, invocationMatcher8, invocationMatcher9, invocationMatcher10, invocationMatcher11, invocationMatcher12);
        List<InvocationMatcher> expectedInvocationMatchers = InvocationMatcher.createFrom(Arrays.asList(mockInvocation, mockInvocation2, mockInvocation, mockInvocation2, mockInvocation, mockInvocation2, mockInvocation, mockInvocation2));

        // then
        assertThat(invocationMatchers, is(expectedInvocationMatchers));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // given
        Invocation mockInvocation = Mockito.mock(InvocationOnMock.class);
        Object[] mockArguments = new Object[]{1, 2, 3};
        when(mockInvocation.getArguments()).thenReturn(mockArguments);
        when(mockInvocation.getRawArguments()).thenReturn(mockArguments);

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        ArgumentCaptor<Integer> integerCaptor = ArgumentCaptor.forClass(Integer.class);

        // then
        invocationMatcher.captureArgumentsFrom(mockInvocation);
        verify(mockInvocation).getArguments();
        verify(mockInvocation).getRawArguments();
        verify(mockInvocation).getArgumentAt(Matchers.anyInt(), Matchers.instanceOf(Object.class));
        verify(mockInvocation).getArgumentAt(Matchers.anyInt(), Matchers.sameInstance(mockArguments[0]));
        verify(mockInvocation).getArgumentAt(Matchers.anyInt(), Matchers.sameInstance(mockArguments[1]));
        verify(mockInvocation).getArgumentAt(Matchers.anyInt(), Matchers.sameInstance(mockArguments[2]));
        verify(mockInvocation, Mockito.times(3)).getArgumentAt(Matchers.anyInt(), Matchers.instanceOf(Object.class));

        // given
        when(mockInvocation.getMethod()).thenReturn(mock(Method.class));

        // then
        invocationMatcher.captureArgumentsFrom(mockInvocation);
        verify(mockInvocation).getArguments();
        verify(mockInvocation).getRawArguments();
        verify(mockInvocation, Mockito.times(1)).getMethod();
        verify(mockInvocation, Mockito.times(3)).getArgumentAt(Matchers.anyInt(), Matchers.instanceOf(Object.class));

        // given
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockInvocation, Arrays.asList(Matchers.anyInt()));

        // when
        invocationMatcher2.captureArgumentsFrom(mockInvocation);

        // then
        verify(mockInvocation, Mockito.times(1)).getArguments();
        verify(mockInvocation, Mockito.times(1)).getRawArguments();
        verify(mockInvocation, Mockito.times(1)).getMethod();
        ArgumentCaptor<Integer> integerCaptor2 = ArgumentCaptor.forClass(Integer.class);
        verify(mockInvocation).getArgumentAt(0, Matchers.equalTo(integerCaptor2.capture()));
        verify(mockInvocation).getArgumentAt(1, Matchers.equalTo(integerCaptor2.capture()));
        verify(mockInvocation).getArgumentAt(2, Matchers.equalTo(integerCaptor2.capture()));
        List<Integer> capturedValues = integerCaptor2.getAllValues();
        assertThat(capturedValues, is(Arrays.asList(1, 2, 3)));
    }

    @Test
    public void testCreateFrom() {
        // given
        Invocation mockInvocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation mockInvocation2 = Mockito.mock(InvocationOnMock.class);

        // when
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(Arrays.asList(mockInvocation1, mockInvocation2));

        // then
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mockInvocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockInvocation2);
        List<InvocationMatcher> expectedInvocationMatchers = Arrays.asList(invocationMatcher1, invocationMatcher2);

        // then
        assertThat(invocationMatchers, is(expectedInvocationMatchers));
    }

    private static class MyCustomMatcher implements Matcher<Object> {
        @Override
        public boolean matches(Object actual) {
            return actual instanceof Integer && ((Integer) actual) > 0;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("a positive integer");
        }
    }
}