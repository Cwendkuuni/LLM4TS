package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.mockito.internal.matchers.Same.same;

public class SameTest {

    @Test
    public void testSameString() {
        String wanted = "foo";
        String actual = "foo";
        Matcher<String> matcher = same(wanted);
        assertThat(matcher, matcher.matches(actual));
    }

    @Test
    public void testSameObject() {
        Object wanted = new Object();
        Object actual = new Object();
        Matcher<Object> matcher = same(wanted);
        assertThat(matcher, matcher.matches(actual));
    }

    @Test
    public void testSameCharacter() {
        Character wanted = 'a';
        Character actual = 'a';
        Matcher<Character> matcher = same(wanted);
        assertThat(matcher, matcher.matches(actual));
    }

    @Test
    public void testSameNull() {
        Object wanted = null;
        Object actual = null;
        Matcher<Object> matcher = same(wanted);
        assertThat(matcher, matcher.matches(actual));
    }

    @Test
    public void testDifferentObjects() {
        Object wanted = new Object();
        Object actual = new Object(); // different instance
        Matcher<Object> matcher = same(wanted);
        assertFalse(matcher.matches(actual));
    }

    @Test
    public void testDescribeTo() {
        Object wanted = new Object();
        Same matcher = new Same(wanted);
        Description description = new Description();
        matcher.describeTo(description);
        String expectedDescription = String.format("same(%s)", wanted.toString());
        assertEquals(expectedDescription, description.toString());
    }
}