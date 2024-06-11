package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class SameTest {

    @Test
    public void testSameInstance() {
        Object wanted = new Object();
        Object actual = wanted;

        ArgumentMatcher<Object> sameMatcher = new Same(wanted);

        Matcher<Object> sameMatcherInstance = is(sameMatcher);

        assertThat(sameMatcherInstance, is(sameMatcher));
        assertThat(sameMatcher, matches(actual));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDifferentInstances() {
        Object wanted = new Object();
        Object actual = new Object();

        ArgumentMatcher<Object> sameMatcher = new Same(wanted);

        Matcher<Object> sameMatcherDifferentInstances = is(sameMatcher);

        assertThat(sameMatcherDifferentInstances, is(not(sameMatcher)));
        sameMatcher.describeTo(mock(Description.class));
    }

    @Test
    public void testNullValue() {
        Object wanted = null;
        Object actual = null;

        ArgumentMatcher<Object> sameMatcher = new Same(wanted);

        Matcher<Object> sameMatcherNullValues = is(sameMatcher);

        assertThat(sameMatcherNullValues, is(sameMatcher));
        assertThat(sameMatcher, matches(actual));
    }

    @Test
    public void testDifferentTypes() {
        Object wanted = "Hello";
        Object actual = 1;

        ArgumentMatcher<Object> sameMatcher = new Same(wanted);

        Matcher<Object> sameMatcherDifferentTypes = is(sameMatcher);

        assertThat(sameMatcherDifferentTypes, is(not(sameMatcher)));
    }

    @Test
    public void testStringValue() {
        Object wanted = "Hello";
        Object actual = "Hello";

        ArgumentMatcher<Object> sameMatcher = new Same(wanted);

        Matcher<Object> sameMatcherStringValues = is(sameMatcher);

        assertThat(sameMatcherStringValues, is(sameMatcher));
        assertThat(sameMatcher, matches(actual));
    }

    @Test
    public void testCharacterValue() {
        Character wanted = 'A';
        Character actual = 'A';

        ArgumentMatcher<Object> sameMatcher = new Same(wanted);

        Matcher<Object> sameMatcherCharacterValues = is(sameMatcher);

        assertThat(sameMatcherCharacterValues, is(sameMatcher));
        assertThat(sameMatcher, matches(actual));
    }

    @Test
    public void testDescribeTo() {
        Object wanted = new Object();

        ArgumentMatcher<Object> sameMatcher = new Same(wanted);

        Description description = mock(Description.class);

        sameMatcher.describeTo(description);
    }
}