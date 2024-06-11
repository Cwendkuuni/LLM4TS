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
        List<String> strings = Arrays.asList("Hello", "World", "!");
        String expected = "HelloWorld!";
        String actual = StringUtil.join(strings, "");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinIterator() {
        String[] strings = new String[]{"Hello", "World", "!"};
        Iterator<String> iterator = Arrays.asList(strings).iterator();
        String expected = "HelloWorld!";
        String actual = StringUtil.join(iterator, "");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinArray() {
        String[] strings = new String[]{"Hello", "World", "!"};
        String expected = "HelloWorld!";
        String actual = StringUtil.join(strings, "");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPadding() {
        String expected = "   Hello";
        String actual = StringUtil.padding(4) + "Hello";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsBlank() {
        Assert.assertTrue(StringUtil.isBlank(null));
        Assert.assertTrue(StringUtil.isBlank(""));
        Assert.assertTrue(StringUtil.isBlank("   "));
        Assert.assertFalse(StringUtil.isBlank("Hello"));
        Assert.assertFalse(StringUtil.isBlank("Hello World"));
    }

    @Test
    public void testIsNumeric() {
        Assert.assertFalse(StringUtil.isNumeric(null));
        Assert.assertFalse(StringUtil.isNumeric(""));
        Assert.assertFalse(StringUtil.isNumeric("123"));
        Assert.assertTrue(StringUtil.isNumeric("12345"));
        Assert.assertFalse(StringUtil.isNumeric("123abc"));
    }

    @Test
    public void testIsWhitespace() {
        Assert.assertTrue(StringUtil.isWhitespace(' '));
        Assert.assertTrue(StringUtil.isWhitespace('\t'));
        Assert.assertTrue(StringUtil.isWhitespace('\n'));
        Assert.assertTrue(StringUtil.isWhitespace('\r'));
        Assert.assertFalse(StringUtil.isWhitespace('a'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        Assert.assertTrue(StringUtil.isActuallyWhitespace(' '));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\t'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\n'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\r'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(160));
        Assert.assertFalse(StringUtil.isActuallyWhitespace('a'));
    }

    @Test
    public void testNormaliseWhitespace() {
        String expected = "Hello World";
        String actual = StringUtil.normaliseWhitespace("Hello\tWorld");
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegativeWidth() {
        StringUtil.padding(-1);
    }

    @Test
    public void testIn() {
        String needle = "needle";
        String[] haystack = new String[]{"haystack1", "haystack2", needle, "haystack3", needle};
        boolean expected = true;
        boolean actual = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInSorted() {
        String needle = "needle";
        String[] haystack = new String[]{"haystack1", "haystack2", needle, "haystack3"};
        boolean expected = true;
        boolean actual = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolve() {
        String baseUrl = "http://example.com/base.html";
        String relUrl = "/relative.html";
        String expected = "http://example.com/relative.html";
        String actual = StringUtil.resolve(new URL(baseUrl), relUrl);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveInvalidBaseUrl() {
        String baseUrl = "example.com";
        String relUrl = "/relative.html";
        StringUtil.resolve(new URL(baseUrl), relUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolveInvalidRelUrl() {
        String baseUrl = "http://example.com/base.html";
        String relUrl = "relative.html"; // no scheme or host
        StringUtil.resolve(new URL(baseUrl), relUrl);
    }
}