package com.imsmart.servlet.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.imsmart.servlet.HTMLFilter;

public class HTMLFilterTest {

    @Test
    public void testFilterNullMessage() {
        String result = HTMLFilter.filter(null);
        assertNull(result);
    }

    @Test
    public void testFilterEmptyMessage() {
        String message = "";
        String result = HTMLFilter.filter(message);
        assertEquals("", result);
    }

    @Test
    public void testFilterSimpleMessage() {
        String message = "<html>Hello World</html>";
        String result = HTMLFilter.filter(message);
        String expected = "&lt;html&gt;Hello World&lt;/html&gt;";
        assertEquals(expected, result);
    }

    @Test
    public void testFilterMessageWithSingleSpecialCharacter() {
        String message = "<script type='text/javascript'>alert('XSS');</script>";
        String result = HTMLFilter.filter(message);
        String expected = "&lt;script type='text/javascript'&gt;alert('XSS');&lt;/script&gt;";
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterNullCharacterArray() {
        char[] content = null;
        String result = HTMLFilter.filter(content);
        assertNull(result);
    }

    @Test
    public void testFilterMessageWithMultipleSpecialCharacters() {
        String message = "<script type='text/javascript' src='http://example.com/script.js'><![CDATA[ alert('XSS'); ]]></script>";
        String result = HTMLFilter.filter(message);
        String expected = "&lt;script type='text/javascript' src='http://example.com/script.js'&gt;&lt;![CDATA[ alert('XSS'); ]]&gt;&lt;/script&gt;";
        assertEquals(expected, result);
    }

    @Test
    public void testFilterMessageWithQuotes() {
        String message = "It's <i>really</i> great!";
        String result = HTMLFilter.filter(message);
        String expected = "It's &lt;i&gt;really&lt;/i&gt; great!";
        assertEquals(expected, result);
    }

    @Test
    public void testFilterMessageWithAmpersand() {
        String message = "This & that & this";
        String result = HTMLFilter.filter(message);
        String expected = "This &amp; that &amp; this";
        assertEquals(expected, result);
    }
}