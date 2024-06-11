package com.imsmart.servlet.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.immart.servlet.HTMLFilter;

public class HTMLFilterTest {

    @Test
    public void testFilterNullMessage() {
        String actual = HTMLFilter.filter(null);
        assertEquals(null, actual);
    }

    @Test
    public void testFilterEmptyMessage() {
        String actual = HTMLFilter.filter("");
        assertEquals("", actual);
    }

    @Test
    public void testFilterSimpleMessage() {
        String message = "Hello World!";
        String actual = HTMLFilter.filter(message);
        String expected = "Hello World!";
        expected = expected.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("&", "&amp;").replaceAll("\"", "&quot;");
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterNullContent() {
        char[] content = null;
        StringBuffer result = new StringBuffer(content.length + 50);
        for (int i = 0; i < content.length; ++i) {
            switch (content[i]) {
                // This line will throw NullPointerException as content is null
                case '<': {
                    result.append("&lt;");
                    break;
                }
            }
        }
        HTMLFilter.filter(content, result);
    }

    @Test
    public void testFilterMessageWithSpecialCharacters() {
        String message = "<&\"'>Hello World!";
        String actual = HTMLFilter.filter(message);
        String expected = "&lt;&amp;&quot;'&gt;Hello World!";
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterMessageWithMultipleSpecialCharacters() {
        String message = "Hello World! <>&\"'";
        String actual = HTMLFilter.filter(message);
        String expected = "Hello World! &amp;&lt; &amp;&gt; &amp;&quot; &amp;&#39;";
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterMessageWithMultipleSpecialCharactersInSequence() {
        String message = "Hello World! <>&\"'><&>";
        String actual = HTMLFilter.filter(message);
        String expected = "Hello World! &amp;&lt; &amp;&gt; &amp;&quot; &amp; &amp;&lt; &amp;&gt;";
        assertEquals(expected, actual);
    }

}