package org.mockito.internal.matchers;

import static org.junit.Assert.assertThat;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

public class SameTest {

    @Test
    public void testSameString() {
        Object wanted = "foo";
        Matcher<String> matcher = new Same(wanted);

        String actual1 = "foo";
        String actual2 = "foo";

        assertThat(matcher.matches(actual1), is(true));
        assertThat(matcher.matches(actual2), is(true));
    }

    @Test
    public void testSameInteger() {
        Object wanted = 10;
        Matcher<Integer> matcher = new Same(wanted);

        Integer actual1 = 10;
        Integer actual2 = 10;

        assertThat(matcher.matches(actual1), is(true));
        assertThat(matcher.matches(actual2), is(true));
    }

    @Test
    public void testSameCharacter() {
        Object wanted = 'a';
        Matcher<Character> matcher = new Same(wanted);

        Character actual1 = 'a';
        Character actual2 = 'a';

        assertThat(matcher.matches(actual1), is(true));
        assertThat(matcher.matches(actual2), is(true));
    }

    @Test
    public void testSameObject() {
        Object wanted = new Object();
        Matcher<Object> matcher = new Same(wanted);

        Object actual1 = new Object();
        Object actual2 = new Object();

        assertThat(matcher.matches(actual1), is(true));
        assertThat(matcher.matches(actual2), is(true));
    }

    @Test
    public void testDifferentStrings() {
        Object wanted = "foo";
        Matcher<String> matcher = new Same(wanted);

        String actual = "bar";

        assertThat(matcher.matches(actual), is(false));
    }

    @Test
    public void testDifferentObjects() {
        Object wanted = new Object();
        Matcher<Object> matcher = new Same(wanted);

        Object actual = new Object()[1];

        assertThat(matcher.matches(actual), is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWanted() {
        new Same(null);
    }

    private Matcher<Object> is(boolean expected) {
        return ArgumentMatchers.is(expected);
    }
}