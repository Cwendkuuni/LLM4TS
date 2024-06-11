package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*; 

public class HTMLFilterTest {
    
    @Test
    public void testFilterEmptyString() {
        String input = "";
        String expected = "";
        String output = HTMLFilter.filter(input);
        assertEquals(expected, output);
    }
    
    @Test
    public void testFilterNullString() {
        String input = null;
        String expected = null;
        String output = HTMLFilter.filter(input);
        assertEquals(expected, output);
    }
    
    @Test
    public void testFilterSimpleMessage() {
        String input = "Hello World!";
        String expected = "Hello World!";
        String output = HTMLFilter.filter(input);
        assertEquals(expected, output);
    }
    
    @Test
    public void testFilterSpecialCharacters() {
        String input = "<>&\"'";
        String expected = "&lt;&gt;&amp;&quot;&apos;";
        String output = HTMLFilter.filter(input);
        assertEquals(expected, output);
    }
    
    @Test(expected = NullPointerException.class)
    public void testFilterNullCharArray() {
        char[] input = null;
        String output = HTMLFilter.filter(input);
    }
    
    @Test
    public void testFilterLongMessage() {
        String input = "This is a long message that should be filtered properly. All special characters should be replaced with their corresponding HTML entities.";
        String expected = "This is a long message that should be filtered properly. All special characters should be replaced with their corresponding HTML entities.";
        String output = HTMLFilter.filter(input);
        assertEquals(expected, output);
    }
    
    @Test
    public void testFilterInputWithHTMLTag() {
        String input = "<p>This is a paragraph.</p>";
        String expected = "&lt;p&gt;This is a paragraph.&lt;/p&gt;";
        String output = HTMLFilter.filter(input);
        assertEquals(expected, output);
    }
}