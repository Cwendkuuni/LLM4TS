package org.jsoup.helper;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> list = Arrays.asList("hello", "world", "!", "test");
        String expected = "hello world ! test";
        String result = StringUtil.join(list, "");
        assertEquals(expected, result);

        List<String> emptyList = Collections.emptyList();
        String emptyResult = "";
        result = StringUtil.join(emptyList, "");
        assertEquals(emptyResult, result);
    }

    @Test
    public void testJoinIterator() {
        String[] array = new String[]{"hello", "world", "!", "test"};
        Iterator<String> iterator = Arrays.asList(array).iterator();
        String expected = "hello world ! test";
        String result = StringUtil.join(iterator, "");
        assertEquals(expected, result);

        String[] emptyArray = new String[0];
        iterator = Arrays.asList(emptyArray).iterator();
        String emptyResult = "";
        result = StringUtil.join(iterator, "");
        assertEquals(emptyResult, result);
    }

    @Test
    public void testJoinArray() {
        String[] array = new String[]{"hello", "world", "!", "test"};
        String expected = "hello world ! test";
        String result = StringUtil.join(array, "");
        assertEquals(expected, result);

        String[] emptyArray = new String[0];
        String emptyResult = "";
        result = StringUtil.join(emptyArray, "");
        assertEquals(emptyResult, result);
    }

    @Test
    public void testPadding() {
        for (int i = 0; i < StringUtil.padding.length; i++) {
            String expected = StringUtil.padding(i);
            String result = StringUtil.padding(i);
            assertEquals(expected, result);
        }
    }

    @Test
    public void testIsBlank() {
        String empty = "";
        String nullString = null;
        String spaces = "   ";
        String trimmedSpaces = "   \t  ";
        String whitespace = "\n\r\f\t ";
        String whitespace2 = "\n\r\f\t \t ";
        String nonBlank = "abc";
        String nonBlank2 = "123";

        assertTrue(StringUtil.isBlank(empty));
        assertTrue(StringUtil.isBlank(nullString));
        assertTrue(StringUtil.isBlank(spaces));
        assertTrue(StringUtil.isBlank(trimmedSpaces));
        assertTrue(StringUtil.isBlank(whitespace));
        assertTrue(StringUtil.isBlank(whitespace2));
        assertFalse(StringUtil.isBlank(nonBlank));
        assertFalse(StringUtil.isBlank(nonBlank2));
    }

    @Test
    public void testIsNumeric() {
        String empty = "";
        String nullString = null;
        String number = "123";
        String nonNumber = "abc";
        String nonNumber2 = "!@#";
        String numberWithDashes = "123-456";

        assertFalse(StringUtil.isNumeric(empty));
        assertFalse(StringUtil.isNumeric(nullString));
        assertTrue(StringUtil.isNumeric(number));
        assertFalse(StringUtil.isNumeric(nonNumber));
        assertFalse(StringUtil.isNumeric(nonNumber2));
        assertTrue(StringUtil.isNumeric(numberWithDashes));
    }

    @Test
    public void testIsWhitespace() {
        int space = 32;
        int tab = 9;
        int linefeed = 10;
        int carriageReturn = 13;
        int formFeed = 12;
        int nonWhitespace = 48;

        assertTrue(StringUtil.isWhitespace(space));
        assertTrue(StringUtil.isWhitespace(tab));
        assertTrue(StringUtil.isWhitespace(linefeed));
        assertTrue(StringUtil.isWhitespace(carriageReturn));
        assertTrue(StringUtil.isWhitespace(formFeed));
        assertFalse(StringUtil.isWhitespace(nonWhitespace));
    }

    @Test
    public void testIsActuallyWhitespace() {
        int space = 32;
        int tab = 9;
        int linefeed = 10;
        int carriageReturn = 13;
        int formFeed = 12;
        int nonBreakingSpace = 160;

        assertTrue(StringUtil.isActuallyWhitespace(space));
        assertTrue(StringUtil.isActuallyWhitespace(tab));
        assertTrue(StringUtil.isActuallyWhitespace(linefeed));
        assertTrue(StringUtil.isActuallyWhitespace(carriageReturn));
        assertTrue(StringUtil.isActuallyWhitespace(formFeed));
        assertTrue(StringUtil.isActuallyWhitespace(nonBreakingSpace));
    }

    @Test
    public void testNormaliseWhitespace() {
        String multipleSpaces = "hello   world\n   \t";
        String expected = "hello world";
        String result = StringUtil.normaliseWhitespace(multipleSpaces);
        assertEquals(expected, result);
    }

    @Test
    public void testIn() {
        String haystack1 = "hello world! test";
        String needle1 = "world";
        String needle2 = "test";
        String needle3 = "hello";

        assertTrue(StringUtil.in(needle1, haystack1));
        assertTrue(StringUtil.in(needle2, haystack1));
        assertFalse(StringUtil.in(needle3, haystack1));

        String haystack2 = "hello world test world test test world";
        assertTrue(StringUtil.in(needle1, haystack2));
        assertTrue(StringUtil.in(needle2, haystack2));
        assertTrue(StringUtil.in(needle3, haystack2));
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"hello", "world", "test"};
        String needle = "world";

        assertTrue(StringUtil.inSorted(needle, haystack));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJoinWithNegativeWidth() {
        String[] strings = {"hello", "world"};
        int width = -1;
        try {
            String result = StringUtil.join(strings, StringUtil.padding(width));
        } catch (IllegalArgumentException e) {
            assertEquals("width must be > 0", e.getMessage());
            throw e;
        }
    }
}