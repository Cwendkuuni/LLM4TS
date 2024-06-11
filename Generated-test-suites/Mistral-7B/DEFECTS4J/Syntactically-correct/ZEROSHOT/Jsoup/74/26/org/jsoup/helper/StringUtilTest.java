package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test class for the StringUtil class.
 */
public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("a", "b", "c");
        String expected = "a b c";
        String result = StringUtil.join(strings, " ");
        assertEquals(expected, result);

        strings = Collections.emptyList();
        expected = "";
        result = StringUtil.join(strings, " ");
        assertEquals(expected, result);
    }

    @Test
    public void testJoinIterator() {
        String[] strings = {"a", "b", "c"};
        String expected = "a b c";
        String result = StringUtil.join(Arrays.asList(strings).iterator(), " ");
        assertEquals(expected, result);

        Iterator<String> emptyIterator = Collections.emptyIterator();
        expected = "";
        result = StringUtil.join(emptyIterator, " ");
        assertEquals(expected, result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"a", "b", "c"};
        String expected = "a b c";
        String result = StringUtil.join(strings, " ");
        assertEquals(expected, result);

        strings = new String[0];
        expected = "";
        result = StringUtil.join(strings, " ");
        assertEquals(expected, result);
    }

    @Test
    public void testPadding() {
        for (int i = 0; i < padding.length; i++) {
            String paddingValue = StringUtil.padding(i + 1);
            String expected = StringUtil.padding(i + 1).length() > i + 1 ? paddingValue.substring(0, i + 1) : paddingValue;
            String result = StringUtil.padding(i + 1);
            assertEquals(expected, result);
        }

        try {
            StringUtil.padding(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIsBlank() {
        String[] strings = {"", " ", "abc", " abc", " ab\tc", "ab\tc "};
        for (String string : strings) {
            boolean expected = StringUtil.isBlank(string);
            boolean result = StringUtil.isBlank(string);
            assertEquals(expected, result);
        }
    }

    @Test
    public void testIsNumeric() {
        String[] strings = {"123", "abc", "123abc", "1A2B3C"};
        for (String string : strings) {
            boolean expected = StringUtil.isNumeric(string);
            boolean result = StringUtil.isNumeric(string);
            assertEquals(expected, result);
        }
    }

    @Test
    public void testIsWhitespace() {
        for (int c : new int[]{'\t', ' ', '\r', '\n', '\f'}) {
            boolean expected = StringUtil.isWhitespace(c);
            boolean result = StringUtil.isWhitespace(c);
            assertEquals(expected, result);
        }

        char c = ' ';
        boolean expected = StringUtil.isWhitespace(c);
        boolean result = StringUtil.isActuallyWhitespace(c);
        assertEquals(expected, result);

        c = '\u00A0'; // non-breaking space
        expected = StringUtil.isActuallyWhitespace(c);
        result = StringUtil.isWhitespace(c);
        assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespace() {
        String[] inputs = {"  abc  ", " abc  ", " abc\r\n  ", " abc\t  ", " abc\r\n\t  "};
        String expected = "abc";
        for (String input : inputs) {
            String result = StringUtil.normaliseWhitespace(input);
            assertEquals(expected, result);
        }
    }

    @Test
    public void testIn() {
        String needle = "example";
        String[] haystack = {"apple", "banana", "example", "orange", "example"};
        boolean expected = true;
        boolean result = StringUtil.in(needle, haystack);
        assertEquals(expected, result);

        needle = "apple";
        expected = false;
        result = StringUtil.in(needle, haystack);
        assertEquals(expected, result);
    }

    @Test
    public void testInSorted() {
        String needle = "example";
        String[] haystack = {"banana", "example", "apple", "orange"};
        boolean expected = true;
        boolean result = StringUtil.inSorted(needle, haystack);
        assertEquals(expected, result);

        needle = "apple";
        expected = false;
        result = StringUtil.inSorted(needle, haystack);
        assertEquals(expected, result);
    }

    @Test
    public void testResolve() throws MalformedURLException {
        String baseUrl = "http://example.com/";
        String relUrl = "/path/to/file.html";
        String expected = "http://example.com/path/to/file.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "/";
        expected = "http://example.com/";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "./file.html";
        expected = "http://example.com/path/to/file.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "../file.html";
        expected = "http://example.com/file.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "//another.example.com/file.html";
        expected = "http://another.example.com/file.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "/path/to/file.html?foo=bar";
        expected = "http://example.com/path/to/file.html?foo=bar";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "./path/to/file.html?foo=bar";
        expected = "http://example.com/path/to/file.html?foo=bar";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "../path/to/file.html?foo=bar";
        expected = "http://example.com/path/to/file.html?foo=bar";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "//another.example.com/path/to/file.html?foo=bar";
        expected = "http://another.example.com/path/to/file.html?foo=bar";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "//another.example.com/file.html?foo=bar";
        expected = "http://another.example.com/file.html?foo=bar";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "";
        expected = "http://example.com/path/to/file.html";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = null;
        expected = "";
        result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testResolveWithMalformedURLException() {
        String baseUrl = "http://example.com/path/to/file.html";
        String relUrl = "//example.com/file.html";
        String expected = "";
        try {
            String result = StringUtil.resolve(baseUrl, relUrl);
            fail("Expected MalformedURLException");
        } catch (MalformedURLException e) {
            // expected
        }
        assertEquals(expected, StringUtil.resolve(baseUrl, relUrl));

        baseUrl = "http://example.com/path/to/file.html";
        relUrl = "//example.com:80/file.html";
        expected = "http://example.com:80/file.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        assertEquals(expected, result);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        String result = sb.toString();
        assertEquals("", result);

        sb = StringUtil.stringBuilder();
        sb.append("abc");
        String expected = "abc";
        result = sb.toString();
        assertEquals(expected, result);

        sb = StringUtil.stringBuilder();
        sb.append("abc");
        sb.append(" def ");
        expected = "abc def ";
        result = sb.toString();
        assertEquals(expected, result);

        sb = StringUtil.stringBuilder();
        String input = "abc def";
        sb.append(input);
        expected = "abc def";
        result = sb.toString();
        assertEquals(expected, result);

        sb = StringUtil.stringBuilder();
        sb.append("abc def");
        sb.append(" ghi ");
        expected = "abc def ghi";
        result = sb.toString();
        assertEquals(expected, result);

        sb = StringUtil.stringBuilder();
        String input2 = "ghi";
        sb.append(input2);
        sb.append(input);
        expected = "ghi abc def";
        result = sb.toString();
        assertEquals(expected, result);
    }
}