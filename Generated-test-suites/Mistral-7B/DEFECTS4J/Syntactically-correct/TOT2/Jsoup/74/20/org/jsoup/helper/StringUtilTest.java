package org.jsoup.helper.test;

import org.junit.Test;
import org.jsoup.helper.StringUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class StringUtilTest {

    @Test
    public void testJoinCollectionWithSeparator() {
        List<String> strings = new ArrayList<>();
        strings.add("apple");
        strings.add("banana");
        strings.add("cherry");

        String expected = "applebananacherry";
        String result = StringUtil.join(strings, "");

        assertEquals(expected, result);
    }

    @Test
    public void testJoinIteratorWithSeparator() {
        String[] strings = {"apple", "banana", "cherry"};
        Iterator<String> it = Arrays.asList(strings).iterator();

        String expected = "applebananacherry";
        String result = StringUtil.join(it, "");

        assertEquals(expected, result);
    }

    @Test
    public void testJoinArrayWithSeparator() {
        String[] strings = {"apple", "banana", "cherry"};

        String expected = "applebananacherry";
        String result = StringUtil.join(strings, "");

        assertEquals(expected, result);
    }

    @Test
    public void testPaddingWithValidSize() {
        String expected = "        ";
        String result = StringUtil.padding(5);

        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingWithInvalidNegativeSize() {
        StringUtil.padding(-5);
    }

    @Test
    public void testIsBlankWithNullString() {
        boolean expected = true;
        String string = null;

        boolean result = StringUtil.isBlank(string);

        assertEquals(expected, result);
    }

    @Test
    public void testIsBlankWithEmptyString() {
        boolean expected = true;
        String string = "";

        boolean result = StringUtil.isBlank(string);

        assertEquals(expected, result);
    }

    @Test
    public void testIsBlankWithWhitespaceString() {
        boolean expected = true;
        String string = "   ";

        boolean result = StringUtil.isBlank(string);

        assertEquals(expected, result);
    }

    @Test
    public void testIsBlankWithNonBlankString() {
        boolean expected = false;
        String string = "abc";

        boolean result = StringUtil.isBlank(string);

        assertEquals(expected, result);
    }

    @Test
    public void testIsNumericWithDigitString() {
        boolean expected = true;
        String string = "12345";

        boolean result = StringUtil.isNumeric(string);

        assertEquals(expected, result);
    }

    @Test
    public void testIsNumericWithNonNumericString() {
        boolean expected = false;
        String string = "abc";

        boolean result = StringUtil.isNumeric(string);

        assertEquals(expected, result);
    }

    @Test
    public void testIsNumericWithEmptyString() {
        boolean expected = false;
        String string = "";

        boolean result = StringUtil.isNumeric(string);

        assertEquals(expected, result);
    }

    @Test
    public void testIsWhitespaceWithSpaceCharacter() {
        boolean expected = true;
        int codePoint = ' ';

        boolean result = StringUtil.isWhitespace(codePoint);

        assertEquals(expected, result);
    }

    @Test
    public void testIsWhitespaceWithTabCharacter() {
        boolean expected = true;
        int codePoint = '\t';

        boolean result = StringUtil.isWhitespace(codePoint);

        assertEquals(expected, result);
    }

    @Test
    public void testIsWhitespaceWithLineFeedCharacter() {
        boolean expected = true;
        int codePoint = '\n';

        boolean result = StringUtil.isWhitespace(codePoint);

        assertEquals(expected, result);
    }

    @Test
    public void testIsWhitespaceWithFormFeedCharacter() {
        boolean expected = true;
        int codePoint = '\f';

        boolean result = StringUtil.isWhitespace(codePoint);

        assertEquals(expected, result);
    }

    @Test
    public void testIsWhitespaceWithCarriageReturnCharacter() {
        boolean expected = true;
        int codePoint = '\r';

        boolean result = StringUtil.isWhitespace(codePoint);

        assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceWithSingleSpace() {
        String input = " foo";
        String expected = " foo";
        String result = StringUtil.normaliseWhitespace(input);

        assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceWithMultipleSpaces() {
        String input = " foo   bar";
        String expected = " foo bar";
        String result = StringUtil.normaliseWhitespace(input);

        assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceWithLeadingSpaces() {
        String input = "   foo   bar";
        String expected = " foo bar";
        String result = StringUtil.normaliseWhitespace(input);

        assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceWithTrailingSpaces() {
        String input = " foo   bar   ";
        String expected = " foo bar";
        String result = StringUtil.normaliseWhitespace(input);

        assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceWithTabAndLineFeed() {
        String input = " foo\t\n bar";
        String expected = " foo bar";
        String result = StringUtil.normaliseWhitespace(input);

        assertEquals(expected, result);
    }

    @Test
    public void testNormaliseWhitespaceWithMixedWhitespace() {
        String input = " foo\t bar\n baz  ";
        String expected = " foo bar baz";
        String result = StringUtil.normaliseWhitespace(input);

        assertEquals(expected, result);
    }

    @Test
    public void testInWithNeedleAndHaystack() {
        String[] haystack = {"apple", "banana", "cherry"};
        String needle = "banana";

        boolean expected = true;
        boolean result = StringUtil.in(needle, haystack);

        assertEquals(expected, result);
    }

    @Test
    public void testInWithNeedleButNotInHaystack() {
        String[] haystack = {"apple", "banana", "cherry"};
        String needle = "orange";

        boolean expected = false;
        boolean result = StringUtil.in(needle, haystack);

        assertEquals(expected, result);
    }

    @Test
    public void testInSortedWithNeedleAndHaystack() {
        String[] haystack = {"apple", "banana", "cherry"};
        String needle = "banana";

        boolean expected = true;
        boolean result = StringUtil.inSorted(needle, haystack);

        assertEquals(expected, result);
    }

    @Test
    public void testInSortedWithNeedleButNotInSortedHaystack() {
        String[] haystack = {"apple", "banana", "cherry"};
        String needle = "orange";

        boolean expected = false;
        boolean result = StringUtil.inSorted(needle, haystack);

        assertEquals(expected, result);
    }

    @Test
    public void testJoinCollectionWithNullCollection() {
        List<String> strings = Collections.emptyList();
        String sep = "";

        String expected = "";
        String result = StringUtil.join(strings, sep);

        assertEquals(expected, result);
    }

    @Test
    public void testJoinIteratorWithEmptyIterator() {
        Iterator<String> it = Collections.emptyIterator();
        String sep = "";

        String expected = "";
        String result = StringUtil.join(it, sep);

        assertEquals(expected, result);
    }

    @Test
    public void testJoinArrayWithEmptyArray() {
        String[] strings = {};
        String sep = "";

        String expected = "";
        String result = StringUtil.join(strings, sep);

        assertEquals(expected, result);
    }

    @Test
    public void testResolveWithBaseUrlAndRelativeUrl() throws MalformedURLException {
        String baseUrl = "http://www.example.com/";
        String relUrl = "/test.html";

        String expected = "http://www.example.com/test.html";
        URL result = StringUtil.resolve(new URL(baseUrl), relUrl);

        assertEquals(expected, result.toExternalForm());
    }

    @Test
    public void testResolveWithBaseUrlAndRelativeUrlWithTrailingSlash() throws MalformedURLException {
        String baseUrl = "http://www.example.com/";
        String relUrl = "/";

        String expected = "http://www.example.com/";
        URL result = StringUtil.resolve(new URL(baseUrl), relUrl);

        assertEquals(expected, result.toExternalForm());
    }

    @Test
    public void testResolveWithBaseUrlAndAbsoluteUrl() throws MalformedURLException {
        String baseUrl = "http://www.example.com/";
        String relUrl = "http://another.example.com/";

        String expected = "http://another.example.com/";
        URL result = StringUtil.resolve(new URL(baseUrl), relUrl);

        assertEquals(expected, result.toExternalForm());
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveWithInvalidBaseUrl() throws MalformedURLException {
        String baseUrl = "http://www.example.com";
        String relUrl = "/test.html";

        URL result = StringUtil.resolve(new URL(baseUrl), relUrl);

        assertNull(result);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveWithInvalidRelativeUrl() throws MalformedURLException {
        String baseUrl = "http://www.example.com/";
        String relUrl = "//test.html";

        URL result = StringUtil.resolve(new URL(baseUrl), relUrl);

        assertNull(result);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveWithEmptyBaseUrl() throws MalformedURLException {
        String baseUrl = "";
        String relUrl = "/test.html";

        URL result = StringUtil.resolve(new URL(baseUrl), relUrl);

        assertNull(result);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveWithNullBaseUrl() throws MalformedURLException {
        String baseUrl = null;
        String relUrl = "/test.html";

        URL result = StringUtil.resolve(new URL(baseUrl), relUrl);

        assertNull(result);
    }

    @Test
    public void testStringBuilderCachedInstance() {
        StringBuilder sb1 = StringUtil.stringBuilder();
        StringBuilder sb2 = StringUtil.stringBuilder();

        assertSame(sb1, sb2);

        sb1.append("foo");
        sb2.append("bar");

        assertNotSame(sb1, sb2);
    }

}