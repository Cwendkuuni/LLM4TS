package org.mockito.internal.matchers;

import org.hamcrest.Matcher;
import org.hamcrest.Description;
import org.junit.Test;
import static org.junit.Assert.assertThat;

public class SameTest {

    @Test
    public void testSameObjects() {
        Object wanted = new Object();
        Object actual = wanted;

        Same sameMatcher = new Same(wanted);

        Matcher matcher = sameMatcher;
        assertThat(matcher, matcher.matches(actual));
        assertThat(matcher, matcher.matches(wanted));
    }

    @Test
    public void testDifferentObjects() {
        Object wanted = new Object();
        Object actual = new Object();

        Same sameMatcher = new Same(wanted);

        Matcher matcher = sameMatcher;
        assertThat(matcher, !matcher.matches(actual));
        assertThat(matcher, !matcher.matches(wanted));
    }

    @Test
    public void testSameStrings() {
        String wanted = "test string";
        String actual = wanted;

        Same sameMatcher = new Same(wanted);

        Matcher matcher = sameMatcher;
        assertThat(matcher, matcher.matches(actual));
        assertThat(matcher, matcher.matches(wanted));
    }

    @Test
    public void testDifferentStrings() {
        String wanted = "test string";
        String actual = "different string";

        Same sameMatcher = new Same(wanted);

        Matcher matcher = sameMatcher;
        assertThat(matcher, !matcher.matches(actual));
        assertThat(matcher, !matcher.matches(wanted));
    }

    @Test
    public void testSameCharacter() {
        Character wanted = 'a';
        Character actual = wanted;

        Same sameMatcher = new Same(wanted);

        Matcher matcher = sameMatcher;
        assertThat(matcher, matcher.matches(actual));
        assertThat(matcher, matcher.matches(wanted));
    }

    @Test
    public void testDifferentCharacter() {
        Character wanted = 'a';
        Character actual = 'b';

        Same sameMatcher = new Same(wanted);

        Matcher matcher = sameMatcher;
        assertThat(matcher, !matcher.matches(actual));
        assertThat(matcher, !matcher.matches(wanted));
    }

    @Test
    public void testDescribeTo() {
        Object wanted = new Object();

        Same sameMatcher = new Same(wanted);

        Description description = new Description();
        Same same = new Same(wanted);
        same.describeTo(description);

        String expectedDescription = "same(test object)";
        String actualDescription = description.toString();

        assertThat(actualDescription, expectedDescription);
    }
}