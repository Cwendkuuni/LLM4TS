package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("hello", "world");
        String expected = "helloworld";
        String actual = StringUtil.join(strings, "");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinCollectionWithSeparator() {
        List<String> strings = Arrays.asList("hello", "world");
        String separator = ",";
        String expected = "hello," + separator + "world";
        String actual = StringUtil.join(strings, separator);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinIterator() {
        String[] strings = new String[]{"hello", "world"};
        Iterator<String> iterator = Arrays.asList(strings).iterator();
        String expected = "helloworld";
        String actual = StringUtil.join(iterator, "");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinArray() {
        String[] strings = new String[]{"hello", "world"};
        String separator = ";";
        String expected = "hello" + separator + "world";
        String actual = StringUtil.join(strings, separator);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinNullCollection() {
        Collection<String> strings = null;
        String separator = ";";
        String expected = "";
        String actual = StringUtil.join(strings, separator);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinEmptyCollection() {
        Collection<String> strings = Collections.emptyList();
        String separator = ";";
        String expected = "";
        String actual = StringUtil.join(strings, separator);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinIteratorEmpty() {
        Iterator<String> strings = Collections.emptyIterator();
        String separator = ";";
        String expected = "";
        String actual = StringUtil.join(strings, separator);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJoinNegativeWidth() {
        int width = -5;
        String actual = StringUtil.padding(width);
        Assert.fail("Expected IllegalArgumentException, but got: " + actual);
    }

    @Test
    public void testIsBlankNull() {
        String string = null;
        boolean expected = true;
        boolean actual = StringUtil.isBlank(string);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsBlankEmpty() {
        String string = "";
        boolean expected = true;
        boolean actual = StringUtil.isBlank(string);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsBlankWhitespace() {
        String string = "   ";
        boolean expected = true;
        boolean actual = StringUtil.isBlank(string);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsBlankNonWhitespace() {
        String string = "hello";
        boolean expected = false;
        boolean actual = StringUtil.isBlank(string);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsNumericEmpty() {
        String string = "";
        boolean expected = false;
        boolean actual = StringUtil.isNumeric(string);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsNumericDigits() {
        String string = "12345";
        boolean expected = true;
        boolean actual = StringUtil.isNumeric(string);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsNumericLetters() {
        String string = "abc";
        boolean expected = false;
        boolean actual = StringUtil.isNumeric(string);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsNumericDigitsAndLetters() {
        String string = "1a2b3";
        boolean expected = false;
        boolean actual = StringUtil.isNumeric(string);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWhitespaceSpace() {
        int c = ' ';
        boolean expected = true;
        boolean actual = StringUtil.isWhitespace(c);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWhitespaceTab() {
        int c = '\t';
        boolean expected = true;
        boolean actual = StringUtil.isWhitespace(c);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWhitespaceNewline() {
        int c = '\n';
        boolean expected = true;
        boolean actual = StringUtil.isWhitespace(c);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWhitespaceNullCharacter() {
        int c = '\u0000';
        boolean expected = true;
        boolean actual = StringUtil.isWhitespace(c);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWhitespaceNonWhitespace() {
        int c = 'A';
        boolean expected = false;
        boolean actual = StringUtil.isWhitespace(c);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsActuallyWhitespaceSpace() {
        int c = ' ';
        boolean expected = true;
        boolean actual = StringUtil.isActuallyWhitespace(c);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsActuallyWhitespaceTab() {
        int c = '\t';
        boolean expected = true;
        boolean actual = StringUtil.isActuallyWhitespace(c);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsActuallyWhitespaceNewline() {
        int c = '\n';
        boolean expected = true;
        boolean actual = StringUtil.isActuallyWhitespace(c);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsActuallyWhitespaceCarriageReturn() {
        int c = '\r';
        boolean expected = true;
        boolean actual = StringUtil.isActuallyWhitespace(c);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsActuallyWhitespaceNonWhitespace() {
        int c = 'A';
        boolean expected = false;
        boolean actual = StringUtil.isActuallyWhitespace(c);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNormaliseWhitespaceSingleSpace() {
        String input = " hello ";
        String expected = "hello";
        String actual = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNormaliseWhitespaceMultipleSpaces() {
        String input = "   hello    ";
        String expected = "hello";
        String actual = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNormaliseWhitespaceTab() {
        String input = " hello\t";
        String expected = "hello";
        String actual = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNormaliseWhitespaceNewline() {
        String input = "hello\n";
        String expected = "hello";
        String actual = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNormaliseWhitespaceMixed() {
        String input = "  hello\tworld\n";
        String expected = "helloworld";
        String actual = StringUtil.normaliseWhitespace(input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInNullNeedle() {
        String[] haystack = new String[]{"apple", "banana", "orange"};
        boolean expected = false;
        boolean actual = StringUtil.in(null, haystack);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInEmptyNeedle() {
        String[] haystack = new String[]{"apple", "banana", "orange"};
        boolean expected = false;
        boolean actual = StringUtil.in("", haystack);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInEmptyHaystack() {
        String needle = "apple";
        boolean expected = false;
        boolean actual = StringUtil.in(needle, new String[0]);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInMatch() {
        String needle = "apple";
        String[] haystack = new String[]{"apple", "banana", "orange"};
        boolean expected = true;
        boolean actual = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInNoMatch() {
        String needle = "cherry";
        String[] haystack = new String[]{"apple", "banana", "orange"};
        boolean expected = false;
        boolean actual = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInMatchCaseSensitive() {
        String needle = "Apple";
        String[] haystack = new String[]{"apple", "banana", "orange"};
        boolean expected = false;
        boolean actual = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInMatchCaseInsensitive() {
        String needle = "Apple";
        String[] haystack = new String[]{"apple", "Banana", "Orange"};
        boolean expected = true;
        boolean actual = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveNullBaseUrl() {
        String relUrl = "/path/file";
        String actual = StringUtil.resolve(null, relUrl);
        Assert.assertEquals("", actual);
    }

    @Test
    public void testResolveEmptyBaseUrl() {
        String relUrl = "/path/file";
        String actual = StringUtil.resolve("", relUrl);
        Assert.assertEquals("/path/file", actual);
    }

    @Test
    public void testResolveNullRelUrl() {
        String baseUrl = "http://example.com/path/file";
        String actual = StringUtil.resolve(baseUrl, null);
        Assert.assertEquals(baseUrl, actual);
    }

    @Test
    public void testResolveEmptyRelUrl() {
        String baseUrl = "http://example.com/path/file";
        String actual = StringUtil.resolve(baseUrl, "");
        Assert.assertEquals(baseUrl, actual);
    }

    @Test
    public void testResolveAbsoluteRelUrl() {
        String baseUrl = "http://example.com/path/file";
        String relUrl = "/path/otherfile";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        String expected = "http://example.com/path/otherfile";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveRelativeRelUrl() {
        String baseUrl = "http://example.com/path/file";
        String relUrl = "otherfile.html";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        String expected = "http://example.com/path/otherfile.html";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveRelativeRelUrlWithBasePath() {
        String baseUrl = "/path/file";
        String relUrl = "otherfile.html";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        String expected = "/path/otherfile.html";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveRelativeRelUrlWithBaseBaseUrl() {
        String baseUrl = "http://example.com/path/file";
        String relUrl = "otherfile.html";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        String expected = "http://example.com/path/otherfile.html";
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveInvalidRelUrl() {
        String baseUrl = "http://example.com/path/file";
        String relUrl = "//otherfile.html";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        Assert.fail("Expected MalformedURLException, but got: " + actual);
    }

    @Test
    public void testStringBuilder() {
        StringBuilder sb = StringUtil.stringBuilder();
        String expected = "";
        String actual = sb.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testStringBuilderAppend() {
        StringBuilder sb = StringUtil.stringBuilder();
        sb.append("hello");
        String expected = "hello";
        String actual = sb.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testStringBuilderAppendCollection() {
        StringBuilder sb = StringUtil.stringBuilder();
        List<String> strings = Arrays.asList("hello", "world");
        sb.append(strings);
        String expected = "helloworld";
        String actual = sb.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testStringBuilderAppendMultiple() {
        StringBuilder sb = StringUtil.stringBuilder();
        sb.append("hello");
        sb.append(" ");
        sb.append("world");
        String expected = "hello world";
        String actual = sb.toString();
        Assert.assertEquals(expected, actual);
    }

}