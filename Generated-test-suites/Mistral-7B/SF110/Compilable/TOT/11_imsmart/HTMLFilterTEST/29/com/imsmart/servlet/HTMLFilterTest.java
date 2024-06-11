package com.imsmart.servlet;
import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilterEmptyString() {
        String message = "";
        String expected = "";
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterSimpleString() {
        String message = "Hello World!";
        String expected = "Hello World!";
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterStringWithSpecialCharacters() {
        String message = "<>&\"'";
        String expected = "&lt;&amp;&gt;&quot;&#39;";
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }
}