package org.mockito.internal.matchers;

import static org.junit.Assert.*;

import org.hamcrest.Description;
import org.junit.Test;

public class SameTest {

    @Test
    public void testSameObjectMatches() {
        Object wanted = new Object();
        Object actual = wanted;

        ArgumentMatcher<Object> same = new Same(wanted);

        assertTrue(same.matches(actual));
    }

    @Test
    public void testSameStringMatches() {
        String wanted = "foo";
        String actual = wanted;

        ArgumentMatcher<String> same = new Same(wanted);

        assertEquals(wanted, actual);
        assertTrue(same.matches(actual));
    }

    @Test
    public void testSameCharacterMatches() {
        char wanted = 'a';
        char actual = wanted;

        ArgumentMatcher<Character> same = new Same(wanted);

        assertEquals(wanted, actual);
        assertTrue(same.matches(actual));
    }

    @Test
    public void testDifferentObjectsDoesNotMatch() {
        Object wanted = new Object();
        Object actual = new Object();

        ArgumentMatcher<Object> same = new Same(wanted);

        assertFalse(same.matches(actual));
    }

    @Test
    public void testStringWithQuotesAreIgnored() {
        String wanted = "\"foo\"";
        String actual = "\"foo\"";

        ArgumentMatcher<String> same = new Same(wanted);

        assertEquals("foo", wanted);
        assertEquals("foo", actual);
        assertTrue(same.matches(actual));
    }

    @Test
    public void testCharacterWithQuotesAreIgnored() {
        char wanted = '\'a\'';
        char actual = '\'a\'';

        ArgumentMatcher<Character> same = new Same(wanted);

        assertEquals(wanted, actual);
        assertTrue(same.matches(actual));
    }

    @Test
    public void testNullWantedObject() {
        Object wanted = null;
        Object actual = new Object();

        ArgumentMatcher<Object> same = new Same(wanted);

        assertNull(same);
        assertFalse(same.matches(actual));
    }

    @Test
    public void testNullActualObject() {
        Object wanted = new Object();
        Object actual = null;

        ArgumentMatcher<Object> same = new Same(wanted);

        assertNull(actual);
        assertFalse(same.matches(actual));
    }
}