package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test public void testFilterNullInput() {
        String message = null;
        String expected = null;
        String actual = HTMLFilter.filter(message);
        assertNull(actual);
        assertEquals(expected, actual);
    }

    @Test public void testFilterEmptyString() {
        String message = "";
        String expected = "";
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }

    @Test public void testFilterSpecialCharsOnly() {
        String message = "&<>\"";
        String expected = "&amp;&lt;&gt;&quot;";
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }

    @Test public void testFilterBasic() {
        String message = "This is a <test> message";
        String expected = "This is a &lt;test&gt; message";
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }
}