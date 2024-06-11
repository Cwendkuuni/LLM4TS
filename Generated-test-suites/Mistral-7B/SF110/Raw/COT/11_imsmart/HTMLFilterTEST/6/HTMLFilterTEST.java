package com.imsmart.servlet.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.immart.servlet.HTMLFilter;

public class HTMLFilterTest {

    @Test
    public void testFilterNullMessage() {
        String result = HTMLFilter.filter(null);
        assertEquals(null, result);
    }

    @Test
    public void testFilterEmptyMessage() {
        String result = HTMLFilter.filter("");
        assertEquals("", result);
    }

    @Test
    public void testFilterSimpleMessage() {
        String message = "Hello, World!";
        String result = HTMLFilter.filter(message);
        String expected = "Hello, World!";
        expected = expected.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("&", "&amp;").replaceAll("\"", "&quot;");
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterNullCharArray() {
        char[] content = null;
        StringBuffer result = new StringBuffer(content.length + 50);
        HTMLFilter.filter(content, result);
    }

    @Test
    public void testFilterWithSpecialCharacters() {
        String message = "<html><head><title>&quot;Hello&World&quot;</title></head><body>&amp;<img src=&quot;image.jpg&quot; alt=&quot;Test Image&quot;></body></html>";
        String result = HTMLFilter.filter(message);
        String expected = "&lt;html&gt;&lt;head&gt;&lt;title&gt;&amp;quot;Hello&World&quot;&lt;/title&gt;&lt;/head&gt;&lt;body&gt;&amp;amp;&lt;img src=&amp;quot;image.jpg&amp;quot; alt=&amp;quot;Test Image&amp;quot;&gt;&lt;/body&gt;&lt;/html&gt;";
        assertEquals(expected, result);
    }

}