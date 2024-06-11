import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        Object[] arguments = new Object[]{1, "two", 3.0, new ContainsExtraTypeInformation(Integer.class, "three")};
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(contains(1));
        matchers.add(contains("two"));
        matchers.add(contains(3));
        matchers.add(new ContainsExtraTypeInformation(Double.class, "three"));

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // typical use case: no suspicious arguments
        Assert.assertArrayEquals(new Integer[0], indexes);

        // edge case: one suspicious argument
        matchers.set(3, new ContainsExtraTypeInformation(Integer.class, "three"));
        arguments = new Object[]{1, "two", 3.0, new ContainsExtraTypeInformation(Integer.class, "four")};
        indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertEquals(1, indexes.length);
        Assert.assertEquals(3, indexes[0].intValue());

        // error scenario: mismatched sizes
        matchers = new LinkedList<>();
        matchers.add(contains(1));
        arguments = new Object[]{1, "two", 3.0};
        try {
            tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSafelyMatches() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        // typical use case: safe match
        Matcher matcher = contains(1);
        Object arg = 1;
        assertTrue(tool.safelyMatches(matcher, arg));

        // edge case: unsafe match (different class)
        matcher = contains("two");
        arg = 1;
        assertFalse(tool.safelyMatches(matcher, arg));

        // error scenario: matcher throws an exception
        matcher = new ThrowsException(IllegalArgumentException.class);
        arg = 1;
        try {
            tool.safelyMatches(matcher, arg);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testToStringEquals() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        // typical use case: equal strings
        Matcher matcher = contains("two");
        Object arg = "two";
        assertTrue(tool.toStringEquals(matcher, arg));

        // edge case: different strings
        matcher = contains("three");
        arg = "two";
        assertFalse(tool.toStringEquals(matcher, arg));

        // error scenario: mismatched types
        matcher = contains(1);
        arg = "two";
        try {
            tool.toStringEquals(matcher, arg);
            fail("Expected AssertionError");
        } catch (AssertionError e) {
            // expected
        }
    }

    private Matcher contains(Object value) {
        return new ContainsMatcher(value);
    }

    private static class ContainsMatcher implements Matcher<Object> {
        private final Object value;

        public ContainsMatcher(Object value) {
            this.value = value;
        }

        @Override
        public boolean matches(Object argument) {
            return value.equals(argument);
        }

        @Override
        public String toString() {
            return "contains(" + value + ")";
        }
    }

    private static class ThrowsException implements Matcher<Object> {
        private final Class<? extends Throwable> exceptionType;

        public ThrowsException(Class<? extends Throwable> exceptionType) {
            this.exceptionType = exceptionType;
        }

        @Override
        public boolean matches(Object argument) {
            throw new IllegalStateException("This matcher is supposed to throw an exception");
        }

        @Override
        public void describeTo(StringDescription description) {
            description.appendText("throws ").appendValue(exceptionType);
        }
    }
}