package org.mockito.internal.verification.argumentmatching;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        Matcher<Integer> intMatcher = new ContainsExtraTypeInformation(org.hamcrest.CoreMatchers.is(1));
        Matcher<String> stringMatcher = new ContainsExtraTypeInformation(org.hamcrest.CoreMatchers.is("one"));
        Matcher<Object> anyMatcher = org.hamcrest.CoreMatchers.anyObject();

        Object[] arguments = new Object[]{1, "one", 2, "two", new Object()};
        List<Matcher> matchers = Arrays.asList(intMatcher, stringMatcher, anyMatcher, intMatcher);

        Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(new Integer[]{1, 3}, indexes);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithEmptyList() {
        Matcher<Object> anyMatcher = org.hamcrest.CoreMatchers.anyObject();
        Object[] arguments = new Object[]{};
        List<Matcher> matchers = Arrays.asList(anyMatcher);

        Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(new Integer[0], indexes);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithNullArguments() {
        Matcher<Object> anyMatcher = org.hamcrest.CoreMatchers.anyObject();
        Object[] arguments = new Object[]{null};
        List<Matcher> matchers = Arrays.asList(anyMatcher);

        Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(new Integer[0], indexes);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithNoSuspiciousMatches() {
        Matcher<Integer> intMatcher = new ContainsExtraTypeInformation(org.hamcrest.CoreMatchers.is(1));
        Object[] arguments = new Object[]{1, 1, 1};
        List<Matcher> matchers = Arrays.asList(intMatcher, intMatcher, intMatcher);

        Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(new Integer[0], indexes);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithMismatchedSize() {
        Matcher<Integer> intMatcher = new ContainsExtraTypeInformation(org.hamcrest.CoreMatchers.is(1));
        Object[] arguments = new Object[]{1, 1, 1};
        List<Matcher> matchers = Arrays.asList(intMatcher, intMatcher, intMatcher, intMatcher);

        Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(new Integer[0], indexes);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithToStringMatchesButNotTypeMatches() {
        Matcher<Integer> intMatcher = new ContainsExtraTypeInformation(new ContainsExtraTypeInformation.TypeMatcher<String>() {
            @Override
            public boolean matches(String t) {
                return t.equals("stringType");
            }
        }, org.hamcrest.CoreMatchers.is(1));
        Matcher<String> stringMatcher = new ContainsExtraTypeInformation(org.hamcrest.CoreMatchers.is("one"));
        Object[] arguments = new Object[]{1, "one", "stringType", "two", new Object()};
        List<Matcher> matchers = Arrays.asList(intMatcher, stringMatcher, intMatcher, intMatcher);

        Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(new Integer[]{2}, indexes);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithToStringDoesNotMatch() {
        Matcher<Integer> intMatcher = new ContainsExtraTypeInformation(org.hamcrest.CoreMatchers.is(1));
        Object[] arguments = new Object[]{2, 1, 1};
        List<Matcher> matchers = Arrays.asList(intMatcher, intMatcher, intMatcher);

        Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(new Integer[0], indexes);
    }
}