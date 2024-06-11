package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        String expected = "HelloWorld";
        String actual = StringUtil.join(strings, "");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinIteratorEmpty() {
        Iterator<String> strings = Collections.emptyIterator();
        String actual = StringUtil.join(strings, "");
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinIteratorSingle() {
        Iterator<String> strings = Collections.singletonList("Test").iterator();
        String actual = StringUtil.join(strings, "");
        String expected = "Test";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinIteratorMultiple() {
        Iterator<String> strings = Arrays.asList("One", "Two", "Three").iterator();
        String expected = "OneTwoThree";
        String actual = StringUtil.join(strings, "");
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJoinIteratorNegativeWidth() {
        Iterator<String> strings = Arrays.asList("One", "Two", "Three").iterator();
        String actual = StringUtil.padding(-1, strings.next().length());
        Assert.fail("Expected IllegalArgumentException, but got: " + actual);
    }

    @Test
    public void testIsBlankNull() {
        String actual = StringUtil.isBlank(null);
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsBlankEmpty() {
        String actual = StringUtil.isBlank("");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsBlankOnlyWhitespace() {
        String actual = StringUtil.isBlank("   ");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsBlankNonBlank() {
        String actual = StringUtil.isBlank("HelloWorld");
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsNumericTrue() {
        String actual = StringUtil.isNumeric("12345");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsNumericFalseEmpty() {
        String actual = StringUtil.isNumeric("");
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsNumericFalseNonNumeric() {
        String actual = StringUtil.isNumeric("abc");
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWhitespaceTrue() {
        int actual = StringUtil.isWhitespace(' ');
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWhitespaceFalseNonWhitespace() {
        int actual = StringUtil.isWhitespace('A');
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinArrayEmpty() {
        String[] strings = new String[0];
        String actual = StringUtil.join(strings, "");
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinArraySingle() {
        String[] strings = new String[]{"Test"};
        String expected = "Test";
        String actual = StringUtil.join(strings, "");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinArrayMultiple() {
        String[] strings = new String[]{"One", "Two", "Three"};
        String expected = "OneTwoThree";
        String actual = StringUtil.join(strings, "");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPaddingValid() {
        String actual = StringUtil.padding(5);
        String expected = "     ";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPaddingInvalidNegative() {
        try {
            String actual = StringUtil.padding(-5);
        } catch (IllegalArgumentException e) {
            String expected = "width must be > 0";
            Assert.assertEquals(expected, e.getMessage());
        }
    }

    @Test   
    public void testResolveEmptyBase() {
        String base = "";
        String relUrl = "/test.html";
        String expected = "";
        String actual = StringUtil.resolve(base, relUrl);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveEmptyRelUrl() {
        String base = "http://example.com/";
        String relUrl = "";
        String expected = base;
        String actual = StringUtil.resolve(base, relUrl);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveAbsoluteRelUrl() {
        String base = "http://example.com/";
        String relUrl = "/test.html";
        String expected = base + relUrl;
        String actual = StringUtil.resolve(base, relUrl);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveBaseWithTrailingSlash() {
        String base = "http://example.com/";
        String relUrl = "test.html";
        String expected = base + relUrl;
        String actual = StringUtil.resolve(base, relUrl);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolveRelUrlWithTrailingSlash() {
        String base = "http://example.com/";
        String relUrl = "/test.html/";
        String expected = base + relUrl.substring(0, relUrl.length() - 1);
        String actual = StringUtil.resolve(base, relUrl);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveInvalidRelUrl() {
        String base = "http://example.com/";
        String relUrl = "/test.html%";
        String actual = StringUtil.resolve(base, relUrl);
        Assert.fail("Expected MalformedURLException, but got: " + actual);
    }

    @Test
    public void testNormaliseWhitespaceEmpty() {
        String actual = StringUtil.normaliseWhitespace("");
        String expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNormaliseWhitespaceLeading() {
        String actual = StringUtil.normaliseWhitespace("   HelloWorld   ");
        String expected = "HelloWorld";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNormaliseWhitespaceTrailing() {
        String actual = StringUtil.normaliseWhitespace("HelloWorld   ");
        String expected = "HelloWorld";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNormaliseWhitespaceMiddle() {
        String actual = StringUtil.normaliseWhitespace("Hello   World");
        String expected = "Hello World";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNormaliseWhitespaceMultipleSpaces() {
        String actual = StringUtil.normaliseWhitespace("Hello\tWorld");
        String expected = "Hello World";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNormaliseWhitespaceNewline() {
        String actual = StringUtil.normaliseWhitespace("\nHelloWorld");
        String expected = "HelloWorld";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNormaliseWhitespaceTab() {
        String actual = StringUtil.normaliseWhitespace("\tHelloWorld");
        String expected = "HelloWorld";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNormaliseWhitespaceMixed() {
        String actual = StringUtil.normaliseWhitespace("\n   Hello\tWorld   ");
        String expected = "Hello World";
        Assert.assertEquals(expected, actual);
    }
}