package org.mockito.internal.matchers;

import org.junit.Test;
import org.junit.Assert.*;
import org.hamcrest.Matcher;
import org.hamcrest.Description;

public class SameTest {

    @Test
    public void testSameObject() {
        Object wanted = new Object();
        Matcher<Object> sameMatcher = new Same(wanted);

        Object actual = wanted;
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentObjects() {
        Object wanted = new Object();
        Matcher<Object> sameMatcher = new Same(wanted);

        Object actual = new Object();
        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testSameString() {
        String wanted = "test string";
        Matcher<String> sameMatcher = new Same(wanted);

        String actual = "test string";
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentStrings() {
        String wanted = "test string";
        Matcher<String> sameMatcher = new Same(wanted);

        String actual = "different string";
        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testSameCharacter() {
        char wanted = 'a';
        Matcher<Character> sameMatcher = new Same(wanted);

        Character actual = 'a';
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentCharacters() {
        char wanted = 'a';
        Matcher<Character> sameMatcher = new Same(wanted);

        Character actual = 'b';
        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testDescribeTo() {
        Object wanted = new Object();
        Matcher<Object> sameMatcher = new Same(wanted);

        Description description = new Description();
        sameMatcher.describeTo(description);

        String expectedDescription = "same(test object)";
        String actualDescription = description.toString();
        assertEquals(expectedDescription, actualDescription);
    }
}