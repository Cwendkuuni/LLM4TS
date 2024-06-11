package org.jsoup.helper.test;

import org.junit.Assert;
import org.jsoup.helper.StringUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringUtilTest {

    @org.junit.Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("apple", "banana", "cherry");
        String expected = "applebananacherry";
        String result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testJoinIteratorEmpty() {
        Iterator<String> strings = Collections.emptyIterator();
        String result = StringUtil.join(strings, "");
        String expected = "";
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testJoinArray() {
        String[] strings = new String[]{"apple", "banana", "cherry"};
        String expected = "applebananacherry";
        String result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testJoinArrayEmpty() {
        String[] strings = new String[0];
        String result = StringUtil.join(strings, "");
        String expected = "";
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testPaddingNegativeWidth() {
        String result = StringUtil.padding(-1);
    }

    @org.junit.Test
    public void testIsBlankNull() {
        String input = null;
        boolean result = StringUtil.isBlank(input);
        boolean expected = true;
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testIsBlankEmpty() {
        String input = "";
        boolean result = StringUtil.isBlank(input);
        boolean expected = true;
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testIsBlankWhitespace() {
        String input = "    ";
        boolean result = StringUtil.isBlank(input);
        boolean expected = true;
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testIsBlankNonBlank() {
        String input = "hello";
        boolean result = StringUtil.isBlank(input);
        boolean expected = false;
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testIsNumericEmpty() {
        String input = "";
        boolean result = StringUtil.isNumeric(input);
        boolean expected = false;
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testIsNumericDigits() {
        String input = "12345";
        boolean result = StringUtil.isNumeric(input);
        boolean expected = true;
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testIsNumericNonDigit() {
        String input = "123a5";
        boolean result = StringUtil.isNumeric(input);
        boolean expected = false;
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testIsWhitespaceSpace() {
        int codePoint = ' '.codePointAt(0);
        boolean result = StringUtil.isWhitespace(codePoint);
        boolean expected = true;
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testIsWhitespaceTab() {
        int codePoint = '\t'.codePointAt(0);
        boolean result = StringUtil.isWhitespace(codePoint);
        boolean expected = true;
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testIsWhitespaceLineFeed() {
        int codePoint = '\n'.codePointAt(0);
        boolean result = StringUtil.isWhitespace(codePoint);
        boolean expected = true;
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testIsActuallyWhitespaceSpace() {
        int codePoint = ' '.codePointAt(0);
        boolean result = StringUtil.isActuallyWhitespace(codePoint);
        boolean expected = true;
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testIsActuallyWhitespaceTab() {
        int codePoint = '\t'.codePointAt(0);
        boolean result = StringUtil.isActuallyWhitespace(codePoint);
        boolean expected = true;
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testIsActuallyWhitespaceLineFeed() {
        int codePoint = '\n'.codePointAt(0);
        boolean result = StringUtil.isActuallyWhitespace(codePoint);
        boolean expected = true;
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testIsActuallyWhitespaceNonBreakingSpace() {
        int codePoint = '\u00A0'.codePointAt(0); // Non-breaking space
        boolean result = StringUtil.isActuallyWhitespace(codePoint);
        boolean expected = true;
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testNormaliseWhitespaceSingleSpace() {
        String input = " ";
        String expected = " ";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testNormaliseWhitespaceMultipleSpaces() {
        String input = "   ";
        String expected = " ";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testNormaliseWhitespaceNewline() {
        String input = "\n";
        String expected = " ";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testNormaliseWhitespaceTab() {
        String input = "\t";
        String expected = " ";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testNormaliseWhitespaceMixed() {
        String input = "   \n\t ";
        String expected = " ";
        String result = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testJoinWithEmptySeparator() {
        List<String> strings = Arrays.asList("apple", "banana");
        String expected = "apple" + StringUtil.padding(1) + "banana";
        String result = StringUtil.join(strings, StringUtil.padding(1));
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testJoinWithNullSeparator() {
        List<String> strings = Arrays.asList("apple", "banana");
        String expected = "apple" + null + "banana";
        String result = StringUtil.join(strings, null);
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testInStringArray() {
        String[] haystack = {"apple", "banana", "cherry"};
        String needle = "banana";
        boolean expected = true;
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testInStringArrayCaseSensitive() {
        String[] haystack = {"apple", "Banana", "Cherry"};
        String needle = "banana";
        boolean expected = true;
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testInStringArrayEmpty() {
        String[] haystack = {};
        String needle = "apple";
        boolean expected = false;
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testInStringArrayNull() {
        String[] haystack = {"apple", "banana"};
        String needle = null;
        boolean expected = false;
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testInStringList() {
        List<String> haystack = Arrays.asList("apple", "banana", "cherry");
        String needle = "banana";
        boolean expected = true;
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testInStringListCaseSensitive() {
        List<String> haystack = Arrays.asList("apple", "Banana", "Cherry");
        String needle = "banana";
        boolean expected = true;
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testInStringListEmpty() {
        List<String> haystack = Collections.emptyList();
        String needle = "apple";
        boolean expected = false;
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testInStringListNull() {
        List<String> haystack = Arrays.asList("apple", "banana");
        String needle = null;
        boolean expected = false;
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testInStringListSorted() {
        String[] haystack = {"apple", "banana", "cherry"};
        String needle = "cherry";
        boolean expected = true;
        boolean result = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(expected, result);
    }

    @org.junit.Test
    public void testResolveAbsoluteUrl() throws MalformedURLException {
        String baseUrl = "http://example.com/index.html";
        String relUrl = "/about.html";
        URL expected = new URL("http://example.com/about.html");
        URL result = StringUtil.resolve(new URL(baseUrl), relUrl);
        Assert.assertEquals(expected.toString(), result.toString());
    }

    @org.junit.Test
    public void testResolveRelativeUrlStartingWithSlash() throws MalformedURLException {
        String baseUrl = "http://example.com/index.html";
        String relUrl = "/contact.html";
        URL expected = new URL("http://example.com/contact.html");
        URL result = StringUtil.resolve(new URL(baseUrl), relUrl);
        Assert.assertEquals(expected.toString(), result.toString());
    }

    @org.junit.Test
    public void testResolveRelativeUrlWithoutSlash() throws MalformedURLException {
        String baseUrl = "http://example.com/index.html";
        String relUrl = "contact.html";
        String expectedBase = baseUrl.substring(0, baseUrl.lastIndexOf("/") + 1);
        URL expected = new URL(expectedBase + relUrl);
        URL result = StringUtil.resolve(new URL(baseUrl), relUrl);
        Assert.assertEquals(expected.toString(), result.toString());
    }

    @org.junit.Test
    public void testResolveAbsoluteUrlWithBaseAbsentProtocol() throws MalformedURLException {
        String baseUrl = "/index.html";
        String relUrl = "//example.com/contact.html";
        URL expected = new URL("http://example.com/contact.html");
        URL result = StringUtil.resolve(new URL(baseUrl), relUrl);
        Assert.assertEquals(expected.toString(), result.toString());
    }

    @org.junit.Test
    public void testResolveRelativeUrlWithBaseAbsentProtocol() throws MalformedURLException {
        String baseUrl = "/index.html";
        String relUrl = "/contact.html";
        String expectedBase = baseUrl.substring(0, baseUrl.lastIndexOf("/") + 1);
        URL expected = new URL(expectedBase + relUrl);
        URL result = StringUtil.resolve(new URL(baseUrl), relUrl);
        Assert.assertEquals(expected.toString(), result.toString());
    }

    @org.junit.Test(expected = MalformedURLException.class)
    public void testResolveMalformedUrl() throws MalformedURLException {
        String baseUrl = "http://example.com/index.html";
        String relUrl = "://example.com/contact.html";
        StringUtil.resolve(new URL(baseUrl), relUrl);
    }

    @org.junit.Test(expected = MalformedURLException.class)
    public void testResolveMalformedUrlWithBaseAbsentProtocol() throws MalformedURLException {
        String baseUrl = "/index.html";
        String relUrl = "://example.com/contact.html";
        StringUtil.resolve(new URL(baseUrl), relUrl);
    }
}