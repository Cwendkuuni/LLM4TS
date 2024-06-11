package org.mockito.internal.invocation;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CaptureArguments;
import org.mockito.internal.matchers.ArgumentsComparator;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.internal.matchers.ArgumentsMatcher.nullArgument;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher() {
        Invocation invocation = new Invocation(new Object(), new Method("testMethod", new Class[0]));
        List<Matcher> matchers = Arrays.asList(new Matcher() {}, new Matcher() {});
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation, matcher.getInvocation());
        assertEquals(matchers, matcher.getMatchers());
    }

    @Test
    public void testInvocationMatcherWithEmptyList() {
        Invocation invocation = new Invocation(new Object(), new Method("testMethod", new Class[0]));
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertEquals(invocation, matcher.getInvocation());
        assertNull(matcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        InvocationMatcher matcher = new InvocationMatcher(new Invocation(new Object(), new Method("testMethod", new Class[0])), Arrays.asList(new Matcher() {}, new Matcher() {}));
        Method method = matcher.getMethod();
        assertEquals(new Method("testMethod", new Class[0]), method);
    }

    @Test
    public void testNullInvocation() {
        List<Matcher> matchers = Arrays.asList(new Matcher() {}, new Matcher() {});
        InvocationMatcher matcher = new InvocationMatcher(null, matchers);
        assertNull(matcher);
    }

    @Test
    public void testNullMatchers() {
        Invocation invocation = new Invocation(new Object(), new Method("testMethod", new Class[0]));
        InvocationMatcher matcher = new InvocationMatcher(invocation, null);
        assertNull(matcher);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMatcher() {
        Invocation invocation = new Invocation(new Object(), new Method("testMethod", new Class[0]));
        List<Matcher> matchers = Arrays.asList(new Object() {}, new Matcher() {});
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        fail("Expected IllegalArgumentException");
    }

    // Add more test cases here for the other edge cases mentioned above

    @Test
    public void testArgumentsComparator() {
        Object[] actualArgs = new Object[]{new Object(){}, new Object() {}};
        Object[] expectedArgs = new Object[]{new Object(){}, new Object() {}};
        InvocationMatcher matcher = new InvocationMatcher(new Invocation(new Object(), new Method("testMethod", new Class[0])), Arrays.asList(new CapturesArguments() {}));
        assertTrue(matcher.safelyArgumentsMatch(expectedArgs));
    }

    @Test
    public void testArgumentsComparator_WithUnequalSize() {
        Object[] actualArgs = new Object[]{new Object(){}, new Object() {}};
        Object[] expectedArgs = new Object[]{new Object(){}, new Object() {}, new Object() {}};
        InvocationMatcher matcher = new InvocationMatcher(new Invocation(new Object(), new Method("testMethod", new Class[0])), Arrays.asList(new CapturesArguments() {}));
        assertFalse(matcher.safelyArgumentsMatch(expectedArgs));
    }

    @Test(expected = NullPointerException.class)
    public void testNullArguments_ForArgumentsComparator() {
        Object[] actualArgs = null;
        InvocationMatcher matcher = new InvocationMatcher(new Invocation(new Object(), new Method("testMethod", new Class[0])), Arrays.asList(new CapturesArguments() {}));
        matcher.safelyArgumentsMatch(actualArgs);
        fail("Expected NullPointerException");
    }

    @Test
    public void testHasSameMethod() {
        InvocationMatcher matcher1 = new InvocationMatcher(new Invocation(new Object(), new Method("testMethod", new Class[0])), Arrays.asList(new Matcher() {}, new Matcher() {}));
        InvocationMatcher matcher2 = new InvocationMatcher(new Invocation(new Object(), new Method("testMethod", new Class[0])), Arrays.asList(new Matcher() {}, new Matcher() {}));
        assertTrue(matcher1.hasSameMethod(matcher2));
    }

    @Test
    public void testHasSimilarMethod() {
        InvocationMatcher matcher1 = new InvocationMatcher(new Invocation(new Object(), new Method("testMethod1", new Class[0])), Arrays.asList(new Matcher() {}, new Matcher() {}));
        InvocationMatcher matcher2 = new InvocationMatcher(new Invocation(new Object(), new Method("testMethod2", new Class[0])), Arrays.asList(new Matcher() {}, new Matcher() {}));
        assertFalse(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testHasSimilarMethod_SameMethodButDifferentMocks() {
        InvocationMatcher matcher1 = new InvocationMatcher(new Invocation(new Object(), new Method("testMethod", new Class[0])), Arrays.asList(new Matcher() {}));
        InvocationMatcher matcher2 = new InvocationMatcher(new Invocation(new Object(), new Method("testMethod", new Class[0])), Arrays.asList(new Matcher() {}));
        InvocationMatcher mock1 = new InvocationMatcher(new Invocation(new Object(), new Method("testMethod", new Class[0])), Arrays.asList(new Matcher() {}));
        InvocationMatcher mock2 = new InvocationMatcher(new Invocation(new Object(), new Method("testMethod", new Class[0])), Arrays.asList(new Matcher() {}));
        assertFalse(matcher1.hasSimilarMethod(mock1));
        assertFalse(matcher1.hasSimilarMethod(mock2));
    }

    @Test
    public void testHasSimilarMethod_SameMethodButUnverified() {
        InvocationMatcher matcher1 = new InvocationMatcher(new Invocation(new Object(), new Method("testMethod", new Class[0])), Arrays.asList(new Matcher() {}));
        InvocationMatcher matcher2 = new InvocationMatcher(new Invocation(new Object(), new Method("testMethod", new Class[0])), Arrays.asList(new Matcher() {}));
        InvocationMatcher verifiedMatcher1 = new InvocationMatcher(new Invocation(new Object(), new Method("testMethod", new Class[0])), Arrays.asList(new Matcher() {}));
        InvocationMatcher verifiedMatcher2 = new InvocationMatcher(new Invocation(new Object(), new Method("testMethod", new Class[0])), Arrays.asList(new Matcher() {}));
        InvocationMatcher mockMatcher = new InvocationMatcher(new Invocation(new Object(), new Method("testMethod", new Class[0])), Arrays.asList(new Matcher() {}));
        assertFalse(matcher1.hasSimilarMethod(verifiedMatcher1));
        assertFalse(matcher1.hasSimilarMethod(verifiedMatcher2));
        assertFalse(matcher1.hasSimilarMethod(mockMatcher));
    }

    @Test
    public void testHasSameMethod_SameMethodButOverloaded() {
        InvocationMatcher matcher1 = new InvocationMatcher(new Invocation(new Object(), new Method("testMethod", new Class[0])), Arrays.asList(new Matcher() {}));
        InvocationMatcher matcher2 = new InvocationMatcher(new Invocation(new Object(), new Method("testMethod", new Class[0], new Class[0])), Arrays.asList(new Matcher() {}));
        assertFalse(matcher1.hasSameMethod(matcher2));
    }

    @Test
    public void testHasSameMethod_SameMethodButArgumentsDifferent() {
        InvocationMatcher matcher1 = new InvocationMatcher(new Invocation(new Object(), new Method("testMethod", new Class[0])), Arrays.asList(nullArgument()));
        InvocationMatcher matcher2 = new InvocationMatcher(new Invocation(new Object(), new Method("testMethod", new Class[0])), Arrays.asList(new Object[]{}));
        assertFalse(matcher1.hasSameMethod(matcher2));
    }

    @Test
    public void testToString() {
        InvocationMatcher matcher = new InvocationMatcher(new Invocation(new Object(), new Method("testMethod", new Class[0])), Arrays.asList(new Matcher() {}, new Matcher() {}));
        String toString = matcher.toString();
        assertTrue(toString.contains("InvocationMatcher"));
        assertTrue(toString.contains("testMethod"));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(new Invocation(new Object(), new Method("testMethod", new Class[0])), new Invocation(new Object(), new Method("testMethod", new Class[0])));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertEquals(invocations.size(), invocationMatchers.size());
    }
}