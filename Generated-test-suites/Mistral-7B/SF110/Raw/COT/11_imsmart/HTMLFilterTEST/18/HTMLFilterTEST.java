package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {

    @Test
    public void testFilterNullMessage() {
        String result = HTMLFilter.filter(null);
        assertEquals("null", result);
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
        for (int i = 0; i < message.length(); ++i) {
            expected = expected.replaceAll("[&<>\"]", "&" + Character.codePointAt(expected, i) + ";");
            expected = expected.replaceAll("\\&(lt|gt|amp|quot);", "&" + Character.codePointAt(expected, i) + ";");
        }
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterNullCharArray() {
        char[] content = null;
        String result = new String(HTMLFilter.filter(content));
        assertEquals("", result);
    }

    @Test
    public void testFilterMessageWithSpecialChars() {
        String message = "<h1>&quot;Hello&quot; <b>World!</b> &amp; <img src=&quot;image.jpg&quot; alt=&quot;Image&quot; />&gt;</h1>";
        String result = HTMLFilter.filter(message);
        String expected = "&lt;h1&gt;&amp;quot;Hello&amp;quot; &lt;b&gt;World!&lt;/b&gt; &amp; &lt;img src=&amp;quot;image.jpg&amp;quot; alt=&amp;quot;Image&amp;quot; /&gt;&lt;/h1&gt;";
        assertEquals(expected, result);
    }
}