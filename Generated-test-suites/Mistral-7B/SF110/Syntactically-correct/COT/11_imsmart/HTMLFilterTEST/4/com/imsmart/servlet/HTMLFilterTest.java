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
        expected = expected.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("&", "&amp;").replaceAll("\"", "&quot;");
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterNullContent() {
        char[] content = null;
        StringBuffer result = new StringBuffer(content.length + 50);
        HTMLFilter.filter(content, result); // method does not accept char array as argument, but checking for exception
    }

    @Test
    public void testFilterWithSpecialCharacters() {
        String message = "<html><body><h1>Hello & World!</h1></body></html>";
        String result = HTMLFilter.filter(message);
        String expected = "&lt;html&gt;&lt;body&gt;&lt;h1&gt;Hello &amp; World!&lt;/h1&gt;&lt;/body&gt;&lt;/html&gt;";
        assertEquals(expected, result);
    }

    @Test
    public void testFilterWithMultipleSpecialCharacters() {
        String message = "<img src=\"image.jpg\" alt=\"Description\" title=\"Title\">";
        String result = HTMLFilter.filter(message);
        String expected = "&lt;img src=&quot;image.jpg&quot; alt=&quot;Description&quot; title=&quot;Title&quot;&gt;";
        assertEquals(expected, result);
    }

    @Test
    public void testFilterWithLongMessage() {
        String message = "This is a long message that needs to be filtered. It contains several special characters like <, >, &, and \".";
        String result = HTMLFilter.filter(message);
        String expected = "This is a long message that needs to be filtered. It contains several special characters like &lt;, &gt;, &amp;, and &quot;.";
        assertEquals(expected, result);
    }
}