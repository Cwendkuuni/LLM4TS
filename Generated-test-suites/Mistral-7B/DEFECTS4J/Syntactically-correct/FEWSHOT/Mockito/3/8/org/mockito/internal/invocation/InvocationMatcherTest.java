package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.internal.matchers.ArgumentsMatchers.equalTo;
import static org.mockito.internal.matchers.ArgumentsMatchers.isA;
import static org.mockito.internal.matchers.ArgumentsMatchers.not;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InvocationOnMock;
import org.mockito.internal.matchers.CaptureArgument;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.StringMatcher;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.stub.Answer;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Invocation invocation;
    private List<Matcher> matchers;
    private Object[] arguments;
    private Location location;

    @Before
    public void setUp() {
        matchers = Collections.singletonList(equalTo("foo"));
        invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(getMethod("foo", Object.class));
        arguments = new Object[]{"foo"};
        location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        matcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testToString() {
        String expected = new PrintSettings().print(matchers, invocation);
        assertEquals(expected, matcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocationWithSameMethod = spy(Invocation.class);
        when(invocationWithSameMethod.getMethod()).thenReturn(getMethod("foo", Object.class));
        when(invocationWithSameMethod.getMock()).thenReturn(matcher.getInvocation().getMock());
        when(invocationWithSameMethod.getArguments()).thenReturn(arguments);
        assertTrue(matcher.matches(invocationWithSameMethod));
    }

    @Test
    public void testMatchesWithDifferentMethod() {
        Invocation invocationWithDifferentMethod = spy(Invocation.class);
        when(invocationWithDifferentMethod.getMethod()).thenReturn(getMethod("bar", Object.class));
        when(invocationWithDifferentMethod.getMock()).thenReturn(matcher.getInvocation().getMock());
        when(invocationWithDifferentMethod.getArguments()).thenReturn(arguments);
        assertFalse(matcher.matches(invocationWithDifferentMethod));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocationWithSameMethod = spy(Invocation.class);
        when(invocationWithSameMethod.getMethod()).thenReturn(getMethod("foo", Object.class));
        assertTrue(matcher.hasSameMethod(invocationWithSameMethod));

        Invocation invocationWithDifferentName = spy(Invocation.class);
        when(invocationWithDifferentName.getMethod()).thenReturn(getMethod("bar", Object.class));
        assertFalse(matcher.hasSameMethod(invocationWithDifferentName));

        Invocation invocationWithDifferentParameters = spy(Invocation.class);
        when(invocationWithDifferentParameters.getMethod()).thenReturn(getMethod("foo", int.class));
        assertFalse(matcher.hasSameMethod(invocationWithDifferentParameters));

        Invocation invocationWithSameNameAndDifferentObjectType = spy(Invocation.class);
        when(invocationWithSameNameAndDifferentObjectType.getMethod()).thenReturn(getMethod("foo", Integer.TYPE));
        assertFalse(matcher.hasSameMethod(invocationWithSameNameAndDifferentObjectType));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        Invocation invocationWithArguments = mock(Invocation.class);
        when(invocationWithArguments.getArguments()).thenReturn(new Object[]{Arrays.asList("one", "two"), 3});

        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        when(capturesArguments.captureFrom(ArgumentMatchers.any())).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                captureArguments.recordThat(captor.capture());
                return null;
            }
        });

        matcher.captureArgumentsFrom(invocationWithArguments);
        assertEquals(Arrays.asList("one", "two"), captor.getValue());
        assertEquals(3, captor.getAllValues().get(1).length);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(invocation, invocation);
        List<InvocationMatcher> matchersList = InvocationMatcher.createFrom(invocations);

        assertTrue(matchersList.size() == 2);
        assertTrue(matchersList.get(0) == matcher);
        assertTrue(matchersList.get(1) == matcher);
    }

    @Test
    public void testEmptyMatcherList() {
        List<Invocation> invocations = Collections.singletonList(invocation);
        List<InvocationMatcher> matchersList = InvocationMatcher.createFrom(invocations);

        assertTrue(matchersList.isEmpty());
    }

    private Method getMethod(String name, Class<?> clazz) {
        try {
            return Class.forName("org.mockito.TestClass")
                    .getMethod(name, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

class ArgumentMatchers {
    public static Matcher<List<String>> any() {
        return new StringMatcher();
    }

    public static Matcher<Object> argumentCaptor(Class<?> type) {
        return new CaptureArgument<>(type);
    }

    static class CaptureArgument<T> implements Matcher<T> {
        private final Class<T> clazz;
        private ArgumentCaptor<T> captor;

        public CaptureArgument(Class<T> clazz) {
            this.clazz = clazz;
        }

        public CaptureArgument recordThat(ArgumentCaptor<T> captor) {
            this.captor = captor;
            return this;
        }

        @Override
        public boolean matches(Object argument) {
            captor.setValue(argument);
            return true;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("the argument captured by an ArgumentCaptor");
        }
    }

    static class StringMatcher implements Matcher<List<String>> {
        @Override
        public boolean matches(List<String> stringList) {
            return true;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("any list of strings");
        }
    }
}

class PrintSettings {
    public String print(List<Matcher> matchers, Invocation invocation) {
        StringBuilder builder = new StringBuilder();
        builder.append("Invoked method: [");
        builder.append(invocation.getMethod().getName());
        builder.append("] on [");
        builder.append(invocation.getMock().getClass().getName());
        builder.append("]");
        builder.append("\n  With arguments: [");
        boolean first = true;
        for (Matcher matcher : matchers) {
            if (first) {
                first = false;
            } else {
                builder.append(", ");
            }
            matcher.describeTo(new Description());
        }
        builder.append("]");
        return builder.toString();
    }
}

class Description {
    public void appendText(String text) {}
}