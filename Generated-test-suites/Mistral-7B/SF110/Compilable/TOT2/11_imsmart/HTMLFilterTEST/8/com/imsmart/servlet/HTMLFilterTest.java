package com.imsmart.servlet;
import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilterEmptyMessage() {
        String input = "";
        String expected = "";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }

    @Test
    public void testFilterNullMessage() {
        String input = null;
        String expected = null;
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }

    @Test
    public void testFilterSpecialCharacters() {
        String input = "This message contains special characters: !@#$%^&*()_+-={}[]|\\:\"<>?";
        String expected = "This message contains special characters: &amp;!&amp;amp;@&amp;amp;#&amp;amp;$&amp;amp;%&amp;amp;^&amp;amp;&amp;amp;*&amp;amp;(&amp;amp;_)&amp;amp;+&amp;amp;-&=amp;amp;{}&amp;amp;}&amp;amp;|&amp;amp;\\:&amp;amp;&quot;<&amp;amp;?>";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }

    @Test
    public void testFilter() {
        String input = "<html><body><h1>Hello World</h1></body></html>";
        String expected = "&lt;html&gt;&lt;body&gt;&lt;h1&gt;Hello World&lt;/h1&gt;&lt;/body&gt;&lt;/html&gt;";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }
}