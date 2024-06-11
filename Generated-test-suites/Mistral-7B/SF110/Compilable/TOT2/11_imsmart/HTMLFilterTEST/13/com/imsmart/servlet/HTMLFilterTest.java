package com.imsmart.servlet;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {
    @Test
    public void testFilter() {
        String input = "<h1>Hello World</h1>";
        String expectedOutput = "&lt;h1&gt;Hello World&lt;/h1&gt;";
        String output = HTMLFilter.filter(input);
        assertEquals(expectedOutput, output);
    }
}