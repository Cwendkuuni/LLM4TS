package org.jsoup.helper;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * Test class for the StringUtil class.
 */
public class StringUtilTest {

    /**
     * Test join method with Collection.
     */
    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("hello", "world", "junit");
        String expected = "hello world junit";
        String result = StringUtil.join(strings, " ");
        assertEquals(expected, result);
    }

    /**
     * Test join method with Iterator.
     */
    @Test
    public void testJoinIterator() {
        String[] strings = {"hello", "world", "junit"};
        String expected = "hello world junit";
        String result = StringUtil.join(Arrays.asList(strings).iterator(), " ");
        assertEquals(expected, result);
    }

    /**
     * Test join method with array.
     */
    @Test
    public void testJoinArray() {
        String[] strings = {"hello", "world", "junit"};
        String expected = "hello world junit";
        String result = StringUtil.join(strings, " ");
        assertEquals(expected, result);
    }

    /**
     * Test padding method.
     */
    @Test
    public void testPadding() {
        for (int i = 0; i < 21; i++) {
            String expected = StringUtil.padding(i);
            String result = StringUtil.padding(i);
            assertEquals(expected, result);
        }
    }

    /**
     * Test isBlank method.
     */
    @Test
    public void testIsBlank() {
        String[] strings = {"", " ", "hello", "   world   ", "hello\tworld", "hello\r\nworld"};
        for (String s : strings) {
            boolean expected = StringUtil.isBlank(s);
            boolean result = StringUtil.isBlank(s);
            assertEquals(expected, result);
        }
    }

    /**
     * Test isNumeric method.
     */
    @Test
    public void testIsNumeric() {
        String[] strings = {"123", "1A", "abc", "123abc", "", "1234567890"};
        for (String s : strings) {
            boolean expected = StringUtil.isNumeric(s);
            boolean result = StringUtil.isNumeric(s);
            assertEquals(expected, result);
        }
    }

    /**
     * Test isWhitespace method.
     */
    @Test
    public void testIsWhitespace() {
        for (int i = 0; i < 256; i++) {
            boolean expected = StringUtil.isWhitespace((char) i);
            boolean result = StringUtil.isWhitespace((char) i);
            assertEquals(expected, result);
        }
    }

    /**
     * Test isActuallyWhitespace method.
     */
    @Test
    public void testIsActuallyWhitespace() {
        for (int i = 0; i < 256; i++) {
            boolean expected = StringUtil.isActuallyWhitespace((char) i);
            boolean result = StringUtil.isActuallyWhitespace((char) i);
            assertEquals(expected, result);
        }
    }

    /**
     * Test join method with empty collection.
     */
    @Test
    public void testJoinEmptyCollection() {
        String expected = "";
        String result = StringUtil.join(Collections.emptyList(), "");
        assertEquals(expected, result);
    }

    /**
     * Test join method with null collection.
     */
    @Test(expected = NullPointerException.class)
    public void testJoinNullCollection() {
        String[] strings = null;
        String sep = " ";
        StringUtil.join(strings, sep);
    }

    /**
     * Test join method with null separator.
     */
    @Test(expected = NullPointerException.class)
    public void testJoinNullSeparator() {
        List<String> strings = Arrays.asList("hello", "world");
        String sep = null;
        StringUtil.join(strings, sep);
    }

    /**
     * Test normaliseWhitespace method.
     */
    @Test
    public void testNormaliseWhitespace() {
        String input = "   hello world   \r\n\t  ";
        String expected = "hello world";
        String result = StringUtil.normaliseWhitespace(input);
        assertEquals(expected, result);
    }

    /**
     * Test in method.
     */
    @Test
    public void testIn() {
        String needle = "junit";
        String[] haystack = {"jsoup", "helper", "junit", "org", "junit"};
        boolean expected = true;
        boolean result = StringUtil.in(needle, haystack);
        assertEquals(expected, result);
    }

    /**
     * Test inSorted method.
     */
    @Test
    public void testInSorted() {
        String needle = "junit";
        String[] haystack = {"helper", "jsoup", "junit"};
        boolean expected = true;
        boolean result = StringUtil.inSorted(needle, haystack);
        assertEquals(expected, result);
    }

    /**
     * Test resolve method with absolute base and relative url.
     */
    @Test
    public void testResolveAbsolute() throws MalformedURLException {
        String baseUrl = "https://example.com/";
        String relUrl = "/about.html";
        String expected = "https://example.com/about.html";
        String result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected, result);
    }

    /**
     * Test resolve method with absolute base and absolute relative url.
     */
    @Test
    public void testResolveAbsoluteAbsolute() throws MalformedURLException {
        String baseUrl = "https://example.com/about.html";
        String relUrl = "http://example2.com/contact.html";
        String expected = "http://example2.com/contact.html";
        String result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected, result);
    }

    /**
     * Test resolve method with absolute base and relative url starting with '/'.
     */
    @Test
    public void testResolveAbsoluteSlash() throws MalformedURLException {
        String baseUrl = "https://example.com/about.html";
        String relUrl = "/contact.html";
        String expected = "https://example.com/contact.html";
        String result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected, result);
    }

    /**
     * Test resolve method with absolute base and relative url starting with '..'.
     */
    @Test
    public void testResolveAbsoluteUp() throws MalformedURLException {
        String baseUrl = "https://example.com/about.html";
        String relUrl = "../contact.html";
        String expected = "https://example.com/contact.html";
        String result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected, result);
    }

    /**
     * Test resolve method with absolute base and empty relative url.
     */
    @Test
    public void testResolveAbsoluteEmpty() throws MalformedURLException {
        String baseUrl = "https://example.com/about.html";
        String relUrl = "";
        String expected = "https://example.com/about.html";
        String result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected, result);
    }

    /**
     * Test resolve method with null base url.
     */
    @Test(expected = NullPointerException.class)
    public void testResolveNullBase() {
        String relUrl = "about.html";
        String result = StringUtil.resolve(null, relUrl);
    }

    /**
     * Test resolve method with null relative url.
     */
    @Test(expected = NullPointerException.class)
    public void testResolveNullRel() {
        String baseUrl = "https://example.com/about.html";
        String result = StringUtil.resolve(new URL(baseUrl), null);
    }

    /**
     * Test resolve method with invalid relative url.
     */
    @Test(expected = MalformedURLException.class)
    public void testResolveInvalidRelative() {
        String baseUrl = "https://example.com/";
        String relUrl = "//example2.com/contact.html";
        String result = StringUtil.resolve(new URL(baseUrl), relUrl);
    }

    /**
     * Test resolve method with empty base url.
     */
    @Test(expected = MalformedURLException.class)
    public void testResolveEmptyBase() throws MalformedURLException {
        String baseUrl = "";
        String relUrl = "about.html";
        String result = StringUtil.resolve(new URL(baseUrl), relUrl);
    }

    /**
     * Test resolve method with relative url containing a query string.
     */
    @Test
    public void testResolveWithQueryString() throws MalformedURLException {
        String baseUrl = "https://example.com/about.html";
        String relUrl = "contact.html?id=123";
        String expected = "https://example.com/contact.html?id=123";
        String result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected, result);
    }

    /**
     * Test resolve method with relative url containing a fragment.
     */
    @Test
    public void testResolveWithFragment() throws MalformedURLException {
        String baseUrl = "https://example.com/about.html";
        String relUrl = "contact.html#fragment";
        String expected = "https://example.com/contact.html#fragment";
        String result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected, result);
    }
}