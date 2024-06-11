package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.argumentsWithAnyValues;
import static org.mockito.internal.matchers.ArgumentsMatcher.argumentsWithSameValues;
import static org.mockito.internal.matchers.ArgumentsMatcher.isNull;
import static org.mockito.internal.matchers.ArgumentsMatcher.same;
import static org.mockito.internal.matchers.ArgumentsMatcher.type;
import static org.mockito.internal.matchers.ArgumentsMatcher.value;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.quality.DumpMatcher;

import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {
    @Test
    public void testHasSameMethod() {
        Object mock = Mockito.mock(String.class);
        Method method1 = Mockito.method(String.class, "method1", new Class[]{Integer.class, String.class});
        Method method2 = Mockito.method(String.class, "method1", new Class[]{String.class, Integer.class});
        Invocation invocation1 = Mockito.on(mock).invoke(method1, 1, "one");
        Invocation invocation2 = Mockito.on(mock).invoke(method2, "one", 1);

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertTrue(matcher1.hasSameMethod(matcher2));
        assertFalse(matcher1.hasSameMethod(Mockito.on(mock).invoke(Mockito.method(String.class, "method2"))));
    }

    @Test
    public void testMatches() {
        Object mock = Mockito.mock(String.class);
        Method method = Mockito.method(String.class, "method", new Class[]{Integer.class});
        Invocation invocation = Mockito.on(mock).invoke(method, 1);

        InvocationMatcher matcher = new InvocationMatcher(invocation);
        InvocationMatcher matcherWithSameMethod = new InvocationMatcher(invocation);
        InvocationMatcher matcherWithDifferentMethod = Mockito.on(mock).invoke(Mockito.method(String.class, "otherMethod"));

        assertTrue(matcher.matches(invocation));
        assertTrue(matcherWithSameMethod.matches(invocation));
        assertFalse(matcher.matches(matcherWithDifferentMethod));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object mock = Mockito.mock(String.class);
        Method method = Mockito.method(String.class, "method", new Class[]{String[].class});
        Object[] args = new Object[]{new String[]{"one", "two"}};
        Invocation invocation = Mockito.on(mock).invoke(method, args);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertTrue(matcher.safelyArgumentsMatch(args));
    }

    @Test
    public void testToString() {
        Object mock = Mockito.mock(String.class);
        Method method = Mockito.method(String.class, "method", new Class[]{Integer.class});
        Invocation invocation = Mockito.on(mock).invoke(method, 1);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertTrue(matcher.toString().startsWith("InvocationMatcher:"));
        assertTrue(matcher.toString().contains("method(int"));
        assertTrue(matcher.toString().contains("1"));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Object mock = Mockito.mock(String.class);
        Method method = Mockito.method(String.class, "method", new Class[]{String[].class});
        Object[] args = new Object[]{new String[]{"one", "two"}};
        Invocation invocation = Mockito.on(mock).invoke(method, args);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        List<Matcher> matchers = Arrays.asList(
                same(new String[]{"one"}),
                value("two")
        );
        InvocationMatcher matcherWithMatchers = new InvocationMatcher(invocation, matchers);

        matcherWithMatchers.captureArgumentsFrom(invocation);

        assertTrue(matchers.get(0).matches(matcherWithMatchers.getArguments()[0]));
        assertTrue(matchers.get(1).matches(matcherWithMatchers.getArguments()[1]));
    }

    @Test
    public void testCreateFrom() {
        Object mock = Mockito.mock(String.class);
        Method method = Mockito.method(String.class, "method", new Class[]{String.class, Integer.class});
        Invocation invocation1 = Mockito.on(mock).invoke(method, "one", 1);
        Invocation invocation2 = Mockito.on(mock).invoke(method, "two", 2);

        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher matcher1 = matchers.get(0);
        InvocationMatcher matcher2 = matchers.get(1);

        assertTrue(matcher1.matches(invocation1));
        assertTrue(matcher2.matches(invocation2));
    }

    @Test
    public void testArgumentsComparator() {
        Object mock = Mockito.mock(String.class);
        Method method = Mockito.method(String.class, "method", new Class[]{String[].class});
        Object[] args1 = new Object[]{new String[]{"one", "two"}};
        Object[] args2 = new Object[]{new String[]{"two", "one"}};
        Invocation invocation = Mockito.on(mock).invoke(method, args1);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        List<Matcher> matchers = Arrays.asList(
                new DumpMatcher("invocation arguments"),
                new ArgumentsMatcher(new String[]{"one", "two"})
        );
        InvocationMatcher matcherWithMatchers = new InvocationMatcher(invocation, matchers);

        matcherWithMatchers.captureArgumentsFrom(invocation);

        assertTrue(matchers.get(1).matches(matcherWithMatchers.getArguments()[0]));
        assertTrue(matchers.get(0).matches(matcherWithMatchers.getArguments()[1]));
    }

    @Test
    public void testArgumentsComparatorWithVarargs() {
        Object mock = Mockito.mock(String.class);
        Method method = Mockito.method(String.class, "method", new Class[]{String[].class});
        Object[] args1 = new Object[]{new String[]{"one", "two"}};
        Object[] args2 = new Object[]{new String[]{"two", "one", "three"}};
        Invocation invocation = Mockito.on(mock).invoke(method, args1);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        List<Matcher> matchers = Arrays.asList(
                new DumpMatcher("invocation arguments"),
                argumentsWithSameValues(new String[]{"one", "two"}),
                argumentsWithAnyValues(String[].class)
        );
        InvocationMatcher matcherWithMatchers = new InvocationMatcher(invocation, matchers);

        matcherWithMatchers.captureArgumentsFrom(invocation);

        assertTrue(matchers.get(1).matches(matcherWithMatchers.getArguments()[0]));
        assertTrue(matchers.get(2).matches(matcherWithMatchers.getArguments()[1]));
    }
}