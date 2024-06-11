package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;;

public class HTMLFilterTest {
    @Test
    public void testFilterNullInput() {
        String actual = HTMLFilter.filter(null);
        assertNull(actual);
        // Test ends here
    }

    @Test
    public void testFilterEmptyInput() {
        String actual = HTMLFilter.filter("");
        assertEquals("", actual);
    }

    @Test
    public void testFilterSimpleText() {
        String actual = HTMLFilter.filter("Hello World!");
        String expected = "Hello World!";
        assertArrayEquals(expected.toCharArray(), actual.toCharArray());
    }

    @Test
    public void testFilterInputWithSpecialCharacters() {
        String actual = HTMLFilter.filter("<Hello World!>");
        String expected = "&lt;Hello World!&gt;";
        assertArrayEquals(expected.toCharArray(), actual.toCharArray());
    }

    @Test
    public void testFilterInputWithMultipleSpecialCharacters() {
        String actual = HTMLFilter.filter("&<\"Hello World!>");
        String expected = "&amp;&lt;&quot;Hello World!&gt;";
        assertArrayEquals(expected.toCharArray(), actual.toCharArray());
    }
}
