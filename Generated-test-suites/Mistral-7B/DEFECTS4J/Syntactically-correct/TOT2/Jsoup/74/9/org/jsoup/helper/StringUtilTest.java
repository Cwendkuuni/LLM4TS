package org.jsoup.helper.test;

import org.junit.Test;
import org.jsoup.helper.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("apple", "banana", "cherry");
        String result = StringUtil.join(strings, ";");
        String expected = "apple;banana;cherry";
        assertEquals(expected, result);

        strings = Collections.emptyList();
        result = StringUtil.join(strings, ":");
        assertEquals("", result);

        strings = Arrays.asList("", null);
        String sep = ":";
        result = StringUtil.join(strings, sep);
        assertEquals("", result);
    }

    @Test
    public void testJoinIterator() {
        String[] strings = {"apple", "banana", "cherry"};
        Iterator<String> iterator = Arrays.asList(strings).iterator();
        String result = StringUtil.join(iterator, ";");
        String expected = "apple;banana;cherry";
        assertEquals(expected, result);

        iterator = Collections.emptyIterator();
        result = StringUtil.join(iterator, ":");
        assertEquals("", result);

        iterator = Arrays.asList("", null).iterator();
        String sep2 = ":";
        result = StringUtil.join(iterator, sep2);
        assertEquals("", result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"apple", "banana", "cherry"};
        String result = StringUtil.join(strings, ";");
        String expected = "apple;banana;cherry";
        assertEquals(expected, result);

        strings = new String[0];
        result = StringUtil.join(strings, ":");
        assertEquals("", result);

        strings = new String[]{null, ""};
        String sep3 = ":";
        result = StringUtil.join(strings, sep3);
        assertEquals("", result);
    }

    @Test
    public void testPadding() {
        for (int i = 0; i < padding.length; i++) {
            String result = StringUtil.padding(i);
            String expected = padding[i];
            assertEquals(expected, result);
        }

        try {
            StringUtil.padding(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIsBlank() {
        String[] strings = {"", " ", "apple", " apple", " apple  ", " apple   ", " apple    ", " apple     ", " apple      "};
        boolean[] results = {true, true, false, false, false, false, false, false, false};
        for (int i = 0; i < strings.length; i++) {
            boolean result = StringUtil.isBlank(strings[i]);
            assertEquals(results[i], result);
        }
    }

    @Test
    public void testIsNumeric() {
        String[] strings = {"123", "1A3", "123ABC", "123", "123$", "123.0", "12_3"};
        boolean[] results = {true, false, false, true, false, true, true};
        for (int i = 0; i < strings.length; i++) {
            boolean result = StringUtil.isNumeric(strings[i]);
            assertEquals(results[i], result);
        }
    }

    @Test
    public void testIsWhitespace() {
        int[] codes = {0x20, 0x09, 0x0A, 0x0C, 0x0D};
        boolean[] results = {true, true, true, true, true};
        for (int i = 0; i < codes.length; i++) {
            boolean result = StringUtil.isWhitespace(codes[i]);
            assertEquals(results[i], result);
        }

        int code = 0x0E;
        assertFalse(StringUtil.isWhitespace(code));
    }

    @Test
    public void testIsActuallyWhitespace() {
        int[] codes = {0x20, 0x09, 0x0A, 0x0C, 0x0D, 0x160};
        boolean[] results = {true, true, true, true, true, true};
        for (int i = 0; i < codes.length; i++) {
            boolean result = StringUtil.isActuallyWhitespace(codes[i]);
            assertEquals(results[i], result);
        }
    }

    @Test
    public void testNormaliseWhitespace() {
        String string = "   foo\tbar   ";
        String result = StringUtil.normaliseWhitespace(string);
        String expected = " foo\tbar";
        assertEquals(expected, result);

        string = " ";
        result = StringUtil.normaliseWhitespace(string);
        assertEquals(string, result);

        string = "\nfoo\nbar\n";
        result = StringUtil.normaliseWhitespace(string);
        expected = "foo\nbar";
        assertEquals(expected, result);
    }

    @Test
    public void testIn() {
        String needle = "apple";
        String[] haystack = {"apple", "banana", "cherry"};
        boolean result = StringUtil.in(needle, haystack);
        assertTrue(result);

        needle = "orange";
        result = StringUtil.in(needle, haystack);
        assertFalse(result);

        haystack = new String[0];
        result = StringUtil.in(needle, haystack);
        assertFalse(result);

        haystack = null;
        try {
            result = StringUtil.in(needle, haystack);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testInSorted() {
        String needle = "apple";
        String[] haystack = {"apple", "banana", "cherry"};
        boolean result = StringUtil.inSorted(needle, haystack);
        assertTrue(result);

        String[] haystack2 = {"banana", "apple", "cherry"};
        result = StringUtil.inSorted(needle, haystack2);
        assertTrue(result);

        String[] haystack3 = {"apple", "banana", "cherry", "orange"};
        result = StringUtil.inSorted(needle, haystack3);
        assertTrue(result);

        String[] haystack4 = {"orange", "banana", "apple", "cherry"};
        result = StringUtil.inSorted(needle, haystack4);
        assertFalse(result);

        haystack4 = new String[0];
        try {
            result = StringUtil.inSorted(needle, haystack4);
            fail();
        } catch (NullPointerException e) {
            // expected
        }

        haystack4 = null;
        try {
            result = StringUtil.inSorted(needle, haystack4);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testResolve() {
        String base = "http://example.com/";
        String relUrl = "/test.html";
        String result = StringUtil.resolve(base, relUrl);
        String expected = "http://example.com/test.html";
        assertEquals(expected, result);

        base = "http://example.com/";
        relUrl = "../images/logo.png";
        result = StringUtil.resolve(base, relUrl);
        expected = "http://example.com/images/logo.png";
        assertEquals(expected, result);

        base = "http://example.com/";
        relUrl = "http://example2.com/index.html";
        result = StringUtil.resolve(base, relUrl);
        assertEquals("http://example2.com/index.html", result);

        base = "http://example.com/";
        relUrl = "//example2.com/index.html";
        result = StringUtil.resolve(base, relUrl);
        expected = "http://example2.com/index.html";
        assertEquals(expected, result);

        base = "http://example.com/";
        relUrl = "//example2.com/index.html?param1=value1";
        result = StringUtil.resolve(base, relUrl);
        expected = "http://example2.com/index.html?param1=value1";
        assertEquals(expected, result);

        base = "http://example.com/";
        relUrl = "//example2.com/index.html?param1=value1#fragment";
        result = StringUtil.resolve(base, relUrl);
        expected = "http://example2.com/index.html?param1=value1#fragment";
        assertEquals(expected, result);

        base = "http://example.com/";
        relUrl = "//example2.com/index.html";
        try {
            result = StringUtil.resolve(base, relUrl);
            fail();
        } catch (MalformedURLException e) {
            // expected
        }

        base = null;
        relUrl = "//example2.com/index.html";
        try {
            result = StringUtil.resolve(base, relUrl);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testResolveEmptyBase() {
        String relUrl = "//example.com/index.html";
        String result = StringUtil.resolve("", relUrl);
        assertEquals(relUrl, result);
    }
}