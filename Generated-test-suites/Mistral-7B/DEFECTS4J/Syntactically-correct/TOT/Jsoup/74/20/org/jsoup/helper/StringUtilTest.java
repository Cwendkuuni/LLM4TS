package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("apple", "banana", "cherry");
        String expected = "applebanancherry";
        String result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinEmptyCollection() {
        List<String> strings = Collections.emptyList();
        String expected = "";
        String result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinIterator() {
        String[] strings = new String[]{"apple", "banana", "cherry"};
        Iterator<String> iterator = Arrays.asList(strings).iterator();
        String expected = "applebananacherry";
        String result = StringUtil.join(iterator, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinEmptyIterator() {
        String[] strings = new String[0];
        Iterator<String> iterator = Arrays.asList(strings).iterator();
        String expected = "";
        String result = StringUtil.join(iterator, "");
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJoinNegativeWidth() {
        String result = StringUtil.padding(-1);
    }

    @Test
    public void testIsBlank() {
        String empty = "";
        String nullString = null;
        String whitespace = " ";
        String whitespaceOnly = "   ";
        String blank = nullString + whitespace + empty;

        Assert.assertTrue(StringUtil.isBlank(empty));
        Assert.assertTrue(StringUtil.isBlank(nullString));
        Assert.assertTrue(StringUtil.isBlank(whitespace));
        Assert.assertTrue(StringUtil.isBlank(whitespaceOnly));
        Assert.assertTrue(StringUtil.isBlank(blank));
    }

    @Test
    public void testIsNumeric() {
        String number = "12345";
        String nonNumeric = "abcd";
        String empty = "";
        String nullString = null;

        Assert.assertTrue(StringUtil.isNumeric(number));
        Assert.assertFalse(StringUtil.isNumeric(nonNumeric));
        Assert.assertFalse(StringUtil.isNumeric(empty));
        Assert.assertFalse(StringUtil.isNumeric(nullString));
    }

    @Test
    public void testIsWhitespace() {
        char space = ' ';
        char tab = '\t';
        char lineFeed = '\n';
        char formFeed = '\f';
        char carriageReturn = '\r';

        Assert.assertTrue(StringUtil.isWhitespace(space));
        Assert.assertTrue(StringUtil.isWhitespace(tab));
        Assert.assertTrue(StringUtil.isWhitespace(lineFeed));
        Assert.assertTrue(StringUtil.isWhitespace(formFeed));
        Assert.assertTrue(StringUtil.isWhitespace(carriageReturn));
    }

    @Test
    public void testIsActuallyWhitespace() {
        String allWhitespace = "   \r\n\t\f";
        String nonWhitespace = "abcd";

        Assert.assertTrue(StringUtil.isActuallyWhitespace(allWhitespace.charAt(0)));
        Assert.assertFalse(StringUtil.isActuallyWhitespace(nonWhitespace.charAt(0)));
    }

    @Test
    public void testNormaliseWhitespace() {
        String multipleSpaces = "  foo  bar  ";
        String leadingSpaces = "   foo";
        String trailingSpaces = "foo   ";
        String spaceAndTab = " foo\tbar";
        String spaceAndNewline = "foo\nbar";

        String expected = "foo bar";
        String result = StringUtil.normaliseWhitespace(multipleSpaces);
        Assert.assertEquals(expected, result);

        expected = "foo";
        result = StringUtil.normaliseWhitespace(leadingSpaces);
        Assert.assertEquals(expected, result);

        expected = "foo";
        result = StringUtil.normaliseWhitespace(trailingSpaces);
        Assert.assertEquals(expected, result);

        expected = "foo\tabar";
        result = StringUtil.normaliseWhitespace(spaceAndTab);
        Assert.assertEquals(expected, result);

        expected = "foobar";
        result = StringUtil.normaliseWhitespace(spaceAndNewline);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIn() {
        String needle = "apple";
        String[] haystack = {"apple", "banana", "cherry", "apple"};
        boolean expected = true;
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testInNull() {
        String needle = null;
        String[] haystack = {"apple", "banana", "cherry"};
        boolean expected = false;
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testInEmpty() {
        String needle = "apple";
        String[] haystack = {};
        boolean expected = false;
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testInSorted() {
        String needle = "apple";
        String[] haystack = {"banana", "cherry", "apple"};
        boolean expected = true;
        boolean result = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"apple", "banana", "cherry"};
        String expected = "applebananacherry";
        String result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinArrayEmpty() {
        String[] strings = new String[0];
        String expected = "";
        String result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveBaseUrl() throws MalformedURLException {
        String baseUrl = "https://example.com";
        String relUrl = "/about.html";
        String expected = "https://example.com/about.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testResolveBaseUrlMalformedUrl() throws MalformedURLException {
        String baseUrl = "https://example.com";
        String relUrl = "//about.html";
        String expected = "https://example.com/about.html";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveBaseUrlMalformedRelativeUrl() throws MalformedURLException {
        String baseUrl = "https://example.com";
        String relUrl = "//about@example.com";
        String result = StringUtil.resolve(baseUrl, relUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveBaseUrlNullBaseUrl() throws MalformedURLException {
        String relUrl = "//about.html";
        String result = StringUtil.resolve(null, relUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveBaseUrlNullRelativeUrl() throws MalformedURLException {
        String baseUrl = "https://example.com";
        String result = StringUtil.resolve(baseUrl, null);
    }
}