package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {

    // Test case for filter method with null input
    @Test(expected = NullPointerException.class)
    public void testFilterNullInput() {
        HTMLFilter.filter(null);
    }

    // Test case for filter method with empty string input
    @Test
    public void testFilterEmptyInput() {
        String input = "";
        String expected = "";
        String output = HTMLFilter.filter(input);
        assertEquals(expected, output);
    }

    // Test case for filter method with valid input
    @Test
    public void testFilterValidInput() {
        String input = "<html><head><title>Test</title></head><body><p>This is a test.</p></body></html>";
        String expected = "&lt;html&gt;&lt;head&gt;&lt;title&gt;Test&lt;/title&gt;&lt;body&gt;&lt;p&gt;This is a test.&lt;/p&gt;&lt;/body&gt;&lt;/html&gt;";
        String output = HTMLFilter.filter(input);
        assertEquals(expected, output);
    }

    // Test case for filter method with input containing only allowed characters
    @Test
    public void testFilterAllowedCharacters() {
        String input = "Hello World!";
        String expected = "Hello World!";
        String output = HTMLFilter.filter(input);
        assertEquals(expected, output);
    }

    // Test case for filter method with input containing special characters before and after an allowed character
    @Test
    public void testFilterSpecialCharactersBeforeAndAfterAllowedCharacters() {
        String input = "Hello& World!";
        String expected = "Hello&amp; World!";
        String output = HTMLFilter.filter(input);
        assertEquals(expected, output);
    }

    // Test case for filter method with input containing special characters in the middle of a word
    @Test
    public void testFilterSpecialCharactersInTheMiddleOfAWord() {
        String input = "This is a &test& case.";
        String expected = "This is a &amp;test&amp; case.";
        String output = HTMLFilter.filter(input);
        assertEquals(expected, output);
    }

    // Test case for filter method with input containing a script tag
    @Test(expected = IllegalArgumentException.class)
    public void testFilterScriptTag() {
        String input = "<script>alert('Hello World!')</script>";
        HTMLFilter.filter(input);
    }

    // Test case for filter method with input containing a style tag
    @Test
    public void testFilterStyleTag() {
        String input = "<style type='text/css'>body { color: red; }</style>";
        String expected = "&lt;style type='text/css'&gt;body { color: red; }&lt;/style&gt;";
        String output = HTMLFilter.filter(input);
        assertEquals(expected, output);
    }

    // Test case for filter method with input containing an HTML comment
    @Test
    public void testFilterHTMLComment() {
        String input = "<!-- This is a comment -->";
        String expected = "&lt;!-- This is a comment --&gt;";
        String output = HTMLFilter.filter(input);
        assertEquals(expected, output);
    }

    // Test case for filter method with input containing a tag with invalid closing tag
    @Test(expected = IllegalArgumentException.class)
    public void testFilterInvalidTag() {
        String input = "<tag>";
        HTMLFilter.filter(input);
    }

    // Test case for filter method with input containing a tag with missing opening tag
    @Test(expected = IllegalArgumentException.class)
    public void testFilterMissingTag() {
        String input = "</tag>";
        HTMLFilter.filter(input);
    }

}