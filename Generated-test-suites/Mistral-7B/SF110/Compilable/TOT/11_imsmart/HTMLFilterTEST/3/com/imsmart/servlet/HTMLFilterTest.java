package com.imsmart.servlet;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {

    @Test
    public void testFilterNullMessage() {
        String actual = HTMLFilter.filter(null);
        assertEquals(null, actual);
    }

    @Test
    public void testFilterEmptyMessage() {
        String message = "";
        String actual = HTMLFilter.filter(message);
        assertEquals("", actual);
    }

    @Test
    public void testFilterSimpleMessage() {
        String message = "Hello World!";
        String actual = HTMLFilter.filter(message);
        String expected = "Hello World!";
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterMessageWithSpecialCharacters() {
        String message = "<Hello & World!>";
        String actual = HTMLFilter.filter(message);
        String expected = "&lt;Hello &amp; &amp; World!&gt;";
        assertEquals(expected, actual);
    }
}