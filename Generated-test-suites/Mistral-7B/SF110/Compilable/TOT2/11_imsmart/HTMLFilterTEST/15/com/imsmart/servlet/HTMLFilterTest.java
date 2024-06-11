package com.imsmart.servlet;
import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter() {
        String input = "<html><body><h1>Welcome</h1></body></html>";
        String expectedOutput = "&lt;html&gt;&lt;body&gt;&lt;h1&gt;Welcome&lt;h1&gt;&lt;/body&gt;&lt;/html&gt;";
        String output = HTMLFilter.filter(input);
        assertEquals(expectedOutput, output);
    }
}