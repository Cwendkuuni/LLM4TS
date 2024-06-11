package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String expectedOutput = "&lt;html&gt;&lt;head&gt;&lt;title&gt;Test&lt;/title&gt;&lt;body&gt;&lt;h1&gt;Hello World!&lt;/h1&gt;&lt;/body&gt;&lt;/html&gt;";
        String output = HTMLFilter.filter(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testFilterEmptyString() {
        String input = "";
        String expectedOutput = "";
        String output = HTMLFilter.filter(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterNullInput() {
        String input = null;
        String output = HTMLFilter.filter(input);
    }

    @Test
    public void testFilterSpecialCharacters() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html><!-- Comment --><script>alert('Hello')</script>";
        String expectedOutput = "&lt;html&gt;&lt;head&gt;&lt;title&gt;Test&lt;/title&gt;&lt;body&gt;&lt;h1&gt;Hello World!&lt;/h1&gt;&lt;!-- Comment --&gt;&lt;script&gt;&amp;lt; alert(&#39;Hello&#39;)&amp;gt;&lt;/script&gt;&lt;/body&gt;&lt;/html&gt;";
        String output = HTMLFilter.filter(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testFilterLargeInput() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html><!-- Comment --><script>alert('Hello')</script>";
        final char[] content = new char[input.length()];
        input.getChars(0, input.length(), content, 0);
        String expectedOutput = new String(content, 0, input.length()) + new String(new char[50]);
        String output = HTMLFilter.filter(new String(content));
        assertEquals(expectedOutput, output);
    }
}