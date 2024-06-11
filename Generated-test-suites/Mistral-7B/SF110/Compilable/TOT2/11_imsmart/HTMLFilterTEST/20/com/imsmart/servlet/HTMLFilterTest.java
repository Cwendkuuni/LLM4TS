package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {
    // Test case 1: Filter a valid message
    @Test
    public void testFilterValidMessage() {
        String message = "Hello <world>! This is a & test.";
        String expected = "Hello &lt;world&gt;! This is a &amp; test.";
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }

    // Test case 2: Filter an empty message
    @Test
    public void testFilterEmptyMessage() {
        String message = "";
        String actual = HTMLFilter.filter(message);
        assertEquals(actual, "");
    }

    // Test case 3: Filter a null message
    @Test(expected = NullPointerException.class)
    public void testFilterNullMessage() {
        String message = null;
        HTMLFilter.filter(message);
    }

    // Test case 4: Filter a message with only non-filtered characters
    @Test
    public void testFilterOnlyNormalChars() {
        String message = "This is a normal message.";
        String expected = "This is a normal message.";
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }
}