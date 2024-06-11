package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.matchers.ContainsExtraTypeInformation.typeMatches;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        Object[] arguments = new Object[]{new Object(), "string", 1, 1.0};
        List<Matcher<Object>> matchers = Arrays.asList(
                new TypeSafeMatcher<Object>() {
                    @Override
                    public boolean matchesSafely(Object item) {
                        return item instanceof String;
                    }

                    @Override
                    public void describeTo(StringDescription description) {
                        description.appendText("a string");
                    }
                },
                new TypeSafeMatcher<Object>() {
                    @Override
                    public boolean matchesSafely(Object item) {
                        return item instanceof Number;
                    }

                    @Override
                    public void describeTo(StringDescription description) {
                        description.appendText("a number");
                    }
                },
                new TypeSafeMatcher<Object>() {
                    @Override
                    public boolean matchesSafely(Object item) {
                        return item.equals(1);
                    }

                    @Override
                    public void describeTo(StringDescription description) {
                        description.appendText("the number 1");
                    }
                },
                new TypeSafeMatcher<Object>() {
                    @Override
                    public boolean matchesSafely(Object item) {
                        return item.equals(1.0);
                    }

                    @Override
                    public void describeTo(StringDescription description) {
                        description.appendText("the number 1.0");
                    }
                }
        );

        Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(new Integer[]{0, 2}, result);

        matchers = Arrays.asList(
                new TypeSafeMatcher<Object>() {
                    @Override
                    public boolean matchesSafely(Object item) {
                        return item instanceof Number;
                    }

                    @Override
                    public void describeTo(StringDescription description) {
                        description.appendText("a number");
                    }
                },
                new TypeSafeMatcher<Object>() {
                    @Override
                    public boolean matchesSafely(Object item) {
                        return item.equals(1);
                    }

                    @Override
                    public void describeTo(StringDescription description) {
                        description.appendText("the number 1");
                    }
                },
                new TypeSafeMatcher<Object>() {
                    @Override
                    public boolean matchesSafely(Object item) {
                        return item.equals(1.0);
                    }

                    @Override
                    public void describeTo(StringDescription description) {
                        description.appendText("the number 1.0");
                    }
                },
                new TypeSafeMatcher<Object>() {
                    @Override
                    public boolean matchesSafely(Object item) {
                        return item.equals("string");
                    }

                    @Override
                    public void describeTo(StringDescription description) {
                        description.appendText("the string 'string'");
                    }
                }
        );
        result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(new Integer[]{1}, result);

        matchers = Arrays.asList(
                new TypeSafeMatcher<Object>() {
                    @Override
                    public boolean matchesSafely(Object item) {
                        return item instanceof String;
                    }

                    @Override
                    public void describeTo(StringDescription description) {
                        description.appendText("a string");
                    }
                },
                new TypeSafeMatcher<Object>() {
                    @Override
                    public boolean matchesSafely(Object item) {
                        return item.equals("string");
                    }

                    @Override
                    public void describeTo(StringDescription description) {
                        description.appendText("the string 'string'");
                    }
                }
        );
        result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertNull(result);

        matchers = Arrays.asList(
                new TypeSafeMatcher<Object>() {
                    @Override
                    public boolean matchesSafely(Object item) {
                        return item instanceof String;
                    }

                    @Override
                    public void describeTo(StringDescription description) {
                        description.appendText("a string");
                    }
                },
                new TypeSafeMatcher<Object>() {
                    @Override
                    public boolean matchesSafely(Object item) {
                        return item.equals("string");
                    }

                    @Override
                    public void describeTo(StringDescription description) {
                        description.appendText("the string 'string'");
                    }
                },
                new TypeSafeMatcher<Object>() {
                    @Override
                    public boolean matchesSafely(Object item) {
                        return item instanceof Number;
                    }

                    @Override
                    public void describeTo(StringDescription description) {
                        description.appendText("a number");
                    }
                }
        );
        result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertNull(result);
    }
}