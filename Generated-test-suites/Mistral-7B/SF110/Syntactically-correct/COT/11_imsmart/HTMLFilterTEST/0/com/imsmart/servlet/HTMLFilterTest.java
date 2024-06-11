package com.imsmart.servlet.test;

import static org.junit.Assert.*;

import org.junit.Test;

public class HTMLFilterTest {

    @Test
    public void testFilterNullMessage() {
        String expected = null;
        String actual = HTMLFilter.filter(null);
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterEmptyMessage() {
        String expected = "";
        String actual = HTMLFilter.filter("");
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterSingleCharacter() {
        String message = "A";
        String expected = "A";
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }

    @Test
    public void testFilterSpecialCharacters() {
        String message = "<>&\"";
        String expected = "&lt;&gt;&amp;&quot;";
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterNullCharacterArray() {
        char[] content = null;
        String message = new String(content);
        String expected = null;
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testFilterLongMessage() {
        String message = new String(new char[Integer.MAX_VALUE]);
        HTMLFilter.filter(message);
    }

    @Test
    public void testFilterWithSpecialCharactersInside() {
        String message = "This message <contains> & some & special & characters.";
        String expected = "This message &lt;contains&gt; &amp; &amp;special&amp; &amp;characters.";
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }
}