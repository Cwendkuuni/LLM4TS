package com.imsmart.servlet;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {
    @Test
    public void testFilter() {
        String input = "<html><head><title>Test</title></head><body><p>This is a test.</p></body></html>";
        String expectedOutput = "&lt;html&gt;&lt;head&gt;&lt;title&gt;Test&lt;/title&gt;&lt;body&gt;&lt;p&gt;This is a test.&lt;/p&gt;&lt;/body&gt;&lt;/html&gt;";
        String output = HTMLFilter.filter(input);
        assertEquals(expectedOutput, output);
    }
}