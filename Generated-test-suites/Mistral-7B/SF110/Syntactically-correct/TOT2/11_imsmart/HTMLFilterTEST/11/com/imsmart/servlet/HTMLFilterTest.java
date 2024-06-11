package com.imsmart.servlet.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {
    @Test
    public void testFilter() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String expected = "&lt;html&gt;&lt;head&gt;&lt;title&gt;Test&lt;/title&gt;&lt;body&gt;&lt;h1&gt;Hello World!&lt;/h1&gt;&lt;/body&gt;&lt;/html&gt;";
        String output = HTMLFilter.filter(input);
        assertEquals(expected, output);
    }

    @Test
    public void testFilter_EmptyString() {
        String input = "";
        String output = HTMLFilter.filter(input);
        assertEquals(output, "");
    }

    @Test(expected = NullPointerException.class)
    public void testFilter_NullInput() {
        String input = null;
        String output = HTMLFilter.filter(input);
        assertNull(output);
    }

    @Test
    public void testFilter_WhitespaceInput() {
        String input = "   ";
        String output = HTMLFilter.filter(input);
        assertEquals(output, "");
    }

    @Test
    public void testFilter_MixedInput() {
        String input = "<html><head><title>Test & < & > & quotes: \" & ' & &amp; &gt; </title></head><body><h1>Hello & World! & <>& '& " & "& </h1></body></html>";
        String expected = "&lt;html&gt;&lt;head&gt;&lt;title&gt;Test & &lt; & &amp; &gt; & quotes: &quot; & &apos; & &amp; &gt; &lt;/title&gt;&lt;body&gt;&lt;h1&gt;Hello &amp; World! & &lt; & &amp; &gt; &lt;/h1&gt;&lt;/body&gt;&lt;/html&gt;";
        String output = HTMLFilter.filter(input);
        assertEquals(expected, output);
    }
}