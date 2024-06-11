package org.jsoup.helper;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * Test class for the StringUtil class
 */
public class StringUtilTest {

    /**
     * Test join method with Collection
     */
    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("hello", "world", "!");
        String expected = "helloworld!";
        String result = StringUtil.join(strings, "");
        assertEquals(expected, result);
    }

    /**
     * Test join method with Iterator
     */
    @Test
    public void testJoinIterator() {
        String[] strings = new String[]{"hello", "world", "!"};
        Iterator<String> iterator = Arrays.asList(strings).iterator();
        String expected = "helloworld!";
        String result = StringUtil.join(iterator, "");
        assertEquals(expected, result);
    }

    /**
     * Test join method with Array
     */
    @Test
    public void testJoinArray() {
        String[] strings = new String[]{"hello", "world", "!"};
        String expected = "helloworld!";
        String result = StringUtil.join(strings, "");
        assertEquals(expected, result);
    }

    /**
     * Test padding method
     */
    @Test
    public void testPadding() {
        String expected = "         hello";
        String result = StringUtil.padding(8) + "hello";
        assertEquals(expected, result);
    }

    /**
     * Test isBlank method
     */
    @Test
    public void testIsBlank() {
        String empty = "";
        String whitespace = "   ";
        String notBlank = "hello";
        assertTrue(StringUtil.isBlank(empty));
        assertTrue(StringUtil.isBlank(whitespace));
        assertFalse(StringUtil.isBlank(notBlank));
    }

    /**
     * Test isNumeric method
     */
    @Test
    public void testIsNumeric() {
        String number = "123";
        String notNumber = "hello";
        String empty = "";
        assertTrue(StringUtil.isNumeric(number));
        assertFalse(StringUtil.isNumeric(notNumber));
        assertFalse(StringUtil.isNumeric(empty));
    }

    /**
     * Test isWhitespace method
     */
    @Test
    public void testIsWhitespace() {
        int space = ' ';
        int tab = '\t';
        int newline = '\n';
        int carriageReturn = '\r';
        int formFeed = '\f';
        int nonBreakingSpace = 160;
        assertTrue(StringUtil.isWhitespace(space));
        assertTrue(StringUtil.isWhitespace(tab));
        assertTrue(StringUtil.isWhitespace(newline));
        assertTrue(StringUtil.isWhitespace(carriageReturn));
        assertTrue(StringUtil.isWhitespace(formFeed));
        assertTrue(StringUtil.isWhitespace(nonBreakingSpace));
    }

    /**
     * Test isActuallyWhitespace method
     */
    @Test
    public void testIsActuallyWhitespace() {
        int space = ' ';
        int tab = '\t';
        int newline = '\n';
        int carriageReturn = '\r';
        int formFeed = '\f';
        int nonBreakingSpace = 160;
        int notWhitespace = 'a';
        assertTrue(StringUtil.isActuallyWhitespace(space));
        assertTrue(StringUtil.isActuallyWhitespace(tab));
        assertTrue(StringUtil.isActuallyWhitespace(newline));
        assertTrue(StringUtil.isActuallyWhitespace(carriageReturn));
        assertTrue(StringUtil.isActuallyWhitespace(formFeed));
        assertTrue(StringUtil.isActuallyWhitespace(nonBreakingSpace));
        assertFalse(StringUtil.isActuallyWhitespace(notWhitespace));
    }

    /**
     * Test normaliseWhitespace method
     */
    @Test
    public void testNormaliseWhitespace() {
        String multipleSpaces = "   hello   ";
        String newlineTab = "hello\n\tworld";
        String expected = "hello world";
        String result = StringUtil.normaliseWhitespace(multipleSpaces);
        assertEquals(expected, result);
        expected = "hello world";
        result = StringUtil.normaliseWhitespace(newlineTab);
        assertEquals(expected, result);
    }

    /**
     * Test in method
     */
    @Test
    public void testIn() {
        String needle = "hello";
        String[] haystack = {"hello", "world", "hello again"};
        assertTrue(StringUtil.in(needle, haystack));
        assertFalse(StringUtil.in(needle, new String[]{"world"}));
    }

    /**
     * Test inSorted method
     */
    @Test
    public void testInSorted() {
        String needle = "hello";
        String[] haystack = {"world", "hello", "hello again"};
        assertFalse(StringUtil.inSorted(needle, haystack));
        String[] sortedHaystack = {"hello", "hello again", "world"};
        assertTrue(StringUtil.inSorted(needle, sortedHaystack));
    }

    /**
     * Test resolve method with absolute base URL and relative URL
     */
    @Test
    public void testResolveAbsoluteBaseAndRelative() throws MalformedURLException {
        String baseUrl = "http://example.com/";
        String relUrl = "/path/file.html";
        URL expected = new URL("http://example.com/path/file.html");
        URL result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected.toExternalForm(), result.toExternalForm());
    }

    /**
     * Test resolve method with absolute base URL and absolute relative URL
     */
    @Test
    public void testResolveAbsoluteBaseAndAbsoluteRelative() throws MalformedURLException {
        String baseUrl = "http://example.com/";
        String relUrl = "http://example.com/path/file.html";
        URL expected = new URL("http://example.com/path/file.html");
        URL result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected.toExternalForm(), result.toExternalForm());
    }

    /**
     * Test resolve method with absolute base URL and empty relative URL
     */
    @Test
    public void testResolveAbsoluteBaseAndEmptyRelative() throws MalformedURLException {
        String baseUrl = "http://example.com/";
        String relUrl = "";
        URL expected = new URL(baseUrl);
        URL result = StringUtil.resolve(new URL(baseUrl), relUrl);
        assertEquals(expected.toExternalForm(), result.toExternalForm());
    }

    /**
     * Test resolve method with absolute base URL and null relative URL
     */
    @Test(expected = NullPointerException.class)
    public void testResolveAbsoluteBaseAndNullRelative() throws MalformedURLException {
        String baseUrl = "http://example.com/";
        String relUrl = null;
        StringUtil.resolve(new URL(baseUrl), relUrl);
    }

    /**
     * Test resolve method with null base URL and relative URL
     */
    @Test(expected = NullPointerException.class)
    public void testResolveNullBaseAndRelative() throws MalformedURLException {
        String relUrl = "/path/file.html";
        URL base = null;
        StringUtil.resolve(base, relUrl);
    }

    /**
     * Test resolve method with null base URL and null relative URL
     */
    @Test(expected = NullPointerException.class)
    public void testResolveNullBaseAndNullRelative() {
        StringUtil.resolve(null, null);
    }

    /**
     * Test stringBuilder method
     */
    @Test
    public void testStringBuilder() {
        StringBuilder expected = new StringBuilder("expected");
        StringBuilder result = StringUtil.stringBuilder();
        assertEquals(expected, result);
        result.append("append");
        assertEquals("expectedappend", expected.toString());
        assertEquals("append", result.toString());
    }

}