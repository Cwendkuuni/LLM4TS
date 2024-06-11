package org.mockito.internal.matchers;

import org.junit.Test;
import org.junit.Assert;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.Objects;

public class SameTest {

    @Test
    public void testMatchesWhenObjectsAreSame() {
        Object wanted = new Object();
        Object actual = wanted;

        Same sameMatcher = new Same(wanted);

        Assert.assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testMatchesWhenObjectsAreDifferentInstancesButEqual() {
        Object wanted = new Object();
        Object actual = new Object();
        if (wanted instanceof String) {
            actual = (String) wanted;
        } else if (wanted instanceof Character) {
            actual = (Character) wanted;
        } else {
            actual = wanted.clone();
        }

        Same sameMatcher = new Same(wanted);

        Assert.assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testMatchesWhenObjectsAreNotEqual() {
        Object wanted = new Object();
        Object actual = new Object();

        Same sameMatcher = new Same(wanted);

        Assert.assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testDescribeToMatchesWhenObjectsAreSame() {
        Object wanted = new Object();
        Object actual = wanted;

        Same sameMatcher = new Same(wanted);
        Description description = new Description();

        sameMatcher.describeTo(description);

        Matcher expectedMatcher = CoreMatchers.is(wanted);
        Matcher actualMatcher = new ArgumentMatcher<Object>() {
            @Override
            public boolean matches(Object argument) {
                return wanted.equals(argument);
            }

            @Override
            public void describeTo(Description description1) {
                expectedMatcher.describeTo(description1);
            }
        };

        Assert.assertEquals(actualMatcher.toString(), description.toString());
    }

    @Test
    public void testDescribeToMatchesWhenObjectsAreDifferentButEqual() {
        Object wanted = new Object();
        Object actual = wanted;

        Same sameMatcher = new Same(wanted);
        Description description = new Description();

        sameMatcher.describeTo(description);

        Matcher expectedMatcher = CoreMatchers.is(wanted.toString());
        Matcher actualMatcher = new ArgumentMatcher<Object>() {
            @Override
            public boolean matches(Object argument) {
                return Objects.equals(wanted, argument);
            }

            @Override
            public void describeTo(Description description1) {
                description1.appendText("same(");
                description1.appendText(wanted.toString());
                description1.appendText(")");
            }
        };

        Assert.assertEquals(actualMatcher.toString(), description.toString());
    }

    @Test
    public void testDescribeToMatchesWhenObjectsAreNotEqual() {
        Object wanted = new Object();
        Object actual = new Object();

        Same sameMatcher = new Same(wanted);
        Description description = new Description();

        sameMatcher.describeTo(description);

        Matcher expectedMatcher = CoreMatchers.not(CoreMatchers.is(wanted));
        Matcher actualMatcher = new ArgumentMatcher<Object>() {
            @Override
            public boolean matches(Object argument) {
                return !Objects.equals(wanted, argument);
            }

            @Override
            public void describeTo(Description description1) {
                description1.appendText("not same(");
                description1.appendText(wanted.toString());
                description1.appendText(")");
            }
        };

        Assert.assertEquals(actualMatcher.toString(), description.toString());
    }
}