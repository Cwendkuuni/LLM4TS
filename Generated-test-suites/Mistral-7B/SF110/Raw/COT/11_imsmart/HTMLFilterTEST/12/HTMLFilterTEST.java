package com.imsmart.servlet.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.immart.servlet.HTMLFilter;

/**
 * Test class for HTMLFilter class.
 */
public class HTMLFilterTest {

    /**
     * Test for filter method with valid input.
     */
    @Test
    public void testFilterValidInput() {
        final String message = "<html><body><h1>Hello & World!</h1></body></html>";
        final String expected = "&lt;html&gt;&lt;body&gt;&lt;h1&gt;Hello & World!&lt;/h1&gt;&lt;/body&gt;&lt;/html&gt;";
        final String result = HTMLFilter.filter(message);
        assertEquals(expected, result);
    }

    /**
     * Test for filter method with null input.
     */
    @Test
    public void testFilterNullInput() {
        String message = null;
        String result = HTMLFilter.filter(message);
        assertNull(result);
    }

    /**
     * Test for filter method with empty input.
     */
    @Test
    public void testFilterEmptyInput() {
        final String message = "";
        final String expected = "";
        final String result = HTMLFilter.filter(message);
        assertEquals(expected, result);
    }

    /**
     * Test for filter method with input containing only whitespaces.
     */
    @Test
    public void testFilterWhitespaceInput() {
        final String message = "   \t\t \t\t\n\t\n ";
        final String expected = "   \t\t \t\t\n\t\n ";
        final String result = HTMLFilter.filter(message);
        assertEquals(expected, result);
    }

    /**
     * Test for filter method with input containing special characters.
     */
    @Test
    public void testFilterSpecialCharactersInput() {
        final String message = "&<>";
        final String expected = "&amp;&lt;&gt;";
        final String result = HTMLFilter.filter(message);
        assertEquals(expected, result);
    }

    /**
     * Test for filter method with input containing script tags.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFilterScriptTagInput() {
        final String message = "<script>alert('Hello!')</script>";
        HTMLFilter.filter(message);
    }
}