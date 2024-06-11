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
        List<String> strings = Arrays.asList("hello", "world", "!");
        String expected = "helloworld!";
        String actual = StringUtil.join(strings, "");
        Assert.assertEquals(expected, actual);

        strings = Collections.singletonList(null);
        expected = "";
        actual = StringUtil.join(strings, "");
        Assert.assertEquals(expected, actual);

        strings = Arrays.asList("", "world", "!", "hello");
        expected = "";
        actual = StringUtil.join(strings, "");
        Assert.assertEquals(expected, actual);

        strings = Collections.emptyList();
        expected = "";
        actual = StringUtil.join(strings, "");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinIterator() {
        String[] strings = new String[]{"hello", "world", "!"};
        Iterator<String> iterator = Arrays.asList(strings).iterator();
        String expected = "helloworld!";
        String actual = StringUtil.join(iterator, "");
        Assert.assertEquals(expected, actual);

        iterator = Collections.emptyIterator();
        expected = "";
        actual = StringUtil.join(iterator, "");
        Assert.assertEquals(expected, actual);

        iterator = Arrays.asList("", "world", "!", "hello").iterator();
        expected = "";
        actual = StringUtil.join(iterator, "");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJoinArray() {
        String[] strings = new String[]{"hello", "world", "!"};
        String expected = "helloworld!";
        String actual = StringUtil.join(strings, "");
        Assert.assertEquals(expected, actual);

        strings = new String[0];
        expected = "";
        actual = StringUtil.join(strings, "");
        Assert.assertEquals(expected, actual);

        strings = new String[]{"", "world", "!", "hello"};
        expected = "";
        actual = StringUtil.join(strings, "");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPadding() {
        String actual = StringUtil.padding(1);
        String expected = " ";
        Assert.assertEquals(expected, actual);

        actual = StringUtil.padding(21);
        expected = "                 ";
        Assert.assertEquals(expected, actual);

        actual = StringUtil.padding(-1);
        Assert.assertThrows(IllegalArgumentException.class, () -> StringUtil.padding(-1));
    }

    @Test
    public void testIsBlank() {
        String actual = StringUtil.isBlank(null);
        boolean expected = true;
        Assert.assertEquals(expected, actual);

        actual = StringUtil.isBlank("");
        expected = true;
        Assert.assertEquals(expected, actual);

        actual = StringUtil.isBlank(" ");
        expected = true;
        Assert.assertEquals(expected, actual);

        actual = StringUtil.isBlank("\t");
        expected = true;
        Assert.assertEquals(expected, actual);

        actual = StringUtil.isBlank("\r\n");
        expected = true;
        Assert.assertEquals(expected, actual);

        actual = StringUtil.isBlank("hello");
        expected = false;
        Assert.assertEquals(expected, actual);

        actual = StringUtil.isBlank("\thello\r\n ");
        expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsNumeric() {
        String actual = StringUtil.isNumeric(null);
        boolean expected = false;
        Assert.assertEquals(expected, actual);

        actual = StringUtil.isNumeric("");
        expected = false;
        Assert.assertEquals(expected, actual);

        actual = StringUtil.isNumeric("123");
        expected = true;
        Assert.assertEquals(expected, actual);

        actual = StringUtil.isNumeric("123abc");
        expected = false;
        Assert.assertEquals(expected, actual);

        actual = StringUtil.isNumeric("abc123");
        expected = false;
        Assert.assertEquals(expected, actual);

        actual = StringUtil.isNumeric("1e3");
        expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNormaliseWhitespace() {
        String actual = StringUtil.normaliseWhitespace("hello world" + "\t" + "123\r\n" + "456");
        String expected = "hello world 123 456";
        Assert.assertEquals(expected, actual);

        actual = StringUtil.normaliseWhitespace("hello world" + "\t" + "123\r\n" + "456" + "\t");
        expected = "hello world 123 456";
        Assert.assertEquals(expected, actual);

        actual = StringUtil.normaliseWhitespace("hello world" + "\t" + "123\r\n" + "456" + "\t" + "   ");
        expected = "hello world 123 456";
        Assert.assertEquals(expected, actual);

        actual = StringUtil.normaliseWhitespace("  ");
        expected = "";
        Assert.assertEquals(expected, actual);

        actual = StringUtil.normaliseWhitespace("\t");
        expected = "";
        Assert.assertEquals(expected, actual);

        actual = StringUtil.normaliseWhitespace("\r\n");
        expected = "";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWhitespace() {
        Assert.assertTrue(StringUtil.isWhitespace(' '));
        Assert.assertTrue(StringUtil.isWhitespace('\t'));
        Assert.assertTrue(StringUtil.isWhitespace('\n'));
        Assert.assertTrue(StringUtil.isWhitespace('\r'));
        Assert.assertTrue(StringUtil.isWhitespace('\f'));

        Assert.assertFalse(StringUtil.isWhitespace('a'));
        Assert.assertFalse(StringUtil.isWhitespace('b'));
        Assert.assertFalse(StringUtil.isWhitespace('c'));
        Assert.assertFalse(StringUtil.isWhitespace('d'));
    }

    @Test
    public void testIsActuallyWhitespace() {
        Assert.assertTrue(StringUtil.isActuallyWhitespace(' '));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\t'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\n'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\r'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace('\f'));
        Assert.assertTrue(StringUtil.isActuallyWhitespace(160)); // non-breaking space

        Assert.assertFalse(StringUtil.isActuallyWhitespace('a'));
        Assert.assertFalse(StringUtil.isActuallyWhitespace('b'));
        Assert.assertFalse(StringUtil.isActuallyWhitespace('c'));
        Assert.assertFalse(StringUtil.isActuallyWhitespace('d'));
    }

    @Test
    public void testIn() {
        String[] haystack = new String[]{"apple", "banana", "cherry"};
        String needle = "banana";
        boolean expected = true;
        boolean actual = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, actual);

        needle = "grape";
        expected = false;
        actual = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, actual);

        haystack = new String[0];
        actual = StringUtil.in(needle, haystack);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInSorted() {
        String[] haystack = {"apple", "banana", "cherry"};
        String needle = "banana";
        boolean expected = true;
        boolean actual = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(expected, actual);

        haystack = new String[0];
        actual = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(expected, actual);

        haystack = new String[]{"apple", "banana", "cherry", "banana"};
        expected = true;
        actual = StringUtil.inSorted(needle, haystack);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testResolve() throws MalformedURLException {
        String baseUrl = "http://example.com/";
        String relUrl = "/about";
        String expected = "http://example.com/about";
        String actual = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, actual);

        baseUrl = "http://example.com/about";
        relUrl = "/contact";
        expected = "http://example.com/contact";
        actual = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, actual);

        baseUrl = "http://example.com/about";
        relUrl = "/../contact";
        expected = "http://example.com/contact";
        actual = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, actual);

        baseUrl = "http://example.com/about";
        relUrl = "/../../contact";
        expected = "http://example.com/contact";
        actual = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, actual);

        baseUrl = "http://example.com/about";
        relUrl = "/../../../contact";
        expected = "http://example.com/contact";
        actual = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, actual);

        baseUrl = "http://example.com/about";
        relUrl = "../contact";
        expected = "http://example.com/contact";
        actual = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, actual);

        baseUrl = "http://example.com/about";
        relUrl = "/contact";
        expected = "http://example.com/contact";
        actual = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, actual);

        baseUrl = "http://example.com/about";
        relUrl = "contact";
        expected = "http://example.com/contact";
        actual = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, actual);

        baseUrl = "http://example.com/about";
        relUrl = "";
        expected = "http://example.com/about";
        actual = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, actual);

        baseUrl = "http://example.com/about";
        relUrl = null;
        expected = "";
        actual = StringUtil.resolve(baseUrl, relUrl);
        Assert.assertEquals(expected, actual);
    }
}