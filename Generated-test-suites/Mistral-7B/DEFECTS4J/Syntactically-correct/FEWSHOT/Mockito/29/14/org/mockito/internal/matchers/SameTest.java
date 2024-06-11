package org.mockito.internal.matchers;

import static org.junit.Assert.assertThat;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

public class SameTest {

    @Test
    public void testSameInstance() {
        Object wanted = new Object();
        Matcher<Object> matcher = new Same(wanted);

        Object actual = wanted;
        assertThat(matcher.matches(actual), is(true));
    }

    @Test
    public void testDifferentInstances() {
        Object wanted = new Object();
        Matcher<Object> matcher = new Same(wanted);

        Object actual = new Object();
        assertThat(matcher.matches(actual), is(false));
    }

    @Test
    public void testSameString() {
        String wanted = "foo";
        Matcher<String> matcher = new Same(wanted);

        String actual = "foo";
        assertThat(matcher.matches(actual), is(true));
    }

    @Test
    public void testDifferentString() {
        String wanted = "foo";
        Matcher<String> matcher = new Same(wanted);

        String actual = "bar";
        assertThat(matcher.matches(actual), is(false));
    }

    @Test
    public void testSameCharacter() {
        Character wanted = 'a';
        Matcher<Character> matcher = new Same(wanted);

        Character actual = 'a';
        assertThat(matcher.matches(actual), is(true));
    }

    @Test
    public void testDifferentCharacter() {
        Character wanted = 'a';
        Matcher<Character> matcher = new Same(wanted);

        Character actual = 'b';
        assertThat(matcher.matches(actual), is(false));
    }

    @Test
    public void testSameArrayOfObjects() {
        Object[] wanted = new Object[]{new Object(), new Object()};
        Matcher<Object[]> matcher = new Same(wanted);

        Object[] actual = new Object[]{new Object(), new Object()};
        assertThat(matcher.matches(actual), is(true));
    }

    @Test
    public void testDifferentArrayOfObjects() {
        Object[] wanted = new Object[]{new Object(), new Object()};
        Matcher<Object[]> matcher = new Same(wanted);

        Object[] actual = new Object[]{new Object(), new Object(), new Object()};
        assertThat(matcher.matches(actual), is(false));
    }

    @Test
    public void testSameArrayOfStrings() {
        String[] wanted = new String[]{"foo", "bar"};
        Matcher<String[]> matcher = new Same(wanted);

        String[] actual = new String[]{"foo", "bar"};
        assertThat(matcher.matches(actual), is(true));
    }

    @Test
    public void testDifferentArrayOfStrings() {
        String[] wanted = new String[]{"foo", "bar"};
        Matcher<String[]> matcher = new Same(wanted);

        String[] actual = new String[]{"foo", "baz"};
        assertThat(matcher.matches(actual), is(false));
    }

    @Test
    public void testSameArrayOfCharacters() {
        Character[] wanted = new Character[]{'a', 'b'};
        Matcher<Character[]> matcher = new Same(wanted);

        Character[] actual = new Character[]{'a', 'b'};
        assertThat(matcher.matches(actual), is(true));
    }

    @Test
    public void testDifferentArrayOfCharacters() {
        Character[] wanted = new Character[]{'a', 'b'};
        Matcher<Character[]> matcher = new Same(wanted);

        Character[] actual = new Character[]{'a', 'c'};
        assertThat(matcher.matches(actual), is(false));
    }

    @Test
    public void testSameNull() {
        Object wanted = null;
        Matcher<Object> matcher = new Same(wanted);

        Object actual = null;
        assertThat(matcher.matches(actual), is(true));
    }

    @Test
    public void testDifferentNull() {
        Object wanted = null;
        Matcher<Object> matcher = new Same(wanted);

        Object actual = new Object();
        assertThat(matcher.matches(actual), is(false));
    }

    @Test
    public void testDescribeTo() {
        Object wanted = new Object();
        Matcher<Object> matcher = new Same(wanted);

        Description description = new Description();
        matcher.describeTo(description);

        String expected = "same(" + wanted.toString() + ")";
        String actual = description.toString();
        assertEquals(expected, actual);
    }
}