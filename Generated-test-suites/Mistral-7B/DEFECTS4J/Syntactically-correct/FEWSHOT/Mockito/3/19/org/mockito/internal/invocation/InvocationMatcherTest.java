package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {

    @Test
    public void testHasSameMethod() {
        Object mock = mock(Object.class);
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.spy(Mockito.method(String.class.getMethod("concat", String.class)));
        Method method2 = Mockito.spy(Mockito.method(String.class.getMethod("concat", String.class)));
        invocation1.setMethod(method1);
        invocation2.setMethod(method2);

        assertTrue(new InvocationMatcher(invocation1).hasSameMethod(invocation1));
        assertTrue(new InvocationMatcher(invocation1).hasSameMethod(invocation2));
        assertFalse(new InvocationMatcher(invocation1).hasSameMethod(mock));
        assertFalse(new InvocationMatcher(invocation1).hasSameMethod(null));
    }

    @Test
    public void testMatches() {
        Object mock = mock(Object.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Matchers.is("foo"), Matchers.is("bar"));

        new InvocationMatcher(invocation, matchers).matches(invocation);

        ArgumentCaptor<Matcher> matcherCaptor = ArgumentCaptor.forClass(Matcher.class);
        new InvocationMatcher(invocation, matchers).captureArgumentsFrom(invocation);
        Mockito.verify(mock).matches(matcherCaptor.capture());
        assertTrue(matcherCaptor.getValue().matches("foo"));
        assertTrue(matcherCaptor.getValue().matches("bar"));
    }

    @Test
    public void testMatchesWithDifferentInvocations() {
        Object mock = mock(Object.class);
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Matchers.is("foo"), Matchers.is("bar"));

        new InvocationMatcher(invocation1, matchers).matches(invocation2);

        assertFalse(invocation1.getArguments()[0].equals(invocation2.getArguments()[0]));
        assertFalse(invocation1.getArguments()[1].equals(invocation2.getArguments()[1]));

        new InvocationMatcher(invocation1, matchers).matches(mock);

        assertFalse(invocation1.getArguments()[0].equals(mock));
        assertFalse(invocation1.getArguments()[1].equals(mock));
    }

    @Test
    public void testToString() {
        Object mock = mock(Object.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Matchers.is("foo"), Matchers.is("bar"));

        String expected = "Invocation[[java.lang.reflect.Method: public java.lang.String java.lang.String.concat(java.lang.String), arguments=[foo, bar]], matchers=[InvocationMatcher[args=[foo]], InvocationMatcher[args=[bar]]]]";
        String actual = new InvocationMatcher(invocation, matchers).toString();

        assertTrue(actual.equals(expected));
    }
}