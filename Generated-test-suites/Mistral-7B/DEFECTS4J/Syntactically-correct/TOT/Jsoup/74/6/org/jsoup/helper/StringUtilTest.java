package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("Hello", "World", "!", "Java");
        String expected = "HelloWorld!Java";
        String actual = StringUtil.join(strings, "");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinIteratorEmpty() {
        Iterator<String> strings = Collections.emptyIterator();
        String actual = StringUtil.join(strings, "");
        Assert.assertEquals("", actual);
    }

    @Test
    public void testJoinIteratorOneElement() {
        Iterator<String> strings = Arrays.asList("Hello").iterator();
        String actual = StringUtil.join(strings, " ");
        String expected = "Hello";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinIteratorMultipleElements() {
        Iterator<String> strings = Arrays.asList("Hello", "World", "!", "Java").iterator();
        String actual = StringUtil.join(strings, " ");
        String expected = "Hello World ! Java";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinArrayEmpty() {
        String[] strings = {};
        String actual = StringUtil.join(strings, "");
        Assert.assertEquals("", actual);
    }

    @Test
    public void testJoinArrayOneElement() {
        String[] strings = {"Hello"};
        String actual = StringUtil.join(strings, " ");
        String expected = "Hello";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinArrayMultipleElements() {
        String[] strings = {"Hello", "World", "!", "Java"};
        String actual = StringUtil.join(strings, " ");
        String expected = "Hello World ! Java";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPadding() {
        String actual = StringUtil.padding(5);
        String expected = "     ";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPaddingNegativeSize() {
        Assert.assertThrows(IllegalArgumentException.class, () -> StringUtil.padding(-5));
    }

    @Test
    public void testIsBlankNull() {
        Assert.assertTrue(StringUtil.isBlank(null));
    }

    @Test
    public void testIsBlankEmpty() {
        String str = "";
        Assert.assertTrue(StringUtil.isBlank(str));
    }

    @Test
    public void testIsBlankWhitespace() {
        String str = "   ";
        Assert.assertTrue(StringUtil.isBlank(str));
    }

    @Test
    public void testIsBlankNonBlank() {
        String str = "Hello World";
        Assert.assertFalse(StringUtil.isBlank(str));
    }

    @Test
    public void testIsNumericNull() {
        Assert.assertFalse(StringUtil.isNumeric(null));
    }

    @Test
    public void testIsNumericEmpty() {
        Assert.assertFalse(StringUtil.isNumeric(""));
    }

    @Test
    public void testIsNumericDigits() {
        String str = "12345";
        Assert.assertTrue(StringUtil.isNumeric(str));
    }

    @Test
    public void testIsNumericDigitsAndOther() {
        String str = "12345abc";
        Assert.assertFalse(StringUtil.isNumeric(str));
    }

    @Test
    public void testIsWhitespace() {
        Assert.assertTrue(StringUtil.isWhitespace(' '));
        Assert.assertTrue(StringUtil.isWhitespace('\t'));
        Assert.assertTrue(StringUtil.isWhitespace('\n'));
        Assert.assertTrue(StringUtil.isWhitespace('\f'));
        Assert.assertTrue(StringUtil.isWhitespace('\r'));
    }

    @Test
    public void testIsWhitespaceNonWhitespace() {
        Assert.assertFalse(StringUtil.isWhitespace('a'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        Assert.assertTrue(StringUtil.isActuallyWhitespace(' '));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\t'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\n'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\f'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\r'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(Character.toChars(0x00A0)[0]));
    }

    @Test
    public void testIsActuallyWhitespaceNonWhitespace() {
        Assert.assertFalse(StringUtil.isActuallyWhitespace('a'));
    }

    @Test
    public void testNormaliseWhitespaceEmpty() {
        String actual = StringUtil.normaliseWhitespace("");
        Assert.assertEquals("", actual);
    }

    @Test
    public void testNormaliseWhitespaceSingleSpace() {
        String actual = StringUtil.normaliseWhitespace(" ");
        Assert.assertEquals("", actual);
    }

    @Test
    public void testNormaliseWhitespaceMultipleSpaces() {
        String actual = StringUtil.normaliseWhitespace("   ");
        Assert.assertEquals("", actual);
    }

    @Test
    public void testNormaliseWhitespaceTab() {
        String actual = StringUtil.normaliseWhitespace("\t");
        Assert.assertEquals("", actual);
    }

    @Test
    public void testNormaliseWhitespaceNewline() {
        String actual = StringUtil.normaliseWhitespace("\n");
        Assert.assertEquals("", actual);
    }

    @Test
    public void testNormaliseWhitespaceMixed() {
        String actual = StringUtil.normaliseWhitespace("  \t\n  ");
        String expected = " ";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIn() {
        String haystack = "apple,banana,cherry,date";
        String needle = "apple";
        Assert.assertTrue(StringUtil.in(needle, haystack.split(",")));
    }

    @Test
    public void testInEmptyHaystack() {
        String haystack = "";
        String needle = "apple";
        Assert.assertFalse(StringUtil.in(needle, haystack.split(",")));
    }

    @Test
    public void testInEmptyNeedle() {
        String haystack = "apple,banana,cherry,date";
        String needle = "";
        Assert.assertFalse(StringUtil.in(needle, haystack.split(",")));
    }

    @Test
    public void testInNeedleNotInHaystack() {
        String haystack = "apple,banana,cherry,date";
        String needle = "orange";
        Assert.assertFalse(StringUtil.in(needle, haystack.split(",")));
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"apple", "banana", "cherry", "date"};
        String needle = "apple";
        Assert.assertTrue(StringUtil.inSorted(needle, haystack));
    }

    @Test
    public void testJoinURL() throws IOException {
        String baseUrl = "http://example.com/index.html";
        String relUrl = "/about.html";
        String expected = "http://example.com/about.html";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinURLInvalidBaseUrl() throws IOException {
        String baseUrl = "/index.html";
        String relUrl = "/about.html";
        String expected = "";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinURLInvalidRelUrl() throws IOException {
        String baseUrl = "http://example.com/index.html";
        String relUrl = "//about.html";
        String expected = "";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinURLValidBaseAndRelUrls() throws IOException {
        String baseUrl = "http://example.com/index.html";
        String relUrl = "contact.html";
        String expected = "http://example.com/contact.html";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, actual);
    }
}