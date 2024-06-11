package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.argThat;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.mockito.exceptions.InvocationNotRecordedException;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stub.Stubber;

public class InvocationMatcherTest {

    @Test
    public void testCreateEmptyMatcher() {
        Invocation invocation = new Invocation(new Location(1, 1), new Object[]{}, new Method(Object.class, "foo", new Class[0]), null);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertNotNull(matcher);
        assertTrue(matcher.matches(invocation));
        assertTrue(matcher.getMatchers().isEmpty());
    }

    @Test
    public void testCreateMatcherWithCustomMatchers() {
        Invocation invocation = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[]{String.class}), null);
        List<Matcher> matchers = Collections.singletonList(argThat((ArgumentsMatcher) args -> args[0].equals("foo")));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        assertNotNull(matcher);
        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void testMatchesInvocationWithSameMethod() {
        Invocation invocation = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[]{String.class}), null);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation invocationToMatch = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[]{String.class}), null);
        assertTrue(matcher.matches(invocationToMatch));
    }

    @Test
    public void testMatchesInvocationWithDifferentMethod() {
        Invocation invocation = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[]{String.class}), null);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation invocationToMatch = new Invocation(new Location(1, 1), new Object[]{"bar"}, new Method(Object.class, "bar", new Class[]{String.class}), null);
        assertFalse(matcher.matches(invocationToMatch));
    }

    @Test
    public void testMatchesInvocationWithSameMock() {
        MockCreationSettings settings = new MockCreationSettings();
        Stubber stubber = settings.createMock(Object.class);
        Invocation invocation = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[]{String.class}), stubber);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation invocationToMatch = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[]{String.class}), stubber);
        assertTrue(matcher.matches(invocationToMatch));
    }

    @Test
    public void testMatchesInvocationWithDifferentMock() {
        MockCreationSettings settings1 = new MockCreationSettings();
        Stubber stubber1 = settings1.createMock(Object.class);
        Invocation invocation = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[]{String.class}), stubber1);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        MockCreationSettings settings2 = new MockCreationSettings();
        Stubber stubber2 = settings2.createMock(Object.class);
        Invocation invocationToMatch = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[]{String.class}), stubber2);
        assertFalse(matcher.matches(invocationToMatch));
    }

    @Test(expected = InvocationNotRecordedException.class)
    public void testMatchesInvocationWithUnverifiedInvocation() {
        Invocation invocation = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[]{String.class}), null, false);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation invocationToMatch = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[]{String.class}), null, true);
        assertTrue(matcher.matches(invocationToMatch));
    }

    @Test
    public void testMatchesInvocationWithUnverifiedInvocationAndDifferentMock() {
        Invocation invocation = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[]{String.class}), null, false);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation invocationToMatch = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[]{String.class}), null, true);
        assertFalse(matcher.matches(invocationToMatch));
    }

    @Test
    public void testMatchesInvocationWithSameMethodAndSameArguments() {
        Invocation invocation = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[]{String.class}), null);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation invocationToMatch = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[]{String.class}), null);
        assertTrue(matcher.matches(invocationToMatch));
    }

    @Test
    public void testMatchesInvocationWithSameMethodButDifferentArguments() {
        Invocation invocation = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[]{String.class}), null);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation invocationToMatch = new Invocation(new Location(1, 1), new Object[]{"bar"}, new Method(Object.class, "foo", new Class[]{String.class}), null);
        assertFalse(matcher.matches(invocationToMatch));
    }

    @Test
    public void testMatchesInvocationWithSameMethodButDifferentArgumentsAndSameMock() {
        Invocation invocation = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[]{String.class}), null);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation invocationToMatch = new Invocation(new Location(1, 1), new Object[]{"bar"}, new Method(Object.class, "foo", new Class[]{String.class}), null);
        assertFalse(matcher.matches(invocationToMatch));
    }

    @Test
    public void testMatchesInvocationWithSameMethodButDifferentArgumentsAndDifferentMock() {
        MockCreationSettings settings1 = new MockCreationSettings();
        Stubber stubber1 = settings1.createMock(Object.class);
        Invocation invocation = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[]{String.class}), stubber1, false);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        MockCreationSettings settings2 = new MockCreationSettings();
        Stubber stubber2 = settings2.createMock(Object.class);
        Invocation invocationToMatch = new Invocation(new Location(1, 1), new Object[]{"bar"}, new Method(Object.class, "foo", new Class[]{String.class}), stubber2, true);
        assertFalse(matcher.matches(invocationToMatch));
    }

    @Test
    public void testMatchesInvocationWithOverloadedMethodButSameArguments() {
        Invocation invocation = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[0]), null);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation invocationToMatch = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[]{String.class}), null);
        assertTrue(matcher.matches(invocationToMatch));
    }

    @Test
    public void testMatchesInvocationWithOverloadedMethodButDifferentArguments() {
        Invocation invocation = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[0]), null);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation invocationToMatch = new Invocation(new Location(1, 1), new Object[]{"bar"}, new Method(Object.class, "foo", new Class[]{String.class}), null);
        assertFalse(matcher.matches(invocationToMatch));
    }

    @Test
    public void testMatchesInvocationWithOverloadedMethodButDifferentArgumentsAndSameMock() {
        Invocation invocation = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[0]), null);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation invocationToMatch = new Invocation(new Location(1, 1), new Object[]{"bar"}, new Method(Object.class, "foo", new Class[]{String.class}), null);
        assertFalse(matcher.matches(invocationToMatch));
    }

    @Test
    public void testMatchesInvocationWithOverloadedMethodButDifferentArgumentsAndDifferentMock() {
        MockCreationSettings settings1 = new MockCreationSettings();
        Stubber stubber1 = settings1.createMock(Object.class);
        Invocation invocation = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[0]), stubber1, false);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        MockCreationSettings settings2 = new MockCreationSettings();
        Stubber stubber2 = settings2.createMock(Object.class);
        Invocation invocationToMatch = new Invocation(new Location(1, 1), new Object[]{"bar"}, new Method(Object.class, "foo", new Class[]{String.class}), stubber2, true);
        assertFalse(matcher.matches(invocationToMatch));
    }

    @Test
    public void testMatchesInvocationWithOverloadedMethodButSameArgumentsAndDifferentMock() {
        Invocation invocation = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[0]), null);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation invocationToMatch = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[]{String.class}), null);
        assertFalse(matcher.matches(invocationToMatch));
    }

    @Test
    public void testMatchesInvocationWithOverloadedMethodButSameArgumentsAndSameMock() {
        Invocation invocation = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[0]), null);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation invocationToMatch = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[]{String.class}), null);
        assertTrue(matcher.matches(invocationToMatch));
    }

    @Test
    public void testHasSimilarMethodReturnsFalseForDifferentMethods() {
        Invocation invocation = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[]{String.class}), null);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation invocationWithDifferentMethod = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "bar", new Class[]{String.class}), null);
        assertFalse(matcher.hasSimilarMethod(invocationWithDifferentMethod));
    }

    @Test
    public void testHasSimilarMethodReturnsTrueForSameMethod() {
        Invocation invocation = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[]{String.class}), null);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation invocationWithSameMethod = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[]{String.class}), null);
        assertTrue(matcher.hasSimilarMethod(invocationWithSameMethod));
    }

    @Test
    public void testHasSameMethodReturnsFalseForDifferentMethods() {
        Invocation invocation = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[]{String.class}), null);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation invocationWithDifferentMethod = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "bar", new Class[]{String.class}), null);
        assertFalse(matcher.hasSameMethod(invocationWithDifferentMethod));
    }

    @Test
    public void testHasSameMethodReturnsTrueForSameMethod() {
        Invocation invocation = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[]{String.class}), null);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation invocationWithSameMethod = new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[]{String.class}), null);
        assertTrue(matcher.hasSameMethod(invocationWithSameMethod));
    }

    @Test
    public void testCreateInvocationMatcherFromList() {
        List<Invocation> invocations = new LinkedList<Invocation>() {{
            add(new Invocation(new Location(1, 1), new Object[]{"foo"}, new Method(Object.class, "foo", new Class[]{String.class}), null));
            add(new Invocation(new Location(1, 1), new Object[]{"bar"}, new Method(Object.class, "foo", new Class[]{String.class}), null));
        }};
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);
        assertTrue(matchers.size() == 2);
    }
}