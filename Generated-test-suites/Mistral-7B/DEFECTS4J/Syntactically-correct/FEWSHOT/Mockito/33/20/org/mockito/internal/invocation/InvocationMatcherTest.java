package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.matchers.Arguments.not;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

public class InvocationMatcherTest {

    private static final Object[] ARGUMENTS = new Object[] { "one", 1, "two" };

    @Test
    public void testInvocationMatcherConstructorWithMatchers() {
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(new BaseMatcher<Object>() {
            @Override
            public boolean matches(Object item) {
                return "one".equals(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("'one'");
            }
        });
        matchers.add(new BaseMatcher<Object>() {
            @Override
            public boolean matches(Object item) {
                return 1 == item;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("1");
            }
        });

        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        Location location = mock(Location.class);

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        assertSame(invocation, matcher.getInvocation());
        assertSame(method, matcher.getMethod());
        assertSame(matchers, matcher.getMatchers());
    }

    @Test
    public void testInvocationMatcherConstructorWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);

        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertSame(invocation, matcher.getInvocation());
        assertSame(method, matcher.getMethod());
        assertNull(matcher.getMatchers());
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());

        assertEquals(location, matcher.getLocation());
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(new BaseMatcher<Object>() {
            @Override
            public boolean matches(Object item) {
                return "one".equals(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("'one'");
            }
        });

        String expected = "Invocation [method=Method [Name: methodName, DeclaringClass: class declaringClass], arguments=[one, 1, two], invocationSite=InvocationSite [location=Location [file=file, line=line, method=method]]] matches:";
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertEquals(expected, matcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method = mock(Method.class);
        Location location = mock(Location.class);
        List<Matcher> matchers = new LinkedList<Matcher>();

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, matchers);

        assertEquals(method, matcher1.getMethod());
        assertEquals(method, matcher2.getMethod());

        assertFalse(matcher1.matches(matcher2));
    }

    @Test
    public void testMatches_sameInvocation() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        Location location = mock(Location.class);

        InvocationMatcher matcher1 = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());

        assertTrue(matcher1.matches(matcher2));
    }

    @Test
    public void testMatches_sameInvocationWithArguments() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        Location location = mock(Location.class);
        Object[] arguments = ARGUMENTS;

        InvocationMatcher matcher1 = new InvocationMatcher(invocation, Collections.<Matcher>emptyList(), arguments);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation, Collections.<Matcher>emptyList(), arguments);

        assertTrue(matcher1.matches(matcher2));
    }

    @Test
    public void testMatches_differentInvocationWithsameMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method = mock(Method.class);

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, Collections.<Matcher>emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, Collections.<Matcher>emptyList());

        assertEquals(method, matcher1.getMethod());
        assertEquals(method, matcher2.getMethod());

        Object[] arguments = ARGUMENTS;
        InvocationMatcher matcherWithArguments1 = new InvocationMatcher(invocation1, Collections.<Matcher>emptyList(), arguments);
        InvocationMatcher matcherWithArguments2 = new InvocationMatcher(invocation2, Collections.<Matcher>emptyList(), arguments);

        assertFalse(matcherWithArguments1.matches(matcherWithArguments2));
    }

    @Test
    public void testMatches_sameInvocationWithDifferentArguments() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        Object[] arguments1 = ARGUMENTS;
        Object[] arguments2 = new Object[] { 1, "two" };
        Location location = mock(Location.class);

        InvocationMatcher matcher1 = new InvocationMatcher(invocation, Collections.<Matcher>emptyList(), arguments1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation, Collections.<Matcher>emptyList(), arguments2);

        assertFalse(matcher1.matches(matcher2));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);

        Method method = mock(Method.class);

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, Collections.<Matcher>emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, Collections.<Matcher>emptyList());

        assertEquals(method, matcher1.getMethod());
        assertEquals(method, matcher2.getMethod());

        assertFalse(matcher1.hasSameMethod(matcher2));
    }

    @Test
    public void testHasSameMethod_sameMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);

        InvocationMatcher matcher1 = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());

        assertTrue(matcher1.hasSameMethod(matcher2));
    }

    @Test
    public void testHasSameMethod_differentMethods() {
        Invocation invocation = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);

        InvocationMatcher matcher1 = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());

        assertFalse(matcher1.hasSameMethod(matcher2));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[] { "one", 1, "two" };

        InvocationMatcher matcher = new InvocationMatcher(invocation);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);

        matcher.captureArgumentsFrom(invocation);

        assertEquals(arguments[0], capturesArguments.getCapturedArgument(0));
        assertEquals(arguments[1], capturesArguments.getCapturedArgument(1));
        assertEquals(arguments[2], capturesArguments.getCapturedArgument(2));
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<Matcher>();

        List<InvocationMatcher> matchersList = InvocationMatcher.createFrom(new LinkedList<Invocation>() {{
            add(invocation1);
            add(invocation2);
        }});

        assertFalse(matchersList.get(0).matches(matchersList.get(1)));
    }

    @Test
    public void testCreateFrom_emptyInvocationsList() {
        List<InvocationMatcher> matchersList = InvocationMatcher.createFrom(Collections.<Invocation>emptyList());

        assertFalse(matchersList.get(0).matches(matchersList.get(0)));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, Collections.<Matcher>emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, Collections.<Matcher>emptyList());

        assertFalse(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testHasSimilarMethod_sameMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);

        InvocationMatcher matcher1 = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());

        assertTrue(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testHasSimilarMethod_sameMethodButDifferentInvocations() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method = mock(Method.class);

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, Collections.<Matcher>emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, Collections.<Matcher>emptyList());

        assertTrue(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testHasSimilarMethod_differentMethods() {
        Invocation invocation = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);

        InvocationMatcher matcher1 = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());

        assertFalse(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testArgumentsComparator_sameArguments() {
        Object[] arguments1 = ARGUMENTS;
        Object[] arguments2 = ARGUMENTS;

        assertTrue(new ArgumentsComparator().argumentsMatch(new InvocationMatcher(null, Collections.<Matcher>emptyList(), arguments1), new InvocationMatcher(null, Collections.<Matcher>emptyList(), arguments2)));
    }

    @Test
    public void testArgumentsComparator_differentArguments() {
        Object[] arguments1 = ARGUMENTS;
        Object[] arguments2 = new Object[] { 1, "two" };

        assertFalse(new ArgumentsComparator().argumentsMatch(new InvocationMatcher(null, Collections.<Matcher>emptyList(), arguments1), new InvocationMatcher(null, Collections.<Matcher>emptyList(), arguments2)));
    }

    @Test
    public void testArgumentsComparator_differentTypes() {
        Object[] arguments1 = new Object[] { "one", 1 };
        Object[] arguments2 = new Object[] { 1, "two" };

        assertFalse(new ArgumentsComparator().argumentsMatch(new InvocationMatcher(null, Collections.<Matcher>emptyList(), arguments1), new InvocationMatcher(null, Collections.<Matcher>emptyList(), arguments2)));
    }

    @Test
    public void testArgumentsComparator_differentTypesAndDifferentLength() {
        Object[] arguments1 = new Object[] { "one", 1 };
        Object[] arguments2 = new Object[] { 1, "two", "three" };

        assertFalse(new ArgumentsComparator().argumentsMatch(new InvocationMatcher(null, Collections.<Matcher>emptyList(), arguments1), new InvocationMatcher(null, Collections.<Matcher>emptyList(), arguments2)));
    }

    @Test
    public void testArgumentsComparator_differentTypesButSameValues() {
        Object[] arguments1 = new Object[] { "one", 1 };
        Object[] arguments2 = new Object[] { "one", 1 };

        assertTrue(new ArgumentsComparator().argumentsMatch(new InvocationMatcher(null, Collections.<Matcher>emptyList(), arguments1), new InvocationMatcher(null, Collections.<Matcher>emptyList(), arguments2)));
    }

    @Test
    public void testArgumentsComparator_sameMethodButDifferentArguments() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        Object[] arguments1 = ARGUMENTS;
        Object[] arguments2 = new Object[] { 1, "two" };

        InvocationMatcher matcher1 = new InvocationMatcher(invocation, Collections.<Matcher>emptyList(), arguments1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation, Collections.<Matcher>emptyList(), arguments2);

        assertFalse(matcher1.matches(matcher2));
    }

    @Test
    public void testToString_PrintSettings() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(new BaseMatcher<Object>() {
            @Override
            public boolean matches(Object item) {
                return "one".equals(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("'one'");
            }
        });

        String expected = "Invocation [method=Method [Name: methodName, DeclaringClass: class declaringClass], arguments=[one], invocationSite=InvocationSite [location=Location [file=file, line=line, method=method]]] matches:";
        PrintSettings printSettings = new PrintSettings();
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertEquals(expected, matcher.toString(printSettings));
    }
}