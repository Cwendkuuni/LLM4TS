package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {

    @Test
    public void testFilterNullMessage() {
        String result = HTMLFilter.filter(null);
        assertEquals(null, result);
    }

    @Test
    public void testFilterEmptyMessage() {
        String message = "";
        String result = HTMLFilter.filter(message);
        assertEquals("", result);
    }

    @Test
    public void testFilterSimpleMessage() {
        String message = "Hello World!";
        String result = HTMLFilter.filter(message);
        String expected = "Hello World!";
        expected = expected.replaceAll("<", "&lt;");
        expected = expected.replaceAll(">", "&gt;");
        expected = expected.replaceAll("&", "&amp;");
        expected = expected.replaceAll("\"", "&quot;");
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterNullCharacterArray() {
        char[] content = null;
        String result = HTMLFilter.filter(new String(content));
        // This test should throw a NullPointerException
    }

    @Test
    public void testFilterMessageWithSpecialCharacters() {
        String message = "<html><head></head><body><h1>&#123;Hello &#65279;World!&#65279;</h1></body></html>";
        String result = HTMLFilter.filter(message);
        String expected = "&lt;html&gt;&lt;head&gt;&lt;!--&gt;&lt;/head&gt;&lt;body&gt;&lt;h1&gt;&amp;#123;Hello &#x203C;World!&#x203E;&amp;#x203C; --&gt;&lt;/h1&gt;&lt;/body&gt;&lt;/html&gt;";
        assertEquals(expected, result);
    }
}