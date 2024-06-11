package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.StringUtil;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringUtilTest {

    @Test
    public void testJoinCollection() {
        List<String> strings = Arrays.asList("apple", "banana", "cherry");
        String expected = "applebananacherry";
        String result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);

        strings = Collections.singletonList("orange");
        expected = "orange";
        result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);

        strings = Collections.emptyList();
        expected = "";
        result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinIterator() {
        String[] strings = {"apple", "banana", "cherry"};
        String expected = "applebananacherry";
        String result = StringUtil.join(Arrays.asList(strings).iterator(), "");
        Assert.assertEquals(expected, result);

        String[] empty = {};
        expected = "";
        result = StringUtil.join(Arrays.asList(empty).iterator(), "");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testJoinArray() {
        String[] strings = {"apple", "banana", "cherry"};
        String expected = "applebananacherry";
        String result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);

        strings = new String[0];
        expected = "";
        result = StringUtil.join(strings, "");
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPaddingNegativeWidth() {
        StringUtil.padding(-1);
    }

    @Test
    public void testIsBlank() {
        String s1 = "";
        boolean expected1 = true;
        boolean result1 = StringUtil.isBlank(s1);
        Assert.assertEquals(expected1, result1);

        String s2 = null;
        boolean expected2 = true;
        boolean result2 = StringUtil.isBlank(s2);
        Assert.assertEquals(expected2, result2);

        String s3 = " ";
        boolean expected3 = true;
        boolean result3 = StringUtil.isBlank(s3);
        Assert.assertEquals(expected3, result3);

        String s4 = "\t";
        boolean expected4 = true;
        boolean result4 = StringUtil.isBlank(s4);
        Assert.assertEquals(expected4, result4);

        String s5 = "\n";
        boolean expected5 = true;
        boolean result5 = StringUtil.isBlank(s5);
        Assert.assertEquals(expected5, result5);

        String s6 = "   ";
        boolean expected6 = true;
        boolean result6 = StringUtil.isBlank(s6);
        Assert.assertEquals(expected6, result6);

        String s7 = "\r";
        boolean expected7 = true;
        boolean result7 = StringUtil.isBlank(s7);
        Assert.assertEquals(expected7, result7);

        String s8 = "abc";
        boolean expected8 = false;
        boolean result8 = StringUtil.isBlank(s8);
        Assert.assertEquals(expected8, result8);
    }

    @Test
    public void testIsNumeric() {
        String s1 = "";
        boolean expected1 = false;
        boolean result1 = StringUtil.isNumeric(s1);
        Assert.assertEquals(expected1, result1);

        String s2 = null;
        boolean expected2 = false;
        boolean result2 = StringUtil.isNumeric(s2);
        Assert.assertEquals(expected2, result2);

        String s3 = "123";
        boolean expected3 = true;
        boolean result3 = StringUtil.isNumeric(s3);
        Assert.assertEquals(expected3, result3);

        String s4 = "123abc";
        boolean expected4 = false;
        boolean result4 = StringUtil.isNumeric(s4);
        Assert.assertEquals(expected4, result4);

        String s5 = "-123";
        boolean expected5 = true;
        boolean result5 = StringUtil.isNumeric(s5);
        Assert.assertEquals(expected5, result5);

        String s6 = "+123";
        boolean expected6 = true;
        boolean result6 = StringUtil.isNumeric(s6);
        Assert.assertEquals(expected6, result6);
    }

    @Test
    public void testIsWhitespace() {
        int c1 = ' ';
        boolean expected1 = true;
        boolean result1 = StringUtil.isWhitespace(c1);
        Assert.assertEquals(expected1, result1);

        int c2 = '\t';
        boolean expected2 = true;
        boolean result2 = StringUtil.isWhitespace(c2);
        Assert.assertEquals(expected2, result2);

        int c3 = '\n';
        boolean expected3 = true;
        boolean result3 = StringUtil.isWhitespace(c3);
        Assert.assertEquals(expected3, result3);

        int c4 = '\f';
        boolean expected4 = true;
        boolean result4 = StringUtil.isWhitespace(c4);
        Assert.assertEquals(expected4, result4);

        int c5 = '\r';
        boolean expected5 = true;
        boolean result5 = StringUtil.isWhitespace(c5);
        Assert.assertEquals(expected5, result5);

        int c6 = 'a';
        boolean expected6 = false;
        boolean result6 = StringUtil.isWhitespace(c6);
        Assert.assertEquals(expected6, result6);
    }

    @Test
    public void testIsActuallyWhitespace() {
        int c1 = ' ';
        boolean expected1 = true;
        boolean result1 = StringUtil.isActuallyWhitespace(c1);
        Assert.assertEquals(expected1, result1);

        int c2 = '\t';
        boolean expected2 = true;
        boolean result2 = StringUtil.isActuallyWhitespace(c2);
        Assert.assertEquals(expected2, result2);

        int c3 = '\n';
        boolean expected3 = true;
        boolean result3 = StringUtil.isActuallyWhitespace(c3);
        Assert.assertEquals(expected3, result3);

        int c4 = '\f';
        boolean expected4 = true;
        boolean result4 = StringUtil.isActuallyWhitespace(c4);
        Assert.assertEquals(expected4, result4);

        int c5 = '\r';
        boolean expected5 = true;
        boolean result5 = StringUtil.isActuallyWhitespace(c5);
        Assert.assertEquals(expected5, result5);

        int c6 = 160;
        boolean expected6 = true;
        boolean result6 = StringUtil.isActuallyWhitespace(c6);
        Assert.assertEquals(expected6, result6);

        int c7 = 'a';
        boolean expected7 = false;
        boolean result7 = StringUtil.isActuallyWhitespace(c7);
        Assert.assertEquals(expected7, result7);
    }

    @Test
    public void testNormaliseWhitespace() {
        String s1 = "   a\n b  \t c\r\t\n";
        String expected1 = "a b c";
        String result1 = StringUtil.normaliseWhitespace(s1);
        Assert.assertEquals(expected1, result1);
    }

    @Test
    public void testIn() {
        String needle = "apple";
        String[] haystack = {"apple", "banana", "cherry", "apple"};
        boolean expected = true;
        boolean result = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, result);

        needle = "grape";
        result = StringUtil.in(needle, haystack);
        Assert.assertFalse(result);
    }

    @Test
    public void testInSorted() {
        String needle = "apple";
        String[] haystack = {"banana", "cherry", "apple"};
        boolean expected = true;
        boolean result = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(expected, result);

        needle = "grape";
        result = StringUtil.inSorted(needle, haystack);
        Assert.assertFalse(result);
    }

    @Test(expected = MalformedURLException.class)
    public void testResolve() throws MalformedURLException {
        String baseUrl = "http://example.com/";
        String relUrl = "//example.com";
        String result = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals("", result);
    }

    @Test
    public void testResolveAbsolute() throws MalformedURLException {
        String baseUrl = "http://example.com/";
        String relUrl = "/about";
        URL result = StringUtil.resolve(new URL(baseUrl), relUrl);
        String expected = "http://example.com/about";
        Assert.assertEquals(expected, result.toExternalForm());
    }

    @Test
    public void testResolveInvalidBase() throws MalformedURLException {
        String baseUrl = "";
        String relUrl = "/about";
        URL result = StringUtil.resolve(new URL(baseUrl), relUrl);
        Assert.assertEquals("", result.toExternalForm());
    }

    @Test
    public void testResolveInvalidRelative() throws MalformedURLException {
        String baseUrl = "http://example.com/";
        String relUrl = "";
        URL result = StringUtil.resolve(new URL(baseUrl), relUrl);
        Assert.assertEquals(baseUrl, result.toExternalForm());
    }
}