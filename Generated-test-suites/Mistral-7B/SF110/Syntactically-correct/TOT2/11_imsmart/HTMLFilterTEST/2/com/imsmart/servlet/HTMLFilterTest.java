package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter() {
        String input = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        String expectedOutput = "&lt;html&gt;&lt;head&gt;&lt;title&gt;Test&lt;/title&gt;&lt;body&gt;&lt;p&gt;Hello World&lt;/p&gt;&lt;/body&gt;&lt;/html&gt;";
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
    public void testFilterNullString() {
        String input = null;
        String output = HTMLFilter.filter(input);
    }

    @Test
    public void testFilterWhitespaceString() {
        String input = "   ";
        String expectedOutput = " ";
        String output = HTMLFilter.filter(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testFilterSpecialCharacter() {
        String input = "<!DOCTYPE html>";
        String expectedOutput = "&lt;!DOCTYPE html&gt;";
        String output = HTMLFilter.filter(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testFilterHTMLTagWithAttribute() {
        String input = "<img src=\"image.jpg\">";
        String expectedOutput = "&lt;img src=&quot;image.jpg&quot;&gt;";
        String output = HTMLFilter.filter(input);
        assertEquals(expectedOutput, output);
    }
}