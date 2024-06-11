package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {
    // Test case for filter method with empty string input
    @Test
    public void testFilterEmptyString() {
        String input = "";
        String expectedOutput = "";
        String output = HTMLFilter.filter(input);
        assertEquals(expectedOutput, output);
    }

    // Test case for filter method with valid HTML input
    @Test
    public void testFilterValidHTML() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String expectedOutput = "&lt;html&gt;&lt;head&gt;&lt;title&gt;Test&lt;/title&gt;&lt;body&gt;&lt;h1&gt;Hello World!&lt;/h1&gt;&lt;/body&gt;&lt;/html&gt;";
        String output = HTMLFilter.filter(input);
        assertEquals(expectedOutput, output);
    }
}
