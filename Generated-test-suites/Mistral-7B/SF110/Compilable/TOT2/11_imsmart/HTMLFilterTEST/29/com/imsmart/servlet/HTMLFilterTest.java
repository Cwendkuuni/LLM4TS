
package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter() {
        String message = "<html><head><title>Test</title></head><body>";
        String expected = "&lt;html&gt;&lt;head&gt;&lt;title&gt;Test&lt;/title&gt;&lt;/head&gt;&lt;body&gt;";
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterEmptyString() {
        String actual = HTMLFilter.filter("");
        assertEquals("", actual);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterNullInput() {
        HTMLFilter.filter(null);
    }

    @Test
    public void testFilterSpecialCharacter() {
        String message = "This is a & test";
        String expected = "This is a &amp; test";
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterSingleCharacter() {
        String message = "<";
        String expected = "&lt;";
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterMultipleCharacters() {
        String message = "<tag & attr=\"value\">Content</tag>";
        String expected = "&lt;tag &amp; attr=&quot;value&quot;&gt;Content&lt;/tag&gt;";
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterMultipleSpecialCharacters() {
        String message = "<tag & & attr=\"value & value\">Content & < tag & /tag>";
        String expected = "&lt;tag &amp; &amp; amp; attr=&quot;value &amp; value&quot;&gt;Content &lt;tag &amp; &amp; lt; &amp; slash&gt;tag&lt;/tag&gt;";
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }
}